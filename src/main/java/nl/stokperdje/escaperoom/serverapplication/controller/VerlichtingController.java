package nl.stokperdje.escaperoom.serverapplication.controller;

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
@RequestMapping(value = "/verlichting")
public class VerlichtingController {

    @Autowired
    private SessionService service;

    @GetMapping(value = "/hoofdverlichting/aan")
    public ResponseEntity turnLightsOn() {
        service.turnLightsOn();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/hoofdverlichting/uit")
    public ResponseEntity turnLightsOff() {
        service.turnLightsOff();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
