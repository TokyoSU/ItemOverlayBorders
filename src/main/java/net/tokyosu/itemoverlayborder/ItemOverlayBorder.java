package net.tokyosu.itemoverlayborder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@SuppressWarnings({"removal"})
@Mod(ItemOverlayBorder.MOD_ID)
public class ItemOverlayBorder {
    public static final String MOD_ID = "itemoverlayborder";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public ItemOverlayBorder() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ItemOverlayConfig.SPEC, "itemoverlayborder.toml");
    }
}
