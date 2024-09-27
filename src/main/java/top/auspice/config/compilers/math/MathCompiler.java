package top.auspice.config.compilers.math;


import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.Ref;
import kotlin.ranges.IntRange;
import kotlin.text.StringsKt;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import top.auspice.config.compilers.base.expressions.MathExpression;
import top.auspice.config.compilers.base.translators.MathematicalVariableTranslator;
import top.auspice.utils.time.TimeUtils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Stream;


@SuppressWarnings("unused")
public final class MathCompiler {
    @NotNull
    public static final Companion Companion;
    @NotNull
    private final String a;
    private int b;
    private final int c;
    private final boolean d;
    @NotNull
    private final LinkedList<LexicalEnvironment> e;
    @NotNull
    private final LinkedList<Object> f;
    @NotNull
    private static final Map<String, Double> g = new HashMap<>(8);
    @NotNull
    private static final Map<String, Function> h = new HashMap<>(44);
    @NotNull
    private static final Operator[] i = new Operator[127];
    @NotNull
    private static final MathematicalVariableTranslator j = MathCompiler::b;
    
    @NotNull
    public static final Expression DEFAULT_VALUE;

    private MathCompiler(@NotNull String var1, int var2, int var3, boolean var4, @NotNull LinkedList<LexicalEnvironment> var5) {
        this.a = var1;
        this.b = var2;
        this.c = var3;
        this.d = var4;
        this.e = var5;
        this.f = new LinkedList<>();
    }

    private int a(int var1, boolean var2) {
        int var3 = var1;
        char var4 = this.a.charAt(var1);
        if (!var2) {
            while(('a' <= var4 && var4 < '{') || ('A' <= var4 && var4 < '[') || ('0' <= var4 && var4 < ':') || var4 == '_') {
                ++var3;
                if (var3 == this.c) {
                    return var3;
                }

                var4 = this.a.charAt(var3);
            }
        } else {
            while(true) {
                if (var4 == '}') {
                    ++var3;
                    break;
                }

                ++var3;
                if (var3 == this.c) {
                    throw a(var1 - 1, "Unclosed variable interpolation", List.of(4));
                }

                var4 = this.a.charAt(var3);
            }
        }

        return var3;
    }

