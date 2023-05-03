package me.void514.finitefieldconnplugin;

import me.void514.finitefieldconnplugin.game.FpP2Pos;
import me.void514.finitefieldconnplugin.menu.MenuListener;
import me.void514.finitefieldconnplugin.ui.RoomMenuListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class FFCon extends JavaPlugin {
    private static FFCon INSTANCE = null;

    public static FFCon getInstance() {
        return INSTANCE;
    }

    @Override
    public void onLoad() {
//        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);
        Bukkit.getPluginManager().registerEvents(new RoomMenuListener(), this);
        FpP2Pos p1, p2;
        p1 = new FpP2Pos(5, 4, 1, 0);
        p2 = new FpP2Pos(5, 4, 0, 1);
        System.err.printf("%s = %s: %s\n", p1, p2, p1.equals(p2));
        getLogger().info("Event registered");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
