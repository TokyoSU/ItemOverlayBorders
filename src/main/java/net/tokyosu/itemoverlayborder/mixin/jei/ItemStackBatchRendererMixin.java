package net.tokyosu.itemoverlayborder.mixin.jei;

import mezz.jei.api.ingredients.rendering.BatchRenderElement;
import mezz.jei.library.render.ItemStackRenderer;
import mezz.jei.library.render.batch.ElementWithModel;
import mezz.jei.library.render.batch.ItemStackBatchRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import net.tokyosu.itemoverlayborder.client.BorderRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = ItemStackBatchRenderer.class, remap = false, priority = 2000)
public class ItemStackBatchRendererMixin {
    @Shadow @Final @Mutable
    private List<ElementWithModel> noBlockLight;

    @Shadow @Final @Mutable
    private List<ElementWithModel> useBlockLight;

    @Shadow @Final @Mutable
    private List<BatchRenderElement<ItemStack>> customRender;

    @Inject(method = "render", at = @At("TAIL"))
    public void render(GuiGraphics guiGraphics, Minecraft minecraft, ItemRenderer itemRenderer, ItemStackRenderer itemStackRenderer, CallbackInfo ci) {
        if (!noBlockLight.isEmpty()) {
            for (final var element : noBlockLight) {
                final var stack = element.stack();
                if (stack.isEmpty()) continue;
                BorderRenderer.render(guiGraphics, element.x(), element.y(), stack);
            }
        }

        if (!useBlockLight.isEmpty()) {
            for (final var element : useBlockLight) {
                final var stack = element.stack();
                if (stack.isEmpty()) continue;
                BorderRenderer.render(guiGraphics, element.x(), element.y(), stack);
            }
        }

        for (final var element : customRender) {
            final var stack = element.ingredient();
            if (stack.isEmpty()) continue;
            BorderRenderer.render(guiGraphics, element.x(), element.y(), stack);
        }
    }
}
