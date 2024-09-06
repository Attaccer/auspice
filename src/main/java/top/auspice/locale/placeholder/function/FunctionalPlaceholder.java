package top.auspice.locale.placeholder.function;

import org.jetbrains.annotations.NotNull;
import top.auspice.locale.placeholder.context.PlaceholderTranslationContext;
import top.auspice.locale.placeholder.function.annotations.PlaceholderFunction;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Function;

public abstract class FunctionalPlaceholder implements Function<PlaceholderTranslationContext, Object> {

    @NotNull
    private final CustomPlaceholderFunction function;

    public FunctionalPlaceholder() {

        CustomPlaceholderFunction function = null;

        Method[] methods = this.getClass().getMethods();

        for (Method method : methods) {
            if (function != null) {
                throw new IllegalArgumentException("Placeholder function only have one!");
            }

            PlaceholderFunction functionAnn = method.getAnnotation(PlaceholderFunction.class);
            if (functionAnn != null) {
                String functionName = method.getName();
                boolean functionOptional = functionAnn.optional();

                List<CustomPlaceholderFunctionParameter> customPlaceholderFunctionParameters = new ArrayList<>();

                for (Parameter parameter : method.getParameters()) {      //遍历此方法的的所有形参
                    top.auspice.locale.placeholder.function.annotations.PlaceholderParameter parameterAnn = parameter.getAnnotation(top.auspice.locale.placeholder.function.annotations.PlaceholderParameter.class);
                    String parameterName;
                    boolean parameterOptional;
                    if (parameterAnn != null) {
                        parameterName = parameterAnn.name();
                        parameterOptional = parameterAnn.optional();
                    } else {
                        parameterName = parameter.getName();
                        parameterOptional = false;
                    }
                    Class<?> parameterType = parameter.getType();
                    customPlaceholderFunctionParameters.add(new CustomPlaceholderFunctionParameter(parameterName, parameterType, parameterOptional));
                }

                function = new CustomPlaceholderFunction(functionName, method, customPlaceholderFunctionParameters, functionOptional);
            }
        }
        if (function == null) {
            throw new IllegalArgumentException("Need one placeholder function!");
        }

        this.function = function;

    }


    public Object apply(PlaceholderTranslationContext context) {
        return this.function.apply(context);
    }

    @NotNull
    public CustomPlaceholderFunction getFunction() {
        return this.function;
    }
}
