package top.auspice.yaml.transformars.base;

import java.util.HashMap;
import java.util.Map;

public class TransformerRegistry {

    public static TransformerRegistry INSTANCE;

    private final Map<Class<?>, Transformer<?>> transformers = new HashMap<>();


    public static <T> void register(Transformer<T> transformer) {
        INSTANCE.transformers.put(transformer.getType(), transformer);
    }

    public <T> Transformer<T> getTransformer(Class<T> type) {
        return (Transformer<T>) transformers.get(type);
    }
    public Map<Class<?>, Transformer<?>> getTransformers() {
        return transformers;
    }


}
