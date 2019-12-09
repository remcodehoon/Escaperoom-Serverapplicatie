package nl.stokperdje.escaperoom.serverapplication.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.stokperdje.escaperoom.serverapplication.domain.EscaperoomSessie;
import nl.stokperdje.escaperoom.serverapplication.dto.Log;
import nl.stokperdje.escaperoom.serverapplication.dto.Message;
import nl.stokperdje.escaperoom.serverapplication.exceptions.NoActiveSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;
    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    /**
     * Voegt een log toe aan de sessie en broadcast deze naar alle listeners
     * @param session De huidige escape room sessie
     * @param message Het te loggen bericht
     */
    public void log(EscaperoomSessie session, String message) {
        Log log = Log.create(session, message);
        session.addLog(log);
        broadcast("logs", log);
    }

    /**
     * Broadcast een object naar alle listeners van het meegegeven topic
     * @param topic De naam van het topic
     * @param o Het te broadcasten object
     */
    public void broadcast(String topic, Object o) {
        messagingTemplate.convertAndSend("/topic/" + topic, gson.toJson(o));
    }

    /**
     * Stuurt een bericht naar het escape room scherm
     * @param session De huidige escape room sessie
     * @param message Het te zenden bericht
     */
    public void sendMessage(EscaperoomSessie session, Message message) {
        if (session != null && session.isActive()) {
            if (!message.getMessage().equals("")) {
                log(session, "Bericht geplaatst: " + message.getMessage());
            }
        }
        broadcast("berichten", message);
    }
}
