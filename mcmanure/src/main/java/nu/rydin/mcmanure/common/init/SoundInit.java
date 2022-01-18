package nu.rydin.mcmanure.common.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nu.rydin.mcmanure.common.MCManure;

public class SoundInit {
  public static DeferredRegister<SoundEvent> SOUNDS =
      DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MCManure.MOD_ID);

  public static RegistryObject<SoundEvent> FLATUS =
      SOUNDS.register(
          "flatus", () -> new SoundEvent(new ResourceLocation(MCManure.MOD_ID, "flatus")));

  public static void init() {
    SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
  }
}
