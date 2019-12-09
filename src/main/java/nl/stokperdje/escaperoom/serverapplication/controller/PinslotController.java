package nl.stokperdje.escaperoom.serverapplication.controller;

import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@RequestMapping(value = "/pinslot")
public class PinslotController {

    private boolean status = false;

    @Autowired
    private SessionService service;

    @GetMapping
    public ResponseEntity getStatus() {

        // Todo: Status ophalen bij Raspberry

        return new ResponseEntity<>(new Status(this.status), HttpStatus.OK);
    }

    @GetMapping(value = "/open")
    public ResponseEntity openPinslot() {
        this.status = Status.open().isStatus();
        service.openSlot(true);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/close")
    public ResponseEntity closePinslot() {
        this.status = Status.closed().isStatus();
        service.closeSlot();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
