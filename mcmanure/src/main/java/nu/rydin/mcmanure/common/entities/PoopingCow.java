package nu.rydin.mcmanure.common.entities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.level.Level;
import nu.rydin.mcmanure.common.init.EntityInit;
import nu.rydin.mcmanure.common.init.ItemsInit;
import nu.rydin.mcmanure.common.init.SoundInit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PoopingCow extends Cow {
  private static final int POOP_INTERVAL = 5 * 60 * 20; // 5 minutes
  private static final Logger LOGGER = LogManager.getLogger();
  public int poopTime = this.random.nextInt(POOP_INTERVAL) + POOP_INTERVAL;

  public PoopingCow(final EntityType<? extends Cow> type, final Level level) {

    super(type, level);
    LOGGER.info("Cow created. Next manure generation in " + poopTime + " ticks");
  }

  @Override
  public void aiStep() {
    super.aiStep();
    if (!this.level.isClientSide && this.isAlive() && --this.poopTime <= 0) {
      this.playSound(SoundInit.FLATUS.get(), 1.0F, 1.0F);
      this.spawnAtLocation(ItemsInit.COW_MANURE_ITEM.get());
      this.poopTime = this.random.nextInt(POOP_INTERVAL) + POOP_INTERVAL;
      LOGGER.info("Generated manure. Next manure generation in " + poopTime + " ticks");
    }
  }

  @Override
  public Cow getBreedOffspring(final ServerLevel level, final AgeableMob parent) {
    return EntityInit.POOPING_COW.get().create(level);
  }
}
