/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandbox.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import org.lwjgl.opengl.GL20;
import org.newdawn.slick.util.ResourceLoader;

/**
 *
 * @author mads
 */
public class Shader {
    // Shader handles
    private int _program = -1;
    private int _vsID = -1;
    private int _fsID = -1; 

    
    // GLSL Shader variable positions
    private int uniform_texture = 0;
    private int attr_position = 0;
    private int attr_coords = 0;
    private int attr_color = 0;

    public Shader(String name) {
        _program = GL20.glCreateProgram();

        if(_program > 0) {
            CreateVertShader(name);
            CreateFragShader(name);
        }

        if(_vsID > 0 && _fsID > 0) {
            GL20.glAttachShader(_program, _vsID);
            GL20.glAttachShader(_program, _fsID);

            GL20.glBindAttribLocation(_program, 0, "v_position");
            GL20.glBindAttribLocation(_program, 1, "v_color");
            GL20.glBindAttribLocation(_program, 2, "v_coords");
            
            GL20.glLinkProgram(_program);
            GL20.glValidateProgram(_program);
        }
        
        uniform_texture = GL20.glGetUniformLocation(_program, "tex");
        attr_position = GL20.glGetAttribLocation(_program, "v_position");
        attr_coords = GL20.glGetAttribLocation(_program, "v_coords");
        attr_color = GL20.glGetAttribLocation(_program, "v_color");
    }

    public void Bind() {
        GL20.glUseProgram(_program);
    }

    public static void Unbind() {
        GL20.glUseProgram(0);
    }

    public int GetTextureIndex() {
        return uniform_texture;
    }

    private int CreateVertShader(String name) {
        _vsID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        if(_vsID <= 0) {
                Helper.LOGGER.log(Level.SEVERE, "Unable to create vertex shader {0}.", new String[]{name});
                return -1;
        }

        GL20.glShaderSource(_vsID, ReadShaderFile(name+".vert"));
        GL20.glCompileShader(_vsID);

        return _vsID;
    }

    private int CreateFragShader(String name) {
        _fsID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        
        if(_fsID <= 0) {
                Helper.LOGGER.log(Level.SEVERE, "Unable to create Fragment shader {0}.", new String[]{name});
                return -1;
        }

        GL20.glShaderSource(_fsID, ReadShaderFile(name+".frag"));
        GL20.glCompileShader(_fsID);

        return _fsID;
    }
    
    private String[] ReadShaderFile(String name){
      StringBuilder sb = new StringBuilder();
      try{
         InputStream is = ResourceLoader.getResourceAsStream(name);
         BufferedReader br = new BufferedReader(new InputStreamReader(is));
         String line;
         while ((line = br.readLine())!=null){
            sb.append(line);
            sb.append('\n');
         }
         is.close();
      }
      catch (Exception e){
         Helper.LOGGER.log(Level.SEVERE, e.toString(), e);
      }
      
      return new String[]{sb.toString()};
    }

}
