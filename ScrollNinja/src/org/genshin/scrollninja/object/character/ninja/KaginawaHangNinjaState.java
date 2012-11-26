package org.genshin.scrollninja.object.character.ninja;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * 鉤縄にぶら下がっている時の忍者の処理。<br>
 * 忍者の状態に合わせた処理を実行し、必要に応じて別の状態に遷移させる。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class KaginawaHangNinjaState extends AbstractKaginawaNinjaState
{
	@Override
	public NinjaStateInterface update(PlayerNinja me, float deltaTime)
	{
		//---- 姿勢を起こす
		nearRotate(me, 0.0f, 0.1f);
		
		//---- あとは基本クラスに任せる。
		return super.update(me, deltaTime);
	}
	
	@Override
	public void collisionTerrain(PlayerNinja me, Contact contact)
	{
		//---- 衝突したのが下半身でなければ何もしない。
		final Fixture footFixture = me.getFootFixture();
		if(contact.getFixtureA() != footFixture && contact.getFixtureB() != footFixture)
			return;
		
		//---- 下半身が地面に衝突した場合、自動的に鉤縄を切断する。
		me.kaginawa.release();
	}
	
	@Override
	protected void updateKaginawa(PlayerNinja me)
	{
		//---- 操作状態に合わせて各種処理を実行する。
		// 鉤縄を離し、同時にジャンプする。
		if( me.controller.isAerialJump() )
		{
			me.kaginawa.release();
			jump(me);
			if( me.restAerialJumpCount > 0 )
			{
				me.restAerialJumpCount--;
			}
		}
		// 鉤縄を離す
		else if( me.controller.isKaginawaRelease() )
		{
			me.kaginawa.release();
		}
		// 鉤縄を縮める
		else if( me.controller.isKaginawaShrink() )
		{
			me.kaginawa.shrink();
		}
		
		//---- 忍者のアニメーションを設定する。
		// 上半身
		me.setBodyAnimation("Kaginawa");
		
		// 下半身
		final Vector2 ninjaDirection = me.controller.getDirection();
		final Vector2 kaginawaDirection = me.kaginawa.getBody().getPosition().tmp().sub(me.getBody().getPosition());
		if( (Vector2.Y.crs(ninjaDirection) < 0.0f) == (Vector2.Y.crs(kaginawaDirection) < 0.0f) )
		{
			me.setFootAnimation("KaginawaFront");
		}
		else
		{
			me.setFootAnimation("KaginawaBack");
		}
		
		// アニメーション時間の同期
		me.getFootRenderObject().setAnimationTime( me.getBodyRenderObject().getAnimationTime() );
	}

	@Override
	protected NinjaStateInterface getNextState(PlayerNinja me)
	{
		//---- 鉤縄が縮んでいる状態へ
		if( me.kaginawa.isShrinkState() )
		{
			return new KaginawaShrinkNinjaState();
		}
		
		//---- あとは基本クラスに任せる。
		return super.getNextState(me);
	}

}
