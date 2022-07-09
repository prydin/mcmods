package nu.rydin.enchante.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class FeatureDetector {
  private static final int MAX_BLOCKS = 1000;

  private interface BlockAction {
    boolean onBlock(BlockPos pos, BlockState state, boolean isRightBlock);
  }

  public static LinkedList<BlockPos> detectOreVein(final LevelReader world, final BlockPos start) {
    return FeatureDetector.recursiveSearch(world, start, null);
  }

  public static LinkedList<BlockPos> detectTreeTrunk(
      final LevelReader world, final BlockPos start) {
    final Block trunkBlock = world.getBlockState(start).getBlock();
    if (!trunkBlock.getDescriptionId().endsWith("_log")) {
      // Not a log block. Definitely not a tree!
      return new LinkedList<>();
    }

    final boolean[] leavesFound = new boolean[1];
    final LinkedList<BlockPos> result =
        FeatureDetector.recursiveSearch(
            world,
            start,
            (pos, bs, isRightBlock) -> {
              if (FeatureDetector.isLeaves(trunkBlock, bs)) {
                leavesFound[0] = true;
              }
              return true;
            });
    return leavesFound[0] ? result : new LinkedList<>();
  }

  private static LinkedList<BlockPos> recursiveSearch(
      final LevelReader world, final BlockPos start, @Nullable final BlockAction action) {
    final Block wantedBlock = world.getBlockState(start).getBlock();
    boolean abort = false;
    final LinkedList<BlockPos> result = new LinkedList<>();
    final Set<BlockPos> visited = new HashSet<>();
    final LinkedList<BlockPos> queue = new LinkedList<>();
    queue.push(start);

    // Recursively scan 3x3x3 cubes while keeping track of already scanned
    // blocks to avoid cycles.
    // There's probably more efficient ways of doing this, but since the structures
    // we're working on are relatively small, this will do fine.
    while (!queue.isEmpty()) {
      final BlockPos center = queue.pop();
      final int x0 = center.getX();
      final int y0 = center.getY();
      final int z0 = center.getZ();
      for (int z = z0 - 1; z <= z0 + 1 && !abort; ++z) {
        for (int y = y0 - 1; y <= y0 + 1 && !abort; ++y) {
          for (int x = x0 - 1; x <= x0 + 1 && !abort; ++x) {
            final BlockPos pos = new BlockPos(x, y, z);
            final BlockState bs = world.getBlockState(pos);
            if ((bs.isAir() || !visited.add(pos))) {
              continue;
            }
            final boolean isRightBlock = bs.is(wantedBlock);
            if (isRightBlock) {
              result.add(pos);
              if (queue.size() > FeatureDetector.MAX_BLOCKS) {
                abort = true;
                break;
              }
              queue.push(pos);
            }
            if (action != null) {
              abort = !action.onBlock(pos, bs, isRightBlock);
            }
          }
        }
      }
    }
    return !abort ? result : new LinkedList();
  }

  private static boolean isLeaves(final Block ref, final BlockState bs) {
    final String name = bs.getBlock().getDescriptionId();
    if (!name.endsWith("_leaves")) {
      return false;
    }
    final Collection<Property<?>> properties = bs.getProperties();
    for (final Property<?> p : properties) {
      if (p.getName() == "persistent") {
        final boolean persistent = bs.getValue((Property<Boolean>) p);
        if (persistent) {
          return false; // True tree leaves are not persistent.
        }
        break; // No need to check more properties
      }
    }
    final String refName = ref.getDescriptionId();
    return name.startsWith(refName.substring(0, refName.length() - 5));
  }
}
