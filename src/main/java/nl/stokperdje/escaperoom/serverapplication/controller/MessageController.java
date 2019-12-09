package nl.stokperdje.escaperoom.serverapplication.controller;

import nl.stokperdje.escaperoom.serverapplication.dto.Message;
import nl.stokperdje.escaperoom.serverapplication.service.SessionService;
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
@RequestMapping(value = "/message")
public class MessageController {

    @Autowired
    private SessionService sessionService;

    @GetMapping(value = "/show/{message}")
    public ResponseEntity showMessage(@PathVariable String message) {
        sessionService.sendMessage(new Message(true, message));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/hide")
    public ResponseEntity hideMessage() {
        sessionService.sendMessage(new Message(false, ""));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
