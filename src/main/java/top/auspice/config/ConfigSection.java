package top.auspice.config;

import org.bukkit.configuration.ConfigurationSection;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import top.auspice.data.Pair;
import top.auspice.libs.snakeyaml.DumperOptions;
import top.auspice.libs.snakeyaml.nodes.*;

import java.util.*;


public class ConfigSection {
    protected final ScalarNode key;
    protected final MappingNode root;
    private static final ScalarNode ROOT;

    public ConfigSection(ScalarNode var1, MappingNode var2) {
        this.key = var1;
        this.root = Objects.requireNonNull(var2);
    }

    public ConfigSection(MappingNode var1) {
        this(ROOT, var1);
    }

    public static ConfigSection empty() {
        return new ConfigSection(new MappingNode());
    }

    public ConfigSection(NodePair var1) {
        this(var1.getKey(), (MappingNode)var1.getValue());
    }

    @Deprecated
    public @NotNull Set<String> getKeys(boolean var1) {
        if (var1) {
            throw new UnsupportedOperationException("Deep keys are not supported");
        } else {
            return this.getKeys();
        }
    }

    public MappingNode getNode() {
        return this.root;
    }

    public @NotNull String getName() {
        return this.key.getValue();
    }

    public ScalarNode getKey() {
        return this.key;
    }

    public @NotNull Set<String> getKeys() {
        return this.root.getPairs().keySet();
    }

    public MappingNode getCurrentNode() {
        return this.root;
    }

    public @NotNull Map<String, ConfigSection> getSections() {
        LinkedHashMap var1 = new LinkedHashMap();
        Iterator var2 = this.root.getPairs().values().iterator();

        while(var2.hasNext()) {
            NodePair var3;
            if ((var3 = (NodePair)var2.next()).getValue() instanceof MappingNode) {
                var1.put(var3.getKey().getValue(), new ConfigSection(var3.getKey(), (MappingNode)var3.getValue()));
            }
        }

        return var1;
    }

    public @NotNull Map<String, Object> getValues(boolean var1) {
        if (var1) {
            throw new UnsupportedOperationException("Deep keys are not supported");
        } else {
            LinkedHashMap var4 = new LinkedHashMap();
            Iterator var2 = this.root.getPairs().values().iterator();

            while(var2.hasNext()) {
                NodePair var3 = (NodePair)var2.next();
                var4.put(var3.getKey().getValue(), var3.getValue().getParsed());
            }

            return var4;
        }
    }



    public Node getNode(String var1) {
        NodePair var2;
        return (var2 = this.getPair(var1)) == null ? null : var2.getValue();
    }

    public boolean isSet(@NotNull String var1) {
        return this.root.getPairs().containsKey(var1);
    }

    public boolean isSet(String... var1) {
        return this.findNode(var1) != null;
    }

    public @Nullable Object get(String... var1) {
        Node var2;
        return (var2 = this.findNode(var1)) == null ? null : var2.getParsed();
    }

    public @Nullable String getString(String... var1) {
        return (String)NodeInterpreter.STRING.parse(this.findNode(var1));
    }

    public @Nullable String getString(@NotNull String var1) {
        return (String)NodeInterpreter.STRING.parse(this.getNode(var1));
    }

    public int getInt(String... var1) {
        return (Integer)NodeInterpreter.INT.parse(this.findNode(var1));
    }

    public boolean getBoolean(String... var1) {
        return (Boolean)NodeInterpreter.BOOLEAN.parse(this.findNode(var1));
    }

    public double getDouble(String... var1) {
        return (Double)NodeInterpreter.DOUBLE.parse(this.findNode(var1));
    }

    public long getLong(String... var1) {
        return (Long)NodeInterpreter.LONG.parse(this.findNode(var1));
    }

    public float getFloat(String... var1) {
        return (Float)NodeInterpreter.FLOAT.parse(this.findNode(var1));
    }

    public <T> T get(NodeInterpreter<T> var1, String var2) {
        return var1.parse(this.getNode(var2));
    }

    public <T> T get(NodeInterpreter<T> var1, NodeInterpretContext<T> var2, String var3) {
        return var1.parse(var2.withNode(this.getNode(var3)));
    }

    public @NotNull List<String> getStringList(String... var1) {
        return (List)NodeInterpreter.STRING_LIST.parse(this.findNode(var1));
    }

    public @NotNull List<Integer> getIntegerList(String... var1) {
        return (List)NodeInterpreter.INT_LIST.parse(this.findNode(var1));
    }

    public ConfigSection getSection(String... var1) {
        Pair var2;
        if ((var2 = this.traverseNodePairs(var1, false)) == null) {
            return null;
        } else {
            return ((NodePair)var2.getValue()).getValue().getNodeType() != NodeType.MAPPING ? null : new ConfigSection(((NodePair)var2.getValue()).getKey(), (MappingNode)((NodePair)var2.getValue()).getValue());
        }
    }

