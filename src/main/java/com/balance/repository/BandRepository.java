package com.balance.repository;

import com.balance.model.Band;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by da_20 on 31/5/2017.
 */
@Repository
public interface BandRepository extends JpaRepository<Band, Integer> {
}
