package nl.stokperdje.escaperoom.serverapplication.helpers;

import nl.stokperdje.escaperoom.serverapplication.domain.EscaperoomSessie;
import nl.stokperdje.escaperoom.serverapplication.dto.Status;
import nl.stokperdje.escaperoom.serverapplication.service.SessionService;
import nl.stokperdje.escaperoom.serverapplication.service.WebSocketService;
import org.springframework.web.client.RestTemplate;

import java.util.TimerTask;

public class SessionTimerTask extends TimerTask {

    private EscaperoomSessie session;
    private SessionService sessionService;
    private WebSocketService ws;
    private boolean isStarted;
    private boolean isCancelled = false;
    private RestTemplate restTemplate = new RestTemplate();

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

        // Wanneer 1 minuut (en 15 seconden ivm lengte filmpje) broadcast
        if (hours == 0 && minutes == 1 && seconds == 15) {
            ws.broadcast("lastminute", Status.on());
        }

        if (hours >= 1 && minutes == 0 && seconds == 0) {
            sessionService.setTime(hours - 1, 59, 59);
        } else if (minutes > 0 && seconds == 0) {
            sessionService.setTime(hours, minutes - 1, 59);
        } else if (seconds > 0) {
            sessionService.setTime(hours, minutes, seconds - 1);
        } else {
            sessionService.stopSession();
            this.cancel();
        }

        if (seconds % 10 == 0 && !session.isAlarmCodeCorrect()) {
            // Rook togglen
            try {
                String url = "http://192.168.2.223:8082/rook/toggle";
                restTemplate.getForEntity(url, String.class);
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
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
