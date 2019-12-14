package nl.stokperdje.escaperoom.serverapplication.controller;

import nl.stokperdje.escaperoom.serverapplication.dto.IOStats;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/iostats")
public class IOStatsController {

    @PostMapping(consumes = "application/json")
    public ResponseEntity postIOStats(@RequestBody IOStats ioStats) {
        System.out.println(ioStats);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
