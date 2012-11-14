package org.genshin.scrollninja.object.character.ninja;

import org.genshin.scrollninja.GlobalParam;
import org.genshin.scrollninja.utils.FixtureDefLoader;
import org.genshin.scrollninja.utils.XMLFactory;

import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * 忍者関連の定数等を定義する。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
enum NinjaParam
{
	/** シングルトンインスタンス */
	INSTANCE;
	
	/**
	 * コンストラクタ
	 */
	private NinjaParam()
	{
		//---- 忍者の挙動関連
		{
			Element rootElement = XMLFactory.getInstance().get(GlobalParam.INSTANCE.OBJECT_PARAM_XML_FILE_PATH);
			rootElement = rootElement.getChildByName("Ninja");
			
			// 走り
			RUN_ACCEL			= rootElement.getFloat("RunAccel");
			RUN_MAX_VELOCITY	= rootElement.getFloat("RunMaxVelocity");
			
			// ダッシュ
			DASH_ACCEL			= rootElement.getFloat("DashAccel");
			DASH_MAX_VELOCITY	= rootElement.getFloat("DashMaxVelocity");
			
			// ジャンプ
			JUMP_POWER			= rootElement.getFloat("JumpPower");
			AERIAL_JUMP_COUNT	= rootElement.getInt("AerialJumpCount");
		}
		
		//---- 衝突関連
		{
			Element rootElement = XMLFactory.getInstance().get(GlobalParam.INSTANCE.COLLISION_PARAM_XML_FILE_PATH);
			rootElement = rootElement.getChildByName("Ninja");
			
			// 上半身
			BODY_FIXTURE_DEF_LOADER = new FixtureDefLoader(rootElement.getChildByName("Body"));
			
			// 下半身
			FOOT_FIXTURE_DEF_LOADER = new FixtureDefLoader(rootElement.getChildByName("Foot"));
		}
	}
	
	/** 走りの加速度 */
	final float RUN_ACCEL;
	
	/** 走りの最高速度 */
	final float RUN_MAX_VELOCITY;

	/** ダッシュの加速度 */
	final float DASH_ACCEL;
	
	/** ダッシュの最高速度 */
	final float DASH_MAX_VELOCITY;
	
	/** ジャンプ力 */
	final float JUMP_POWER;
	
	/** 空中でジャンプできる回数 */
	final int AERIAL_JUMP_COUNT;
	
	/** 上半身Fixtureの定義情報 */
	final FixtureDefLoader BODY_FIXTURE_DEF_LOADER;
	
	/** 下半身Fixtureの定義情報 */
	final FixtureDefLoader FOOT_FIXTURE_DEF_LOADER;
}
