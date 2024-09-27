package top.auspice.utils.internal;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.*;

@SuppressWarnings("unused")
public final class Fn {
    private static final Predicate<Boolean> TRUE_PREDICATE = (x) -> true;
    private static final Predicate<Boolean> FALSE_PREDICATE = (x) -> false;
    private static final Supplier<?> NULL_SUPPLIER = new ConstantSupplier<>(null);

    public Fn() {
    }

    public static <T> Supplier<T> supply(Supplier<T> supplier) {
        return supplier;
    }

    public static <T> Supplier<T> supply(T constant) {
        return new ConstantSupplier(constant);
    }

    public static <T> Callable<T> call(Callable<T> callable) {
        return callable;
    }

    public static <T> Predicate<T> predicate(Predicate<T> predicate) {
        return predicate;
    }

    public static <T> Consumer<T> consume(Consumer<T> consumer) {
        return consumer;
    }

    public static Runnable run(Runnable runnable) {
        return runnable;
    }

    public static <T, R> Function<T, R> function(Function<T, R> function) {
        return function;
    }

    public static <T, U, R> BiFunction<T, U, R> function(BiFunction<T, U, R> function) {
        return function;
    }

    public static <T> T cast(Object obj) {
        return (T) obj;
    }

    public static <T> @Nullable T safeCast(@NonNull Object object, Class<T> clazz) {
        Objects.requireNonNull(object, "Cannot safely cast null to generic type");
        return clazz.isInstance(object) ? (T) object : null;
    }

    public static <T> Supplier<T> nullSupplier() {
        return (Supplier<T>) NULL_SUPPLIER;
    }

    public static <T> Predicate<T> alwaysTrue() {
        return (Predicate<T>) TRUE_PREDICATE;
    }

    public static <T> Predicate<T> alwaysFalse() {
        return (Predicate<T>) FALSE_PREDICATE;
    }

    public static <T> @NonNull ChainedCallable<T> take(Callable<T> supplier) {
        return new ChainedCallable(supplier);
    }

    private static final class ConstantSupplier<T> implements Supplier<T> {
        private final T obj;

        private ConstantSupplier(T obj) {
            this.obj = obj;
        }

        public T get() {
            return this.obj;
        }
    }

    public static final class ChainedCallable<R> {
        private Callable<R> handle;
        private boolean hasResult;
        private R result;
        private RuntimeException error;

        private ChainedCallable(Callable<R> handle) {
            this.handle = Objects.requireNonNull(handle);
            this.checkResult();
        }

        private void fail(Throwable ex) {
            if (this.error == null) {
                this.error = new RuntimeException("All possibilities failed");
            }

            this.error.addSuppressed(ex);
        }

        private boolean checkResult() {
            try {
                this.result = this.handle.call();
                return this.hasResult = true;
            } catch (Throwable var2) {
                this.fail(var2);
                return false;
            }
        }

        public @NonNull ChainedCallable<R> or(Callable<R> other) {
            if (!this.hasResult) {
                this.handle = other;
                this.checkResult();
            }

            return this;
        }

        public @NonNull ChainedCallable<R> or(@Nullable R other) {
            return other == null ? this : this.or(new ConstantCallable<>(other));
        }

        public @Nullable R orNull() {
            return this.orElse(new ConstantCallable<>(null));
        }

        public boolean isPresent() {
            return this.result != null;
        }

        public @Nullable R get() {
            return this.result;
        }

        public @NonNull R orElse(R finalCheck) {
            Objects.requireNonNull(finalCheck, "Final non-lambda object cannot be null");
            return this.orElse((new ConstantCallable<>(finalCheck)));
        }

        public @Nullable R orElse(Callable<R> finalCheck) {
            if (!this.hasResult) {
                this.handle = finalCheck;
                this.checkResult();
            }

            if (this.hasResult) {
                return this.result;
            } else {
                throw this.error;
            }
        }
    }

    private static final class ConstantCallable<T> implements Callable<T> {
        private final T obj;

        private ConstantCallable(T obj) {
            this.obj = obj;
        }

        public T call() {
            return this.obj;
        }
    }
}
