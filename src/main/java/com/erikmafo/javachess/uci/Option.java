package com.erikmafo.javachess.uci;

/**
 * Created by erikf on 14.07.2016.
 */
public class Option {

    public static class Type {
        public static final String CHECK = "check";
        public static final String SPIN = "spin";
        public static final String COMBO = "combo";
        public static final String BUTTON = "button";
        public static final String STRING = "string";
    }

    private final String name;
    private final String valueAsString;
    private final String defaultValue;
    private final String type;

    public Option(String name, String valueAsString, String defaultValue, String type) {
        this.name = name;
        this.valueAsString = valueAsString;
        this.defaultValue = defaultValue;
        this.type = type;
    }

    public String getValueAsString() {
        return valueAsString;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Option)) return false;

        Option option = (Option) o;

        if (name != null ? !name.equals(option.name) : option.name != null) return false;
        if (valueAsString != null ? !valueAsString.equals(option.valueAsString) : option.valueAsString != null)
            return false;
        return type != null ? type.equals(option.type) : option.type == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (valueAsString != null ? valueAsString.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
