package nu.rydin.mcmanure.common.init;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.EggItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nu.rydin.mcmanure.common.MCManure;
import nu.rydin.mcmanure.common.items.BaseItem;
import nu.rydin.mcmanure.common.items.FuelItem;
import nu.rydin.mcmanure.common.items.PoopingChickenEgg;

public class ItemsInit {

  public static final DeferredRegister<Item> ITEMS =
      DeferredRegister.create(ForgeRegistries.ITEMS, MCManure.MOD_ID);

  public static final DeferredRegister<Item> VANILLA_ITEMS =
      DeferredRegister.create(ForgeRegistries.ITEMS, "minecraft");

  public static final RegistryObject<BaseItem> COW_MANURE_ITEM =
      ITEMS.register("cow_manure", BaseItem::new);

  public static final RegistryObject<BaseItem> HORSE_MANURE_ITEM =
      ITEMS.register("horse_manure", BaseItem::new);

  public static final RegistryObject<BaseItem> DRIED_COW_MANURE_ITEM =
      ITEMS.register("dried_cow_manure", () -> new FuelItem(300));

  public static final RegistryObject<BaseItem> DRIED_HORSE_MANURE_ITEM =
      ITEMS.register("dried_horse_manure", () -> new FuelItem(300));

  public static final RegistryObject<ForgeSpawnEggItem> COW_SPAWN_EGG =
      VANILLA_ITEMS.register(
          "cow_spawn_egg",
          () ->
              new ForgeSpawnEggItem(
                  EntityInit.POOPING_COW,
                  0x443626,
                  0xA1A1A1,
                  new Item.Properties()
                      .tab(CreativeModeTab.TAB_MISC)
                      .stacksTo(64)
                      .rarity(Rarity.COMMON)));

  public static final RegistryObject<ForgeSpawnEggItem> CHICKEN_SPAWN_EGG =
      VANILLA_ITEMS.register(
          "chicken_spawn_egg",
          () ->
              new ForgeSpawnEggItem(
                  EntityInit.POOPING_CHICKEN,
                  0xA1A1A1,
                  0xFF0000,
                  new Item.Properties()
                      .tab(CreativeModeTab.TAB_MISC)
                      .stacksTo(64)
                      .rarity(Rarity.COMMON)));

  public static final RegistryObject<ForgeSpawnEggItem> HORSE_SPAWN_EGG =
      VANILLA_ITEMS.register(
          "horse_spawn_egg",
          () ->
              new ForgeSpawnEggItem(
                  EntityInit.POOPING_HORSE,
                  0xC09E7D,
                  0xEEE500,
                  new Item.Properties()
                      .tab(CreativeModeTab.TAB_MISC)
                      .stacksTo(64)
                      .rarity(Rarity.COMMON)));

  public static final RegistryObject<EggItem> EGG =
      VANILLA_ITEMS.register("egg", PoopingChickenEgg::new);

  public static void init() {
    ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    VANILLA_ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
  }
}
