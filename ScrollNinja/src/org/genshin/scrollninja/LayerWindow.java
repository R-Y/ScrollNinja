package org.genshin.scrollninja;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class LayerWindow {
	
	private final int		WIDTH = 128;
	private final int		HEIGHT = 720;
	
	private JFrame frm = new JFrame();					// 全体
	private JPanel panel = new JPanel();				// スクロールバー用	
	private JScrollPane scrollPane;						// スクロールバー
	private boolean frontFlag;							// 表示位置フラグ
//	private 
	
	/**
	 * コンストラクタ
	 */
	public LayerWindow(){
		frontFlag = true;
	}
	
	public void Init() {
		frm.toFront();
		frm.setTitle("レイヤー");
        frm.setSize(new Dimension(WIDTH,HEIGHT));
        frm.setBounds((int)ScrollNinja.window.x - WIDTH,0/*(int)ScrollNinja.window.y - a.top*/, WIDTH, HEIGHT);
		panel.setLayout( new GridLayout(0,1));

//    	for( int i = 0; i < 10; i ++ ) {
 //   		panel.add( new ButtonForEdit("data/shuriken.png", 0));
 //   	}
    	
    	scrollPane = new JScrollPane(panel);	// なんかコンストラクタで指定しないとうまく動かないわ
    	frm.add(scrollPane);
    	
//	    panel.add( new ButtonForEdit("data/shuriken.png", 0));
    	
    	frm.setVisible(true);
	}
	
	/**
	 * 更新
	 */
	public void Update() {
	    
		if( frontFlag ) {
			frm.setAlwaysOnTop(true);		// 常に前面だけど非アクティブ
		}
		else {
			frm.toBack();
			frm.setAlwaysOnTop(false);
		}
	}
}
