package top.auspice.locale.message.placeholder.function;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import top.auspice.locale.message.placeholder.PlaceholderTranslationContext;
import top.auspice.locale.message.placeholder.function.annotations.PlaceholderFunction;
import top.auspice.locale.message.placeholder.function.annotations.PlaceholderParameter;
import top.auspice.locale.message.placeholder.function.exceptions.MultiplePlaceholderFunctionException;
import top.auspice.locale.message.placeholder.function.exceptions.NoEnoughPlaceholderFunctionException;
import top.auspice.locale.message.placeholder.function.exceptions.WrongPlaceholderParameterInputException;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Function;

public abstract class CustomPlaceholderFunction implements Function<PlaceholderTranslationContext, Object> {
    @NotNull
    private final String functionName;
    @NotNull
    private final Method method;
    @NotNull
    private final List<CustomPlaceholderFunctionParameter> parameters;



    public CustomPlaceholderFunction(@NotNull String functionName, @NotNull Method method, @NotNull List<CustomPlaceholderFunctionParameter> parameters) {
        Objects.requireNonNull(functionName);
        Objects.requireNonNull(method);
        Objects.requireNonNull(parameters);
        this.functionName = functionName;
        this.method = method;
        this.parameters = parameters;
    }


    public CustomPlaceholderFunction(@NotNull String functionName) throws MultiplePlaceholderFunctionException, NoEnoughPlaceholderFunctionException {
        Objects.requireNonNull(functionName);
        this.functionName = functionName;
        Method cms = null;
        for (Method pendingMethod : this.getClass().getMethods()) {
            if (pendingMethod.getAnnotation(PlaceholderFunction.class) != null) {
                if (cms != null) {
                    throw new MultiplePlaceholderFunctionException("");
                } else {
                    cms = pendingMethod;
                }
            }
        }
        if (cms == null) {
            throw new NoEnoughPlaceholderFunctionException("");
        }
        this.method = cms;
        List<CustomPlaceholderFunctionParameter> parameters = new ArrayList<>();
        for (Parameter parameter : cms.getParameters()) {
            boolean optional = false;
            String paramName = null;

            for (Annotation annotation : parameter.getAnnotations()) {
                if (annotation.annotationType() == PlaceholderParameter.class) {
                    paramName = ((PlaceholderParameter) annotation).name();
                    optional = ((PlaceholderParameter) annotation).optional();
                    continue;
                }
                String anName = annotation.getClass().getSimpleName();
                if (anName.contains("Nullable")) {
                    optional = true;
                }
            }
            if (paramName == null) {
                paramName = parameter.getName();
            }
            parameters.add(new CustomPlaceholderFunctionParameter(paramName, parameter.getType(), optional));
        }
        this.parameters = parameters;
    }


    @Deprecated()
    @NotNull
    public String getFunctionName() {
        return this.functionName;
    }

    @NotNull
    public Method getMethod() {
        return this.method;
    }

    @NotNull
    public List<CustomPlaceholderFunctionParameter> getParameters() {
        return this.parameters;
    }


    @Nullable
    public Object apply(@NotNull PlaceholderTranslationContext context) {
        List<CustomPlaceholderFunctionParameter> requiredParams = this.parameters;
        Object[] methodInvoker = new Object[requiredParams.size()];
        Map<String, Object> providedParams = context.getFunctionsInfo();
        if (providedParams != null && !providedParams.isEmpty()) {
            for (int j = 0, size = requiredParams.size(); j < size; j++) {
                CustomPlaceholderFunctionParameter requiredParameter = requiredParams.get(j);
                Object providedObject;
                if ((providedObject = providedParams.get(requiredParameter.getName())) == null) {
                    if (requiredParameter.isOptional()) {
                        methodInvoker[j] = null;
                    } else {
                        throw new WrongPlaceholderParameterInputException("Can not find custom placeholder function parameter: '" + requiredParameter.getName() + '\'');
                    }
                } else {
                    methodInvoker[j] = providedObject;
                }
            }
        }

        try {
            return this.method.invoke(methodInvoker);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }

}
