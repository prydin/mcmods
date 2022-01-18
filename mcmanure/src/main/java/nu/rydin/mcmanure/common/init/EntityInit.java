package nu.rydin.mcmanure.common.init;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nu.rydin.mcmanure.common.entities.PoopingCow;

public class EntityInit {
  public static final DeferredRegister<EntityType<?>> VANILLA_ENTITIES =
      DeferredRegister.create(ForgeRegistries.ENTITIES, "minecraft");

  public static final RegistryObject<EntityType<PoopingCow>> POOPING_COW =
      VANILLA_ENTITIES.register(
          "cow",
          () ->
              EntityType.Builder.of(PoopingCow::new, MobCategory.CREATURE).build("minecraft:cow"));

  public static void init() {
    EntityInit.VANILLA_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
  }
}
