package top.auspice.yaml.validation.std;

import top.auspice.libs.snakeyaml.error.Mark;
import top.auspice.yaml.validation.ValidationContext;
import top.auspice.yaml.validation.ValidationFailure;
import top.auspice.yaml.validation.ValidationFailure.Severity;
import top.auspice.yaml.validation.base.NodeValidator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UnionValidator implements NodeValidator {
    private final NodeValidator[] union;

    public UnionValidator(NodeValidator[] union) {
        this.union = union;
    }

    public ValidationFailure validate(ValidationContext context) {
        List<ValidationFailure> allFails = new ArrayList();
        NodeValidator[] var3 = this.union;
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            NodeValidator validator = var3[var5];
            List<ValidationFailure> capturedFails = new ArrayList();
            ValidationContext innerContext = new ValidationContext(context.getRelatedKey(), context.getNode(), context.getValidatorMap(), capturedFails);
            ValidationFailure directResult = validator.validate(innerContext);
            if (directResult == null || directResult.getSeverity() == Severity.WARNING) {
                context.getExceptions().addAll(capturedFails);
                return null;
            }

            allFails.addAll(capturedFails);
        }

        return context.fail(new ValidationFailure(Severity.ERROR, context.getNode(), (Mark)null, "None of the types matched: " + (String)allFails.stream().map(ValidationFailure::getMessage).collect(Collectors.joining(", "))));
    }

    public String getName() {
        return "one of " + Arrays.toString(this.union);
    }

    public String toString() {
        return "UnionValidator{" + Arrays.toString(this.union) + '}';
    }
}

