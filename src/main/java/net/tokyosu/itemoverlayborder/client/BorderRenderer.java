package net.tokyosu.itemoverlayborder.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.Objects;

/**
 * Render an animated border using rarity color.
 */
public class BorderRenderer {
    protected static final @NotNull TextColor WHITE_COLOR = Objects.requireNonNull(TextColor.fromLegacyFormat(ChatFormatting.WHITE));
    protected static final HashMap<Rarity, Style> RARITY_CACHE = new HashMap<>();
    private static final int SIZE = 16;
    private static final int PERIMETER = 4 * SIZE; // 4 border
    private static final int[] PX = new int[PERIMETER];
    private static final int[] PY = new int[PERIMETER];

    /**
     * Precompute perimeter to avoid allocating PX/PY during loop.
     */
    public static void initialize() {
        // PERIMETER must be 4 * SIZE.
        // PX/PY must be sized to PERIMETER.
        for (int p = 0; p < PERIMETER; p++) {
            if (p < SIZE) { // top: left -> right
                PX[p] = p;
                PY[p] = 0;
            } else if (p < 2 * SIZE) { // right: top -> bottom
                PX[p] = SIZE - 1;
                PY[p] = p - SIZE;
            } else if (p < 3 * SIZE) { // bottom: right -> left
                PX[p] = (3 * SIZE - 1) - p;
                PY[p] = SIZE - 1;
            } else { // left: bottom -> top
                PX[p] = 0;
                PY[p] = (4 * SIZE - 1) - p;
            }
        }
    }

    /**
     * Calculate brightness based on current position and last.
     * @return The more headPose is far or pixelPose the more it will be transparent.
     */
    private static float brightness(float pixelPos, float headPos) {
        float d = Math.abs(pixelPos - headPos);
        d = Math.min(d, PERIMETER - d); // wrap-around
        return Math.max(0.0f, 1.0f - d / SIZE); // fade length = 16px
    }

    /**
     * Custom implementation of GuiGraphics.fill() without flush() and float ARGB color passed directly to vertex color.
     * @param graphics A valid gui graphics.
     * @param x1 Starting X point.
     * @param y1 Starting Y point.
     * @param x2 End X point.
     * @param y2 End Y point.
     * @param r Red component.
     * @param g Green component.
     * @param b Blue component.
     * @param a Alpha component.
     */
    private static void fill(@NotNull GuiGraphics graphics, int x1, int y1, int x2, int y2, float r, float g, float b, float a) {
        Matrix4f mat = graphics.pose().last().pose();

        if (x1 < x2) {
            int i = x1;
            x1 = x2;
            x2 = i;
        }
        if (y1 < y2) {
            int j = y1;
            y1 = y2;
            y2 = j;
        }

        var vc = graphics.bufferSource().getBuffer(RenderType.gui());
        vc.vertex(mat, (float)x1, (float)y1, 0).color(r, g, b, a).endVertex();
        vc.vertex(mat, (float)x1, (float)y2, 0).color(r, g, b, a).endVertex();
        vc.vertex(mat, (float)x2, (float)y2, 0).color(r, g, b, a).endVertex();
        vc.vertex(mat, (float)x2, (float)y1, 0).color(r, g, b, a).endVertex();
    }

    /**
     * Get rarity style by ItemStack and cache it.
     * @param stack A valid ItemStack.
     * @return A valid rarity style.
     */
    public static @NotNull Style getRarityStyle(@NotNull ItemStack stack) {
        return RARITY_CACHE.computeIfAbsent(stack.getRarity(), (e) -> stack.getRarity().getStyleModifier().apply(Style.EMPTY));
    }

    /**
     * Check if a given ItemStack have common rarity.
     * @param stack A valid ItemStack.
     * @return True if rarity is common, false otherwise.
     */
    public static boolean isRarityCommon(@NotNull ItemStack stack) {
        var style = getRarityStyle(stack);
        var styleColor = style.getColor();
        if (styleColor == null) return false; // Avoid crash if TextColor is undefined !
        return styleColor == WHITE_COLOR;
    }

    /**
     * Get a rarity ARGB color from this style.
     * @param style A valid rarity style.
     * @return ARGB color or white if rarity color is null.
     */
    public static int getRarityARGB(@NotNull Style style) {
        var styleColor = style.getColor();
        if (styleColor == null) return 0xFFFFFFFF;
        return styleColor.getValue();
    }

    /**
     * Render a animated border.
     * @param graphics A valid GUI graphics.
     * @param x Starting X position.
     * @param y Starting Y position.
     * @param stack A valid ItemStack.
     */
    public static void render(@NotNull GuiGraphics graphics, int x, int y, @NotNull ItemStack stack) {
        if (isRarityCommon(stack)) return; // Avoid common rarity.

        // Check if player is inside a level before doing anything.
        var mc = Minecraft.getInstance();
        if (mc.level == null) return;

        // Time and color calculation.
        var color = getRarityARGB(getRarityStyle(stack));
        var r = (float)FastColor.ARGB32.red(color) / 255.0f;
        var g = (float)FastColor.ARGB32.green(color) / 255.0f;
        var b = (float)FastColor.ARGB32.blue(color) / 255.0f;
        var ticks = mc.level.getGameTime();
        var partial = mc.getFrameTime(); // 0..1
        var time = ticks + partial;
        var pixelsPerSecond = 20.0f; // slow, smooth
        var timeSeconds = time / 20.0f; // convert ticks → seconds
        var head = (timeSeconds * pixelsPerSecond) % PERIMETER;
        var mirror = (head + 32.0f) % PERIMETER;

        // Enable basic states.
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();

        // Now draw pixels.
        for (int i = 0; i < PERIMETER; i++) {
            var intensity = Math.max(brightness(i, head), brightness(i, mirror));
            if (intensity <= 0) continue;
            int px = x + PX[i];
            int py = y + PY[i];
            fill(graphics, px, py, px+1, py+1, r, g, b, intensity);
        }
    }
}
