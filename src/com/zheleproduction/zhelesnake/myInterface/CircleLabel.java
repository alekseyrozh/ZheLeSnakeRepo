package com.zheleproduction.zhelesnake.myInterface;
import android.graphics.*;
import android.animation.*;
import android.util.*;

public class CircleLabel
{
	Bitmap numBitmap;
	public static float v0GO;
	public static float k;
	public static float kSpeed;
	public static float vCircle;
	public static float aCircle=720f;
	public static float kAccel;
	static Paint circleTP;
	public static float ang;
	static Paint p;
	static Paint p2;
	static float shift;
	public static Mode mode;
	private static float decSpeed;
	
	public float tW;
	public float wordLenDeg;
    float centX,centY,r;
	Path path;
	public Rect rect;
	public float pathLen;
	String label;
	public Mode privMode;
	public int num;
	
	private float tS;
	private int invert;
	private Position pos;
	private RectF finalRect;
	private Canvas c;
//	private RectF rf;
	private static RectF prepRect;
	private static Rect leftRect;
	private static Paint finalPaint;
	private static int i;
	private static boolean scaled;
	private static Paint fillPaint;
	public enum Position
	{
		LEFT,RIGHT;
	}
	
    public enum Mode
	{
		NORMAL,GAMEOVER,FINAL;
	}


	public CircleLabel(String s)
	{
		
		path=new Path();
		rect=new Rect();
		c=new Canvas();
		leftRect=new Rect();
		finalRect=new RectF();
		prepRect=new RectF();
		if(circleTP==null)
		{
			circleTP=new Paint(Paint.ANTI_ALIAS_FLAG);
			circleTP.setColor(Color.WHITE);
			circleTP.setTextAlign(Paint.Align.CENTER);
		}
		
		if(fillPaint==null)
		{
			fillPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
			fillPaint.setColor(Color.LTGRAY);
			fillPaint.setAlpha(50);
			fillPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		}
		
		if(p==null)
		{
			p=new Paint(Paint.ANTI_ALIAS_FLAG);
			p.setColor(Color.WHITE);
			p.setTextAlign(Paint.Align.CENTER);
			p.setStyle(Paint.Style.FILL_AND_STROKE);
		}
		
		if(p2==null)
		{
			p2=new Paint(Paint.ANTI_ALIAS_FLAG);
			p2.setColor(Color.YELLOW);
			p2.setAlpha(150);
			p2.setStrokeWidth(5);
			p2.setTextAlign(Paint.Align.CENTER);
			p2.setStyle(Paint.Style.STROKE);
		}
		
		if(finalPaint==null)
		{
			finalPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
			finalPaint.setColor(Color.WHITE);
			finalPaint.setTextAlign(Paint.Align.CENTER);
		}
		label=s;
		pathLen=0;
	}
	
	public void prepareBitmap()
	{
		scaled=false;
		i=0;
	//	c.drawCircle(rect.width()/2f,rect.height()/2f,rect.width()/2,fillPaint);
	//	c.drawCircle(c.getWidth()/2f,c.getWidth()/2f,c.getWidth()/2f,fillPaint);
		c.drawText(Integer.toString(num),rect.centerX()-rect.left,rect.centerY()-rect.top+circleTP.getTextSize()/2,circleTP);
	}
    public void createFinal(int w,int h)
	{
		path.reset();
		p.setTextAlign(Paint.Align.CENTER);
		if(pos==Position.RIGHT)
		{
			RectF rec=new RectF(w-(centY+3*r),h-centY/2f-4*r,w-(centY-r),h-centY/2f);
	    	path.addArc(rec ,180,180);
		}
		else
		{
			RectF rec=new RectF(centY-r,h-centY/2f-4*r,centY+3*r,h-centY/2f);
			path.addArc(rec ,180,180);
		}
		privMode=Mode.FINAL;
		i=0;
		finalPaint.setTextSize(circleTP.getTextSize()*2);
		finalPaint.setAlpha(0);
	}
	
	public void reset(int w,int h, Position pos)
	{
		//mode=Mode.NORMAL;
		num=0;
		circleTP.setTextSize((int)(2*Math.PI*Math.abs((h-w)/4)/25));
		decSpeed=circleTP.getTextSize()/10f;
		privMode=Mode.NORMAL;
		k=0;
		ang=0;
		this.pos=pos;
		p.setTextSize((int)(2*Math.PI*Math.abs((h-w)/4f)/25f));
		tS=p.getTextSize();
		Rect b=new Rect();
		p.getTextBounds(label,0,label.length(),b);
		tW=b.width();
		r=Math.abs(w-h)/8f;
		vCircle=45f;
		centY = Math.abs((w-h)/4f);
		if(pos==Position.LEFT)
			resetLeft(w,h);
		else
			resetRight(w,h);
		numBitmap= Bitmap.createBitmap(rect.width(),rect.height(),Bitmap.Config.ARGB_8888);
		c.setBitmap(numBitmap);
		i=0;
	}
	
