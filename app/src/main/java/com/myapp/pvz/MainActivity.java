package com.myapp.pvz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.myapp.pvz.layer.WelcomeLayer;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;

public class MainActivity extends AppCompatActivity {
    private CCDirector ccDirector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CCGLSurfaceView view = new CCGLSurfaceView(this);
        setContentView(view);
        ccDirector = CCDirector.sharedDirector();
        ccDirector.attachInView(view);
        ccDirector.setDisplayFPS(true);
        ccDirector.setScreenSize(480,320);
        ccDirector.setDeviceOrientation(CCDirector.kCCDeviceOrientationLandscapeLeft);
        CCScene ccScene = CCScene.node();
        ccScene.addChild(new WelcomeLayer());
        ccDirector.runWithScene(ccScene);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ccDirector.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ccDirector.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ccDirector.end();
    }
}
