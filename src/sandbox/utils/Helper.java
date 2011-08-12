/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandbox.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.lwjgl.Sys;

/**
 *
 * @author B
 */
public final class Helper {
    public static final Logger LOGGER = Logger.getLogger("sandbox");
    
    private static Helper _instance = null;
    private static final long _ticksPerSecond = Sys.getTimerResolution();
    
    static{
        try{
            FileHandler h = new FileHandler("sandbox.log",true);
            h.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(h);
        } catch (IOException e){
            LOGGER.log(Level.WARNING, e.toString(), e);
        }
    }
    
    public static Helper getInstance(){
        if(_instance == null){
            _instance = new Helper();
        }
        
        return _instance;
    }
    
    public static long getTime() {
        return (Sys.getTime() * 1000) / _ticksPerSecond;
    }
}
