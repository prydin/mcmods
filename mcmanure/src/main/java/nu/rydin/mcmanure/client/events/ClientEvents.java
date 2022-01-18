package nu.rydin.mcmanure.client.events;

import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nu.rydin.mcmanure.common.MCManure;
import nu.rydin.mcmanure.common.init.EntityInit;

@Mod.EventBusSubscriber(
    modid = MCManure.MOD_ID,
    bus = Mod.EventBusSubscriber.Bus.MOD,
    value = Dist.CLIENT)
public class ClientEvents {
  @SubscribeEvent
  public static void onRegisterRenderer(final EntityRenderersEvent.RegisterRenderers e) {
    e.registerEntityRenderer(EntityInit.POOPING_COW.get(), CowRenderer::new);
  }
}
