package com.zheleproduction.zhelesnake.myInterface;
import android.graphics.*;
import android.view.View.*;

public class LabeledButton extends FullButton
{
	protected String label;
	private Path path=new Path();
	protected static Paint firstLayerP;
	protected static Paint secondLayerP;
	protected static Paint thirdLayerP;
	protected static Paint thirdLayerPressedP;
	protected static Paint firstLayerPressedP;
	protected Paint textPaint;
	public Rect inv;
  //  static boolean active;
    public boolean pressed;
	
	public LabeledButton(String s)
	{
		super();
	//	active=false;
		pressed=false;
		textPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setColor(Color.YELLOW);
		textPaint.setTextAlign(Paint.Align.CENTER);
		
	
		if(firstLayerP==null)
		{
			firstLayerP=new Paint(Paint.ANTI_ALIAS_FLAG);
			firstLayerP.setColor(Color.BLACK);
		}
		
		
		if(firstLayerPressedP==null)
		{
			firstLayerPressedP=new Paint(Paint.ANTI_ALIAS_FLAG);
			firstLayerPressedP.setColor(Color.BLACK);
		}
		if(secondLayerP==null)
		{
			secondLayerP=new Paint(Paint.ANTI_ALIAS_FLAG);
			secondLayerP.setColor(Color.WHITE);
		}
		
		if(thirdLayerP==null)
		{
			thirdLayerP=new Paint(Paint.ANTI_ALIAS_FLAG);
			thirdLayerP.setColor(Color.RED);
			thirdLayerP.setAlpha(150);
		}
		
		if(thirdLayerPressedP==null)
		{
			thirdLayerPressedP=new Paint(Paint.ANTI_ALIAS_FLAG);
			thirdLayerPressedP.setColor(Color.rgb(150,0,0));
			thirdLayerPressedP.setAlpha(176);
		}
		
		label=s;
		inv=new Rect();
	}
	/*void setLabel(String s)
	{
		label=s;
		textPaint.setTextSize(color.height()*3/4);
		Rect bounds=new Rect();
		textPaint.getTextBounds(label,0,label.length(),bounds);
		if(bounds.width()>color.width()-color.height()/8)
		{
			textPaint.setTextSize(textPaint.getTextSize()*(color.width()-color.height()/8)/bounds.width());
			textPaint.getTextBounds(label,0,label.length(),bounds);
		}
		path.moveTo(black.left,black.top+black.height()/2+bounds.height()*2/5);
		path.lineTo(black.right,black.top+black.height()/2+bounds.height()*2/5);
		
	}*/
	public void setBlackRect(float left,float top,float right,float bottom)
	{
		black.set(left,top,right,bottom);
		float	height=black.height();
		white.set(left+height/16,top+height/16,right-height/16,bottom-height/16);
		color.set(left+height/8,top+height/8,right-height/8,bottom-height/8);
		roundBlack=height/2;
		roundWhite=white.height()/2;
		roundColor=color.height()/2;
		
		
		textPaint.setTextSize(color.height()*3/4);
		Rect bounds=new Rect();
		textPaint.getTextBounds(label,0,label.length(),bounds);
		if(bounds.width()>color.width()-color.height()/8)
		{
			textPaint.setTextSize(textPaint.getTextSize()*(color.width()-color.height()/8)/bounds.width());
			textPaint.getTextBounds(label,0,label.length(),bounds);
		}
		//	path.moveTo(full.color.left+full.color.width()/2-bounds.width()/2,top+height/2+bounds.height()/3);
        //	path.lineTo(full.color.right+bounds.width()/2+full.color.width()/2,top+height/2+bounds.height()/3);
		path.moveTo(left,top+height/2+bounds.height()*2/5);
		path.lineTo(right,top+height/2+bounds.height()*2/5);
		
		firstLayerP.setShadowLayer(5,height/4,height/4,Color.DKGRAY);

		firstLayerPressedP.setShadowLayer(5,height/8,height/8,Color.DKGRAY);
		
		inv.set((int)black.left-1,(int)black.top-1,(int)black.right+(int)(height/4),(int)black.bottom+(int)(height/4));
	}

	public void unpress()
	{
		pressed=false;
	//	if(mode.equals("restart"))
		//	ProDraw.needRestart=true;
	}
	
    public boolean isPressed(float X,float Y)
	{
		boolean b=false;
		if(black.contains(X,Y))
			b=true;
		return b;
	}
	
	public void onPress()
	{
		pressed=true;
		
	}
	public void draw(Canvas c)
	{
		c.save();
		if(pressed)
		{
			c.translate(black.height()/16,black.height()/16);
			c.drawRoundRect(black,roundBlack,roundBlack,firstLayerPressedP);
			c.drawRoundRect(white,roundWhite,roundWhite,secondLayerP);
			c.drawRoundRect(color,roundColor,roundColor,thirdLayerPressedP);
			
		}
		else
		{
	    	c.drawRoundRect(black,roundBlack,roundBlack,firstLayerP);
			c.drawRoundRect(white,roundWhite,roundWhite,secondLayerP);
			c.drawRoundRect(color,roundColor,roundColor,thirdLayerP);
		}
		c.drawTextOnPath(label,path,0f,0f,textPaint);
		c.restore();	
	}
}
