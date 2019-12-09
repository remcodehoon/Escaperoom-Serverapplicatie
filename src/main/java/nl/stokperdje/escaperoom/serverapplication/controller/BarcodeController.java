package nl.stokperdje.escaperoom.serverapplication.controller;

import nl.stokperdje.escaperoom.serverapplication.exceptions.BarcodeAlreadyScannedException;
import nl.stokperdje.escaperoom.serverapplication.exceptions.NoActiveSessionException;
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
@RequestMapping("/barcode")
public class BarcodeController {

    @Autowired
    private SessionService sessionService;

    @GetMapping("/scan/{code}")
    public ResponseEntity scanCode(@PathVariable String code) {
        try {
            sessionService.scanCode(code);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/reset")
    public ResponseEntity reset() {
        sessionService.resetBarcodes();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/test/{state}")
    public ResponseEntity test(@PathVariable String state) {
        System.out.println(state);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
