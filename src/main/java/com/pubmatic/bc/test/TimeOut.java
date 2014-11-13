package com.pubmatic.bc.test;

import java.util.TimerTask;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TimeOut extends TimerTask {
    private final Server server;
    private final static Logger logger = Logger.getLogger(TimeOut.class.getName());

    public TimeOut(Server server) {
        this.server = server;
    }

    @Override
    public void run(){
        logger.log(Level.INFO,"Timed out while downloading.");
        server.setState(ServerState.TIMEDOUT);
    }
};

