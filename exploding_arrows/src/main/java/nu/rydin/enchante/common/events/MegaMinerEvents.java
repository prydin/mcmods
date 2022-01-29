package nu.rydin.enchante.common.events;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nu.rydin.enchante.common.Main;
import nu.rydin.enchante.common.enchantments.ModEnchantments;
import nu.rydin.enchante.utils.FeatureDetector;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MegaMinerEvents {
  private static class PlayerEventHandler {
    private final Player player;

    private LinkedList<BlockPos> pendingBlocks = new LinkedList<>();

    public PlayerEventHandler(final Player player) {
      this.player = player;
    }

    private void onPlayerTick(final TickEvent.PlayerTickEvent e) {
      if (pendingBlocks.isEmpty()) {
        return;
      }
      if (e.player.level.isClientSide()) {
        final BlockPos pos = pendingBlocks.getFirst();
        final BlockState bs = e.player.level.getBlockState(pos);
        final Block block = bs.getBlock();
        final SoundType sound = block.getSoundType(bs, e.player.getLevel(), pos, null);
        e.player.level.playLocalSound(
            pos.getX(),
            pos.getY(),
            pos.getZ(),
            sound.getBreakSound(),
            SoundSource.BLOCKS,
            1.0F,
            1.0F,
            true);
      } else {
        if (pendingBlocks.isEmpty()) {
          // Should not happen. Just a safeguard.
          return;
        }
        final BlockPos pos = pendingBlocks.pop();
        if (pendingBlocks.isEmpty()) {
          // Did we pop the last block? Let's deregister ourselves.
          MegaMinerEvents.players.remove(player.getUUID());
        }
        final Level world = player.level;
        final BlockState bs = world.getBlockState(pos);

        final Block block = bs.getBlock();
        final SoundType sound = block.getSoundType(bs, player.getLevel(), pos, null);
        player
            .getLevel()
            .playSound(player, pos, sound.getBreakSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
        ((ServerPlayer) player).gameMode.destroyBlock(pos);
      }
    }

    private void onBlockBreak(final BlockEvent.BreakEvent e) {
      if (!pendingBlocks.isEmpty()) {
        return; // Mega mining already in progress
      }
      final BlockState bs = e.getState();
      final Player player = e.getPlayer();
      final Item tool = player.getMainHandItem().getItem();

      // This only works for Pickaxes and ores or Axes and logs. Everything else
      // is handled the vanilla way.
      if (tool instanceof PickaxeItem) {
        if (bs.is(Tags.Blocks.ORES)) {
          pendingBlocks = FeatureDetector.detectOreVein(e.getWorld(), e.getPos());
        }
      } else if (tool instanceof AxeItem) {
        if (bs.getBlock().getRegistryName().toString().endsWith(("_log"))) {
          pendingBlocks = FeatureDetector.detectTreeTrunk(e.getWorld(), e.getPos());
        }
      }
    }
  }

  private static final Map<UUID, PlayerEventHandler> players = new HashMap<>();

  @SubscribeEvent
  public static void onBlockBreak(final BlockEvent.BreakEvent e) {
    final BlockState bs = e.getState();
    final Player player = e.getPlayer();
    final ItemStack itemStack = player.getMainHandItem();
    final Item tool = itemStack.getItem();

    boolean megaMiner = false;
    for (final Tag t : itemStack.getEnchantmentTags()) {
      final CompoundTag ct = (CompoundTag) t;
      if (ct.getString("id")
          .equals(ModEnchantments.MEGA_MINER.get().getRegistryName().toString())) {
        megaMiner = true;
        break;
      }
    }
    if (!megaMiner) {
      return;
    }
    PlayerEventHandler peh = MegaMinerEvents.players.get(player.getUUID());
    if (peh == null) {
      peh = new PlayerEventHandler(player);
      MegaMinerEvents.players.put(player.getUUID(), peh);
    }
    peh.onBlockBreak(e);
  }

  @SubscribeEvent
  public static void onPlayerTick(final TickEvent.PlayerTickEvent e) {
    final PlayerEventHandler peh = MegaMinerEvents.players.get(e.player.getUUID());
    if (peh == null) {
      return;
    }
    peh.onPlayerTick(e);
  }
}
