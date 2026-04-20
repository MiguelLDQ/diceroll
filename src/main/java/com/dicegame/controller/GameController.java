package com.dicegame.controller;

import com.dicegame.model.GameDTOs.*;
import com.dicegame.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Controller REST — define os endpoints da API.
 *
 * Endpoints:
 *   POST /api/game/play   → processa uma jogada
 *   GET  /api/leaderboard → retorna o ranking top 10
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/game/play")
    public ResponseEntity<PlayResponse> play(@RequestBody PlayRequest request) {
        try {
            return ResponseEntity.ok(gameService.play(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (ExecutionException | InterruptedException e) {
            // Erro de comunicação com o Firestore
            Thread.currentThread().interrupt();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<LeaderboardEntry>> leaderboard() {
        try {
            return ResponseEntity.ok(gameService.getLeaderboard());
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.internalServerError().build();
        }
    }
}
