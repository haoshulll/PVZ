package com.myapp.pvz.layer;

import android.os.AsyncTask;
import android.view.MotionEvent;

import com.myapp.pvz.utils.CommonUtils;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.instant.CCHide;
import org.cocos2d.actions.instant.CCShow;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

/**
 * @包名: com.myapp.pvz
 * @作者: haoshul
 * @时间: 2016/10/22 20:18
 * @描述: TODO
 */

public class WelcomeLayer extends BaseLayer {

    private CCSprite logo;
    private CCSprite start;

    public WelcomeLayer(){
        asysTask();
        init();
    }

    private void asysTask() {
        new AsyncTask<Void, Void, Void>(){
            // 在子线程中执行的代码
            @Override
            protected Void doInBackground(Void... params) {
                // 模拟耗时的操作
                try {
                    Thread.sleep(8000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
            //在子线程之后执行的代码
            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                start.setVisible(true);
                setIsTouchEnabled(true);//  触摸事件能早关就早关 能晚开就晚开

            }

        }.execute();
    }

    @Override
    public boolean ccTouchesBegan(MotionEvent event) {
        CGPoint cgPoint = this.convertTouchToNodeSpace(event);
        CGRect boundingBox = start.getBoundingBox();
        if (CGRect.containsPoint(boundingBox,cgPoint)){
            System.out.println("切换界面");
           CommonUtils.changeLayer(new MenuLayer());
        }
        return super.ccTouchesBegan(event);
    }

    private void init() {
        logo();
    }



    private void logo() {
        logo = CCSprite.sprite("image/popcap_logo.png");

        logo.setPosition(winSize.width/2, winSize.height/2);
        this.addChild(logo);

        CCHide ccHide = CCHide.action();
        CCShow ccShow = CCShow.action();
        CCDelayTime delayTime = CCDelayTime.action(1);
        CCCallFunc callFunc = CCCallFunc.action(this,"welcome");
        CCSequence sequence = CCSequence.actions(ccHide,delayTime,ccShow,delayTime,delayTime,ccHide,delayTime,callFunc);
        logo.runAction(sequence);
    }
    public void welcome(){
        logo.removeSelf();
        CCSprite welcome = CCSprite.sprite("image/welcome.jpg");
        welcome.setAnchorPoint(0,0);
        this.addChild(welcome);

        CCSprite loading = CCSprite.sprite("image/loading/loading_01.png");
        loading.setPosition(winSize.width/2,30);
        this.addChild(loading);

        String str = "image/loading/loading_%02d.png";
        CCAction animate = CommonUtils.animate(str,9, false);
        loading.runAction(animate);

        start = CCSprite.sprite("image/loading/loading_start.png");
        start.setPosition(winSize.width/2,30);
        this.addChild(start);
        start.setVisible(false);
    }
}
