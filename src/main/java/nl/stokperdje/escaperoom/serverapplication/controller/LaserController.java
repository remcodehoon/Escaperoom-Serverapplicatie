package nl.stokperdje.escaperoom.serverapplication.controller;

import nl.stokperdje.escaperoom.serverapplication.dto.Status;
import nl.stokperdje.escaperoom.serverapplication.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin
@RequestMapping(value = "/lasers")
public class LaserController {

    @Autowired
    private SessionService service;

    @GetMapping(value = "/aan")
    public ResponseEntity turnLasersOn() {
        service.turnLasersOn();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/uit")
    public ResponseEntity turnLasersOff() {
        service.turnLasersOff();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
