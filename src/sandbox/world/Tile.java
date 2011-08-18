/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandbox.world;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.logging.Level;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import sandbox.utils.Helper;

/**
 *
 * @author B
 */
public class Tile {
    
     public class VertexData {
            
        float x, y, z;
        float q, r;
        
        final static int paddings = 3; //keep it alligned with 32.
        final static int DataSizeInBytes = (5 + paddings) * Helper.FLOAT_SIZE;
        final static int DataSize = 5 + paddings;

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

    VertexData[] _vertexData;
    Texture _Texture;

    public Tile(int x, int y) {
        //_Texture = tex;
        _vertexData = new VertexData[4];
        
        _vertexData[0] = new VertexData();
        _vertexData[0].x = x * Helper.SCALE;
        _vertexData[0].y = y * Helper.SCALE;
        _vertexData[0].z = 0;
        _vertexData[0].q = 0;
        _vertexData[0].r = 0;
        
        _vertexData[1] = new VertexData();
        _vertexData[1].x = (x+1) * Helper.SCALE;
        _vertexData[1].y = y * Helper.SCALE;
        _vertexData[1].z = 0;
        _vertexData[1].q = 1;
        _vertexData[1].r = 0;
        
        _vertexData[2] = new VertexData();
        _vertexData[2].x = (x+1) * Helper.SCALE;
        _vertexData[2].y = (y+1) * Helper.SCALE;
        _vertexData[2].z = 0;
        _vertexData[2].q = 1;
        _vertexData[2].r = 1;
        
        _vertexData[3] = new VertexData();
        _vertexData[3].x = x * Helper.SCALE;
        _vertexData[3].y = (y+1) * Helper.SCALE;
        _vertexData[3].z = 0;
        _vertexData[3].q = 0;
        _vertexData[3].r = 1;
    }
    
    public float[] GetData(){
        //NOTE: Is this the best way? Check memory..
        FloatBuffer fb = ByteBuffer.allocateDirect(VertexData.DataSizeInBytes * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        float[] ret = new float[VertexData.DataSize * 4];
        
        fb.put(_vertexData[0].data());
        fb.put(_vertexData[1].data());
        fb.put(_vertexData[2].data());
        fb.put(_vertexData[3].data());
        fb.flip();      
        
        fb.get(ret);
        return ret;
    }

    public void Render() {
    }
}
