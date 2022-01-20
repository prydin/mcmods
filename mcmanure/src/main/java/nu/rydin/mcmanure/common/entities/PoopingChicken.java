package nu.rydin.mcmanure.common.entities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.level.Level;
import nu.rydin.mcmanure.common.init.EntityInit;
import nu.rydin.mcmanure.common.init.SoundInit;
import nu.rydin.mcmanure.utils.Helpers;

public class PoopingChicken extends Chicken {
  private static final int POOP_INTERVAL = 5 * 60 * 20; // 5 minutes
  public int poopTime = this.random.nextInt(POOP_INTERVAL) + POOP_INTERVAL;

  public PoopingChicken(final EntityType<? extends Chicken> entityType, final Level level) {
    super(entityType, level);
  }

  @Override
  public void aiStep() {
    super.aiStep();
    if (!this.level.isClientSide && this.isAlive() && --this.poopTime <= 0) {
      this.playSound(SoundInit.CHICKEN_FERTILIZE.get(), 1.0F, 2.0F);
      Helpers.mobApplyFertilizer(this.getLevel(), this.getOnPos());
      poopTime = this.random.nextInt(POOP_INTERVAL) + POOP_INTERVAL;
    }
  }

  @Override
  public Chicken getBreedOffspring(final ServerLevel level, final AgeableMob parent) {
    return EntityInit.POOPING_CHICKEN.get().create(level);
  }
}
