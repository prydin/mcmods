package nu.rydin.enchante.common.events;

import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nu.rydin.enchante.common.Main;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ExplodingArrowsEvents {
  @SubscribeEvent
  public static void onAttack(final AttackEntityEvent e) {
    System.out.println(e);
  }
}
