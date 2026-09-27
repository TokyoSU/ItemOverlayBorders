package net.tokyosu.itemoverlayborder.client;

import net.minecraft.network.chat.Style;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.Rarity;
import net.tokyosu.apocalypselib.utils.RarityUtils;
import org.jetbrains.annotations.NotNull;

public class BorderColor {
    public float R = 1.0F;
    public float G = 1.0F;
    public float B = 1.0F;

    public BorderColor(@NotNull Rarity rarity) {
        int color = this.getRarityARGB(RarityUtils.getStyleByRarity(rarity));
        this.R = FastColor.ARGB32.red(color) / 255.0F;
        this.G = FastColor.ARGB32.green(color) / 255.0F;
        this.B = FastColor.ARGB32.blue(color) / 255.0F;
    }

    private int getRarityARGB(@NotNull Style style) {
        var styleColor = style.getColor();
        if (styleColor == null) return 0xFFFFFFFF;
        return styleColor.getValue();
    }
}
