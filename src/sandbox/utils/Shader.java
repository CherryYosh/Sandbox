/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandbox.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.logging.Level;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
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
    private int textureLoc = -1;
    private int modelviewLoc = -1;
    private int projectionLoc = -1;
    private int vertexLoc = -1;

    public Shader(String name) {
        _program = GL20.glCreateProgram();

        if (_program > 0) {
            CreateVertShader(name);
            CreateFragShader(name);
        }

        if (_vsID > 0 && _fsID > 0) {
            GL20.glAttachShader(_program, _vsID);
            GL20.glAttachShader(_program, _fsID);

            GL30.glBindFragDataLocation(_program, 0, "pixelColor");
            GL20.glBindAttribLocation(_program, 0, "vertex");
            GL20.glBindAttribLocation(_program, 1, "texCoord");

            GL20.glLinkProgram(_program);
            GL20.glValidateProgram(_program);
            PrintProgramLog();
        }

        modelviewLoc = GL20.glGetUniformLocation(_program, "modelview");
        projectionLoc = GL20.glGetUniformLocation(_program, "projection");
        vertexLoc = GL20.glGetAttribLocation(_program, "vertex");
        textureLoc = GL20.glGetUniformLocation(_program, "texture");
    }

    public void Bind() {
        GL20.glUseProgram(_program);
    }

    public static void Unbind() {
        GL20.glUseProgram(0);
    }

    public int GetTextureIndex() {
        return textureLoc;
    }

    public void SetProjection(Matrix4f m) {
        FloatBuffer buf = ByteBuffer.allocateDirect(16 * Helper.FLOAT_SIZE).order(ByteOrder.nativeOrder()).asFloatBuffer();
        buf.rewind();
        m.store(buf);
        //buf.flip();
        buf.rewind();
        GL20.glUniformMatrix4(projectionLoc, false, buf);
    }

    public void SetModelview(Matrix4f m) {
        FloatBuffer buf = ByteBuffer.allocateDirect(16 * Helper.FLOAT_SIZE).order(ByteOrder.nativeOrder()).asFloatBuffer();
        buf.rewind();
        m.store(buf);
        //buf.flip();
        buf.rewind();
        GL20.glUniformMatrix4(modelviewLoc, false, buf);
    }

    private int CreateVertShader(String name) {
        _vsID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        if (_vsID <= 0) {
            Helper.LOGGER.log(Level.SEVERE, "Unable to create vertex shader {0}.", new String[]{name});
            return -1;
        }

        GL20.glShaderSource(_vsID, ReadShaderFile(name + ".vert"));
        GL20.glCompileShader(_vsID);
        PrintShaderLog(_vsID);

        return _vsID;
    }

    private int CreateFragShader(String name) {
        _fsID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);

        if (_fsID <= 0) {
            Helper.LOGGER.log(Level.SEVERE, "Unable to create Fragment shader {0}.", new String[]{name});
            return -1;
        }

        GL20.glShaderSource(_fsID, ReadShaderFile(name + ".frag"));
        GL20.glCompileShader(_fsID);
        PrintShaderLog(_fsID);

        return _fsID;
    }

    private String[] ReadShaderFile(String name) {
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = ResourceLoader.getResourceAsStream(name);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
            is.close();
        } catch (Exception e) {
            Helper.LOGGER.log(Level.SEVERE, e.toString(), e);
        }

        return new String[]{sb.toString()};
    }

    private void PrintProgramLog() {
        if (GL11.GL_FALSE == GL20.glGetShader(_program, GL20.GL_COMPILE_STATUS)) {
            Helper.LOGGER.log(Level.SEVERE, GL20.glGetProgramInfoLog(_program, GL20.glGetShader(_program, GL20.GL_INFO_LOG_LENGTH)));
        }
    }
    
    private void PrintShaderLog(int shader) {
        if (GL11.GL_FALSE == GL20.glGetShader(shader, GL20.GL_COMPILE_STATUS)) {
            Helper.LOGGER.log(Level.SEVERE, GL20.glGetShaderInfoLog(shader, GL20.glGetShader(shader, GL20.GL_INFO_LOG_LENGTH)));
        }
    }
}
