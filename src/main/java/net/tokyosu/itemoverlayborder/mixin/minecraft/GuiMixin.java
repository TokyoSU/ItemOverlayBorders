package net.tokyosu.itemoverlayborder.mixin.minecraft;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.tokyosu.itemoverlayborder.client.BorderRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
    @Inject(method = "renderSlot", at = @At("HEAD"))
    public void renderSlot(GuiGraphics graphics, int x, int y, float delta, Player player, ItemStack stack, int p_283261_, CallbackInfo ci) {
        if (stack.isEmpty()) return;
        BorderRenderer.render(graphics, x, y, stack);
    }
}
