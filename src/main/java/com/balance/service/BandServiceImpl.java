package com.balance.service;

import com.balance.model.Band;
import com.balance.model.Terminal;
import com.balance.repository.BandRepository;
import com.balance.repository.TerminalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by da_20 on 31/5/2017.
 */
@Service
public class BandServiceImpl implements BandService {

    @Autowired
    private BandRepository bandRepository;

    @Override
    public void saveBand(Band band) {
        bandRepository.save(band);
    }

    @Override
    public Iterable<Band> listAllBands() {
        return bandRepository.findAll();
    }

    @Override
    public Band getBandById(Integer id) {
        return bandRepository.findOne(id);
    }

    @Override
    public void deleteBand(Integer id) {
        bandRepository.delete(id);
    }
}
