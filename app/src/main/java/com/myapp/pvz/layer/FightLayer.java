package com.myapp.pvz.layer;

import android.view.MotionEvent;

import com.myapp.pvz.domain.ShowPlant;
import com.myapp.pvz.domain.ShowZombies;
import com.myapp.pvz.engine.GameController;
import com.myapp.pvz.utils.CommonUtils;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCTMXTiledMap;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 加载地图 过了两秒钟地图的平移 加载展示的僵尸 加载玩家可选植物的容器 加载玩家已选植物的容器
 *
 * 添加僵尸 安放植物 僵尸攻击植物 植物攻击僵尸
 *
 */
public class FightLayer extends BaseLayer {
    public static final int TAG_CHOSE = 10;
    private CCTMXTiledMap map;
    private List<CGPoint> zombiesShow;
    private CCSprite choose; // 大的容器 玩家可选植物的容器
    private CCSprite chose; // 小的容器 玩家已选植物的
    private List<ShowPlant> showPlants;
    private List<ShowPlant> selectPlants = new CopyOnWriteArrayList<ShowPlant>();// 玩家已选择植物的集合
    private List<ShowZombies> zombiesList; // 为了方便回收僵尸

    public FightLayer() {
        init();
    }

    private void init() {
        loadMap();
        loadShowZombies();
    }

    // 加载僵尸
    private void loadShowZombies() {
        zombiesList = new ArrayList<ShowZombies>();
        zombiesShow = CommonUtils.getMapPoint(map, "zombies");
        for (CGPoint cgPoint : zombiesShow) {
            ShowZombies zombies = new ShowZombies(); // 创建了一个特殊的精灵
            zombies.setPosition(cgPoint); // 给精灵添加一个坐标
            map.addChild(zombies); // 把精灵添加到地图上 地图移动 僵尸也移动了
            zombiesList.add(zombies);
        }
        moveMap();
    }

    private void loadMap() {
        map = CCTMXTiledMap.tiledMap("image/fight/map_day.tmx");
        map.setAnchorPoint(0.5f, 0.5f);// 锚点
        map.setPosition(map.getContentSize().width / 2,
                map.getContentSize().height / 2);// 设置了坐标
        this.addChild(map);
        // moveMap(map);
    }

    // 移动地图
    private void moveMap() {
        // 要向左移动的距离
        float distance = map.getContentSize().width - winSize.width;

        CCMoveBy ccMoveBy = CCMoveBy.action(2, ccp(-distance, 0));
        CCSequence sequence = CCSequence.actions(CCDelayTime.action(3),
                ccMoveBy, CCDelayTime.action(1),
                CCCallFunc.action(this, "loadContainer"));// 三秒后平移地图
        map.runAction(sequence); // 地图运行动作
    }

    /**
     * 加载玩家选择植物的容器
     */
    public void loadContainer() {
        chose = CCSprite.sprite("image/fight/chose/fight_chose.png");
        chose.setAnchorPoint(0, 1);// 设置锚点在图片的左上角
        chose.setPosition(0, winSize.height);// 设置容器坐标是屏幕的左上角
        this.addChild(chose,0,TAG_CHOSE); // 指定标签添加容器

        choose = CCSprite.sprite("image/fight/chose/fight_choose.png");
        choose.setAnchorPoint(0, 0);
        this.addChild(choose);

        loadPlant();

        start = CCSprite.sprite("image/fight/chose/fight_start.png");
        start.setPosition(choose.getContentSize().width / 2, 30);
        choose.addChild(start); // 添加了一起来摇滚的按钮
    }

    // 加载植物
    private void loadPlant() {
        showPlants = new ArrayList<ShowPlant>();
        for (int i = 1; i <= 9; i++) {
            ShowPlant plant = new ShowPlant(i);
            CCSprite bgSprite = plant.getBgSprite();
            CCSprite showSprite = plant.getShowSprite();

            // 设置坐标
            bgSprite.setPosition(16 + ((i - 1) % 4) * 54,

                    175 - ((i - 1) / 4) * 59);

            showSprite.setPosition(16 + ((i - 1) % 4) * 54,

                    175 - ((i - 1) / 4) * 59);

            showPlants.add(plant);
            choose.addChild(bgSprite);
            choose.addChild(showSprite);
        }
        // 能晚开就晚开 能早关就早关
        this.setIsTouchEnabled(true);
    }

    boolean islock;

    public void unlock() {
        islock = false;// 解锁了
    }

    boolean isDel;
    private CCSprite start;
    private CCSprite ready;

