package nl.stokperdje.escaperoom.serverapplication.controller;

import nl.stokperdje.escaperoom.serverapplication.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/rook")
public class RookController {

    @Autowired
    private SessionService service;

    @GetMapping(value = "/aan")
    public ResponseEntity turnOn() {
        service.turnRookOn();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/uit")
    public ResponseEntity turnOff() {
        service.turnRookOff();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/toggle")
    public ResponseEntity toggle() {
        service.toggleRook();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
