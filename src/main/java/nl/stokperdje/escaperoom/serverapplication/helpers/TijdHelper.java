package nl.stokperdje.escaperoom.serverapplication.helpers;

import com.google.gson.Gson;
import nl.stokperdje.escaperoom.serverapplication.domain.EscaperoomSessie;
import nl.stokperdje.escaperoom.serverapplication.dto.TimeChange;
import nl.stokperdje.escaperoom.serverapplication.service.SessionService;
import nl.stokperdje.escaperoom.serverapplication.service.WebSocketService;
import nl.stokperdje.escaperoom.serverapplication.statics.pinslot.TimeChangeType;

import java.util.Timer;

public class TijdHelper {

    private final String websocketTopic = "tijd";
    private Timer timer = new Timer();
    private SessionTimerTask timerTask;

    private EscaperoomSessie session;
    private SessionService sessionService;
    private WebSocketService ws;

    public TijdHelper(SessionService sessionService, EscaperoomSessie session, WebSocketService ws) {
        this.sessionService = sessionService;
        this.session = session;
        this.ws = ws;
    }

    /**
     * Wijzigt de tijd zoals aangegeven, stuurt een broadcast naar listeners en wijzigt de tijd in de sessie
     * @param adjustment Attribuut met het type wijziging en het aantal tijdseenheden
     */
    public void changeTime(TimeChange adjustment) {
        // Broadcast
        Gson gson = new Gson();
        System.out.println(gson.toJson(adjustment).toLowerCase());
        ws.broadcast(this.websocketTopic, adjustment);

        // Log + Session
        TimeChangeType type = adjustment.getType();

        int hours = session.getHours();
        int minutes = session.getMinutes();
        int seconds = session.getSeconds();

        if (type.equals(TimeChangeType.MINUS)) {
            ws.log(this.session,
                    "Tijd in mindering gebracht: " +
                    adjustment.getHours() + " uren, " +
                    adjustment.getMinutes() + " minuten, " +
                    adjustment.getSeconds() + " seconden"
            );
            session.setTime(
                    hours - adjustment.getHours(),
                    minutes - adjustment.getMinutes(),
                    seconds - adjustment.getSeconds()
            );
        } else if (type.equals(TimeChangeType.PLUS)) {
            ws.log(this.session,
                    "Tijd toegevoegd: " +
                            adjustment.getHours() + " uren, " +
                            adjustment.getMinutes() + " minuten, " +
                            adjustment.getSeconds() + " seconden"
            );
            session.setTime(
                    hours + adjustment.getHours(),
                    minutes + adjustment.getMinutes(),
                    seconds + adjustment.getSeconds()
            );
        } else if (type.equals(TimeChangeType.SET)) {
            ws.log(this.session,
                    "Tijd ingesteld: " +
                            adjustment.getHours() + " uren, " +
                            adjustment.getMinutes() + " minuten, " +
                            adjustment.getSeconds() + " seconden"
            );
            session.setTime(
                    adjustment.getHours(),
                    adjustment.getMinutes(),
                    adjustment.getSeconds()
            );
        } else if (type.equals(TimeChangeType.RESET)) {
            ws.log(this.session,
                    "Tijd gereset: " +
                            adjustment.getHours() + " uren, " +
                            adjustment.getMinutes() + " minuten, " +
                            adjustment.getSeconds() + " seconden"
            );
            session.setTime(1, 0, 0);
        }
    }

    /**
     * Start de timer en logt de gebeurtenis
     */
    public void startTimer() {
        if (this.timerTask != null && this.timerTask.isStarted()) {
            this.timerTask.cancel();
            this.timer.cancel();
            this.timer.purge();
        }
        this.session.setStopped(false);
        this.timer = new Timer();
        this.timerTask = this.getTimerTask();
        timer.schedule(this.timerTask, 0, 1000);
        ws.log(this.session,"Timer gestart");
    }

    /**
     * Pauzeert de timer en logt de gebeurtenis
     */
    public void pauseTimer() {
        timer.cancel();
        this.session.setStopped(true);
        ws.log(this.session,"Timer gepauzeerd");
    }

    /**
     * Returnt een timertask voor het aftellen van de ingestelde tijd
     * @return TimerTask
     */
    private SessionTimerTask getTimerTask() {
        return new SessionTimerTask(this.sessionService, this.session, this.ws);
    }

}
