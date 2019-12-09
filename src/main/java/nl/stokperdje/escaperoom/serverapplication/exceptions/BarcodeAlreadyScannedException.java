package nl.stokperdje.escaperoom.serverapplication.exceptions;

public class BarcodeAlreadyScannedException extends Exception {

    public BarcodeAlreadyScannedException(String message) {
        super(message);
    }
}
