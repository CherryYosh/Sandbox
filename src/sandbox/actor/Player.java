/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandbox.actor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.logging.Level;
import javolution.util.FastMap;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;
import sandbox.DrawableObject;
import sandbox.Main;
import sandbox.actor.Animation.LOOPTYPE;
import sandbox.utils.Helper;

/**
 *
 * @author B
 */
public class Player extends DrawableObject {
    
    private float[] tmpVertexData = new float[]{
        //X,Y,Z,Q,R,P,P,P
        0,0,0,0,0,0,0,0,
        Helper.SCALE,0,0,1,0,0,0,0,
        Helper.SCALE,Helper.SCALE,0,1,1,0,0,0,
        0,Helper.SCALE,0,0,1,0,0,0
        };
    private int[] tmpIndexData = new int[]{ 0,1,3,1,2,3};
    private int vbo,ibo,vao;
    
    private static FastMap<String, Sprite> _SpriteList = new FastMap<String, Sprite>();

    private long _lastSpriteTick = 0;
    private Animation _CurrentAnimation = null;
    private int _Frame = 0;
    private byte pol = 1;
    private Vector3f _Velocity = new Vector3f(0, 0, 0);
    private Vector3f _Movement = new Vector3f(0, 0, 0);
    private int _WalkSpeed = 500;
    private int _WalkTicks = 0;

    public Player() {
        if (_SpriteList.get("Human") == null) {
            Helper.LOGGER.log(Level.INFO, "Loading the animations for Human...");

            Sprite human = new Sprite("Human");
            human.add("WalkForward", new Animation("WalkForward", LOOPTYPE.PINGPONG, "ManWalkR", "ManWalkR2"));
            human.add("WalkBackwards", new Animation("WalkBackwards", LOOPTYPE.PINGPONG, "ManWalk", "ManWalk2"));
            human.add("StandBack", new Animation("StandBack", LOOPTYPE.NONE, "ManStand"));
            human.add("StandForward", new Animation("StandForward", LOOPTYPE.NONE, "ManStandR"));
            _SpriteList.put(human.Name, human);

            Helper.LOGGER.log(Level.INFO, "Finished loading the animation set...");
        }
        
        _position.set(16, 16);
        _sprite = _SpriteList.get("Human");
        _CurrentAnimation = _sprite.get("StandBack");
        Main.camera.SetPosition(-16*32,-16*32,0);
        
        
        ///*******************ARGGGHHHGHGHGHGHG
        FloatBuffer fb = ByteBuffer.allocateDirect(tmpVertexData.length * Helper.FLOAT_SIZE).order(ByteOrder.nativeOrder()).asFloatBuffer();
        fb.put(tmpVertexData);
        fb.flip();
        
        //Generate the ibo data;
        IntBuffer ib = ByteBuffer.allocateDirect(tmpIndexData.length*Helper.INT_SIZE).order(ByteOrder.nativeOrder()).asIntBuffer();
        ib.put(tmpIndexData);
        ib.flip();
        
        
        vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, fb, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        
        ibo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, ib, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 8 * Helper.FLOAT_SIZE, 0);
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 8 * Helper.FLOAT_SIZE, 3*Helper.FLOAT_SIZE); //the texture data
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL11.glEnableClientState( GL11.GL_VERTEX_ARRAY );
        
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);

        GL30.glBindVertexArray(0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

    }

    @Override
    public void Render() {       
        GL30.glBindVertexArray(vao);
        _CurrentAnimation.BindTexture(_Frame);

        GL11.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_INT, 0);

        
        GL30.glBindVertexArray(0);
    }

    @Override
    public void Update(long t) {
        long delta = t - _lastTick;
        _lastTick = t;

        HandleMovementKeyPress();
        UpdateAvatar(delta);
        UpdatePosition(delta);
    }

    private void HandleMovementKeyPress() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            _Movement.y = 1;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            _Movement.y = -1;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            _Movement.x = -1;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            _Movement.x = 1;
        }
    }

    private void UpdatePosition(long t) {
        boolean temp = false;
        if (_WalkTicks + t > _WalkSpeed) {
            t = _WalkSpeed - _WalkTicks;
            temp = true;
        }

       Main.camera.Move(-(_Velocity.x * ((t*32)/(float)_WalkSpeed)), 
                        (_Velocity.y * ((t*32)/(float)_WalkSpeed)), 
                        0);
        _position.x += (_Velocity.x * ((t*32)/(float)_WalkSpeed));
        _position.y += (_Velocity.y * ((t*32)/(float)_WalkSpeed));

        if (temp) {
            //Helper.LOGGER.log(Level.INFO, "Moved to {0},{1}", new Object[]{_position.x, _position.y});
             
            _WalkTicks = 0;
            _Velocity.x = _Movement.x;
            _Velocity.y = _Movement.y;
            _Movement.x = 0;
            _Movement.y = 0;
        } else {
            _WalkTicks += t;
        }
    }

    private void StepFrame(long t) {
        //return if there is just one image.
        if ((_CurrentAnimation.FrameCount() == 1) || (_CurrentAnimation._loopType == LOOPTYPE.NONE)) {
            return;
        }
        
        if(_lastSpriteTick + t < _WalkSpeed / _CurrentAnimation.FrameCount()){
            _lastSpriteTick += t;
            return;
        }

        _Frame += pol;

        if (_Frame < 0) {
            if (_CurrentAnimation._loopType == LOOPTYPE.PINGPONG) {
                _Frame = 1;
                pol *= -1;
            } //NOTE: LOOP should never have a polarity of -1. so no need to check
        } else if (_Frame > _CurrentAnimation.FrameCount() - 1) {
            if (_CurrentAnimation._loopType == LOOPTYPE.PINGPONG) {
                _Frame = _CurrentAnimation.FrameCount() - 2;
                pol *= -1;
            } else if (_CurrentAnimation._loopType == LOOPTYPE.LOOP) {
                _Frame = 0;
            }
        }
        
        _lastSpriteTick = 0;
    }

    private void UpdateAvatar(long t) {
        if (_Velocity.y > 0) { //are we going north
            if (_CurrentAnimation.GetName().equals("WalkForward")) {
                StepFrame(t);
            } else {
                _CurrentAnimation = _sprite.get("WalkForward");
                _Frame = 0;
                _lastSpriteTick = 0;
            }
        } else if (_Velocity.y < 0) { //are we going south
            if (_CurrentAnimation.GetName().equals("WalkBackwards")) {
                StepFrame(t);
            } else {
                _CurrentAnimation = _sprite.get("WalkBackwards");
                _Frame = 0;
                _lastSpriteTick = 0;
            }
        }
    }
}
