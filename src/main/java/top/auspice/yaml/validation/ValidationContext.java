package top.auspice.yaml.validation;

import top.auspice.libs.snakeyaml.nodes.Node;
import top.auspice.yaml.validation.ValidationFailure.Severity;
import top.auspice.yaml.validation.base.NodeValidator;

import java.util.Collection;
import java.util.Map;


public final class ValidationContext {
    private final Node relatedKey;
    private final Node node;
    private final Map<String, NodeValidator> validatorMap;
    private final Collection<ValidationFailure> exceptions;

    public ValidationContext(Node relatedKey, Node node, Map<String, NodeValidator> validatorMap, Collection<ValidationFailure> exceptions) {
        this.relatedKey = relatedKey;
        this.node = node;
        this.validatorMap = validatorMap;
        this.exceptions = exceptions;
    }

    public ValidationContext(Node node, Map<String, NodeValidator> validatorMap, Collection<ValidationFailure> exceptions) {
        this(null, node, validatorMap, exceptions);
    }

    public ValidationContext delegate(Node relatedKey, Node node) {
        return new ValidationContext(relatedKey, node, this.validatorMap, this.exceptions);
    }

    public Node getRelatedKey() {
        return this.relatedKey;
    }

    public Map<String, NodeValidator> getValidatorMap() {
        return this.validatorMap;
    }

    public Collection<ValidationFailure> getExceptions() {
        return this.exceptions;
    }

    public ValidationFailure fail(ValidationFailure failure) {
        this.exceptions.add(failure);
        return failure;
    }

    public ValidationFailure err(String message) {
        return this.fail(new ValidationFailure(Severity.ERROR, this.node, null, message));
    }

    public void warn(String message) {
        this.fail(new ValidationFailure(Severity.WARNING, this.node, null, message));
    }

    public Node getNode() {
        return this.node;
    }

    public NodeValidator getValidator(String name) {
        return (NodeValidator)this.validatorMap.get(name);
    }
}
