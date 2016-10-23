package net.ghosttrails.openglplayground;

import android.content.Context;
import android.opengl.GLSurfaceView;

import net.ghosttrails.openglplayground.util.LoggerConfig;
import net.ghosttrails.openglplayground.util.MatrixHelper;
import net.ghosttrails.openglplayground.util.ShaderHelper;
import net.ghosttrails.openglplayground.util.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.orthoM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;
import static net.ghosttrails.openglplayground.data.Constants.BYTES_PER_FLOAT;

/**
 * AirHockey Renderer
 */
public class AirHockeyRenderer implements GLSurfaceView.Renderer {

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE =
            (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    private static final String A_COLOR = "a_Color";
    private static final String A_POSITION = "a_Position";
    private static final String U_MATRIX = "u_Matrix";

    private final FloatBuffer vertexData;
    private final Context context;
    private int program;

    private int aPositionLocation;
    private int aColorLocation;
    private int uMatrixLocation;

    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];

    AirHockeyRenderer(Context context) {
        this.context = context;

        float[] tableVertices = {

                //  X,     Y,    R,    G,    B
                // Triangle Fan
                   0f,    0f,   1f,   1f,   1f,
                -0.5f, -0.8f,   0.7f, 0.7f, 0.7f,
                 0.5f, -0.8f,   0.7f, 0.7f, 0.7f,
                 0.5f,  0.8f,   0.7f, 0.7f, 0.7f,
                -0.5f,  0.8f,   0.7f, 0.7f, 0.7f,
                -0.5f, -0.8f,   0.7f, 0.7f, 0.7f,

                // Line 1
                -0.5f, 0.0f,    1f,   0f,   0f,
                 0.5f, 0.0f,    1f,   0f,   0f,

                // Mallets
                0.0f, -0.4f,    0f,   0f,   1f,
                0.0f,  0.4f,    1f,   0f,   0f

        };

        vertexData = ByteBuffer.allocateDirect(tableVertices.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(tableVertices);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.2f, 0.2f, 0.2f, 0.0f);

        String vertexShaderSource = TextResourceReader.readTextFileFromResource(context,
                R.raw.simple_vertex_shader);
        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(context,
                R.raw.simple_fragment_shader);

        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);

        if (LoggerConfig.ON) {
            ShaderHelper.validateProgram(program);
        }

        glUseProgram(program);
        aColorLocation = glGetAttribLocation(program, A_COLOR);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);

        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, STRIDE,
                vertexData);
        glEnableVertexAttribArray(aPositionLocation);

        vertexData.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT, false, STRIDE,
                vertexData);
        glEnableVertexAttribArray(aColorLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // Set the OpenGL viewport to fill the entire surface
        glViewport(0, 0, width, height);

        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1f, 10f);

        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, 0f, 0f, -2.5f);
        rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);

        // multiply projectionMatrix and modelMatrix together and copy result back into
        // projectionMatrix.
        final float[] temp = new float[16];
        multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // Clear the rendering surface
        glClear(GL_COLOR_BUFFER_BIT);

        // Set up the ortho projection.
        glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);

        // Draw table
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

        // Draw dividing line
        glDrawArrays(GL_LINES, 6, 2);

        // Draw Mallets
        glDrawArrays(GL_POINTS, 8, 1);
        glDrawArrays(GL_POINTS, 9, 1);
    }
}
