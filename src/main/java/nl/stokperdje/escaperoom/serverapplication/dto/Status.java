package nl.stokperdje.escaperoom.serverapplication.dto;

import com.google.gson.annotations.Expose;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public class Status {

    /**
     * Status false = open. Status true = closed
     */
    @NotNull
    @Expose
    private boolean status;

    public static Status open() {
        return new Status(false);
    }

    public static Status closed() {
        return new Status(true);
    }

    public static Status on() {
        return new Status(true);
    }

    public static Status off() {
        return new Status(false);
    }

}
