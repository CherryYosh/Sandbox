/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandbox;

import org.lwjgl.util.vector.Vector3f;
import sandbox.actor.Sprite;

/**
 *
 * @author B
 */
public abstract class DrawableObject {
    
    protected Vector3f _position = new Vector3f();
    protected Sprite _sprite = null;
    protected long _lastTick = 0;
    
    public void Render(){}
    
    public void Update(long t){}
    
    public Vector3f GetPosition(){
        return _position;
    }
    
    public void SetPosition(Vector3f pos){
        _position = pos;
    }
}
