package top.auspice.locale.compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class MessageCompilerException extends RuntimeException {
    private final String target;
    private final String problem;
    private final int pointer;

    public MessageCompilerException(String target, String problem, int pointer, String message) {
        super(message);
        this.target = target;
        this.problem = problem;
        this.pointer = pointer;
    }

    public String getTarget() {
        return this.target;
    }

    public int getPointer() {
        return this.pointer;
    }

    public String getProblem() {
        return this.problem;
    }

    protected static String spaces(int var0) {
        char[] var1;
        Arrays.fill(var1 = new char[var0], ' ');
        return new String(var1);
    }

    protected static Collection<Integer> pointerToName(int var0, String var1) {
        if (var1 == null) {
            return new ArrayList<>();
        } else {
            ArrayList<Integer> var2 = new ArrayList<>(var1.length());

            for(int var3 = 1; var3 < var1.length(); ++var3) {
                var2.add(var0 + var3);
            }

            return var2;
        }
    }
}