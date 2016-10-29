package com.myapp.pvz.base;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * 攻击型植物
 *
 * @author Administrator
 *
 */
public abstract class AttackPlant extends Plant {
	// 弹夹
	protected List<Bullet> bullets = new CopyOnWriteArrayList<Bullet>();

	public AttackPlant(String filepath) {
		super(filepath);
	}

	/**
	 * 生产用于攻击的子弹
	 *
	 * @return
	 */
	public abstract Bullet createBullet();
	/**
	 * 弹夹  管理我产生的子弹
	 * @return
	 */
	public List<Bullet> getBullets() {
		return bullets;
	}

//	@Override
//	public void onDie(BaseElement element) {
//
//		if (element instanceof Bullet) {
//			bullets.remove(element);
//		}
//	}

}
