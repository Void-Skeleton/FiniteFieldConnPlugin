package me.void514.finitefieldconnplugin.game;

import me.void514.finitefieldconnplugin.game.FpP2Pos;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;

public class FpP2Grid<E> {
    private final int prime;
    private final Map<FpP2Pos, E> grid;

    public FpP2Grid(int prime) {
        this.prime = prime;
        this.grid = new HashMap<>(prime * prime + prime + 1);
        for (FpP2Pos pos: FpP2Pos.allPointsForPrime(prime)) {
            grid.put(pos, null);
        }
    }

    public E getAt(FpP2Pos pos) {
        return grid.get(pos);
    }

    public E setAt(FpP2Pos pos, E val) {
        return grid.put(pos, val);
    }

}
