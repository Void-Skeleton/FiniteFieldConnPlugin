package me.void514.finitefieldconnplugin.menu;

import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class MenuListener implements Listener {
    private static final Map<String, Menu> registry = new HashMap<>();

    public static Map<String, Menu> getRegistry() {
        return registry;
    }

    private static Menu getMenuForInventory(Inventory inventory) {
        for (Menu menu: registry.values()) {
            if (inventory.equals(menu.getInventory())) return menu;
        }
        return null;
    }

    @EventHandler
    public void onMenuItemClicked(InventoryClickEvent event) {
        System.out.println("Menu listener called");
        Inventory clickedInventory = event.getClickedInventory();
        Menu menu;
        if ((menu = getMenuForInventory(clickedInventory)) == null) return;
        event.setCancelled(true);
        int slot = event.getRawSlot();
        if (slot < 0 || slot >= clickedInventory.getSize()) return;
        Consumer<InventoryClickEvent> handler;
        if (!menu.isHandlerActiveAtSlot(slot)) return;
        if ((handler = menu.getHandlerAtSlot(slot)) != null) {
            handler.accept(event);
        }
    }

    @EventHandler
    public void onMenuClosed(InventoryCloseEvent event) {
        System.out.println("Menu listener called");
        Inventory closedInventory = event.getInventory();
        Menu menu;
        if ((menu = getMenuForInventory(closedInventory)) == null) return;
        Consumer<InventoryCloseEvent> closeHandler;
        if ((closeHandler = menu.getCloseHandler()) != null) {
            closeHandler.accept(event);
        }
    }
}
