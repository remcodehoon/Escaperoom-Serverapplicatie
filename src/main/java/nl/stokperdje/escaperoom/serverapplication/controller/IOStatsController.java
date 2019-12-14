package nl.stokperdje.escaperoom.serverapplication.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.stokperdje.escaperoom.serverapplication.dto.IOStats;

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

    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    @PostMapping(consumes = "application/json")
    public ResponseEntity postIOStats(@RequestBody IOStats ioStats) {
        System.out.println(ioStats);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
