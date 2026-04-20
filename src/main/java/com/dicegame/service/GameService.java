package com.dicegame.service;

import com.dicegame.model.GameDTOs.*;
import com.dicegame.model.Player;
import com.dicegame.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Camada de serviço — contém toda a lógica de negócio.
 * Agora chama o PlayerRepository que usa Firestore em vez de JPA.
 */
@Service
public class GameService {

    private final PlayerRepository playerRepository;
    private final Random random = new Random();

    public GameService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    /**
     * Processa uma jogada:
     * 1. Busca ou cria o jogador no Firestore pelo nome
     * 2. Sorteia o número-alvo (2–12)
     * 3. Compara soma dos dados com o alvo
     * 4. Atualiza wins/losses no Firestore
     * 5. Retorna o resultado completo
     */
    public PlayResponse play(PlayRequest request) throws ExecutionException, InterruptedException {
        if (request.getDie1() < 1 || request.getDie1() > 6 ||
            request.getDie2() < 1 || request.getDie2() > 6) {
            throw new IllegalArgumentException("Valores dos dados devem estar entre 1 e 6.");
        }

        // Busca o jogador ou cria um novo (findOrCreate)
        Player player = playerRepository.findByName(request.getPlayerName())
                .orElse(new Player(request.getPlayerName()));

        int sum    = request.getDie1() + request.getDie2();
        int target = random.nextInt(11) + 2;
        boolean won = (sum == target);

        if (won) player.setWins(player.getWins() + 1);
        else     player.setLosses(player.getLosses() + 1);

        // Persiste no Firestore
        playerRepository.save(player);

        return new PlayResponse(
                request.getDie1(),
                request.getDie2(),
                sum,
                target,
                won,
                player.getWins(),
                player.getLosses(),
                player.getWinRate()
        );
    }

    /**
     * Retorna o top 10 jogadores por vitórias.
     */
    public List<LeaderboardEntry> getLeaderboard() throws ExecutionException, InterruptedException {
        List<Player> top = playerRepository.findTop10ByWins();

        return IntStream.range(0, top.size())
                .mapToObj(i -> {
                    Player p = top.get(i);
                    return new LeaderboardEntry(
                            i + 1,
                            p.getName(),
                            p.getWins(),
                            p.getLosses(),
                            p.getWinRate()
                    );
                })
                .collect(Collectors.toList());
    }
}
