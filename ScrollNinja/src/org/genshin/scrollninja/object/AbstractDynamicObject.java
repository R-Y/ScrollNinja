package org.genshin.scrollninja.object;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
 * 動くオブジェクトの基本クラス
 * @author kou
 *
 */
public abstract class AbstractDynamicObject extends AbstractObject
{
	/**
	 * 古いソースとの整合性を保つためのコンストラクタ。
	 * FIXME 最終的には消し去る。
	 */
	@Deprecated
	public AbstractDynamicObject()
	{
		/* 何もしない */
	}
	
	/**
	 * コンストラクタ
	 * @param world		所属するWorldオブジェクト
	 */
	public AbstractDynamicObject(World world)
	{
		super(world);
	}

	@Override
	protected BodyDef createBodyDef()
	{
		BodyDef bd = super.createBodyDef();
		bd.type		= BodyType.DynamicBody;		// 動くオブジェクト
		bd.bullet	= true;						// すり抜けない
		return bd;
	}
}
