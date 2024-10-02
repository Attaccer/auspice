package top.auspice.locale;

import java.util.Locale;

public enum SupportedLocale implements StyledLanguage {

    EN(Locale.ENGLISH),
    CH(Locale.CHINESE),



    ;


    private final Locale locale;

    SupportedLocale(Locale locale) {
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
