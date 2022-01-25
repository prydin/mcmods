package nu.rydin.mcmanure.common;

import net.minecraft.Util;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import nu.rydin.mcmanure.common.config.ConfigHandler;
import nu.rydin.mcmanure.common.events.ManureFertilizationEvent;
import nu.rydin.mcmanure.common.init.EntityInit;
import nu.rydin.mcmanure.common.init.ItemsInit;
import nu.rydin.mcmanure.common.init.SoundInit;
import nu.rydin.mcmanure.common.items.ThrownPoopingChickenEgg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("mcmanure")
public class MCManure {
  public static final String MOD_ID = "mcmanure";
  private static final Logger LOGGER = LogManager.getLogger();

  public MCManure() {
    final ModLoadingContext modLoadingContext = ModLoadingContext.get();
    modLoadingContext.registerConfig(ModConfig.Type.COMMON, ConfigHandler.spec);

    final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    bus.addListener(this::setup);
    bus.addListener(this::finish);

    ItemsInit.init();
    SoundInit.init();
    EntityInit.init();

    MinecraftForge.EVENT_BUS.register(this);
  }

  private void finish(final FMLLoadCompleteEvent e) {
    DispenserBlock.registerBehavior(
        ItemsInit.EGG.get(),
        new AbstractProjectileDispenseBehavior() {
          @Override
          protected Projectile getProjectile(
              final Level level, final Position position, final ItemStack stack) {
            return Util.make(
                new ThrownPoopingChickenEgg(level, position.x(), position.y(), position.z()),
                (thrownEgg) -> {
                  thrownEgg.setItem(stack);
                });
          }
        });
  }

  private void setup(final FMLCommonSetupEvent event) {
    MinecraftForge.EVENT_BUS.register(new ManureFertilizationEvent());
  }
}
