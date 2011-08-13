/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandbox.world;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javolution.util.FastList;
import javolution.util.FastMap;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL30;
import sandbox.DrawableObject;
import sandbox.utils.Helper;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 *
 * @author B
 */
public final class World extends DrawableObject {

    private FastMap<String, Texture> _TileList = new FastMap<String, Texture>();
    private boolean _updatingEnabled = false;
    private boolean _updateThreadAlive = true;
    private final Thread _updateThread;
    int vao;
    int vbo;

    public World() {
        try {
            _TileList.put("Grass", TextureLoader.getTexture("png", ResourceLoader.getResource("sandbox/images/grass.png").openStream(), GL11.GL_NEAREST));
        } catch (IOException ex) {
            Logger.getLogger(World.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
        
        float[] vertices = new float[18];
        float z = -1;

        vertices[0] = -5;
        vertices[1] = -5;
        vertices[2] = z; // Bottom left corner  
        vertices[3] = -5;
        vertices[4] = 5;
        vertices[5] = z; // Top left corner  
        vertices[6] = 5;
        vertices[7] = 5;
        vertices[8] = z; // Top Right corner  

        vertices[9] = 5;
        vertices[10] = -5;
        vertices[11] = z; // Bottom right corner  
        vertices[12] = -5;
        vertices[13] = -5;
        vertices[14] = z; // Bottom left corner  
        vertices[15] = 5;
        vertices[16] = 5;
        vertices[17] = z; // Top Right corner 


        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);

        FloatBuffer buf = ByteBuffer.allocateDirect(18 * 8).asFloatBuffer().put(vertices);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buf, GL15.GL_STATIC_DRAW); // Set the size and data of our VBO and set it to STATIC_DRAW  

        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0); // Set up our vertex attributes pointer  

        GL20.glEnableVertexAttribArray(0); // Disable our Vertex Array Object  
        GL30.glBindVertexArray(0); // Disable our Vertex Buffer Object 

        _updateThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    if (!_updateThreadAlive) {
                        return; //kill thread
                    }

                    if (!_updatingEnabled) {
                        synchronized (_updateThread) {
                            try {
                                _updateThread.wait();
                            } catch (InterruptedException e) {
                                Helper.LOGGER.log(Level.SEVERE, e.toString());
                            }
                        }
                    }
                }
            }
        });
    }

    public void dispose() {
        synchronized (_updateThread) {
            _updateThreadAlive = false;
            _updateThread.notify();
        }
    }

    @Override
    public void Render() {
        GL30.glBindVertexArray(vao);
        GL20.glEnableVertexAttribArray(0);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);
        GL30.glBindVertexArray(0);
    }

    @Override
    public void Update(long t) {
    }

    public void StartUpdateThread() {
        _updatingEnabled = true;
        _updateThread.start();
    }

    public void ResumeUpdateThread() {
        _updatingEnabled = true;
        synchronized (_updateThread) {
            _updateThread.notify();
        }
    }

    public void SuspendUpdateThread() {
        _updatingEnabled = false;
    }
}
