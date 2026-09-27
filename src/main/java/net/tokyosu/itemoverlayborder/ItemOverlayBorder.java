package net.tokyosu.itemoverlayborder;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@SuppressWarnings({"removal"})
@Mod(ItemOverlayBorder.MOD_ID)
public class ItemOverlayBorder {
    public static final String MOD_ID = "itemoverlayborder";
    public ItemOverlayBorder() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ItemOverlayConfig.SPEC, "itemoverlayborder.toml");
    }
}
