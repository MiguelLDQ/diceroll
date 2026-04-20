package com.dicegame.model;

import lombok.Data;
import lombok.AllArgsConstructor;

/**
 * DTOs (Data Transfer Objects) — objetos simples usados para
 * receber dados do frontend e enviar respostas de volta.
 * Separar DTOs da entidade é boa prática: evita expor campos
 * internos do banco de dados diretamente na API.
 */
public class GameDTOs {

    /**
     * Corpo do POST /api/game/play
     * O frontend manda: nome do jogador + valores dos dois dados
     */
    @Data
    public static class PlayRequest {
        private String playerName;  // Ex: "Miguel"
        private int die1;           // Valor entre 1 e 6
        private int die2;           // Valor entre 1 e 6
    }

    /**
     * Resposta do POST /api/game/play
     * O backend devolve o resultado da jogada + stats atualizadas do jogador
     */
    @Data
    @AllArgsConstructor
    public static class PlayResponse {
        private int die1;            // Dado 1 enviado
        private int die2;            // Dado 2 enviado
        private int sum;             // Soma dos dados
        private int target;          // Número sorteado pelo backend
        private boolean won;         // true se sum == target
        private int totalWins;       // Vitórias acumuladas do jogador
        private int totalLosses;     // Derrotas acumuladas do jogador
        private double winRate;      // Taxa de vitória em %
    }

    /**
     * Entrada do leaderboard — retornado em GET /api/leaderboard
     */
    @Data
    @AllArgsConstructor
    public static class LeaderboardEntry {
        private int    rank;        // Posição no ranking (1, 2, 3...)
        private String name;        // Nome do jogador
        private int    wins;        // Total de vitórias
        private int    losses;      // Total de derrotas
        private double winRate;     // Taxa de vitória em %
    }
}
