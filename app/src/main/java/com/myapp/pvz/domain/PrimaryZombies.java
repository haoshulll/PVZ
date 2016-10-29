package com.myapp.pvz.domain;

import com.myapp.pvz.base.BaseElement;
import com.myapp.pvz.base.Plant;
import com.myapp.pvz.base.Zombies;
import com.myapp.pvz.utils.CommonUtils;

import org.cocos2d.actions.CCScheduler;
import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.util.CGPointUtil;


/**
 * 基本的僵尸
 *
 * @author sks
 *
 */
public class PrimaryZombies extends Zombies {

	public PrimaryZombies(CGPoint startPoint, CGPoint endPoint) {
		super("image/zombies/zombies_1/walk/z_1_01.png");
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.setPosition(startPoint);// 设置僵尸的位置在起点
		move();
	}

	@Override
	public void move() {
		float distance = CGPointUtil.distance(getPosition(), endPoint);
		CCMoveTo moveTo = CCMoveTo.action(distance / speed, endPoint);// 移动的动作
		CCAction animate = CommonUtils.animate(
				"image/zombies/zombies_1/walk/z_1_%02d.png", 7, true);
		this.runAction(animate);
		CCSequence sequence = CCSequence.actions(moveTo,
				CCCallFunc.action(this, "destroy"));
		this.runAction(sequence);
	}

	Plant targetPlant = null;// 目标植物

	@Override
	public void attack(BaseElement element) {
		if (element instanceof Plant) {
			Plant plant = (Plant) element;
			if (targetPlant == null) { // 证明之前没有攻击目标
				targetPlant = plant; // 锁定攻击目标
				this.stopAllActions();// 停止走路
				//  切换攻击动作 image/zombies/zombies_1/attack/z_1_attack_01.png
				CCAction animate = CommonUtils.animate("image/zombies/zombies_1/attack/z_1_attack_%02d.png", 10, true);
				this.runAction(animate);

				// 植物持续掉血
				CCScheduler.sharedScheduler().schedule("attackPlant", this, 1, false);
			}
		}
	}
	// 定时任务 每隔一段时间 让植物掉血一次
	public void attackPlant(float t){
		// 植物被攻击的方法
		targetPlant.attacked(attack);
		if(targetPlant.getLife()<0){
			targetPlant=null;
			this.stopAllActions();//停止攻击的动作
			move();  // 继续向前走
		}

	}
	boolean  isDieing;// 正在死
	@Override
	public void attacked(int attack) {
		life-=attack;
		if(life<0&& !isDieing){
			isDieing=true;
			//destroy();// 让僵尸销毁
			stopAllActions();
			CCAction animate = CommonUtils.animate("image/zombies/zombies_1/head/z_1_head_%02d.png", 6, false);
			CCAction animate2 = CommonUtils.animate("image/zombies/zombies_1/die/z_1_die_%02d.png", 6, false);
			CCSequence sequence=CCSequence.actions((CCAnimate)animate, (CCAnimate)animate2,CCCallFunc.action(this, "destroy"));
			this.runAction(sequence);

		}
	}

	@Override
	public void baseAction() {
		// TODO Auto-generated method stub

	}

}
