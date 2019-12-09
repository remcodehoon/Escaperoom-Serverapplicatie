package nl.stokperdje.escaperoom.serverapplication.statics.pinslot;

import com.google.gson.annotations.Expose;

import java.util.Arrays;

public enum TimeChangeType {

    MINUS("minus"),
    PLUS("plus"),
    SET("set"),
    RESET("reset");

    @Expose
    private String type;

    TimeChangeType(String type) {
        this.type = type;
    }

    public static TimeChangeType fromValue(String value) {
        for (TimeChangeType changeType : values()) {
            if (changeType.type.equalsIgnoreCase(value)) {
                return changeType;
            }
        }
        throw new IllegalArgumentException(
                "Unknown enum type " + value + ", allowed values are " + Arrays.toString(values())
        );
    }
}
