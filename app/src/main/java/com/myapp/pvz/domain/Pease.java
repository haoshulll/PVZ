package com.myapp.pvz.domain;

import com.myapp.pvz.base.Bullet;

import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.util.CGPointUtil;

/**
 * 豌豆 子弹
 * @author sks
 *
 */
public class Pease extends Bullet {

	public Pease() {
		super("image/fight/bullet.png");
		setScale(0.65f);
		//move();
	}
	//子弹移动的动作
	@Override
	public void move() {
		CGPoint endPoint=CCNode.ccp(CCDirector.sharedDirector().getWinSize().width, this.getPosition().y);
		float distance = CGPointUtil.distance(this.getPosition(), endPoint);
		float t=distance/speed;
		CCMoveTo ccMoveTo=CCMoveTo.action(t, endPoint);
		CCSequence sequence=CCSequence.actions(ccMoveTo, CCCallFunc.action(this, "destroy"));
		this.runAction(sequence);
	}

}
