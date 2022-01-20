package nu.rydin.mcmanure.common.events;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nu.rydin.mcmanure.common.MCManure;
import nu.rydin.mcmanure.common.entities.PoopingChicken;
import nu.rydin.mcmanure.common.entities.PoopingCow;
import nu.rydin.mcmanure.common.init.EntityInit;

@Mod.EventBusSubscriber(modid = MCManure.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonEvents {
  @SubscribeEvent
  public static void onEntityCreateAttributes(final EntityAttributeCreationEvent e) {
    e.put(EntityInit.POOPING_COW.get(), PoopingCow.createAttributes().build());
    e.put(EntityInit.POOPING_CHICKEN.get(), PoopingChicken.createAttributes().build());
  }
}
