package top.auspice.locale.message;

import top.auspice.locale.Locale;

public interface Messenger {


    MessageObjectProvider getProvider(Locale locale);


}
