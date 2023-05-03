package me.void514.finitefieldconnplugin.game;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class GameLogic {

    private final int prime;
    private final FpP2Grid<EnumState> grid;

    public GameLogic(int prime) {
        this.prime = prime;
        grid = new FpP2Grid<>(prime);
    }

    public int getPrime() {
        return prime;
    }

    public EnumState getStateAt(FpP2Pos pos) {
        return grid.getAt(pos);
    }

    private boolean checkLine(FpP2Line line, EnumState state) {
        int currentCount = 0;
//        int oppositeCount = 0;
        for (FpP2Pos pos: line) {
            EnumState state1 = getStateAt(pos);
            if (state1 == state) currentCount ++;
//            else if (state1 != null) oppositeCount ++;
            System.out.println("Checked point " + pos + ", state = " + state1);
        }
//        System.out.printf("Current count = %d, opposite count = %d\n", currentCount, oppositeCount);
        return currentCount >= 5;
    }

    /**
     * @return the line where victory is achieved
     */
    public FpP2Line setStateAt(FpP2Pos pos, EnumState state, boolean checkVictory) {
        System.out.println("Logical set state occured: " + pos + " " + state);
        grid.setAt(pos, state);
        if (checkVictory) {
            for (Iterator<FpP2Line> it = pos.getAllContainingLines(); it.hasNext(); ) {
                System.out.println("Checking new line");
                FpP2Line line = it.next();
                if (checkLine(line, state)) return line;
            }
        }
        return null;
    }

    public void reset() {
        for (FpP2Pos pos: FpP2Pos.allPointsForPrime(prime)) {
            setStateSilently(pos, null);
        }
    }

    public FpP2Line setStateWithCheck(FpP2Pos pos, EnumState state) {
        return setStateAt(pos, state, true);
    }

    public void setStateSilently(FpP2Pos pos, EnumState state) {
        setStateAt(pos, state, false);
    }
}
