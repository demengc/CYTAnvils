package com.demeng7215.cytanvils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public final class CYTAnvils extends JavaPlugin implements Listener {

	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable() {
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPrepareAnvil(PrepareAnvilEvent e) {

		if (e.getInventory().getItem(0) != null &&
				e.getInventory().getItem(1) != null &&
				e.getResult() != null) {

			final ItemStack left = e.getInventory().getItem(0);
			final ItemStack right = e.getInventory().getItem(1);
			final ItemStack result = e.getResult();

			if (right.getType() != Material.ENCHANTED_BOOK) return;

			final EnchantmentStorageMeta meta = (EnchantmentStorageMeta) right.getItemMeta();
			final Map<Enchantment, Integer> enchantments = meta.getStoredEnchants();

			result.addUnsafeEnchantments(enchantments);

			e.setResult(result);
		}
	}
}
