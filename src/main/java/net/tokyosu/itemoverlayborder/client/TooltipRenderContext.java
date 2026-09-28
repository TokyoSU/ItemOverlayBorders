package net.tokyosu.itemoverlayborder.client;

public final class TooltipRenderContext {
	private static final ThreadLocal<Integer> OBSCURE_DEPTH = ThreadLocal.withInitial(() -> 0);

    public static void enterObscureTooltip() {
        OBSCURE_DEPTH.set(OBSCURE_DEPTH.get() + 1);
    }

    public static void exitObscureTooltip() {
        int depth = OBSCURE_DEPTH.get();
        if (depth <= 1) {
            OBSCURE_DEPTH.remove();
        } else {
            OBSCURE_DEPTH.set(depth - 1);
        }
    }

    public static boolean isInsideObscureTooltip() {
        return OBSCURE_DEPTH.get() > 0;
    }
}
