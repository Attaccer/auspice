package top.auspice.gui;

import java.util.Locale;

public final class GUIPath {

    private final String[] path;

    public GUIPath(String[] path) {
        this.path = path;
    }

    public String[] getPath() {
        return path;
    }

    public static GUIPath getPathFromEnum(String name) {
        String a = name.replace('$', '-').toLowerCase(Locale.ENGLISH);
        String[] out = a.split("_");
        return new GUIPath(out);
    }

}
