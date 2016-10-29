package com.myapp.pvz.base;
/**
 * 子弹
 */
public abstract class Bullet extends Product {
	protected int attack = 10;// 攻击力
	protected int speed =100;// 移动速度

	public Bullet(String filepath) {
		super(filepath);
	}

	@Override
	public void baseAction() {

	}
	/**
	 * 移动
	 */
	public abstract void move();

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

}

