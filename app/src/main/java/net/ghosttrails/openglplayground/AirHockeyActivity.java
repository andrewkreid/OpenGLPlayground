package net.ghosttrails.openglplayground;

import android.os.Bundle;

/**
 * Created by andrewr on 17/09/16.
 */
public class AirHockeyActivity extends OpenGLBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        renderer = new AirHockeyRenderer();
        super.onCreate(savedInstanceState);
    }
}
