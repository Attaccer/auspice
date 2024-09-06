package top.auspice.locale.placeholder.target;

import java.util.HashMap;
import java.util.Map;

public class PlaceholderTargets {

    private final Map<Class<?>, Object> targets;
    private final Map<Class<?>, Object> others;

    public PlaceholderTargets() {
        this.targets = new HashMap<>();
        this.others = new HashMap<>();
    }


    public PlaceholderTargets of(Class<?> type, Object target) {
        if (!type.isInstance(target)) {
            throw new IllegalArgumentException("target class isn't type.");
        }
        this.targets.put(type, target);
        return this;
    }

    public PlaceholderTargets ofOther(Class<?> type, Object other) {
        if (!type.isInstance(other)) {
            throw new IllegalArgumentException("target class isn't type.");
        }
        this.others.put(type, other);
        return this;
    }


    public Object get(Class<?> type) {
        return this.targets.get(type);
    }

    public Object getOther(Class<?> type) {
        return this.others.get(type);
    }


}
