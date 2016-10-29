package com.myapp.pvz.domain;

import com.myapp.pvz.base.AttackPlant;
import com.myapp.pvz.base.Bullet;
import com.myapp.pvz.utils.CommonUtils;

import org.cocos2d.actions.base.CCAction;

public class PeasePlant extends AttackPlant {

	public PeasePlant() {
		super("image/plant/pease/p_2_01.png");
		baseAction();
	}
	// 创建子弹
	@Override
	public Bullet createBullet() {
		if(bullets.size()<1){
			final Pease pease=new Pease();  // 豌豆没有坐标
			pease.setPosition(ccp(this.getPosition().x+25, this.getPosition().y+35));
			pease.move();
			this.getParent().addChild(pease);// 把子弹添加到了地图上
			bullets.add(pease);
			pease.setDieListener(new DieListener() {
				// 当子弹死亡的时候  在弹夹中移除出去
				@Override
				public void die() {
					bullets.remove(pease);
				}
			});
		}
		return null;
	}

	@Override
	public void baseAction() {
		CCAction animate = CommonUtils.animate("image/plant/pease/p_2_%02d.png", 8, true);
		this.runAction(animate);
	}

}
