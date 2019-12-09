package nl.stokperdje.escaperoom.serverapplication.helpers;

import nl.stokperdje.escaperoom.serverapplication.domain.EscaperoomSessie;
import nl.stokperdje.escaperoom.serverapplication.exceptions.BarcodeAlreadyScannedException;
import nl.stokperdje.escaperoom.serverapplication.exceptions.ButtonNotPressedException;
import nl.stokperdje.escaperoom.serverapplication.exceptions.NoActiveSessionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BarcodeHelper {

    private HashMap<String, Integer> barcodeValues;
    private List<String> scannedCodes;

    public BarcodeHelper() {
        barcodeValues = new HashMap<>();
        scannedCodes = new ArrayList<>();

        barcodeValues.put("code1", 100);
        barcodeValues.put("code2", 200);
        barcodeValues.put("code3", -50);
    }

    /**
     * Telt de waarde van de barcode bij de buit van de sessie op als de sessie actief is
     * en de barcode nog niet eerder is gescand.
     * @param barcode De gescande barcode
     * @param session De huidige sessie instantie
     * @throws BarcodeAlreadyScannedException Wanneer barcode al een keer gescand is in deze sessie
     * @throws NoActiveSessionException Wanneer de sessie nog niet actief is
     * @throws ButtonNotPressedException Wanneer de knop nog niet ingedrukt is geweest
     */
    public int scanCode(String barcode, EscaperoomSessie session)
            throws BarcodeAlreadyScannedException, NoActiveSessionException, ButtonNotPressedException {
        if (session.isActive() && !session.isStopped()) {
            if (session.isButtonPressed()) {
                if (!scannedCodes.contains(barcode)) {
                    if (this.barcodeValues.containsKey(barcode)) {
                        scannedCodes.add(barcode);
                        return Integer.sum(session.getBuit(), barcodeValues.get(barcode));
                    }
                } else {
                    throw new BarcodeAlreadyScannedException("Barcode [" + barcode + "] is al een keer gescand.");
                }
            } else {
                throw new ButtonNotPressedException("De grote rode knop moet ingedrukt zijn, voordat je kunt scannen!");
            }
        } else {
            throw new NoActiveSessionException();
        }
        return 0;
    }

    /**
     * Clears list of scanned codes
     */
    public void reset() {
        this.scannedCodes.clear();
    }
}
