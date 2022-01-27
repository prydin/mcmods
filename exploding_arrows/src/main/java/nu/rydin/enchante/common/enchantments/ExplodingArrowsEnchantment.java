package nu.rydin.enchante.common.enchantments;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

public class ExplodingArrowsEnchantment extends Enchantment {
  public ExplodingArrowsEnchantment(final Enchantment.Rarity rarity, final EquipmentSlot... slots) {
    super(rarity, EnchantmentCategory.BOW, slots);
  }

  @Override
  public int getMinCost(final int p_44580_) {
    return 4;
  }

  @Override
  public int getMaxCost(final int p_44582_) {
    return 10;
  }

  @Override
  public int getMaxLevel() {
    return 4;
  }

  @Override
  public void doPostAttack(final LivingEntity attacker, final Entity target, final int level) {
    final Level world = attacker.level;
    if (!attacker.level.isClientSide()) {
      world.explode(
          attacker,
          target.getX(),
          target.getY(),
          target.getZ(),
          ((float) level) / 4 + 1,
          Explosion.BlockInteraction.BREAK);
    }
  }
}
