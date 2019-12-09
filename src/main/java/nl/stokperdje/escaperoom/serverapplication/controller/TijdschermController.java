package nl.stokperdje.escaperoom.serverapplication.controller;

import nl.stokperdje.escaperoom.serverapplication.converter.TimeChangeTypeConverter;
import nl.stokperdje.escaperoom.serverapplication.dto.TimeChange;
import nl.stokperdje.escaperoom.serverapplication.helpers.TijdHelper;
import nl.stokperdje.escaperoom.serverapplication.service.SessionService;
import nl.stokperdje.escaperoom.serverapplication.statics.pinslot.TimeChangeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Controller
@CrossOrigin
@RequestMapping(value = "/tijdscherm")
@Validated
public class TijdschermController {

    @Autowired
    SessionService service;

    @Validated
    @GetMapping("/tijd/{action}/{hours}/{minutes}/{seconds}")
    public ResponseEntity adjustTime(
            @PathVariable TimeChangeType action,
            @PathVariable @Min(0) @Max(1) int hours,
            @PathVariable @Min(0) @Max(59) int minutes,
            @PathVariable @Min(0) @Max(59) int seconds
    ) {
        TimeChange adjustment = new TimeChange(action, hours, minutes, seconds);
        service.changeTime(adjustment);
        return new ResponseEntity<>(adjustment, HttpStatus.OK);
    }

    @GetMapping("/tijd/hervatten")
    public ResponseEntity startTimer() {
        service.startTimer();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/tijd/pauzeren")
    public ResponseEntity pauseTimer() {
        service.pauseTimer();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @InitBinder
    public void initBinder(final WebDataBinder webdataBinder) {
        webdataBinder.registerCustomEditor(TimeChangeType.class, new TimeChangeTypeConverter());
    }

}
