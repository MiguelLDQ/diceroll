package com.dicegame.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Modelo de domínio para um jogador.
 *
 * Sem anotações JPA (@Entity, @Table) — o Firestore não precisa delas.
 * Os dados são serializados para Map<String, Object> antes de salvar,
 * e desserializados de volta para Player ao ler.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    private String name;
    private int wins   = 0;
    private int losses = 0;

    public Player(String name) {
        this.name = name;
    }

    public int getTotalGames() {
        return wins + losses;
    }

    public double getWinRate() {
        if (getTotalGames() == 0) return 0.0;
        return (double) wins / getTotalGames() * 100;
    }
}
