package org.genshin.scrollninja;

import java.awt.RenderingHints.Key;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

// 制作メモ
// 10/5 制作開始
// 10/6 とりあえず表示まで。シングルトンを全部モノステートに。
// 10/8 コメント追加
// 10/10 仕様変更

// *メモ*
// stageってのがシーンの代わりです。
// シーン（ステージ）を遷移させたい時は、Stage型の変数を宣言してnew(world)して
// StageManager.StageTrance(変数名)で遷移します。
// 初期化処理は今はコンストラクタでやってますがあとで追加していきます。
// 10/23 line135で画像など解放？してタイトル遷移して増殖しないようにスタート

public class GameMain implements Screen{
	private Game						scrollNinja;
	public static World					world;			// ワールド
	public static OrthographicCamera	camera;		// カメラ
	public static SpriteBatch			spriteBatch;	// スプライトバッチ
	public static Interface 				playerInfo;	// インターフェース
	public boolean				PauseFlag;
	private Stage 			stage;						// ステージ
	private int				stageNum;					// ステージナンバー
	private long 			error			= 0;
	private int				fps				= 60;
	private long			idealSleep		= (1000 << 16) / fps;
	private long			newTime			= System.currentTimeMillis() << 16;
	private long			oldTime;
	private long			sleepTime		= idealSleep - (newTime - oldTime) - error; // 休止できる時間

	private boolean gotomenu;

	// 仮
	public static int gameState;
	public final static int GAME_RUNNING	= 0;	// ゲーム中
	public final static int GAME_PAUSED		= 1;	// 一時停止中
	public final static int GO_TO_MENU		= 9;	// メニュー画面へ

	// コンストラクタ
	public GameMain(Game game, int num) {
		scrollNinja		= game;
		world				= new World(new Vector2(0, -100.0f), true);

		// TODO 画面サイズによって数値を変更
		camera				= new OrthographicCamera(ScrollNinja.window.x * ScrollNinja.scale,
													 ScrollNinja.window.y * ScrollNinja.scale);
		spriteBatch 		= new SpriteBatch();
		stageNum			= num;
		stage				= new Stage(stageNum);
		playerInfo			= new Interface();

		// とりあえず適当に設定してみました
		world.setGravity(new Vector2( 0.0f, -150.0f ));

		StageManager.ChangeStage(stage);
		StageManager.GetNowStage().Init();
		BackgroundManager.CreateBackground(stageNum, true);

		gotomenu = false;
		gameState = GAME_RUNNING;
	}

	//************************************************************
	// Get
	// ゲッターまとめ
	//************************************************************
//	public World GetWorld(){ return world; }
//	public OrthographicCamera GetCamera(){ return camera; }
//	public SpriteBatch GetSpriteBatch(){ return spriteBatch; }

	//************************************************************
	// render
	// メイン処理
	//************************************************************
	@Override
	public void render(float delta) {
		switch (gameState) {
		case GAME_RUNNING:
			oldTime = newTime;

			// TODO
			if(Gdx.input.isKeyPressed(Keys.I)) {
				scrollNinja.setScreen(new StageEditor());
				StageEditor.Init();
			}

			StageManager.Update();
			StageManager.Draw();

			FPS();
			break;
		case GAME_PAUSED:
			updatePaused(delta);
			break;
		case GO_TO_MENU:
			stage.dispose();
			scrollNinja.setScreen(new MainMenu(scrollNinja));
			break;
		}
	}

	//************************************************************
	// FPS
	// FPS処理。汚いので関数化
	//************************************************************
	public void FPS() {
		newTime = System.currentTimeMillis() << 16;
		if (sleepTime < 0x20000) sleepTime = 0x20000; // 最低でも2msは休止
		oldTime = newTime;
		try {
			Thread.sleep(sleepTime >> 16);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} // 休止
		newTime = System.currentTimeMillis() << 16;
		error = newTime - oldTime - sleepTime; // 休止時間の誤差
	}

	/**
	 * updatePaused
	 * マップ表示中は他の描画をしない
	 */
	// TODO ぶれるので調整必要
	public void updatePaused(float delta) {
		// ポーズしたら全画面マップ表示

		// Lキーでポーズ解除（仮
		if(Gdx.input.isKeyPressed(Keys.L)) {
			playerInfo.SetPauseFlag(false);
			gameState = GAME_RUNNING;
		}

		if(Gdx.input.isKeyPressed(Keys.G)) {
			gameState = GO_TO_MENU;
			//scrollNinja.getScreen().dispose();
			//scrollNinja.setScreen(new MainMenu(scrollNinja));
			/*増殖する*/
		}
		
		if(Gdx.input.isTouched()) {
			
			/* 	マウス取得 ウィンドウの中心が原点 */
			float x = Gdx.input.getX() - Gdx.graphics.getWidth()*0.5f;
			float y = Gdx.graphics.getHeight()*0.5f - Gdx.input.getY();
			//x += GameMain.camera.position.x;
			//y += GameMain.camera.position.y;
			System.out.println(x);
			System.out.println(y);
			
			// (仮)コンティニューをクリックしたら
			if(x > 395 && x < 550 && y < 240 && y > 210) {
				playerInfo.SetPauseFlag(false);
				gameState = GAME_RUNNING;
			}			
			// (仮)コンティニューをクリックしたら
			if(x > 400 && x < 575 && y < 200 && y > 175) {

			}		
			// (仮)コンティニューをクリックしたら
			if(x > 400 && x < 585 && y < 170 && y > 142) {

			}
			

		}
			//フルスクリーン、ウィンドウだと座標が変わるのでマウス座標は×
			if(Gdx.input.isKeyPressed(Keys.T)) {
				playerInfo.SetPauseFlag(false);
				gameState = GAME_RUNNING;
			}
			//}
		
	}
	

	@Override
	public void resize(int width, int height) {}

	@Override
	public void show() {}

	@Override
	public void hide() {}

	@Override
	public void pause() {
		if (gameState == GAME_RUNNING)
			gameState = GAME_PAUSED;
	}

	@Override
	public void resume() {}

	@Override
	public void dispose() {
		world.dispose();
		spriteBatch.dispose();
		stage = null;
	}
}