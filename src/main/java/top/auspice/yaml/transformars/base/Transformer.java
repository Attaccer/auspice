package top.auspice.yaml.transformars.base;

import java.util.function.Function;

public interface Transformer<O> extends Function<String, O> {

    O transform(String s);

    Class<O> getType();

    @Override
    default O apply(String s) {
        return transform(s);
    }
}
