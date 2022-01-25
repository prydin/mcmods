package nu.rydin.explodingarrows.common.events;

import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nu.rydin.explodingarrows.common.Main;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GamePlayEvents {
    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent e) {
        /*
        Projectile p = e.getProjectile();
        Level level = p.getLevel();
        if(level.isClientSide()) {
            return;
        }
        Vec3 pos = p.getPosition(0.0F);
        level.explode(p, pos.x, pos.y, pos.z, 4.0F, Explosion.BlockInteraction.BREAK);
        e.setCanceled(true);
         */
    }
}
