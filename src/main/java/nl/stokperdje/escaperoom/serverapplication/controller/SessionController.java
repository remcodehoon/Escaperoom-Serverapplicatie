package nl.stokperdje.escaperoom.serverapplication.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.stokperdje.escaperoom.serverapplication.dto.TimeChange;
import nl.stokperdje.escaperoom.serverapplication.service.SessionService;
import nl.stokperdje.escaperoom.serverapplication.statics.pinslot.TimeChangeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin
@RequestMapping(value = "/session")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @GetMapping(value = "")
    public ResponseEntity getSession() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return new ResponseEntity<>(gson.toJson(sessionService.getSession()), HttpStatus.OK);
    }

    @GetMapping(value = "/new/{teamName}")
    public ResponseEntity newSession(@PathVariable String teamName) {
        sessionService.createNewSession(teamName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/start")
    public ResponseEntity startSession() {
        System.out.println("Session start!");
        sessionService.startSession();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/stop")
    public ResponseEntity stopSession() {
        System.out.println("Session Stopped!");
        sessionService.stopSession();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/timechange")
    public ResponseEntity timechange() {
        TimeChange change = new TimeChange(TimeChangeType.SET, 0, 50, 20);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return new ResponseEntity<>(gson.toJson(change), HttpStatus.OK);
    }
}