    private Expression a() throws NumberFormatException, MathEvaluateException {
        if (this.b == this.c) {
            return this.b();
        } else {
            do {
                int var3 = this.b;
                char var1;
                int var2;
                if ((var1 = this.a.charAt(var3)) != ' ') {
                    int var4;
                    int var5;
                    int var6;
                    MathCompiler var18;
                    char var21;
                    Expression var27;
                    String var32;
                    Expression var33;
                    if ('0' <= var1 && var1 < ':') {
                        var2 = this.b;
                        var18 = this;
                        var4 = this.b;

                        for(var21 = this.a.charAt(var4); ('0' <= var21 && var21 < ':') || var21 == 'x' || var21 == 'e' || var21 == 'E' || var21 == '-' || var21 == '.'; var21 = var18.a.charAt(var4)) {
                            if (var21 == '-') {
                                var6 = var18.b - 1;
                                if ((var5 = var18.a.charAt(var6)) != 101 && var5 != 69) {
                                    break;
                                }
                            }

                            ++var18.b;
                            int var10000 = var18.b;
                            if (var18.b == var18.c) {
                                break;
                            }

                            var4 = var18.b;
                        }

                        var5 = this.b;
                        this.b = var5 + -1;
                        var32 = this.a.substring(var2, var5);
                        Intrinsics.checkNotNullExpressionValue(var32, "");
                        String var24 = var32;

                        ConstantExpr var25;
                        try {
                            var25 = new ConstantExpr(Double.parseDouble(var24), MathCompiler.ConstantExprType.NUMBER);
                        } catch (NumberFormatException var16) {
                            throw this.a(var2, "Invalid numeric value \"" + var24 + '"', MathCompiler.Companion.access$pointerToName(Companion, var2, var24));
                        }

                        var33 = var25;
                    } else if ('a' <= var1 && var1 < '{' || ('A' <= var1 && var1 < '[')) {
                        var33 = this.a(false);
                    } else if (var1 == '{') {
                        var2 = this.b++;
                        var33 = this.a(true);
                    } else {
                        if (var1 == '"') {
                            var2 = this.b++;
                            StringBuilder var23 = new StringBuilder();

                            do {
                                var6 = this.b++;
                                var21 = this.a.charAt(var6);
                                var23.append(var21);
                            } while(var21 != '"');

                            String var10002 = var23.toString();
                            Intrinsics.checkNotNullExpressionValue(var10002, "");
                            return new StringConstant(var10002);
                        }

                        LexicalEnvironment var38;
                        if (var1 == ',' || var1 == ';') {
                            var38 = this.e.peekLast();
                            if (var38 == null) {
                                throw a(this.b, "Function argument separator outside of functions", List.of(4));
                            }

                            LexicalEnvironment var22 = var38;
                            if (var38.getFunction() == null) {
                                Stream<LexicalEnvironment> var39 = this.e.stream();
                                Function1<LexicalEnvironment, Boolean> var30 = isInsideFunction.INSTANCE;

                                if (var39.anyMatch(new b(var30) {

                                })) {
                                    throw a(var22.getIndex(), "Unclosed parentheses", List.of(4));
                                }

                                throw a(this.b, "Function argument separator outside of functions", List.of(4));
                            }

                            return this.b();
                        }

                        if (var1 == '[') {
                            var4 = this.b;
                            if ((var5 = StringsKt.indexOf$default((CharSequence)this.a, ']', var4 + 1, false, 4, (Object)null)) == -1) {
                                throw a(var4, "Cannot find time literal closing bracket.", List.of(4));
                            }

                            ++var4;
                            byte var34;
                            if (this.a.charAt(var4) == '-') {
                                ++var4;
                                var34 = -1;
                            } else {
                                var34 = 1;
                            }

                            byte var28 = var34;
                            var32 = this.a.substring(var4, var5);
                            Intrinsics.checkNotNullExpressionValue(var32, "");
                            String var17 = var32;
                            this.b = var5;
                            Long var35 = TimeUtils.parseTime(var17);
                            if (var35 == null) {
                                throw this.a(this.b, "Unknown time format", CollectionsKt.toMutableList((new IntRange(var4, var5))));
                            }

                            long var14 = var35;
                            var33 = new ConstantExpr((double)var28 * (double)var14, ConstantExprType.TIME);
                        } else if (var1 == '(') {
                            LinkedList<LexicalEnvironment> var36 = this.e;
                            var2 = this.b++;
                            var36.add(new LexicalEnvironment(var2, null));
                            MathCompiler var19;
                            var27 = (var19 = new MathCompiler(this.a, this.b, this.c, true, this.e)).a();
                            this.b = var19.b;
                            var33 = var27;
                        } else {
                            if (var1 == ')') {
                                var38 = this.e.pollLast();
                                if (var38 == null) {
                                    throw a(this.b, "No opening parentheses found for closing parenthes", List.of(4));
                                }

                                if (var38.getFunction() == null && this.f.isEmpty()) {
                                    throw a(this.b, "Empty subexpression", List.of(4));
                                }

                                return this.b();
                            }

                            var33 = null;
                        }
                    }

                    Expression var20 = var33;
                    Object var26;
                    if (var33 == null) {
                        Operator var37 = i[var1];
                        if (var37 == null) {
                            throw a(this.b, "Unrecognized character '" + var1 + "' (" + var1 + ") outside of variable/placeholder interpolation", List.of(4));
                        }

                        Operator var31 = var37;
                        var26 = this.f.peekLast();
                        if (!var31.getArity$core().isUnary$core() && var26 instanceof Operator) {
                            throw a(this.b, "Blank operand on the left hand side of binary operator", List.of(4));
                        }

                        this.f.addLast(var31);
                    } else {
                        var18 = this;
                        if (this.f.isEmpty()) {
                            this.f.add(var20);
                        } else {
                            var27 = var20;
                            if (!((var26 = this.f.getLast()) instanceof Operator)) {
                                throw a(this.b, "Expected an operator before operand", List.of(4));
                            }

                            label196:
                            for(; var26 instanceof Operator; var26 = var18.f.peekLast()) {
                                switch (MathCompiler.WhenMappings.$EnumSwitchMapping$0[((Operator)var26).getArity$core().ordinal()]) {
                                    case 1:
                                        break label196;
                                    case 2:
                                        var27 = new BiOperation(DEFAULT_VALUE, (Operator)var26, var27);
                                        var18.f.removeLast();
                                        break;
                                    case 3:
                                        var18.f.removeLast();
                                        Object var29;
                                        if ((var29 = var18.f.peekLast()) != null && !(var29 instanceof Operator)) {
                                            var18.f.add(var26);
                                            break label196;
                                        }

                                        var27 = new BiOperation(DEFAULT_VALUE, (Operator)var26, var27);
                                }
                            }

                            var18.f.addLast(var27);
                        }
                    }
                }

                var2 = this.b++;
            } while(this.b < this.c);

            return this.b();
        }
    }




    public static final class isInsideFunction extends Lambda<Boolean> implements Function1<MathCompiler.LexicalEnvironment, Boolean> {
        public static final isInsideFunction INSTANCE = new isInsideFunction();

        public isInsideFunction() {
            super(1);
        }

        public Boolean invoke(MathCompiler.LexicalEnvironment var1) {
            return var1.getFunction() != null;
        }
    }

