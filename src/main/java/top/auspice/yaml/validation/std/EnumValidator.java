package top.auspice.yaml.validation.std;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import top.auspice.libs.snakeyaml.nodes.ScalarNode;
import top.auspice.yaml.validation.ValidationContext;
import top.auspice.yaml.validation.ValidationFailure;
import top.auspice.yaml.validation.base.NodeValidator;

import java.util.Locale;
import java.util.Objects;


public class EnumValidator implements NodeValidator {
    private final Class enumerator;

    public EnumValidator(Class enumerator) {
        this.enumerator = Objects.requireNonNull(enumerator);
    }

    public ValidationFailure validate(ValidationContext context) {
        if (!(context.getNode() instanceof ScalarNode)) {
            return context.err("Expected a " + this.enumerator.getSimpleName() + " type, but got an option of type '" + context.getNode().getTag().getValue());
        } else {
            ScalarNode scalarNode = (ScalarNode)context.getNode();

            try {
                Enum enumerate = Enum.valueOf(this.enumerator, scalarNode.getValue().toUpperCase(Locale.ENGLISH));
                scalarNode.cacheConstructed(enumerate);
                return null;
            } catch (IllegalArgumentException var4) {
                return context.err("Expected a " + this.enumerator.getSimpleName() + " type, but got '" + scalarNode.getValue() + '\'');
            }
        }
    }

    public String getName() {
        return this.enumerator.getSimpleName();
    }

    public String toString() {
        return "EnumValidator{" + this.enumerator.getName() + '}';
    }
}

