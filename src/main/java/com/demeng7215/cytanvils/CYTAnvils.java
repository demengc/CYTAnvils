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

import java.util.HashMap;
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

			final EnchantmentStorageMeta rightMeta = (EnchantmentStorageMeta) right.getItemMeta();

			try {
				rightMeta.getStoredEnchants();
			} catch (NullPointerException ex) {
				return;
			}

			final Map<Enchantment, Integer> enchants = new HashMap<>(rightMeta.getStoredEnchants());

			if (left.getType() == Material.ENCHANTED_BOOK) {

				final EnchantmentStorageMeta leftMeta = (EnchantmentStorageMeta) left.getItemMeta();

				try {
					leftMeta.getStoredEnchants();
				}catch(NullPointerException ex) {
					return;
				}

				for (Enchantment enchant : leftMeta.getStoredEnchants().keySet())
					if (!enchants.containsKey(enchant))
						enchants.put(enchant, leftMeta.getStoredEnchantLevel(enchant));
					else
						enchants.put(enchant, leftMeta.getStoredEnchantLevel(enchant) +
								rightMeta.getStoredEnchantLevel(enchant));

				final ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
				final EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) left.getItemMeta();

				for (Enchantment enchant : bookMeta.getStoredEnchants().keySet())
					bookMeta.removeStoredEnchant(enchant);

				for (Enchantment enchant : enchants.keySet())
					bookMeta.addStoredEnchant(enchant, enchants.get(enchant), true);

				book.setItemMeta(bookMeta);
				e.setResult(book);
				return;
			}

			for (Enchantment enchant : left.getEnchantments().keySet())
				if (!enchants.containsKey(enchant)) enchants.put(enchant, left.getEnchantmentLevel(enchant));
				else
					enchants.put(enchant, left.getEnchantmentLevel(enchant) + rightMeta.getStoredEnchantLevel(enchant));

			result.addUnsafeEnchantments(enchants);
			e.setResult(result);
		}
	}
}
