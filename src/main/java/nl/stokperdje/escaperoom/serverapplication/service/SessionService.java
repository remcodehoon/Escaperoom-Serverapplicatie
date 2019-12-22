package nl.stokperdje.escaperoom.serverapplication.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.stokperdje.escaperoom.serverapplication.domain.EscaperoomSessie;
import nl.stokperdje.escaperoom.serverapplication.dto.Buit;
import nl.stokperdje.escaperoom.serverapplication.dto.Message;
import nl.stokperdje.escaperoom.serverapplication.dto.Status;
import nl.stokperdje.escaperoom.serverapplication.dto.TimeChange;
import nl.stokperdje.escaperoom.serverapplication.exceptions.BarcodeAlreadyScannedException;
import nl.stokperdje.escaperoom.serverapplication.exceptions.ButtonNotPressedException;
import nl.stokperdje.escaperoom.serverapplication.exceptions.NoActiveSessionException;
import nl.stokperdje.escaperoom.serverapplication.helpers.BarcodeHelper;
import nl.stokperdje.escaperoom.serverapplication.helpers.TijdHelper;
import nl.stokperdje.escaperoom.serverapplication.statics.pinslot.TimeChangeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SessionService {

    @Autowired
    private WebSocketService ws;

    private final String SESSION_TOPIC = "sessie";
    private final String TIME_TOPIC = "tijd";
    private final String BUIT_TOPIC = "buit";
    private final String PINSLOT_TOPIC = "pinslot";
    private final String BUTTON_TOPIC = "button";
    private final String ALARM_TOPIC = "alarm";
    private final String SENSOR_TOPIC = "sensor";

    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private BarcodeHelper barcodeHelper = new BarcodeHelper();
    private TijdHelper tijdHelper;
    private RestTemplate restTemplate = new RestTemplate();

    private EscaperoomSessie session;

    public void createNewSession(String teamName) {
        if (this.tijdHelper != null) {
            this.tijdHelper.pauseTimer();
        }
        System.out.println("Nieuwe sessie: " + teamName);
        this.session = new EscaperoomSessie(teamName);
        tijdHelper = new TijdHelper(this, session, ws);
        barcodeHelper.reset();

        ws.sendMessage(this.session, new Message(false, ""));
        ws.log(this.session, "Nieuwe sessie: Teamnaam " + teamName);
        ws.broadcast(SESSION_TOPIC, this.session);
        ws.broadcast(BUIT_TOPIC, new Buit(0));
        ws.broadcast(TIME_TOPIC, new TimeChange(TimeChangeType.SET, 1, 0, 0));

        try {
            // Verlichting uit zetten
            String url1 = "http://192.168.2.223:8082/verlichting/hoofdverlichting/uit";
            restTemplate.getForEntity(url1, String.class);

            // Knop LED uit zetten
            String url2 = "http://192.168.2.223:8082/verlichting/knopled/uit";
            restTemplate.getForEntity(url2, String.class);

            // Lasers aan zetten
            String url3 = "http://192.168.2.223:8082/lasers/aan";
            restTemplate.getForEntity(url3, String.class);

            // Lasers aan zetten
            String url4 = "http://192.168.2.223:8082/rook/aan";
            restTemplate.getForEntity(url4, String.class);
        } catch (Exception ignored) {}
    }

    public void startSession() {
        this.setActive(true);
        this.setStopped(false);
        this.tijdHelper.startTimer();
        ws.broadcast(SESSION_TOPIC, this.session);
    }

    public void stopSession() {
        this.setActive(false);
        this.setStopped(true);
        ws.log(this.session, "Sessie beeïndigd.");
        ws.broadcast(SESSION_TOPIC, this.session);
        this.tijdHelper.pauseTimer();

        try {
            // Verlichting uit zetten
            String url1 = "http://192.168.2.223:8082/verlichting/hoofdverlichting/uit";
            restTemplate.getForEntity(url1, String.class);

            // Knop LED uit zetten
            String url2 = "http://192.168.2.223:8082/verlichting/knopled/uit";
            restTemplate.getForEntity(url2, String.class);

            // Lasers uit zetten
            String url3 = "http://192.168.2.223:8082/lasers/uit";
            restTemplate.getForEntity(url3, String.class);

            // Rook uit zetten
            String url4 = "http://192.168.2.223:8082/rook/uit";
            restTemplate.getForEntity(url4, String.class);
        } catch (Exception ignored) {}
    }

    public void startTimer() {
        if (this.session != null && this.tijdHelper != null && this.session.isActive()) {
            this.tijdHelper.startTimer();
            ws.broadcast(SESSION_TOPIC, this.session);
        }
    }

    public void pauseTimer() {
        if (this.session != null && this.tijdHelper != null && this.session.isActive()) {
            this.tijdHelper.pauseTimer();
            ws.broadcast(SESSION_TOPIC, this.session);
        }
    }

    public EscaperoomSessie getSession() {
        return this.session;
    }

    public void performPressButtonActions() {
        if (this.session != null && this.session.isActive() && !session.isButtonPressed()) {
            // Set isButtonPressed true, zodat gescand kan worden
            this.session.pressButton();
            ws.broadcast(SESSION_TOPIC, this.session);

            // Informatiescherm op de hoogte stellen, zodat filmpje afspeeld
            ws.broadcast(BUTTON_TOPIC, Status.on());

            // Loggen
            ws.log(
                    this.session,
                    "Knop ingedrukt: De deur naar buiten is geopend en er kan nu gescand worden"
            );

            // Tijd aanpassen indien minuten >= 7
            if (this.session.getHours() >= 0 && this.session.getMinutes() >=7) {
                this.setTime(0, 7, 32);
            }

            try {
                // Knop LED aanzetten
                String url = "http://192.168.2.223:8082/verlichting/knopled/aan";
                restTemplate.getForEntity(url, String.class);
            } catch (Exception ignored) {}

            // Slot openen
            this.openSlot(false);
        }
    }

    public void performLaserCrossedActions() {
        if (this.session != null && session.isActive() && !this.session.isAlarmCodeCorrect()) {
            ws.log(this.session, "Laser onderbroken");
            tijdHelper.changeTime(new TimeChange(TimeChangeType.MINUS, 0, 1, 0));
            if (!this.session.isLasersCrossed()) {
                this.session.crossLasers();
                ws.broadcast(SENSOR_TOPIC, Status.on());
            }
        }
    }

    public void performAlarmCodeCorrectActions() {
        if (this.session != null && this.session.isActive() && !session.isAlarmCodeCorrect()) {
            this.session.setAlarmCodeCorrect();
            ws.broadcast(SESSION_TOPIC, this.session);

            // Informatiescherm op de hoogte stellen, zodat filmpje afspeeld
            ws.broadcast(ALARM_TOPIC, Status.on());

            // Loggen
            ws.log(
                    this.session,
                    "Alarm uitgeschakeld"
            );

            try {
                // Lasers uit zetten
                String url1 = "http://192.168.2.223:8082/lasers/uit";
                restTemplate.getForEntity(url1, String.class);

                // Verlichting aan zetten
                String url2 = "http://192.168.2.223:8082/verlichting/hoofdverlichting/aan";
                restTemplate.getForEntity(url2, String.class);

                // Rook uit zetten
                String url3 = "http://192.168.2.223:8082/rook/uit";
                restTemplate.getForEntity(url3, String.class);
            } catch (Exception ignored) {}
        }
    }

    public void scanCode(String code)
            throws NoActiveSessionException, BarcodeAlreadyScannedException, ButtonNotPressedException
    {
        if (this.session != null && this.session.isActive()) {
            Buit totaleBuit = new Buit(barcodeHelper.scanCode(code, this.session));
            System.out.println(gson.toJson(totaleBuit));
            ws.log(
                    this.session,
                    "Barcode gescand: Buit gewijzigd van " + session.getBuit() + " naar " + totaleBuit.getTotaleBuit()
            );
            session.setBuit(totaleBuit.getTotaleBuit());
            ws.broadcast(BUIT_TOPIC, totaleBuit);
        }
    }

    public void resetBarcodes() {
        barcodeHelper.reset();
    }

    public void setActive(boolean active) {
        if (this.session != null) {
            this.session.setActive(active);
        }
    }

    public void setStopped(boolean stopped) {
        if (this.session != null) {
            this.session.setStopped(stopped);
        }
    }

    public void sendMessage(Message message) {
        if (this.session != null && this.session.isActive()) {
            ws.sendMessage(this.session, message);
        } else {
            ws.sendMessage(null, message);
        }
    }

    public void changeTime(TimeChange adjustment) {
        if (this.tijdHelper != null) {
            this.tijdHelper.changeTime(adjustment);
        }
    }

    public void setTime(int hours, int minutes, int seconds) {
        if (session != null) {
            session.setTime(hours, minutes, seconds);
            TimeChange change = new TimeChange(TimeChangeType.SET, hours, minutes, seconds);
            ws.broadcast(TIME_TOPIC, change);
        }
    }

    public void openSlot(boolean byController) {
        if (session != null) {
            if (byController) {
                ws.log(this.session, "Slot geopend door controlroom");
            } else {
                ws.log(this.session, "Slot geopend door spelers");
            }
        }
        ws.broadcast(PINSLOT_TOPIC, Status.open());

        try {
            // Slot openen
            String url1 = "http://192.168.2.223:8082/slot/open";
            restTemplate.getForEntity(url1, String.class);
        } catch (Exception ignored) {}
    }

    public void turnLasersOn() {
        try {
            // Lasers aan
            String url1 = "http://192.168.2.223:8082/lasers/aan";
            restTemplate.getForEntity(url1, String.class);
        } catch (Exception ignored) {}
    }

    public void turnLasersOff() {
        try {
            // Lasers aan
            String url1 = "http://192.168.2.223:8082/lasers/uit";
            restTemplate.getForEntity(url1, String.class);
        } catch (Exception ignored) {}
    }

    public void closeSlot() {
        if (session != null) {
            ws.log(this.session, "Slot gesloten door controlroom");
        }
        ws.broadcast(PINSLOT_TOPIC, Status.closed());

        try {
            // Slot sluiten
            String url1 = "http://192.168.2.223:8082/slot/dicht";
            restTemplate.getForEntity(url1, String.class);
        } catch (Exception ignored) {}
    }
}
