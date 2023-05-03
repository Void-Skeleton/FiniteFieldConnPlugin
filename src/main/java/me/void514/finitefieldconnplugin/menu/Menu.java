package me.void514.finitefieldconnplugin.menu;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Menu {
    public static final ItemStack FILLER_ITEM = new ItemStack(Material.STAINED_GLASS_PANE);
    private final Inventory inventory;
    protected final Player boundPlayer;
    protected final Map<Integer, Consumer<InventoryClickEvent>> clickHandlers = new HashMap<>();
    protected Consumer<InventoryCloseEvent> closeHandler = null;
    protected final boolean[] handlerActive;
    protected final String[] forceNames;
    protected final int[] forceAmounts;

    /*
    static {
        FILLER_ITEM.setData(new Dye(DyeColor.GRAY));
    }
     */

    public static int slotFor(Point point) {
        return 9 * point.x + point.y;
    }

    public static int slotFor(int x, int y) {
        return 9 * x + y;
    }

    public Menu(Player boundPlayer, int size) {
        this.boundPlayer = boundPlayer;
        this.inventory = Bukkit.createInventory(boundPlayer, size);
        this.handlerActive = new boolean[size];
        this.forceNames = new String[size];
        this.forceAmounts = new int[size];
    }

    public void setItem(int slot, ItemStack stack) {
        if (stack == null) {
            inventory.setItem(slot, null);
            return;
        }
        ItemStack copy = stack.clone();
        if (forceAmounts[slot] != 0) {
            copy.setAmount(forceAmounts[slot]);
        }
        if (forceNames[slot] != null) {
            copy.setLore(Collections.singletonList(forceNames[slot]));
        }
        inventory.setItem(slot, copy);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Player getBoundPlayer() {
        return boundPlayer;
    }

    public void bindClickEventHandler(int slot, Consumer<InventoryClickEvent> handler) {
        clickHandlers.put(slot, handler);
    }

    public Consumer<InventoryClickEvent> getHandlerAtSlot(int slot) {
        return clickHandlers.get(slot);
    }

    public void setHandlerActiveAtSlot(int slot, boolean active) {
        this.handlerActive[slot] = active;
    }

    public boolean isHandlerActiveAtSlot(int slot) {
        return this.handlerActive[slot];
    }

    public void bindMenuClosedHandler(Consumer<InventoryCloseEvent> handler) {
        this.closeHandler = handler;
    }

    public Consumer<InventoryCloseEvent> getCloseHandler() {
        return closeHandler;
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public String[] getForceNames() {
        return forceNames;
    }

    public int[] getForceAmounts() {
        return forceAmounts;
    }
}
