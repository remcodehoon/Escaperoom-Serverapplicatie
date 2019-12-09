package nl.stokperdje.escaperoom.serverapplication.dto;

import com.google.gson.annotations.Expose;
import lombok.Value;

@Value
public class Message {

    @Expose private boolean show;
    @Expose private String message;

}
