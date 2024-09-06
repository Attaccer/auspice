package top.auspice.locale.compiler.pieces;

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





}
