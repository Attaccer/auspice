package top.auspice.locale.message.placeholder.target;

import java.util.HashMap;
import java.util.Map;

public class PlaceholderTargets {

    private final Map<Class<?>, Object> primaryTargets;
    private final Map<Class<?>, Object> secondaryTargets;
    private final Map<Class<?>, Object> otherTargets;


    public PlaceholderTargets() {
        this.primaryTargets = new HashMap<>();
        this.secondaryTargets = new HashMap<>();
        this.otherTargets = new HashMap<>();
    }


    public PlaceholderTargets ofPrimary(Class<?> type, Object target) {
        if (!type.isInstance(target)) {
            throw new IllegalArgumentException("target class isn't type.");
        }
        this.primaryTargets.put(type, target);
        return this;
    }

    public PlaceholderTargets ofSecondary(Class<?> type, Object other) {
        if (!type.isInstance(other)) {
            throw new IllegalArgumentException("target class isn't type.");
        }
        this.secondaryTargets.put(type, other);
        return this;
    }

    public PlaceholderTargets ofOther(Class<?> type, Object other) {
        if (!type.isInstance(other)) {
            throw new IllegalArgumentException("target class isn't type.");
        }
        this.otherTargets.put(type, other);
        return this;
    }


    public Object getPrimary(Class<?> type) {
        return this.primaryTargets.get(type);
    }
    public Object getSecondary(Class<?> type) {
        return this.secondaryTargets.get(type);
    }
    public Object getOther(Class<?> type) {
        return this.otherTargets.get(type);
    }


}
