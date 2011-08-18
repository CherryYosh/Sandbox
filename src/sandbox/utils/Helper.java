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
    
    public static final int BOOL_SIZE = 1;
    public static final int BYTE_SIZE = 1;
    public static final int CHAR_SIZE = 2;
    public static final int SHORT_SIZE = 2;
    public static final int INT_SIZE = 4;
    public static final int FLOAT_SIZE = 4;
    public static final int LONG_SIZE = 8;
    public static final int DOUBLE_SIZE = 8;
    
    public static final int SCALE = 32;
    
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
