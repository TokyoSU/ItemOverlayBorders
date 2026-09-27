package net.tokyosu.itemoverlayborder.mixin.emi;

import dev.emi.emi.api.stack.EmiIngredient;
import net.minecraft.client.gui.GuiGraphics;
import net.tokyosu.itemoverlayborder.client.BorderRenderer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "dev.emi.emi.screen.StackBatcher", remap = false)
public abstract class StackBatcherMixin {
    @Inject(method = "render(Ldev/emi/emi/api/stack/EmiIngredient;Lnet/minecraft/client/gui/GuiGraphics;IIF)V", at = @At("TAIL"))
    public void render(@NotNull EmiIngredient ingredient, @NotNull GuiGraphics draw, int x, int y, float delta, @NotNull CallbackInfo ci) {
        final var stack = ingredient.getEmiStacks().get(0).getItemStack();
        if (stack.isEmpty()) return;
        BorderRenderer.render(draw, x, y, stack);
    }
}
