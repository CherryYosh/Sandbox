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
import java.nio.ShortBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javolution.util.FastList;
import javolution.util.FastMap;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
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
    int ibo;
    short pindex_quad[] = new short[6];
    float pvertex_quad[] = new float[4 * 4];

    public World() {

        try {
            _TileList.put("Grass", TextureLoader.getTexture("png", ResourceLoader.getResource("sandbox/images/grass.png").openStream(), GL11.GL_NEAREST));
        } catch (IOException ex) {
            Logger.getLogger(World.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }

        //A quad
        pvertex_quad[0] = -0.8f;
        pvertex_quad[1] = -0.5f;
        pvertex_quad[2] = -0.9f;
        pvertex_quad[3] = 0xFFFFFFFF;

        pvertex_quad[4] = 0.0f;
        pvertex_quad[5] = -0.5f;
        pvertex_quad[6] = -0.9f;
        pvertex_quad[7] = 0xFFFF0000;

        pvertex_quad[8] = -0.8f;
        pvertex_quad[9] = 0.5f;
        pvertex_quad[10] = -0.9f;
        pvertex_quad[11] = 0xFF00FF00;

        pvertex_quad[12] = 0.0f;
        pvertex_quad[13] = 0.5f;
        pvertex_quad[14] = -0.9f;
        pvertex_quad[15] = 0xFF0000FF;

        pindex_quad[0] = 0;
        pindex_quad[1] = 1;
        pindex_quad[2] = 2;
        pindex_quad[3] = 2;
        pindex_quad[4] = 1;
        pindex_quad[5] = 3;

        ibo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, (ShortBuffer) ByteBuffer.allocateDirect(6 * 2).asShortBuffer().put(pindex_quad).flip(), GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, (FloatBuffer) ByteBuffer.allocateDirect(4 * 4 * 4).asFloatBuffer().put(pvertex_quad).flip(), GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 4 * 4, 0);
        GL20.glEnableVertexAttribArray(0);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);

        GL30.glBindVertexArray(0);
        GL20.glDisableVertexAttribArray(0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

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

        GL12.glDrawRangeElements(GL11.GL_TRIANGLES, 0, 3, 6, GL11.GL_UNSIGNED_SHORT, 0);

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
