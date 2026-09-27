package net.tokyosu.itemoverlayborder.mixin.rei;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.impl.client.gui.widget.EntryWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.tokyosu.itemoverlayborder.client.BorderRenderer;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(value = EntryWidget.class, remap = false)
public abstract class EntryWidgetMixin {
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lme/shedaniel/rei/impl/client/gui/widget/EntryWidget;drawCurrentEntry(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", shift = At.Shift.BEFORE), require = 1)
    private void itemoverlayborder$renderBorder(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float delta, @NotNull CallbackInfo ci) {
        EntryWidget widget = (EntryWidget)(Object)this;
        EntryStack<?> entry = widget.getCurrentEntry();
        if (entry.isEmpty() || entry.getType() != VanillaEntryTypes.ITEM) return;

        ItemStack stack = entry.castValue();
        if (stack.isEmpty()) return;

        Rectangle bounds = widget.getInnerBounds();
        BorderRenderer.render(graphics, bounds.x, bounds.y, stack);
    }
}
