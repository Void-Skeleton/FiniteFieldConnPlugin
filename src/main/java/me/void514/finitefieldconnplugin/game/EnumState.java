package me.void514.finitefieldconnplugin.game;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum EnumState {
    CROSS("cross", new ItemStack(Material.REDSTONE), new ItemStack(Material.REDSTONE_BLOCK), DyeColor.RED),
    CIRCLE("circle", new ItemStack(Material.EMERALD), new ItemStack(Material.EMERALD_BLOCK), DyeColor.LIME);

    private final String name;
    private final ItemStack item;
    private final ItemStack highlightItem;
    private final DyeColor stainColor;
    public static final ItemStack NULL_STACK = new ItemStack(Material.STAINED_GLASS);

    /*
    static {
        NULL_STACK.setData(new Dye(DyeColor.WHITE));
    }
     */

    EnumState(String name, ItemStack item, ItemStack highlightItem, DyeColor stainColor) {
        this.name = name;
        this.item = item;
        this.highlightItem = highlightItem;
        this.stainColor = stainColor;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static EnumState fromName(String name) {
        if ("null".equals(name)) return null;
        for (EnumState state: values()) {
            if (state.name.equals(name)) return state;
        }
        throw new IllegalArgumentException();
    }

    public static ItemStack getDisplayItem(EnumState state) {
        if (state == null) return NULL_STACK.clone();
        else return state.item.clone();
    }

    public ItemStack getHighlightItem() {
        return highlightItem;
    }

    public DyeColor getStainColor() {
        return stainColor;
    }
}
