package nl.stokperdje.escaperoom.serverapplication.helpers;

import nl.stokperdje.escaperoom.serverapplication.domain.EscaperoomSessie;
import nl.stokperdje.escaperoom.serverapplication.service.SessionService;
import nl.stokperdje.escaperoom.serverapplication.service.WebSocketService;

import java.util.TimerTask;

public class SessionTimerTask extends TimerTask {

    private EscaperoomSessie session;
    private SessionService sessionService;
    private WebSocketService ws;
    private boolean isStarted;
    private boolean isCancelled = false;

    public SessionTimerTask(SessionService sessionService, EscaperoomSessie session, WebSocketService ws) {
        super();
        this.session = session;
        this.sessionService = sessionService;
        this.ws = ws;
    }

    public boolean isStarted() {
        return this.isStarted;
    }

    @Override
    public void run() {
        this.isStarted = true;
        EscaperoomSessie session = this.session;
        int hours = session.getHours();
        int minutes = session.getMinutes();
        int seconds = session.getSeconds();
        session.countSecond();

        if (hours >= 1 && minutes == 0 && seconds == 0) {
            sessionService.setTime(hours - 1, 59, 59);
        } else if (minutes > 0 && seconds == 0) {
            sessionService.setTime(hours, minutes - 1, 59);
        } else if (seconds > 0) {
            sessionService.setTime(hours, minutes, seconds - 1);
        } else {
            ws.log(this.session, "Tijd voorbij! Sessie beeïndigd.");
            sessionService.setActive(false);
            sessionService.setStopped(true);
            ws.broadcast("sessie", this.session);
            this.cancel();
        }
    }

    @Override
    public boolean cancel() {
        if (!isCancelled) {
            this.isCancelled = true;
            return super.cancel();
        }
        return false;
    }
}
