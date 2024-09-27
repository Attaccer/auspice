package top.auspice.locale;

import java.util.Locale;

public enum SupportedLanguage implements StyledLanguage {

    EN(Locale.ENGLISH),
    CH(Locale.CHINESE),



    ;


    private final Locale locale;

    SupportedLanguage(Locale locale) {
        this.locale = locale;
    }

    @Override
    public String getLanguageName() {
        return null;
    }

    @Override
    public Locale getLocal() {
        return null;
    }

    @Override
    public String getStyleName() {
        return null;
    }
}
