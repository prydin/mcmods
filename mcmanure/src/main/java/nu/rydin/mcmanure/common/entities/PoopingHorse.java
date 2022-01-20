package nu.rydin.mcmanure.common.entities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.level.Level;
import nu.rydin.mcmanure.common.init.EntityInit;
import nu.rydin.mcmanure.common.init.ItemsInit;
import nu.rydin.mcmanure.common.init.SoundInit;

public class PoopingHorse extends Horse {
  private static final int POOP_INTERVAL = 5 * 60 * 20; // 5 minutes
  public int poopTime = random.nextInt(POOP_INTERVAL) + POOP_INTERVAL;

  public PoopingHorse(final EntityType<? extends Horse> type, final Level level) {
    super(type, level);
  }

  @Override
  public void aiStep() {
    super.aiStep();
    if (!level.isClientSide && isAlive() && --poopTime <= 0) {
      playSound(SoundInit.FLATUS.get(), 1.0F, 1.0F);
      spawnAtLocation(ItemsInit.HORSE_MANURE_ITEM.get());
      poopTime = random.nextInt(POOP_INTERVAL) + POOP_INTERVAL;
    }
  }

  @Override
  public Horse getBreedOffspring(final ServerLevel level, final AgeableMob parent) {
    return EntityInit.POOPING_HORSE.get().create(level);
  }
}
