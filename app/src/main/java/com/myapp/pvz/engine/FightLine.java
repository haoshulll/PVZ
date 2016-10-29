package com.myapp.pvz.engine;

import com.myapp.pvz.base.AttackPlant;
import com.myapp.pvz.base.BaseElement;
import com.myapp.pvz.base.Bullet;
import com.myapp.pvz.base.Plant;
import com.myapp.pvz.base.Zombies;

import org.cocos2d.actions.CCScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 行战场
 *
 */
public class FightLine {
	private int lineNum;//行号
	/**
	 * 记录僵尸
	 */
	ArrayList<Zombies> zombiesList=new ArrayList<Zombies>();
	/**
	 * 记录植物  key 植物的列号
	 */
	Map<Integer,Plant> plants=new HashMap<Integer, Plant>();
	List<AttackPlant> attackPlants=new ArrayList<AttackPlant>();// 创建了一个攻击类型的植物的集合
	public FightLine(int lineNum) {
		super();
		this.lineNum = lineNum;
		//  1定时任务的方法名    2  对象  3 间隔时间  4 是否暂停
		CCScheduler.sharedScheduler().schedule("attactPlant", this, 0.2f, false);
		CCScheduler.sharedScheduler().schedule("creatPease", this,2, false);
		CCScheduler.sharedScheduler().schedule("attackZombies", this, 0.1f, false);
	}

	public void attackZombies(float t){
		if(zombiesList.size()>0&&attackPlants.size()>0){
			//  遍历所有僵尸 观察僵尸的x坐标的范围

			for(Zombies zombies:zombiesList){
				float x=zombies.getPosition().x;
				float left=x-20;
				float right=x+20;
				// 遍历所有的攻击类型植物
				for(AttackPlant attackPlant:attackPlants){
					List<Bullet> bullets = attackPlant.getBullets();
					for(Bullet bullet:bullets){
						float bulletX=bullet.getPosition().x;
						if(bulletX>left&&bulletX<right){
							//子弹在僵尸的范围之中  产生了碰撞
							zombies.attacked(bullet.getAttack());// 僵尸被攻击
							bullet.setAttack(0);// 子弹的攻击力 变为0
							bullet.setVisible(false);// 子弹不可见了
						}
					}

				}


			}


		}


	}

	public void creatPease(float t){
		//  保证当前行 必须有攻击的植物   //  保证当前的行  必须有僵尸
		if(attackPlants.size()>0&& zombiesList.size()>0){
			for(AttackPlant attackPlant:attackPlants){
				attackPlant.createBullet();//创建子弹
			}


		}

	}
	/**
	 * 僵尸攻击植物
	 * @param t
	 */
	public void  attactPlant(float t){
		if(plants.size()>0&&zombiesList.size()>0){
			//僵尸攻击植物
			for(Zombies zombies:zombiesList){
				int row=(int) (zombies.getPosition().x/46);  // 获取到僵尸所在的列
				row=row-1;
				Plant plant = plants.get(row);  // 根据列号获取到列上的植物
				if(plant!=null){  //植物不为空
					zombies.attack(plant);// 攻击植物
				}
			}


		}
	}
	/**
	 * 添加植物
	 * @param plant
	 */
	public void addPlant(final Plant plant){
		plants.put(plant.getRow(), plant);
		plant.setDieListener(new BaseElement.DieListener() {
			// 当植物死亡的时候调用
			@Override
			public void die() {
				plants.remove(plant.getRow());// 当植物死亡的时候 把它在集合中移除出去
				if(plant instanceof AttackPlant){
					attackPlants.remove(plant);//  当植物如果是攻击类型的植物 死亡的时候 把他在攻击类型的植物中移除
				}
			}
		});
		if(plant instanceof AttackPlant){
			attackPlants.add((AttackPlant)plant);
		}
	}
	/**
	 * 判断一列上是否有植物
	 * @param plant
	 * @return  true 有植物
	 */
	public  boolean havePlant(Plant plant){
		return plants.containsKey(plant.getRow());
	}
	/**
	 * 添加僵尸
	 * @param zombies
	 */
	public void addZombies(final Zombies zombies){
		zombiesList.add(zombies);
		zombies.setDieListener(new BaseElement.DieListener() {

			@Override
			public void die() {
				zombiesList.remove(zombies);// 当僵尸死亡的时候 移除集合
			}
		});
	}


}	
