package top.auspice.config.compilers.message.placeholders;

import top.auspice.locale.message.placeholder.modifier.PlaceholderModifier;

import java.util.List;

public interface PlaceholderObject {


    String getOriginalString();

    List<PlaceholderModifier> getModifiers();

    /**
     *
     * @return 占位符的全部字符, 不包括最外边的两个'%'
     */
    String getFullString();


}
