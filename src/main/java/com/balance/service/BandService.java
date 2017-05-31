package com.balance.service;

import com.balance.model.Band;

/**
 * Created by da_20 on 31/5/2017.
 */
public interface BandService {
    void saveBand(Band band);
    Iterable<Band> listAllBands();
    Band getBandById(Integer id);
    void deleteBand(Integer id);
}
