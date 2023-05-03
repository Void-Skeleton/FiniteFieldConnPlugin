package me.void514.finitefieldconnplugin.ui;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class RoomMenuListener implements Listener {
    private final RoomMenu menu = new RoomMenu();

    @EventHandler
    public void onItemClicked(PlayerInteractEvent event) {
        System.out.println("RoomMenuListener called");
        System.out.println(event.getAction());
        System.out.println(event.getItem());
        if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (event.getItem().getType().equals(Material.BOOK)) {
                menu.open(event.getPlayer());
            }
        }
    }
}
