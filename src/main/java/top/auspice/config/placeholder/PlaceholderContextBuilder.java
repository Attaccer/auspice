package top.auspice.config.placeholder;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

public class PlaceholderContextBuilder implements InternalPlaceholderProvider, PlaceholderTargetsProvider {

    protected Map<String, Object> variables;

    protected Object primaryTarget;
    protected Object secondaryTarget;
    protected Map<String, Object> targets;


    @Override
    public Object providePlaceholder(String varName) {
        return variables == null ? null : variables.get(varName);
    }

    @Override
    public @Nullable Object getPrimaryTarget() {
        return this.primaryTarget;
    }

    @Override
    public void setPrimaryTarget(Object primaryTarget) {
        this.primaryTarget = primaryTarget;
    }

    @Override
    public @Nullable Object getSecondaryTarget() {
        return this.secondaryTarget;
    }

    @Override
    public void setSecondaryTarget(Object secondaryTarget) {
        this.secondaryTarget = secondaryTarget;
    }

    @Override
    public Map<String, Object> getTargets() {
        return this.targets;
    }
}
