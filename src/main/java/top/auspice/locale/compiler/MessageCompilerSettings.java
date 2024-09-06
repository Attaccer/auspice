package top.auspice.locale.compiler;

public class MessageCompilerSettings {
    protected boolean colorize;
    protected boolean plainOnly = true;         //是否是平坦的, 若为false, 则可以有hover
    protected boolean translatePlaceholders;
    protected boolean allowNewLines;


    public MessageCompilerSettings colorize() {
        this.colorize = true;
        return this;
    }
    public MessageCompilerSettings hovers() {
        this.plainOnly = false;
        return this;
    }
    public MessageCompilerSettings translatePlaceholders() {
        this.translatePlaceholders = true;
        return this;
    }
    public MessageCompilerSettings allowNewLines() {
        this.allowNewLines = true;
        return this;
    }

}
