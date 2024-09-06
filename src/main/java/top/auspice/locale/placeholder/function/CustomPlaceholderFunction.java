package top.auspice.locale.placeholder.function;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import top.auspice.locale.placeholder.context.PlaceholderTranslationContext;
import top.auspice.locale.placeholder.function.annotations.PlaceholderFunction;
import top.auspice.locale.placeholder.function.exceptions.MultiplePlaceholderFunctionException;
import top.auspice.locale.placeholder.function.exceptions.NoEnoughPlaceholderFunctionException;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;

public abstract class CustomPlaceholderFunction implements Function<PlaceholderTranslationContext, Object> {
    @NotNull
    private final String functionName;
    @NotNull
    private final Method method;
    @NotNull
    private final List<CustomPlaceholderFunctionParameter> customPlaceholderFunctionParameters;

    private final boolean optional;


    public CustomPlaceholderFunction(@NotNull String functionName, @NotNull Method method, @NotNull List<CustomPlaceholderFunctionParameter> customPlaceholderFunctionParameters, boolean optional) {
        Objects.requireNonNull(functionName);
        Objects.requireNonNull(method);
        Objects.requireNonNull(customPlaceholderFunctionParameters);
        this.functionName = functionName;
        this.method = method;
        this.customPlaceholderFunctionParameters = customPlaceholderFunctionParameters;
        this.optional = optional;
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


    }




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
        return this.customPlaceholderFunctionParameters;
    }

    public boolean isOptional() {
        return optional;
    }

    @Nullable
    public Object apply(@NotNull PlaceholderTranslationContext context) {
        Map<String>





        return null;
    }



}
