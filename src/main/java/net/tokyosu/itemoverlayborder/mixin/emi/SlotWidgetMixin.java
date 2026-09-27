package net.tokyosu.itemoverlayborder.mixin.emi;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.Bounds;
import net.minecraft.client.gui.GuiGraphics;
import net.tokyosu.itemoverlayborder.client.BorderRenderer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "dev.emi.emi.api.widget.SlotWidget", remap = false)
public abstract class SlotWidgetMixin {
    @Shadow @NotNull
    public abstract EmiIngredient getStack();

    @Shadow @NotNull
    public abstract Bounds getBounds();

    @Inject(method = "drawStack", at = @At("TAIL"))
    public void drawStack(@NotNull GuiGraphics draw, int mouseX, int mouseY, float delta, @NotNull CallbackInfo ci) {
        final var stack = getStack().getEmiStacks().get(0).getItemStack();
        if (stack.isEmpty()) return;
        final var bounds = getBounds();
        int xOff = (bounds.width() - 16) / 2;
        int yOff = (bounds.height() - 16) / 2;
        BorderRenderer.render(draw, bounds.x() + xOff, bounds.y() + yOff, stack);
    }
}
