package net.tokyosu.itemoverlayborder.mixin.obscuretooltips;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.tokyosu.itemoverlayborder.client.BorderRenderer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Obscure Tooltips renders a large rotating item preview inside tooltips by
 * calling GuiGraphics#renderItem. Suppress ItemOverlayBorders only while that
 * preview is being rendered, without affecting normal inventory/JEI/REI items.
 */
@Pseudo
@Mixin(targets = "dev.obscuria.tooltips.client.component.ToolPreviewComponent", remap = false)
public abstract class ToolPreviewComponentMixin {
    @Inject(method = "renderImage", at = @At("HEAD"), require = 0, remap = false)
    private void beginToolPreview(@NotNull Font font, int x, int y, @NotNull GuiGraphics graphics, @NotNull CallbackInfo ci) {
        BorderRenderer.pushSuppression();
    }

    @Inject(method = "renderImage", at = @At("RETURN"), require = 0, remap = false)
    private void endToolPreview(@NotNull Font font, int x, int y, @NotNull GuiGraphics graphics, @NotNull CallbackInfo ci) {
        BorderRenderer.popSuppression();
    }
}
