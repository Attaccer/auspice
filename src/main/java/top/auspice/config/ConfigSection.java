package top.auspice.config;

import org.jetbrains.annotations.NotNull;
import top.auspice.libs.snakeyaml.DumperOptions;
import top.auspice.libs.snakeyaml.nodes.*;

import java.util.*;

public class ConfigSection {

    private final NodeTuple value;


    public ConfigSection(NodeTuple value) {
        this.value = value;
    }


    public Node getKeyNode() {
        return this.value.getKeyNode();
    }

    public Node getValueNode() {
        return this.value.getValueNode();
    }


    public Map<Node, Object> getValues(boolean var1) {
        LinkedHashMap<Node, Object> values = null;
        if (this.value.getValueNode() instanceof MappingNode mappingNode) {
            values = new LinkedHashMap<>();
            for (NodeTuple tuple : mappingNode.getValue()) {
                values.put(tuple.getKeyNode(), tuple.getValueNode().getParsed());
            }
        }
        return values;
    }

    public List<ConfigSection> getSections() {
        List<ConfigSection> sections = null;
        if (this.value.getValueNode() instanceof MappingNode mappingNode) {
            sections = new ArrayList<>();
            for (NodeTuple tuple : mappingNode.getValue()) {
                sections.add(new ConfigSection(tuple));
            }
        }
        if (this.value.getValueNode() instanceof SequenceNode) {
            sections = null;
        }

        return sections;
    }



}