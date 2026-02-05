package net.tokyosu.itemoverlayborder.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;

public class BorderRenderer {
    protected static final TextColor WHITE_COLOR = TextColor.fromLegacyFormat(ChatFormatting.WHITE);

    private static boolean isRarityValid(Style rarityStyle) {
        var rarityColor = rarityStyle.getColor();
        if (rarityColor == null || WHITE_COLOR == null)
            return false;
        return rarityColor.getValue() != WHITE_COLOR.getValue();
    }

    private static float getR(Style rarityStyle) {
        var rarityColor = rarityStyle.getColor();
        if (rarityColor == null)
            return 1.0F;
        return ((rarityColor.getValue() >> 16) & 0xFF) / 255.0F;
    }

    private static float getG(Style rarityStyle) {
        var rarityColor = rarityStyle.getColor();
        if (rarityColor == null)
            return 1.0F;
        return ((rarityColor.getValue() >> 8) & 0xFF) / 255.0F;
    }

    private static float getB(Style rarityStyle) {
        var rarityColor = rarityStyle.getColor();
        if (rarityColor == null)
            return 1.0F;
        return (rarityColor.getValue() & 0xFF) / 255.0F;
    }

    private static float brightness(float pixelPos, float headPos) {
        float d = Math.abs(pixelPos - headPos);
        d = Math.min(d, 64.0f - d); // wrap-around
        return Math.max(0.0f, 1.0f - d / 24.0f); // fade length = 8px
    }

    private static void perimeterToXY(int p, int x, int y, int[] out) {
        if (p < 16) {              // top
            out[0] = x + p;
            out[1] = y;
        } else if (p < 32) {       // right
            out[0] = x + 15;
            out[1] = y + (p - 16);
        } else if (p < 48) {       // bottom
            out[0] = x + (47 - p);
            out[1] = y + 15;
        } else {                   // left
            out[0] = x;
            out[1] = y + (63 - p);
        }
    }

    public static void render(GuiGraphics graphics, int x, int y, ItemStack stack) {
        if (stack.isEmpty()) return;

        var mc = Minecraft.getInstance();
        if (mc.level == null) return;

        var style = stack.getRarity().getStyleModifier().apply(Style.EMPTY);
        if (!isRarityValid(style)) return; // Rarity should be more than white !

        var r = getR(style);
        var g = getG(style);
        var b = getB(style);

        float ticks = mc.level.getGameTime();
        float partial = mc.getFrameTime(); // 0..1
        float time = ticks + partial;
        float pixelsPerSecond = 20.0f; // slow, smooth
        float timeSeconds = time / 20.0f; // convert ticks → seconds
        float head = (timeSeconds * pixelsPerSecond) % 64.0f;
        float mirror = (head + 32.0f) % 64.0f;

        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();

        for (int i = 0; i < 64; i++) {
            float b1 = brightness(i, head);
            float b2 = brightness(i, mirror);
            float intensity = Math.max(b1, b2);
            if (intensity <= 0) continue;

            int[] pos = new int[2];
            perimeterToXY(i, x, y, pos);
            graphics.setColor(r * intensity, g * intensity, b * intensity, intensity);

            graphics.fill(
                    pos[0], pos[1],
                    pos[0] + 1, pos[1] + 1,
                    ((int)(intensity * 255) << 24) | 0xFFFFFF
            );
        }

        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
