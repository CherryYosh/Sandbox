/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandbox.world;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author B
 */
public class Camera {

    public Matrix4f orthographic = new Matrix4f();
    public Matrix4f projection = new Matrix4f();

    public Camera() {
        projection.setIdentity();
        SetOrtho(0, Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight(), 0, 0, 1000);
    }

    public void SetOrtho(float left, float right, float bottom, float top, float near, float far) {
        Vector3f scale = new Vector3f(2.0f / (right - left),
                2.0f / (top - bottom),
                -2.0f / (far - near));
        Vector3f trans = new Vector3f(-(right + left) / (right - left),
                -(top + bottom) / (top - bottom),
                -(far + near) / (far - near));

        projection.setIdentity();
        projection.scale(scale);
        projection.translate(trans);
    }

    public void Move(float x, float y, float z) {
        orthographic.m30 += x;
        orthographic.m31 += y;
        orthographic.m32 += z;
    }
    
    public void SetPosition(float x, float y, float z){
        orthographic.m30 = x;
        orthographic.m31 = y;
        orthographic.m32 = z;
    }

    public void SetProjection(float fovy, float aspecty, float near, float far) {
        float fov = fovy;
        float aspect = aspecty;
        float zNear = near;
        float zFar = far;

        float f = (float) (1.0 / Math.tan(fovy / 2.0));
        //set up the scale
        //http://www.opengl.org/sdk/docs/man/xhtml/gluPerspective.xml
        Vector3f scale = new Vector3f(f / aspect,
                f,
                (zFar + zNear) / (zNear - zFar));

        //sets the values of [i,i] = 1 other wise all are 0
        projection.setIdentity();
        //set the projection data
        projection.scale(scale);
        projection.m23 = -1.0f;
        projection.m32 = (float) (2.0 * zFar * zNear) / (zNear - zFar);
        projection.m33 = 0.0f;
    }

    public Matrix4f GetProjection() {
        return projection;
    }

    public Matrix4f GetOrthographic() {
        return orthographic;
    }
}
