package top.auspice.yaml.validation.std;

import top.auspice.libs.snakeyaml.nodes.Node;
import top.auspice.libs.snakeyaml.nodes.SequenceNode;
import top.auspice.yaml.validation.ValidationContext;
import top.auspice.yaml.validation.ValidationFailure;
import top.auspice.yaml.validation.base.NodeValidator;

import java.util.*;
import java.util.function.IntFunction;

public class StandardSequenceValidator implements NodeValidator {
    private final Type type;
    private final int minLen;
    private final int maxLen;

    public StandardSequenceValidator(Type type, int minLen, int maxLen) {
        if ((minLen != 0 || maxLen != 0) && minLen >= maxLen) {
            throw new IllegalArgumentException("Validation range cannot be equal or smaller one greater than the bigger one: " + minLen + " - " + maxLen);
        } else {
            this.type = (Type) Objects.requireNonNull(type);
            this.minLen = minLen;
            this.maxLen = maxLen;
        }
    }

    public String toString() {
        return "StandardSequenceValidator<" + this.type + '>' + (this.maxLen == 0 && this.minLen == 0 ? "" : "{" + this.minLen + '-' + this.maxLen + '}');
    }

    public ValidationFailure validate(ValidationContext context) {
        if (!(context.getNode() instanceof SequenceNode seq)) {
            return context.err("Standard sequence validation cannot be used on node: " + context.getNode().getClass().getSimpleName());
        } else {
            int length = seq.getValue().size();
            if (this.minLen != 0 || this.maxLen != 0) {
                if (length < this.minLen) {
                    return context.err("Value's length must be greater than " + this.minLen);
                }

                if (length > this.maxLen) {
                    return context.err("Value's length must be less than " + this.maxLen);
                }
            }

            Collection<Object> collection = this.type.constructor.apply(length);

            for (Node item : seq.getValue()) {
                String parsed = String.valueOf(item.getParsed());
                boolean changed = collection.add(parsed);
                if (!changed && this.type == Type.SET) {
                    context.delegate(context.getRelatedKey(), item).warn("Duplicated value '" + parsed + "' in set");
                }
            }

            return null;
        }
    }

    public String getName() {
        return this.type.name;
    }

    public static Type getStandardType(String str) {
        switch (str) {
            case "list":
                return StandardSequenceValidator.Type.LIST;
            case "set":
                return StandardSequenceValidator.Type.SET;
            default:
                return null;
        }
    }

    public enum Type {
        LIST("list", (len) -> {
            return new ArrayList(len);
        }),
        SET("set", (len) -> {
            return new HashSet(len);
        });

        private final String name;
        private final IntFunction<Collection<Object>> constructor;

        private Type(String name, IntFunction constructor) {
            this.name = name;
            this.constructor = constructor;
        }

        public IntFunction<Collection<Object>> getConstructor() {
            return this.constructor;
        }

        public String getName() {
            return this.name;
        }
    }
}

