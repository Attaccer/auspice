package top.auspice.yaml.validation.std;

import top.auspice.libs.snakeyaml.nodes.Node;
import top.auspice.libs.snakeyaml.nodes.SequenceNode;
import top.auspice.yaml.validation.ValidationContext;
import top.auspice.yaml.validation.ValidationFailure;
import top.auspice.yaml.validation.base.NodeValidator;

import java.util.Iterator;

public class SequenceValidator implements NodeValidator {
    private final NodeValidator type;
    private final NodeValidator elements;

    public SequenceValidator(NodeValidator type, NodeValidator elements) {
        this.type = type;
        this.elements = elements;
    }

    public ValidationFailure validate(ValidationContext context) {
        if (!(context.getNode() instanceof SequenceNode)) {
            return context.err("Expected " + this.getName());
        } else {
            SequenceNode seq = (SequenceNode)context.getNode();
            Iterator var3 = seq.getValue().iterator();

            while(var3.hasNext()) {
                Node item = (Node)var3.next();
                this.elements.validate(context.delegate(context.getRelatedKey(), item));
            }

            this.type.validate(context);
            return null;
        }
    }

    public String getName() {
        return "a " + this.type.getName() + " of " + this.elements.getName();
    }

    public String toString() {
        return "SequenceValidator<" + this.type + ">{" + this.elements + '}';
    }
}
