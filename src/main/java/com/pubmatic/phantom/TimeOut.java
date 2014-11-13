package com.pubmatic.phantom;

import java.util.TimerTask;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TimeOut extends TimerTask {    
    private final static Logger logger = Logger.getLogger(TimeOut.class.getName());


    @Override
    public void run(){
        logger.log(Level.INFO,"Timed out while downloading.");
        
    }
};

