package net.tokyosu.itemoverlayborder.mixin.obscuretooltips;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.obscuria.tooltips.client.TooltipState;
import net.minecraft.client.gui.GuiGraphics;
import net.tokyosu.itemoverlayborder.client.BorderRenderer;

@Pseudo
@Mixin(targets = {
        "dev.obscuria.tooltips.client.tooltip.element.icon.StaticIcon",
        "dev.obscuria.tooltips.client.tooltip.element.icon.AccentIcon",
        "dev.obscuria.tooltips.client.tooltip.element.icon.AccentSpinIcon",
        "dev.obscuria.tooltips.client.tooltip.element.icon.AccentBurstIcon"
}, remap = false)
public abstract class TooltipIconMixin {
    @Inject(method = "render", at = @At("HEAD"), require = 0)
    private void itemoverlayborder$beginSuppress(@NotNull TooltipState state, @NotNull GuiGraphics graphics, int x, int y, @NotNull CallbackInfo ci) {
    	if (!state.stack.isEmpty()) {
            BorderRenderer.render(graphics, x - 8, y - 8, state.stack);
        }
        BorderRenderer.pushSuppression(); // Avoid duplication.
    }

    @Inject(method = "render", at = @At("RETURN"), require = 0)
    private void itemoverlayborder$endSuppress(@NotNull TooltipState state, @NotNull GuiGraphics graphics, int x, int y, @NotNull CallbackInfo ci) {
        BorderRenderer.popSuppression();
    }
}
