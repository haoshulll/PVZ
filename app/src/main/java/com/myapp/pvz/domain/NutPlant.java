package com.myapp.pvz.domain;

import com.myapp.pvz.base.DefancePlant;
import com.myapp.pvz.utils.CommonUtils;

import org.cocos2d.actions.base.CCAction;

;

public class NutPlant extends DefancePlant {

	public NutPlant() {
		super("image/plant/nut/p_3_01.png");
		baseAction();
		
	}

	@Override
	public void baseAction() {
		CCAction animate = CommonUtils.animate("image/plant/nut/p_3_%02d.png", 11, true);
		this.runAction(animate);
		
		
	}

}
