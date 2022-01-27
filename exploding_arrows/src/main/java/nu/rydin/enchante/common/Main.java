package nu.rydin.enchante.common;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import nu.rydin.enchante.common.enchantments.ModEnchantments;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("enchante")
public class Main {
  public static final String MOD_ID = "enchante";
  private static final Logger LOGGER = LogManager.getLogger();

  public Main() {
    final ModLoadingContext modLoadingContext = ModLoadingContext.get();

    final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    bus.addListener(this::setup);
    ModEnchantments.init();
    MinecraftForge.EVENT_BUS.register(this);
  }

  private void setup(final FMLCommonSetupEvent event) {
    //    MinecraftForge.EVENT_BUS.register(new ManureFertilizationEvent());
  }
}
