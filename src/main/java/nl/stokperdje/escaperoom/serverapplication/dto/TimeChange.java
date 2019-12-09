package nl.stokperdje.escaperoom.serverapplication.dto;

import com.google.gson.annotations.Expose;
import lombok.Value;
import nl.stokperdje.escaperoom.serverapplication.statics.pinslot.TimeChangeType;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Value
public class TimeChange {

    @Expose
    @NotNull
    private TimeChangeType type;

    @Expose
    @Min(message = "Hours can only be 0 or 1", value = 0)
    @Max(message = "Hours can only be 0 or 1", value = 1)
    private int hours;

    @Expose
    @Min(message = "Minutes can only be between 0 or 59", value = 0)
    @Max(message = "Minutes can only be between 0 or 59", value = 59)
    private int minutes;

    @Expose
    @Min(message = "Seconds can only be between 0 or 59", value = 0)
    @Max(message = "Seconds can only be between 0 or 59", value = 59)
    private int seconds;
}
