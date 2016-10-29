package net.ghosttrails.openglplayground.objects;

import net.ghosttrails.openglplayground.data.VertexArray;
import net.ghosttrails.openglplayground.programs.ColorShaderProgram;
import net.ghosttrails.openglplayground.util.Geometry;

import java.util.List;

/**
 * Geometry for the hockey puck.
 */

public class Puck {
    private static final int POSITION_COMPONENT_COUNT = 3;

    public final float radius, height;

    private final VertexArray vertexArray;
    private final List<ObjectBuilder.DrawCommand> drawList;

    public Puck(float radius, float height, int numPointsAroundPuck) {
        this.radius = radius;
        this.height = height;

        ObjectBuilder.GeneratedData generatedData = ObjectBuilder.createPuck(
                new Geometry.Cylinder(new Geometry.Point(0f, 0f, 0f), radius, height),
                numPointsAroundPuck);

        vertexArray = new VertexArray(generatedData.vertexData);
        drawList = generatedData.drawList;
    }

    public void bindData(ColorShaderProgram colorProgram) {
        vertexArray.setVertexAttribPointer(0,
                colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, 0);
    }

    public void draw() {
        for(ObjectBuilder.DrawCommand drawCommand: drawList) {
            drawCommand.draw();
        }
    }
}
