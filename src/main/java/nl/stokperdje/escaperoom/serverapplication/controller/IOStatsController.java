package nl.stokperdje.escaperoom.serverapplication.controller;

import nl.stokperdje.escaperoom.serverapplication.dto.IOStats;
import nl.stokperdje.escaperoom.serverapplication.service.SessionService;
import nl.stokperdje.escaperoom.serverapplication.service.WebSocketService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping(value = "/iostats")
public class IOStatsController {

    @Autowired
    private WebSocketService ws;

    @Autowired
    private SessionService service;

    private RestTemplate restTemplate = new RestTemplate();

    @PostMapping(consumes = "application/json")
    public ResponseEntity postIOStats(@RequestBody IOStats ioStats) {
        System.out.println(ioStats);
        ws.broadcast("iostats", ioStats);

        // Als knop ingedrukt is
        if (ioStats.isDrukknop()) {
            service.performPressButtonActions();
        }

        // Als knoppen combinatie goed is
        if (ioStats.isSchakelkast()) {
            service.performAlarmCodeCorrectActions();
        }

//        if (ioStats.isSensor1()) {
//            service.performLaserCrossedActions();
//        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getIOStats() {
        try {
            // IOStats ophalen
            String url = "http://192.168.2.223:8082/iostats";
            return new ResponseEntity<>(restTemplate.getForEntity(url, String.class), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

}