    private Expression b() {
        if ((this.b >= this.c || !this.d) && !this.e.isEmpty()) {
            final ArrayList<Integer> var11 = new ArrayList<>();
            final Ref.BooleanRef var9 = new Ref.BooleanRef();

            for (LexicalEnvironment var1 : this.e) {
                var11.add(var1.getIndex());
                if (!var9.element) {
                    var9.element = var1.getFunction() != null;
                }
            }

            throw this.a(this.e.getLast().getIndex(), "Unclosed parentheses" + (var9.element ? " and functions" : ""), var11);
        } else if (this.f.isEmpty()) {
            return DEFAULT_VALUE;
        } else {
            Object var12;
            if (this.f.size() == 1) {
                var12 = this.f.getLast();
                Intrinsics.checkNotNull(var12);
                return (Expression)var12;
            } else {
                Object var2;
                if ((var2 = this.f.getLast()) instanceof Operator) {
                    String var10 = ((Operator)var2).getSymbol() == '%' ? " (Hint: Write placeholder without % around them." : "";
                    throw a(this.c - 1, "Blank operand on right hand side of " + ((Operator)var2).getSymbol() + var10, List.of(4));
                } else {
                    BiOperation var1 = null;
                    ListIterator<Object> var10000 = this.f.listIterator();
                    Intrinsics.checkNotNullExpressionValue(var10000, "");

                    BiOperation var13;
                    for(; var10000.hasNext(); var1 = var13) {
                        var12 = var10000.next();
                        Intrinsics.checkNotNull(var12);
                        Expression var7 = (Expression)var12;
                        var12 = var10000.next();
                        Intrinsics.checkNotNull(var12);
                        Operator var3 = (Operator)var12;
                        var12 = var10000.next();
                        Intrinsics.checkNotNull(var12);
                        Expression var4 = (Expression)var12;
                        if (!var10000.hasNext()) {
                            var13 = new BiOperation(var7, var3, var4);
                        } else {
                            var12 = var10000.next();
                            Intrinsics.checkNotNull(var12);
                            Operator var5 = (Operator)var12;
                            var12 = var10000.next();
                            Intrinsics.checkNotNull(var12);
                            Expression var6 = (Expression)var12;
                            if (var3.hasPrecedenceOver$core(var5)) {
                                var1 = new BiOperation(var7, var3, var4);
                                var10000.previous();
                                var10000.previous();
                                var10000.previous();
                                var10000.remove();
                                var10000.previous();
                                var10000.remove();
                                var10000.previous();
                                var10000.set(var1);
                                var13 = var1;
                            } else {
                                var1 = new BiOperation(var4, var5, var6);
                                var10000.remove();
                                var10000.previous();
                                var10000.remove();
                                var10000.previous();
                                var10000.set(var1);
                                var10000.previous();
                                var10000.previous();
                                var13 = var1;
                            }
                        }
                    }

                    Intrinsics.checkNotNull(var1);
                    return var1;
                }
            }
        }
    }

    private Expression a(boolean var1) {
        int var2 = this.a(this.b, var1);
        String var10000 = this.a.substring(this.b, var1 ? var2 - 1 : var2);
        Intrinsics.checkNotNullExpressionValue(var10000, "");
        if (Intrinsics.areEqual(var10000, "_")) {
            throw a(this.b, "Reserved single underscore identifier", List.of(4));
        } else {
            this.b = var2;
            MathCompiler var3 = this;
            int var4 = this.b;
            int var5 = this.c;

            for(; var4 < var5 && var3.a.charAt(var4) == ' '; ++var4) {
            }

            this.b = var4;
            if (this.b < this.c) {
                var4 = this.b;
                if (this.a.charAt(var4) == '(') {
                    return this.a(var10000);
                }
            }

            this.b = var2 - 1;
            Double var8;
            return (var8 = g.get(var10000)) == null ? new a(var10000) : new ConstantExpr(var8, ConstantExprType.CONSTANT_VARIABLE);
        }
    }

    private FunctionExpr a(String var1) {
        String var2 = (var2 = MathCompiler.Companion.access$findFunction(Companion, var1)) == null ? "" : "; Did you mean '" + var2 + "' function?";
        Function var10000 = h.get(var1);
        if (var10000 == null) {
            throw a(this.b, "Unknown function: " + var1 + var2, List.of(4));
        } else {
            int var3 = this.b++;
            ArrayList<Expression> var9 = new ArrayList<>();
            LexicalEnvironment var4 = new LexicalEnvironment(this.b, var10000);
            this.e.add(var4);
            int var5 = this.b;

            do {
                MathCompiler var6;
                Expression var7;
                if (!Intrinsics.areEqual(var7 = (var6 = new MathCompiler(this.a, this.b, this.c, true, this.e)).a(), DEFAULT_VALUE)) {
                    var9.add(var7);
                }

                this.b = var6.b + 1;
            } while(Intrinsics.areEqual(this.e.peekLast(), var4));

            int var10 = this.b;
            this.b = var10 + -1;
            if (var10000.getArgCount() < 0) {
                var10 = Math.abs(var10000.getArgCount()) - 1;
                if (var9.size() < var10) {
                    throw this.a(var5, "Too few arguments for function '" + var1 + "', expected at least: " + var10 + ", got: " + var9.size(), MathCompiler.Companion.access$pointerToName(Companion, var5, var1));
                }
            } else {
                if (var9.size() < var10000.getArgCount()) {
                    throw this.a(var5, "Too few arguments for function '" + var1 + "', expected: " + var10000.getArgCount() + ", got: " + var9.size(), MathCompiler.Companion.access$pointerToName(Companion, var5, var1));
                }

                if (var9.size() > var10000.getArgCount()) {
                    throw this.a(var5, "Too many arguments for function '" + var1 + "', expected: " + var10000.getArgCount() + ", got: " + var9.size(), MathCompiler.Companion.access$pointerToName(Companion, var5, var1));
                }
            }

            Collection var11;
            return new FunctionExpr(var1, var10000, var9.toArray(new Expression[0]));
        }
    }

