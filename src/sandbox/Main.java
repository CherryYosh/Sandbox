/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandbox;

import java.util.logging.Level;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import sandbox.utils.Helper;
import sandbox.world.World;
import sandbox.actor.Player;
import sandbox.utils.Shader;

/**
 *
 * @author B
 */
public final class Main {

    private final int _MAXFPS = 60;
    private boolean _run = true;
    World _world = null;
    Player _player = null;
    Shader shader;
    public static Matrix4f mv = new Matrix4f();
    public static Matrix4f pro = new Matrix4f();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Helper.LOGGER.log(Level.INFO, "Starting Sandbox.");

        Main main = null;

        try {
            main = new Main();
            main.Init();
            main.Run();
        } catch (Exception e) {
            Helper.LOGGER.log(Level.SEVERE, e.toString(), e);
        } finally {
            if (main != null) {
                main.destroy();
            }
        }

        System.exit(0);
    }

    public Main() {
        //TODO: Something?
    }

    private void destroy() {
        //TODO
        Display.destroy();
    }

    public void Run() {
        while (_run && !Display.isCloseRequested()) {
            Display.update();
            _player.Update(Helper.getTime());

            if (Display.isActive()) {

                render();
                Display.sync(_MAXFPS);

            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }

                // Only bother rendering if the window is visible or dirty
                if (Display.isVisible() || Display.isDirty()) {
                    render();
                }
            }
        }

    }

    private void Init() {
        try {
            Display.setTitle("Testing");
            Display.setDisplayMode(new DisplayMode(640, 480));
            Display.create();

            Keyboard.create();
            Keyboard.enableRepeatEvents(true);

            Mouse.create();

            InitGL();

            mv.setIdentity();
            pro.setIdentity();

            //SetOrtho(0, Display.getDisplayMode().getWidth(), 0, Display.getDisplayMode().getHeight(), -1, 1);

            shader = new Shader("sandbox/shaders/simple");
            _world = new World();
            _player = new Player();
        } catch (Exception e) { //General catch-all
            Helper.LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    private void InitGL() {       
        GL11.glClearDepth(1.0);
    }

    private void render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glLoadIdentity();
        shader.Bind();

        shader.SetProjection(pro);
        shader.SetModelview(mv);
        
        _world.Render();
        //_player.Render();
        
        shader.Unbind();
    }

    public void SetOrtho(float left, float right, float bottom, float top, float near, float far) {
        Vector3f scale = new Vector3f(2.0f / (right - left),
                2.0f / (top - bottom),
                -2.0f / (far - near));
        Vector3f trans = new Vector3f(-(right + left) / (right - left),
                -(top + bottom) / (top - bottom),
                -(far + near) / (far - near));

        mv.setIdentity();
        mv.scale(scale);
        mv.translate(trans);
    }
}
