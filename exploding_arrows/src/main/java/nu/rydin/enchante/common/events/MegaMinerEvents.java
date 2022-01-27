package nu.rydin.enchante.common.events;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
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

import java.util.LinkedList;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MegaMinerEvents {
  private static final class BlockToBreak {

    private final BlockPos pos;

    private final Player player;

    private final Block wantedBlock;

    public BlockToBreak(final BlockPos pos, final Player player, final Block wantedBlock) {
      this.pos = pos;
      this.player = player;
      this.wantedBlock = wantedBlock;
    }
  }

  private static final LinkedList<BlockToBreak> queue = new LinkedList<>();

  private static SoundEvent pendingSound;

  @SubscribeEvent
  public static void onBlockBreak(final BlockEvent.BreakEvent e) {
    final BlockState bs = e.getState();
    final Player player = e.getPlayer();
    final Item tool = player.getMainHandItem().getItem();

    // This only works for Pickaxes and ores or Axes and logs. Everyhing else
    // is handled the vanilla way.
    if (tool instanceof PickaxeItem) {
      if (!bs.is(Tags.Blocks.ORES)) {
        return;
      }
    } else if (tool instanceof AxeItem) {
      if (!bs.getBlock().getRegistryName().toString().endsWith(("_log"))) {
        return;
      }
    } else {
      return;
    }
    final LevelAccessor world = e.getWorld();
    if (world.isClientSide()) {
      return;
    }
    for (final Tag t : player.getMainHandItem().getEnchantmentTags()) {
      final CompoundTag ct = (CompoundTag) t;
      if (ct.getString("id")
          .equals(ModEnchantments.MEGA_MINER.get().getRegistryName().toString())) {
        MegaMinerEvents.mineNeighborhood(e.getPos(), player, e.getState().getBlock());
      }
    }
  }

  @SubscribeEvent
  public static void onClientTick(final TickEvent.PlayerTickEvent e) {
    if (MegaMinerEvents.queue.isEmpty()) {
      return;
    }
    final BlockToBreak b = MegaMinerEvents.queue.getFirst();
    final BlockState bs = e.player.level.getBlockState(b.pos);
    final Block block = bs.getBlock();
    final SoundType sound = block.getSoundType(bs, b.player.getLevel(), b.pos, null);
    e.player.level.playLocalSound(
        b.pos.getX(),
        b.pos.getY(),
        b.pos.getZ(),
        sound.getBreakSound(),
        SoundSource.BLOCKS,
        1.0F,
        1.0F,
        true);
  }

  @SubscribeEvent
  public static void onServerTick(final TickEvent.ServerTickEvent e) {

    // Pull blocks until we get the right kind or the queue is empty
    BlockToBreak b = null;
    for (; ; ) {
      if (MegaMinerEvents.queue.isEmpty()) {
        return;
      }
      b = MegaMinerEvents.queue.removeFirst();
      final Level world = b.player.level;
      final BlockState bs = world.getBlockState(b.pos);
      if (bs.is(b.wantedBlock)) {

        // We found what we were looking for. Mine it and continue recursively
        final Block block = bs.getBlock();
        final SoundType sound = block.getSoundType(bs, b.player.getLevel(), b.pos, null);
        b.player
            .getLevel()
            .playSound(b.player, b.pos, sound.getBreakSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
        ((ServerPlayer) b.player).gameMode.destroyBlock(b.pos);
        break;
      }
    }
  }

  private static void playBreakSound(final BlockToBreak b, final BlockState bs) {
    final Block block = bs.getBlock();
    final SoundType sound = block.getSoundType(bs, b.player.getLevel(), b.pos, null);
    b.player
        .getLevel()
        .playSound(b.player, b.pos, sound.getBreakSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
  }

  private static void mineNeighborhood(
      final BlockPos pos, final Player player, final Block blockType) {
    final float x0 = pos.getX();
    final float y0 = pos.getY();
    final float z0 = pos.getZ();
    for (float z = z0 - 1.0F; z <= z0 + 1; z += 1.0) {
      for (float y = y0 - 1.0F; y <= y0 + 1; y += 1.0) {
        for (float x = x0 - 1.0F; x <= x0 + 1; x += 1.0) {
          final BlockPos here = new BlockPos(x, y, z);
          final BlockState b = player.getLevel().getBlockState(here);
          if (b.is(blockType)) {
            MegaMinerEvents.queue.addLast(new BlockToBreak(here, player, blockType));
          }
        }
      }
    }
  }
}
