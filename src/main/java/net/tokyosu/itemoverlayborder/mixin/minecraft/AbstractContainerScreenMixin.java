package net.tokyosu.itemoverlayborder.mixin.minecraft;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.tokyosu.itemoverlayborder.client.BorderRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {
    @Inject(method = "renderSlot", at = @At("HEAD"))
    public void renderSlot(GuiGraphics graphics, Slot slot, CallbackInfo ci) {
        final var stack = slot.getItem();
        if (stack.isEmpty()) return;
        BorderRenderer.render(graphics, slot.x, slot.y, stack);
    }
}
