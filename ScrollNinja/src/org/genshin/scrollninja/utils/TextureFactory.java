package org.genshin.scrollninja.utils;

import org.genshin.engine.resource.factory.AbstractResourceFactory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;

/**
 * テクスチャを生成・管理するクラス（シングルトン）<br>
 * テクスチャを取得する際、既に同じファイルから作られたテクスチャが存在する場合はそれを使い回す。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public final class TextureFactory extends AbstractResourceFactory<Texture, String>
{
	/**
	 * コンストラクタ
	 */
	private TextureFactory()
	{
		/* 何もしない */
	}

	/**
	 * シングルトンインスタンスを取得する。
	 * @return		シングルトンインスタンス
	 */
	public static TextureFactory getInstance()
	{
		return instance;
	}
	
	@Override
	protected Texture create(String id)
	{
		Texture texture = new Texture(id);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		return texture;
	}

	/** シングルトンインスタンス */
	private static final TextureFactory instance = new TextureFactory(); 
}
