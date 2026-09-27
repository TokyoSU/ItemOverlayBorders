package net.tokyosu.itemoverlayborder.mixin.jei;

import mezz.jei.api.ingredients.rendering.BatchRenderElement;
import mezz.jei.library.render.ItemStackRenderer;
import mezz.jei.library.render.batch.ElementWithModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import net.tokyosu.itemoverlayborder.client.BorderRenderer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Pseudo
@Mixin(
        targets = "mezz.jei.library.render.batch.ItemStackBatchRenderer",
        remap = false,
        priority = 2000
)
public class ItemStackBatchRendererMixin {
    @Shadow @Final @Mutable @NotNull
    private List<ElementWithModel> noBlockLight;

    @Shadow @Final @Mutable @NotNull
    private List<ElementWithModel> useBlockLight;

    @Shadow @Final @Mutable @NotNull
    private List<BatchRenderElement<ItemStack>> customRender;

    @Inject(method = "render", at = @At("TAIL"))
    public void render(@NotNull GuiGraphics guiGraphics, @NotNull Minecraft minecraft, @NotNull ItemRenderer itemRenderer, @NotNull ItemStackRenderer itemStackRenderer, @NotNull CallbackInfo ci) {
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
