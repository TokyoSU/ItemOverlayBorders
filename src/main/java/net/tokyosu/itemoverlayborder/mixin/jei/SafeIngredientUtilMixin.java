package net.tokyosu.itemoverlayborder.mixin.jei;

import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.common.util.SafeIngredientUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.tokyosu.itemoverlayborder.client.BorderRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SafeIngredientUtil.class, remap = false)
public class SafeIngredientUtilMixin {
    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lmezz/jei/api/ingredients/IIngredientRenderer;Lmezz/jei/api/ingredients/IIngredientType;Ljava/lang/Object;II)V", at = @At("TAIL"))
    private static <T> void render(GuiGraphics guiGraphics, IIngredientRenderer<T> ingredientRenderer, IIngredientType<T> ingredientType, T ingredient, int x, int y, CallbackInfo ci) {
        if (ingredient instanceof ItemStack stack) {
            if (stack.isEmpty()) return;
            BorderRenderer.render(guiGraphics, x, y, stack);
        }
    }
}
