package com.zheleproduction.zhelesnake;

import android.graphics.*;

public class Cell
	{
		RectF black;
		RectF white;
		RectF color;
		Paint bigP;
		Paint blockP;
		Paint snakeP;

		public Cell()
		{
			black=new RectF();
			white=new RectF();
			color=new RectF();
			
			bigP=new Paint(Paint.ANTI_ALIAS_FLAG);
			blockP=new Paint(Paint.ANTI_ALIAS_FLAG);
			snakeP=new Paint(Paint.ANTI_ALIAS_FLAG);
		}
		public void setRectsAndP(int i,int kwidth,int kheight,int r,int stx,int sty)
		{
			int pdelr=r/32;
			int pdel=r/16;
			RectF rect=new RectF();
			rect.left=(float)((i%kwidth)*r+pdelr+stx);
			rect.top = (float)((i/kwidth)*r+pdelr+sty);
			rect.right=rect.left +r-2*pdelr;
			rect.bottom=rect.top+r-2*pdelr;

			//кисть нижнего слоя
			bigP.setColor(Color.BLACK);
			bigP.setShadowLayer(r/10f,-((kwidth*r+2*stx)/2.0f-(rect.left+rect.right)/2)/(2*(kwidth-1.0f)),
											 -((kheight*r+2*sty)/2.0f-(rect.bottom+rect.top)/2)/(2*(kheight-1.0f)),Color.argb(170,30,30,30));

			//нижний рект
			black.set(rect);

			
			
			//средний рект
			rect.left+=pdel;
			rect.top+= pdel;
			rect.right-=pdel;
			rect.bottom-=pdel;

			white.set(rect);

			//цветной рект
			rect.left+=pdel;
			rect.top+= pdel;
			rect.right-=pdel;
			rect.bottom-=pdel;

			color.set(rect);

			//кисти верхнего слоя(змеи и блока)

			float smallX=-((kwidth*r+2*stx)/2.0f-(rect.left+rect.right)/2)/(2*(kwidth-1.0f));
			float smallY=-((kheight*r+2*sty)/2.0f-(rect.bottom+rect.top)/2)/(2*(kheight-1.0f));


			blockP.setColor(Color.argb(170,0,0,0));
			blockP.setShadowLayer(r/10f,smallX,smallY,Color.argb(255,30,30,30));
			snakeP.setColor(Color.argb(170,255,0,0));
			snakeP.setShadowLayer(r/10f,smallX,smallY,Color.argb(255,30,30,30));
		}
	}
