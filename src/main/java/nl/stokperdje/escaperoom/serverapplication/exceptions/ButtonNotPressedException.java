package nl.stokperdje.escaperoom.serverapplication.exceptions;

public class ButtonNotPressedException extends Exception {

    public ButtonNotPressedException() {
        super();
    }

    public ButtonNotPressedException(String message) {
        super(message);
    }
}
