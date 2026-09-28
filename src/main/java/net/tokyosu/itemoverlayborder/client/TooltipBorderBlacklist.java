package net.tokyosu.itemoverlayborder.client;

import java.lang.reflect.Method;
import java.util.List;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

public final class TooltipBorderBlacklist {
	private static final String COMPOSITE = "dev.obscuria.fragmentum.client.CompositeClientTooltipComponentImpl";
    private static final String STACK_BUFFER = "dev.obscuria.tooltips.client.component.StackBuffer";
    private static Method getComponentsMethod;
    private static boolean lookupDone;
    
    public static boolean containsObscureTooltip(List<ClientTooltipComponent> components) {
        for (ClientTooltipComponent component : components) {
            if (containsObscureTooltip(component)) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsObscureTooltip(ClientTooltipComponent component) {
        if (component == null) {
            return false;
        }

        Class<?> type = component.getClass();
        String className = type.getName();

        if (STACK_BUFFER.equals(className)) {
            return true;
        }

        if (!COMPOSITE.equals(className)) {
            return false;
        }

        Method getter = getComponentsGetter(type);
        if (getter == null) {
            return false;
        }

        try {
            Object result = getter.invoke(component);

            if (!(result instanceof List<?> children)) {
                return false;
            }

            for (Object child : children) {
                if (child instanceof ClientTooltipComponent tooltipComponent && containsObscureTooltip(tooltipComponent)) {
                    return true;
                }
            }
        } catch (ReflectiveOperationException ignored) {
        }

        return false;
    }

    private static Method getComponentsGetter(Class<?> type) {
        if (lookupDone) {
            return getComponentsMethod;
        }

        lookupDone = true;

        try {
            getComponentsMethod = type.getMethod("getComponents");
        } catch (NoSuchMethodException ignored) {
            getComponentsMethod = null;
        }

        return getComponentsMethod;
    }
}
