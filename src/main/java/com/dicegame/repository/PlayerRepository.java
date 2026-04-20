package com.dicegame.repository;

import com.dicegame.model.Player;
import com.google.cloud.firestore.*;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Repositório Firestore para a coleção "players".
 *
 * Cada jogador é salvo como um Document cujo ID é o próprio nome.
 * Ex: players/Miguel → { name: "Miguel", wins: 3, losses: 7 }
 *
 * Não estendemos JpaRepository — fazemos as operações diretamente
 * via SDK do Firestore.
 */
@Repository
public class PlayerRepository {

    // Nome da coleção no Firestore
    private static final String COLLECTION = "players";

    private final Firestore db;

    public PlayerRepository(Firestore db) {
        this.db = db;
    }

    /**
     * Busca um jogador pelo nome.
     * Retorna Optional.empty() se não existir.
     */
    public Optional<Player> findByName(String name) throws ExecutionException, InterruptedException {
        DocumentSnapshot doc = db.collection(COLLECTION).document(name).get().get();

        if (!doc.exists()) return Optional.empty();

        Player player = new Player();
        player.setName(doc.getString("name"));
        player.setWins(Objects.requireNonNull(doc.getLong("wins")).intValue());
        player.setLosses(Objects.requireNonNull(doc.getLong("losses")).intValue());
        return Optional.of(player);
    }

    /**
     * Salva (cria ou atualiza) um jogador.
     * O ID do documento é o nome do jogador — simples e sem colisão.
     */
    public void save(Player player) throws ExecutionException, InterruptedException {
        Map<String, Object> data = new HashMap<>();
        data.put("name",   player.getName());
        data.put("wins",   player.getWins());
        data.put("losses", player.getLosses());

        // set() cria ou substitui o documento inteiro
        db.collection(COLLECTION).document(player.getName()).set(data).get();
    }

    /**
     * Retorna os 10 jogadores com mais vitórias, ordenados de forma decrescente.
     * O Firestore exige que índices compostos sejam criados no console para
     * queries com orderBy — aqui só ordenamos por um campo, então funciona direto.
     */
    public List<Player> findTop10ByWins() throws ExecutionException, InterruptedException {
        QuerySnapshot snapshot = db.collection(COLLECTION)
                .orderBy("wins", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .get();

        return snapshot.getDocuments().stream()
                .map(doc -> {
                    Player p = new Player();
                    p.setName(doc.getString("name"));
                    p.setWins(Objects.requireNonNull(doc.getLong("wins")).intValue());
                    p.setLosses(Objects.requireNonNull(doc.getLong("losses")).intValue());
                    return p;
                })
                .collect(Collectors.toList());
    }
}
