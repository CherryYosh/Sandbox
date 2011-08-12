/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandbox.world;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

/**
 *
 * @author B
 */
class Tile {

    int _x;
    int _y;
    Texture _Texture;

    public Tile(Texture tex, int x, int y) {
        _Texture = tex;
        _x = x;
        _y = y;
    }

    public void Render() {
        GL11.glPushMatrix();
        GL11.glTranslatef(_x, _y, 0);

        GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(1.0f, 1.0f);
            GL11.glVertex2i(0, 0);
            GL11.glTexCoord2f(.0f, 1.0f);
            GL11.glVertex2i(32, 0);
            GL11.glTexCoord2f(0.0f, 0.0f);
            GL11.glVertex2i(32, 32);
            GL11.glTexCoord2f(1.0f, 0.0f);
            GL11.glVertex2i(0, 32);
        GL11.glEnd();

        GL11.glPopMatrix();
    }
}
