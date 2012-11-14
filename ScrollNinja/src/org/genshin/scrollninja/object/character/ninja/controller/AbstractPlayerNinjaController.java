/**
 * 
 */
package org.genshin.scrollninja.object.character.ninja.controller;

import org.genshin.scrollninja.utils.XMLFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * プレイヤーが操作する忍者の操作状態を管理するオブジェクトの基本クラス。
 * @author	kou
 * @since		1.0
 * @version	1.0
 */
public abstract class AbstractPlayerNinjaController implements NinjaControllerInterface
{
	/**
	 * コンストラクタ
	 */
	public AbstractPlayerNinjaController()
	{
		inputHelpers = new InputHelperInterface[InputType.values().length];
		
		Element root = XMLFactory.getInstance().get("data/xml/object_param.xml");
		root = root.getChildByName("Kaginawa");
		KAGINAWA_RELEASE_TIME = root.getFloat("ReleaseTime");
		
		initialize();
	}

	@Override
	public final void update()
	{
		/** 入力情報を更新する。 */
		for(int i = 0;  i < inputHelpers.length;  ++i)
		{
			assert inputHelpers[i]!=null : "NinjaControllerは正しく初期化されていません。(InputType=" + InputType.values()[i].toString() + ")";
			inputHelpers[i].update();
		}
		
		/** 鉤縄を離す入力の判定用タイマー */
		final float delta = 1.0f/60.0f;
		if( inputHelpers[InputType.KAGINAWA.ordinal()].isTrigger() )
		{
			kaginawaReleaseTimer = 0.0f;
		}
		else
		{
			kaginawaReleaseTimer += delta;
		}
	}

	@Override
	public final float getMoveLevel()
	{
		float result = 0.0f;
		if(inputHelpers[InputType.LEFT.ordinal()].isPress())	result -= 1.0f;
		if(inputHelpers[InputType.RIGHT.ordinal()].isPress())	result += 1.0f;
		return result;
	}

	@Override
	public final boolean isDash()
	{
		return inputHelpers[InputType.DASH.ordinal()].isPress();
	}

	@Override
	public final boolean isJump()
	{
		return inputHelpers[InputType.JUMP.ordinal()].isPress();
	}

	@Override
	public final boolean isAerialJump()
	{
		return inputHelpers[InputType.JUMP.ordinal()].isTrigger();
	}

	@Override
	public final boolean isAttack()
	{
		return inputHelpers[InputType.ATTACK.ordinal()].isPress();
	}

	@Override
	public final boolean isKaginawaSlack()
	{
		return inputHelpers[InputType.KAGINAWA.ordinal()].isTrigger();
	}
	
	@Override
	public final boolean isKaginawaShrink()
	{
		return !inputHelpers[InputType.KAGINAWA.ordinal()].isPress();
	}

	@Override
	public final boolean isKaginawaHang()
	{
		return inputHelpers[InputType.KAGINAWA.ordinal()].isPress() && kaginawaReleaseTimer>=KAGINAWA_RELEASE_TIME;
	}

	@Override
	public final boolean isKaginawaRelease()
	{
		return inputHelpers[InputType.KAGINAWA.ordinal()].isRelease() && kaginawaReleaseTimer<KAGINAWA_RELEASE_TIME;
	}

	/**
	 * インスタンスを初期化する。
	 */
	protected abstract void initialize();
	
	/**
	 * キーボード入力を登録する。
	 * @param inputType		行動の種類
	 * @param key			キーコード(com.badlogic.gdx.Input.Keys)
	 * @see com.badlogic.gdx.Input.Keys
	 */
	protected final void registKey(InputType inputType, int key)
	{
		inputHelpers[inputType.ordinal()] = new KeyboardInputHelper(key);
	}
	
	/**
	 * マウス入力を登録する。
	 * @param inputType		行動の種類
	 * @param mouse			マウスコード(com.badlogic.gdx.Input.Buttons)
	 * @see com.badlogic.gdx.Input.Buttons
	 */
	protected final void registMouse(InputType inputType, int mouse)
	{
		inputHelpers[inputType.ordinal()] = new MouseInputHelper(mouse);
	}
	

	/** 
	 * 忍者に対する操作の種類
	 */
	protected enum InputType
	{
		/** 左入力 */
		LEFT,
		RIGHT,
		DASH,
		JUMP,
		ATTACK,
		KAGINAWA,
	}
	
	/** 入力補助オブジェクト */
	private InputHelperInterface inputHelpers[];
	
	/** 鉤縄を離す入力の判定用 */
	private final float KAGINAWA_RELEASE_TIME;
	
	/** 鉤縄を離す入力の判定用タイマー */
	private float kaginawaReleaseTimer;
	
	
	
	/** 
	 * 入力補助インタフェース
	 */
	private interface InputHelperInterface
	{
		/**
		 * 入力状態を更新する。
		 */
		void update();
		
		/**
		 * プレス入力を取得する。
		 * @return	プレス入力
		 */
		boolean isPress();

		/**
		 * トリガ入力を取得する。
		 * @return	トリガ入力
		 */
		boolean isTrigger();

		/**
		 * リリース入力を取得する。
		 * @return	リリース入力
		 */
		boolean isRelease();
	}
	
	/** 
	 * 入力補助基本クラス
	 */
	private abstract class AbstractInputHelper implements InputHelperInterface
	{
		/**
		 * 入力状態を更新する。
		 * @param newInput		新しい入力状態
		 */
		protected final void update(boolean newInput)
		{
			prev = now;
			now = newInput;
		}

		@Override
		public final boolean isPress()
		{
			return now;
		}

		@Override
		public final boolean isTrigger()
		{
			return !prev && now;
		}

		@Override
		public final boolean isRelease()
		{
			return prev && !now;
		}
		
		/** 入力状態 */
		protected boolean prev, now;
	}
	
	/** 
	 * キーボード入力補助クラス
	 */
	private final class KeyboardInputHelper extends AbstractInputHelper
	{
		/**
		 * コンストラクタ
		 * @param key		キーコード
		 */
		public KeyboardInputHelper(int key)
		{
			this.key = key;
		}
		
		@Override
		public final void update()
		{
			update(Gdx.input.isKeyPressed(key));
		}

		/** キーコード */
		private final int key;
	}
	
	/** 
	 * マウス入力補助クラス
	 */
	private final class MouseInputHelper extends AbstractInputHelper
	{
		/**
		 * コンストラクタ
		 * @param button		ボタンコード
		 */
		public MouseInputHelper(int button)
		{
			this.button = button;
		}

		@Override
		public final void update()
		{
			update(Gdx.input.isButtonPressed(button));
		}

		/** ボタンコード */
		private final int button;
	}
}
