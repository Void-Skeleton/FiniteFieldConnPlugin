package me.void514.finitefieldconnplugin.ui;

import me.void514.finitefieldconnplugin.menu.Menu;
import me.void514.finitefieldconnplugin.menu.MenuListener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RoomMenu extends Menu {
    public static final int MAX_ROOMS = 9;
    public final BoardMenu[] boards;
    public static final ItemStack ROOM_STACK = new ItemStack(Material.BEACON);

    public RoomMenu() {
        super(null, MAX_ROOMS);
        System.out.println("Creating room system");
        boards = new BoardMenu[MAX_ROOMS];
        for (int i = 0; i < boards.length; i ++) {
            boards[i] = new BoardMenu();
            MenuListener.getRegistry().put("board_" + i, boards[i]);
        }
        MenuListener.getRegistry().put("rooms", this);
        resetItems();
        createHandlers();
    }

    private void resetItems() {
        for (int i = 0; i < getInventory().getSize(); i ++) {
            getForceNames()[i] = "Room #" + i + " - click to join";
            getForceAmounts()[i] = i + 1;
            setItem(i, ROOM_STACK);
        }
    }

    private void createHandlers() {
        for (int i = 0; i < getInventory().getSize(); i ++) {
            final int j = i;
            bindClickEventHandler(i, event -> {
                BoardMenu board = boards[j];
                if (board.getBoardState() == BoardMenu.EnumBoardState.WAITING && !board.hasEnoughPlayers()) {
                    board.open((Player) event.getWhoClicked());
                }
            });
            setHandlerActiveAtSlot(i, true);
        }
    }

}
