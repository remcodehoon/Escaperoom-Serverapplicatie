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

        // CAT 1
        barcodeValues.put("C1", -9999);
        barcodeValues.put("C2", 25700);
        barcodeValues.put("C3", 39100);
        barcodeValues.put("C4", 28000);
        barcodeValues.put("C5", 30500);
        barcodeValues.put("C6", 22000);
        barcodeValues.put("C7", 32900);
        barcodeValues.put("C8", 26250);
        barcodeValues.put("C9", 30800);
        barcodeValues.put("C10", 27000);
        barcodeValues.put("C11", 39250);
        barcodeValues.put("C12", 24000);
        barcodeValues.put("C13", 41000);
        barcodeValues.put("C14", 23500);
        barcodeValues.put("C15", 35000);

        // CAT 2
        barcodeValues.put("C16", 54000);
        barcodeValues.put("C17", 88570);
        barcodeValues.put("C18", 62000);
        barcodeValues.put("C19", 79300);
        barcodeValues.put("C20", 86000);
        barcodeValues.put("C21", 87200);
        barcodeValues.put("C22", 73900);
        barcodeValues.put("C23", 67430);
        barcodeValues.put("C24", 59200);
        barcodeValues.put("C25", 66100);
        barcodeValues.put("C26", 89000);
        barcodeValues.put("C27", 84300);
        barcodeValues.put("C28", 75400);
        barcodeValues.put("C29", 63900);
        barcodeValues.put("C30", 74700);
        barcodeValues.put("C31", 83660);
        barcodeValues.put("C32", 54340);
        barcodeValues.put("C33", 65000);

        // CAT 3
        barcodeValues.put("C34", 7450);
        barcodeValues.put("C35", 4550);
        barcodeValues.put("C36", 5200);
        barcodeValues.put("C37", 4900);
        barcodeValues.put("C38", 3900);
        barcodeValues.put("C39", 9800);
        barcodeValues.put("C40", 5200);
        barcodeValues.put("C41", 4850);
        barcodeValues.put("C42", 7450);

        // CAT 4
        barcodeValues.put("C43", 550);
        barcodeValues.put("C44", 350);
        barcodeValues.put("C45", 410);

        // CAT 5
        barcodeValues.put("C46", 4350);
        barcodeValues.put("C47", 2600);
        barcodeValues.put("C48", 3400);
        barcodeValues.put("C49", 1500);
        barcodeValues.put("C50", 1200);
        barcodeValues.put("C51", 3600);
        barcodeValues.put("C52", 3800);
        barcodeValues.put("C53", 1950);

        // CAT 6
        barcodeValues.put("C54", 1250);
        barcodeValues.put("C55", 1250);
        barcodeValues.put("C56", 1250);
        barcodeValues.put("C57", 1250);
        barcodeValues.put("C58", 1250);
        barcodeValues.put("C59", 1250);
        barcodeValues.put("C60", 1250);
        barcodeValues.put("C61", 1250);

        // CAT 7
        barcodeValues.put("C62", 54000);
        barcodeValues.put("C63", 3250);
        barcodeValues.put("C64", 120);
        barcodeValues.put("C65", 120);
        barcodeValues.put("C66", 2800);
        barcodeValues.put("C67", 2800);
        barcodeValues.put("C68", 2800);
        barcodeValues.put("C69", 9000);
        barcodeValues.put("C70", 1500);
        barcodeValues.put("C71", 1500);
        barcodeValues.put("C72", 38500);
        barcodeValues.put("C73", 4600);
        barcodeValues.put("C74", 4200);
        barcodeValues.put("C75", 7300);
        barcodeValues.put("C76", 8500);
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
                        if (barcodeValues.get(barcode) == -9999) {
                            this.reset();
                            scannedCodes.add(barcode);
                            return 0;
                        } else {
                            scannedCodes.add(barcode);
                            return Integer.sum(session.getBuit(), barcodeValues.get(barcode));
                        }
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
