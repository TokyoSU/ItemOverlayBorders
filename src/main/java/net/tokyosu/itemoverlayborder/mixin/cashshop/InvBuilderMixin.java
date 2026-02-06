package net.tokyosu.itemoverlayborder.mixin.cashshop;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.tokyosu.cashshop.utils.InvBuilder;
import net.tokyosu.itemoverlayborder.client.BorderRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = InvBuilder.class, remap = false)
public class InvBuilderMixin {
    @Shadow
    private GuiGraphics pGui;

    @Inject(method = "drawIcon", at = @At("RETURN"))
    public void drawIcon(ItemStack stack, int x, int y, CallbackInfo ci) {
        if (stack.isEmpty()) return;
        BorderRenderer.render(this.pGui, x, y, stack);
    }
}
