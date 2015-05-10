package com.zheleproduction.zhelesnake.myInterface;

import android.graphics.*;
import android.animation.*;

public abstract class SlideScreen
{
    protected int w,h;
	private float slide;
	protected static Paint bitmapP;
	protected Bitmap bitmap;
	protected Canvas bitmapCanvas;
	public LabeledButton[] buttons;
	public boolean buttonsAreActive;
	
	public ObjectAnimator slideAnim;
	public ObjectAnimator reverseSlideAnim;
	
	protected SlideScreen(float w,float h)
	{
		this.w=(int)w;
		this.h=(int)h;
	
		buttonsAreActive=false;
		bitmapP=new Paint(Paint.ANTI_ALIAS_FLAG);
	    bitmap= Bitmap.createBitmap(this.w,this.h,Bitmap.Config.ARGB_8888);
		bitmapCanvas=new Canvas(bitmap);
		drawBackground();
		initButtons();
		drawText();
		slideAnim=ObjectAnimator.ofFloat(this,"slide",h);
	    slideAnim.setDuration(1000);
		reverseSlideAnim=ObjectAnimator.ofFloat(this,"slide",0);
	    reverseSlideAnim.setDuration(1000);
	}

	
	public void setSlide(float slide)
	{
		this.slide = slide;
	}

	public float getSlide()
	{
		return slide;
	}
	
	private void drawBackground()
	{
		Paint p=new Paint(Paint.ANTI_ALIAS_FLAG);
		p.setColor(Color.DKGRAY);
		p.setAlpha(180);
		float k=Math.min(w,h);
		bitmapCanvas.drawRoundRect(new RectF(0,0,w,h),k/5f,k/5f,p);
	}
	
	private void drawText()
	{
	
		Path textPath=new Path();
		Paint textP=new Paint(Paint.ANTI_ALIAS_FLAG);
	
		textP.setColor(Color.WHITE);
		textP.setTextAlign(Paint.Align.CENTER);
		textP.setStyle(Paint.Style.STROKE);
		textP.setTextSize(w/6f);
		textP.setStrokeWidth(w/160f);
		textP.setShadowLayer(w/150f, w/25f, w/25f, Color.BLACK);


		textPath.moveTo(0,h/4f);
		textPath.lineTo(w,h/4f);
	//	String label=new String("GAME OVER");
		
		bitmapCanvas.drawTextOnPath(getLabel(),textPath,0,0,textP);
	}
	
	private void drawButtons(Canvas c)
	{
		for (int i=0;i<buttons.length;i++)
			buttons[i].draw(c);
	}
	
	public void slideIn()
	{
		slideAnim.start();
	}
	public void slideOut()
	{
		reverseSlideAnim.start();
	}
	

	abstract void initButtons();
	abstract String getLabel();
	public void draw(Canvas c)
	{
		c.save();
		c.translate(0,h-slide);
		c.drawBitmap(bitmap,0,0,bitmapP);
		drawButtons(c);
		c.restore();
	}
}
