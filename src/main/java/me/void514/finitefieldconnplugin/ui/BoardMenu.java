package me.void514.finitefieldconnplugin.ui;

import me.void514.finitefieldconnplugin.game.*;
import me.void514.finitefieldconnplugin.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class BoardMenu extends Menu {
    public enum EnumBoardState {
        WAITING, RUNNING, COMPLETE
    }

    public static final int PRIME = 5;
    public static final Point GRID_START = new Point(0, 2);
    public static final Point RESET_BUTTON = new Point(5, 8);
    public static final ItemStack RESET_STACK = new ItemStack(Material.BARRIER);
    public static final ItemStack TRANSFORM_STACK = new ItemStack(Material.ENDER_PEARL);
    private Map<Player, EnumState> playerStates = new HashMap<>();
    private final GameLogic gameLogic;
    private EnumBoardState boardState;
    private EnumState nextState = null;
    public BoardMenu() {
        super(null, 9 * 6);
        System.out.println("Creating board");
        this.createHandlers();
        this.gameLogic = new GameLogic(PRIME);
        this.setBoardState(EnumBoardState.WAITING);
        if (this.hasEnoughPlayers()) {
            this.setBoardState(EnumBoardState.RUNNING);
        }
        this.gameLogic.reset();
        this.resetItems();
        this.setItem(slotFor(RESET_BUTTON), FILLER_ITEM);
        this.setHandlerActiveAtSlot(slotFor(RESET_BUTTON), false);
    }

    public void tryPlayerJoin(Player player, EnumState state) {
        if (this.boardState != EnumBoardState.WAITING) return;
        if (!this.playerStates.containsValue(state)) {
            this.playerStates.put(player, state);
        }
        if (this.hasEnoughPlayers()) this.setBoardState(EnumBoardState.RUNNING);
    }

    private void setBoardState(EnumBoardState boardState) {
        this.boardState = boardState;
        switch (boardState) {
            case WAITING:
                this.gameLogic.reset();
                this.resetItems();
                this.setItem(slotFor(RESET_BUTTON), FILLER_ITEM);
                this.setHandlerActiveAtSlot(slotFor(RESET_BUTTON), false);
                this.nextState = EnumState.CROSS;
                setAllPointHandlersActive(false);
                break;
            case RUNNING:
                setAllPointHandlersActive(true);
                break;
            case COMPLETE:
                setAllPointHandlersActive(false);
                this.setItem(slotFor(RESET_BUTTON), RESET_STACK);
                this.setHandlerActiveAtSlot(slotFor(RESET_BUTTON), true);
                break;
        }
    }

    public EnumBoardState getBoardState() {
        return boardState;
    }

    public void onPlayerLeave(Player player) {
        if (!playerStates.containsKey(player)) return;
        playerStates.remove(player);
        if (!this.hasEnoughPlayers()) {
            this.setBoardState(EnumBoardState.WAITING);
        }
    }

    public boolean hasEnoughPlayers() {
        return playerStates.size() >= 2;
    }

    private void setAllPointHandlersActive(boolean active) {
        for (FpP2Pos pos: FpP2Pos.allPointsForPrime(PRIME)) {
            this.setHandlerActiveAtSlot(getIndex(pos), active);
        }
    }

    private static int slopeFor(FpP2Pos pos) {
        if (pos.getZ() != 0) throw new IllegalArgumentException();
        if (pos.getX() == 0) return FpUtil.INFINITY;
        else return FpUtil.normalize(PRIME, pos.getY() * FpUtil.invert(PRIME, pos.getX()));
    }

    private static int getIndex(FpP2Pos pos) {
        if (pos.getPrime() != PRIME) return 0;
        /*
        FpP2Pos normal = pos.normalize();
        if (pos.getZ() != 0) {
            int x = FpUtil.normalize(PRIME, normal.getX());
            int y = FpUtil.normalize(PRIME, normal.getY());
            // finite point
            return slotFor(GRID_START.x + x + 1, GRID_START.y + y);
        } else if (pos.getY() != 0) {
            // slope point
            // x = ay, (a, 1, 0)
            int a = FpUtil.normalize(PRIME, normal.getX());
            return slotFor(GRID_START.x + a + 1, GRID_START.y + PRIME);
        } else {
            return slotFor(GRID_START.x, GRID_START.y + PRIME);
        }
         */
        final int INVALID = 114514;
        int x, y;
        x = y = INVALID;
        pos = pos.deepNormalize();
        if (pos.getZ() != 0) {
            x = GRID_START.x + 1 + (PRIME - 1 - pos.getY());
            y = GRID_START.y + pos.getX();
        } else {
            int slope = slopeFor(pos);
            if (slope == FpUtil.INFINITY) {
                x = GRID_START.x;
                y = GRID_START.y + PRIME;
            } else {
                x = GRID_START.x + 1 + (PRIME - 1 - slope);
                y = GRID_START.y + PRIME;
            }
        }
        return slotFor(x, y);
    }

    private void resetItems() {
        for (int i = 0; i < getInventory().getSize(); i ++) {
            setItem(i, Menu.FILLER_ITEM.clone());
        }
        for (FpP2Pos pos: FpP2Pos.allPointsForPrime(PRIME)) {
            FpP2Pos normal = pos.deepNormalize();
            if (normal.getZ() == 1) {
                getForceNames()[getIndex(pos)] = "Finite point " + normal.toString();
            } else {
                int slope = slopeFor(pos);
                if (slope != FpUtil.INFINITY) {
                    slope = slope == 0 ? 10 : slope;
                    getForceNames()[getIndex(pos)] = "Infinite point " + normal.toString()
                            + ", slope = " + slope;
                    getForceAmounts()[getIndex(pos)] = slope;
                } else {
                    getForceNames()[getIndex(pos)] = "Infinite point " + normal.toString()
                            + ", slope = infinity";
                    getForceAmounts()[getIndex(pos)] = 8;
                }
            }
            setItem(getIndex(pos), EnumState.NULL_STACK);
        }
    }

    private void flipState() {
        switch (nextState) {
            case CROSS: nextState = EnumState.CIRCLE; break;
            case CIRCLE: nextState = EnumState.CROSS; break;
        }
    }

    private void createHandlers() {
        for (FpP2Pos pos: FpP2Pos.allPointsForPrime(PRIME)) {
            this.bindClickEventHandler(getIndex(pos), (event -> {
                if (gameLogic == null) return;
                EnumState state = playerStates.get((Player) event.getWhoClicked());
                if (state == null || state != nextState) return;
                if (gameLogic.getStateAt(pos) == null) {
                    flipState();
                    FpP2Line victoryLine = gameLogic.setStateWithCheck(pos, state);
                    this.setItem(getIndex(pos), EnumState.getDisplayItem(state));
                    if (victoryLine != null) {
                        for (FpP2Pos linePos: victoryLine) {
                            if (gameLogic.getStateAt(linePos) == state) {
                                this.setItem(getIndex(linePos), state.getHighlightItem());
                            }
                        }
                        setBoardState(EnumBoardState.COMPLETE);
                    }
                }
            }));
            this.setHandlerActiveAtSlot(getIndex(pos), true);
        }
        this.bindClickEventHandler(slotFor(RESET_BUTTON), (event -> {
            this.setBoardState(EnumBoardState.WAITING);
            if (this.hasEnoughPlayers()) {
                this.setBoardState(EnumBoardState.RUNNING);
            }
        }));
        this.bindMenuClosedHandler(event -> {
            onPlayerLeave((Player) event.getPlayer());
        });
    }

    public Map<Player, EnumState> getPlayerStates() {
        return playerStates;
    }

    public GameLogic getGameLogic() {
        return gameLogic;
    }

    @Override
    public void open(Player player) {
        super.open(player);
        EnumState assignedState = null;
        for (EnumState state: EnumState.values()) {
            if (!playerStates.containsValue(state)) {
                assignedState = state;
                break;
            }
        }
        if (assignedState != null) tryPlayerJoin(player, assignedState);
        else player.closeInventory();
    }
}
