package top.auspice.locale.placeholder.context;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PlaceholderContextBuilder implements Cloneable, LocalPlaceholderProvider, PlaceholderTargetProvider {
    public static final PlaceholderContextBuilder DEFAULT = new PlaceholderContextBuilder();
    protected Map<String, Object> placeholders;
    protected Map<String, PlaceholderProvider> children;
    protected PlaceholderProvider unknownPlaceholderHandler;
    protected Object primaryTarget;
    protected Object secondaryTarget;
    protected Map<String, Object> targets;

    public PlaceholderContextBuilder() {
    }

    public Object providePlaceholder(String var1) {
        Object var2;
        if ((var2 = this.provideLocalPlaceholder(new PlaceholderParts(var1))) != null) {
            return var2;
        } else {
            Placeholder var3;
            var2 = (var3 = PlaceholderParser.parse(var1)).request(this);
            return var3.applyModifiers(var2);
        }
    }

    public @Nullable Object provideLocalPlaceholder(@NotNull PlaceholderParts var1) {
        Object var2;
        if (this.placeholders != null && (var2 = this.placeholders.get(var1.getFull())) != null) {
            return PlaceholderTranslationContext.unwrapPlaceholder(var2);
        } else {
            PlaceholderProvider var3;
            if (this.children != null && (var3 = (PlaceholderProvider)this.children.get(var1.getId())) != null) {
                return PlaceholderTranslationContext.unwrapPlaceholder(var3.providePlaceholder(var1.getParameterFrom(1)));
            } else {
                return this.unknownPlaceholderHandler != null && (var2 = this.unknownPlaceholderHandler.providePlaceholder(var1.getFull())) != null ? PlaceholderTranslationContext.unwrapPlaceholder(var2) : null;
            }
        }
    }

    public PlaceholderContextBuilder clone() {
        return this.cloneInto(new PlaceholderContextBuilder());
    }

    public PlaceholderContextBuilder cloneInto(PlaceholderContextBuilder var1) {
        var1.primaryTarget = this.primaryTarget;
        var1.secondaryTarget = this.secondaryTarget;
        if (this.placeholders != null) {
            var1.placeholders = new HashMap(this.placeholders);
        }

        if (this.children != null) {
            var1.children = this.children;
        }

        return this;
    }

    public void setChildren(Map<String, PlaceholderProvider> var1) {
        this.children = var1;
    }

    public Map<String, PlaceholderProvider> getChildren() {
        return this.children;
    }

    public PlaceholderContextBuilder impregnate(int var1) {
        if (this.children == null) {
            this.children = new HashMap(var1);
        }

        return this;
    }

    public PlaceholderContextBuilder addChild(String var1, PlaceholderProvider var2) {
        if (var1.contains("_")) {
            throw new IllegalArgumentException("Child element name cannot contain underscore, consider nesting groups instead or using another name: " + var1);
        } else {
            this.impregnate(1);
            if (this.children.put(var1, var2) != null) {
                throw new IllegalArgumentException("Child added twice: " + var1 + " -> " + var2);
            } else {
                return this;
            }
        }
    }

    public PlaceholderContextBuilder inheritPlaceholders(PlaceholderContextBuilder var1) {
        this.addAllIfAbsent(var1.placeholders);
        if (this.children == null && var1.children != null) {
            this.children = var1.children;
        }

        return this;
    }

    public PlaceholderContextBuilder inheritContext(BasePlaceholderTargetProvider var1, boolean var2) {
        if (var2 || this.primaryTarget == null) {
            this.primaryTarget = var1.getPrimaryTarget();
        }

        if (var2 || this.secondaryTarget == null) {
            this.secondaryTarget = var1.getSecondaryTarget();
        }

        return this;
    }

    public PlaceholderContextBuilder addAll(Map<String, Object> var1) {
        if (this.placeholders == null) {
            this.placeholders = var1;
            return this;
        } else {
            this.placeholders.putAll(var1);
            return this;
        }
    }

    public PlaceholderContextBuilder addAllIfAbsent(Map<String, Object> var1) {
        if (var1 != null && !var1.isEmpty()) {
            if (this.placeholders == null) {
                this.placeholders = new HashMap(var1);
                return this;
            } else {
                Iterator var3 = var1.entrySet().iterator();

                while(var3.hasNext()) {
                    Map.Entry var2 = (Map.Entry)var3.next();
                    this.placeholders.putIfAbsent((String)var2.getKey(), var2.getValue());
                }

                return this;
            }
        } else {
            return this;
        }
    }

    public PlaceholderContextBuilder raws(Object... var1) {
        this.variables(true, var1);
        return this;
    }

    public PlaceholderContextBuilder placeholders(Object... var1) {
        this.variables(false, var1);
        return this;
    }

    protected PlaceholderContextBuilder variables(boolean var1, Object[] var2) {
        if (var2.length == 0) {
            return this;
        } else if (var2.length % 2 == 1) {
            throw new IllegalArgumentException("Missing variable/replacement for one of edits, possibly: " + var2[var2.length - 1]);
        } else {
            this.addAll(PlaceholderParser.serializeVariables(var1, this.placeholders, var2));
            return this;
        }
    }

    public Map<String, Object> getPlaceholders() {
        return this.placeholders;
    }

    public boolean hasContext() {
        return this.primaryTarget != null;
    }

    public PlaceholderContextBuilder addTarget(String var1, Object var2) {
        this.getTargets0().put(var1, var2);
        return this;
    }

    public PlaceholderContextBuilder withContext(Player var1) {
        return var1 == null ? this : this.withContext((OfflinePlayer)var1);
    }

    public PlaceholderContextBuilder withContext(KingdomPlayer var1) {
        Player var2;
        if ((var2 = var1.getPlayer()) != null) {
            this.withContext(var2);
        }

        OfflinePlayer var3 = var1.getOfflinePlayer();
        this.withContext(var3);
        return this;
    }

    public PlaceholderContextBuilder withContext(PlaceholderTarget var1) {
        Object var2;
        if ((var2 = var1.provideTo(this)) == null) {
            return this;
        } else {
            this.primaryTarget = var2;
            return this;
        }
    }

    public PlaceholderContextBuilder withContext(OfflinePlayer var1) {
        if (var1 == null) {
            return this;
        } else {
            this.primaryTarget = var1;
            return this;
        }
    }

    public PlaceholderContextBuilder withContext(CommandSender var1) {
        Objects.requireNonNull(var1);
        if (var1 instanceof Player) {
            return this.withContext((Player)var1);
        } else {
            return var1 instanceof OfflinePlayer ? this.withContext((OfflinePlayer)var1) : this;
        }
    }


    public PlaceholderProvider getUnknownPlaceholderHandler() {
        return this.unknownPlaceholderHandler;
    }

    public PlaceholderProvider onUnknownPlaceholder(PlaceholderProvider var1) {
        this.unknownPlaceholderHandler = var1;
        return this;
    }




    public PlaceholderContextBuilder ensurePlaceholdersCapacity(int var1) {
        if (this.placeholders == null) {
            this.placeholders = new HashMap(var1);
        }

        return this;
    }

    public PlaceholderContextBuilder resetPlaceholders() {
        this.placeholders = null;
        return this;
    }

    public PlaceholderContextBuilder parse(String var1, Object var2) {
        if (var2 == null) {
            return this;
        } else {
            this.ensurePlaceholdersCapacity(12);
            this.placeholders.put(var1, PlaceholderTranslationContext.withDefaultContext(var2));
            return this;
        }
    }

    public PlaceholderContextBuilder raw(String var1, Supplier<Object> var2) {
        return this.raw(var1, (Object)var2);
    }

    public PlaceholderContextBuilder raw(String var1, Object var2) {
        if (var2 == null) {
            return this;
        } else {
            this.ensurePlaceholdersCapacity(12);
            this.placeholders.put(var1, var2);
            return this;
        }
    }

    public Object getPlaceholder(String var1) {
        return this.placeholders != null ? this.placeholders.get(var1) : null;
    }

    public String toString() {
        return "MessageBuilder{ context=" + this.primaryTarget + ", other=" + this.secondaryTarget + ", placeholder=" + (this.placeholders == null ? "{}" : this.placeholders.entrySet().stream().map((var0) -> {
            return String.valueOf(var0.getKey()) + '=' + var0.getValue();
        }).collect(Collectors.toList())) + " }";
    }

    public void setPrimaryTarget(Object var1) {
        this.primaryTarget = var1;
    }

    public void setSecondaryTarget(Object var1) {
        this.secondaryTarget = var1;
    }

    public PlaceholderTargetProvider switchTargets() {
        Object var1 = this.primaryTarget;
        this.primaryTarget = this.secondaryTarget;
        this.secondaryTarget = var1;
        return this;
    }

    public @Nullable Object getPrimaryTarget() {
        return this.primaryTarget;
    }

    public @Nullable Object getSecondaryTarget() {
        return this.secondaryTarget;
    }

    protected Map<String, Object> getTargets0() {
        if (this.targets == null) {
            this.targets = new HashMap(3);
        }

        return this.targets;
    }

    public @NonNull Map<String, Object> getTargets() {
        return this.getTargets0();
    }
}

