package top.auspice.config.placeholder;

import org.checkerframework.checker.nullness.qual.Nullable;

public interface BasePlaceholderTargetProvider {

    BasePlaceholderTargetProvider EMPTY = new BasePlaceholderTargetProvider() {
        @Override
        public @Nullable Object getPrimaryTarget() {
            return null;
        }

        @Override
        public void setPrimaryTarget(Object primaryTarget) {

        }

        @Override
        public @Nullable Object getSecondaryTarget() {
            return null;
        }

        @Override
        public void setSecondaryTarget(Object secondaryTarget) {

        }
    };

    @Nullable
    Object getPrimaryTarget();
    void setPrimaryTarget(Object primaryTarget);
    @Nullable
    Object getSecondaryTarget();
    void setSecondaryTarget(Object secondaryTarget);

}
