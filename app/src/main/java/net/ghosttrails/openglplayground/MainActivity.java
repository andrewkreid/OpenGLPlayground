package net.ghosttrails.openglplayground;

import android.os.Bundle;

public class MainActivity extends OpenGLBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        renderer = new FirstOpenGLProjectRenderer();
        super.onCreate(savedInstanceState);
    }

}






