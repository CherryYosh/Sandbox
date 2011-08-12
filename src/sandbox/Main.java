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
            
            shader = new Shader("sandbox/shaders/simple");
            _world = new World();
            _player = new Player();
        } catch (Exception e) { //General catch-all
            Helper.LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    private void InitGL() {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, Display.getDisplayMode().getWidth(), 0, Display.getDisplayMode().getHeight(), -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    private void render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glLoadIdentity();
        shader.Bind();
        _world.Render();
        _player.Render();
        shader.Unbind();
    }
}
