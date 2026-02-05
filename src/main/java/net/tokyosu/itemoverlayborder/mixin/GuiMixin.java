package net.tokyosu.itemoverlayborder.mixin;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.tokyosu.itemoverlayborder.client.BorderRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Gui.class, priority = 500)
public class GuiMixin {
    @Inject(method = "renderSlot", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiGraphics;renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;II)V", shift = At.Shift.AFTER))
    public void renderSlot(GuiGraphics graphics, int x, int y, float p_281885_, Player p_283644_, ItemStack stack, int p_283261_, CallbackInfo ci) {
        BorderRenderer.render(graphics, x, y, stack);
    }
}
