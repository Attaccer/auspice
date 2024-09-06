package top.auspice.locale.compiler.pieces;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public sealed abstract class MessagePieceCompiler<P extends MessagePiece> {
    private final CheckedChars checkChar;
    public static final @NonNull Map<CheckedChars, List<MessagePieceCompiler<?>>> allCompilers = new HashMap<>();

    public abstract @Nullable P compilePieces(char[] chars, int pointer);

    public static List<MessagePieceCompiler<?>> getCompilers(char ch) {
        return allCompilers.get(CheckedChars.chars.get(ch));
    }


    protected MessagePieceCompiler(CheckedChars checkChar) {

        this.checkChar = checkChar;
        if (allCompilers.get(checkChar) == null) {
            allCompilers.put(checkChar, new ArrayList<>(List.of(this)));
        } else {
            allCompilers.get(checkChar).add(this);
        }
    }


    public static void init() {

        new NewLineCompiler();

    }


    public enum CheckedChars {                  //TODO rename

        AMPERSAND('&'),
        ASTERISK('*'),
        AT('@'),
        BACK_SLASH('\\'), //不确定能不能用于yaml配置文件 (
        COLON(':'),
        LEFT_BRACE('{'),
        LEFT_BRACKET('['),
        LEFT_PARENTHESIS('('),
        MINUS('-'),
        NEXT_LINE('\n'),
        NUMBER_SIGN('#'),
        PERCENT('%'),
        PLUS('+'),
        QUESTION_MARK('?'),
        SECTION('§'),
        SLASH('/'),

        ;

        public static final Map<Character, CheckedChars> chars = new HashMap<>();
        private final char ch;

        CheckedChars(char ch) {
            this.ch = ch;
        }

        public char getChar() {
            return this.ch;
        }

        public static boolean isCheckedChar(char c) {
            return chars.containsKey(c);
        }

        static {
            for (CheckedChars c : CheckedChars.values()) {
                chars.put(c.ch, c);
            }
        }

    }

    public static final class NewLineCompiler extends MessagePieceCompiler<MessagePiece.NewLine> {

        private NewLineCompiler() {
            super(CheckedChars.NEXT_LINE);
        }

        @Override
        public MessagePiece.NewLine compilePieces(char[] chars, int pointer) {
            return new MessagePiece.NewLine();
        }
    }



}
