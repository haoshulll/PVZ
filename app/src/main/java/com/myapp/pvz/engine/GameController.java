package com.myapp.pvz.engine;

import com.myapp.pvz.base.Plant;
import com.myapp.pvz.domain.NutPlant;
import com.myapp.pvz.domain.PeasePlant;
import com.myapp.pvz.domain.PrimaryZombies;
import com.myapp.pvz.domain.ShowPlant;
import com.myapp.pvz.layer.FightLayer;
import com.myapp.pvz.utils.CommonUtils;

import org.cocos2d.actions.CCProgressTimer;
import org.cocos2d.actions.CCScheduler;
import org.cocos2d.layers.CCTMXTiledMap;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * 游戏开始后的操作
 *
 * @author sks 添加植物 添加僵尸 植物攻击僵尸 僵尸攻击植物
 */
public class GameController {
	public static boolean isStart;// 游戏是否开始
	private static GameController controller = new GameController();

	private static List<FightLine> lines;// 记录行战场
	static {
		lines = new ArrayList<FightLine>();
		for (int i = 0; i < 5; i++) {
			FightLine line = new FightLine(i);
			lines.add(line);
		}

	}
	CCProgressTimer progressTimer;
	/**
	 * 更新进度
	 */
	private void progress() {
		//  创建了一个进度条
		progressTimer = CCProgressTimer.progressWithFile("image/fight/progress.png");
		//  设置进度条的位置
		progressTimer.setPosition(CCDirector.sharedDirector().getWinSize().width - 80, 13);
		// 获取到图层 添加进度条
		map.getParent().addChild(progressTimer);
		//  设置缩放
		progressTimer.setScale(0.6f);
		//  设置了进度条的百分比
		progressTimer.setPercentage(0);// 每增加一个僵尸需要调整进度，增加5
		// 设置了进度条的方向   从右向左的进度条
		progressTimer.setType(CCProgressTimer.kCCProgressTimerTypeHorizontalBarRL);

		CCSprite sprite = CCSprite.sprite("image/fight/flagmeter.png");
		sprite.setPosition(CCDirector.sharedDirector().getWinSize().width - 80, 13);
		map.getParent().addChild(sprite);
		sprite.setScale(0.6f);
		CCSprite name = CCSprite.sprite("image/fight/FlagMeterLevelProgress.png");
		name.setPosition(CCDirector.sharedDirector().getWinSize().width - 80, 5);
		map.getParent().addChild(name);
		name.setScale(0.6f);
	}


	private GameController() {

	}

	public static GameController getInstance() {
		return controller;
	}

	private CCTMXTiledMap map;
	private List<ShowPlant> selectPlants;
	private List<CGPoint> roads;

	public void startGame(CCTMXTiledMap map, List<ShowPlant> selectPlants) {
		isStart = true;
		this.map = map;
		this.selectPlants = selectPlants;

		loadRoads();
		// 定时器 定时任务
		// 参数1 方法名 方法有一个参数 float类型代表的间隔时间 参数2 当前类的对象 参数3 每次的间隔时间 单位 秒 参数4 是否暂停
		CCScheduler.sharedScheduler().schedule("addZombies", this, 5, false);
		// addZombies();

		loadTowers();

		progress();//加载进度条
	}
	// 用来存放安放植物的点
	CGPoint[][] towers=new CGPoint[5][9];
	private void loadTowers() {
		String format="tower%02d";
		for(int i=1;i<=5;i++){
			List<CGPoint> mapPoint = CommonUtils.getMapPoint(map,String.format(format, i));
			for(int j=0;j<mapPoint.size();j++){
				towers[i-1][j]=mapPoint.get(j);
			}
		}

	}

	private void loadRoads() {
		roads = CommonUtils.getMapPoint(map, "road");
	}
	int process=0;
	public void addZombies(float t) {
		Random random = new Random();
		int lineNum = random.nextInt(5);// [0-5) 0-4
		FightLine fightLine = lines.get(lineNum);

		// 创建一个僵尸的精灵 添加到地图上
		PrimaryZombies zombies = new PrimaryZombies(roads.get(lineNum * 2),
				roads.get(lineNum * 2 + 1));
		map.addChild(zombies,1);  // 把僵尸优先级 改成1  比默认的高
		// 行战场上记录僵尸
		fightLine.addZombies(zombies);

		process+=5;

		progressTimer.setPercentage(process);// 修改进度

	}

	public void endGame() {
		isStart = false;
	}

	private ShowPlant plant;// 玩家选择要安放的植物的
	private Plant installPlant;// 准备安放的植物

	/**
	 * 游戏开始后处理的点击事件
	 *
	 * @param point
	 */
	public void handleTouch(CGPoint point) {
		// 获取到了玩家已选植物的容器
		CCSprite chose = (CCSprite) map.getParent().getChildByTag(
				FightLayer.TAG_CHOSE);
		// 玩家正在选择植物
		if (plant != null) {
			plant.getShowSprite().setOpacity(255);
		}
		if (CGRect.containsPoint(chose.getBoundingBox(), point)) {
			for (ShowPlant plant : selectPlants) {
				CCSprite showSprite = plant.getShowSprite();
				if (CGRect.containsPoint(showSprite.getBoundingBox(), point)) {
					// 选择了该植物
					this.plant = plant;
					plant.getShowSprite().setOpacity(150);
					switch (plant.id) {
						case 4:
							installPlant = new NutPlant();
							break;
						case 1:
							installPlant=new PeasePlant();
							break;
						default:
							break;
					}
				}
			}
		} else {
			// 可能正在安放植物
			// 安放植物应该首先根据不同的图标创建不同的植物 安放
			if (plant != null && installPlant != null) {
				// plant.getShowSprite().setPosition(point);
				if (isBuilder(point)) {
					FightLine fightLine = lines.get(installPlant.getLine());
					if(!fightLine.havePlant(installPlant)){
						//installPlant.setPosition(point);
						fightLine.addPlant(installPlant);// 把植物记录到行战场中
						map.addChild(installPlant);
					}

				}
				plant.getShowSprite().setOpacity(255);
				plant = null;
				installPlant = null;
			}

		}
	}
	/**
	 * 判断点的范围是否有效
	 * @param point
	 * @return
	 */
	private boolean isBuilder(CGPoint point) {
		float x = point.x;
		float y = CCDirector.sharedDirector().getWinSize().height - point.y;
		int row = (int) (x / 46); // 1-9
		int line = (int) (y / 54); // 1-5     //修正植物的坐标
		row = row - 1; // 0-8  列号
		line -= 1; // 0-4  行号

		if (row <= 8 && row >= 0 && line >= 0 && line <= 4) {
			installPlant.setLine(line);
			installPlant.setRow(row);
			installPlant.setPosition(towers[line][row]);  // 修正了植物的坐标
			return true;
		} else {

			return false;
		}
	}

}
