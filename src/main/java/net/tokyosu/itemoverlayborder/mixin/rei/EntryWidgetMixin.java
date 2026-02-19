package net.tokyosu.itemoverlayborder.mixin.rei;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.impl.client.gui.widget.EntryWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.tokyosu.itemoverlayborder.client.BorderRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntryWidget.class, remap = false)
public abstract class EntryWidgetMixin {
    @Shadow
    public abstract EntryStack<?> getCurrentEntry();

    @Shadow
    public abstract Rectangle getInnerBounds();

    @Inject(method = "drawExtra", at = @At("TAIL"))
    protected void drawExtra(GuiGraphics graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        var entry = this.getCurrentEntry();
        if (entry.getType() != VanillaEntryTypes.ITEM) return;
        ItemStack stack = entry.castValue();
        var innerBounds = this.getInnerBounds();
        BorderRenderer.render(graphics, innerBounds.x, innerBounds.y, stack);
        graphics.flush();
    }
}