    private MathEvaluateException a(int var1, String var2, Collection<Integer> var3) {
        var2 = "\n" + var2 + " at offset " + var1 + " in expression: \n\"" + this.a + '"';
        int var4 = 0;
        var3.add(var1);

        for (Integer integer : var3) {
            int var5;
            if ((var5 = ((Number) integer).intValue()) > var4) {
                var4 = var5;
            }
        }

        char[] var7;
        Arrays.fill(var7 = new char[var4 + 2], ' ');
        String var8 = new String(var7);
        var3.forEach(MathCompiler::a);
        return new MathEvaluateException(var2 + '\n' + var8);
    }

    private static void a(Function1<Object, Unit> var0, Object var1) {
        Objects.requireNonNull(var0, "");
        var0.invoke(var1);
    }

    private static void a(StringBuilder var0, int var1) {
        Objects.requireNonNull(var0, "");
        var0.setCharAt(var1 + 1, '^');
    }

    private static Double b(String var0) {
        Objects.requireNonNull(var0, "");
        throw new IllegalAccessError();
    }

    @NotNull
    public static Map<String, Double> getConstants() {
        return Companion.getConstants();
    }

    @NotNull
    public static Map<String, Function> getFunctions() {
        return Companion.getFunctions();
    }

    @NotNull
    public static Expression compile(@Nullable String var0) throws NumberFormatException, ArithmeticException {
        return Companion.compile(var0);
    }

    static {
        Companion = new Companion();
        DEFAULT_VALUE = new ConstantExpr(0.0, ConstantExprType.NUMBER);
        MathCompiler.Companion.registerOperators();
        MathCompiler.Companion.registerFunctions();
        MathCompiler.Companion.registerConstants();
    }


    public enum Arity {
        UNARY,
        BINARY,
        UNARY_AND_BINARY;

        Arity() {
        }

        public final boolean isUnary$core() {
            return this == UNARY || this == UNARY_AND_BINARY;
        }


    }


    public static final class BiOperation extends Expression {
        @NotNull
        private final Expression a;
        @NotNull
        private final Operator b;
        @NotNull
        private final Expression c;

        public BiOperation(@NotNull Expression var1, @NotNull Operator var2, @NotNull Expression var3) {
            Objects.requireNonNull(var1, "");
            Objects.requireNonNull(var2, "");
            Objects.requireNonNull(var3, "");
            
            this.a = var1;
            this.b = var2;
            this.c = var3;
        }

        @NotNull
        public Expression getLeft$core() {
            return this.a;
        }

        @NotNull
        public Operator getOp$core() {
            return this.b;
        }

        @NotNull
        public Expression getRight$core() {
            return this.c;
        }

        @NotNull
        public Double eval(@NotNull MathematicalVariableTranslator var1) {
            Objects.requireNonNull(var1, "");
            return this.b.getFunction$core().apply(this.a.eval(var1), this.c.eval(var1));
        }

        @NotNull
        public String asString(boolean var1) {
            return this.a.asString(var1) + ' ' + this.b.getSymbol() + ' ' + this.c.asString(var1);
        }

        @NotNull
        public String toString() {
            return "(" + this.a + ' ' + this.b.getSymbol() + (this.b.getSymbol() == '(' ? "" : ' ') + this.c + ')';
        }
    }


    
    public static final class Companion {
        private Companion() {
        }

        
        @NotNull
        public Map<String, Double> getConstants() {
            return MathCompiler.g;
        }

        
        @NotNull
        public Map<String, Function> getFunctions() {
            return MathCompiler.h;
        }

        private static void a(Operator var0) {
            if (var0.getSymbol() >= MathCompiler.i.length) {
                String var1 = "Operator handler cannot handle char '" + var0.getSymbol() + "' with char code: " + var0.getSymbol();
                throw new IllegalArgumentException(var1);
            } else {
                MathCompiler.i[var0.getSymbol()] = var0;
            }
        }

        private static void a(String var0, boolean var1, QuantumFunction var2, int var3) {
            MathCompiler.h.put(var0, new Function(var2, var1, var3));
        }