    @Override
    public boolean ccTouchesBegan(MotionEvent event) {
        CGPoint point = this.convertTouchToNodeSpace(event);
        if (GameController.isStart) {
            GameController.getInstance().handleTouch(point);
        } else {

            // 判断玩家已选植物容器是否被点击
            if (CGRect.containsPoint(chose.getBoundingBox(), point)) {
                // 反选植物
                isDel = false;
                for (ShowPlant plant : selectPlants) {
                    CCSprite showSprite = plant.getShowSprite();
                    if (CGRect
                            .containsPoint(showSprite.getBoundingBox(), point)) {
                        // 反选植物
                        CCSprite bgSprite = plant.getBgSprite();
                        CGPoint position = bgSprite.getPosition();
                        CCMoveTo moveTo = CCMoveTo.action(0.2f, position);
                        showSprite.runAction(moveTo);
                        selectPlants.remove(plant); // 移除出选择植物的集合
                        isDel = true;
                        continue;// / 跳出本次循环 继续下次循环
                    }
                    // 如果发现之前删除了一个元素 让之后的元素 全部向左移动53长度
                    if (isDel) {
                        CCMoveBy ccMoveBy = CCMoveBy.action(0.2f, ccp(-53, 0));
                        showSprite.runAction(ccMoveBy);
                    }
                }

            }
            // 判断 玩家已有植物的容器有没有被点击
            else if (CGRect.containsPoint(choose.getBoundingBox(), point)) {
                if (CGRect.containsPoint(start.getBoundingBox(), point)) {
                    // 开始游戏
                    startGame();
                } else {

                    // 可以判断植物是否被点击
                    for (ShowPlant plant : showPlants) {
                        CCSprite showSprite = plant.getShowSprite();
                        if (CGRect.containsPoint(showSprite.getBoundingBox(),
                                point)) {
                            if (selectPlants.size() < 5 && !islock) { // 如果植物的选择数量
                                // 超过或者等于5的情况下
                                // 就不允许再选择植物了
                                // 用户选择了植物
                                islock = true;// 加了一把锁
                                CCMoveTo moveTo = CCMoveTo
                                        .action(0.5f,
                                                ccp(75 + selectPlants.size() * 53,
                                                        255));
                                CCSequence ccSequence = CCSequence.actions(
                                        moveTo,
                                        CCCallFunc.action(this, "unlock"));
                                showSprite.runAction(ccSequence); // 当移动完成前 不应该
                                // 再接受点击事件了
                                selectPlants.add(plant);// 记录了玩家选择的植物
                            }
                        }
                    }
                }
            }
        }

        return super.ccTouchesBegan(event);
    }

    private void startGame() {
        System.out.println("开始游戏");
        // 回收容器
        choose.removeSelf();
        chose.setScale(0.65f);// 缩小玩家已选植物的容器
        // 把玩家选择的植物 重新添加到容器中
        for (ShowPlant plant : selectPlants) {

            plant.getShowSprite().setScale(0.65f);// 因为父容器缩小了 孩子一起缩小

            plant.getShowSprite().setPosition(
                    plant.getShowSprite().getPosition().x * 0.65f,
                    plant.getShowSprite().getPosition().y

                            + (CCDirector.sharedDirector().getWinSize().height - plant

                            .getShowSprite().getPosition().y)

                            * 0.35f);// 设置坐标

            this.addChild(plant.getShowSprite());

        }

        float distance = map.getContentSize().width - winSize.width;
        // 移动地图
        CCMoveBy ccMoveBy = CCMoveBy.action(2, ccp(distance, 0));
        CCSequence sequence = CCSequence.actions(ccMoveBy,
                CCCallFunc.action(this, "removeZombies"));
        map.runAction(sequence);
        // 当地图移动完成的时候 回收僵尸

    }

    /**
     * 回收僵尸
     */
    public void removeZombies() {
        for (ShowZombies zombies : zombiesList) {
            zombies.removeSelf();// 回收僵尸了
        }
        zombiesList.clear();
        zombiesList = null;

        ready();
    }

    private void ready() {
        ready = CCSprite.sprite("image/fight/startready_01.png");
        // winSize.width
        ready.setPosition(winSize.width / 2, winSize.height / 2);
        this.addChild(ready);
        // 准备开始 游戏的序列帧
        CCAction animate = CommonUtils.animate(
                "image/fight/startready_%02d.png", 3, false);

        CCSequence ccSequence = CCSequence.actions((CCAnimate) animate,
                CCCallFunc.action(this, "start"));
        ready.runAction(ccSequence);
    }

    /**
     * 真正开始游戏
     */
    public void start() {
        ready.removeSelf();
        GameController.getInstance().startGame(map, selectPlants); // 目的就是为了优化代码结构
        // 增加阅读性
    }
}
