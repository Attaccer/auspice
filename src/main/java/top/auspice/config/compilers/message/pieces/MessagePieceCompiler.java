package top.auspice.config.compilers.message.pieces;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import top.auspice.config.compilers.message.MessageCompilerException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public sealed abstract class MessagePieceCompiler<P extends MessagePiece> {
    private final CheckerChars checkerChar;
    public static final @NonNull Map<CheckerChars, List<MessagePieceCompiler<?>>> allCompilers = new HashMap<>();
    private static boolean initialized = false;

    public abstract @Nullable P compilePiece(char[] chars, int pointer, List<MessageCompilerException> exceptions);

    public static List<MessagePieceCompiler<?>> getCompilers(char ch) {
        return allCompilers.get(CheckerChars.chars.get(ch));
    }


    public MessagePieceCompiler(CheckerChars checkerChar) {

        this.checkerChar = checkerChar;
        if (allCompilers.get(checkerChar) == null) {
            allCompilers.put(checkerChar, new ArrayList<>(List.of(this)));
        } else {
            allCompilers.get(checkerChar).add(this);
        }
    }


    public static void init() {

        if (!initialized) {
            new NewLineCompiler();
            new HexColorCompiler();
            new OpenedAdvancedColorCompiler();
        }

    }

    public CheckerChars getCheckerChar() {
        return checkerChar;
    }


    public enum CheckerChars {                  //TODO rename

        AMPERSAND('&'),
        ASTERISK('*'),
        AT('@'),
        BACK_SLASH('\\'), //不确定能不能用于yaml配置文件 (
        COLON(':'),
        LEFT_BRACE('{'),
        LEFT_BRACKET('['),
        LEFT_CHEVRON('<'),
        LEFT_PARENTHESIS('('),
        MINUS('-'),
        NEXT_LINE('\n'),
        NUMBER_SIGN('#'),
        PERCENT('%'),
        PLUS('+'),
        QUESTION_MARK('?'),
        RIGHT_BRACE('}'),
        RIGHT_BRACKET(']'),
        RIGHT_CHEVRON('>'),
        RIGHT_PARENTHESIS(')'),
        SECTION('§'),
        SLASH('/'),

        ;

        public static final Map<Character, CheckerChars> chars = new HashMap<>();
        private final char ch;

        CheckerChars(char ch) {
            this.ch = ch;
        }

        public char getChar() {
            return this.ch;
        }

        public static boolean isCheckedChar(char c) {
            return chars.containsKey(c);
        }

        static {
            for (CheckerChars c : CheckerChars.values()) {
                chars.put(c.ch, c);
            }
        }

    }

    public static final class NewLineCompiler extends MessagePieceCompiler<MessagePiece.NewLine> {

        private NewLineCompiler() {
            super(CheckerChars.NEXT_LINE);
        }

        @Override
        public MessagePiece.NewLine compilePiece(char[] chars, int pointer, List<MessageCompilerException> exceptions) {
            return new MessagePiece.NewLine();
        }
    }


    public static final class HexColorCompiler extends MessagePieceCompiler<MessagePiece.HexColor> {
        private HexColorCompiler() {
            super(CheckerChars.LEFT_BRACE);
        }

        @Override
        public MessagePiece.HexColor compilePiece(char[] chars, int pointer, List<MessageCompilerException> exceptions) {
            return null;
        }

    }

    public static final class OpenedAdvancedColorCompiler extends MessagePieceCompiler<MessagePiece.OpenedAdvanceColor> {

        public OpenedAdvancedColorCompiler() {
            super(CheckerChars.LEFT_BRACE);
        }

        @Override
        public MessagePiece.@Nullable OpenedAdvanceColor compilePiece(char[] chars, int pointer, List<MessageCompilerException> exceptions) {
            return null;
        }
    }


}
