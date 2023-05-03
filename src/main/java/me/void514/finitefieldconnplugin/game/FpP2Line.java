package me.void514.finitefieldconnplugin.game;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FpP2Line implements Iterable<FpP2Pos> {
    private final int prime;
    private final int a;
    private final int b;
    private final int c;
    // ax+by+cz=0

    public FpP2Line(FpP2Pos point1, FpP2Pos point2) {
        if (point1.getPrime() != point2.getPrime()) throw new IllegalArgumentException();
        if (point1.equals(point2)) throw new IllegalArgumentException();
        this.prime = point1.getPrime();
        int x1, x2, y1, y2, z1, z2;
        x1 = point1.getX(); y1 = point1.getY(); z1 = point1.getZ();
        x2 = point2.getX(); y2 = point2.getY(); z2 = point1.getZ();
        this.a = y1 * z2 - y2 * z1;
        this.b = z1 * x2 - z2 * x1;
        this.c = x1 * y2 - x2 * y1;
    }

    public FpP2Line(int prime, int a, int b, int c) {
        this.prime = prime;
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public boolean contains(FpP2Pos pos) {
        if (pos.getPrime() != prime) return false;
        int val = a * pos.getX() + b * pos.getY() + c * pos.getZ();
        return val % prime == 0;
    }

    @Override
    public Iterator<FpP2Pos> iterator() {
        return FpP2Pos.allPointsForPrime(prime).stream().filter(this::contains).iterator();
    }

    private static final Map<Integer, List<FpP2Line>> ALL_LINES_CACHE = new HashMap<>();

    public static List<FpP2Line> allLinesForPrime(int prime) {
        if (ALL_LINES_CACHE.containsKey(prime)) return ALL_LINES_CACHE.get(prime);
        else {
            List<FpP2Line> list = FpP2Pos.allPointsForPrime(prime).stream().map(FpP2Pos::dualLine).collect(Collectors.toList());
            ALL_LINES_CACHE.put(prime, list);
            return list;
        }
    }
}
