package net.tokyosu.itemoverlayborder;

import net.minecraftforge.common.ForgeConfigSpec;

public class ItemOverlayConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static ForgeConfigSpec.BooleanValue DISABLE_ANIMATION;

    static {
        BUILDER.push("Item Overlay Border Config");
        DISABLE_ANIMATION = BUILDER.comment("Remove animation from the item overlay, will be just a colored square.").define("Disable animation", false);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
