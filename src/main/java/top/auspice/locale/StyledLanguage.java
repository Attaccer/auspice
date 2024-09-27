package top.auspice.locale;

public interface StyledLanguage extends Language, Styled {

    @Override
    default String getLocaleName() {
        return this.getLanguageName() + '-' + this.getStyleName();
    }
}
