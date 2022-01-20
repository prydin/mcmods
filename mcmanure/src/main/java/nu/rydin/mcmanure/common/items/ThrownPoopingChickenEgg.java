package nu.rydin.mcmanure.common.items;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import nu.rydin.mcmanure.common.init.EntityInit;

public class ThrownPoopingChickenEgg extends ThrownEgg {
  public ThrownPoopingChickenEgg(
      final EntityType<? extends ThrownEgg> entityType, final Level level) {
    super(entityType, level);
  }

  public ThrownPoopingChickenEgg(final Level level, final LivingEntity entity) {
    super(level, entity);
  }

  public ThrownPoopingChickenEgg(
      final Level level, final double x, final double y, final double z) {
    super(level, x, y, z);
  }

  protected void superOnHit(final HitResult result) {
    // Workaround to avoid calling ThrownEgg::onHit since it would create another entity
    final HitResult.Type resultType = result.getType();
    if (resultType == HitResult.Type.ENTITY) {
      this.onHitEntity((EntityHitResult) result);
    } else if (resultType == HitResult.Type.BLOCK) {
      this.onHitBlock((BlockHitResult) result);
    }

    if (resultType != HitResult.Type.MISS) {
      this.gameEvent(GameEvent.PROJECTILE_LAND, this.getOwner());
    }
  }

  @Override
  protected void onHit(final HitResult result) {
    superOnHit(result);
    if (!this.level.isClientSide) {
      if (this.random.nextInt(8) == 0) {
        int i = 1;
        if (this.random.nextInt(32) == 0) {
          i = 4;
        }

        for (int j = 0; j < i; ++j) {
          final Chicken chicken = EntityInit.POOPING_CHICKEN.get().create(this.level);
          chicken.setAge(-24000);
          chicken.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
          this.level.addFreshEntity(chicken);
        }
      }

      this.level.broadcastEntityEvent(this, (byte) 3);
      this.discard();
    }
  }
}
