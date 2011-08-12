/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandbox.actor;

import javolution.util.FastMap;

/**
 * NOTE: Could possibly replace this with a FastMap<>. Here in case we need to expand
 * @author B
 */
public class Sprite {
    private FastMap<String, Animation> _AnimationList;
    final String Name;
    
    public Sprite(String name){
        Name = name;
        
        _AnimationList = new FastMap<String, Animation>();
    }
    
    public void add(String str, Animation animation){
        _AnimationList.put(str, animation); 
    }
    
    public Animation get(String str){
        return _AnimationList.get(str);
    }
}
