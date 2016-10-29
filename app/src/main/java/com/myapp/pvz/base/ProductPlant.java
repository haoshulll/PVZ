package com.myapp.pvz.base;
/**
 * 生产型植物
 * @author Administrator
 *
 */
public abstract class ProductPlant extends Plant {

	public ProductPlant(String filepath) {
		super(filepath);
	}

	/**
	 * 阳光、金币
	 */
	public abstract void create();


}
