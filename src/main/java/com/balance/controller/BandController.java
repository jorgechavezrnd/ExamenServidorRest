package com.balance.controller;

import com.balance.model.Band;
import com.balance.model.Model;
import com.balance.model.User;
import com.balance.service.BandService;
import com.balance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by da_20 on 31/5/2017.
 */
@RestController
public class BandController {
    private BandService bandService;
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setBandService(BandService bandService) {
        this.bandService = bandService;
    }
    @RequestMapping(value = "/band", method = RequestMethod.POST)
    public Band saveBand(@Valid Band band, BindingResult bindingResult, Model model) {
        band.setFecha_evento(new Date());
        band.setFecha_registro(new Date());
        if (band.getFecha_registro().before(band.getFecha_evento())) {
            bandService.saveBand(band);
        }
        return band;
    }
    @RequestMapping(value = "/bands", method = RequestMethod.GET)
    public ResponseEntity<Iterable<Band>> getBands() {
        return new ResponseEntity(bandService.listAllBands(), HttpStatus.NOT_FOUND);
    }
    @RequestMapping(value = "/getSteps/{id}", method = RequestMethod.GET)
    public Band getBandSteps(@PathVariable Integer id) {
        Integer steps = 0;
        Integer sleep_quality = 0;

        Iterator<Band> iterator = bandService.listAllBands().iterator();

        while(iterator.hasNext()){
            if(iterator.next().getUser().equals(id)) {
                Band aux = iterator.next();
                steps += aux.getSteps();
                sleep_quality += aux.getSleep_quality();
            }

        };

        Band band = new Band();
        band.setSteps(steps);
        band.setSleep_quality(sleep_quality);
        band.setId(67626L);
        return band;
    }

}