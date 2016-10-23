package net.ghosttrails.openglplayground.util;

/**
 * Matrix Helper Functions.
 */

public class MatrixHelper {

    /**
     * Create a perspective projection matrix. Hand-rolled alternative to Matrix.perspectiveM
     * @param m 16-element array for 4x4 matrix. Values set by this method.
     * @param yFovInDegrees Field of view.
     * @param aspect screen aspect ratio (width/height).
     * @param n near plane
     * @param f far plane.
     */
    public static void perspectiveM(float[] m, float yFovInDegrees, float aspect, float n,
                                    float f) {
        final float angleInRadians = (float) (yFovInDegrees * Math.PI / 180.0);

        // focal length
        final float a = (float) (1.0 / Math.tan(angleInRadians / 2.0));

        m[0] = a / aspect;
        m[1] = 0f;
        m[2] = 0f;
        m[3] = 0f;

        m[4] = 0f;
        m[5] = a;
        m[6] = 0f;
        m[7] = 0f;

        m[8] = 0f;
        m[9] = 0f;
        m[10] = -((f + n) / (f - n));
        m[11] = -1f;

        m[12] = 0f;
        m[13] = 0f;
        m[14] = -((2f * f * n) / (f - n));
        m[15] = 0f;
    }
}
