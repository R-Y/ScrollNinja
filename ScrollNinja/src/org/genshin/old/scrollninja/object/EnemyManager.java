package org.genshin.old.scrollninja.object;

//========================================
// インポート
//========================================
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

//========================================
// クラス宣言
//========================================
//***** モノステート *****/
public class EnemyManager {
	// 変数宣言
	public static ArrayList<Enemy>		enemyList = new ArrayList<Enemy>();

	// コンストラクタ
	private EnemyManager() {}

	/**
	 * 更新
	 * @param deltaTime TODO
	 */
	public static void Update(float deltaTime) {
		for (int i = 0; i <enemyList.size(); i ++) {
			// 存在しているなら更新
			if (enemyList.get(i) != null)
				enemyList.get(i).update(deltaTime);
		}
	}

	/**
	 * スプライト描画
	 */
	public static void Draw() {
		for (int i = 0; i < enemyList.size(); i ++) {
			// 存在しているなら描画
			if (enemyList.get(i) != null)
				enemyList.get(i).render();
		}
	}

	/**
	 * 敵生成
	 * @param id		エネミーID
	 * @param position	出現位置
	 */
	public static void CreateEnemy(int id, Vector2 position) {
		Enemy pEnemy = new Enemy(id, enemyList.size(), position);
		enemyList.add(pEnemy);
	}

	/**
	 * 削除
	 * @param num		管理番号
	 */
	public static void DeleteEnemy(int num) {
		enemyList.get(num).dispose();
		enemyList.set(num, null);
	}

	/**
	 * 解放処理
	 */
	public static void dispose() {
		if (enemyList != null) {
			for (int i = 0; i < enemyList.size(); i++) {
				if (enemyList.get(i) != null)
					enemyList.get(i).dispose();
			}
		}
		enemyList = new ArrayList<Enemy>();
	}
}