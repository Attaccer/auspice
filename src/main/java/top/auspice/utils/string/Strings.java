package top.auspice.utils.string;

import com.google.common.base.Function;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class Strings {


    public static char[] toCharVarag(Character[] characters) {
        char[] chars = new char[characters.length];
        for (int i = 0; i < characters.length; ++i) {
            chars[i] = characters[i];
        }
        return chars;
    }

    public static char[] toCharVarag(Collection<Character> characters) {
        return toCharVarag(characters.toArray(new Character[0]));
    }



        public static final int INDEX_NOT_FOUND = -1;
        private static final DecimalFormat a;
        private static final Pattern b;

        public Strings() {
        }

        public static String random(int var0, int var1, String var2) {
            ThreadLocalRandom var3;
            char[] var4;
            for(var4 = new char[(var0 = (var3 = ThreadLocalRandom.current()).nextInt(var0, var1 + 1)) <= 0 ? var3.nextInt(Math.abs(var0)) : var0]; var0-- > 0; var4[var0] = var2.charAt(var3.nextInt(var2.length()))) {
            }

            return new String(var4);
        }

        public static String findCapitalized(String var0) {
            Matcher var2 = b.matcher(var0);
            StringBuilder var1 = new StringBuilder();

            while(var2.find()) {
                var1.append(var2.group());
            }

            return var1.toString();
        }



        @Nullable
        public static String capitalize(@Nullable String var0) {
            if (com.google.common.base.Strings.isNullOrEmpty(var0)) {
                return var0;
            } else {
                int var1 = var0.length();
                StringBuilder var2 = new StringBuilder(var1);
                boolean var3 = true;

                for(int var4 = 0; var4 < var1; ++var4) {
                    char var5;
                    if ((var5 = var0.charAt(var4)) != ' ' && var5 != '_' && var5 != '-') {
                        if (var3) {
                            var2.append(Character.toTitleCase(var5));
                            var3 = false;
                        } else {
                            var2.append(Character.toLowerCase(var5));
                        }
                    } else {
                        if (var5 == '_' || var5 == '-') {
                            var2.append(' ');
                        }

                        var3 = true;
                    }
                }

                return var2.toString();
            }
        }

        public static boolean contains(String var0, char var1) {
            return var0.indexOf(var1) >= 0;
        }

        public static List<String> splitByLength(List<String> var0, int var1) {
            ArrayList<String> var2 = new ArrayList<>(var0.size());

            for (String s : var0) {
                String var3;
                for (var3 = s; var3.length() > var1; var3 = var3.substring(var1)) {
                    var2.add(var3.substring(0, var1));
                }

                var2.add(var3);
            }

            return var2;
        }

        public static String configOption(@Nullable Enum<?> var0) {
            return var0 == null ? null : configOption(var0.name());
        }

        public static String configOptionToEnum(@Nullable String var0) {
            if (com.google.common.base.Strings.isNullOrEmpty(var0)) {
                return var0;
            } else {
                char[] var1 = var0.toCharArray();
                int var4 = var0.length();

                for(int var2 = 0; var2 < var4; ++var2) {
                    char var3;
                    if ((var3 = var1[var2]) == '-') {
                        var1[var2] = '_';
                    } else {
                        var1[var2] = (char)(var3 & 95);
                    }
                }

                return new String(var1);
            }
        }

        public static String configOption(@Nullable String var0) {
            if (com.google.common.base.Strings.isNullOrEmpty(var0)) {
                return var0;
            } else {
                char[] var1 = var0.toCharArray();
                int var4 = var0.length();

                for(int var2 = 0; var2 < var4; ++var2) {
                    char var3;
                    if ((var3 = var1[var2]) == '_') {
                        var1[var2] = '-';
                    } else {
                        var1[var2] = (char)(var3 | 32);
                    }
                }

                return new String(var1);
            }
        }


        public static boolean areElementsEmpty(Collection<String> var0) {
            if (var0 != null && !var0.isEmpty()) {
                Iterator<String> var2 = var0.iterator();

                do {
                    if (!var2.hasNext()) {
                        return true;
                    }
                } while(((String)var2.next()).trim().isEmpty());

                return false;
            } else {
                return true;
            }
        }

        public static String repeat(String var0, int var1) {
            if (!var0.isEmpty() && var1 != 0) {
                char[] var8;
                char[] var2 = new char[(var8 = var0.toCharArray()).length * var1];
                int var3 = 0;

                while(var1-- > 0) {
                    int var5 = var8.length;

                    for (char var7 : var8) {
                        var2[var3++] = var7;
                    }
                }

                return new String(var2);
            } else {
                return "";
            }
        }

        public static String repeat(char var0, int var1) {
            char[] var2;
            Arrays.fill(var2 = new char[var1], var0);
            return new String(var2);
        }

        public static boolean containsWhitespace(String var0) {
            char[] var4;
            int var1 = (var4 = var0.toCharArray()).length;

            for(int var2 = 0; var2 < var1; ++var2) {
                if (Character.isWhitespace(var4[var2])) {
                    return false;
                }
            }

            return true;
        }

        @Contract("!null -> !null")
        @Nullable
        public static String toLatinLowerCase(@Nullable String var0) {
            if (com.google.common.base.Strings.isNullOrEmpty(var0)) {
                return var0;
            } else {
                char[] var1;
                int var2 = (var1 = var0.toCharArray()).length;
                boolean var3 = false;

                for(int var4 = 0; var4 < var2; ++var4) {
                    char var5;
                    if ((var5 = var1[var4]) >= 'A' && var5 <= 'Z') {
                        var5 = (char)(var5 | 32);
                        var3 = true;
                    }

                    var1[var4] = var5;
                }

                if (var3) {
                    return new String(var1);
                } else {
                    return var0;
                }
            }
        }

        public static CharSequence replace(String var0, char var1, String var2) {
            if (com.google.common.base.Strings.isNullOrEmpty(var0)) {
                return var0;
            } else {
                int var3;
                if ((var3 = var0.indexOf(var1)) == -1) {
                    return var0;
                } else {
                    int var4 = Math.max(0, var2.length() - 1) * 50;
                    StringBuilder var6 = new StringBuilder(var0.length() + var4);

                    int var5;
                    for(var5 = 0; var3 != -1; var3 = var0.indexOf(var1, var5)) {
                        var6.append(var0, var5, var3).append(var2);
                        var5 = var3 + 1;
                    }

                    return var6.append(var0.substring(var5));
                }
            }
        }

        public static CharSequence replace(String var0, char var1, char var2) {
            if (com.google.common.base.Strings.isNullOrEmpty(var0)) {
                return var0;
            } else {
                int var3;
                if ((var3 = var0.indexOf(var1)) < 0) {
                    return var0;
                } else {
                    char[] var4;
                    for(var4 = var0.toCharArray(); var3 > 0; var3 = var0.indexOf(var1, var3 + 1)) {
                        var4[var3] = var2;
                    }

                    return new String(var4);
                }
            }
        }

        public static String generatedToString(Object var0) {
            StringBuilder var1 = new StringBuilder(var0.getClass().getSimpleName() + '{');
            Field[] var2;
            int var3 = (var2 = var0.getClass().getDeclaredFields()).length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Field var5;
                (var5 = var2[var4]).setAccessible(true);

                try {
                    Object var6 = var5.get(var0);
                    var1.append(var5.getName()).append('=').append(var6).append(", ");
                } catch (IllegalAccessException var7) {
                    throw new RuntimeException(var7);
                }
            }

            return var1.append('}').toString();
        }

        @Nullable
        public static String toLatinUpperCase(@Nullable String var0) {
            if (com.google.common.base.Strings.isNullOrEmpty(var0)) {
                return var0;
            } else {
                char[] var1;
                int var2 = (var1 = var0.toCharArray()).length;
                boolean var3 = false;

                for(int var4 = 0; var4 < var2; ++var4) {
                    char var5;
                    if ((var5 = var1[var4]) >= 'a' && var5 <= 'z') {
                        var5 = (char)(var5 & 95);
                        var3 = true;
                    }

                    var1[var4] = var5;
                }

                if (var3) {
                    return new String(var1);
                } else {
                    return var0;
                }
            }
        }

        public static String upperCaseReplaceChar(@Nullable String var0, char var1, char var2) {
            if (com.google.common.base.Strings.isNullOrEmpty(var0)) {
                return var0;
            } else {
                char[] var6;
                int var3 = (var6 = var0.toCharArray()).length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    char var5;
                    if ((var5 = var6[var4]) == var1) {
                        var5 = var2;
                    } else if (var5 >= 'a' && var5 <= 'z') {
                        var5 = (char)(var5 & 95);
                    }

                    var6[var4] = var5;
                }

                return new String(var6);
            }
        }

        public static String join(Object[] var0, String var1) {
            return var0 == null ? null : join(var0, var1, 0, var0.length);
        }

        public static String join(Object[] var0, String var1, int var2, int var3) {
            if (var0 == null) {
                return null;
            } else {
                if (var1 == null) {
                    var1 = "";
                }

                int var4;
                if ((var4 = var3 - var2) <= 0) {
                    return "";
                } else {
                    var4 *= (var0[var2] == null ? 16 : var0[var2].toString().length()) + var1.length();
                    StringBuilder var6 = new StringBuilder(var4);

                    for(int var5 = var2; var5 < var3; ++var5) {
                        if (var5 > var2) {
                            var6.append(var1);
                        }

                        if (var0[var5] != null) {
                            var6.append(var0[var5]);
                        }
                    }

                    return var6.toString();
                }
            }
        }

        public static String lowerCaseReplaceChar(@Nullable String var0, char var1, char var2) {
            if (com.google.common.base.Strings.isNullOrEmpty(var0)) {
                return var0;
            } else {
                char[] var6;
                int var3 = (var6 = var0.toCharArray()).length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    char var5;
                    if ((var5 = var6[var4]) == var1) {
                        var5 = var2;
                    } else if (var5 >= 'a' && var5 <= 'z') {
                        var5 = (char)(var5 | 32);
                    }

                    var6[var4] = var5;
                }

                return new String(var6);
            }
        }

        public static boolean isEnglish(@Nullable CharSequence var0) {
            if (var0 == null) {
                return false;
            } else {
                int var1;
                if ((var1 = var0.length()) == 0) {
                    return false;
                } else {
                    for(int var2 = 0; var2 < var1; ++var2) {
                        char var3;
                        if ((var3 = var0.charAt(var2)) != '_' && var3 != ' ' && !isEnglishLetterOrDigit(var3)) {
                            return false;
                        }
                    }

                    return true;
                }
            }
        }

        public static URL validateURL(String var0) {
            try {
                URL var2;
                (var2 = new URL(var0)).toURI();
                return var2;
            } catch (MalformedURLException | URISyntaxException var1) {
                return null;
            }
        }

        public static boolean hasSymbol(@Nullable CharSequence var0) {
            if (var0 == null) {
                return false;
            } else {
                int var1;
                if ((var1 = var0.length()) == 0) {
                    return false;
                } else {
                    for(int var2 = 0; var2 < var1; ++var2) {
                        char var3;
                        if ((var3 = var0.charAt(var2)) != '_' && var3 != ' ' && !Character.isLetterOrDigit(var3)) {
                            return true;
                        }
                    }

                    return false;
                }
            }
        }

        public static boolean isEnglishLetter(char var0) {
            return var0 >= 'A' && var0 <= 'Z' || var0 >= 'a' && var0 <= 'z';
        }

        public static boolean isEnglishDigit(char var0) {
            return var0 >= '0' && var0 <= '9';
        }

        @Nonnull
        public static List<String> cleanSplit(@Nonnull String var0, char var1) {
            return split(deleteWhitespace(var0), var1, false);
        }

        @Nonnull
        public static List<String> cleanSplitManaged(@Nonnull String var0, char var1) {
            if (var1 != ' ') {
                var0 = deleteWhitespace(var0);
            }

            return split(var0, var1, false);
        }

        @Nullable
        public static String deleteWhitespace(@Nullable String var0) {
            if (com.google.common.base.Strings.isNullOrEmpty(var0)) {
                return var0;
            } else {
                int var1;
                char[] var2 = new char[var1 = var0.length()];
                int var3 = 0;

                for(int var4 = 0; var4 < var1; ++var4) {
                    char var5;
                    if ((var5 = var0.charAt(var4)) != ' ') {
                        var2[var3++] = var5;
                    }
                }

                if (var3 == var1) {
                    return var0;
                } else {
                    return new String(var2, 0, var3);
                }
            }
        }

        public static CharSequence join(char var0, CharSequence... var1) {
            int var2;
            if ((var2 = var1.length) == 0) {
                return null;
            } else if (var2 == 1) {
                return var1[0];
            } else {
                CharSequence[] var3 = var1;
                int var4 = var1.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    CharSequence var6 = var3[var5];
                    var2 += var6.length();
                }

                char[] var11 = new char[var2 - 1];
                var2 = var1.length;
                var4 = 0;
                int var13 = var1.length;

                for (CharSequence charSequence : var1) {
                    CharSequence var7;
                    int var8 = (var7 = charSequence).length();

                    for (int var9 = 0; var9 < var8; ++var9) {
                        var11[var4++] = var7.charAt(var9);
                    }

                    --var2;
                    if (var2 > 0) {
                        var11[var4++] = var0;
                    }
                }

                return new String(var11);
            }
        }

        public static String join(String var0, Collection<String> var1, Function<String, String> var2) {
            return join(var0, var1, var2, "[]");
        }

        public static String join(String var0, Collection<String> var1, Function<String, String> var2, String var3) {
            if (var1.isEmpty()) {
                return var3;
            } else {
                StringBuilder var7 = new StringBuilder();
                int var4 = 0;

                String var5;
                for(Iterator var6 = var1.iterator(); var6.hasNext(); var7.append((String)var2.apply(var5))) {
                    var5 = (String)var6.next();
                    if (var4++ != 0) {
                        var7.append(var0);
                    }
                }

                return var7.toString();
            }
        }

        public static String toOrdinalNumeral(int var0) {
            if (var0 <= 0) {
                throw new IllegalArgumentException("Ordinal numerals must start from 1");
            } else {
                char var1;
                String var2;
                if ((var1 = (var2 = Integer.toString(var0)).charAt(var2.length() - 1)) == '1') {
                    return var2 + "st";
                } else if (var1 == '2') {
                    return var2 + "nd";
                } else {
                    return var1 == '3' ? var2 + "rd" : var2 + "th";
                }
            }
        }

        @Nonnull
        public static List<String> split(@Nonnull String var0, char var1, boolean var2) {
            if (var0 == null) {
                throw new IllegalArgumentException("Cannot split a null string: " + var0);
            } else {
                ArrayList<String> var3 = new ArrayList<>();
                if (var0.isEmpty()) {
                    var3.add("");
                } else {
                    boolean var4 = false;
                    int var5 = var0.length();
                    int var6 = 0;

                    for(int var7 = 0; var7 < var5; ++var7) {
                        if (var0.charAt(var7) != var1) {
                            var4 = true;
                        } else {
                            if (var4 || var2) {
                                var3.add(var0.substring(var6, var7));
                                var4 = false;
                            }

                            var6 = var7 + 1;
                        }
                    }

                    if (var4 || var2) {
                        var3.add(var0.substring(var6, var5));
                    }

                }
                return var3;
            }
        }

        @Nonnull
        public static List<SplitInfo> advancedSplit(@Nonnull String var0, char var1, boolean var2) {
            if (com.google.common.base.Strings.isNullOrEmpty(var0)) {
                throw new IllegalArgumentException("Cannot split a null or empty string: " + var0);
            } else {
                ArrayList<SplitInfo> var3 = new ArrayList<>();
                boolean var4 = false;
                boolean var5 = false;
                int var6 = var0.length();
                int var7 = 0;

                for(int var8 = 0; var8 < var6; ++var8) {
                    if (var0.charAt(var8) != var1) {
                        var5 = false;
                        var4 = true;
                    } else {
                        if (var4 || var2) {
                            var3.add(new SplitInfo(var0.substring(var7, var8), var7, var8));
                            var4 = false;
                            var5 = true;
                        }

                        var7 = var8 + 1;
                    }
                }

                if (var4 || var2 && var5) {
                    var3.add(new SplitInfo(var0.substring(var7, var6), var7, var6));
                }

                return var3;
            }
        }

        public static String[] splitLocation(@Nonnull String var0, int var1) {
            String[] var2 = new String[var1];
            int var3 = var0.length();
            int var4 = 0;
            var1 = 0;

            for(int var5 = 0; var5 < var3; ++var5) {
                if (var0.charAt(var5) == ',') {
                    var2[var1++] = var0.substring(var4, var5);
                    var5 += 2;
                    var4 = var5;
                }
            }

            var2[var1] = var0.substring(var4, var3);
            return var2;
        }

        public static boolean isEnglishLetterOrDigit(char var0) {
            return isEnglishDigit(var0) || isEnglishLetter(var0);
        }

        public static boolean containsNumber(@Nullable CharSequence var0) {
            if (var0 == null) {
                return false;
            } else {
                int var1;
                if ((var1 = var0.length()) == 0) {
                    return false;
                } else {
                    for(int var2 = 0; var2 < var1; ++var2) {
                        if (isEnglishDigit(var0.charAt(var2))) {
                            return true;
                        }
                    }

                    return false;
                }
            }
        }

        public static boolean containsAnyLangNumber(@Nullable CharSequence var0) {
            if (var0 == null) {
                return false;
            } else {
                int var1;
                if ((var1 = var0.length()) == 0) {
                    return false;
                } else {
                    for(int var2 = 0; var2 < var1; ++var2) {
                        if (Character.isDigit(var0.charAt(var2))) {
                            return true;
                        }
                    }

                    return false;
                }
            }
        }

        @Nullable
        public static Dimension getImageSize(@Nonnull URL var0) {
            try {
                InputStream var16 = var0.openStream();

                label151: {
                    Dimension var17;
                    try {
                        label152: {
                            ImageInputStream var1 = ImageIO.createImageInputStream(var16);

                            label153: {
                                try {
                                    Iterator var2;
                                    if (!(var2 = ImageIO.getImageReaders(var1)).hasNext()) {
                                        break label153;
                                    }

                                    ImageReader var3 = (ImageReader)var2.next();

                                    try {
                                        var3.setInput(var1);
                                        var17 = new Dimension(var3.getWidth(0), var3.getHeight(0));
                                    } finally {
                                        var3.dispose();
                                    }
                                } catch (Throwable var13) {
                                    if (var1 != null) {
                                        try {
                                            var1.close();
                                        } catch (Throwable var11) {
                                            var13.addSuppressed(var11);
                                        }
                                    }

                                    throw var13;
                                }

                                if (var1 != null) {
                                    var1.close();
                                }
                                break label152;
                            }

                            if (var1 != null) {
                                var1.close();
                            }
                            break label151;
                        }
                    } catch (Throwable var14) {
                        if (var16 != null) {
                            try {
                                var16.close();
                            } catch (Throwable var10) {
                                var14.addSuppressed(var10);
                            }
                        }

                        throw var14;
                    }

                    if (var16 != null) {
                        var16.close();
                    }

                    return var17;
                }

                if (var16 != null) {
                    var16.close();
                }
            } catch (IOException var15) {
            }

            return null;
        }

        public static boolean isNumeric(@Nullable String var0) {
            if (var0 == null) {
                return false;
            } else {
                int var1;
                if ((var1 = var0.length()) == 0) {
                    return false;
                } else {
                    int var2 = 0;
                    char var3;
                    if (var1 != 1 && ((var3 = var0.charAt(0)) == '-' || var3 == '+')) {
                        var2 = 1;
                    }

                    do {
                        if (var2 >= var1) {
                            return true;
                        }
                    } while(isEnglishDigit(var0.charAt(var2++)));

                    return false;
                }
            }
        }

        public static <K, V> StringBuilder associatedArrayMap(Map<K, V> var0) {
            return a(var0, 1);
        }

        private static <K, V> StringBuilder a(Map<K, V> var0, int var1) {
            if (var0 == null) {
                return new StringBuilder("<null>");
            } else {
                StringBuilder var2;
                (var2 = new StringBuilder(var0.size() * 15)).append(var0.getClass().getSimpleName()).append('{');
                Iterator var4 = var0.entrySet().iterator();

                while(var4.hasNext()) {
                    Map.Entry var3 = (Map.Entry)var4.next();
                    var2.append('\n');
                    var2.append(spaces(var1 << 1));
                    var2.append(var3.getKey()).append(" => ");
                    Object var5 = var3.getValue();
                    var2.append(var5 instanceof Map ? a((Map)var5, var1 + 1) : var5);
                    var2.append('\n');
                }

                return var2.append('}');
            }
        }

        public static String spaces(int var0) {
            if (var0 <= 0) {
                return "";
            } else {
                char[] var1;
                Arrays.fill(var1 = new char[var0], ' ');
                return new String(var1);
            }
        }

        public static boolean isPureNumber(@Nullable String var0) {
            if (com.google.common.base.Strings.isNullOrEmpty(var0)) {
                return false;
            } else {
                int var1 = var0.length();

                for(int var2 = 0; var2 < var1; ++var2) {
                    if (!isEnglishDigit(var0.charAt(var2))) {
                        return false;
                    }
                }

                return true;
            }
        }

        public static int indexOfAny(String var0, String[] var1) {
            Objects.requireNonNull(var0);
            Objects.requireNonNull(var1);
            int var2 = Integer.MAX_VALUE;
            int var3 = (var1 = var1).length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String var5;
                int var6;
                if ((var5 = var1[var4]) != null && (var6 = var0.indexOf(var5)) != -1 && var6 < var2) {
                    var2 = var6;
                }
            }

            if (var2 == Integer.MAX_VALUE) {
                return -1;
            } else {
                return var2;
            }
        }

        @Nonnull
        public static String getGroupedOption(@Nonnull String str, int... var1) {
            Objects.requireNonNull(str, "Enum option name cannot be null");
            String var0 = toLatinLowerCase(str);
            if (var1.length == 0) {
                return var0.replace('_', '-');
            } else {
                String[] var2;
                if ((var2 = splitArray(var0, '_', false)).length < var1.length) {
                    throw new IllegalArgumentException("Groups cannot be greater than enum separators: " + str);
                } else {
                    boolean[] var3 = new boolean[var2.length];
                    int var4 = (var1).length;

                    for(int var5 = 0; var5 < var4; ++var5) {
                        int var6 = var1[var5];
                        var3[var6 - 1] = true;
                    }

                    StringBuilder var7 = new StringBuilder(str.length());

                    for(var4 = 0; var4 < var2.length; ++var4) {
                        var7.append(var2[var4]);
                        if (var3[var4]) {
                            var7.append('.');
                        } else {
                            var7.append('-');
                        }
                    }

                    var7.setLength(var7.length() - 1);
                    return var7.toString();
                }
            }
        }

        public static String[] splitArray(String var0, char var1) {
            return splitArray(var0, var1, false);
        }

        public static String[] splitArray(String var0, char var1, boolean var2) {
            if (var0 == null) {
                return null;
            } else {
                int var3;
                if ((var3 = var0.length()) == 0) {
                    return new String[0];
                } else {
                    ArrayList<String> var4 = new ArrayList<>();
                    int var5 = 0;
                    int var6 = 0;
                    boolean var7 = false;
                    boolean var8 = false;

                    while(true) {
                        while(var5 < var3) {
                            if (var0.charAt(var5) == var1) {
                                if (var7 || var2) {
                                    var4.add(var0.substring(var6, var5));
                                    var7 = false;
                                    var8 = true;
                                }

                                ++var5;
                                var6 = var5;
                            } else {
                                var8 = false;
                                var7 = true;
                                ++var5;
                            }
                        }

                        if (var7 || var2 && var8) {
                            var4.add(var0.substring(var6, var5));
                        }

                        return var4.toArray(new String[var4.size()]);
                    }
                }
            }
        }

        public static String reverse(String var0) {
            char[] var4 = var0.toCharArray();
            int var1 = 0;

            for(int var2 = var4.length - 1; var2 > var1; ++var1) {
                char var3 = var4[var1];
                var4[var1] = var4[var2];
                var4[var2] = var3;
                --var2;
            }

            return new String(var4);
        }

        public static boolean isOneOf(@Nullable String var0, @Nonnull String... var1) {
            return !com.google.common.base.Strings.isNullOrEmpty(var0) && Arrays.asList(var1).contains(var0);
        }

        @Nonnull
        public static String toFancyNumber(double var0) {
            return a.format(var0);
        }

        @Nullable
        public static String remove(@Nullable String var0, char var1) {
            if (com.google.common.base.Strings.isNullOrEmpty(var0)) {
                return var0;
            } else {
                char[] var2 = var0.toCharArray();
                int var3 = 0;
                int var5 = var2.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    char var7;
                    if ((var7 = var2[var6]) != var1) {
                        var2[var3++] = var7;
                    }
                }

                if (var2.length == var3) {
                    return var0;
                } else {
                    return new String(var2, 0, var3);
                }
            }
        }

        public static String remove(String var0, String var1) {
            Objects.requireNonNull(var0);
            Objects.requireNonNull(var1);
            return replace(var0, var1, "", -1);
        }

        public static String replaceOnce(String var0, String var1, String var2) {
            return replace(var0, var1, var2, 1);
        }

        public static String replace(String var0, String var1, String var2) {
            return replace(var0, var1, var2, -1);
        }

        public static String replace(String var0, String var1, String var2, int var3) {
            if (!var0.isEmpty() && !var1.isEmpty() && var2 != null && var3 != 0) {
                int var4 = 0;
                int var5;
                if ((var5 = var0.indexOf(var1, 0)) == -1) {
                    return var0;
                } else {
                    int var6 = var1.length();
                    int var7 = Math.max(var2.length() - var6, 0) * (var3 < 0 ? 16 : Math.min(var3, 64));

                    StringBuilder var8;
                    for(var8 = new StringBuilder(var0.length() + var7); var5 != -1; var5 = var0.indexOf(var1, var4)) {
                        var8.append(var0.substring(var4, var5)).append(var2);
                        var4 = var5 + var6;
                        --var3;
                        if (var3 == 0) {
                            break;
                        }
                    }

                    var8.append(var0.substring(var4));
                    return var8.toString();
                }
            } else {
                return var0;
            }
        }

        public static boolean containsAny(@Nullable String var0, @Nonnull String... var1) {
            if (!com.google.common.base.Strings.isNullOrEmpty(var0) && var1.length != 0) {
                int var2 = (var1).length;

                for (String var4 : var1) {
                    if (var0.contains(var4)) {
                        return true;
                    }
                }

                return false;
            } else {
                return false;
            }
        }



        static {
            a = new DecimalFormat("#,###.##", new DecimalFormatSymbols(Locale.ENGLISH));
            b = Pattern.compile("([A-Z])");
        }

        public static final class SplitInfo {
            public final String text;
            public final int index;
            public final int endIndex;

            public SplitInfo(String var1, int var2, int var3) {
                this.text = var1;
                this.index = var2;
                this.endIndex = var3;
            }
        }



}
