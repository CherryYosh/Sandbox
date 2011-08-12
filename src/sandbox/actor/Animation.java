/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandbox.actor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javolution.util.FastList;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import sandbox.utils.Helper;

/**
 *
 * @author B
 */
public class Animation {

    public enum LOOPTYPE {

        NONE,
        PINGPONG,
        LOOP;
    }
    FastList<Texture> _textures = null;
    LOOPTYPE _loopType;
    String Name;

    public Animation(String name, LOOPTYPE loop, String... texturePaths) {
        Name = name;
        _loopType = loop;

        try {
            _textures = new FastList<Texture>();

            for (String s : texturePaths) {
                _textures.add(TextureLoader.getTexture("png", ResourceLoader.getResource("sandbox/images/" + s + ".png").openStream(), GL11.GL_NEAREST));
            }
        } catch (IOException e) {
            Helper.LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    public int GetTextureID(int i) {
        return _textures.get(i).getTextureID();
    }

    public void BindTexture(int i) {
        _textures.get(i).bind();
    }

    public String GetName() {
        return Name;
    }
    
    public int FrameCount(){ 
        return _textures.size(); 
    }
}
