package top.auspice.yaml.validation.std;

import top.auspice.libs.snakeyaml.nodes.ScalarNode;
import top.auspice.yaml.validation.ValidationContext;
import top.auspice.yaml.validation.ValidationFailure;
import top.auspice.yaml.validation.base.NodeValidator;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

public class FixedValuedValidator implements NodeValidator {
    private final Set<String> acceptedValues;

    public FixedValuedValidator(Set<String> acceptedValues) {
        this.acceptedValues = acceptedValues;
    }

    public ValidationFailure validate(ValidationContext context) {
        if (!(context.getNode() instanceof ScalarNode)) {
            return context.err("Expected a simple scalar value here, but got a " + context.getNode().getTag().getValue() + " instead");
        } else {
            ScalarNode scalarNode = (ScalarNode)context.getNode();
            String val = scalarNode.getValue().toLowerCase(Locale.ENGLISH);
            return this.acceptedValues.contains(val) ? null : context.err("Unexpected value '" + scalarNode.getValue() + "' expected one of " + Arrays.toString(this.acceptedValues.toArray()));
        }
    }

    public String getName() {
        return "one of " + this.acceptedValues;
    }

    public String toString() {
        return "FixedValuedValidator" + this.acceptedValues;
    }
}
