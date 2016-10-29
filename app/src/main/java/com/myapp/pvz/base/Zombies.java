package com.myapp.pvz.base;

import org.cocos2d.types.CGPoint;

/**
 * 僵尸基类
 *
 * @author Administrator
 *
 */
public abstract class Zombies extends BaseElement {

	protected int life = 50;// 生命
	protected int attack = 10;// 攻击力
	protected int speed = 10;// 移动速度

	protected CGPoint startPoint;// 起点
	protected CGPoint endPoint;// 终点

	public Zombies(String filepath) {
		super(filepath);

		setScale(0.5);
		setAnchorPoint(0.5f, 0);// 将解析的点位放在两腿之间
	}

	/**
	 * 移动
	 */
	public abstract void move();

	/**
	 * 攻击
	 *
	 * @param element:攻击植物，攻击僵尸
	 */
	public abstract void attack(BaseElement element);

	/**
	 * 被攻击
	 */
	public abstract void attacked(int attack);

}
