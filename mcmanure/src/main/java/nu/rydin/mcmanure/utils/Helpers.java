package nu.rydin.mcmanure.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ForgeEventFactory;

public class Helpers {
  public static boolean applyFertilizer(
      final ItemStack itemstack, final Level world, final BlockPos pos, final Player player) {
    final BlockState blockstate = world.getBlockState(pos);
    final int hook = ForgeEventFactory.onApplyBonemeal(player, world, pos, blockstate, itemstack);
    if (hook != 0) {
        return hook > 0;
    }

    if (blockstate.getBlock() instanceof BonemealableBlock) {
      final BonemealableBlock bonemealableblock = (BonemealableBlock) blockstate.getBlock();
      if (bonemealableblock.isValidBonemealTarget(world, pos, blockstate, world.isClientSide)) {
        if (world instanceof ServerLevel) {
          if (bonemealableblock.isBonemealSuccess(world, world.random, pos, blockstate)) {
            bonemealableblock.performBonemeal((ServerLevel) world, world.random, pos, blockstate);
          }

          if (!player.isCreative()) {
            itemstack.shrink(1);
          }
        }
        return true;
      }
    }
    return false;
  }
}