	static public void updateNum(double dt)
	{
		switch (mode)
	    {
			case NORMAL:
	    	case GAMEOVER:
			if(!scaled)
		   		i++;
				//fillPaint.setAlpha(255-i);
			break;
			case FINAL:
				if(i<254)
					i+=3;
			break;
		//	case FINAL: circleTP.setTextSize(circleTP.getTextSize()+(float)dt*decSpeed);
		//	break;
		}
	}
	
	private void resetLeft(int w,int h)
	{
		invert=1;
		centX=centY;
		path.reset();
		float tS=p.getTextSize();
		path.addCircle(centX,centY,r,Path.Direction.CW);
		rect.set((int)(centX-r-tS)-1,(int)(centY-r-tS)-1,(int)(centY+r+tS)+1,(int)(centY+r+tS)+1);
		wordLenDeg=degrees(label,"SCORE","    1");
        leftRect.set(rect);
	}
	private void resetRight(int w, int h)
	{
		invert=-1;
		centX=w-centY;
		path.reset();
		float tS=p.getTextSize();
		path.addCircle(centX,centY,r,Path.Direction.CW);
		rect.set((int)(w-(centY+r+tS)-1),(int)(centY-tS-r-1),(int)(w-centY+r+tS)+1,(int)(centY+r+tS)+1);
		wordLenDeg=degrees(label,"TIME","           1");
	}

	private float degrees(String full, String part,String add)
	{
		Rect bounds=new Rect();
		full+=add;
		p.getTextBounds(full,0,full.length(),bounds);
		float t;
		t=bounds.width();
		p.getTextBounds(part,0,part.length(),bounds);
		return bounds.width()/t*360;
	}
	
	public void createGameOverPath(int w,int h)
	{
		if(mode==Mode.NORMAL)
		{
			path.reset();
			RectF oval=new RectF();
			oval.set(rect);
			oval.left+=tS;
			oval.top+=tS;
			oval.right-=tS;
			oval.bottom-=tS;
			k=0;
			
		//	float R=w/5f;
			if(pos==Position.RIGHT)
			{
				finalRect.set(w-(centY+3*r),h-centY/2f-4*r,w-(centY-r),h-centY/2f);
				
				path.arcTo(finalRect, (180+wordLenDeg/4f), (180+60+wordLenDeg/4f));
				//path.moveTo(w-centY-r, h-centY/2f);
				path.quadTo(0, h, centX-r-1, centY);
     			//path.arcTo(oval,120+wordLenDeg/2-ang,-(120-ang+180+wordLenDeg/2f));
				path.arcTo(oval,180,180+120+wordLenDeg/2f-ang);
			}
			else
			{
		        path.arcTo(oval,180-120-wordLenDeg/2f+ang,(120-ang+180+wordLenDeg/2f));
	    		path.quadTo(w, h, centY+r, h-centY/2f);
			//	RectF rec=new RectF(centY-r,h-centY/2f-4*r,centY+3*r,h-centY/2f);
			    finalRect.set(centY-r,h-centY/2f-4*r,centY+3*r,h-centY/2f);
			    path.arcTo(finalRect, 90, 180+60+wordLenDeg/4f);
			   // path.addArc(rec,90,90);
			}
			
			p.setTextAlign(Paint.Align.LEFT);
	 	    k=0;
			pathLen=(new PathMeasure(path,false)).getLength();
			kSpeed=(float)(vCircle/360*2*Math.PI*r);
			v0GO=kSpeed;
			kAccel=(float)(aCircle/360*2*Math.PI*r);
			

			privMode=Mode.GAMEOVER;
	    }
	}
	
