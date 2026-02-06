package net.tokyosu.itemoverlayborder.mixin.emi;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.SlotWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.tokyosu.itemoverlayborder.client.BorderRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SlotWidget.class, remap = false)
public abstract class SlotWidgetMixin {
    @Shadow
    public abstract EmiIngredient getStack();

    @Shadow
    public abstract Bounds getBounds();

    @Inject(method = "drawStack", at = @At("TAIL"))
    public void drawStack(GuiGraphics draw, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        final var stack = getStack();
        Bounds bounds = getBounds();
        int xOff = (bounds.width() - 16) / 2;
        int yOff = (bounds.height() - 16) / 2;
        BorderRenderer.render(draw, bounds.x() + xOff, bounds.y() + yOff, stack.getEmiStacks().get(0).getItemStack());
    }
}
