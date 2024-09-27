package top.auspice.data;

import java.util.Map;

public final class Pair<K, V> implements Map.Entry<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }

    public static <K, V> Pair<K, V> of(Map.Entry<K, V> entry) {
        return new Pair<>(entry.getKey(), entry.getValue());
    }

    public static <K, V> Pair<K, V> empty() {
        return new Pair<>(null, null);
    }

    public K getKey() {
        return this.key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return this.value;
    }

    public V setValue(V value) {
        return this.value = value;
    }

    public boolean isKeyPresent() {
        return this.key != null;
    }

    public boolean isValuePresent() {
        return this.value != null;
    }

    public boolean areBothPresent() {
        return this.isKeyPresent() && this.isValuePresent();
    }

    public boolean areBothNull() {
        return !this.isKeyPresent() && !this.isValuePresent();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Map.Entry)) {
            return false;
        } else {
            label43: {
                label29: {
                    Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
                    if (this.key == null) {
                        if (entry.getKey() != null) {
                            break label29;
                        }
                    } else if (!this.key.equals(entry.getKey())) {
                        break label29;
                    }

                    if (this.value == null) {
                        if (entry.getValue() == null) {
                            break label43;
                        }
                    } else if (this.value.equals(entry.getValue())) {
                        break label43;
                    }
                }

                return false;
            }

            return true;
        }
    }

    public int hashCode() {
        return (this.key == null ? 0 : this.key.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
    }

    public String toString() {
        return "Pair{" + this.key + " | " + this.value + '}';
    }
}
