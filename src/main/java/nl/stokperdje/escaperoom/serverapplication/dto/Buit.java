package nl.stokperdje.escaperoom.serverapplication.dto;

import com.google.gson.annotations.Expose;
import lombok.Value;

@Value
public class Buit {

    @Expose
    private int totaleBuit;

    @Override
    public String toString() {
        return totaleBuit + " euro";
    }

}