	public static void stop()
	{
		CircleLabel.kAccel=0;
		CircleLabel.kSpeed=0;
	//	mode=Mode.FINAL;
	}
	
	
/*	
	public void startMoving()
	{
		ObjectAnimator animk=ObjectAnimator.ofFloat(this,"k",this.pathLen);
		animk.start();
	}*/



/*	private RectF scale(RectF rect, float i)
	{
		
	}*/
/*
	Rect rectFToRect(RectF rectF)
	{
		Rect r=new Rect();
		r.left=(int)rectF.left-1;
	    r.right=(int)rectF.right+1;
		r.bottom=(int)rectF.bottom+1;
	    r.top=(int)rectF.top-1;
		return r;
	}*/
	/*
	private RectF scale(RectF rect,float i)
	{
		Rect r= new Rect(rectFToRect(rect));
		return scale(r, i);
	}*/
	
	private RectF scale(Rect rect,float i)
	{
		switch (mode)
		{
			case NORMAL:
			case GAMEOVER:
			if(pos==Position.LEFT)
			{
	        	if(!scaled)
		    	{
			    	prepRect.set(rect);
			    	prepRect.bottom-=decSpeed*i;
			    	prepRect.left+=decSpeed*i;
			    	prepRect.right-=decSpeed*i;
			    	prepRect.top+=decSpeed*i;
	            	if(4*i>rect.width())
				    	scaled=true;
	        	}
		    	else
				{
					prepRect.setEmpty();
				}
			}
			break;
		/*	case FINAL:
				prepRect.set(finalRect);
				rf.left=2*i;
				rf.right=rect.width()-2*i;
				rf.top=2*i;
				rf.bottom=rect.height()-2*i;*/
		}		
		return prepRect;
	}

	/*
	private RectF prepareFinalRectF(float i)
	{
		prepRect.set(finalRect);
		prepRect.left+=i*4;
		prepRect.top+=i*4;
		return prepRect;
	}*/
	
	public void draw(Canvas canvas, boolean gmover)
	{
		switch (mode)
		{
		case NORMAL:
			  // if(!gmover)
			  //    canvas.drawCircle(centX,centY,rect.width()/2,fillPaint);
	     	   canvas.save();
	      	   if(pos==Position.RIGHT)
		      	 canvas.rotate(invert*ang+180,centX,centY);
	    	   else
		       	 canvas.rotate(invert*ang,centX,centY);
        	canvas.drawTextOnPath(label,path,0,0,p);
	    	canvas.restore();
		    if(!gmover)
		    	canvas.drawText(Integer.toString(num),centX,centY+circleTP.getTextSize()/2,circleTP);
        	else
			//    canvas.drawBitmap(numBitmap,rect.left,rect.top,circleTP);
			{
				canvas.save();
				canvas.translate(rect.left,rect.top);
				//canvas.drawRect(rect,p2);
				canvas.drawBitmap(numBitmap,leftRect,scale(leftRect,i),circleTP);
				canvas.restore();
			}
		break;
		case GAMEOVER:
	    	if(pos==Position.LEFT)
            	canvas.drawTextOnPath(label,path,k,0,p);
	    	else
				canvas.drawTextOnPath(label,path,pathLen-tW-k,0,p);
			
		//	canvas.drawText(Integer.toString(num),centX,centY+circleTP.getTextSize()/2,circleTP);
		   // canvas.drawBitmap(numBitmap,rect.left,rect.top,p);
			//	canvas.drawBitmap(numBitmap,rect,scale(rect,i),circleTP);
				
					canvas.save();
				/*	
			    	if(pos==Position.RIGHT)
			    	    canvas.drawText(Float.toString(scale(rect,i).width()),200,300,circleTP);
					else
						canvas.drawText(Float.toString(prepRect.w),100,300,circleTP);
				*/  
					canvas.translate(rect.left,rect.top);
			//	(	canvas.drawRect(scale(rect,i),p2);
				//	canvas.drawRect();
				    
					canvas.drawBitmap(numBitmap,leftRect,scale(leftRect,i),circleTP);
					canvas.restore();
		break;
		case FINAL:
			canvas.drawTextOnPath(label,path,0,0,circleTP);
			finalPaint.setAlpha(i);
			canvas.drawText(Integer.toString(num),finalRect.centerX(),finalRect.centerY()+finalPaint.getTextSize()/2f,finalPaint);
	//	    canvas.save()
	
	         //   canvas.drawRect(finalRect,p2);
			//	canvas.save();
			//	canvas.translate(finalRect.left-scale(finalRect,i+3*5).right+finalRect.right+finalRect.right,finalRect.top);
			//	canvas.drawRect(prepareFinalRectF(i+3*5),p2);
			//	canvas.restore();
		//	canvas.drawBitmap(numBitmap,rectFToRect(finalRect),scale(finalRect,i),circleTP);
	//	break;
		}
	//	canvas.drawPath(path,p2);
		
	}
	
}
