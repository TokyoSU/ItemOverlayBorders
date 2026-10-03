package net.tokyosu.itemoverlayborder.client;

import net.tokyosu.apocalypselib.utils.ColorUtils;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

public class BorderColor {
    public float R = 1.0F;
    public float G = 1.0F;
    public float B = 1.0F;

    public BorderColor(@NotNull Rarity rarity) {
        int color = ColorUtils.getRGBFromRarity(rarity, 0xFFFFFF);
        this.R = ColorUtils.getRedFloat(color);
        this.G = ColorUtils.getGreenFloat(color);
        this.B = ColorUtils.getBlueFloat(color);
    }

}