        @NotNull
        public Expression compile(@Nullable String var1) throws NumberFormatException, ArithmeticException {
            CharSequence var2;
            return (var2 = var1) == null || var2.isEmpty() ? MathCompiler.DEFAULT_VALUE : this.a((new MathCompiler(var1, 0, 0, false, new LinkedList<>())).a()).withOriginalString(var1);
        }

        private Expression a(Expression var1) {
            if (var1 instanceof BiOperation) {
                Expression var2 = this.a(((BiOperation)var1).getLeft$core());
                Expression var3 = this.a(((BiOperation)var1).getRight$core());
                if (var2 instanceof ConstantExpr && var3 instanceof ConstantExpr) {
                    return new ConstantExpr(((BiOperation)var1).getOp$core().getFunction$core().apply(((ConstantExpr)var2).getValue(), ((ConstantExpr)var3).getValue()), ConstantExprType.OPTIMIZED);
                }
            } else if (var1 instanceof FunctionExpr) {
                if (!((FunctionExpr)var1).getHandler$core().getOptimizable$core()) {
                    return var1;
                }

                boolean var8 = true;
                ArrayList<Expression> var9 = new ArrayList<>(((FunctionExpr)var1).getArgs$core().length);
                Expression[] var4 = ((FunctionExpr)var1).getArgs$core();
                int var5 = 0;

                for(int var6 = var4.length; var5 < var6; ++var5) {
                    Expression var7 = var4[var5];
                    var7 = this.a(var7);
                    var9.add(var7);
                    if (var8) {
                        var8 = var7 instanceof ConstantExpr;
                    }
                }

                if (var8) {
                    return new ConstantExpr(((FunctionExpr)var1).getHandler$core().getFunction$core().apply(new FnArgs((FunctionExpr)var1, MathCompiler.j)), ConstantExprType.OPTIMIZED);
                }

                Collection<Expression> var10;
                return new FunctionExpr(((FunctionExpr)var1).getName$core(), ((FunctionExpr)var1).getHandler$core(), var9.toArray(new Expression[0]));
            }

            return var1;
        }

        private static double pow(double var0, double var2) {
            return Math.pow(var0, var2);
        }

        private static double c(double var0, double var2) {
            return var0 * var2;
        }

        private static double d(double var0, double var2) {
            return var0 / var2;
        }

        private static double e(double var0, double var2) {
            return var0 % var2;
        }

        private static double sum(double var0, double var2) {
            return Double.sum(var0, var2);
        }

        private static double minus(double var0, double var2) {
            return var0 - var2;
        }

        private static double h(double var0, double var2) {
            return (double)(~((long)var2));
        }

        private static double rotateLeft(double var0, double var2) {
            return (double)Long.rotateLeft((long)var0, (int)var2);
        }

        private static double rotateRight(double var0, double var2) {
            return (double)Long.rotateRight((long)var0, (int)var2);
        }

        private static double k(double var0, double var2) {
            return (double)((long)var0 >> (int)((long)var2));
        }

        private static double l(double var0, double var2) {
            return (double)((long)var0 << (int)((long)var2));
        }

        private static double m(double var0, double var2) {
            return (double)((long)var0 >>> (int)((long)var2));
        }

        private static double n(double var0, double var2) {
            return (double)((long)var0 & (long)var2);
        }

        private static double o(double var0, double var2) {
            return (double)((long)var0 ^ (long)var2);
        }

        private static double p(double var0, double var2) {
            return (double)((long)var0 | (long)var2);
        }

        private static double abs(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Math.abs(var0.next());
        }

        private static double acos(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Math.acos(var0.next());
        }

        private static double asin(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Math.asin(var0.next());
        }

        private static double atan(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Math.atan(var0.next());
        }

        private static double cbrt(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Math.cbrt(var0.next());
        }

        private static double ceil(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Math.ceil(var0.next());
        }

        private static double cos(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Math.cos(var0.next());
        }

        private static double cosh(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Math.cosh(var0.next());
        }

        private static double exp(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Math.exp(var0.next());
        }

        private static double expm1(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Math.expm1(var0.next());
        }

        private static double floor(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Math.floor(var0.next());
        }

        private static double getExponent(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Math.getExponent(var0.next());
        }

        private static double log(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Math.log(var0.next());
        }

        private static double log10(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Math.log10(var0.next());
        }

        private static double log1p(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Math.log1p(var0.next());
        }

        private static double maxOrThrow(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return CollectionsKt.maxOrThrow(var0.allArgs());
        }

        private static double minOrThrow(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return CollectionsKt.minOrThrow(var0.allArgs());
        }

        private static double nextUp(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Math.nextUp(var0.next());
        }

        private static double nextDown(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Math.nextDown(var0.next());
        }

        private static double nextAfter(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Math.nextAfter(var0.next(), var0.next());
        }

        private static double doubleRandom(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return ThreadLocalRandom.current().nextDouble(var0.next(), var0.next() + 1.0);
        }

        private static double intRandom(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return ThreadLocalRandom.current().nextInt((int)var0.next(), (int)var0.next() + 1);
        }

        private static double round(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return (double)Math.round(var0.next());
        }

