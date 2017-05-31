package com.balance.service;

import com.balance.model.Terminal;
import com.balance.repository.TerminalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by KEVIN on 26/05/2017.
 */
@Service("TerminalService")
public class TerminalServiceImpl implements TerminalService {

    @Autowired
    private TerminalRepository terminalRepository;

    @Override
    public void saveTerminal(Terminal Terminal) {
        terminalRepository.save(Terminal);
    }

    @Override
    public Iterable<Terminal> listAllTerminals() {
        return terminalRepository.findAll();
    }

    @Override
    public Terminal getTerminalById(Integer id) {
        return terminalRepository.findOne(id);
    }

    @Override
    public void deleteTerminal(Integer id) {
        terminalRepository.delete(id);
    }

    @Override
    public void setActiveTerminalById(Integer id){
        if(terminalRepository.findOne(id).isActive()){
            terminalRepository.findOne(id).setActive(false);
        }else{
            terminalRepository.findOne(id).setActive(true);
        }

    }

}
