package nu.rydin.mcmanure.common.entities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.level.Level;
import nu.rydin.mcmanure.common.config.ConfigHandler;
import nu.rydin.mcmanure.common.init.EntityInit;
import nu.rydin.mcmanure.common.init.SoundInit;
import nu.rydin.mcmanure.utils.Helpers;

public class PoopingChicken extends Chicken {
  private final int poopInterval =
      ConfigHandler.GENERAL.chickenDefecationInterval.get(); // Default 5 minutes
  private final boolean fertilizeGround = ConfigHandler.GENERAL.chickenFertilizeGround.get();
  private int poopTime = random.nextInt(poopInterval) + poopInterval;

  public PoopingChicken(final EntityType<? extends Chicken> entityType, final Level level) {
    super(entityType, level);
  }

  @Override
  public void aiStep() {
    super.aiStep();
    if (!level.isClientSide && isAlive() && !isBaby() && --poopTime <= 0) {
      if (Helpers.mobApplyFertilizer(getLevel(), getOnPos().above())) {
        playSound(SoundInit.CHICKEN_FERTILIZE.get(), 1.0F, 2.0F);
      }
      if (fertilizeGround && Helpers.mobApplyFertilizer(getLevel(), getOnPos())) {
        playSound(SoundInit.CHICKEN_FERTILIZE.get(), 1.0F, 2.0F);
      }
      poopTime = random.nextInt(poopInterval) + poopInterval;
    }
  }

  @Override
  public Chicken getBreedOffspring(final ServerLevel level, final AgeableMob parent) {
    return EntityInit.POOPING_CHICKEN.get().create(level);
  }
}
