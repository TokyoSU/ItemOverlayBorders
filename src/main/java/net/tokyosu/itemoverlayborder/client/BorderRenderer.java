package net.tokyosu.itemoverlayborder.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
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

    public static void render(@NotNull GuiGraphics graphics, int x, int y, @NotNull ItemStack stack) {
        if (isSuppressed()) return;
        renderInternal(graphics, x, y, stack);
    }

    public static void renderForced(@NotNull GuiGraphics graphics, int x, int y, @NotNull ItemStack stack) {
        renderInternal(graphics, x, y, stack);
    }

    private static void renderInternal(@NotNull GuiGraphics graphics, int x, int y, @NotNull ItemStack stack) {
        if (stack.isEmpty() || RarityUtils.isCommon(stack)) return;

        var mc = Minecraft.getInstance();
        if (mc.level == null) return;

        BorderColor color = RARITY_MAP.computeIfAbsent(
                stack.getRarity(),
                BorderColor::new
        );

        int frame = ItemOverlayConfig.DISABLE_ANIMATION.get() ? 0 : (int) (mc.level.getGameTime() % FRAME_COUNT);
        int v = frame * SIZE;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
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

        graphics.flush();

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }
}
