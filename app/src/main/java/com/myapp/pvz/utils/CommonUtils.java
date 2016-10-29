package com.myapp.pvz.utils;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.layers.CCTMXObjectGroup;
import org.cocos2d.layers.CCTMXTiledMap;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.transitions.CCFlipAngularTransition;
import org.cocos2d.types.CGPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @包名: com.myapp.pvz
 * @作者: haoshul
 * @时间: 2016/10/22 21:42
 * @描述: TODO
 */

public class CommonUtils {
    /**
     * 序列帧播放的工具方法
     *
     * @param path
     *            图片的格式化的路径
     * @param num 帧数
     * @param isForever
     *            是否永不停止的循环
     *
     * @return
     */
    public static CCAction animate(String path,int num, boolean isForever) {
        ArrayList<CCSpriteFrame> frames = new ArrayList<CCSpriteFrame>();
        String format = path;
        for (int i = 1; i <= num; i++) {
            CCSpriteFrame displayedFrame = CCSprite.sprite(
                    String.format(format, i)).displayedFrame();
            frames.add(displayedFrame);
        }

        CCAnimation anim = CCAnimation.animation("", 0.2f, frames);
        if (isForever) {
            CCAnimate animate = CCAnimate.action(anim);
            CCRepeatForever forever = CCRepeatForever.action(animate);
            return forever;
        } else {
            CCAnimate animate = CCAnimate.action(anim, false); // 序列帧默认必须永不停止的循环
            // 第二个参数false代表可以不用循环
            return animate;

        }
    }

    /**
     * 切换场景的工具方法
     *
     * @param newLayer
     */
    public static void changeLayer(CCLayer newLayer) {
        CCScene ccScene = CCScene.node();
        ccScene.addChild(newLayer);
        // 参数1 切换的时间 参数2 切换后新的场景
        CCFlipAngularTransition transition = CCFlipAngularTransition
                .transition(1, ccScene, 1);

        // 切换界面
        CCDirector.sharedDirector().replaceScene(transition);// 替换场景 实现了界面的切换
        // 替换之前的场景
    }

    /**
     * 获取地图上的点
     *
     * @param map
     *            地图
     * @param name
     *            地图上对象的名字
     * @return
     */
    public static List<CGPoint> getMapPoint(CCTMXTiledMap map, String name) {
        List<CGPoint> cgPoints = new ArrayList<CGPoint>();
        CCTMXObjectGroup objectGroupNamed = map.objectGroupNamed(name);
        ArrayList<HashMap<String, String>> objects = objectGroupNamed.objects;
        for (HashMap<String, String> hashMap : objects) {
            int x = Integer.parseInt(hashMap.get("x"));
            int y = Integer.parseInt(hashMap.get("y"));
            CGPoint point = CCNode.ccp(x, y);
            cgPoints.add(point);
        }
        return cgPoints;
    }
}
