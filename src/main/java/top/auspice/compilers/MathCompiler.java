package top.auspice.compilers;


import kotlin.collections.CollectionsKt;
import kotlin.enums.EnumEntries;
import kotlin.jvm.JvmField;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.ranges.IntRange;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import top.auspice.compilers.expressions.MathExpression;
import top.auspice.compilers.translators.MathematicalVariableTranslator;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class MathCompiler {
    @NotNull
    public static final Companion Companion = new Companion();
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
    private static final Map<String, Double> g = (Map)(new HashMap(8));
    @NotNull
    private static final Map<String, Function> h = (Map)(new HashMap(44));
    @NotNull
    private static final Operator[] i = new Operator[127];
    @NotNull
    private static final MathematicalVariableTranslator j = MathCompiler::b;
    @JvmField
    @NotNull
    public static final Expression DEFAULT_VALUE;

    private MathCompiler(String var1, int var2, int var3, boolean var4, LinkedList<LexicalEnvironment> var5) {
        this.a = var1;
        this.b = var2;
        this.c = var3;
        this.d = var4;
        this.e = var5;
        this.f = new LinkedList();
    }

    private final int a(int var1, boolean var2) {
        int var3 = var1;
        char var4 = this.a.charAt(var1);
        if (!var2) {
            while(('a' <= var4 ? var4 < '{' : false) || ('A' <= var4 ? var4 < '[' : false) || ('0' <= var4 ? var4 < ':' : false) || var4 == '_') {
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
                    throw a(this, var1 - 1, "Unclosed variable interpolation", (Collection)null, 4);
                }

                var4 = this.a.charAt(var3);
            }
        }

        return var3;
    }

    private final Expression a() throws NumberFormatException, MathEvaluateException {
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
                    if ('0' <= var1 ? var1 < ':' : false) {
                        var2 = this.b;
                        var18 = this;
                        var4 = this.b;

                        for(var21 = this.a.charAt(var4); ('0' <= var21 ? var21 < ':' : false) || var21 == 'x' || var21 == 'e' || var21 == 'E' || var21 == '-' || var21 == '.'; var21 = var18.a.charAt(var4)) {
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
                    } else if (('a' <= var1 ? var1 < '{' : false) ? true : ('A' <= var1 ? var1 < '[' : false)) {
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
                            return (Expression)(new StringConstant(var10002));
                        }

                        LexicalEnvironment var38;
                        if (var1 == ',' ? true : var1 == ';') {
                            var38 = (LexicalEnvironment)this.e.peekLast();
                            if (var38 == null) {
                                throw a(this, this.b, "Function argument separator outside of functions", (Collection)null, 4);
                            }

                            LexicalEnvironment var22 = var38;
                            if (var38.getFunction() == null) {
                                Stream var39 = this.e.stream();
                                Function1 var30 = (Function1)null.INSTANCE;
                                if (var39.anyMatch((Predicate)(new Predicate(var30) {
                                    {
                                        Intrinsics.checkNotNullParameter(var1, "");
                                        this.a = var1;
                                    }
                                }))) {
                                    throw a(this, var22.getIndex(), "Unclosed parentheses", (Collection)null, 4);
                                }

                                throw a(this, this.b, "Function argument separator outside of functions", (Collection)null, 4);
                            }

                            return this.b();
                        }

                        if (var1 == '[') {
                            var4 = this.b;
                            if ((var5 = StringsKt.indexOf$default((CharSequence)this.a, ']', var4 + 1, false, 4, (Object)null)) == -1) {
                                throw a(this, var4, "Cannot find time literal closing bracket.", (Collection)null, 4);
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
                                throw this.a(this.b, "Unknown time format", (Collection) CollectionsKt.toMutableList((Iterable)(new IntRange(var4, var5))));
                            }

                            long var14 = var35;
                            var33 = (Expression)(new ConstantExpr((double)var28 * (double)var14, MathCompiler.ConstantExprType.TIME));
                        } else if (var1 == '(') {
                            LinkedList var36 = this.e;
                            var2 = this.b++;
                            var36.add(new LexicalEnvironment(var2, (Function)null));
                            MathCompiler var19;
                            var27 = (var19 = new MathCompiler(this.a, this.b, this.c, true, this.e)).a();
                            this.b = var19.b;
                            var33 = var27;
                        } else {
                            if (var1 == ')') {
                                var38 = (LexicalEnvironment)this.e.pollLast();
                                if (var38 == null) {
                                    throw a(this, this.b, "No opening parentheses found for closing parenthes", (Collection)null, 4);
                                }

                                if (var38.getFunction() == null && this.f.isEmpty()) {
                                    throw a(this, this.b, "Empty subexpression", (Collection)null, 4);
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
                            throw a(this, this.b, "Unrecognized character '" + var1 + "' (" + var1 + ") outside of variable/placeholder interpolation", (Collection)null, 4);
                        }

                        Operator var31 = var37;
                        var26 = this.f.peekLast();
                        if (!var31.getArity$core().isUnary$core() && var26 instanceof Operator) {
                            throw a(this, this.b, "Blank operand on the left hand side of binary operator", (Collection)null, 4);
                        }

                        this.f.addLast(var31);
                    } else {
                        var18 = this;
                        if (this.f.isEmpty()) {
                            this.f.add(var20);
                        } else {
                            var27 = var20;
                            if (!((var26 = this.f.getLast()) instanceof Operator)) {
                                throw a(this, this.b, "Expected an operator before operand", (Collection)null, 4);
                            }

                            label196:
                            for(; var26 != null && var26 instanceof Operator; var26 = var18.f.peekLast()) {
                                switch (MathCompiler.WhenMappings.$EnumSwitchMapping$0[((Operator)var26).getArity$core().ordinal()]) {
                                    case 1:
                                        break label196;
                                    case 2:
                                        var27 = (Expression)(new BiOperation(DEFAULT_VALUE, (Operator)var26, var27));
                                        var18.f.removeLast();
                                        break;
                                    case 3:
                                        var18.f.removeLast();
                                        Object var29;
                                        if ((var29 = var18.f.peekLast()) != null && !(var29 instanceof Operator)) {
                                            var18.f.add(var26);
                                            break label196;
                                        }

                                        var27 = (Expression)(new BiOperation(DEFAULT_VALUE, (Operator)var26, var27));
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

    private final Expression b() {
        if ((this.b >= this.c || !this.d) && !this.e.isEmpty()) {
            final ArrayList var11 = new ArrayList();
            final Ref.BooleanRef var9 = new Ref.BooleanRef();

            @Metadata(
                    mv = {1, 9, 0},
                    k = 3,
                    xi = 48,
                    d1 = {"\u0000\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0010\u0003\u001a\u00020\u00022\n\u0010\u0001\u001a\u0006*\u00020\u00000\u0000H\n¢\u0006\u0004\b\u0003\u0010\u0004"},
                    d2 = {"Lorg/kingdoms/utils/compilers/MathCompiler$LexicalEnvironment;", "p0", "", "a", "(Lorg/kingdoms/utils/compilers/MathCompiler$LexicalEnvironment;)V"}
            )
            @SourceDebugExtension({"SMAP\nMathCompiler.kt\nKotlin\n*S Kotlin\n*F\n+ 1 MathCompiler.kt\norg/kingdoms/utils/compilers/MathCompiler$build$1\n+ 2 MathCompiler.kt\norg/kingdoms/utils/compilers/MathCompiler$LexicalEnvironment\n*L\n1#1,848:1\n58#2:849\n*S KotlinDebug\n*F\n+ 1 MathCompiler.kt\norg/kingdoms/utils/compilers/MathCompiler$build$1\n*L\n199#1:849\n*E\n"})
            final class NamelessClass_1 extends Lambda implements Function1<LexicalEnvironment, Unit> {
                NamelessClass_1() {
                    super(1);
                }

                public final void a(LexicalEnvironment var1) {
                    var11.add(var1.getIndex());
                    if (!var9.element) {
                        var9.element = var1.getFunction() != null;
                    }

                }
            }

            this.e.stream().forEach(MathCompiler::a);
            throw this.a(((LexicalEnvironment)this.e.getLast()).getIndex(), "Unclosed parentheses" + (var9.element ? " and functions" : ""), (Collection)var11);
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
                    throw a(this, this.c - 1, "Blank operand on right hand side of " + ((Operator)var2).getSymbol() + var10, (Collection)null, 4);
                } else {
                    BiOperation var1 = null;
                    ListIterator var10000 = this.f.listIterator();
                    Intrinsics.checkNotNullExpressionValue(var10000, "");

                    BiOperation var13;
                    for(ListIterator var8 = var10000; var8.hasNext(); var1 = var13) {
                        var12 = var8.next();
                        Intrinsics.checkNotNull(var12);
                        Expression var7 = (Expression)var12;
                        var12 = var8.next();
                        Intrinsics.checkNotNull(var12);
                        Operator var3 = (Operator)var12;
                        var12 = var8.next();
                        Intrinsics.checkNotNull(var12);
                        Expression var4 = (Expression)var12;
                        if (!var8.hasNext()) {
                            var13 = new BiOperation(var7, var3, var4);
                        } else {
                            var12 = var8.next();
                            Intrinsics.checkNotNull(var12);
                            Operator var5 = (Operator)var12;
                            var12 = var8.next();
                            Intrinsics.checkNotNull(var12);
                            Expression var6 = (Expression)var12;
                            if (var3.hasPrecedenceOver$core(var5)) {
                                var1 = new BiOperation(var7, var3, var4);
                                var8.previous();
                                var8.previous();
                                var8.previous();
                                var8.remove();
                                var8.previous();
                                var8.remove();
                                var8.previous();
                                var8.set(var1);
                                var13 = var1;
                            } else {
                                var1 = new BiOperation(var4, var5, var6);
                                var8.remove();
                                var8.previous();
                                var8.remove();
                                var8.previous();
                                var8.set(var1);
                                var8.previous();
                                var8.previous();
                                var13 = var1;
                            }
                        }
                    }

                    Intrinsics.checkNotNull(var1);
                    return (Expression)var1;
                }
            }
        }
    }

    private final Expression a(boolean var1) {
        int var2 = this.a(this.b, var1);
        String var10000 = this.a.substring(this.b, var1 ? var2 - 1 : var2);
        Intrinsics.checkNotNullExpressionValue(var10000, "");
        String var7 = var10000;
        if (Intrinsics.areEqual(var10000, "_")) {
            throw a(this, this.b, "Reserved single underscore identifier", (Collection)null, 4);
        } else {
            this.b = var2;
            MathCompiler var3 = this;
            int var4 = this.b;
            int var5 = this.c;

            for(var4 = var4; var4 < var5 && var3.a.charAt(var4) == ' '; ++var4) {
            }

            this.b = var4;
            if (this.b < this.c) {
                var4 = this.b;
                if (this.a.charAt(var4) == '(') {
                    return (Expression)this.a(var7);
                }
            }

            this.b = var2 - 1;
            Double var8;
            return (var8 = (Double)g.get(var7)) == null ? (Expression)(new a(var7)) : (Expression)(new ConstantExpr(var8, MathCompiler.ConstantExprType.CONSTANT_VARIABLE));
        }
    }

    private final FunctionExpr a(String var1) {
        String var2 = (var2 = MathCompiler.Companion.access$findFunction(Companion, var1)) == null ? "" : "; Did you mean '" + var2 + "' function?";
        Function var10000 = (Function)h.get(var1);
        if (var10000 == null) {
            throw a(this, this.b, "Unknown function: " + var1 + var2, (Collection)null, 4);
        } else {
            Function var8 = var10000;
            int var3 = this.b++;
            ArrayList var9 = new ArrayList();
            LexicalEnvironment var4 = new LexicalEnvironment(this.b, var8);
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
            if (var8.getArgCount() < 0) {
                var10 = Math.abs(var8.getArgCount()) - 1;
                if (var9.size() < var10) {
                    throw this.a(var5, "Too few arguments for function '" + var1 + "', expected at least: " + var10 + ", got: " + var9.size(), MathCompiler.Companion.access$pointerToName(Companion, var5, var1));
                }
            } else {
                if (var9.size() < var8.getArgCount()) {
                    throw this.a(var5, "Too few arguments for function '" + var1 + "', expected: " + var8.getArgCount() + ", got: " + var9.size(), MathCompiler.Companion.access$pointerToName(Companion, var5, var1));
                }

                if (var9.size() > var8.getArgCount()) {
                    throw this.a(var5, "Too many arguments for function '" + var1 + "', expected: " + var8.getArgCount() + ", got: " + var9.size(), MathCompiler.Companion.access$pointerToName(Companion, var5, var1));
                }
            }

            Collection var11;
            return new FunctionExpr(var1, var8, (Expression[])(var11 = (Collection)var9).toArray(new Expression[0]));
        }
    }

    private final MathEvaluateException a(int var1, String var2, Collection<Integer> var3) {
        var2 = "\n" + var2 + " at offset " + var1 + " in expression: \n\"" + this.a + '"';
        int var4 = 0;
        var3.add(var1);
        Iterator var6 = var3.iterator();

        while(var6.hasNext()) {
            int var5;
            if ((var5 = ((Number)var6.next()).intValue()) > var4) {
                var4 = var5;
            }
        }

        char[] var7;
        Arrays.fill(var7 = new char[var1 = var4 + 2], ' ');
        StringBuilder var8 = new StringBuilder(new String(var7));
        var3.forEach(MathCompiler::a);
        return new MathEvaluateException(var2 + '\n' + var8);
    }

    private static final void a(Function1 var0, Object var1) {
        Intrinsics.checkNotNullParameter(var0, "");
        var0.invoke(var1);
    }

    private static final void a(StringBuilder var0, int var1) {
        Intrinsics.checkNotNullParameter(var0, "");
        var0.setCharAt(var1 + 1, '^');
    }

    private static final Double b(String var0) {
        Intrinsics.checkNotNullParameter(var0, "");
        throw new IllegalAccessError();
    }

    @NotNull
    public static final Map<String, Double> getConstants() {
        return Companion.getConstants();
    }

    @NotNull
    public static final Map<String, Function> getFunctions() {
        return Companion.getFunctions();
    }

    @NotNull
    public static final Expression compile(@Nullable String var0) throws NumberFormatException, ArithmeticException {
        return Companion.compile(var0);
    }

    static {
        DEFAULT_VALUE = (Expression)(new ConstantExpr(0.0, MathCompiler.ConstantExprType.NUMBER));
        MathCompiler.Companion.access$registerOperators(Companion);
        MathCompiler.Companion.access$registerFunctions(Companion);
        MathCompiler.Companion.access$registerConstants(Companion);
    }


    public enum Arity {
        UNARY,
        BINARY,
        UNARY_AND_BINARY;

        private Arity() {
        }

        public final boolean isUnary$core() {
            return this == UNARY || this == UNARY_AND_BINARY;
        }

        @NotNull
        public static EnumEntries<Arity> getEntries() {
            return $ENTRIES;
        }

        static {
            Arity[] var0;
            (var0 = new Arity[3])[0] = UNARY;
            var0[1] = BINARY;
            var0[2] = UNARY_AND_BINARY;
            $ENTRIES = EnumEntriesKt.enumEntries((Enum[])var0);
        }
    }

    @Metadata(
            mv = {1, 9, 0},
            k = 1,
            xi = 48,
            d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0006\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\f\b\u0000\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0003\u001a\u00020\u0001\u0012\u0006\u0010\u001b\u001a\u00020\u0012\u0012\u0006\u0010\u001c\u001a\u00020\u0001¢\u0006\u0004\b\u001d\u0010\u001eJ\u0017\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0017¢\u0006\u0004\b\u0005\u0010\u0006J\u0017\u0010\t\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u0007H\u0017¢\u0006\u0004\b\t\u0010\nJ\u000f\u0010\u000b\u001a\u00020\u0004H\u0017¢\u0006\u0004\b\u000b\u0010\fR\u001a\u0010\u0011\u001a\u00020\u00018\u0001X\u0081\u0004¢\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0017\u001a\u00020\u00128\u0001X\u0081\u0004¢\u0006\f\n\u0004\b\u0013\u0010\u0014\u001a\u0004\b\u0015\u0010\u0016R\u001a\u0010\u001a\u001a\u00020\u00018\u0001X\u0081\u0004¢\u0006\f\n\u0004\b\u0018\u0010\u000e\u001a\u0004\b\u0019\u0010\u0010"},
            d2 = {"Lorg/kingdoms/utils/compilers/MathCompiler$BiOperation;", "Lorg/kingdoms/utils/compilers/MathCompiler$Expression;", "", "p0", "", "asString", "(Z)Ljava/lang/String;", "Lorg/kingdoms/utils/compilers/translators/MathematicalVariableTranslator;", "", "eval", "(Lorg/kingdoms/utils/compilers/translators/MathematicalVariableTranslator;)Ljava/lang/Double;", "toString", "()Ljava/lang/String;", "a", "Lorg/kingdoms/utils/compilers/MathCompiler$Expression;", "getLeft$core", "()Lorg/kingdoms/utils/compilers/MathCompiler$Expression;", "left", "Lorg/kingdoms/utils/compilers/MathCompiler$Operator;", "b", "Lorg/kingdoms/utils/compilers/MathCompiler$Operator;", "getOp$core", "()Lorg/kingdoms/utils/compilers/MathCompiler$Operator;", "op", "c", "getRight$core", "right", "p1", "p2", "<init>", "(Lorg/kingdoms/utils/compilers/MathCompiler$Expression;Lorg/kingdoms/utils/compilers/MathCompiler$Operator;Lorg/kingdoms/utils/compilers/MathCompiler$Expression;)V"}
    )
    public static final class BiOperation extends Expression {
        @NotNull
        private final Expression a;
        @NotNull
        private final Operator b;
        @NotNull
        private final Expression c;

        public BiOperation(@NotNull Expression var1, @NotNull Operator var2, @NotNull Expression var3) {
            Intrinsics.checkNotNullParameter(var1, "");
            Intrinsics.checkNotNullParameter(var2, "");
            Intrinsics.checkNotNullParameter(var3, "");
            super();
            this.a = var1;
            this.b = var2;
            this.c = var3;
        }

        @NotNull
        @JvmName(
                name = "getLeft$core"
        )
        public final Expression getLeft$core() {
            return this.a;
        }

        @NotNull
        @JvmName(
                name = "getOp$core"
        )
        public final Operator getOp$core() {
            return this.b;
        }

        @NotNull
        @JvmName(
                name = "getRight$core"
        )
        public final Expression getRight$core() {
            return this.c;
        }

        @NotNull
        public final Double eval(@NotNull MathematicalVariableTranslator var1) {
            Intrinsics.checkNotNullParameter(var1, "");
            return this.b.getFunction$core().apply(this.a.eval(var1), this.c.eval(var1));
        }

        @NotNull
        public final String asString(boolean var1) {
            return this.a.asString(var1) + ' ' + this.b.getSymbol() + ' ' + this.c.asString(var1);
        }

        @NotNull
        public final String toString() {
            return "(" + this.a + ' ' + this.b.getSymbol() + (this.b.getSymbol() == '(' ? "" : ' ') + this.c + ')';
        }
    }

    @Metadata(
            mv = {1, 9, 0},
            k = 1,
            xi = 48,
            d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0011\n\u0002\b\u0005\b\u0087\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b'\u0010(J\u0019\u0010\u0005\u001a\u00020\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002H\u0007¢\u0006\u0004\b\u0005\u0010\u0006J/\u0010\u000e\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\u000bH\u0002¢\u0006\u0004\b\u000e\u0010\u000fJ\u001b\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00110\u0010H\u0007¢\u0006\u0004\b\u0012\u0010\u0013J\u001b\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00140\u0010H\u0007¢\u0006\u0004\b\u0015\u0010\u0013J\u0017\u0010\u000e\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\u0016H\u0002¢\u0006\u0004\b\u000e\u0010\u0017J\u0017\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004H\u0002¢\u0006\u0004\b\u000e\u0010\u0018R \u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00110\u00108\u0002X\u0083\u0004¢\u0006\u0006\n\u0004\b\u0019\u0010\u001aR\u0014\u0010\u001b\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\b\u001b\u0010\u001cR\u0014\u0010 \u001a\u00020\u001d8\u0002X\u0083\u0004¢\u0006\u0006\n\u0004\b\u001e\u0010\u001fR \u0010\"\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00140\u00108\u0002X\u0083\u0004¢\u0006\u0006\n\u0004\b!\u0010\u001aR\u001c\u0010&\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00160#8\u0002X\u0083\u0004¢\u0006\u0006\n\u0004\b$\u0010%"},
            d2 = {"Lorg/kingdoms/utils/compilers/MathCompiler$Companion;", "", "", "p0", "Lorg/kingdoms/utils/compilers/MathCompiler$Expression;", "compile", "(Ljava/lang/String;)Lorg/kingdoms/utils/compilers/MathCompiler$Expression;", "", "p1", "Lorg/kingdoms/utils/compilers/MathCompiler$QuantumFunction;", "p2", "", "p3", "", "a", "(Ljava/lang/String;ZLorg/kingdoms/utils/compilers/MathCompiler$QuantumFunction;I)V", "", "", "getConstants", "()Ljava/util/Map;", "Lorg/kingdoms/utils/compilers/MathCompiler$Function;", "getFunctions", "Lorg/kingdoms/utils/compilers/MathCompiler$Operator;", "(Lorg/kingdoms/utils/compilers/MathCompiler$Operator;)V", "(Lorg/kingdoms/utils/compilers/MathCompiler$Expression;)Lorg/kingdoms/utils/compilers/MathCompiler$Expression;", "g", "Ljava/util/Map;", "DEFAULT_VALUE", "Lorg/kingdoms/utils/compilers/MathCompiler$Expression;", "Lorg/kingdoms/utils/compilers/translators/MathematicalVariableTranslator;", "j", "Lorg/kingdoms/utils/compilers/translators/MathematicalVariableTranslator;", "b", "h", "c", "", "i", "[Lorg/kingdoms/utils/compilers/MathCompiler$Operator;", "d", "<init>", "()V"}
    )
    @SourceDebugExtension({"SMAP\nMathCompiler.kt\nKotlin\n*S Kotlin\n*F\n+ 1 MathCompiler.kt\norg/kingdoms/utils/compilers/MathCompiler$Companion\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 ArraysJVM.kt\nkotlin/collections/ArraysKt__ArraysJVMKt\n*L\n1#1,848:1\n1#2:849\n37#3,2:850\n*S KotlinDebug\n*F\n+ 1 MathCompiler.kt\norg/kingdoms/utils/compilers/MathCompiler$Companion\n*L\n811#1:850,2\n*E\n"})
    public static final class Companion {
        private Companion() {
        }

        @JvmStatic
        @NotNull
        public final Map<String, Double> getConstants() {
            return MathCompiler.g;
        }

        @JvmStatic
        @NotNull
        public final Map<String, Function> getFunctions() {
            return MathCompiler.h;
        }

        private static void a(Operator var0) {
            if (var0.getSymbol() >= MathCompiler.i.length) {
                String var1 = "Operator handler cannot handle char '" + var0.getSymbol() + "' with char code: " + var0.getSymbol();
                throw new IllegalArgumentException(var1.toString());
            } else {
                MathCompiler.i[var0.getSymbol()] = var0;
            }
        }

        private static void a(String var0, boolean var1, QuantumFunction var2, int var3) {
            MathCompiler.h.put(var0, new Function(var2, var1, var3));
        }

        @JvmStatic
        @NotNull
        public final Expression compile(@Nullable String var1) throws NumberFormatException, ArithmeticException {
            CharSequence var2;
            return (var2 = (CharSequence)var1) == null || var2.length() == 0 ? MathCompiler.DEFAULT_VALUE : this.a((new MathCompiler(var1, 0, 0, false, (LinkedList)null, 30, (DefaultConstructorMarker)null)).a()).withOriginalString(var1);
        }

        private final Expression a(Expression var1) {
            if (var1 instanceof BiOperation) {
                Expression var2 = this.a(((BiOperation)var1).getLeft$core());
                Expression var3 = this.a(((BiOperation)var1).getRight$core());
                if (var2 instanceof ConstantExpr && var3 instanceof ConstantExpr) {
                    return (Expression)(new ConstantExpr(((BiOperation)var1).getOp$core().getFunction$core().apply(((ConstantExpr)var2).getValue(), ((ConstantExpr)var3).getValue()), MathCompiler.ConstantExprType.OPTIMIZED));
                }
            } else if (var1 instanceof FunctionExpr) {
                if (!((FunctionExpr)var1).getHandler$core().getOptimizable$core()) {
                    return var1;
                }

                boolean var8 = true;
                ArrayList var9 = new ArrayList(((FunctionExpr)var1).getArgs$core().length);
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
                    return (Expression)(new ConstantExpr(((FunctionExpr)var1).getHandler$core().getFunction$core().apply(new FnArgs((FunctionExpr)var1, MathCompiler.j)), MathCompiler.ConstantExprType.OPTIMIZED));
                }

                Collection var10;
                return (Expression)(new FunctionExpr(((FunctionExpr)var1).getName$core(), ((FunctionExpr)var1).getHandler$core(), (Expression[])(var10 = (Collection)var9).toArray(new Expression[0])));
            }

            return var1;
        }

        private static final double a(double var0, double var2) {
            return Math.pow(var0, var2);
        }

        private static final double b(double var0, double var2) {
            return var0 * var2;
        }

        private static final double c(double var0, double var2) {
            return var0 * var2;
        }

        private static final double d(double var0, double var2) {
            return var0 / var2;
        }

        private static final double e(double var0, double var2) {
            return var0 % var2;
        }

        private static final double f(double var0, double var2) {
            return Double.sum(var0, var2);
        }

        private static final double g(double var0, double var2) {
            return var0 - var2;
        }

        private static final double h(double var0, double var2) {
            return (double)(~((long)var2));
        }

        private static final double i(double var0, double var2) {
            return (double)Long.rotateLeft((long)var0, (int)var2);
        }

        private static final double j(double var0, double var2) {
            return (double)Long.rotateRight((long)var0, (int)var2);
        }

        private static final double k(double var0, double var2) {
            return (double)((long)var0 >> (int)((long)var2));
        }

        private static final double l(double var0, double var2) {
            return (double)((long)var0 << (int)((long)var2));
        }

        private static final double m(double var0, double var2) {
            return (double)((long)var0 >>> (int)((long)var2));
        }

        private static final double n(double var0, double var2) {
            return (double)((long)var0 & (long)var2);
        }

        private static final double o(double var0, double var2) {
            return (double)((long)var0 ^ (long)var2);
        }

        private static final double p(double var0, double var2) {
            return (double)((long)var0 | (long)var2);
        }

        private static final double a(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return Math.abs(var0.next());
        }

        private static final double b(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return Math.acos(var0.next());
        }

        private static final double c(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return Math.asin(var0.next());
        }

        private static final double d(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return Math.atan(var0.next());
        }

        private static final double e(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return Math.cbrt(var0.next());
        }

        private static final double f(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return Math.ceil(var0.next());
        }

        private static final double g(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return Math.cos(var0.next());
        }

        private static final double h(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return Math.cosh(var0.next());
        }

        private static final double i(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return Math.exp(var0.next());
        }

        private static final double j(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return Math.expm1(var0.next());
        }

        private static final double k(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return Math.floor(var0.next());
        }

        private static final double l(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return (double)Math.getExponent(var0.next());
        }

        private static final double m(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return Math.log(var0.next());
        }

        private static final double n(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return Math.log10(var0.next());
        }

        private static final double o(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return Math.log1p(var0.next());
        }

        private static final double p(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return CollectionsKt.maxOrThrow((Iterable)var0.allArgs());
        }

        private static final double q(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return CollectionsKt.minOrThrow((Iterable)var0.allArgs());
        }

        private static final double r(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return Math.nextUp(var0.next());
        }

        private static final double s(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return Math.nextDown(var0.next());
        }

        private static final double t(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return Math.nextAfter(var0.next(), var0.next());
        }

        private static final double u(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return ThreadLocalRandom.current().nextDouble(var0.next(), var0.next() + 1.0);
        }

        private static final double v(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return (double)ThreadLocalRandom.current().nextInt((int)var0.next(), (int)var0.next() + 1);
        }

        private static final double w(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return (double)Math.round(var0.next());
        }

        private static final double x(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return Math.rint(var0.next());
        }

        private static final double y(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return Math.signum(var0.next());
        }

        private static final double z(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return var0.next() / var0.next() * 100.0;
        }

        private static final double A(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return var0.next() / 100.0 * var0.next();
        }

        private static final double B(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return Math.sin(var0.next());
        }

        private static final double C(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return Math.sinh(var0.next());
        }

        private static final double D(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return (double)Double.doubleToRawLongBits(var0.next());
        }

        private static final double E(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return (double)Double.hashCode(var0.next());
        }

        private static final double F(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return (double)System.identityHashCode(var0.next());
        }

        private static final double G(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return (double)System.currentTimeMillis();
        }

        private static final double H(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return Math.sqrt(var0.next());
        }

        private static final double I(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return Math.tan(var0.next());
        }

        private static final double J(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return Math.tanh(var0.next());
        }

        private static final double K(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return Math.toDegrees(var0.next());
        }

        private static final double L(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return Math.toRadians(var0.next());
        }

        private static final double M(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return Math.ulp(var0.next());
        }

        private static final double N(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return Math.scalb(var0.next(), (int)var0.next());
        }

        private static final double O(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return Math.hypot(var0.next(), var0.next());
        }

        private static final double P(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return Math.copySign(var0.next(), var0.next());
        }

        private static final double Q(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return Math.IEEEremainder(var0.next(), var0.next());
        }

        private static final double R(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            int var1;
            return (double)((var1 = (int)var0.next()) * (var1 + 1)) / 2.0;
        }

        private static final double S(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return (double)Long.reverse((long)var0.next());
        }

        private static final double T(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return (double)Long.reverseBytes((long)var0.next());
        }

        private static final double U(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return var0.next() == var0.next() ? var0.next() : var0.next(3);
        }

        private static final double V(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return var0.next() != var0.next() ? var0.next() : var0.next(3);
        }

        private static final double W(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return var0.next() > var0.next() ? var0.next() : var0.next(3);
        }

        private static final double X(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return var0.next() < var0.next() ? var0.next() : var0.next(3);
        }

        private static final double Y(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return var0.next() >= var0.next() ? var0.next() : var0.next(3);
        }

        private static final double Z(FnArgs var0) {
            Intrinsics.checkNotNullParameter(var0, "");
            return var0.next() <= var0.next() ? var0.next() : var0.next(3);
        }

        private static final boolean a(Function1 var0, Object var1) {
            Intrinsics.checkNotNullParameter(var0, "");
            return (Boolean)var0.invoke(var1);
        }

        private static final boolean b(Function1 var0, Object var1) {
            Intrinsics.checkNotNullParameter(var0, "");
            return (Boolean)var0.invoke(var1);
        }

        private static final String a(final String var0) {
            Intrinsics.checkNotNullParameter(var0, "");

            @Metadata(
                    mv = {1, 9, 0},
                    k = 3,
                    xi = 48,
                    d1 = {"\u0000\u000e\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0003\u0010\u0004"},
                    d2 = {"", "p0", "", "a", "(Ljava/lang/String;)Ljava/lang/Boolean;"}
            )
            final class NamelessClass_1 extends Lambda implements Function1<String, Boolean> {
                NamelessClass_1() {
                    super(1);
                }

                public final Boolean a(String var1) {
                    Intrinsics.checkNotNullParameter(var1, "");
                    Locale var10000 = Locale.ENGLISH;
                    Intrinsics.checkNotNullExpressionValue(var10000, "");
                    String var2 = var1.toLowerCase(var10000);
                    Intrinsics.checkNotNullExpressionValue(var2, "");
                    var1 = var2;
                    return (StringsKt.contains$default((CharSequence)var0, (CharSequence)var1, false, 2, (Object)null) || StringsKt.contains$default((CharSequence)var1, (CharSequence)var0, false, 2, (Object)null)) && Math.abs(var1.length() - var0.length()) < 2;
                }
            }

            return (String)MathCompiler.h.keySet().stream().filter(Companion::b).findFirst().orElse((Object)null);
        }
    }

    @Metadata(
            mv = {1, 9, 0},
            k = 1,
            xi = 48,
            d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u000e\b\u0016\u0018\u00002\u00020\u0001B\u0011\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\b¢\u0006\u0004\b\u0018\u0010\u0019B\u0017\u0012\u0006\u0010\u0003\u001a\u00020\b\u0012\u0006\u0010\u001a\u001a\u00020\r¢\u0006\u0004\b\u0018\u0010\u001bJ\u0017\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0017¢\u0006\u0004\b\u0005\u0010\u0006J\u0017\u0010\t\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u0007H\u0017¢\u0006\u0004\b\t\u0010\nJ\u000f\u0010\u000b\u001a\u00020\u0004H\u0017¢\u0006\u0004\b\u000b\u0010\fR\u001a\u0010\u0012\u001a\u00020\r8\u0007X\u0087\u0004¢\u0006\f\n\u0004\b\u000e\u0010\u000f\u001a\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0017\u001a\u00020\b8\u0007X\u0087\u0004¢\u0006\f\n\u0004\b\u0013\u0010\u0014\u001a\u0004\b\u0015\u0010\u0016"},
            d2 = {"Lorg/kingdoms/utils/compilers/MathCompiler$ConstantExpr;", "Lorg/kingdoms/utils/compilers/MathCompiler$Expression;", "", "p0", "", "asString", "(Z)Ljava/lang/String;", "Lorg/kingdoms/utils/compilers/translators/MathematicalVariableTranslator;", "", "eval", "(Lorg/kingdoms/utils/compilers/translators/MathematicalVariableTranslator;)Ljava/lang/Double;", "toString", "()Ljava/lang/String;", "Lorg/kingdoms/utils/compilers/MathCompiler$ConstantExprType;", "b", "Lorg/kingdoms/utils/compilers/MathCompiler$ConstantExprType;", "getType", "()Lorg/kingdoms/utils/compilers/MathCompiler$ConstantExprType;", "type", "a", "D", "getValue", "()D", "value", "<init>", "(D)V", "p1", "(DLorg/kingdoms/utils/compilers/MathCompiler$ConstantExprType;)V"}
    )
    public static class ConstantExpr extends Expression {
        private final double a;
        @NotNull
        private final ConstantExprType b;

        public ConstantExpr(double var1, @NotNull ConstantExprType var3) {
            Intrinsics.checkNotNullParameter(var3, "");
            super();
            this.a = var1;
            this.b = var3;
        }

        @JvmName(
                name = "getValue"
        )
        public final double getValue() {
            return this.a;
        }

        @NotNull
        @JvmName(
                name = "getType"
        )
        public final ConstantExprType getType() {
            return this.b;
        }

        public ConstantExpr(double var1) {
            this(var1, MathCompiler.ConstantExprType.NUMBER);
        }

        @NotNull
        public Double eval(@NotNull MathematicalVariableTranslator var1) {
            Intrinsics.checkNotNullParameter(var1, "");
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

    @Metadata(
            mv = {1, 9, 0},
            k = 1,
            xi = 48,
            d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0007\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\b"},
            d2 = {"Lorg/kingdoms/utils/compilers/MathCompiler$ConstantExprType;", "", "<init>", "(Ljava/lang/String;I)V", "OPTIMIZED", "NUMBER", "STRING", "CONSTANT_VARIABLE", "TIME"}
    )
    public static enum ConstantExprType {
        OPTIMIZED,
        NUMBER,
        STRING,
        CONSTANT_VARIABLE,
        TIME;

        private ConstantExprType() {
        }

        @NotNull
        public static EnumEntries<ConstantExprType> getEntries() {
            return $ENTRIES;
        }

        static {
            ConstantExprType[] var0;
            (var0 = new ConstantExprType[5])[0] = OPTIMIZED;
            var0[1] = NUMBER;
            var0[2] = STRING;
            var0[3] = CONSTANT_VARIABLE;
            var0[4] = TIME;
            $ENTRIES = EnumEntriesKt.enumEntries((Enum[])var0);
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
            Intrinsics.checkNotNullParameter(var1, "");
            this.a = var1;
            return this;
        }

        public final <T extends Expression> boolean contains(@NotNull Class<T> var1, @NotNull Predicate<T> var2) {
            Intrinsics.checkNotNullParameter(var1, "");
            Intrinsics.checkNotNullParameter(var2, "");
            if (var1.isInstance(this)) {
                Intrinsics.checkNotNull(this);
                if (var2.test((Expression)this)) {
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
            Intrinsics.checkNotNullParameter(var1, "");
            Intrinsics.checkNotNullParameter(var2, "");
            super();
            this.a = var1;
            this.b = var2;
        }

        public final double next() {
            Expression[] var10000 = this.a.getArgs$core();
            int var1 = this.c++;
            return var10000[var1].eval(this.b);
        }

        @NotNull
        public List<Double> allArgs() {
            Expression[] var1;
            Expression[] var2 = var1 = this.a.getArgs$core();
            Collection var7 = new ArrayList(var1.length);
            int var3 = 0;

            for(int var4 = var2.length; var3 < var4; ++var3) {
                Expression var5 = var2[var3];
                var7.add(var5.eval(this.b));
            }

            return (List)var7;
        }

        public final double next(int var1) {
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
            Intrinsics.checkNotNullParameter(var1, "");
            super();
            this.a = var1;
            this.b = var2;
            this.c = var3;
        }

        @NotNull
        public final QuantumFunction getFunction$core() {
            return this.a;
        }


        public final boolean getOptimizable$core() {
            return this.b;
        }


        public final int getArgCount() {
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
            Intrinsics.checkNotNullParameter(var1, "");
            Intrinsics.checkNotNullParameter(var2, "");
            Intrinsics.checkNotNullParameter(var3, "");
            super();
            this.a = var1;
            this.b = var2;
            this.c = var3;
        }

        @NotNull
        public final String getName$core() {
            return this.a;
        }

        @NotNull
        public final Function getHandler$core() {
            return this.b;
        }

        @NotNull
        public final Expression[] getArgs$core() {
            return this.c;
        }

        @NotNull
        public final Double eval(@NotNull MathematicalVariableTranslator var1) {
            Intrinsics.checkNotNullParameter(var1, "");
            return this.b.getFunction$core().apply(new FnArgs(this, var1));
        }

        @NotNull
        public final String asString(boolean var1) {
            return this.a + '(' + ArraysKt.joinToString$default(this.c, (CharSequence)", ", (CharSequence)null, (CharSequence)null, 0, (CharSequence)null, (Function1)null, 62, (Object)null) + ')';
        }

        @NotNull
        public final String toString() {
            Object[] var1;


            final class NamelessClass_1 extends Lambda implements Function1<Expression, String> {
                public static final NamelessClass_1 a = new NamelessClass_1();

                NamelessClass_1() {
                    super(1);
                }

                public final String a(Expression var1) {
                    Intrinsics.checkNotNullParameter(var1, "");
                    return var1.toString();
                }
            }

            Intrinsics.checkNotNullExpressionValue(var1 = Arrays.stream(this.c).map(FunctionExpr::a).toArray(FunctionExpr::a), "");
            String[] var2 = (String[])var1;
            return this.a + '(' + (this.c.length == 0 ? "" : ArraysKt.joinToString$default(var2, (CharSequence)", ", (CharSequence)null, (CharSequence)null, 0, (CharSequence)null, (Function1)null, 62, (Object)null)) + ')';
        }

        private static final String a(Function1 var0, Object var1) {
            Intrinsics.checkNotNullParameter(var0, "");
            return (String)var0.invoke(var1);
        }

        private static final String[] a(int var0) {
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
        @JvmName(
                name = "getFunction"
        )
        public final Function getFunction() {
            return this.b;
        }

        public final boolean isFunction() {
            return this.getFunction() != null;
        }

        @NotNull
        public final String toString() {
            return "LexicalEnvironment{index=" + this.a + ", function=" + (this.getFunction() != null) + '}';
        }
    }

    @Metadata(
            mv = {1, 9, 0},
            k = 1,
            xi = 48,
            d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u00002\u00060\u0001j\u0002`\u0002B\u000f\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0004\b\u0005\u0010\u0006"},
            d2 = {"Lorg/kingdoms/utils/compilers/MathCompiler$MathEvaluateException;", "Ljava/lang/ArithmeticException;", "Lorg/kingdoms/libs/kotlin/ArithmeticException;", "", "p0", "<init>", "(Ljava/lang/String;)V"}
    )
    public static final class MathEvaluateException extends ArithmeticException {
        public MathEvaluateException(@NotNull String var1) {
            Intrinsics.checkNotNullParameter(var1, "");
            super(var1);
        }
    }

    @Metadata(
            mv = {1, 9, 0},
            k = 1,
            xi = 48,
            d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0005\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\f\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0007\b\u0000\u0018\u00002\u00020\u0001B!\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u001d\u0012\u0006\u0010#\u001a\u00020\"\u0012\u0006\u0010$\u001a\u00020\u000f¢\u0006\u0004\b%\u0010&B1\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u001d\u0012\u0006\u0010#\u001a\u00020\"\u0012\u0006\u0010$\u001a\u00020\"\u0012\u0006\u0010'\u001a\u00020\u001a\u0012\u0006\u0010(\u001a\u00020\u000f¢\u0006\u0004\b%\u0010)J\u0017\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0000H\u0000¢\u0006\u0004\b\u0004\u0010\u0005J\u000f\u0010\u0007\u001a\u00020\u0006H\u0017¢\u0006\u0004\b\u0007\u0010\bR\u001a\u0010\u000e\u001a\u00020\t8\u0001X\u0081\u0004¢\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\f\u0010\rR\u001a\u0010\u0014\u001a\u00020\u000f8\u0001X\u0081\u0004¢\u0006\f\n\u0004\b\u0010\u0010\u0011\u001a\u0004\b\u0012\u0010\u0013R\u0014\u0010\u0018\u001a\u00020\u00158\u0002X\u0083\u0004¢\u0006\u0006\n\u0004\b\u0016\u0010\u0017R\u0014\u0010\u0016\u001a\u00020\u00158\u0002X\u0083\u0004¢\u0006\u0006\n\u0004\b\u0019\u0010\u0017R\u0014\u0010\u0019\u001a\u00020\u001a8\u0002X\u0083\u0004¢\u0006\u0006\n\u0004\b\u001b\u0010\u001cR\u001a\u0010!\u001a\u00020\u001d8\u0007X\u0087\u0004¢\u0006\f\n\u0004\b\u0018\u0010\u001e\u001a\u0004\b\u001f\u0010 "},
            d2 = {"Lorg/kingdoms/utils/compilers/MathCompiler$Operator;", "", "p0", "", "hasPrecedenceOver$core", "(Lorg/kingdoms/utils/compilers/MathCompiler$Operator;)Z", "", "toString", "()Ljava/lang/String;", "Lorg/kingdoms/utils/compilers/MathCompiler$Arity;", "e", "Lorg/kingdoms/utils/compilers/MathCompiler$Arity;", "getArity$core", "()Lorg/kingdoms/utils/compilers/MathCompiler$Arity;", "arity", "Lorg/kingdoms/utils/compilers/MathCompiler$TriDoubleFn;", "f", "Lorg/kingdoms/utils/compilers/MathCompiler$TriDoubleFn;", "getFunction$core", "()Lorg/kingdoms/utils/compilers/MathCompiler$TriDoubleFn;", "function", "", "b", "B", "a", "c", "Lorg/kingdoms/utils/compilers/MathCompiler$Side;", "d", "Lorg/kingdoms/utils/compilers/MathCompiler$Side;", "", "C", "getSymbol", "()C", "symbol", "", "p1", "p2", "<init>", "(CILorg/kingdoms/utils/compilers/MathCompiler$TriDoubleFn;)V", "p3", "p4", "(CIILorg/kingdoms/utils/compilers/MathCompiler$Side;Lorg/kingdoms/utils/compilers/MathCompiler$TriDoubleFn;)V"}
    )
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
            Intrinsics.checkNotNullParameter(var4, "");
            Intrinsics.checkNotNullParameter(var5, "");
            super();
            this.a = var1;
            this.b = (byte)var2;
            this.c = (byte)var3;
            this.d = var4;
            this.f = var5;
            this.e = (var1 = this.a) == '-' ? MathCompiler.Arity.UNARY_AND_BINARY : (var1 == '~' ? MathCompiler.Arity.UNARY : MathCompiler.Arity.BINARY);
        }

        @JvmName(
                name = "getSymbol"
        )
        public final char getSymbol() {
            return this.a;
        }

        @NotNull
        @JvmName(
                name = "getArity$core"
        )
        public final Arity getArity$core() {
            return this.e;
        }

        @NotNull
        @JvmName(
                name = "getFunction$core"
        )
        public final TriDoubleFn getFunction$core() {
            return this.f;
        }

        public Operator(char var1, int var2, @NotNull TriDoubleFn var3) {
            Intrinsics.checkNotNullParameter(var3, "");
            this(var1, var2, var2, MathCompiler.Side.NONE, var3);
        }

        @NotNull
        public final String toString() {
            return "MathOperator['" + this.a + "']";
        }

        public final boolean hasPrecedenceOver$core(@NotNull Operator var1) {
            Intrinsics.checkNotNullParameter(var1, "");
            return this.b >= var1.b;
        }
    }

    @Metadata(
            mv = {1, 9, 0},
            k = 1,
            xi = 48,
            d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\bæ\u0080\u0001\u0018\u00002\u00020\u0001J\u0017\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H&¢\u0006\u0004\b\u0005\u0010\u0006ø\u0001\u0000\u0082\u0002\u0006\n\u0004\b!0\u0001À\u0006\u0001"},
            d2 = {"Lorg/kingdoms/utils/compilers/MathCompiler$QuantumFunction;", "", "Lorg/kingdoms/utils/compilers/MathCompiler$FnArgs;", "p0", "", "apply", "(Lorg/kingdoms/utils/compilers/MathCompiler$FnArgs;)D"}
    )
    public interface QuantumFunction {
        double apply(@NotNull FnArgs var1);
    }

    @Metadata(
            mv = {1, 9, 0},
            k = 1,
            xi = 48,
            d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0080\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006"},
            d2 = {"Lorg/kingdoms/utils/compilers/MathCompiler$Side;", "", "<init>", "(Ljava/lang/String;I)V", "RIGHT", "LEFT", "NONE"}
    )
    public static enum Side {
        RIGHT,
        LEFT,
        NONE;

        private Side() {
        }

        @NotNull
        public static EnumEntries<Side> getEntries() {
            return $ENTRIES;
        }

        static {
            Side[] var0;
            (var0 = new Side[3])[0] = RIGHT;
            var0[1] = LEFT;
            var0[2] = NONE;
            $ENTRIES = EnumEntriesKt.enumEntries((Enum[])var0);
        }
    }

    @Metadata(
            mv = {1, 9, 0},
            k = 1,
            xi = 48,
            d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0006\n\u0002\b\n\b\u0000\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0004\b\u0011\u0010\u0012J\u0017\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0017¢\u0006\u0004\b\u0005\u0010\u0006J\u0017\u0010\t\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u0007H\u0017¢\u0006\u0004\b\t\u0010\nJ\u000f\u0010\u000b\u001a\u00020\u0004H\u0017¢\u0006\u0004\b\u000b\u0010\fR\u001a\u0010\u0010\u001a\u00020\u00048\u0001X\u0081\u0004¢\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000f\u0010\f"},
            d2 = {"Lorg/kingdoms/utils/compilers/MathCompiler$StringConstant;", "Lorg/kingdoms/utils/compilers/MathCompiler$ConstantExpr;", "", "p0", "", "asString", "(Z)Ljava/lang/String;", "Lorg/kingdoms/utils/compilers/translators/MathematicalVariableTranslator;", "", "eval", "(Lorg/kingdoms/utils/compilers/translators/MathematicalVariableTranslator;)Ljava/lang/Double;", "toString", "()Ljava/lang/String;", "a", "Ljava/lang/String;", "getString$core", "string", "<init>", "(Ljava/lang/String;)V"}
    )
    public static final class StringConstant extends ConstantExpr {
        @NotNull
        private final String a;

        public StringConstant(@NotNull String var1) {
            Intrinsics.checkNotNullParameter(var1, "");
            super((double)var1.hashCode(), MathCompiler.ConstantExprType.STRING);
            this.a = var1;
        }

        @NotNull
        @JvmName(
                name = "getString$core"
        )
        public final String getString$core() {
            return this.a;
        }

        @NotNull
        public final Double eval(@NotNull MathematicalVariableTranslator var1) {
            Intrinsics.checkNotNullParameter(var1, "");
            return super.getValue();
        }

        @NotNull
        public final String toString() {
            return "StringConstant(\"" + this.a + "\")";
        }

        @NotNull
        public final String asString(boolean var1) {
            return this.a;
        }
    }

    @Metadata(
            mv = {1, 9, 0},
            k = 1,
            xi = 48,
            d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u0006\n\u0002\b\u0004\bæ\u0080\u0001\u0018\u00002\u00020\u0001J\u001f\u0010\u0005\u001a\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u0002H&¢\u0006\u0004\b\u0005\u0010\u0006ø\u0001\u0000\u0082\u0002\u0006\n\u0004\b!0\u0001À\u0006\u0001"},
            d2 = {"Lorg/kingdoms/utils/compilers/MathCompiler$TriDoubleFn;", "", "", "p0", "p1", "apply", "(DD)D"}
    )
    public interface TriDoubleFn {
        double apply(double var1, double var3);
    }

    private static final class a extends Expression {
        @NotNull
        private final String a;

        public a(@NotNull String var1) {
            Intrinsics.checkNotNullParameter(var1, "");
            super();
            this.a = var1;
        }

        @NotNull
        public final Double eval(@NotNull MathematicalVariableTranslator var1) {
            Intrinsics.checkNotNullParameter(var1, "");
            Double var3;
            if ((var3 = (Double)var1.apply(this.a)) == null) {
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
        public final String asString(boolean var1) {
            return this.a;
        }

        @NotNull
        public final String toString() {
            return "MathVariable(" + this.a + ')';
        }
    }
}
