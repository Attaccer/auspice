package top.auspice.config.compilers.math;


import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.Ref;
import kotlin.ranges.IntRange;
import kotlin.text.StringsKt;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import top.auspice.config.compilers.base.expressions.MathExpression;
import top.auspice.config.compilers.base.translators.MathConfigStringTranslator;
import top.auspice.utils.time.TimeUtils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Stream;


@SuppressWarnings("unused")
public final class MathCompiler {
    @NotNull
    public static final Companion COMPANION = new Companion();
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
    private static final Map<String, Double> constants = new HashMap<>(8);
    @NotNull
    private static final Map<String, MathFunction> functions = new HashMap<>(44);
    @NotNull
    private static final Operator[] operators = new Operator[127];
    @NotNull
    private static final MathConfigStringTranslator j = MathCompiler::b;
    
    @NotNull
    public static final Expression DEFAULT_VALUE = new ConstantExpr(0.0, ConstantExprType.NUMBER);

    private MathCompiler(@NotNull String str, int index, int var3, boolean var4, @NotNull LinkedList<LexicalEnvironment> var5) {
        this.a = str;
        this.b = index;
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
                    throw e(this, var1 - 1, "Unclosed variable interpolation", null, 4);
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
                            var25 = new ConstantExpr(Double.parseDouble(var24), ConstantExprType.NUMBER);
                        } catch (NumberFormatException var16) {
                            throw this.e(var2, "Invalid numeric value \"" + var24 + '"', access$pointerToName(var2, var24));
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
                                throw e(this, this.b, "Function argument separator outside of functions", null, 4);
                            }

                            LexicalEnvironment var22 = var38;
                            if (var38.getFunction() == null) {
                                Stream<LexicalEnvironment> var39 = this.e.stream();
                                java.util.function.Function<LexicalEnvironment, Boolean> var30 = isInsideFunction.INSTANCE;

                                if (var39.anyMatch(new b(var30) {

                                })) {
                                    throw e(this, var22.getIndex(), "Unclosed parentheses", null, 4);
                                }

                                throw e(this, this.b, "Function argument separator outside of functions", null, 4);
                            }

