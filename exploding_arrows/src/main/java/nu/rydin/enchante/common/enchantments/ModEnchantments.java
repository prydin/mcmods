package nu.rydin.enchante.common.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nu.rydin.enchante.common.Main;

public class ModEnchantments {
  public static final DeferredRegister<Enchantment> ENCHANTMENTS =
      DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Main.MOD_ID);

  public static final RegistryObject<ExplodingArrowsEnchantment> ENCHANTE =
      ModEnchantments.ENCHANTMENTS.register(
          "exploding_arrows",
          () ->
              new ExplodingArrowsEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND));

  public static final RegistryObject<MegaMinerEnchantment> MEGA_MINER =
      ModEnchantments.ENCHANTMENTS.register(
          "megaminer",
          () -> new MegaMinerEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND));

  public static void init() {
    ModEnchantments.ENCHANTMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
  }
}