        private static double rint(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Math.rint(var0.next());
        }

        private static double signum(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Math.signum(var0.next());
        }

        private static double z(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return var0.next() / var0.next() * 100.0;
        }

        private static double A(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return var0.next() / 100.0 * var0.next();
        }

        private static double sin(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Math.sin(var0.next());
        }

        private static double sinh(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Math.sinh(var0.next());
        }

        private static double D(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return (double)Double.doubleToRawLongBits(var0.next());
        }

        private static double hashCode(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Double.hashCode(var0.next());
        }

        private static double identityHashCode(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return System.identityHashCode(var0.next());
        }

        private static double G(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return (double)System.currentTimeMillis();
        }

        private static double H(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Math.sqrt(var0.next());
        }

        private static double I(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Math.tan(var0.next());
        }

        private static double J(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Math.tanh(var0.next());
        }

        private static double K(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Math.toDegrees(var0.next());
        }

        private static double L(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Math.toRadians(var0.next());
        }

        private static double M(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Math.ulp(var0.next());
        }

        private static double N(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Math.scalb(var0.next(), (int)var0.next());
        }

        private static double O(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Math.hypot(var0.next(), var0.next());
        }

        private static double P(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Math.copySign(var0.next(), var0.next());
        }

        private static double Q(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return Math.IEEEremainder(var0.next(), var0.next());
        }

        private static double R(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            int var1;
            return (double)((var1 = (int)var0.next()) * (var1 + 1)) / 2.0;
        }

        private static double S(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return (double)Long.reverse((long)var0.next());
        }

        private static double T(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return (double)Long.reverseBytes((long)var0.next());
        }

        private static double U(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return var0.next() == var0.next() ? var0.next() : var0.next(3);
        }

        private static double V(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return var0.next() != var0.next() ? var0.next() : var0.next(3);
        }

        private static double W(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return var0.next() > var0.next() ? var0.next() : var0.next(3);
        }

        private static double X(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return var0.next() < var0.next() ? var0.next() : var0.next(3);
        }

        private static double Y(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return var0.next() >= var0.next() ? var0.next() : var0.next(3);
        }

        private static double Z(FnArgs var0) {
            Objects.requireNonNull(var0, "");
            return var0.next() <= var0.next() ? var0.next() : var0.next(3);
        }

        private static boolean a(Function1<Object, Boolean> var0, Object var1) {
            Objects.requireNonNull(var0, "");
            return var0.invoke(var1);
        }

        private static boolean b(java.util.function.Function<Object, Boolean> var0, Object var1) {
            Objects.requireNonNull(var0, "");
            return var0.apply(var1);
        }

        private static String a(final String var0) {
            Objects.requireNonNull(var0, "");

            return MathCompiler.h.keySet().stream().filter((var1) -> {
                Objects.requireNonNull(var1, "");
                Locale var10000 = Locale.ENGLISH;
                Intrinsics.checkNotNullExpressionValue(var10000, "");
                String var2 = var1.toLowerCase(var10000);
                Intrinsics.checkNotNullExpressionValue(var2, "");
                var1 = var2;
                return (StringsKt.contains(var0, var1, false) || StringsKt.contains(var1, var0, false) && Math.abs(var1.length() - var0.length()) < 2);
            }).findFirst().orElse(null);
        }

        public String access$findFunction(MathCompiler.Companion companion, String a) {




            return companion.getFunctions().getOrDefault(a, );
            //TODO
        }

        public Collection<Integer> access$pointerToName(MathCompiler.Companion companion, int var5, String var1) {




            //TODO
        }

        public void registerOperators() {
            //TODO
        }

        public void registerFunctions() {
            //TODO
        }

        public void registerConstants() {
            //TODO
        }
    }
    
    
    public static class ConstantExpr extends Expression {
        private final double a;
        @NotNull
        private final ConstantExprType b;

        public ConstantExpr(double var1, @NotNull ConstantExprType var3) {
            Objects.requireNonNull(var3, "");
            
            this.a = var1;
            this.b = var3;
        }
        
        public final double getValue() {
            return this.a;
        }

        @NotNull
        public final ConstantExprType getType() {
            return this.b;
        }

        public ConstantExpr(double var1) {
            this(var1, MathCompiler.ConstantExprType.NUMBER);
        }

        @NotNull
        public Double eval(@NotNull MathematicalVariableTranslator var1) {
            Objects.requireNonNull(var1, "");
            return this.a;
        }

        @NotNull
        public String asString(boolean var1) {
            return String.valueOf(this.a);
        }

        @NotNull
        public String toString() {
            return "ConstantExpr(" + this.a + ')';
        }
    }


    public enum ConstantExprType {
        OPTIMIZED,
        NUMBER,
        STRING,
        CONSTANT_VARIABLE,
        TIME;

        ConstantExprType() {
        }

        
    }


    public abstract static class Expression implements MathExpression {
        @Nullable
        private String a;

        public Expression() {
        }