                            return this.b();
                        }

                        if (var1 == '[') {
                            var4 = this.b;
                            if ((var5 = StringsKt.indexOf(this.a, ']', var4 + 1, false)) == -1) {
                                throw e(this,  var4, "Cannot find time literal closing bracket.", null, 4);
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
                                throw this.e(this.b, "Unknown time format", CollectionsKt.toMutableList((new IntRange(var4, var5))));
                            }

                            long var14 = var35;
                            var33 = new ConstantExpr((double)var28 * (double)var14, ConstantExprType.TIME);
                        } else if (var1 == '(') {
                            var2 = this.b++;
                            this.e.add(new LexicalEnvironment(var2, null));
                            MathCompiler var19;
                            var27 = (var19 = new MathCompiler(this.a, this.b, this.c, true, this.e)).a();
                            this.b = var19.b;
                            var33 = var27;
                        } else {
                            if (var1 == ')') {
                                var38 = this.e.pollLast();
                                if (var38 == null) {
                                    throw e(this, this.b, "No opening parentheses found for closing parenthes", null, 4);
                                }

                                if (var38.getFunction() == null && this.f.isEmpty()) {
                                    throw e(this, this.b, "Empty subexpression", null, 4);
                                }

                                return this.b();
                            }

                            var33 = null;
                        }
                    }

                    Expression var20 = var33;
                    Object var26;
                    if (var33 == null) {
                        Operator var37 = operators[var1];
                        if (var37 == null) {
                            throw e(this, this.b, "Unrecognized character '" + var1 + "' (" + var1 + ") outside of variable/placeholder interpolation", null, 4);
                        }

                        var26 = this.f.peekLast();
                        if (!var37.getArity().isUnary$core() && var26 instanceof Operator) {
                            throw e(this, this.b, "Blank operand on the left hand side of binary operator", null, 4);
                        }

                        this.f.addLast(var37);
                    } else {
                        var18 = this;
                        if (this.f.isEmpty()) {
                            this.f.add(var20);
                        } else {
                            var27 = var20;
                            if (!((var26 = this.f.getLast()) instanceof Operator)) {
                                throw e(this, this.b, "Expected an operator before operand", null, 4);
                            }

//                            label196:
//                            for(; var26 instanceof Operator; var26 = var18.f.peekLast()) {
//                                switch (OldMathCompiler.WhenMappings.$EnumSwitchMapping$0[((Operator)var26).getArity$core().ordinal()]) {
//                                    case 1:
//                                        break label196;
//                                    case 2:
//                                        var27 = new BiOperation(DEFAULT_VALUE, (Operator)var26, var27);
//                                        var18.f.removeLast();
//                                        break;
//                                    case 3:
//                                        var18.f.removeLast();
//                                        Object var29;
//                                        if ((var29 = var18.f.peekLast()) != null && !(var29 instanceof Operator)) {
//                                            var18.f.add(var26);
//                                            break label196;
//                                        }
//
//                                        var27 = new BiOperation(DEFAULT_VALUE, (Operator)var26, var27);
//                                }
//                            }

                            label196:
                            for(; var26 instanceof Operator; var26 = var18.f.peekLast()) {
                                switch (((Operator)var26).getArity()) {
                                    case BINARY:
                                        break label196;
                                    case UNARY:
                                        var27 = new BiOperation(DEFAULT_VALUE, (Operator)var26, var27);
                                        var18.f.removeLast();
                                        break;
                                    case UNARY_AND_BINARY:
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




    private static final class isInsideFunction extends Lambda<Boolean> implements java.util.function.Function<LexicalEnvironment, Boolean> {
        public static final isInsideFunction INSTANCE = new isInsideFunction();

        public isInsideFunction() {
            super(1);
        }

        public Boolean apply(LexicalEnvironment var1) {
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

            throw this.e(this.e.getLast().getIndex(), "Unclosed parentheses" + (var9.element ? " and functions" : ""), var11);
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
                    throw e(this, this.c - 1, "Blank operand on right hand side of " + ((Operator)var2).getSymbol() + var10, null, 4);
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



















    private Expression a(boolean bl) {
        int n;
        MathCompiler mathCompiler = this;
        int n2 = mathCompiler.a(mathCompiler.b, bl);
        String string = this.a.substring(this.b, bl ? n2 - 1 : n2);
        if (Intrinsics.areEqual(string, "_")) {
            MathCompiler mathCompiler2 = this;
            throw e(mathCompiler2, mathCompiler2.b, "Reserved single underscore identifier", null, 4);
        }
        this.b = n2;
        MathCompiler mathCompiler3 = this;
        int n3 = this.b;
        while (n3 < this.c && mathCompiler3.a.charAt(n3) == ' ') {
            ++n3;
        }
        mathCompiler3.b = n3;
        if (this.b < this.c) {
            MathCompiler mathCompiler5 = this;
            if (mathCompiler5.a.charAt(n3) == '(') {
                return this.a(string);
            }
        }
        this.b = n2 - 1;
        Double d = constants.get(string);
        if (d == null) {
            return new MathVariable(string);
        }
        return new ConstantExpr(d, ConstantExprType.CONSTANT_VARIABLE);
    }




    private FunctionExpr a(String var1) {
        String var2 = (var2 = access$findFunction(var1)) == null ? "" : "; Did you mean '" + var2 + "' function?";
        MathFunction var10000 = functions.get(var1);
        if (var10000 == null) {
            throw e(this, this.b, "Unknown function: " + var1 + var2, null, 4);
        } else {
            int var3 = this.b++;
            ArrayList<Expression> var9 = new ArrayList<>();
            LexicalEnvironment var4 = new LexicalEnvironment(this.b, var10000);
            this.e.add(var4);
            int var5 = this.b;

            do {
                MathCompiler var6 = new MathCompiler(this.a, this.b, this.c, true, this.e);
                Expression var7 = var6.a();
                if (!Intrinsics.areEqual(var7, DEFAULT_VALUE)) {
                    var9.add(var7);
                }

                this.b = var6.b + 1;
            } while (Intrinsics.areEqual(this.e.peekLast(), var4));

            int var10 = this.b;
            this.b = var10 + -1;
            if (var10000.getArgCount() < 0) {
                var10 = Math.abs(var10000.getArgCount()) - 1;
                if (var9.size() < var10) {
                    throw this.e(var5, "Too few arguments for function '" + var1 + "', expected at least: " + var10 + ", got: " + var9.size(), access$pointerToName(var5, var1));
                }
            } else {
                if (var9.size() < var10000.getArgCount()) {
                    throw this.e(var5, "Too few arguments for function '" + var1 + "', expected: " + var10000.getArgCount() + ", got: " + var9.size(), access$pointerToName(var5, var1));
                }

                if (var9.size() > var10000.getArgCount()) {
                    throw this.e(var5, "Too many arguments for function '" + var1 + "', expected: " + var10000.getArgCount() + ", got: " + var9.size(), access$pointerToName(var5, var1));
                }
            }

            return new FunctionExpr(var1, var10000, var9.toArray(new Expression[0]));
        }
    }





    private MathEvaluateException e(int n, String string, Collection<Integer> collection) {
        string = "\n" + string + " at offset " + n + " in expression: \n\"" + this.a + '\"';
        int n2 = 0;
        collection.add(n);
        for (Integer integer : collection) {
            int n3 = ((Number) integer).intValue();
            if (n3 <= n2) continue;
            n2 = n3;
        }
        int n4 = n2 + 2;
        char[] object = new char[n4];
        Arrays.fill(object, ' ');
        StringBuilder object0 = new StringBuilder(new String(object));
        collection.forEach(arg_0 -> MathCompiler.a(object0, arg_0));
        return new MathEvaluateException(string + '\n' + object0);
    }

    private static MathEvaluateException e(MathCompiler mathCompiler, int n, String string, Collection<Integer> collection, int n2) {
        collection = new ArrayList<>(n2);
        return mathCompiler.e(n, string, collection);
    }






    private static boolean i(java.util.function.Function<Object, Boolean> var0, Object var1) {
        Objects.requireNonNull(var0, "");
        return var0.apply(var1);
    }



    private static String findFunction(final String var0) {
        Objects.requireNonNull(var0, "");

        return MathCompiler.functions.keySet().stream().filter((var1) -> {
            Objects.requireNonNull(var1, "");
            Locale var10000 = Locale.ENGLISH;
            Intrinsics.checkNotNullExpressionValue(var10000, "");
            String var2 = var1.toLowerCase(var10000);
            Intrinsics.checkNotNullExpressionValue(var2, "");
            var1 = var2;
            return (StringsKt.contains(var0, var1, false) || StringsKt.contains(var1, var0, false) && Math.abs(var1.length() - var0.length()) < 2);
        }).findFirst().orElse(null);
    }

    public static Collection<Integer> access$pointerToName(int n, String name) {
        List<Integer> object = new ArrayList<>(name.length());
        int n3 = name.length();
        for (int i = 1; i < n3; ++i) {
            object.add(n + i);
        }
        return object;
    }

    public static String access$findFunction(String name) {
        Objects.requireNonNull(name, "");
        String string = name.toLowerCase(Locale.ENGLISH);
        Optional<String> object2 = MathCompiler.getFunctions().keySet().stream().filter(arg_0 -> i(new Companion.MathCompiler$Companion$1(string), arg_0)).findFirst();  //TODO class 1 ? 2
        return object2.orElseGet(() -> findFunction(name));
    }





























    private static void a(java.util.function.Function<Object, Unit> var0, Object var1) {
        Objects.requireNonNull(var0, "");
        var0.apply(var1);
    }

    private static void a(StringBuilder var0, int var1) {
        Objects.requireNonNull(var0, "");
        var0.setCharAt(var1 + 1, '^');
    }



    private static Double b(String var0) {
        throw new IllegalAccessError();
    }



    private static void default$registerFunctions() {
        a("abs", MathCompiler::abs, 0, 4);
        a("acos", MathCompiler::acos, 0, 4);
        a("asin", MathCompiler::asin, 0, 4);
        a("atan", MathCompiler::atan, 0, 4);
        a("cbrt", MathCompiler::cbrt, 0, 4);
        a("ceil", MathCompiler::ceil, 0, 4);
        a("cos", MathCompiler::cos, 0, 4);
        a("cosh", MathCompiler::cosh, 0, 4);
        a("exp", MathCompiler::exp, 0, 4);
        a("expm1", MathCompiler::expm1, 0, 4);
        a("floor", MathCompiler::floor, 0, 4);
        a("getExponent", MathCompiler::getExponent, 0, 4);
        a("log", MathCompiler::log, 0, 4);
        a("log10", MathCompiler::log10, 0, 4);
        a("log1p", MathCompiler::log1p, 0, 4);
        a("max", true, MathCompiler::max, -2);
        a("min", true, MathCompiler::min, -2);
        a("nextUp", MathCompiler::nextUp, 0, 4);
        a("nextDown", MathCompiler::nextDown, 0, 4);
        a("nextAfter", true, MathCompiler::nextAfter, 2);
        a("random", false, MathCompiler::random, 2);
        a("randInt", false, MathCompiler::randInt, 2);
        a("round", MathCompiler::round, 0, 4);
        a("rint", MathCompiler::rint, 0, 4);
        a("signum", MathCompiler::signum, 0, 4);
        a("whatPercentOf", true, MathCompiler::whatPercentOf, 2);
        a("percentOf", true, MathCompiler::percentOf, 2);
        a("sin", MathCompiler::sin, 0, 4);
        a("sinh", MathCompiler::sinh, 0, 4);
        a("bits", MathCompiler::bits, 0, 4);
        a("hash", MathCompiler::hash, 0, 4);
        a("identityHash", MathCompiler::identityHash, 0, 4);
        a("time", false, MathCompiler::time, 0);
        a("sqrt", MathCompiler::sqrt, 0, 4);
        a("tan", MathCompiler::tan, 0, 4);
        a("tanh", MathCompiler::tanh, 0, 4);
        a("toDegrees", MathCompiler::toDegrees, 0, 4);
        a("toRadians", MathCompiler::toRadians, 0, 4);
        a("ulp", MathCompiler::ulp, 0, 4);
        a("scalb", true, MathCompiler::scalb, 2);
        a("hypot", true, MathCompiler::hypot, 2);
        a("copySign", true, MathCompiler::copySign, 2);
        a("IEEEremainder", true, MathCompiler::IEEEremainder, 2);
        a("naturalSum", MathCompiler::naturalSum, 0, 4);
        a("reverse", MathCompiler::reverse, 0, 4);
        a("reverseBytes", MathCompiler::reverseBytes, 0, 4);
        a("eq", true, MathCompiler::eq, 4);
        a("ne", true, MathCompiler::ne, 4);
        a("gt", true, MathCompiler::gt, 4);
        a("lt", true, MathCompiler::lt, 4);
        a("ge", true, MathCompiler::ge, 4);
        a("le", true, MathCompiler::le, 4);

    }

    private static void default$registerOperators() {
        a(new Operator('^', 12, 13, Side.NONE, MathCompiler::pow));
        a(new Operator('*', 10, MathCompiler::b));
        a(new Operator('(', 10, MathCompiler::c));
        a(new Operator('/', 10, MathCompiler::d));
        a(new Operator('%', 10, MathCompiler::e));
        a(new Operator('+', 9, MathCompiler::f));
        a(new Operator('-', 9, MathCompiler::g));
        a(new Operator('~', 10, MathCompiler::h));
        a(new Operator('@', 8, MathCompiler::i));
        a(new Operator('#', 8, MathCompiler::j));
        a(new Operator('>', 8, MathCompiler::k));
        a(new Operator('<', 8, MathCompiler::l));
        a(new Operator('$', 8, MathCompiler::m));
        a(new Operator('&', 7, MathCompiler::n));
        a(new Operator('!', 6, MathCompiler::o));
        a(new Operator('|', 5, MathCompiler::p));
    }

    private static void default$registerConstants() {
        constants.put("E", Math.E);
        constants.put("PI", Math.PI);
        constants.put("Euler", 0.5772156649015329);
        constants.put("LN2", 0.693147180559945);
        constants.put("LN10", 2.302585092994046);
        constants.put("LOG2E", 1.442695040888963);
        constants.put("LOG10E", 0.434294481903252);
        constants.put("PHI", 1.618033988749895);
    }


    static {
        default$registerFunctions();
        default$registerOperators();
        default$registerConstants();
    }









    private static void a(Operator var0) {
        if (var0.getSymbol() >= MathCompiler.operators.length) {
            String var1 = "Operator handler cannot handle char '" + var0.getSymbol() + "' with char code: " + var0.getSymbol();
            throw new IllegalArgumentException(var1);
        } else {
            MathCompiler.operators[var0.getSymbol()] = var0;
        }
    }

    private static void a(String var0, boolean optimizable, QuantumFunction quantumFunction, int argCount) {
        MathCompiler.functions.put(var0, new MathFunction(quantumFunction, optimizable, argCount));
    }


    private static void a(String string, QuantumFunction quantumFunction, int n, int n2) {
        n = 1;
        a(string, true, quantumFunction, n);
    }








    @NotNull
    public static Expression compile(@Nullable String str) throws NumberFormatException, ArithmeticException {
        return str == null || str.isEmpty() ? MathCompiler.DEFAULT_VALUE : a((new MathCompiler(str, 0, 0, false, new LinkedList<>())).a()).withOriginalString(str);
    }

    private static Expression a(Expression expr) {
        if (expr instanceof BiOperation biOp) {
            Expression leftExpr = a(biOp.getLeft());
            Expression rightExpr = a(biOp.getRight());
            if (leftExpr instanceof ConstantExpr && rightExpr instanceof ConstantExpr) {
                return new ConstantExpr(biOp.getOperator().getFunction().apply(((ConstantExpr)leftExpr).getValue(), ((ConstantExpr)rightExpr).getValue()), ConstantExprType.OPTIMIZED);
            }
        } else if (expr instanceof FunctionExpr funExpr) {
            if (!funExpr.getHandler().isOptimizable()) {
                return expr;
            }

            boolean isConstantExpr = true;
            ArrayList<Expression> expressions = new ArrayList<>(funExpr.getArgs().length);
            Expression[] var4 = funExpr.getArgs();
            int var5 = 0;

            for(int var6 = var4.length; var5 < var6; ++var5) {
                Expression var7 = var4[var5];
                var7 = a(var7);
                expressions.add(var7);
                if (isConstantExpr) {
                    isConstantExpr = var7 instanceof ConstantExpr;
                }
            }

            if (isConstantExpr) {
                return new ConstantExpr(funExpr.getHandler().getFunction().apply(new FnArgs(funExpr, MathCompiler.j)), ConstantExprType.OPTIMIZED);
            }

            Collection<Expression> var10;
            return new FunctionExpr(funExpr.getName(), funExpr.getHandler(), expressions.toArray(new Expression[0]));
        }

        return expr;
    }























    //=================   operations   =====================
    private static double pow(double var0, double var2) {
        return Math.pow(var0, var2);
    }

    private static double b(double var0, double var2) {
        return var0 * var2;
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

    private static double f(double var0, double var2) {
        return Double.sum(var0, var2);
    }

    private static double g(double var0, double var2) {
        return var0 - var2;
    }

    private static double h(double var0, double var2) {
        return (double)(~((long)var2));
    }

    private static double i(double var0, double var2) {
        return (double)Long.rotateLeft((long)var0, (int)var2);
    }

    private static double j(double var0, double var2) {
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



    //==================   functions   =================

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

    private static double max(FnArgs var0) {
        Objects.requireNonNull(var0, "");
        return CollectionsKt.maxOrThrow(var0.allArgs());
    }

    private static double min(FnArgs var0) {
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

    private static double random(FnArgs var0) {
        Objects.requireNonNull(var0, "");
        return ThreadLocalRandom.current().nextDouble(var0.next(), var0.next() + 1.0);
    }

    private static double randInt(FnArgs var0) {
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

    private static double whatPercentOf(FnArgs var0) {
        Objects.requireNonNull(var0, "");
        return var0.next() / var0.next() * 100.0;
    }

    private static double percentOf(FnArgs var0) {
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

    private static double bits(FnArgs var0) {
        Objects.requireNonNull(var0, "");
        return (double)Double.doubleToRawLongBits(var0.next());
    }

    private static double hash(FnArgs var0) {
        Objects.requireNonNull(var0, "");
        return Double.hashCode(var0.next());
    }

    private static double identityHash(FnArgs var0) {
        Objects.requireNonNull(var0, "");
        return System.identityHashCode(var0.next());
    }

    private static double time(FnArgs var0) {
        Objects.requireNonNull(var0, "");
        return (double)System.currentTimeMillis();
    }

    private static double sqrt(FnArgs var0) {
        Objects.requireNonNull(var0, "");
        return Math.sqrt(var0.next());
    }

    private static double tan(FnArgs var0) {
        Objects.requireNonNull(var0, "");
        return Math.tan(var0.next());
    }

    private static double tanh(FnArgs var0) {
        Objects.requireNonNull(var0, "");
        return Math.tanh(var0.next());
    }

    private static double toDegrees(FnArgs var0) {
        Objects.requireNonNull(var0, "");
        return Math.toDegrees(var0.next());
    }

    private static double toRadians(FnArgs var0) {
        Objects.requireNonNull(var0, "");
        return Math.toRadians(var0.next());
    }

    private static double ulp(FnArgs var0) {
        Objects.requireNonNull(var0, "");
        return Math.ulp(var0.next());
    }

    private static double scalb(FnArgs var0) {
        Objects.requireNonNull(var0, "");
        return Math.scalb(var0.next(), (int)var0.next());
    }

    private static double hypot(FnArgs var0) {
        Objects.requireNonNull(var0, "");
        return Math.hypot(var0.next(), var0.next());
    }

    private static double copySign(FnArgs var0) {
        Objects.requireNonNull(var0, "");
        return Math.copySign(var0.next(), var0.next());
    }

    private static double IEEEremainder(FnArgs var0) {
        Objects.requireNonNull(var0, "");
        return Math.IEEEremainder(var0.next(), var0.next());
    }

    private static double naturalSum(FnArgs var0) {
        Objects.requireNonNull(var0, "");
        int var1;
        return (double)((var1 = (int)var0.next()) * (var1 + 1)) / 2.0;
    }

    private static double reverse(FnArgs var0) {
        Objects.requireNonNull(var0, "");
        return (double)Long.reverse((long)var0.next());
    }

    private static double reverseBytes(FnArgs var0) {
        Objects.requireNonNull(var0, "");
        return (double)Long.reverseBytes((long)var0.next());
    }

    private static double eq(FnArgs var0) {
        Objects.requireNonNull(var0, "");
        return var0.next() == var0.next() ? var0.next() : var0.next(3);
    }

    private static double ne(FnArgs var0) {
        Objects.requireNonNull(var0, "");
        return var0.next() != var0.next() ? var0.next() : var0.next(3);
    }

    private static double gt(FnArgs var0) {
        Objects.requireNonNull(var0, "");
        return var0.next() > var0.next() ? var0.next() : var0.next(3);
    }

    private static double lt(FnArgs var0) {
        Objects.requireNonNull(var0, "");
        return var0.next() < var0.next() ? var0.next() : var0.next(3);
    }

    private static double ge(FnArgs var0) {
        Objects.requireNonNull(var0, "");
        return var0.next() >= var0.next() ? var0.next() : var0.next(3);
    }

    private static double le(FnArgs var0) {
        Objects.requireNonNull(var0, "");
        return var0.next() <= var0.next() ? var0.next() : var0.next(3);
    }






    public static final class Companion {


        private Companion() {
        }





        private static boolean a(java.util.function.Function<Object, Boolean> var0, Object var1) {
            Objects.requireNonNull(var0, "");
            return var0.apply(var1);
        }

        private static boolean b(java.util.function.Function<Object, Boolean> var0, Object var1) {
            Objects.requireNonNull(var0, "");
            return var0.apply(var1);
        }







        static final class MathCompiler$Companion$1 extends Lambda<Boolean> implements java.util.function.Function<Object, Boolean> {

            private final String $$a;
            MathCompiler$Companion$1(String string) {
                super(1);
                this.$$a = string;
            }

            public Boolean a(Object o) {
                Objects.requireNonNull(o, "");
                String string = (String) o;
                String string2 = string.toLowerCase(Locale.ENGLISH);
                return (StringsKt.contains(this.$$a, string, false) || StringsKt.contains(string, this.$$a, false)) && Math.abs(string.length() - this.$$a.length()) < 2;
            }

            public Boolean apply(Object object) {
                return this.a(object);
            }

        }


        static final class MathCompiler$Companion$2 extends Lambda<Boolean> implements java.util.function.Function<String, Boolean> {

            private final String $$a;

            MathCompiler$Companion$2(String string) {
                super(1);
                this.$$a = string;
            }

            public Boolean a(String string) {
                Objects.requireNonNull(string, "");
                String string2 = string.toLowerCase(Locale.ENGLISH);
                return Intrinsics.areEqual(this.$$a, string2);
            }


            @Override
            public Boolean apply(String s) {
                return this.a(s);
            }

        }


    }


    public static Map<String, MathFunction> getFunctions() {
        return Collections.unmodifiableMap(functions);
    }

    @NotNull
    private static Map<String, MathFunction> getFunctions$core() {
        return functions;
    }

    public static Map<String, Double> getConstants() {
        return Collections.unmodifiableMap(constants);
    }

    @NotNull
    private static Map<String, Double> getConstants$core() {
        return constants;
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
            this(var1, ConstantExprType.NUMBER);
        }

        @NotNull
        public Double eval(@NotNull MathConfigStringTranslator var1) {
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
        public Expression getLeft() {
            return this.a;
        }

        @NotNull
        public Operator getOperator() {
            return this.b;
        }

        @NotNull
        public Expression getRight() {
            return this.c;
        }

        @NotNull
        public Double eval(@NotNull MathConfigStringTranslator var1) {
            Objects.requireNonNull(var1, "");
            return this.b.getFunction().apply(this.a.eval(var1), this.c.eval(var1));
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


    public abstract static class Expression implements MathExpression {

        @Nullable
        private String originalString;
        public Expression() {
        }

        @Nullable
        public final String getOriginalString$core() {
            return this.originalString;
        }


        public final void setOriginalString$core(@Nullable String var1) {
            this.originalString = var1;
        }

        @NotNull
        public abstract Double eval(@NotNull MathConfigStringTranslator var1);

        public boolean isDefault() {
            return this == MathCompiler.DEFAULT_VALUE;
        }

        @Nullable
        public String getOriginalString() {
            return this.originalString;
        }

        @NotNull
        public final Expression withOriginalString(@NotNull String var1) {
            Objects.requireNonNull(var1, "");
            this.originalString = var1;
            return this;
        }

        public final <T extends Expression> boolean contains(@NotNull Class<T> var1, @NotNull Predicate<T> var2) {
            Objects.requireNonNull(var1, "");
            Objects.requireNonNull(var2, "");
            if (var1.isInstance(this)) {
                return var2.test((T) this);

            }

            if (this instanceof BiOperation) {
                return ((BiOperation)this).getLeft().contains(var1, var2) || ((BiOperation)this).getRight().contains(var1, var2);
            } else if (this instanceof FunctionExpr) {
                Expression[] var3 = ((FunctionExpr)this).getArgs();
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
        private final MathConfigStringTranslator b;
        private int c;

        public FnArgs(@NotNull FunctionExpr var1, @NotNull MathConfigStringTranslator var2) {
            Objects.requireNonNull(var1, "");
            Objects.requireNonNull(var2, "");

            this.a = var1;
            this.b = var2;
        }

        public double next() {
            Expression[] var10000 = this.a.getArgs();
            int var1 = this.c++;
            return var10000[var1].eval(this.b);
        }

        @NotNull
        public List<Double> allArgs() {
            Expression[] var1;
            Expression[] var2 = var1 = this.a.getArgs();
            List<Double> var7 = new ArrayList<>(var1.length);
            int var3 = 0;

            for(int var4 = var2.length; var3 < var4; ++var3) {
                Expression var5 = var2[var3];
                var7.add(var5.eval(this.b));
            }

            return var7;
        }

        public double next(int d) {
            Expression[] expressions = this.a.getArgs();
            this.c = d;
            return expressions[d].eval(this.b);
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



    public static final class MathFunction {
        @NotNull
        private final QuantumFunction a;
        private final boolean b;
        private final int c;

        public MathFunction(@NotNull QuantumFunction function, boolean optimizable, int argCount) {
            Objects.requireNonNull(function, "");
            this.a = function;
            this.b = optimizable;
            this.c = argCount;
        }

        @NotNull
        public QuantumFunction getFunction() {
            return this.a;
        }


        public boolean isOptimizable() {
            return this.b;
        }

        public int getArgCount() {
            return this.c;
        }
    }


    public static final class FunctionExpr extends Expression {
        @NotNull
        private final String name;
        @NotNull
        private final MathFunction handler;
        @NotNull
        private final Expression[] args;

        public FunctionExpr(@NotNull String name, @NotNull MathFunction handler, @NotNull Expression[] args) {
            Objects.requireNonNull(name, "");
            Objects.requireNonNull(handler, "");
            Objects.requireNonNull(args, "");
            this.name = name;
            this.handler = handler;
            this.args = args;
        }

        @NotNull
        public String getName() {
            return this.name;
        }

        @NotNull
        public MathFunction getHandler() {
            return this.handler;
        }

        @NotNull
        public Expression[] getArgs() {
            return this.args;
        }

        @NotNull
        public Double eval(@NotNull MathConfigStringTranslator var1) {
            Objects.requireNonNull(var1, "");
            return this.handler.getFunction().apply(new FnArgs(this, var1));
        }

        @NotNull
        public String asString(boolean var1) {
            return this.name + '(' + ArraysKt.joinToString(this.args, ", ", "", "", 0, "...", null) + ')';
        }

        @NotNull
        public String toString() {
            String[] var1;
            Intrinsics.checkNotNullExpressionValue(var1 = Arrays.stream(this.args).map(Object::toString).toArray(FunctionExpr::a), "");
            return this.name + '(' + (this.args.length == 0 ? "" : ArraysKt.joinToString(var1, ", ", "", "", 0, "...", null)) + ')';
        }

        private static String a(java.util.function.Function<Object, String> var0, Object var1) {
            Objects.requireNonNull(var0, "");
            return var0.apply(var1);
        }

        private static String[] a(int var0) {
            return new String[var0];
        }
    }


    public static final class LexicalEnvironment {
        private final int a;
        private final MathCompiler.@Nullable MathFunction b;

        public LexicalEnvironment(int var1, MathCompiler.@Nullable MathFunction var2) {
            this.a = var1;
            this.b = var2;
        }


        public int getIndex() {
            return this.a;
        }

        public MathCompiler.@Nullable MathFunction getFunction() {
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
        private final char symbol;
        private final byte b;
        private final byte c;
        @NotNull
        private final Side side;
        @NotNull
        private final Arity arity;
        @NotNull
        private final TriDoubleFn function;

        public Operator(char var1, int var2, int var3, @NotNull Side var4, @NotNull TriDoubleFn var5) {
            Objects.requireNonNull(var4, "");
            Objects.requireNonNull(var5, "");
            this.symbol = var1;
            this.b = (byte)var2;
            this.c = (byte)var3;
            this.side = var4;
            this.function = var5;
            this.arity = (var1) == '-' ? Arity.UNARY_AND_BINARY : (var1 == '~' ? Arity.UNARY : Arity.BINARY);
        }


        public Operator(char var1, int var2, @NotNull TriDoubleFn var3) {
            this(var1, var2, var2, Side.NONE, var3);
        }

        public char getSymbol() {
            return this.symbol;
        }

        @NotNull
        public Arity getArity() {
            return this.arity;
        }

        @NotNull
        public TriDoubleFn getFunction() {
            return this.function;
        }

        @NotNull
        public Side getSide() {
            return this.side;
        }

        @NotNull
        public String toString() {
            return "MathOperator['" + this.symbol + "']";
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

    }


    public static final class StringConstant extends ConstantExpr {
        @NotNull
        private final String a;

        public StringConstant(@NotNull String var1) {
            super(var1.hashCode(), ConstantExprType.STRING);
            this.a = var1;
        }

        @NotNull
        public String getString$core() {
            return this.a;
        }

        @NotNull
        public Double eval(@NotNull MathConfigStringTranslator var1) {
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



    private static final class MathVariable extends Expression {
        @NotNull
        private final String a;

        public MathVariable(@NotNull String var1) {
            this.a = var1;
        }

        @NotNull
        public Double eval(@NotNull MathConfigStringTranslator var1) {
            Objects.requireNonNull(var1, "");
            Double var3;
            if ((var3 = var1.apply(this.a)) == null) {
                String var4 = MathCompiler.access$findFunction(this.a);
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

        private final java.util.function.Function<LexicalEnvironment, Boolean> a;

        b(@NotNull java.util.function.Function<LexicalEnvironment, Boolean> var1) {
            this.a = var1;
        }

        @Override
        public boolean test(LexicalEnvironment o) {
            return this.a.apply(o);
        }
    }


}
