package com.zheleproduction.zhelesnake.myInterface;

import android.graphics.*;

public class StartScreen extends SlideScreen
{
	Bitmap bitmap;
	public Canvas canvas;
	Paint bitmapP;
	public StartScreen(float w,float h)
	{
		super(w,h);
		
		bitmapP= new Paint(Paint.ANTI_ALIAS_FLAG);
		bitmap= Bitmap.createBitmap((int)w,(int)h,Bitmap.Config.ARGB_8888);
		canvas=new Canvas(bitmap);
	}

	@Override
	void initButtons()
	{
		buttons=new LabeledButton[1];
		buttons[0]=new LabeledButton("Start");
		buttons[0].setBlackRect(1f/2*w-w/6f,h/2f-w/12f,w/2f+w/6f,h/2f+w/12f);
	}
	@Override
	String getLabel()
	{
		String l=new String("");
		return l;
	}

	@Override
	public void draw(Canvas c)
	{
		c.drawBitmap(bitmap,0,0,bitmapP);
		super.draw(c);
	}
	
	
}
