package net.tokyosu.itemoverlayborder.mixin.rei;

import org.apache.commons.lang3.mutable.*;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.impl.client.gui.widget.EntryWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.tokyosu.itemoverlayborder.client.BorderRenderer;

@Pseudo
@Mixin(targets = "me.shedaniel.rei.impl.client.gui.widget.BatchedEntryRendererManager", remap = false)
public abstract class BatchedEntryRendererManagerMixin {
	@Inject(method = "renderBatched", at = @At(value = "INVOKE", target = "Lme/shedaniel/rei/api/client/entry/renderer/BatchedEntryRenderer;startBatch(Lme/shedaniel/rei/api/common/entry/EntryStack;Ljava/lang/Object;Lnet/minecraft/client/gui/GuiGraphics;F)V", shift = At.Shift.BEFORE), require = 1)
	private static <T extends EntryWidget> void itemoverlayborder$renderBorders(boolean debugTime, @NotNull MutableInt size, @NotNull MutableLong time, @NotNull GuiGraphics graphics, int mouseX, int mouseY, float delta, @NotNull Iterable<T> entries, @NotNull Object[] extraData, @NotNull CallbackInfo ci) {
	    for (T widget : entries) {
	        EntryStack<?> entry = widget.getCurrentEntry();

	        if (entry.isEmpty() || entry.getType() != VanillaEntryTypes.ITEM) {
	            continue;
	        }

	        ItemStack stack = entry.castValue();

	        if (stack.isEmpty()) {
	            continue;
	        }

	        Rectangle bounds = widget.getInnerBounds();
	        BorderRenderer.render(graphics, bounds.x, bounds.y, stack);
	    }
	}
}
