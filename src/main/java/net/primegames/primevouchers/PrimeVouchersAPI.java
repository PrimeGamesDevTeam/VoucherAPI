package net.primegames.primevouchers;


import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public final class PrimeVouchersAPI  implements Listener {
    public static final String TAG_KEY = "primevouchers";
    @Getter
    private final JavaPlugin plugin;
    @Getter
    private static PrimeVouchersAPI instance;
    private Map<String, VoucherCallback> vouchers;

    public static PrimeVouchersAPI init(JavaPlugin plugin) {
        if (instance == null) {
            instance = new PrimeVouchersAPI(plugin);
        }
        return instance;
    }

    private PrimeVouchersAPI(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public ItemStack createVoucher(VoucherCallback callback) {
        NBTItem nbtItem = new NBTItem(callback.getItem());
        nbtItem.setObject(TAG_KEY, callback);
        return nbtItem.getItem();
    }


    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        ItemStack itemStack = event.getItem();
        if (itemStack == null  || itemStack.getAmount() < 1) return;
        NBTItem nbtItem = new NBTItem(itemStack);
        if (!nbtItem.hasKey(TAG_KEY)) return;
        VoucherCallback callback = nbtItem.getObject(TAG_KEY, VoucherCallback.class);
        if (callback == null) return;
        if (callback.run(event.getPlayer(), event.getHand())) {
            if (itemStack.getAmount() > 1) {
                itemStack.setAmount(itemStack.getAmount() - 1);
                event.getPlayer().getInventory().setItemInMainHand(itemStack);
            } else {
                event.getPlayer().getInventory().setItemInMainHand(null);
            }
        }
    }

}
