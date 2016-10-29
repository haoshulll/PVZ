package com.myapp.pvz.domain;

import com.myapp.pvz.utils.CommonUtils;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.nodes.CCSprite;

;

public class ShowZombies extends CCSprite {

	public ShowZombies() {
		super("image/zombies/zombies_1/shake/z_1_01.png");
		CCAction animate = CommonUtils.animate("image/zombies/zombies_1/shake/z_1_%02d.png",2, true);
		this.setScale(0.65f);
		this.setAnchorPoint(0.5f,0);
		this.runAction(animate);
	}
	
}
