package com.myapp.pvz.layer;

import com.myapp.pvz.utils.CommonUtils;

import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemSprite;
import org.cocos2d.nodes.CCSprite;

/**
 * @包名: com.myapp.pvz.layer
 * @作者: haoshul
 * @时间: 2016/10/22 22:46
 * @描述: TODO
 */

public class MenuLayer extends BaseLayer {
    public MenuLayer(){
        init();
    }

    private void init() {
        CCSprite menu = CCSprite.sprite("image/menu/main_menu_bg.jpg");
        menu.setAnchorPoint(0,0);
        this.addChild(menu);

        CCMenu ccMenu = CCMenu.menu();
        CCMenuItemSprite item = CCMenuItemSprite.item(CCSprite.sprite("image/menu/start_adventure_default.png"),
                CCSprite.sprite("image/menu/start_adventure_press.png"), this, "click");
        ccMenu.addChild(item);
        ccMenu.setScale(0.5f);
        ccMenu.setPosition(winSize.width/2-25,winSize.height/2-110);
        ccMenu.setRotation(4.5f);
        this.addChild(ccMenu);
    }

    public void click(Object object){
        System.out.println("我被按下了");
        CommonUtils.changeLayer(new FightLayer());
    }


}
