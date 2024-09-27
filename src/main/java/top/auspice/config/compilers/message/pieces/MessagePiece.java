package top.auspice.config.compilers.message.pieces;

import top.auspice.utils.ColorUtils;

public abstract class MessagePiece {

    public abstract int length();

    public abstract int jsonLength();



    public static final class NewLine extends MessagePiece {
        public NewLine() {
        }
        public int length() {
            return 1;
        }

        public int jsonLength() {
            return 0;
        }

        public String toString() {
            return "Newline";
        }
    }

    public static final class Plain extends MessagePiece {
        private final String value;
        public static final Plain EMPTY = new Plain("");
        public static final int JSON_LEN = 9 + 3;

        public Plain(String var1) {
            this.value = var1;
        }

        public String getMessage() {
            return this.value;
        }



        public int length() {
            return this.value.length();
        }
        public int jsonLength() {
            return JSON_LEN + this.length();
        }

        public String toString() {
            return "Plain{ \"" + this.value + "\", length=" + this.length() + " }";
        }

    }

    public static abstract class Color extends MessagePiece {

    }

    public static final class SimpleColor extends Color {
        private final org.bukkit.ChatColor color;
        private static final int a = 11;

        public SimpleColor(org.bukkit.ChatColor color) {
            this.color = color;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof SimpleColor)) {
                return false;
            } else {
                return this.color == ((SimpleColor)obj).color;
            }
        }

        public org.bukkit.ChatColor getColor() {
            return this.color;
        }

        public int length() {
            return 2;
        }

        public int jsonLength() {
            return a + this.color.name().length();
        }

        public String toString() {
            return "SimpleColor{ " + this.color.name() + " }";
        }

    }

    public static final class HexColor extends Color {
        private final java.awt.Color color;
        private final net.md_5.bungee.api.ChatColor bungeeColor;
        private static final int jsonHeadLength = 11 + 6;

        public java.awt.Color getColor() {
            return this.color;
        }

        public HexColor(java.awt.Color var1) {
            this.color = var1;
            this.bungeeColor = net.md_5.bungee.api.ChatColor.of(var1);
        }

        public int length() {
            return 12;
        }

        public int jsonLength() {
            return jsonHeadLength;
        }

        public String toString() {
            return "Hex{ " + ColorUtils.toHexString(this.color) + " }";
        }
    }


    public static abstract class AdvanceColor extends Color {

    }

    public static final class ScopedAdvanceColor extends AdvanceColor {
        @Override
        public int length() {
            return 0;   //TODO
        }

        @Override
        public int jsonLength() {
            return 0;   //TODO
        }


    }

    public static final class OpenedAdvanceColor extends AdvanceColor {

        @Override
        public int length() {
            return 0;   //TODO
        }

        @Override
        public int jsonLength() {
            return 0;   //TODO
        }


    }





}
