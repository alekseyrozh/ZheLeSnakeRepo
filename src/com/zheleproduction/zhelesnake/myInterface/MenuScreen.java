package com.zheleproduction.zhelesnake.myInterface;


import android.animation.*;
import android.graphics.*;

public class MenuScreen extends SlideScreen
{
    Bitmap startBitmap;
	private Bitmap endBitmap;
	private Paint startP;
	private Paint endP;
	public Canvas startCanvas;
	public Canvas endCanvas;
	private ObjectAnimator backAnimOff;
	private ObjectAnimator backAnimIn;
	public boolean needSecondScreen;

	public MenuScreen(float w,float h)
	{
		super(w,h);
		
		
		startP= new Paint(Paint.ANTI_ALIAS_FLAG);
		endP= new Paint(Paint.ANTI_ALIAS_FLAG);
		backAnimOff=ObjectAnimator.ofInt(startP,"alpha",0);
	    backAnimOff.setDuration(1000);

		backAnimIn=ObjectAnimator.ofInt(endP,"alpha",255);
	    backAnimIn.setDuration(1000);

		startBitmap= Bitmap.createBitmap((int)w,(int)h,Bitmap.Config.ARGB_8888);
		startCanvas=new Canvas(startBitmap);
		endBitmap= Bitmap.createBitmap((int)w,(int)h,Bitmap.Config.ARGB_8888);
		endCanvas=new Canvas(endBitmap);

		needSecondScreen=false;
	}


	@Override
	void initButtons()
	{
		buttons=new LabeledButton[4];
		buttons[0]=new LabeledButton("Restart");
		buttons[1]=new LabeledButton("Main Menu");
		buttons[2]=new LabeledButton("Resume");
		buttons[3]=new LabeledButton("Level Menu");
		buttons[0].setBlackRect(1f/12*w,h/2f-w/12f,w/2f-1f/12*w,h/2f+w/12f);
		buttons[1].setBlackRect(w/2f+1f/12*w,h/2f-w/12f,w-1f/12*w,h/2f+w/12f);
		buttons[2].setBlackRect(w/2f-1f/6*w,h/2f+2*w/12f,w/2f+1f/6*w,h/2f+4*w/12f);
		buttons[3].setBlackRect(w/2f-1f/6*w,h/2f-4*w/12f,w/2f+1f/6*w,h/2f-2*w/12f);
	}

	@Override
	public void draw(Canvas c)
	{
		c.drawBitmap(startBitmap,0,0,startP);
		c.drawBitmap(endBitmap,0,0,endP);

		super.draw(c);
	}

	@Override
	String getLabel()
	{
		String l=new String("MENU");
		return l;
	}

	@Override
	public void slideOut()
	{
		super.slideOut();
		startP.setAlpha(255);
		endP.setAlpha(0);
		if(needSecondScreen)
		{
	    	backAnimIn.start();
			backAnimOff.start();
			needSecondScreen=false;
		}
	}

	@Override
	public void slideIn()
	{
		super.slideIn();
		startP.setAlpha(255);
		//	backAnimOff.start();
		endP.setAlpha(0);
		//	backAnimIn.start();
	}
	
}
