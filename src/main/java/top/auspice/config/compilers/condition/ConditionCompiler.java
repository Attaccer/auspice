package top.auspice.config.compilers.condition;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import top.auspice.config.compilers.math.MathCompiler;
import top.auspice.config.compilers.base.expressions.ConditionalExpression;
import top.auspice.config.compilers.base.expressions.MathExpression;
import top.auspice.config.compilers.base.translators.ConditionalVariableTranslator;
import top.auspice.utils.MathUtils;
import top.auspice.utils.internal.Fn;

import java.util.*;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class ConditionCompiler {
    private final String a;
    private final int b;
    private int c;
    private final LinkedList<LogicalOperand> d = new LinkedList<>();
    private final LinkedList<LogicalOperator> e = new LinkedList<>();
    private int f = -1;
    private int g = -1;
    private int h = 0;
    private static final ConstantLogicalOperand i = new ConstantLogicalOperand(true);

    public ConditionCompiler(String var1, int var2, int var3) {
        this.a = var2 != 0 ? var1 : var1.replace('\n', ' ').replace('\r', ' ');
        this.c = var2;
        this.b = var3;
    }

    public static ConditionCompiler compile(String var0) {
        return new ConditionCompiler(var0, 0, var0.length());
    }

    private void a(boolean var1) {
        if (this.f != -1) {
            String var2;
            boolean var3 = ((var2 = this.a.substring(this.f, this.c)).endsWith("}"));
            if (var3) {
                var2 = var2.substring(1, var2.length() - 1);
            }

            b var10000;
            if (var3) {
                var10000 = new b(ConditionCompiler.OperandType.VARIABLE, 0);
            } else {
                int var4 = var2.length();
                boolean var5 = true;
                int var6 = 0;
                int var7;
                if (var2.charAt(0) == '\'') {
                    for(var7 = 1; var7 < var4; ++var7) {
                        if (var2.charAt(var7) == '\'') {
                            var6 = var7 + 1;
                            break;
                        }
                    }

                    var10000 = new b(ConditionCompiler.OperandType.STRING, var6);
                } else {
                    for(var7 = 0; var7 < var4; ++var7) {
                        char var8;
                        if (((var8 = var2.charAt(var7)) < 'A' || var8 > 'Z') && (var8 < 'a' || var8 > 'z') && var8 != '_') {
                            if (var8 == ' ') {
                                if (var6 == 0) {
                                    var6 = var7;
                                }
                            } else {
                                var5 = false;
                                var6 = 0;
                            }
                        } else if (var6 != 0) {
                            var5 = false;
                        }
                    }

                    var10000 = new b(var5 ? ConditionCompiler.OperandType.VARIABLE : ConditionCompiler.OperandType.ARITHMETIC, var6);
                }
            }

            b var10 = var10000;
            if (var10000.b != 0 && var10.a != ConditionCompiler.OperandType.ARITHMETIC) {
                var2 = var2.substring(0, Math.abs(var10.b));
            }

            LogicalOperand var11;
            label69:
            switch (var10.a.ordinal()) {
                case 0:
                    switch (var2) {
                        case "true":
                        case "else":
                            var11 = ConditionCompiler.ConstantLogicalOperand.TRUE;
                            break label69;
                        case "false":
                            var11 = ConditionCompiler.ConstantLogicalOperand.FALSE;
                            break label69;
                        case "null":
                        case "nil":
                            throw this.a(this.f, "Cannot use reserved logical keyword '" + var2 + '\'', var2);
                        default:
                            var11 = new LogicalVariableOperand(var2);
                            break label69;
                    }
                case 1:
                    MathCompiler.Expression var12 = MathCompiler.compile(var2);
                    var11 = new ArithmeticOperand(var12);
                    break;
                case 2:
                    var11 = new StringOperand(var2.substring(1, var2.length() - 1));
                    break;
                default:
                    throw new AssertionError();
            }

            String finalVar = var2;
            this.a(var11, var1, () -> finalVar);
        }
    }

    private void a(LogicalOperand var1, boolean var2, Supplier<String> var3) {
        LogicalOperator var4;
        if ((var4 = this.e.peekLast()) != null && var4.unary) {
            if (!var4.acceptsOperandOfType(var1)) {
                throw this.a(this.f, "Right hand side of '" + var4.symbol + "' unary operator must be " + var4.acceptedOperands[0] + " expression", var3.get());
            }

            var1 = new UnaryLogicalOperator(var4, var1);
            this.e.removeLast();
        }

        this.d.addLast(var1);
        if (var2 || this.e.size() == 2 && this.d.size() == 3) {
            this.handleOperations(var2, var3);
        }

    }

    private BiLogicalOperator a(LogicalOperand var1, LogicalOperator var2, LogicalOperand var3) {
        if (!var2.acceptsOperandOfType(var1)) {
            throw this.a(this.h, "Left hand side of '" + var2.symbol + "' operator must be " + var2.acceptedOperands[0] + " expression", var2.symbol);
        } else if (!var2.acceptsOperandOfType(var3)) {
            throw this.a(this.f, "Right hand side of '" + var2.symbol + "' operator must be " + var2.acceptedOperands[0] + " expression", var2.symbol);
        } else {
            return new BiLogicalOperator(var1, var2, var3);
        }
    }

    public void handleOperations(boolean var1, Supplier<String> var2) {
        if (this.d.size() >= 2) {
            if (this.e.size() == 2) {
                LogicalOperand var8 = this.d.pollFirst();
                LogicalOperand var3 = this.d.pollFirst();
                LogicalOperand var4 = this.d.pollFirst();
                LogicalOperator var5 = this.e.getFirst();
                LogicalOperator var6 = this.e.peekLast();
                if (var4 == null) {
                    throw this.b(this.f, "Right hand side empty");
                }

                BiLogicalOperator var9;
                if (var5.isComparator() && var6.isComparator()) {
                    var9 = this.a(var8, var5, var3);
                    BiLogicalOperator var7 = this.a(var3, var5, var4);
                    var7 = this.a(var9, ConditionCompiler.LogicalOperator.AND, var7);
                    this.d.add(var7);
                    this.e.clear();
                    return;
                }

                if (var5.hasPrecedenceOver(var6)) {
                    var9 = this.a(var8, var5, var3);
                    this.d.add(var9);
                    this.d.add(var4);
                    this.e.removeFirst();
                } else {
                    var9 = this.a(var3, var6, var4);
                    this.d.add(var8);
                    this.d.add(var9);
                    this.e.removeLast();
                }

                if (!var1) {
                    return;
                }
            }

            this.d.add(this.a(this.d.pollFirst(), this.e.pollLast(), this.d.pollLast()));
        }
    }

    public void handleOp() {
        if (this.g != -1) {
            int var2 = this.g;
            String var3;
            int var4 = (var3 = this.a.substring(var2, this.c)).length();
            LogicalOperator[] var5;
            int var6 = (var5 = ConditionCompiler.LogicalOperator.OPERATORS).length;

            for(int var7 = 0; var7 < var6; ++var7) {
                LogicalOperator var8;
                String var9 = (var8 = var5[var7]).symbol;
                if (var4 == var8.symbolSize() && var9.equals(var3)) {
                    LogicalOperator var10 = this.e.peekLast();
                    if (this.d.isEmpty() && !var8.unary) {
                        throw this.a(this.h, "Blank operand on left hand side of '" + var8.symbol + "' operator", var8.symbol);
                    }

                    if (!this.e.isEmpty()) {
                        if (!var8.unary && this.d.size() < 2) {
                            throw this.a(this.h - var10.symbolSize(), "Blank operand on right side of '" + var10.symbol + "' binary operator.", var10.symbol);
                        }

                        if (var10.unary) {
                            throw this.a(this.f, "Unary operator '" + var10.symbol + "' was followed by another operator " + var8.symbol, var8.symbol);
                        }
                    }

                    this.e.addLast(var8);
                    return;
                }
            }

            String var11;
            if (var3.startsWith("!!")) {
                var11 = " (hint: Redundant multiple negation operators are not allowed)";
            } else if (var3.startsWith("=>")) {
                var11 = " (hint: Did you mean '>=' operator?)";
            } else if (var3.startsWith("=<")) {
                var11 = " (hint: Did you mean '<=' operator?)";
            } else {
                var11 = Arrays.stream(LogicalOperator.OPERATORS).filter((var1) -> {
                    return var3.contains(var1.symbol);
                }).findFirst().map((var0) -> {
                    return " (hint: You have to write '" + var0.symbol + "' operator separated with a space from other operators)";
                }).orElse("");
            }

            throw this.a(var2, "Unrecognized logical operator '" + var3 + '\'' + var11, var3);
        }
    }

    private static Collection<Integer> a(int var0, String var1) {
        ArrayList<Integer> var2 = new ArrayList<>(var1.length());

        for(int var3 = 1; var3 < var1.length(); ++var3) {
            var2.add(var0 + var3);
        }

        return var2;
    }

    public LogicalOperand evaluate() {
        for(; this.c < this.b; ++this.c) {
            int sep;
            char var2;
            if ((var2 = (char)(sep = this.a.charAt(this.c))) == '<' || var2 == '>' || var2 == '!' || var2 == '=' || var2 == '&' || var2 == '|') {
                if (this.f != -1) {
                    this.a(false);
                    this.f = -1;
                }

                if (this.g == -1) {
                    this.g = this.h = this.c;
                }
            } else {
                this.handleOp();
                if (sep != 32 && this.f == -1) {
                    if (sep == 40) {
                        sep = this.c + 1;
                        ConditionCompiler var8 = this;
                        int var4 = 1;
                        int var5 = this.a.length();
                        int var6 = sep;

                        while(true) {
                            if (var6 >= var5) {
                                throw var8.b(sep - 1, "Unclosed subexpression");
                            }

                            char var7;
                            if ((var7 = var8.a.charAt(var6)) == '(') {
                                ++var4;
                            } else if (var7 == ')') {
                                --var4;
                                if (var4 == 0) {
                                    this.f = this.c++;
                                    LogicalOperand var3 = (new ConditionCompiler(this.a, sep, var6)).evaluate();
                                    int finalVar = sep;
                                    int finalVar1 = var6;
                                    this.a(var3, false, () -> this.a.substring(finalVar - 1, finalVar1 + 1));
                                    this.c = var6;
                                    this.f = -1;
                                    break;
                                }
                            }

                            ++var6;
                        }
                    } else {
                        this.f = this.c;
                        if (sep == 123) {
                            while(this.a.charAt(++this.c) != '}') {
                            }
                        }
                    }
                }

                this.g = -1;
            }
        }

        if (this.f != -1) {
            this.a(true);
        } else {
            this.handleOperations(true, () -> this.a);
        }

        this.handleOp();
        if (this.d.isEmpty()) {
            throw this.b(0, "Blank expression");
        } else if (!this.e.isEmpty()) {
            LogicalOperator var9 = this.e.getLast();
            throw this.a(this.h, "Blank operand on right hand side of '" + var9.symbol + "' binary operator", var9.symbol);
        } else {
            return this.d.getLast();
        }
    }

    private static String a(int var0) {
        char[] var1;
        Arrays.fill(var1 = new char[var0], ' ');
        return new String(var1);
    }

    private LogicalException b(int var1, String var2) {
        return this.a(var1, var2, new ArrayList<>());
    }

    private LogicalException a(int var1, String var2, String var3) {
        return this.a(var1, var2, a(var1, var3));
    }

    private LogicalException a(int var1, String var2, Collection<Integer> var3) {
        int var4 = 0;
        var3.add(var1);

        for (Integer var6 : var3) {
            if (var6 > var4) {
                var4 = var6;
            }
        }

        StringBuilder var7 = new StringBuilder(a(var4 + 2));
        var3.forEach((var1x) -> {
            var7.setCharAt(var1x + 1, '^');
        });
        return new LogicalException(var2 + " at offset " + var1 + " in expression:\n\"" + this.a + "\"\n" + var7);
    }

    public static final class ConstantLogicalOperand extends LogicalOperand {
        private final boolean a;
        public static final ConstantLogicalOperand TRUE = new ConstantLogicalOperand(true);
        public static final ConstantLogicalOperand FALSE = new ConstantLogicalOperand(false);

        private ConstantLogicalOperand(boolean var1) {
            this.a = var1;
        }

        public Boolean eval0(ConditionalVariableTranslator var1) {
            return this.a;
        }

        public String toString() {
            return Boolean.toString(this.a);
        }

        public @NotNull String asString(boolean var1) {
            return Boolean.toString(this.a);
        }
    }

    private static final class b {
        private final OperandType a;
        private final int b;

        private b(OperandType var1, int var2) {
            this.a = var1;
            this.b = var2;
        }
    }

    private enum OperandType {
        VARIABLE,
        ARITHMETIC,
        STRING;

        OperandType() {
        }
    }

    public static final class LogicalException extends RuntimeException {
        public LogicalException(String var1) {
            super(var1);
        }
    }

    public static final class LogicalVariableOperand extends LogicalOperand {
        private final String a;

        public LogicalVariableOperand(String var1) {
            this.a = var1;
        }

        public Object eval0(ConditionalVariableTranslator var1) {
            Object var2;
            if ((var2 = var1.apply(this.a)) == null) {
                throw new IllegalArgumentException("Unknown variable: " + this.a);
            } else {
                return this.parseVariable(this.a, var2);
            }
        }

        public String toString() {
            return "{" + this.a + '}';
        }

        public @NotNull String asString(boolean var1) {
            return this.a;
        }
    }

    public static final class ArithmeticOperand extends LogicalOperand {
        private final MathExpression a;

        public ArithmeticOperand(MathExpression var1) {
            this.a = var1;
        }

        public Double eval0(ConditionalVariableTranslator var1) {
            return this.a.eval((var2) -> {
                Object var3;
                if ((var3 = var1.apply(var2)) == null) {
                    throw new IllegalArgumentException("Unknown variable: " + var2);
                } else {
                    var3 = this.parseVariable(var2, var3);
                    return MathUtils.expectDouble(var2, var3);
                }
            });
        }

        public String toString() {
            return this.a.toString();
        }

        public @NotNull String asString(boolean var1) {
            return this.a.asString(var1);
        }
    }

    public static final class StringOperand extends LogicalOperand {
        private final String a;

        public StringOperand(String var1) {
            this.a = var1;
        }

        public String eval0(ConditionalVariableTranslator var1) {
            return this.a;
        }

        public String toString() {
            return "StringOperand[" + this.a + ']';
        }

        public @NotNull String asString(boolean var1) {
            return this.a;
        }
    }

    public abstract static class LogicalOperand implements ConditionalExpression {
        protected String originalString = null;

        public LogicalOperand() {
        }

        protected abstract Object eval0(ConditionalVariableTranslator var1);

        protected LogicalOperand withOriginalString(String var1) {
            this.originalString = var1;
            return this;
        }

        public @Nullable String getOriginalString() {
            return this.originalString;
        }

        @NotNull
        public Boolean eval(@NotNull ConditionalVariableTranslator var1) {
            Object var2;
            if (!((var2 = this.eval0(var1)) instanceof Boolean)) {
                throw new UnsupportedOperationException("Not a boolean expression: " + var2);
            } else {
                return (Boolean)var2;
            }
        }

        public boolean isDefault() {
            return this == ConditionCompiler.i;
        }

        public Object parseVariable(String var1, Object var2) {
            if (var2 instanceof Boolean) {
                return var2;
            } else if (var2 instanceof Number) {
                return ((Number)var2).doubleValue();
            } else {
                if (var2 instanceof String) {
                    if (var2.equals("true")) {
                        return Boolean.TRUE;
                    }

                    if (var2.equals("false")) {
                        return Boolean.FALSE;
                    }

                    try {
                        return Double.parseDouble((String)var2);
                    } catch (NumberFormatException ignored) {
                    }
                }

                return var2;
            }
        }
    }

    public enum LogicalOperator {
        NOT("!", 1, new AcceptedOperand[]{ConditionCompiler.LogicalOperator.AcceptedOperand.LOGICAL}, true) {
            final boolean a(Object var1, Object var2) {
                return !this.assertBool(var2);
            }
        },
        AND("&&", 4, AcceptedOperand.LOGICAL) {
            final boolean a(Supplier<Object> var1, Supplier<Object> var2) {
                return this.assertBool(var1.get()) && this.assertBool(var2.get());
            }
        },
        OR("||", 5, AcceptedOperand.LOGICAL) {
            final boolean a(Supplier<Object> var1, Supplier<Object> var2) {
                return this.assertBool(var1.get()) || this.assertBool(var2.get());
            }
        },
        NOT_EQUALS("!=", 3, AcceptedOperand.ARITHMETIC, AcceptedOperand.LOGICAL) {
            final boolean a(Object var1, Object var2) {
                try {
                    return !var1.equals(var2);
                } catch (Exception var3) {
                    return var1 != var2;
                }
            }
        },
        EQUALS("==", 3, AcceptedOperand.ARITHMETIC, AcceptedOperand.LOGICAL, AcceptedOperand.STRING) {
            final boolean a(Object var1, Object var2) {
                try {
                    return var1.equals(var2);
                } catch (Exception var3) {
                    return var1 == var2;
                }
            }
        },
        LESS_THAN_OR_EQUAL("<=", 2, AcceptedOperand.ARITHMETIC, AcceptedOperand.COMPARATOR, AcceptedOperand.STRING) {
            final boolean a(Object var1, Object var2) {
                return this.assertNumber(var1) <= this.assertNumber(var2);
            }
        },
        LESS_THAN("<", 2, AcceptedOperand.ARITHMETIC, AcceptedOperand.COMPARATOR) {
            final boolean a(Object var1, Object var2) {
                return this.assertNumber(var1) < this.assertNumber(var2);
            }
        },
        GREATER_THAN_OR_EQUAL(">=", 2, AcceptedOperand.ARITHMETIC, AcceptedOperand.COMPARATOR) {
            final boolean a(Object var1, Object var2) {
                return this.assertNumber(var1) >= this.assertNumber(var2);
            }
        },
        GREATER_THAN(">", 2, AcceptedOperand.ARITHMETIC, AcceptedOperand.COMPARATOR) {
            final boolean a(Object var1, Object var2) {
                return this.assertNumber(var1) > this.assertNumber(var2);
            }
        };

        private static final LogicalOperator[] OPERATORS = values();
        private final String symbol;
        private final boolean unary;
        private final byte priority;
        private final AcceptedOperand[] acceptedOperands;

        LogicalOperator(String var3, int var4, AcceptedOperand... var5) {
            this(var3, var4, var5, false);
        }

        public boolean isComparator() {
            return Arrays.stream(this.acceptedOperands).anyMatch((var0) -> {
                return var0 == ConditionCompiler.LogicalOperator.AcceptedOperand.COMPARATOR;
            });
        }

        LogicalOperator(String var3, int var4, AcceptedOperand[] var5, boolean var6) {
            if (var5.length != 0 && var5.length <= 3) {
                this.symbol = var3;
                this.priority = (byte)var4;
                this.acceptedOperands = var5;
                this.unary = var6;
            } else {
                throw new AssertionError("Invalid list of accepted operands: " + Arrays.toString(var5) + " for operator " + var3);
            }
        }

        public boolean hasPrecedenceOver(LogicalOperator var1) {
            return this.priority <= var1.priority;
        }

        public boolean acceptsOperandOfType(LogicalOperand var1) {
            if (var1 instanceof LogicalVariableOperand) {
                return true;
            } else {
                AcceptedOperand var2 = var1 instanceof ArithmeticOperand ? ConditionCompiler.LogicalOperator.AcceptedOperand.ARITHMETIC : ConditionCompiler.LogicalOperator.AcceptedOperand.LOGICAL;
                return Arrays.stream(this.acceptedOperands).anyMatch((var1x) -> var1x == var2);
            }
        }

        boolean a(Object var1, Object var2) {
            throw new UnsupportedOperationException();
        }

        boolean a(Supplier<Object> var1, Supplier<Object> var2) {
            return this.a(var1.get(), var2.get());
        }

        public int symbolSize() {
            return this.symbol.length();
        }

        public Double assertNumber(Object var1) {
            if (var1 instanceof Double) {
                return (Double)var1;
            } else {
                throw new IllegalArgumentException("Operands of '" + this.symbol + "' operator must be numbers instead got: '" + var1 + "' (" + (var1 == null ? null : var1.getClass().getName() + ')'));
            }
        }

        public boolean assertBool(Object var1) {
            if (var1 instanceof Boolean) {
                return (Boolean)var1;
            } else {
                throw new IllegalArgumentException("Operands of '" + this.symbol + "' operator must be booleans instead got: '" + var1 + "' (" + (var1 == null ? null : var1.getClass().getName() + ')'));
            }
        }

        public enum AcceptedOperand {
            ARITHMETIC,
            LOGICAL,
            COMPARATOR,
            STRING;

            AcceptedOperand() {
            }
        }
    }

    public static final class UnaryLogicalOperator extends LogicalOperand {
        private final LogicalOperator a;
        private final LogicalOperand b;

        public UnaryLogicalOperator(LogicalOperator var1, LogicalOperand var2) {
            this.a = var1;
            this.b = var2;
        }

        public Boolean eval0(ConditionalVariableTranslator var1) {
            return this.a.a(Fn.nullSupplier(), new a(this.b, var1));
        }

        public String toString() {
            return this.a.symbol + this.b.toString();
        }

        public @NotNull String asString(boolean var1) {
            return this.a.symbol + this.b.asString(var1);
        }
    }

    public static final class BiLogicalOperator extends LogicalOperand {
        private final LogicalOperator a;
        private final LogicalOperand b;
        private final LogicalOperand c;

        public BiLogicalOperator(LogicalOperand var1, LogicalOperator var2, LogicalOperand var3) {
            this.a = var2;
            this.b = var1;
            this.c = var3;
        }

        public Boolean eval0(ConditionalVariableTranslator var1) {
            return this.a.a(new a(this.b, var1), new a(this.c, var1));
        }

        public String toString() {
            return "(" + this.b.toString() + ' ' + this.a.symbol + ' ' + this.c.toString() + ')';
        }

        public @NotNull String asString(boolean var1) {
            return this.b.toString() + ' ' + this.a.symbol + ' ' + this.c.toString();
        }
    }

    private static final class a implements Supplier<Object> {
        private final LogicalOperand a;
        private final ConditionalVariableTranslator b;

        public a(LogicalOperand var1, ConditionalVariableTranslator var2) {
            this.a = var1;
            this.b = var2;
        }

        public Object get() {
            return this.a.eval0(this.b);
        }
    }
}
