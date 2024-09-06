package top.auspice.locale.compiler.placeholders;

import top.auspice.locale.compiler.placeholders.modifiers.PlaceholderModifier;

import java.util.List;

public interface Placeholder {



    String getOriginalString();

    List<PlaceholderModifier> getModifiers();

    /**
     *
     * @return 占位符的全部字符, 不包括最外边的两个'%'
     */
    String getFullString();


}
