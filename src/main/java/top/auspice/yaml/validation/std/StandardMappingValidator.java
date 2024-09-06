package top.auspice.yaml.validation.std;

import top.auspice.libs.snakeyaml.error.Mark;
import top.auspice.libs.snakeyaml.nodes.MappingNode;
import top.auspice.libs.snakeyaml.nodes.NodeTuple;
import top.auspice.libs.snakeyaml.nodes.Tag;
import top.auspice.yaml.validation.ValidationContext;
import top.auspice.yaml.validation.ValidationFailure;
import top.auspice.yaml.validation.base.NodeValidator;

import java.util.*;

public class StandardMappingValidator implements NodeValidator {
    private final NodeValidator keyValidator;
    private final NodeValidator valueValidator;
    private final Map<String, NodeValidator> specificValidators;
    private final HashSet<String> requiredKeys;
    private final HashSet<String> valueValidatorKeys;
    private final NodeValidator[] extendedValidators;
    private final boolean optional;

    public StandardMappingValidator(NodeValidator[] extendedValidators, NodeValidator keyValidator, NodeValidator validators, Collection<String> valueValidatorKeys, Map<String, NodeValidator> specificValidators, Collection<String> requiredKeys, boolean isOptional) {
        this.extendedValidators = extendedValidators;
        this.keyValidator = keyValidator;
        this.valueValidatorKeys = valueValidatorKeys == null ? null : new HashSet(valueValidatorKeys);
        this.requiredKeys = requiredKeys == null ? null : new HashSet(requiredKeys);
        this.valueValidator = validators;
        this.specificValidators = specificValidators;
        this.optional = isOptional;
    }

    public Set<String> getRequiredKeys() {
        return this.requiredKeys;
    }

    public Set<String> getValueValidatorKeys() {
        return this.valueValidatorKeys;
    }

    public Map<String, NodeValidator> getSpecificValidators() {
        return this.specificValidators;
    }

    public NodeValidator getKeyValidator() {
        return this.keyValidator;
    }

    public boolean isOptional() {
        return this.optional;
    }

    public NodeValidator getValueValidator() {
        return this.valueValidator;
    }

    public NodeValidator[] getExtendedValidators() {
        return this.extendedValidators;
    }

    public NodeValidator getValidatorForEntry(String key) {
        if (this.specificValidators != null) {
            NodeValidator validator = (NodeValidator)this.specificValidators.get(key);
            if (validator != null) {
                return validator;
            }
        }

        return this.valueValidator == null || this.valueValidatorKeys != null && !this.valueValidatorKeys.contains(key) ? null : this.valueValidator;
    }

    public ValidationFailure validate(ValidationContext context) {
        if (this.extendedValidators != null) {
            NodeValidator[] var2 = this.extendedValidators;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                NodeValidator extendedValidator = var2[var4];
                ValidationFailure res = extendedValidator.validate(context);
                if (res != null) {
                    return res;
                }
            }
        }

        if (!(context.getNode() instanceof MappingNode)) {
            return this.optional && context.getNode().getTag() == Tag.NULL ? null : context.err("Expected a mapping section, instead got " + context.getNode().getClass().getSimpleName().toLowerCase(Locale.ENGLISH));
        } else {
            MappingNode mapping = (MappingNode)context.getNode();
            Set<String> requiredKeys = null;
            if (this.requiredKeys != null) {
                requiredKeys = (Set)this.requiredKeys.clone();
            }

            Iterator var10 = mapping.getValue().iterator();

            while(true) {
                Map.Entry entry;
                NodeTuple pair;
                do {
                    do {
                        do {
                            if (!var10.hasNext()) {
                                if (requiredKeys != null && !requiredKeys.isEmpty()) {
                                    return context.err("Missing required entries " + Arrays.toString(requiredKeys.toArray())).withMarker(context.getRelatedKey() == null ? new Mark("[ROOT]", 0, 0, 0, new char[0], 0) : context.getRelatedKey().getStartMark());
                                }

                                return null;
                            }

                            entry = (Map.Entry)var10.next();
                            pair = (NodeTuple) entry.getValue();
                            if (this.keyValidator != null) {
                                ValidationFailure failure = this.keyValidator.validate(context.delegate(pair.getKeyNode(), pair.getKeyNode()));
                                if (failure != null) {
                                    failure.setMessage("Disallowed key type. " + failure.getMessage());
                                }
                            }

                            if (requiredKeys != null) {
                                requiredKeys.remove(entry.getKey());
                            }

                            if (this.specificValidators == null) {
                                break;
                            }

                            NodeValidator validator = this.specificValidators.get(entry.getKey());
                            if (validator == null) {
                                break;
                            }

                            validator.validate(context.delegate(pair.getKeyNode(), pair.getValueNode()));
                        } while(!(pair.getValueNode() instanceof MappingNode));
                    } while(this.valueValidator == null);
                } while(this.valueValidatorKeys != null && !this.valueValidatorKeys.contains(entry.getKey()));

                this.valueValidator.validate(context.delegate(pair.getKeyNode(), pair.getValueNode()));
            }
        }
    }

    public String getName() {
        return "a section";
    }

    static int findLongestString(Collection<String> strings) {
        int longest = 0;

        String str;
        for(Iterator var2 = strings.iterator(); var2.hasNext(); longest = Math.max(longest, str.length())) {
            str = (String)var2.next();
        }

        return longest;
    }

    static String repeat(int times) {
        StringBuilder builder = new StringBuilder(times);

        for(int i = 0; i < times; ++i) {
            builder.append(' ');
        }

        return builder.toString();
    }

    static String padRight(String str, int num) {
        int diff = num - str.length();
        return str + repeat(diff);
    }

    static String toString(NodeValidator validator, String rootSpaces) {
        StringBuilder builder = new StringBuilder(100);
        String spaces = rootSpaces + "  ";
        if (validator instanceof StandardMappingValidator) {
            StandardMappingValidator mappingValidator = (StandardMappingValidator)validator;
            builder.append("StandardMappingValidator").append(mappingValidator.optional ? "?" : "").append(" {").append('\n');
            if (mappingValidator.keyValidator != null) {
                builder.append(spaces).append("keyValidator=").append(toString(mappingValidator.keyValidator, rootSpaces + "  ")).append('\n');
            }

            if (mappingValidator.requiredKeys != null && !mappingValidator.requiredKeys.isEmpty()) {
                builder.append(spaces).append("requiredKeys=").append(mappingValidator.requiredKeys).append('\n');
            }

            if (mappingValidator.valueValidator != null) {
                builder.append(spaces).append("valueValidator=").append(toString(mappingValidator.valueValidator, rootSpaces + "  ")).append('\n');
            }

            if (mappingValidator.specificValidators != null && !mappingValidator.specificValidators.isEmpty()) {
                int longest = findLongestString(mappingValidator.specificValidators.keySet());
                String innerSpaces = spaces + repeat(longest) + "    ";
                builder.append(spaces).append("specificValidator={\n");
                Iterator var7 = mappingValidator.specificValidators.entrySet().iterator();

                while(var7.hasNext()) {
                    Map.Entry<String, NodeValidator> entry = (Map.Entry)var7.next();
                    builder.append(spaces).append("  ").append(padRight((String)entry.getKey(), longest)).append(" -> ").append(toString((NodeValidator)entry.getValue(), innerSpaces + "  ")).append('\n');
                }

                builder.append(spaces).append("}\n");
            }

            builder.append(rootSpaces).append('}');
        } else {
            builder.append(validator.toString());
        }

        return builder.toString();
    }

    public String toString() {
        return toString(this, "");
    }
}
