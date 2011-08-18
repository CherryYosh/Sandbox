/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandbox.world;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
    int vao;
    int vbo;
    int ibo;
    short pindex_quad[] = new short[6];
    float pvertex_quad[] = new float[4 * 4];
    FastList<VertexData> tileArray = new FastList<VertexData>();

    public class VertexData {

        float x, y, z;
        float q, r;
        final static int paddings = 3; //keep it alligned with 32.
                
        final static int DataSizeInBytes = (5 + paddings) * Helper.FLOAT_SIZE;

        float[] data() {
            float[] ret = new float[5 + paddings];
            ret[0] = x;
            ret[1] = y;
            ret[2] = z;
            ret[3] = q;
            ret[4] = r;

            return ret;
        }
    }

    public World() {

        try {
            _TileList.put("Grass", TextureLoader.getTexture("png", ResourceLoader.getResource("sandbox/images/grass.png").openStream(), GL11.GL_NEAREST));
        } catch (IOException ex) {
            Logger.getLogger(World.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }

        VertexData[] t = new VertexData[4];
        for (int y = 0; y < 32; y++) {
            for (int x = 0; x < 32; x++) {
                
                t[0] = new VertexData();
                t[0].x = (x + 0) * 32f;
                t[0].y = (y + 0) * 32f;
                t[0].z = 1f;
                t[0].q = 1;
                t[0].r = 1;

                t[1] = new VertexData();
                t[1].x = (x + 1) * 32f;
                t[1].y = (y + 0) * 32f;
                t[1].z = 1.0f;
                t[1].q = 0;
                t[1].r = 1;

                t[2] = new VertexData();
                t[2].x = (x + 0) * 32f;
                t[2].y = (y + 1) * 32f;
                t[2].z = 1f;
                t[2].q = 0;
                t[2].r = 0;

                t[3] = new VertexData();
                t[3].x = (x + 1) * 32f;
                t[3].y = (y + 1) * 32f;
                t[3].z = 1f;
                //t[3].q = 1;
                //t[3].r = 0;

                tileArray.add(t[0]);
                tileArray.add(t[1]);
                tileArray.add(t[2]);
                //tileArray.add(t[3]);
            }
        }

        FloatBuffer tilebuf = ByteBuffer.allocateDirect(VertexData.DataSizeInBytes * tileArray.size()).order(ByteOrder.nativeOrder()).asFloatBuffer();
        for (FastList.Node<VertexData> n = tileArray.head(), end = tileArray.tail(); (n = n.getNext()) != end;) {
            VertexData d =  n.getValue();  
            tilebuf.put(d.data());
        }
        tilebuf.flip();
        
        vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, tilebuf, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, VertexData.DataSizeInBytes, 0);
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, VertexData.DataSizeInBytes, 3*Helper.FLOAT_SIZE);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL11.glEnableClientState( GL11.GL_VERTEX_ARRAY );

        GL30.glBindVertexArray(0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    @Override
    public void Render() {
        GL30.glBindVertexArray(vao);

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, tileArray.size());

        GL30.glBindVertexArray(0);
    }

    @Override
    public void Update(long t) {
    }
}
