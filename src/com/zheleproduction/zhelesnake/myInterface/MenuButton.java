package com.zheleproduction.zhelesnake.myInterface;

import android.graphics.*;

public class MenuButton extends LabeledButton
{
	Paint p;
	Path path;
	public MenuButton()
	{
		super("");
		path=new Path();
		p=new Paint(Paint.ANTI_ALIAS_FLAG);
		p.setColor(Color.YELLOW);
		p.setAlpha(150);
		p.setStyle(Paint.Style.FILL_AND_STROKE);
	}

	@Override
	public void setBlackRect(float left, float top, float right, float bottom)
	{
		super.setBlackRect(left, top, right, bottom);
		roundBlack=black.height()/3;
		roundWhite=white.height()/3;
		roundColor=color.height()/3;
		
	//	path.lineTo(-r/2+pdelr+2*pdel+roundColor,-r/2+pdelr+2*pdel);
	//	path.lineTo(-r/4+pdelr+2*pdel+roundColor,-r/2+pdelr+2*pdel);	path.lineTo(r/4,0);
	//	path.lineTo(-r/4+pdelr+2*pdel+roundColor,r/2-pdelr-2*pdel);
//	path.lineTo(-r/2+pdelr+2*pdel+roundColor,r/2-pdelr-2*pdel);
		//path.close();
		path.moveTo(color.left,color.centerY()    +color.height()/16);
		path.lineTo(color.centerX(),color.centerY()-color.height()/8   -color.height()/16);
		path.lineTo(color.right,color.centerY()   +color.height()/16);
		path.lineTo(color.right,color.centerY()+color.height()/8    +color.height()/16);
		path.lineTo(color.centerX(),color.centerY()    -color.height()/16);
		path.lineTo(color.left,color.centerY()+color.height()/8    +color.height()/16 );
		path.lineTo(color.left,color.centerY()      +color.height()/16);
		path.close();
	}

	@Override
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
		c.drawPath(path,p);
		c.restore();	
	}

	
}
