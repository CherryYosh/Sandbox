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
import sandbox.actor.Player;
import sandbox.utils.Shader;

/**
 *
 * @author B
 */
public final class World extends DrawableObject {

    private FastMap<String, Texture> _TileList = new FastMap<String, Texture>();
    int vao;
    int vbo;
    int ibo;
    FastList<Tile> tileArray = new FastList<Tile>();
    Player player;

    public World() {

        try {
            _TileList.put("Grass", TextureLoader.getTexture("png", ResourceLoader.getResource("sandbox/images/grass.png").openStream(), GL11.GL_NEAREST));
        } catch (IOException ex) {
            Logger.getLogger(World.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
        
        for(int i= 0; i < 32; i++){
            for(int j = 0; j < 32; j++){
                tileArray.add(new Tile(i,j));
            }
        }

        //Generate the VBO vertex data
        FloatBuffer tilebuf = ByteBuffer.allocateDirect(Tile.VertexData.DataSizeInBytes * (tileArray.size() * 4)).order(ByteOrder.nativeOrder()).asFloatBuffer();
        for (FastList.Node<Tile> n = tileArray.head(), end = tileArray.tail(); (n = n.getNext()) != end;) {
            Tile t =  n.getValue();  
            tilebuf.put(t.GetData());
        }
        tilebuf.flip();
        
        //Generate the ibo data;
        IntBuffer indexbuf = ByteBuffer.allocateDirect(tileArray.size()*6*Helper.INT_SIZE).order(ByteOrder.nativeOrder()).asIntBuffer();
        for(int i = 0; i < tileArray.size()*4; i+=4){
            indexbuf.put(i);
            indexbuf.put(i+1);
            indexbuf.put(i+3);
            indexbuf.put(i+1);
            indexbuf.put(i+2);
            indexbuf.put(i+3);
        }
        indexbuf.flip();
        
        vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, tilebuf, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        
        ibo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexbuf, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, Tile.VertexData.DataSizeInBytes, 0);
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, Tile.VertexData.DataSizeInBytes, 3*Helper.FLOAT_SIZE); //the texture data
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL11.glEnableClientState( GL11.GL_VERTEX_ARRAY );
        
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);

        GL30.glBindVertexArray(0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        
        
        player = new Player();
    }

    @Override
    public void Render() {
        GL30.glBindVertexArray(vao);
        _TileList.get("Grass").bind();

        GL11.glDrawElements(GL11.GL_TRIANGLES, tileArray.size()*6, GL11.GL_UNSIGNED_INT, 0);
        
        GL30.glBindVertexArray(0);
                
        player.Render();
    }

    @Override
    public void Update(long t) {
        player.Update(t);
    }
}
