package top.auspice.locale.compiler;

import top.auspice.locale.compiler.pieces.MessagePiece;
import top.auspice.locale.compiler.pieces.MessagePieceCompiler;
import top.auspice.utils.string.Strings;

import java.util.*;


/*
 *color:
 *  &a  §a ...                          SimpleColor
 *  {&#35f2a4}   {§#23a344} ...         HexColor
 *  {&~<gradient|#121212|#131313>} ...  AdvanceColor
 *
 *
 *
 *
 */
public class MessageCompiler {

    protected MessageCompilerSettings settings;
    protected final char[] chars;
    protected final int charsLength;
    protected int pointer;
    protected char pointerChar;
    protected final StringBuilder compiled;
    protected boolean isCompiled = false;
    protected final Boolean usePrefix;
    protected final List<MessagePiece> pieces;
    protected final ArrayList<Character> plainChars;            //用于构建 MessagePiece.Plain
    protected final List<MessageCompilerException> exceptions;


    public MessageCompiler(char[] chars, MessageCompilerSettings settings) {
        Objects.requireNonNull(chars);
        Objects.requireNonNull(settings);
        this.settings = settings;
        this.chars = chars;
        this.charsLength = chars.length;
        if (startWith("NOPREFIX|")) {
            this.pointer = 9;
            this.usePrefix = Boolean.FALSE;
        } else if (startWith("PREFIX|")) {
            this.pointer = 7;
            this.usePrefix = Boolean.TRUE;
        } else {
            this.pointer = 0;
            this.usePrefix = null;
        }
        this.compiled = new StringBuilder(this.charsLength);
        this.pieces = new ArrayList<>();
        this.plainChars = new ArrayList<>();
        this.exceptions = new ArrayList<>();
    }

    public MessageCompiler(String string, MessageCompilerSettings settings) {
        this(string.toCharArray(), settings);
    }


    public String joinExceptions() {
        StringBuilder builder = new StringBuilder(this.exceptions.size() * this.charsLength << 1);
        int size = this.exceptions.size();

        for (MessageCompilerException exception : this.exceptions) {
            builder.append(exception.getMessage());
            --size;
            if (size != 0) {
                builder.append('\n');
            }
        }
        return builder.toString();
    }


    public void compile() {
        if (!this.isCompiled) {
            label1:
            for(this.isCompiled = true; this.pointer < this.charsLength; ++this.pointer) {       //遍历字符串每个字符
                this.pointerChar = this.chars[this.pointer];

                compilePieces();


            }
        }
    }


    public void compilePieces() {
        if (MessagePieceCompiler.CheckedChars.isCheckedChar(this.pointerChar)) {
            List<MessagePiece> compiledPieces = null;
            List<MessagePieceCompiler<?>> compilers = MessagePieceCompiler.allCompilers.get(MessagePieceCompiler.CheckedChars.chars.get(this.pointerChar));
            if (compilers != null && !compilers.isEmpty()) {
                compiledPieces = new ArrayList<>();
                for (MessagePieceCompiler<?> compiler : compilers) {
                    MessagePiece piece = compiler.compilePieces(chars, pointer);
                    if (piece != null) {
                        compiledPieces.add(piece);
                    }
                }
            }
            if (compiledPieces != null) {
                if (compiledPieces.isEmpty()) {               //若解析出多个MessagePiece, 不修改指针; 若只解析出一个MessagePiece, 将指针增加此MessagePiece的长度; 若没有解析出MessagePiece, 则代表是MessagePiece.Plain, 指针自增1
                    this.plainChars.add(pointerChar);
                } else if (compiledPieces.size() == 1) {
                    this.pointer = this.pointer + compiledPieces.getFirst().length();
                } else {                                              //TODO
                    this.exceptions.add(new MessageCompilerException("", "", this.pointer, "Compiled " + compiledPieces.size() + " MessagePieces, are you using the custom MessagePieceCompiler?"));
                }
                this.pieces.addAll(compiledPieces);
            } else {
                this.plainChars.add(pointerChar);
            }
        }
    }





    private boolean startWith(String str) {
        if (this.charsLength < str.length()) {
            return false;
        } else {
            for(int i = 0; i < str.length(); ++i) {
                if (this.chars[i] != str.charAt(i)) {
                    return false;
                }
            }

            return true;
        }
    }



}
