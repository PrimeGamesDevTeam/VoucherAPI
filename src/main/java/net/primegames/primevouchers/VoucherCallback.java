package net.primegames.primevouchers;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public abstract class VoucherCallback {

    @Getter
    private final ItemStack item;

    public VoucherCallback(ItemStack item) {
        this.item = item;
    }

    public abstract boolean run(Player player, EquipmentSlot slot);
}
