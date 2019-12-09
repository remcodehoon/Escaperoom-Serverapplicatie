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

@Service
public class SessionService {

    @Autowired
    private WebSocketService ws;

    private final String SESSION_TOPIC = "sessie";
    private final String TIME_TOPIC = "tijd";
    private final String BUIT_TOPIC = "buit";
    private final String PINSLOT_TOPIC = "pinslot";

    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private BarcodeHelper barcodeHelper = new BarcodeHelper();
    private TijdHelper tijdHelper;

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
    }

    public void startSession() {
        this.setActive(true);
        this.setStopped(false);
        this.tijdHelper.startTimer();
        ws.broadcast(SESSION_TOPIC, this.session);
    }

    public void stopSession() {
        ws.log(this.session, "Sessie beeïndigd.");
        this.tijdHelper.pauseTimer();
        this.setActive(false);
        this.setStopped(true);
        ws.broadcast(SESSION_TOPIC, this.session);
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

    public void scanCode(String code)
            throws NoActiveSessionException, BarcodeAlreadyScannedException, ButtonNotPressedException
    {
        if (this.session != null && this.session.isActive()) {
            Buit totaleBuit = new Buit(barcodeHelper.scanCode(code, this.session));
            ws.log(
                    this.session,
                    "Barcode gescand: Buit gewijzigd van " + session.getBuit() + " naar " + totaleBuit.getTotaleBuit()
            );
            session.setBuit(totaleBuit.getTotaleBuit());
            ws.broadcast(BUIT_TOPIC, gson.toJson(totaleBuit));
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
        // Todo: Pinslot aansturen
    }

    public void closeSlot() {
        if (session != null) {
            ws.log(this.session, "Slot gesloten door controlroom");
        }
        ws.broadcast(PINSLOT_TOPIC, Status.closed());
        // Todo: Pinslot aansturen
    }
}
