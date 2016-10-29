package com.myapp.pvz.domain;

import org.cocos2d.nodes.CCSprite;

import java.util.HashMap;

public class ShowPlant {
	//  集合 当做一个数据库了
	public static  HashMap<Integer	, HashMap<String, String>> plants;

	static{
		//  模拟数据库   id integer aotuoincrement
		String format= "image/fight/chose/choose_default%02d.png";
		plants =new HashMap<Integer, HashMap<String,String>>();
		for(int i=1;i<=9;i++){
			HashMap<String, String> value=new HashMap<String, String>();
			value.put("path",String.format(format, i)); // 把图片的路径存到数据库中
			value.put("sun", 50+"");
			plants.put(i, value);//  把植物存到数据库中

		}
	}


	public int id;
	private CCSprite showSprite; //展示用的精灵
	private CCSprite bgSprite; //背景精灵

	public ShowPlant(int id){
		this.id=id;
		HashMap<String, String> hashMap = plants.get(id);
		String path = hashMap.get("path");
		showSprite=CCSprite.sprite(path);
		showSprite.setAnchorPoint(0,0);

		bgSprite=CCSprite.sprite(path);
		bgSprite.setOpacity(100);// 给背景精灵设置透明度
		bgSprite.setAnchorPoint(0,0);

	}

	public CCSprite getShowSprite() {
		return showSprite;
	}

	public CCSprite getBgSprite() {
		return bgSprite;
	}

}
