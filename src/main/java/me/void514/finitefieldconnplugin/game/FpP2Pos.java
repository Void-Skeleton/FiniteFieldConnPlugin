package me.void514.finitefieldconnplugin.game;

import java.util.*;

import static me.void514.finitefieldconnplugin.game.FpUtil.*;

public class FpP2Pos {
    private final int prime;
    private final int x;
    private final int y;
    private final int z;

    public FpP2Pos(int prime, int x, int y, int z) {
        this.prime = prime;
        this.x = x;
        this.y = y;
        this.z = z;
        if ((x | y | z) == 0) throw new IllegalArgumentException("Zero projective point");
    }

    private static final Map<Integer, List<FpP2Pos>> ALL_POINTS_CACHE = new HashMap<>();

    public static List<FpP2Pos> allPointsForPrime(int prime) {
        if (ALL_POINTS_CACHE.containsKey(prime)) return ALL_POINTS_CACHE.get(prime);
        else {
            List<FpP2Pos> list = allPointsForPrime0(prime);
            ALL_POINTS_CACHE.put(prime, list);
            return list;
        }
    }

    private static List<FpP2Pos> allPointsForPrime0(int prime) {
        List<FpP2Pos> list = new ArrayList<>(prime * prime + prime + 1);
        for (int x = 0; x < prime; x ++) {
            for (int y = 0; y < prime; y ++) {
                list.add(fromXY(prime, x, y));
            }
            list.add(fromXZ(prime, x, 0));
        }
        list.add(fromYZ(prime, 0, 0));
        return Collections.unmodifiableList(list);
    }

    public static FpP2Pos fromXY(int prime, int x, int y) {
        return new FpP2Pos(prime, x, y, 1);
    }

    public static FpP2Pos fromXZ(int prime, int x, int z) {
        return new FpP2Pos(prime, x, 1, z);
    }

    public static FpP2Pos fromYZ(int prime, int y, int z) {
        return new FpP2Pos(prime, 1, y, z);
    }

    public FpP2Pos scale(int factor) {
        if (isCongruent(prime, factor, 0)) throw new IllegalArgumentException();
        return new FpP2Pos(prime, x * factor, y * factor, z * factor);
    }

    public FpP2Pos yScale(int factor) {
        if (isCongruent(prime, factor, 0)) throw new IllegalArgumentException();
        return new FpP2Pos(prime, x, y * factor, z * factor);
    }

    public FpP2Pos normalize() {
        if (this.z != 0) return scale(invert(prime, z));
        else if (this.y != 0) return scale(invert(prime, y));
        return scale(invert(prime, x));
    }

    public FpP2Pos deepNormalize() {
        FpP2Pos normal = this.normalize();
        return new FpP2Pos(prime, FpUtil.normalize(prime, normal.getX()), FpUtil.normalize(prime, normal.getY()), FpUtil.normalize(prime, normal.getZ()));
    }

    public FpP2Line dualLine() {
        return new FpP2Line(prime, x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        final int STUPID_VALUE = 114514;
        if (this == o) return true;
        if (!(o instanceof FpP2Pos)) return false;
        FpP2Pos other = (FpP2Pos) o;
        if (this.prime != other.prime) return false;
        int potentialMultiple = STUPID_VALUE;
        if (this.x != 0) potentialMultiple = other.x * invert(prime, this.x);
        else if (this.y != 0) potentialMultiple = other.y * invert(prime, this.y);
        else if (this.z != 0) potentialMultiple = other.z * invert(prime, this.z);
        int expectedX, expectedY, expectedZ;
        expectedX = potentialMultiple * x;
        expectedY = potentialMultiple * y;
        expectedZ = potentialMultiple * z;
//        System.out.printf("p = %d, ex = %d, ey = %d, ez = %d \n", potentialMultiple, expectedX, expectedY, expectedZ);
        return isCongruent(prime, other.x, expectedX) &&
                isCongruent(prime, other.y, expectedY) &&
                isCongruent(prime, other.z, expectedZ);
    }

    public Iterator<FpP2Line> getAllContainingLines() {
        return FpP2Line.allLinesForPrime(prime).stream().filter(l -> l.contains(this)).iterator();
    }

    @Override
    public int hashCode() {
        FpP2Pos normal = normalize();
        return Objects.hash(prime, FpUtil.normalize(prime, normal.x + normal.y + normal.z));
    }

    public int getPrime() {
        return prime;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public String toString() {
        return String.format("[%d : %d : %d]", x, y, z);
    }
}
