package com.myapp.pvz.base;

import org.cocos2d.nodes.CCSprite;


/**
 * 对战元素共性
 *
 * @author Administrator
 *
 */
public abstract class BaseElement extends CCSprite {
	public interface DieListener{  // 暴漏了一个接口
		void die();
	}
	private DieListener dieListener;  // 死亡的监听     定义了一个接口类型
	public void setDieListener(DieListener dieListener) {  // 暴漏了一个方法
		this.dieListener = dieListener;
	}

	public BaseElement(String filepath) {
		super(filepath);
	}

	/**
	 * 原地不动的基本动作
	 */
	public abstract void baseAction();

	/**
	 * 销毁
	 */
	public void destroy() {
		if(dieListener!=null){
			dieListener.die();     //当植物死亡的时候 调用接口的方法
		}
		this.removeSelf();
	}
}
