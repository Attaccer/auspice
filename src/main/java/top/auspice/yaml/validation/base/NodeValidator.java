package top.auspice.yaml.validation.base;

import top.auspice.yaml.validation.ValidationContext;
import top.auspice.yaml.validation.ValidationFailure;

public interface NodeValidator {
    ValidationFailure validate(ValidationContext var1);

    default String getName() {
        return "a " + this.getClass().getSimpleName();
    }
}
