package top.auspice.config.placeholder;

import java.util.Map;

public interface PlaceholderTargetsProvider extends BasePlaceholderTargetProvider {

    Map<String, Object> getTargets();

}
