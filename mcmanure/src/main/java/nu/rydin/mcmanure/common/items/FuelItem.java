package nu.rydin.mcmanure.common.items;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

public class FuelItem extends BaseItem {
  private final int burnTime;

  public FuelItem(final int burnTime) {
    this.burnTime = burnTime;
  }

  @Override
  public int getBurnTime(final ItemStack itemStack, @Nullable final RecipeType<?> recipeType) {
    return burnTime;
  }
}
