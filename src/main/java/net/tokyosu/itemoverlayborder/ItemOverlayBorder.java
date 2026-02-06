package net.tokyosu.itemoverlayborder;

import net.minecraftforge.fml.common.Mod;
import net.tokyosu.itemoverlayborder.client.BorderRenderer;

@Mod(ItemOverlayBorder.MOD_ID)
public class ItemOverlayBorder {
    public static final String MOD_ID = "itemoverlayborder";
    public ItemOverlayBorder() {
        BorderRenderer.initialize();
    }
}
