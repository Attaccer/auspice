package top.auspice.locale;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

import top.auspice.utils.string.Strings;

import static top.auspice.NamingContract.LOCALE_ENTRY_PATH;
import static top.auspice.NamingContract.LOCALE_ENTRY_PATH_SECTION;

public final class LocaleEntry {
    public static final Pattern ACCEPTED_LOCALE_ENTRY_PATH = Pattern.compile(LOCALE_ENTRY_PATH);
    public static final Pattern ACCEPTED_LOCALE_ENTRY_PATH_SECTION = Pattern.compile(LOCALE_ENTRY_PATH_SECTION);
    private final String[] path;

    public LocaleEntry(String[] path) {
        this.path = Objects.requireNonNull(path);
        for (String s : path) {
            if (!ACCEPTED_LOCALE_ENTRY_PATH_SECTION.matcher(s).matches()) {
                throw new IllegalArgumentException("Excepted path name: " + Arrays.toString(path));
            }
        }
    }

    public String[] getPath() {
        return this.path;
    }

    public final int hashCode() {
        int var1 = 1;
        String[] var2;
        int var3 = (var2 = this.path).length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            var1 = var1 * 31 + var5.hashCode();
        }

        return var1;
    }

    public static LocaleEntry fromConfig(@org.intellij.lang.annotations.Pattern(LOCALE_ENTRY_PATH) String var0) {
        return new LocaleEntry(Strings.splitArray(var0, '.'));
    }

    public static boolean isValidConfigLanguageEntry(String var0) {
        return ACCEPTED_LOCALE_ENTRY_PATH.matcher(var0).matches();
    }

    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        LocaleEntry that;
        if ((that = (LocaleEntry)obj).path.length != this.path.length) {
            return false;
        } else {
            for(int var2 = 0; var2 < this.path.length; ++var2) {
                if (!this.path[var2].equals(that.path[var2])) {
                    return false;
                }
            }

            return true;
        }
    }

    public String toString() {
        return "LanguageEntry{" + Arrays.toString(this.path) + '}';
    }
}
