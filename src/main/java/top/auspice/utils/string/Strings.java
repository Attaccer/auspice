package top.auspice.utils.string;

import java.util.Collection;

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

}
