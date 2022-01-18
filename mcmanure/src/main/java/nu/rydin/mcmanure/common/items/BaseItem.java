package nu.rydin.mcmanure.common.items;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BaseItem extends Item {
  private static final Logger LOGGER = LogManager.getLogger();

  public BaseItem() {
    super(new Item.Properties().tab(CreativeModeTab.TAB_MISC));
  }
}