        @Nullable
        public final String getOriginalString$core() {
            return this.a;
        }


        public final void setOriginalString$core(@Nullable String var1) {
            this.a = var1;
        }

        @NotNull
        public abstract Double eval(@NotNull MathematicalVariableTranslator var1);

        public boolean isDefault() {
            return this == MathCompiler.DEFAULT_VALUE;
        }

        @Nullable
        public String getOriginalString() {
            return this.a;
        }

        @NotNull
        public final Expression withOriginalString(@NotNull String var1) {
            Objects.requireNonNull(var1, "");
            this.a = var1;
            return this;
        }

        public final <T extends Expression> boolean contains(@NotNull Class<T> var1, @NotNull Predicate<T> var2) {
            Objects.requireNonNull(var1, "");
            Objects.requireNonNull(var2, "");
            if (var1.isInstance(this)) {
                Intrinsics.checkNotNull(this);
                if (var2.test((T) this)) {
                    return true;
                }
            }

            if (this instanceof BiOperation) {
                return ((BiOperation)this).getLeft$core().contains(var1, var2) || ((BiOperation)this).getRight$core().contains(var1, var2);
            } else if (this instanceof FunctionExpr) {
                Expression[] var3 = ((FunctionExpr)this).getArgs$core();
                int var4 = 0;

                for(int var5 = var3.length; var4 < var5; ++var4) {
                    if (var3[var4].contains(var1, var2)) {
                        return true;
                    }
                }

                return false;
            } else {
                return false;
            }
        }
    }


    public static final class FnArgs {
        @NotNull
        private final FunctionExpr a;
        @NotNull
        private final MathematicalVariableTranslator b;
        private int c;

        public FnArgs(@NotNull FunctionExpr var1, @NotNull MathematicalVariableTranslator var2) {
            Objects.requireNonNull(var1, "");
            Objects.requireNonNull(var2, "");
            
            this.a = var1;
            this.b = var2;
        }

        public double next() {
            Expression[] var10000 = this.a.getArgs$core();
            int var1 = this.c++;
            return var10000[var1].eval(this.b);
        }

        @NotNull
        public List<Double> allArgs() {
            Expression[] var1;
            Expression[] var2 = var1 = this.a.getArgs$core();
            List<Double> var7 = new ArrayList<>(var1.length);
            int var3 = 0;

            for(int var4 = var2.length; var3 < var4; ++var3) {
                Expression var5 = var2[var3];
                var7.add(var5.eval(this.b));
            }

            return var7;
        }

        public double next(int var1) {
            Expression[] var2 = this.a.getArgs$core();
            this.c = var1;
            return var2[var1].eval(this.b);
        }
    }


    public static final class Function {
        @NotNull
        private final QuantumFunction a;
        private final boolean b;
        private final int c;

        public Function(@NotNull QuantumFunction var1, boolean var2, int var3) {
            Objects.requireNonNull(var1, "");
            this.a = var1;
            this.b = var2;
            this.c = var3;
        }

        @NotNull
        public QuantumFunction getFunction$core() {
            return this.a;
        }


        public boolean getOptimizable$core() {
            return this.b;
        }


        public int getArgCount() {
            return this.c;
        }
    }


    public static final class FunctionExpr extends Expression {
        @NotNull
        private final String a;
        @NotNull
        private final Function b;
        @NotNull
        private final Expression[] c;

        public FunctionExpr(@NotNull String var1, @NotNull Function var2, @NotNull Expression[] var3) {
            Objects.requireNonNull(var1, "");
            Objects.requireNonNull(var2, "");
            Objects.requireNonNull(var3, "");
            this.a = var1;
            this.b = var2;
            this.c = var3;
        }

        @NotNull
        public String getName$core() {
            return this.a;
        }

        @NotNull
        public Function getHandler$core() {
            return this.b;
        }

        @NotNull
        public Expression[] getArgs$core() {
            return this.c;
        }

        @NotNull
        public Double eval(@NotNull MathematicalVariableTranslator var1) {
            Objects.requireNonNull(var1, "");
            return this.b.getFunction$core().apply(new FnArgs(this, var1));
        }

        @NotNull
        public String asString(boolean var1) {
            return this.a + '(' + ArraysKt.joinToString(this.c, (CharSequence)", ", (CharSequence)null, (CharSequence)null, 0, (CharSequence)null, (Function1)null, 62, (Object)null) + ')';
        }

        @NotNull
        public String toString() {
            Object[] var1;
            Intrinsics.checkNotNullExpressionValue(var1 = Arrays.stream(this.c).map(Object::toString).toArray(FunctionExpr::a), "");
            String[] var2 = (String[])var1;
            return this.a + '(' + (this.c.length == 0 ? "" : ArraysKt.joinToString(var2, (CharSequence)", ", (CharSequence)null, (CharSequence)null, 0, (CharSequence)null, null, 62, (Object)null)) + ')';
        }

        private static String a(Function1<Object, String> var0, Object var1) {
            Objects.requireNonNull(var0, "");
            return var0.invoke(var1);
        }

