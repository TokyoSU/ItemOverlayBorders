package net.tokyosu.itemoverlayborder.mixin.minecraft;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tokyosu.itemoverlayborder.client.BorderRenderer;
import net.tokyosu.itemoverlayborder.client.TooltipRenderContext;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin {
    /**
     * Draw the rarity border once the actual item model has finished rendering.
     * Hooking the innermost GuiGraphics#renderItem overload means every normal
     * GUI item path (container slots, hotbar, etc.) reaches this point exactly
     * once, including custom item models such as Re:Avaritia's halo/cosmic
     * rendering. The caller can then render stack count/durability decorations
     * afterward, keeping those above the border.
     */
    @Inject(method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V", at = @At("HEAD"))
    private void itemoverlayborder$renderItemBorder(@NotNull LivingEntity entity, @NotNull Level level, @NotNull ItemStack stack, int x, int y, int seed, int z, @NotNull CallbackInfo ci) {
        if (stack.isEmpty()) return;
        // Obscure Tooltips' large rotating preview renders its item at (0, 0).
        // Do not suppress the normal tooltip icon or JEI items.
        if (TooltipRenderContext.isInsideObscureTooltip() && x == 0 && y == 0) {
            return;
        }
        BorderRenderer.render((GuiGraphics)(Object)this, x, y, stack);
    }
}
