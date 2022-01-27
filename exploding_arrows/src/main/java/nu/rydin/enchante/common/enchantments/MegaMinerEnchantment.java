package nu.rydin.enchante.common.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class MegaMinerEnchantment extends Enchantment {
  public MegaMinerEnchantment(final Rarity rarity, final EquipmentSlot... slots) {
    super(rarity, EnchantmentCategory.DIGGER, slots);
  }

  @Override
  public int getMinCost(final int p_44580_) {
    return 30;
  }

  @Override
  public int getMaxLevel() {
    return 1;
  }

  @Override
  public boolean canEnchant(final ItemStack item) {
    return item.getItem() instanceof PickaxeItem || item.getItem() instanceof AxeItem;
  }
}
