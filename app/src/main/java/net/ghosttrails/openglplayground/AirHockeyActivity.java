package net.ghosttrails.openglplayground;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by andrewr on 17/09/16.
 */
public class AirHockeyActivity extends OpenGLBaseActivity {

    final AirHockeyRenderer airHockeyRenderer = new AirHockeyRenderer(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        renderer = airHockeyRenderer;
        super.onCreate(savedInstanceState);

        glSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event != null) {
                    // Convert touch coordinates into normalized device coordinates, keeping in
                    // mind that Android's Y coordinates are inverted.
                    final float normalizedX = (event.getX() / (float) v.getWidth()) * 2 - 1;
                    final float normalizedY = -((event.getY() / (float) v.getHeight()) * 2 - 1);

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        glSurfaceView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                airHockeyRenderer.handleTouchPress(normalizedX, normalizedY);
                            }
                        });
                    } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        glSurfaceView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                airHockeyRenderer.handleTouchDrag(normalizedX, normalizedY);
                            }
                        });
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });

    }
}
