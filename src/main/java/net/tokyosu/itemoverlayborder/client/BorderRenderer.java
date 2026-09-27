package net.tokyosu.itemoverlayborder.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.tokyosu.apocalypselib.utils.RarityUtils;
import net.tokyosu.itemoverlayborder.ItemOverlayBorder;
import net.tokyosu.itemoverlayborder.ItemOverlayConfig;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Renders the animated rarity border using a vertically stacked 16x16 texture.
 */
public final class BorderRenderer {
    private static final Map<Rarity, BorderColor> RARITY_MAP = new WeakHashMap<>();
    private static final ThreadLocal<Integer> SUPPRESSION_DEPTH = ThreadLocal.withInitial(() -> 0);
    private static final ResourceLocation BORDER_TEXTURE = ResourceLocation.fromNamespaceAndPath(ItemOverlayBorder.MOD_ID, "textures/gui/animated_border.png");
    private static final int DEPTH = 0;
    private static final int SIZE = 16;
    private static final int FRAME_COUNT = 64;
    private static final int TEXTURE_WIDTH = SIZE;
    private static final int TEXTURE_HEIGHT = SIZE * FRAME_COUNT;

    /**
     * Temporarily prevents borders from being rendered by nested GuiGraphics item draws.
     */
    public static void pushSuppression() {
        SUPPRESSION_DEPTH.set(SUPPRESSION_DEPTH.get() + 1);
    }

    /**
     * Restores border rendering after a matching {@link #pushSuppression()} call.
     */
    public static void popSuppression() {
        int depth = SUPPRESSION_DEPTH.get();
        if (depth <= 1) {
            SUPPRESSION_DEPTH.remove();
        } else {
            SUPPRESSION_DEPTH.set(depth - 1);
        }
    }

    public static boolean isSuppressed() {
        return SUPPRESSION_DEPTH.get() > 0;
    }

    /**
     * Gets the rarity RGB color from a style.
     *
     * @param style rarity style
     * @return packed RGB color, or white if no color is defined
     */
    public static int getRarityARGB(@NotNull Style style) {
        var styleColor = style.getColor();
        if (styleColor == null) return 0xFFFFFFFF;
        return styleColor.getValue();
    }

    /**
     * Renders a single 16x16 frame from the vertical animation texture and tints it
     * with the item's rarity color.
     */
    public static void render(@NotNull GuiGraphics graphics, int x, int y, @NotNull ItemStack stack) {
        if (isSuppressed() || stack.isEmpty() || RarityUtils.isCommon(stack)) return;

        var mc = Minecraft.getInstance();
        if (mc.level == null) return;

        BorderColor color = RARITY_MAP.computeIfAbsent(stack.getRarity(), BorderColor::new);

        int frame;
        if (ItemOverlayConfig.DISABLE_ANIMATION.get()) {
            frame = 0;
        } else {
            // The old renderer advanced the head by exactly 20 pixels/second.
            // At 20 game ticks/second this is one perimeter pixel (one exported
            // texture frame) per tick, wrapping after all 64 perimeter positions.
            frame = (int) (mc.level.getGameTime() % FRAME_COUNT);
        }

        int v = frame * SIZE;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.setShaderColor(color.R, color.G, color.B, 1.0F);

        graphics.blit(
                BORDER_TEXTURE,
                x,
                y,
                DEPTH,
                0.0F,
                (float)v,
                SIZE,
                SIZE,
                TEXTURE_WIDTH,
                TEXTURE_HEIGHT
        );

        // The shader color is global state. Flush before restoring it so this
        // border is submitted with its rarity tint and later GUI draws stay white.
        graphics.flush();

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }
}
