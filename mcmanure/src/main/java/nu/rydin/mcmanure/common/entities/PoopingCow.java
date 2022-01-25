package nu.rydin.mcmanure.common.entities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.level.Level;
import nu.rydin.mcmanure.common.config.ConfigHandler;
import nu.rydin.mcmanure.common.init.EntityInit;
import nu.rydin.mcmanure.common.init.ItemsInit;
import nu.rydin.mcmanure.common.init.SoundInit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PoopingCow extends Cow {
  private static final Logger LOGGER = LogManager.getLogger();
  private final int poopInterval =
      ConfigHandler.GENERAL.cowDefecationInterval.get(); // Default 5 minutes
  private int poopTime = random.nextInt(poopInterval) + poopInterval;

  public PoopingCow(final EntityType<? extends Cow> type, final Level level) {

    super(type, level);
    LOGGER.debug("Cow created. Next manure generation in " + poopTime + " ticks");
  }

  @Override
  public void aiStep() {
    super.aiStep();
    if (!level.isClientSide && isAlive() && --poopTime <= 0) {
      playSound(SoundInit.FLATUS.get(), 1.0F, 1.0F);
      spawnAtLocation(ItemsInit.COW_MANURE_ITEM.get());
      poopTime = random.nextInt(poopInterval) + poopInterval;
      LOGGER.debug("Generated manure. Next manure generation in " + poopTime + " ticks");
    }
  }

  @Override
  public Cow getBreedOffspring(final ServerLevel level, final AgeableMob parent) {
    return EntityInit.POOPING_COW.get().create(level);
  }
}
