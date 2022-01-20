package nu.rydin.mcmanure.common.events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import nu.rydin.mcmanure.common.init.ItemsInit;
import nu.rydin.mcmanure.utils.Helpers;

@EventBusSubscriber
public class ManureFertilizationEvent {
  @SubscribeEvent
  public void onKelpUse(final PlayerInteractEvent.RightClickBlock e) {
    final Level world = e.getWorld();
    if (world.isClientSide) {
      return;
    }

    final ItemStack itemstack = e.getItemStack();
    if (!itemstack.getItem().equals(ItemsInit.COW_MANURE_ITEM.get())) {
      return;
    }

    final Player player = e.getPlayer();
    final BlockPos cpos = e.getPos();
    if (Helpers.playerApplyFertilizer(itemstack, world, cpos, player)) {
      world.levelEvent(2005, cpos, 0);
      player.swing(e.getHand());
    }
  }
}
