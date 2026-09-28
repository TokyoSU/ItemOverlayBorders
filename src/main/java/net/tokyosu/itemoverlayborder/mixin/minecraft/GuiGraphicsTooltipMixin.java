package net.tokyosu.itemoverlayborder.mixin.minecraft;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.tokyosu.itemoverlayborder.client.TooltipBorderBlacklist;
import net.tokyosu.itemoverlayborder.client.TooltipRenderContext;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsTooltipMixin {
    @Inject(
            method = "renderTooltipInternal",
            at = @At("HEAD")
    )
    private void itemoverlayborder$detectObscureTooltip(Font font, List<ClientTooltipComponent> components, int mouseX, int mouseY, ClientTooltipPositioner positioner, CallbackInfo ci) {
    	 if (TooltipBorderBlacklist.containsObscureTooltip(components)) {
             TooltipRenderContext.enterObscureTooltip();
         }
    }
    
    @Inject(
            method = "renderTooltipInternal",
            at = @At("RETURN")
    )
    private void itemoverlayborder$endTooltip(Font font, List<ClientTooltipComponent> components, int mouseX, int mouseY, ClientTooltipPositioner positioner, CallbackInfo ci) {
    	if (TooltipBorderBlacklist.containsObscureTooltip(components)) {
            TooltipRenderContext.exitObscureTooltip();
        }
    }
}