        private static String[] a(int var0) {
            return new String[var0];
        }
    }


    public static final class LexicalEnvironment {
        private final int a;
        @Nullable
        private final Function b;

        public LexicalEnvironment(int var1, @Nullable Function var2) {
            this.a = var1;
            this.b = var2;
        }


        public int getIndex() {
            return this.a;
        }

        @Nullable
        public Function getFunction() {
            return this.b;
        }

        public boolean isFunction() {
            return this.getFunction() != null;
        }

        @NotNull
        public String toString() {
            return "LexicalEnvironment{index=" + this.a + ", function=" + (this.getFunction() != null) + '}';
        }
    }


    public static final class MathEvaluateException extends ArithmeticException {
        public MathEvaluateException(@NotNull String var1) {
            super(var1);
        }
    }
    
    
    public static final class Operator {
        private final char a;
        private final byte b;
        private final byte c;
        @NotNull
        private final Side d;
        @NotNull
        private final Arity e;
        @NotNull
        private final TriDoubleFn f;

        public Operator(char var1, int var2, int var3, @NotNull Side var4, @NotNull TriDoubleFn var5) {
            Objects.requireNonNull(var4, "");
            Objects.requireNonNull(var5, "");
            this.a = var1;
            this.b = (byte)var2;
            this.c = (byte)var3;
            this.d = var4;
            this.f = var5;
            this.e = (var1) == '-' ? MathCompiler.Arity.UNARY_AND_BINARY : (var1 == '~' ? MathCompiler.Arity.UNARY : MathCompiler.Arity.BINARY);
        }


        public char getSymbol() {
            return this.a;
        }

        @NotNull
        public Arity getArity$core() {
            return this.e;
        }

        @NotNull
        public TriDoubleFn getFunction$core() {
            return this.f;
        }

        public Operator(char var1, int var2, @NotNull TriDoubleFn var3) {
            this(var1, var2, var2, MathCompiler.Side.NONE, var3);
        }

        @NotNull
        public String toString() {
            return "MathOperator['" + this.a + "']";
        }

        public boolean hasPrecedenceOver$core(@NotNull Operator var1) {
            Objects.requireNonNull(var1, "");
            return this.b >= var1.b;
        }
    }

    

    public interface QuantumFunction {
        double apply(@NotNull FnArgs var1);
    }


    
    public enum Side {
        RIGHT,
        LEFT,
        NONE,
        ;

    }


    public static final class StringConstant extends ConstantExpr {
        @NotNull
        private final String a;

        public StringConstant(@NotNull String var1) {
            super(var1.hashCode(), MathCompiler.ConstantExprType.STRING);
            this.a = var1;
        }

        @NotNull
        public String getString$core() {
            return this.a;
        }

        @NotNull
        public Double eval(@NotNull MathematicalVariableTranslator var1) {
            Objects.requireNonNull(var1, "");
            return super.getValue();
        }

        @NotNull
        public String toString() {
            return "StringConstant(\"" + this.a + "\")";
        }

        @NotNull
        public String asString(boolean var1) {
            return this.a;
        }
    }


    
    public interface TriDoubleFn {
        double apply(double var1, double var3);
    }



    private static final class a extends Expression {
        @NotNull
        private final String a;

        public a(@NotNull String var1) {
            this.a = var1;
        }

        @NotNull
        public Double eval(@NotNull MathematicalVariableTranslator var1) {
            Objects.requireNonNull(var1, "");
            Double var3;
            if ((var3 = var1.apply(this.a)) == null) {
                String var4 = MathCompiler.Companion.access$findFunction(MathCompiler.Companion, this.a);
                String var2 = "";
                if (var4 != null) {
                    var2 = "; Did you mean to invoke '" + var4 + "' function? If so, put parentheses after the name like '" + var4 + "(args)'";
                }

                throw new MathEvaluateException("Unknown variable: '" + this.a + '\'' + var2);
            } else {
                return var3;
            }
        }

        @NotNull
        public String asString(boolean var1) {
            return this.a;
        }

        @NotNull
        public String toString() {
            return "MathVariable(" + this.a + ')';
        }
    }



    public static final class WhenMappings {
        static int[] $EnumSwitchMapping$0;

        static {
            int[] var0 = new int[Arity.values().length];

            try {
                var0[Arity.BINARY.ordinal()] = 1;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                var0[Arity.UNARY.ordinal()] = 2;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                var0[Arity.UNARY_AND_BINARY.ordinal()] = 3;
            } catch (NoSuchFieldError ignored) {
            }

            $EnumSwitchMapping$0 = var0;
        }
    }


    static class b implements Predicate<LexicalEnvironment> {

        private final Function1<LexicalEnvironment, Boolean> a;

        b(@NotNull Function1<LexicalEnvironment, Boolean> var1) {
            this.a = var1;
        }

        @Override
        public boolean test(LexicalEnvironment o) {
            return this.a.invoke(o);
        }
    }


}
