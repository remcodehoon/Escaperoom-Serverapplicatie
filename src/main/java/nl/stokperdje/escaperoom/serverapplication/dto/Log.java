package nl.stokperdje.escaperoom.serverapplication.dto;

import com.google.gson.annotations.Expose;
import lombok.Value;
import nl.stokperdje.escaperoom.serverapplication.domain.EscaperoomSessie;

@Value
public class Log {

    @Expose private int hours;
    @Expose private int minutes;
    @Expose private int seconds;

    @Expose private String message;

    public static Log create(EscaperoomSessie session, String message) {
        return new Log(session.getHours(), session.getMinutes(), session.getSeconds(), message);
    }
}