    public Node findNode(String[] var1) {
        Pair var2;
        return (var2 = this.traverseNodePairs(var1, false)) == null ? null : ((NodePair)var2.getValue()).getValue();
    }

    public Pair<ConfigSection, NodeTuple> traverseNodePairs(String[] var1, boolean var2) {
        if (var1.length == 1) {
            NodeTuple var9;
            if ((var9 = this.getPair(var1[0])) == null) {
                return var2 ? Pair.of(this, this.root.put(var1[0], new MappingNode())) : null;
            } else {
                return Pair.of(this, var9);
            }
        } else {
            ConfigSection var3 = this;
            NodeTuple var4 = new NodeTuple(this.key == null ? ROOT : this.key, this.root);
            boolean var5 = false;
            int var6 = (var1 = var1).length;

            for(int var7 = 0; var7 < var6; ++var7) {
                String var8 = var1[var7];
                if (var5) {
                    var4 = (var3 = new ConfigSection(null, (MappingNode)var4.getValueNode())).root.put(var8, new MappingNode());
                } else {
                    if (var4.getValueNode().getNodeId() != NodeId.mapping) {
                        var4 = null;
                    } else {
                        var4 = (var3 = new ConfigSection(null, (MappingNode)var4.getValueNode())).getPair(var8);
                    }

                    if (var4 == null) {
                        if (!var2) {
                            return null;
                        }

                        var5 = true;
                        var4 = var3.root.put(var8, new MappingNode());
                    }
                }
            }

            return Pair.of(var3, var4);
        }
    }

    public NodePair set(String var1, Object var2) {
        if (var2 == null) {
            this.root.getPairs().remove(var1);
            return null;
        } else {
            NodePair var3;
            if ((var3 = this.getPair(var1)) == null) {
                var3 = this.root.put(var1, NodeInterpreter.nodeOfObject(var2));
            } else {
                var3.setValue(NodeInterpreter.nodeOfObject(var2));
            }

            return var3;
        }
    }

    public Pair<ConfigSection, NodeTuple> set(String[] var1, Object var2) {
        Pair var3;
        ((NodeTuple)(var3 = this.traverseNodePairs(var1, true)).getValue()).setValueNode(NodeInterpreter.nodeOfObject(var2));
        return var3;
    }

    public ConfigSection createSection(String... var1) {
        return this.createSection(var1, null).getKey();
    }

    public Pair<ConfigSection, NodeTuple> createSection(String[] var1, Object var2) {
        MappingNode var3 = this.root;
        NodeTuple var4 = new NodeTuple(ROOT, this.root);
        int var5 = 0;
        String[] var6 = var1;
        int var7 = var1.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            String var9 = var6[var8];
            if (var2 != null) {
                ++var5;
                if (var5 == var1.length) {
                    NodeTuple var11 = (new ConfigSection(null, var3)).set(var9, var2);
                    return Pair.of(new ConfigSection(var4), var11);
                }
            }

            if ((var4 = (NodeTuple)var3.getPairs().get(var9)) != null && var4.getValue().getNodeType() == NodeType.MAPPING) {
                var3 = (MappingNode)var4.getValue();
            } else {
                MappingNode var10 = new MappingNode();
                var4 = var3.put(var9, var10);
                var3 = var10;
            }
        }

        return Pair.of(new ConfigSection(null, var3), var4);
    }

    public String toString() {
        return "ConfigSection{ " + this.root + " }";
    }

    public Node findNode(String var1) {
        MappingNode var2 = this.root;
        int var3;
        if ((var3 = var1.indexOf(46)) == -1) {
            return this.getNode(var1);
        } else {
            int var4 = 0;

            while((var2).getNodeId() == NodeId.mapping) {
                String var5 = var1.substring(var4, var3);
                if ((var2 = (MappingNode) (new ConfigSection(null, var2)).getNode(var5)) == null) {
                    return null;
                }

                if (var3 == var1.length()) {
                    return var2;
                }

                var4 = var3 + 1;
                if ((var3 = var1.indexOf(46, var4)) == -1) {
                    var3 = var1.length();
                }
            }

            return null;
        }
    }

    public @NonNull ConfigurationSection toBukkitConfigurationSection() {
        return new ROOT(this);
    }

    public @Nullable ConfigurationSection getConfigurationSection(@NotNull String var1) {
        ConfigSection var2;
        return (var2 = this.getSection(var1)) == null ? null : var2.toBukkitConfigurationSection();
    }

    static {
        ROOT = new ScalarNode(Tag.STR, "[({<ROOT>})]", DumperOptions.ScalarStyle.PLAIN);
    }
}
