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
import sandbox.world.Camera;

/**
 *
 * @author B
 */
public final class Main {

    private final int _MAXFPS = 60;
    private boolean _run = true;
    World _world = null;
    static Camera camera;
    Shader shader;

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
    }

    private void destroy() {
        Display.destroy();
    }

    public void Run() {
        while (_run && !Display.isCloseRequested()) {
            Display.update();
            _world.Update(Helper.getTime());

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

            _world = new World();
            camera = new Camera();
            shader = new Shader("sandbox/shaders/simple");
        } catch (Exception e) { //General catch-all
            Helper.LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    private void InitGL() {
        GL11.glViewport(0, 0, 640, 480);
        
        GL11.glClearDepth(1.0);
    }

    private void render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glLoadIdentity();
        
        shader.Bind();
        shader.SetModelview(camera.GetOrthographic());
        shader.SetProjection(camera.GetProjection());
        
        _world.Render();
        
        shader.Unbind();
    }
}
