
//package ru.ZheLeProducti.ZheLeSnake;
package com.zheleproduction.zhelesnake;

import android.animation.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.zheleproduction.zhelesnake.myInterface.*;
import com.zheleproduction.zhelesnake.util.*;
import java.io.*;
import java.util.*;


public class ProDraw extends View
{
//	GestureDetector gestureDetector;
	boolean firstDraw;
	double deltaTime;
	Context savedContext;
	Bitmap fieldBitmap;
	Canvas fieldCanvas;
    
	Paint whiteP=new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint fieldP=new Paint(Paint.ANTI_ALIAS_FLAG);
	Paint pathP=new Paint(Paint.ANTI_ALIAS_FLAG);
	Paint curSnakeP=new Paint();
	Paint foodP=new Paint();
    static int kwidth, kheight;
    int stx,sty;
    boolean[] field;
	float prevAng;
	boolean timer;
	boolean win;

	String gameOverString=new String("GAME OVER");
	CircleLabel circleTime;
	CircleLabel circleScore;
	Path snakePath=new Path();
    Path pathArrow=new Path();
	Path timePath=new Path();
	
    static ArrayList<Direction> dir = new ArrayList<Direction>();

    //рисование квадратов
//	Paint gameOverP=new Paint(Paint.ANTI_ALIAS_FLAG);
//	Paint gameOverTextP=new Paint(Paint.ANTI_ALIAS_FLAG);
//	Paint gameOverBitmapP=new Paint(Paint.ANTI_ALIAS_FLAG);
	
    int r;
    int pdelr;//раница между половиной r и половиной рисующейся стороны.
    int pdel;//разница полусторон вложенных квадратов

	//Path circlePath=new Path();
	int width,height;
    float x,y;
    boolean touched = false;
    int delta = 40;
 
	long t0;
    int[] blocks;
	int time;
    float local;
    int foodx,foody,food;
    boolean gmover;
    int fps ,cfr;
    int score ;
    Random rn;
    int first;
    int length;
    //Long time ;
    //boolean	gameOverAnim;
    int[] mass;
  //  boolean gup = true;
    ProTimer protimer=new ProTimer(100000,33);
    Path path = new Path();
	Path gameOverPath = new Path();
	int radiusPE;
//	boolean restartButtonPressed;
//	LabeledButton menuButton=new LabeledButton("Menu");
	
//	LabeledButton restartButton=new LabeledButton("Restart");
//	LabeledButton menuButton=new LabeledButton("Main Menu");
	
	StartScreen startScreen;
	public static GameOverMenuScreen gameOverScreen;
	MenuScreen menuScreen;
	MenuButton menuButton=new MenuButton();
//	LabeledButton 
//	LabeledButton
	Rect scoreRect=new Rect();
	Rect timeRect=new Rect();
    float roundBlack;
	float roundWhite;
	float roundColor;
	Cell[] cellField;

	public void onRestart()
	{
	//	Toast.makeText(savedContext,"restarted",Toast.LENGTH_SHORT).show();
		kwidth=LevelInfo.kwidth;
		kheight=LevelInfo.kheight;
		mass= new int[kwidth*kheight];
		field=new boolean[kwidth*kheight];
		cellField = new Cell[kwidth*kheight];
		for (int i=0;i<kwidth*kheight;i++)
		    cellField[i]= new Cell();
		blocks=new int[LevelInfo.blocks.length];
	    System.arraycopy(LevelInfo.blocks,0,blocks,0,blocks.length);
		
		createLevelByIntent();
	    restart();
     	circleTime.reset(width,height,CircleLabel.Position.RIGHT);
		circleScore.reset(width,height,CircleLabel.Position.LEFT);
		CircleLabel.mode=CircleLabel.Mode.NORMAL;

	/*	cellField = new Cell[kwidth*kheight];
		for (int i=0;i<kwidth*kheight;i++)
		    cellField[i]= new Cell();*/
			
			
		r=Math.min(width/kwidth,(height)/kheight);
		//	if (width<height)
		//	{
		//r=width/kwidth;
		stx=(width-kwidth*r)/2;
		sty=(height-kheight*r)/2;
		pdelr=r/32;
		pdel=r/16;
		for(int i=0;i<kwidth*kheight;i++)
		{
			cellField[i].setRectsAndP(i,kwidth,kheight,r,stx,sty);
		}

		roundBlack=(cellField[0].black.width())/3;
		roundWhite=(cellField[0].white.width())/3;
		roundColor=(cellField[0].color.width())/3;





		pathArrow.lineTo(-r/2+pdelr+2*pdel+roundColor,-r/2+pdelr+2*pdel);
		pathArrow.lineTo(r/2-pdelr-2*pdel-roundColor,0);
		pathArrow.lineTo(-r/2+pdelr+2*pdel+roundColor,r/2-pdelr-2*pdel);
		pathArrow.close();

		pathP.setStrokeWidth(r/16);
		radiusPE=r/4;
		pathP.setPathEffect(new CornerPathEffect(radiusPE));


	 
		//	Toast.makeText(savedContext,Float.toString(local),Toast.LENGTH_SHORT).show();



			
			
        protimer.start();
		timer=false;
		

		t0=System.currentTimeMillis();
		firstDraw=true;

		gmover=false;
		win=false;
		newPath();

	    drawPictureOnCanvasWithoutCircles(fieldCanvas);

		startScreen.slideIn();
		logMemory("prodraw:");
	}
	
	public void onStop()
	{
		protimer.cancel();
	}
/*	private Bitmap blur(Bitmap original, float radius) 
	{
		Bitmap bitmap = Bitmap.createBitmap(
			original.getWidth(), original.getHeight(),
			Bitmap.Config.ARGB_8888);

		//	Activity a=MainActivity.class;
		RenderScript rs = RenderScript.create(savedContext);



		Allocation allocIn = Allocation.createFromBitmap(rs, original);
		Allocation allocOut = Allocation.createFromBitmap(rs, bitmap);		ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs,Element.U8_4(rs));
		 blur.setInput(allocIn);
		 blur.setRadius(radius);
		 blur.forEach(allocOut);

		 allocOut.copyTo(bitmap);

		rs.destroy();

		return bitmap;

	}*/
/*	static void goToMainMenu()
	{
		savedContext.startActivity(new Intent(savedContext,MainActivity.class));
		
	}*/
	
	float bxToX(int Bx)
	{
		return (Bx%kwidth)*r+stx;
	}
	float bxToY(int Bx)
	{
		return (Bx/kwidth)*r+sty;
	}
	void newPath()
	{
		snakePath.reset();
		int prev=mass[first];
		snakePath.moveTo(bxToX(prev)+r/2,bxToY(prev)+r/2);
		int cur;
		int px=0,py=0;
		//snakePath.moveTo(bxToX(mass[first])+r/2,bxToY(mass[first])+r/2);
		for(int i=1;i<length;i++)
		{
			cur=mass[(first-i+mass.length)%mass.length];
			
			if(i==length-1)
			{
				px=(cur%kwidth-prev%kwidth)*radiusPE;
				py=(cur/kwidth-prev/kwidth)*radiusPE;
			}
			
			if (Math.abs(cur%kwidth+cur/kwidth-prev/kwidth-prev%kwidth)==1)
			{
			
			    snakePath.lineTo(bxToX(cur)+r/2+px,bxToY(cur)+r/2+py);
			}
			else
			{
				if(cur%kwidth-prev%kwidth==kwidth-1)
				{
			    	snakePath.lineTo(bxToX(prev),bxToY(prev)+r/2);
					snakePath.moveTo(bxToX(cur)+r,bxToY(cur)+r/2);
				}
				else
				    if((cur%kwidth-prev%kwidth==-kwidth+1))
					{
				    	snakePath.lineTo(bxToX(prev)+r,bxToY(prev)+r/2);
						snakePath.moveTo(bxToX(cur),bxToY(cur)+r/2);
			        }
				    else
			        	if((cur/kwidth-prev/kwidth==-kheight+1))
			        	{
				    		snakePath.lineTo(bxToX(prev)+r/2,bxToY(prev)+r);
							snakePath.moveTo(bxToX(cur)+r/2,bxToY(cur));
						}
						else
							if((cur/kwidth-prev/kwidth==kheight-1))
							{
								snakePath.lineTo(bxToX(prev)+r/2,bxToY(prev));
								snakePath.moveTo(bxToX(cur)+r/2,bxToY(cur)+r);
							}
				snakePath.lineTo(bxToX(cur)+r/2-px/kwidth,bxToY(cur)+r/2-py/kheight);
				
			}
			prev=cur;
			
		//	if(Math.abs(mass[(first-i+mass.length)%mass.length]-mass[(first+1-i+mass.length)%mass.length])==1||Math.abs(mass[(first-i+mass.length)%mass.length]-mass[(first-1-i+mass.length)%mass.length])==kwidth)
	    //    	snakePath.lineTo(bxToX(mass[(first-i+mass.length)%mass.length])+r/2,bxToY(mass[(first-i+mass.length)%mass.length])+r/2);
		//	else
		//		snakePath.moveTo(bxToX(mass[(first-i+mass.length)%mass.length])+r/2,bxToY(mass[(first-i+mass.length)%mass.length])+r/2);
			
		}
	
	}
	
	Rect areaRect(int Bx)
	{
		Rect re=new Rect();
		re.left=(Bx%kwidth-1)*r+stx;
	    re.right=(Bx%kwidth+2)*r+stx;
		re.bottom=(Bx/kwidth+2)*r+sty;
	    re.top=(Bx/kwidth-1)*r+sty;
		return re;
	}

	void gameWin()
	{
		
	}
    void gameOver()
	{
		  //  goPathIsDrawn=false;
	//	savedCanvas.drawTextOnPath(gameOverString,gameOverPath,0,0,gameOverP);
	      //  Toast.makeText(savedContext,"error",Toast.LENGTH_SHORT).show();
	        gmover=true;
	        circleScore.prepareBitmap();
			circleTime.prepareBitmap();
	        
			Toast.makeText(savedContext,circleTime.num+" "+score,Toast.LENGTH_SHORT).show();
			
			
			/*
	    	gameOverCanvas.drawColor(Color.BLACK);
			//поле
     		gameOverCanvas.drawRect(stx,sty,stx+kwidth*r,sty+kheight*r,fieldP);
			//еда
			foodP.set(cellField[food].snakeP);
			foodP.setColor(Color.argb(150,0,255,0));
	    	gameOverCanvas.drawRoundRect(cellField[food].black,roundBlack*3/2,roundBlack*3/2,cellField[food].bigP);
		    gameOverCanvas.drawRoundRect(cellField[food].white,roundWhite*3/2,roundWhite*3/2,whiteP);
		    gameOverCanvas.drawRoundRect(cellField[food].color,roundColor*3/2,roundColor*3/2,foodP);


			int e=(first+1)%mass.length;
			int i=(first-length+1+mass.length)%mass.length;


			//тени 1 слой+Черн квад

			while(i!=e)
			{
				gameOverCanvas.drawRoundRect(cellField[mass[i]].black,roundBlack,roundBlack,cellField[mass[i]].bigP);
				i=(i+1)%mass.length;
			}

			//блоки
			for(int j=0;j<blocks.length;j++)
				gameOverCanvas.drawRoundRect(cellField[blocks[j]].black,roundBlack,roundBlack,cellField[blocks[j]].bigP);

			//белый кв
			i=(first-length+1+mass.length)%mass.length;
			while(i!=e)
			{
				gameOverCanvas.drawRoundRect(cellField[mass[i]].white,roundWhite,roundWhite,whiteP);
				i=(i+1)%mass.length;
			}

			//блоки
			for(int j=0;j<blocks.length;j++)
				gameOverCanvas.drawRoundRect(cellField[blocks[j]].white,roundWhite,roundWhite,whiteP);


			//красный

			i=(first-length+1+mass.length)%mass.length;
			int num=0;
			while(i!=e)
			{
				num++;
				curSnakeP.set(cellField[mass[i]].snakeP);
				curSnakeP.setColor(Color.argb(150,255*(num)/length,0,0));
				gameOverCanvas.drawRoundRect(cellField[mass[i]].color,roundColor,roundColor,curSnakeP);
				i=(i+1)%mass.length;
			}


			for(int j=0;j<blocks.length;j++)
				gameOverCanvas.drawRoundRect(cellField[blocks[j]].color,roundColor,roundColor,cellField[blocks[j]].blockP);
			newPath();
	    	gameOverCanvas.drawPath(snakePath,pathP);
			
			*/
			
		gameOverScreen.startCanvas.drawColor(Color.BLACK);
		//поле
		gameOverScreen.startCanvas.drawRect(stx,sty,stx+kwidth*r,sty+kheight*r,fieldP);
		//еда
		foodP.set(cellField[food].snakeP);
		foodP.setColor(Color.argb(150,0,255,0));
		gameOverScreen.startCanvas.drawRoundRect(cellField[food].black,roundBlack*3/2,roundBlack*3/2,cellField[food].bigP);
		gameOverScreen.startCanvas.drawRoundRect(cellField[food].white,roundWhite*3/2,roundWhite*3/2,whiteP);
		gameOverScreen.startCanvas.drawRoundRect(cellField[food].color,roundColor*3/2,roundColor*3/2,foodP);


		int e=(first+1)%mass.length;
		int i=(first-length+1+mass.length)%mass.length;


		//тени 1 слой+Черн квад

		while(i!=e)
		{
			gameOverScreen.startCanvas.drawRoundRect(cellField[mass[i]].black,roundBlack,roundBlack,cellField[mass[i]].bigP);
			i=(i+1)%mass.length;
		}

		//блоки
		for(int j=0;j<blocks.length;j++)
			gameOverScreen.startCanvas.drawRoundRect(cellField[blocks[j]].black,roundBlack,roundBlack,cellField[blocks[j]].bigP);

		//белый кв
		i=(first-length+1+mass.length)%mass.length;
		while(i!=e)
		{
			gameOverScreen.startCanvas.drawRoundRect(cellField[mass[i]].white,roundWhite,roundWhite,whiteP);
			i=(i+1)%mass.length;
		}

		//блоки
		for(int j=0;j<blocks.length;j++)
			gameOverScreen.startCanvas.drawRoundRect(cellField[blocks[j]].white,roundWhite,roundWhite,whiteP);


		//красный

		i=(first-length+1+mass.length)%mass.length;
		int num=0;
		while(i!=e)
		{
			num++;
			curSnakeP.set(cellField[mass[i]].snakeP);
			curSnakeP.setColor(Color.argb(150,255*(num)/length,0,0));
			gameOverScreen.startCanvas.drawRoundRect(cellField[mass[i]].color,roundColor,roundColor,curSnakeP);
			i=(i+1)%mass.length;
		}


		for(int j=0;j<blocks.length;j++)
			gameOverScreen.startCanvas.drawRoundRect(cellField[blocks[j]].color,roundColor,roundColor,cellField[blocks[j]].blockP);
		newPath();
		gameOverScreen.startCanvas.drawPath(snakePath,pathP);
		menuButton.draw(gameOverScreen.startCanvas);
		gameOverScreen.slideIn();
	}
		
    private void newFood()
    {
		if(circleScore!=null)
			circleScore.num++;
        int i;
        {
            rn = new Random();
            i=rn.nextInt(mass.length-length-blocks.length);
        }

        int p=-1;
        while (i>-1)
        {
            if(field[p+1])
                i--;
            p++;
        }
		food=p;
        foody = p/kwidth;
        foodx = p%kwidth;
	  
    }

	void drawPictureOnCanvas(Canvas c)
	{
		c.drawColor(Color.BLACK);
		//поле
		c.drawRect(stx,sty,stx+kwidth*r,sty+kheight*r,fieldP);
		

		int e=(first+1)%mass.length;
		int i=(first-length+1+mass.length)%mass.length;


		//тени 1 слой+Черн квад

		while(i!=e)
		{
			c.drawRoundRect(cellField[mass[i]].black,roundBlack,roundBlack,cellField[mass[i]].bigP);
			i=(i+1)%mass.length;
		}

		//блоки
		for(int j=0;j<blocks.length;j++)
			c.drawRoundRect(cellField[blocks[j]].black,roundBlack,roundBlack,cellField[blocks[j]].bigP);

		//белый кв
		i=(first-length+1+mass.length)%mass.length;
		while(i!=e)
		{
			c.drawRoundRect(cellField[mass[i]].white,roundWhite,roundWhite,whiteP);
			i=(i+1)%mass.length;
		}

		//блоки
		for(int j=0;j<blocks.length;j++)
			c.drawRoundRect(cellField[blocks[j]].white,roundWhite,roundWhite,whiteP);


		//красный

		i=(first-length+1+mass.length)%mass.length;
		int num=0;
		while(i!=e)
		{
			num++;
			curSnakeP.set(cellField[mass[i]].snakeP);
			curSnakeP.setColor(Color.argb(150,255*(num)/length,0,0));
			c.drawRoundRect(cellField[mass[i]].color,roundColor,roundColor,curSnakeP);
			i=(i+1)%mass.length;
		}


		for(int j=0;j<blocks.length;j++)
			c.drawRoundRect(cellField[blocks[j]].color,roundColor,roundColor,cellField[blocks[j]].blockP);
		newPath();
		c.drawPath(snakePath,pathP);
		CircleLabel.ang=0;
		circleScore.num=0;
		circleScore.draw(c,false);
		circleTime.draw(c,false);
		
		menuButton.draw(c);
	}
	
	void drawPictureOnCanvasWithoutCircles(Canvas c)
	{
		c.drawColor(Color.BLACK);
		//поле
		c.drawRect(stx,sty,stx+kwidth*r,sty+kheight*r,fieldP);


		int e=(first+1)%mass.length;
		int i=(first-length+1+mass.length)%mass.length;


		//тени 1 слой+Черн квад

		while(i!=e)
		{
			c.drawRoundRect(cellField[mass[i]].black,roundBlack,roundBlack,cellField[mass[i]].bigP);
			i=(i+1)%mass.length;
		}

		//блоки
		for(int j=0;j<blocks.length;j++)
			c.drawRoundRect(cellField[blocks[j]].black,roundBlack,roundBlack,cellField[blocks[j]].bigP);

		//белый кв
		i=(first-length+1+mass.length)%mass.length;
		while(i!=e)
		{
			c.drawRoundRect(cellField[mass[i]].white,roundWhite,roundWhite,whiteP);
			i=(i+1)%mass.length;
		}

		//блоки
		for(int j=0;j<blocks.length;j++)
			c.drawRoundRect(cellField[blocks[j]].white,roundWhite,roundWhite,whiteP);


		//красный

		i=(first-length+1+mass.length)%mass.length;
		int num=0;
		while(i!=e)
		{
			num++;
			curSnakeP.set(cellField[mass[i]].snakeP);
			curSnakeP.setColor(Color.argb(150,255*(num)/length,0,0));
			c.drawRoundRect(cellField[mass[i]].color,roundColor,roundColor,curSnakeP);
			i=(i+1)%mass.length;
		}


		for(int j=0;j<blocks.length;j++)
			c.drawRoundRect(cellField[blocks[j]].color,roundColor,roundColor,cellField[blocks[j]].blockP);
		newPath();
		c.drawPath(snakePath,pathP);

		menuButton.draw(c);
	}
	
	
    private void restart()
    {
		gmover=false;

	/*	if(MainActivity.appMode==MainActivity.AppMode.LOADGAME)
	       	read();
	   	else*/
	  // createEmptyLevel();
	   
	//	if(gameOverScreen!=null)
		//	drawPictureOnCanvas(gameOverScreen.endCanvas);
	/*		
		circleTime.reset(width,height,CircleLabel.Position.RIGHT);
		circleScore.reset(width,height,CircleLabel.Position.LEFT);
		CircleLabel.mode=CircleLabel.Mode.NORMAL;
		*/

	//	LabeledButton.active=false;

	/*	circleTime.reset(width,height,CircleLabel.Position.RIGHT);
		circleScore.reset(width,height,CircleLabel.Position.LEFT);
		CircleLabel.mode=CircleLabel.Mode.NORMAL;
		*/




		
        cfr=0;
        score=0;
        fps=5;
		time=0;
		
		circleTime.num=0;
		circleScore.num=0;
	
		newFood();
    }
	
	private void restartWithoutFood()
    {
	//	gmover=false;

	/*	if(MainActivity.appMode==MainActivity.AppMode.LOADGAME)
	       	read();
	   	else*/
	    //	createEmptyLevel();
			createLevelByIntent();
			
		//	Toast.makeText(savedContext,""+length,Toast.LENGTH_SHORT).show();
		//	if(gameOverScreen!=null)
		//	drawPictureOnCanvas(gameOverScreen.endCanvas);
		/*		
		 circleTime.reset(width,height,CircleLabel.Position.RIGHT);
		 circleScore.reset(width,height,CircleLabel.Position.LEFT);
		 CircleLabel.mode=CircleLabel.Mode.NORMAL;
		 */



		//	LabeledButton.active=false;

		


		/*	circleTime.reset(width,height,CircleLabel.Position.RIGHT);
		 circleScore.reset(width,height,CircleLabel.Position.LEFT);
		 CircleLabel.mode=CircleLabel.Mode.NORMAL;
		 */


        cfr=0;
        score=0;
        fps=5;
		time=0;

	//	circleTime.num=0;
	//	circleScore.num=0;

		newPath();
    }

	boolean read()
	{
		boolean bool=true;
		File f=new File(savedContext.getDir(null,savedContext.MODE_PRIVATE),"myfile.txt");
		try
		{
			f.createNewFile();
			Scanner sc=new Scanner(f);

			kwidth=sc.nextInt();
			kheight=sc.nextInt();

			field=new boolean[kwidth*kheight];
			mass=new int[kwidth*kheight];
		

			for(int i=0;i<mass.length;i++)
				field[i]=true;
				
			length=sc.nextInt();

			for(int i=0;i<length;i++)
				mass[i]=sc.nextInt();
			
			first=length-1;

			for(int i=0;i<length;i++)
			    field[mass[first-i]]=false;
			
			
			blocks=new int[sc.nextInt()];
			for(int i=0;i<blocks.length;i++)
			{
				blocks[i]=(sc.nextInt());
			}
			
			for(int i=0;i<blocks.length;i++)
				field[blocks[i]]=false;
				
	//		sc.nextInt()
		/*	dir.clear();
			switch(sc.nextInt())
			{
				case 0:
					dir.add(Direction.LEFT);
				break;
				case 1:
					dir.add(Direction.UP);
				break;
				case 2:
					dir.add(Direction.RIGHT);
				break;
				case 3:
					dir.add(Direction.DOWN);
				break;
			}*/
            sc.close();
		}
		catch (IOException e)
		{
			Toast.makeText(savedContext,e.getMessage(),Toast.LENGTH_SHORT).show();
			bool=false;
		}
		return bool;
	}
	

	void createLevelByIntent()
	{
		length=LevelInfo.snake.length;
		first=length-1;
		for(int i=0;i<length;i++)
			mass[first-i]=(int)LevelInfo.snake[length-1-i];
	 
		for(int i=0;i<mass.length;i++)
			field[i]=true;

		for(int i=0;i<length;i++)
			field[(int)mass[i]]=false;
			
	//	blocks=ProDrawActivity.blocks;
	
		for(int i=0;i<blocks.length;i++)
		    field[(int)blocks[i]]=false;

		dir.clear();
		dir.add(LevelInfo.dir);
	//	dir.add(ProDrawActivity.dir);
	}
	
	
/*	void createEmptyLevel()
	{
		kwidth=5;
		kheight=5;
		mass= new int[kwidth*kheight];
		field=new boolean[kwidth*kheight];
		length=2;
		first=1;
		for(int i=0;i<length;i++)
			mass[first-i]=length-i;
		
	    blocks=new int[0];
		
		for(int i=0;i<mass.length;i++)
			field[i]=true;
		
		for(int i=0;i<length;i++)
			field[mass[i]]=false;
		for(int i=0;i<blocks.length;i++)
		    field[blocks[i]]=false;
		
		dir.clear();
	    dir.add(Direction.RIGHT);
	}
*/
	
    public ProDraw(Context context)
    {
        super(context);
		
//		gestureDetector=new GestureDetector(savedContext, new MyGestureListener());
		FileHelper.prepareAll(context);
		
		savedContext=context;
	//	restartButtonPressed=false;
		circleScore= new CircleLabel("SCORE     SCORE     SCORE");
	    circleTime= new CircleLabel("TIME        TIME        TIME");

		kwidth=LevelInfo.kwidth;
		kheight=LevelInfo.kheight;
		mass= new int[kwidth*kheight];
		field=new boolean[kwidth*kheight];
		cellField = new Cell[kwidth*kheight];
		for (int i=0;i<kwidth*kheight;i++)
		    cellField[i]= new Cell();
		blocks=new int[LevelInfo.blocks.length];
		System.arraycopy(LevelInfo.blocks,0,blocks,0,blocks.length);
		
		createLevelByIntent();
        restart();
   
	//circleTime.reset(width,height,CircleLabel.Position.RIGHT);
	//	circleScore.reset(width,height,CircleLabel.Position.LEFT);
	//	CircleLabel.mode=CircleLabel.Mode.NORMAL;

	    pathP.setStyle(Paint.Style.STROKE);
		pathP.setColor(Color.YELLOW);
		pathP.setAlpha(150);
        whiteP.setColor(Color.WHITE);
		fieldP.setColor(Color.rgb(200,230,120));
	
	/*	cellField = new Cell[kwidth*kheight];
		for (int i=0;i<kwidth*kheight;i++)
		    cellField[i]= new Cell();

*/
	  
	//	bitmapCanvas=new Canvas();
	//	gameOverCanvas=new Canvas();
	//	gameOverBitmapCanvasWithButtons=new Canvas();
	
        protimer.start();
		timer=false;
		
	//	MainActivity.GAME_PHASE=MainActivity.Phase.S
    }
	
	@Override
	protected void onSizeChanged (int w, int h, int oldw, int oldh)
	{	
	//	Toast.makeText(savedContext,"onsize",Toast.LENGTH_SHORT).show();
		
			width=w;
			height=h;
			
			circleTime.reset(w,h,CircleLabel.Position.RIGHT);
			circleScore.reset(w,h,CircleLabel.Position.LEFT);
			CircleLabel.mode=CircleLabel.Mode.NORMAL;

		
		    menuButton.setBlackRect(w-w*5/24f,h-w*5/24f,w-w*1f/24,h-w/24f);
	        

	       r=Math.min(width/kwidth,(height)/kheight);
	//	if (width<height)
	//	{
			//r=width/kwidth;
			stx=(width-kwidth*r)/2;
			sty=(height-kheight*r)/2;

		
			fieldBitmap= Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
			fieldCanvas=new Canvas(fieldBitmap);
		
			startScreen=new StartScreen(w,h);
			gameOverScreen=new GameOverMenuScreen(w,h);
	    	menuScreen=new MenuScreen(w,h);
	    	gameOverScreen.slideAnim.addListener(	new Animator.AnimatorListener()
			{
				@Override
				public void onAnimationStart(Animator animation) 
				{
					MainActivity.GAME_PHASE=MainActivity.Phase.GAME_OVER_SCREEN;
					for(int i=0;i<gameOverScreen.buttons.length;i++)
						if(gameOverScreen.buttons[i].pressed)
							gameOverScreen.buttons[i].unpress();
				}

				@Override
				public void onAnimationRepeat(Animator animation) 
				{

				}

				@Override
				public void onAnimationEnd(Animator animation) 
				{
					//for(int i=0;i<gameOverScreen.buttons.length;i++)
					gameOverScreen.buttonsAreActive=true;
				}

				@Override
				public void onAnimationCancel(Animator animation) 
				{

				}
			});
			
			gameOverScreen.reverseSlideAnim.addListener(	new Animator.AnimatorListener() 
			{
				@Override
				public void onAnimationStart(Animator animation) 
				{
				//	for(int i=0;i<gameOverScreen.buttons.length;i++)
						gameOverScreen.buttonsAreActive=false;
					
				}

				@Override
				public void onAnimationRepeat(Animator animation) 
				{

				}

				@Override
				public void onAnimationEnd(Animator animation) 
				{
					for(int i=0;i<gameOverScreen.buttons.length;i++)
						if(gameOverScreen.buttons[i].pressed)
						{
						//	Toast.makeText(savedContext,Integer.toString(i),Toast.LENGTH_SHORT).show();
							
							gameOverScreen.buttons[i].unpress();
							switch(i)
							{
								case 0:
				
									
								//	restartWithoutFood();
									circleTime.reset(width,height,CircleLabel.Position.RIGHT);
									circleScore.reset(width,height,CircleLabel.Position.LEFT);
									CircleLabel.mode=CircleLabel.Mode.NORMAL;
									timer=false;
									gmover=false;
									startScreen.slideIn();
									//invalidate();
									break;
								case 1:
									MainActivity.GAME_PHASE=MainActivity.Phase.MAIN_MENU;
									Intent intent1 =new Intent(savedContext,MainActivity.class);
									intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
									LevelInfo.needLevelChoice=false;
									savedContext.startActivity(intent1);
							//		ProDrawActivity.activity.finish();
								break;
								case 2:
									MainActivity.GAME_PHASE=MainActivity.Phase.LEVEL_CHOICER;
									Intent intent=new Intent(savedContext,MainActivity.class);
									intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
									
									//intent.putExtra("needLevelChoice",true);
									LevelInfo.needLevelChoice=true;
									savedContext.startActivity(intent);
								//	ProDrawActivity.activity.finish();
									
							}
									//	Toast.makeText(savedContext,"rt",Toast.LENGTH_SHORT).show();
								//	savedContext.startActivity(new Intent(savedContext,MainActivity.class));
							
						}
				     	CircleLabel.ang=0;
						circleScore.num=0;
				}

				@Override
				public void onAnimationCancel(Animator animation) 
				{

				}
			}
		);
		
		menuScreen.slideAnim.addListener(	new Animator.AnimatorListener() 
			{
				@Override
				public void onAnimationStart(Animator animation) 
				{
					MainActivity.GAME_PHASE=MainActivity.Phase.MENU_SCREEN;
					for(int i=0;i<menuScreen.buttons.length;i++)
						if(menuScreen.buttons[i].pressed)
							menuScreen.buttons[i].unpress();
				//	menuScreen.needSecondScreen=false;
				}

				@Override
				public void onAnimationRepeat(Animator animation) 
				{

				}

				@Override
				public void onAnimationEnd(Animator animation) 
				{
					//for(int i=0;i<gameOverScreen.buttons.length;i++)
					menuScreen.buttonsAreActive=true;
				}

				@Override
				public void onAnimationCancel(Animator animation) 
				{

				}
			});

		menuScreen.reverseSlideAnim.addListener(	new Animator.AnimatorListener() 
			{
				@Override
				public void onAnimationStart(Animator animation) 
				{
					//	for(int i=0;i<gameOverScreen.buttons.length;i++)
					menuScreen.buttonsAreActive=false;

				}

				@Override
				public void onAnimationRepeat(Animator animation) 
				{

				}

				@Override
				public void onAnimationEnd(Animator animation) 
				{
					for(int i=0;i<menuScreen.buttons.length;i++)
						if(menuScreen.buttons[i].pressed)
						{
							//	Toast.makeText(savedContext,Integer.toString(i),Toast.LENGTH_SHORT).show();

							menuScreen.buttons[i].unpress();
							switch(i)
							{
								case 0:
								//	restartWithoutFood();
									timer=false;
									gmover=false;
									startScreen.slideIn();
									circleTime.num=0;
									circleScore.num=0;
									//invalidate();
									break;
								case 1:
									MainActivity.GAME_PHASE=MainActivity.Phase.MAIN_MENU;
									Intent intent1=new Intent(savedContext,MainActivity.class);
									intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
									LevelInfo.needLevelChoice=false;
									savedContext.startActivity(intent1);
									//ProDrawActivity.activity.finish();
								break;
								case 2:
									MainActivity.GAME_PHASE=MainActivity.Phase.MAIN_GAME;
									timer=true;
								break;
								case 3:
									MainActivity.GAME_PHASE=MainActivity.Phase.LEVEL_CHOICER;
									Intent intent=new Intent(savedContext,MainActivity.class);
									intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
									
									//	Toast.makeText(savedContext,"needLevelch",Toast.LENGTH_SHORT).show();
									
								//	intent.putExtra("needLevelChoice",true);
									LevelInfo.needLevelChoice=true;
									savedContext.startActivity(intent);
							
							}
							//	Toast.makeText(savedContext,"rt",Toast.LENGTH_SHORT).show();
							//	savedContext.startActivity(new Intent(savedContext,MainActivity.class));

						}
					
				}

				@Override
				public void onAnimationCancel(Animator animation) 
				{

				}
			}
		);
		
		startScreen.slideAnim.addListener(	new Animator.AnimatorListener() 
			{
				@Override
				public void onAnimationStart(Animator animation) 
				{
					MainActivity.GAME_PHASE=MainActivity.Phase.START_SCREEN;
				
					drawPictureOnCanvasWithoutCircles(startScreen.canvas);

					
					
					foodP.set(cellField[food].snakeP);
					foodP.setColor(Color.argb(150,0,255,0));
					startScreen.canvas.drawRoundRect(cellField[food].black,roundBlack*3/2,roundBlack*3/2,cellField[food].bigP);
					startScreen.canvas.drawRoundRect(cellField[food].white,roundWhite*3/2,roundWhite*3/2,whiteP);
					startScreen.canvas.drawRoundRect(cellField[food].color,roundColor*3/2,roundColor*3/2,foodP);
					
					startScreen.buttons[0].unpress();
				}

				@Override
				public void onAnimationRepeat(Animator animation) 
				{

				}

				@Override
				public void onAnimationEnd(Animator animation) 
				{
					startScreen.buttonsAreActive=true;
				}

				@Override
				public void onAnimationCancel(Animator animation) 
				{

				}
			}
		);
		startScreen.reverseSlideAnim.addListener(	new Animator.AnimatorListener() 
			{
				@Override
				public void onAnimationStart(Animator animation) 
				{
				//	for(int i=0;i<gameOverScreen.buttons.length-1;i++)
						startScreen.buttonsAreActive=false;
				}

				@Override
				public void onAnimationRepeat(Animator animation) 
				{

				}

				@Override
				public void onAnimationEnd(Animator animation) 
				{
					MainActivity.GAME_PHASE=MainActivity.Phase.MAIN_GAME;
					timer=true;
				}

				@Override
				public void onAnimationCancel(Animator animation) 
				{

				}
			}
		);
		
		
            pdelr=r/32;
            pdel=r/16;
			
      //  gameOverTextP.setColor(Color.BLACK);

			gameOverPath.moveTo(0,h/4f);
			gameOverPath.lineTo(w,h/4f);
			
		//	gameOverBitmap= Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
		//	gameOverCanvas.setBitmap(gameOverBitmap);
		
	    //	gameOverBitmapWithButtons=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
		//	gameOverBitmapCanvasWithButtons.setBitmap(gameOverBitmapWithButtons);
			
    //		bitmap= Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
	    //	bitmapCanvas.setBitmap(bitmap);
	    //	RectF re=new RectF();
	    //	re.set(0,0,w,h);
	    //	bitmapCanvas.drawRoundRect(re,r,r,gameOverP);
		//	Paint p=new Paint(gameOverTextP);
		//	p.setAlpha(255);
		//	bitmapCanvas.drawTextOnPath(gameOverString,gameOverPath,1,gameOverString.length()-1,gameOverTextP);
	//		menuButton.draw(gameOverBitmapCanvasWithButtons);
		    for(int i=0;i<kwidth*kheight;i++)
			{
				cellField[i].setRectsAndP(i,kwidth,kheight,r,stx,sty);
			}
			
	    	roundBlack=(cellField[0].black.width())/3;
	    	roundWhite=(cellField[0].white.width())/3;
	    	roundColor=(cellField[0].color.width())/3;
	
	
			
		
		
		pathArrow.lineTo(-r/2+pdelr+2*pdel+roundColor,-r/2+pdelr+2*pdel);
		pathArrow.lineTo(r/2-pdelr-2*pdel-roundColor,0);
		pathArrow.lineTo(-r/2+pdelr+2*pdel+roundColor,r/2-pdelr-2*pdel);
		pathArrow.close();
		
		pathP.setStrokeWidth(r/16);
		radiusPE=r/4;
		pathP.setPathEffect(new CornerPathEffect(radiusPE));

		
	    local=(120-circleScore.wordLenDeg);
	//	Toast.makeText(savedContext,Float.toString(local),Toast.LENGTH_SHORT).show();
		
		
	
		
	
		t0=System.currentTimeMillis();
		firstDraw=true;
		
		gmover=false;
		newPath();
		
	    drawPictureOnCanvasWithoutCircles(fieldCanvas);
	
		startScreen.slideIn();
		logMemory("prodraw:");
	}
	
	void logMemory(String s) 
	{
	//	Toast.makeText(savedContext,s+ String.format("Total memory = %s", 
					//						 (int) (Runtime.getRuntime().totalMemory() / 1024)),Toast.LENGTH_SHORT).show();
	}
	
    @Override
    protected void onDraw(Canvas canvas)
    {
		boolean b=false;
		switch(MainActivity.GAME_PHASE)
		{
			case START_SCREEN:
				b=true;
				startScreen.draw(canvas);
				deltaTime=(double)(System.currentTimeMillis()-t0)/1000;
				CircleLabel.ang+=(CircleLabel.vCircle*(float)deltaTime);
				if(CircleLabel.ang>360)
					CircleLabel.ang-=360;
			break;
			case MENU_SCREEN:
				b=true;
				menuScreen.draw(canvas);
				deltaTime=(double)(System.currentTimeMillis()-t0)/1000;
				CircleLabel.ang+=(CircleLabel.vCircle*(float)deltaTime);
				if(CircleLabel.ang>360)
					CircleLabel.ang-=360;
				circleTime.draw(canvas,false);
				circleScore.draw(canvas,false);
			break;
			case MAIN_MENU:
				b=true;
		}
		
		if(!b)
		{
		if(!gmover)
		{
		//	Toast.makeText(savedContext,"I'm drawing",Toast.LENGTH_SHORT).show();
        canvas.drawColor(Color.BLACK);
        //поле
        canvas.drawRect(stx,sty,stx+kwidth*r,sty+kheight*r,fieldP);

        //еда
 
		foodP.set(cellField[food].snakeP);
		foodP.setColor(Color.argb(150,0,255,0));
		canvas.drawRoundRect(cellField[food].black,roundBlack*3/2,roundBlack*3/2,cellField[food].bigP);
		canvas.drawRoundRect(cellField[food].white,roundWhite*3/2,roundWhite*3/2,whiteP);
		canvas.drawRoundRect(cellField[food].color,roundColor*3/2,roundColor*3/2,foodP);
		

        int e=(first+1)%mass.length;
        int i=(first-length+1+mass.length)%mass.length;
   

        //тени 1 слой+Черн квад

        while(i!=e)
        {
        	canvas.drawRoundRect(cellField[mass[i]].black,roundBlack,roundBlack,cellField[mass[i]].bigP);
            i=(i+1)%mass.length;
        }
		
        //блоки
        for(int j=0;j<blocks.length;j++)
       		canvas.drawRoundRect(cellField[blocks[j]].black,roundBlack,roundBlack,cellField[blocks[j]].bigP);
        
        //белый кв
        i=(first-length+1+mass.length)%mass.length;
        while(i!=e)
        {
			canvas.drawRoundRect(cellField[mass[i]].white,roundWhite,roundWhite,whiteP);
		    i=(i+1)%mass.length;
        }

        //блоки
        for(int j=0;j<blocks.length;j++)
			canvas.drawRoundRect(cellField[blocks[j]].white,roundWhite,roundWhite,whiteP);
			
	     
        //красный

        i=(first-length+1+mass.length)%mass.length;
		int num=0;
        while(i!=e)
        {
			num++;
			curSnakeP.set(cellField[mass[i]].snakeP);
			curSnakeP.setColor(Color.argb(150,255*(num)/length,0,0));
        	canvas.drawRoundRect(cellField[mass[i]].color,roundColor,roundColor,curSnakeP);
            i=(i+1)%mass.length;
        }

        
        for(int j=0;j<blocks.length;j++)
       		canvas.drawRoundRect(cellField[blocks[j]].color,roundColor,roundColor,cellField[blocks[j]].blockP);
			
        	canvas.drawPath(snakePath,pathP);

			//рисование цифр
			//canvas.drawText(Integer.toString(score),circleScore.centX,circleScore.centY+CircleLabel.circleTP.getTextSize()/2,CircleLabel.circleTP);
      	 //   canvas.drawText(Long.toString(circleTime.num*3/100),circleTime.centX,circleTime.centY+CircleLabel.circleTP.getTextSize()/2,CircleLabel.circleTP);

	    	canvas.save();
			deltaTime=(double)(System.currentTimeMillis()-t0)/1000;
			CircleLabel.ang+=(CircleLabel.vCircle*(float)deltaTime);
			if(CircleLabel.ang>360)
				CircleLabel.ang-=360;
			menuButton.draw(canvas);
		//	circleTime.draw(canvas, gmover);
		//	circleScore.draw(canvas, gmover);	
	    //	startScreen.draw(canvas);
//	menuScreen.draw(canvas);
	}
	else
		{
	
		    //Bitmap b=blur(gameOverBitmap,1);

			CircleLabel.updateNum(deltaTime);
			
			menuButton.draw(canvas);
		  // 	canvas.drawBitmap(gameOverBitmap,0,0,gameOverBitmapP);
			gameOverScreen.draw(canvas);
		 //   canvas.restore();
	
		
			
			    switch(CircleLabel.mode)
				{
				case NORMAL:
				{
				//	CircleLabel.mode=CircleLabel.Mode.GAMEOVER;
					if(Math.abs(CircleLabel.ang)<=local||(CircleLabel.ang>0&&prevAng<0))
					{
			      	    circleTime.createGameOverPath(width,height);
						circleScore.createGameOverPath(width,height);
						if(circleScore.privMode==CircleLabel.Mode.GAMEOVER&&circleTime.privMode==CircleLabel.Mode.GAMEOVER)
				   	    {
							
						  //  Toast.makeText(savedContext,"norm-->GO",Toast.LENGTH_SHORT).show();
						    CircleLabel.mode=CircleLabel.Mode.GAMEOVER;
							
						//	Toast.makeText(savedContext,"wtf",Toast.LENGTH_SHORT).show();
				//	goPathIsDrawn=true;
				   	    }
					}
					else
					{
						deltaTime=(double)(System.currentTimeMillis()-t0)/1000;
				     	CircleLabel.vCircle+=CircleLabel.aCircle*deltaTime;
						CircleLabel.ang+=CircleLabel.vCircle*deltaTime;
				

						if(CircleLabel.ang>360)
							CircleLabel.ang-=360;
						prevAng=CircleLabel.ang;
					}
					
				}
				
				break;
				case GAMEOVER:
						deltaTime=(double)(System.currentTimeMillis()-t0)/1000;
                   
						float x=(circleScore.pathLen*1.03f-circleScore.tW-CircleLabel.v0GO*CircleLabel.v0GO/(2*CircleLabel.aCircle))/2;
						if(CircleLabel.k>=x)
						{
							if(CircleLabel.kAccel>0)
							{
								CircleLabel.kAccel=-CircleLabel.kAccel;
							}
							else
							{
								if(CircleLabel.k>=circleScore.pathLen-circleScore.tW)
								{
									CircleLabel.kSpeed=0;
								}
								else
								{
									deltaTime=(double)(System.currentTimeMillis()-t0)/1000;

									CircleLabel.kSpeed+=CircleLabel.kAccel*(float)deltaTime;
									CircleLabel.k+=CircleLabel.kSpeed*(float)deltaTime;
								}
								if(CircleLabel.kSpeed<=0)
								{
									if(CircleLabel.mode==CircleLabel.Mode.GAMEOVER)
									{
									//	Toast.makeText(savedContext,"yes",Toast.LENGTH_SHORT).show();
										CircleLabel.stop();	
										circleScore.createFinal(width,height);
										circleTime.createFinal(width,height);
										if(circleScore.privMode==CircleLabel.Mode.FINAL&&circleTime.privMode==CircleLabel.Mode.FINAL)
										{
											CircleLabel.mode=CircleLabel.Mode.FINAL;
										//	Toast.makeText(savedContext,CircleLabel.mode.toString(),Toast.LENGTH_SHORT).show();
										}

									}
								}
							}
				    	}
						else
						{
							deltaTime=(double)(System.currentTimeMillis()-t0)/1000;
							CircleLabel.kSpeed+=CircleLabel.kAccel*(float)deltaTime;
							CircleLabel.k+=CircleLabel.kSpeed*(float)deltaTime;
						}
			    	break;
				    case FINAL:
						deltaTime=(double)(System.currentTimeMillis()-t0)/1000;
					    CircleLabel.updateNum(deltaTime);
					break;
				}
		}
		}
	    circleTime.draw(canvas, gmover);
	    circleScore.draw(canvas, gmover);
		t0=System.currentTimeMillis();
	    if(firstDraw) firstDraw=false;
	}
		

    static public void kdir(int dr)
    {
        if (dir.get(0)!=Direction.UP&&dir.get(0)!=Direction.DOWN)
        {
            if (dr==51)
            {
                dir.add(Direction.UP);
            }
            else
            if (dr==47)
            {
                dir.add(Direction.DOWN);
            }
        }
        else{
            if (dr==29)
            {
                dir.add(Direction.LEFT);
            }
            else
            if (dr==32)
            {
                dir.add(Direction.RIGHT);
            }
        }
    }
	
	float getVy(float x,float y)
	{
		 //CircleLabel.v0GO)
		return (float)Math.sqrt( CircleLabel.v0GO*CircleLabel.v0GO-2*CircleLabel.kAccel*(y-x));
	}

    class ProTimer extends CountDownTimer
    {

        ProTimer(long l1, long l2)
        {
            super(l1,l2);
        }

        @Override
        public void onTick(long p1)
        {
			if(timer)
			{
				if(!gmover)
				{
				//	if(!win)
					//{
           	   	  		if (cfr==fps)
             	  		{
                  	 		nextFrame();
                   		    cfr=0;
			       		    invalidate();
						}
						else 
						{
							invalidate(circleTime.rect);
							invalidate(circleScore.rect);
						}
              		    if (cfr<fps)
                 	    	cfr++;
						time++;
						if(time%30==0)
							circleTime.num++;
				//	}
				//	else
				//	{
						
				//	}
				}
				else
				{
			   	  invalidate();
				}
        }
		else
			invalidate();
		}
		

        @Override
        public void onFinish()
        {
            this.start();
        }
    }

    private void nextFrame()
    {
        if (dir.size()>1)
        {
            dir.remove(0);
        }

		
		switch (dir.get(0))
		{
			case RIGHT:
				mass[(first+1)%mass.length]=(mass[first]+1+kwidth)%kwidth+(mass[first]/kwidth)*kwidth;
				break;
				
			case LEFT:
				mass[(first+1)%mass.length]=(mass[first]-1+kwidth)%kwidth+(mass[first]/kwidth)*kwidth;
				break;
			case DOWN:
				mass[(first+1)%mass.length]=(mass[first]+kwidth+mass.length)%mass.length;
				break;
			case UP:
				mass[(first+1)%mass.length]=(mass[first]-kwidth+mass.length)%mass.length;
				break;
		}
		
		
        first=(first+1)%mass.length;
		
        boolean needf = false;
        if (mass[first]==foodx+foody*kwidth)
        {
            //Toast.makeText(getContext(), "Правильно!", Toast.LENGTH_SHORT).show();
            length++;
            score++;
            if ((score %10 ==0)&&(fps>5))
                fps--;
            needf= true;
        }
        else
            field[mass[(first-length+mass.length)%mass.length]]= true;
        if (field[mass[first]]==false)
        {
            
			gameOver();
			
	
        }
        field[mass[first]]=false;
		if(length==kwidth*kheight)
		{
			win=true;
			timer=false;
			invalidate();
		}
		else
 	       if (needf&&!gmover)
		   		newFood();
		
		newPath();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float X=event.getX(), Y=event.getY();
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                touched = true;
                if (dir.size()!=1)
                {
                    Direction d = dir.get(0);
                    dir.clear();
                    dir.add(d);
                }
                x=X;
                y=Y;
		
			/*	if(gmover)
				{
				/*	if (gameOverScreen.buttonsAreActive)
						for (int i=0;i<gameOverScreen.buttons.length;i++)
						{
							if (gameOverScreen.buttons[i].isPressed(X,Y) )
							{
								gameOverScreen.buttons[i].onPress();
								invalidate(gameOverScreen.buttons[i].inv);
								switch(i)
								{
									case 0:
										gameOverScreen.needSecondScreen=true;
								
										newFood();
										foodP.set(cellField[food].snakeP);
										foodP.setColor(Color.argb(150,0,255,0));
										gameOverScreen.endCanvas.drawRoundRect(cellField[food].black,roundBlack*3/2,roundBlack*3/2,cellField[food].bigP);
										gameOverScreen.endCanvas.drawRoundRect(cellField[food].white,roundWhite*3/2,roundWhite*3/2,whiteP);
										gameOverScreen.endCanvas.drawRoundRect(cellField[food].color,roundColor*3/2,roundColor*3/2,foodP);
										
										
										break;
									case 1:
								}
								
							}
						}
				}
				else
				{
					if(!menuScreen.buttonsAreActive)
					{
					/*	if(menuButton.isPressed(X,Y))
						{
							menuButton.onPress();
							drawPictureOnCanvasWithoutCircles(menuScreen.startCanvas);
							
							foodP.set(cellField[food].snakeP);
							foodP.setColor(Color.argb(150,0,255,0));
							menuScreen.startCanvas.drawRoundRect(cellField[food].black,roundBlack*3/2,roundBlack*3/2,cellField[food].bigP);
							menuScreen.startCanvas.drawRoundRect(cellField[food].white,roundWhite*3/2,roundWhite*3/2,whiteP);
							menuScreen.startCanvas.drawRoundRect(cellField[food].color,roundColor*3/2,roundColor*3/2,foodP);
							
							
							timer=false;
							menuScreen.slideIn();
							invalidate(menuButton.inv);
						}*/
					/*	if (startScreen.buttonsAreActive)
							if (startScreen.buttons[0].isPressed(X,Y) )
							{
								startScreen.buttons[0].onPress();
						    	invalidate(	startScreen.buttons[0].inv);
							}
					}
					else
					{
					/*	for (int i=0;i<menuScreen.buttons.length;i++)
						{
							if (menuScreen.buttons[i].isPressed(X,Y) )
							{
								menuScreen.buttons[i].onPress();
								invalidate(menuScreen.buttons[i].inv);
								switch(i)
								{
									case 0:
									//	startScreen.needSecondScreen=true;
									break;
									case 1:
										
									break;
									case 2:
								//		menuScreen.slideOut();
								}
								menuScreen.slideOut();

							}
						}
					}
				}
			*/
				
				switch (MainActivity.GAME_PHASE)
				{
					case MENU_SCREEN:
					if(menuScreen.buttonsAreActive)
						for (int i=0;i<menuScreen.buttons.length;i++)
						{
							if (menuScreen.buttons[i].isPressed(X,Y) )
							{
								menuScreen.buttons[i].onPress();
								invalidate(menuScreen.buttons[i].inv);
								switch(i)
								{
									case 0:
										//	startScreen.needSecondScreen=true;
										DialogHelper dm=new DialogHelper(savedContext)
										{

											@Override
                                            protected void onDialogDismissed()
											{
												
											}


											@Override
                                            protected void onNegativeButtonClick()
											{
												menuScreen.buttons[0].unpress();
											}

											@Override
                                            protected void onPositiveButtonClick()
											{
												menuScreen.needSecondScreen=true;

												menuScreen.endCanvas.drawBitmap(fieldBitmap,0,0,whiteP);

												restartWithoutFood();
												newFood();
												circleScore.num--;

												foodP.set(cellField[food].snakeP);
												foodP.setColor(Color.argb(150,0,255,0));
												menuScreen.endCanvas.drawRoundRect(cellField[food].black,roundBlack*3/2,roundBlack*3/2,cellField[food].bigP);
												menuScreen.endCanvas.drawRoundRect(cellField[food].white,roundWhite*3/2,roundWhite*3/2,whiteP);
												menuScreen.endCanvas.drawRoundRect(cellField[food].color,roundColor*3/2,roundColor*3/2,foodP);
												
												menuScreen.slideOut();
											}
										};
										
										dm.showDialog(null, "Are you sure?", "Yes","No",true);
										break;
									case 1:
										DialogHelper dl=new DialogHelper(savedContext)
										{

											@Override
                                            protected void onDialogDismissed()
											{
												// TODO: Implement this method
											}


											@Override
                                            protected void onNegativeButtonClick()
											{
												menuScreen.buttons[1].unpress();
											}

											@Override
                                            protected void onPositiveButtonClick()
											{
												menuScreen.endCanvas.drawColor(Color.BLACK);
												menuScreen.needSecondScreen=true;
												menuScreen.slideOut();
											}
										};

										dl.showDialog(null, "Are you sure?", "Yes","No",true);
										break;
									case 2:
										//		menuScreen.slideOut();
										menuScreen.slideOut();
									break;
									case 3:
										menuScreen.slideOut();
										
								}
								
							}
						}
					break;
					case START_SCREEN:
						if (startScreen.buttonsAreActive)
							if (startScreen.buttons[0].isPressed(X,Y) )
							{
								startScreen.buttons[0].onPress();
						    	invalidate(	startScreen.buttons[0].inv);
							}
					break;
					case GAME_OVER_SCREEN:
						if (gameOverScreen.buttonsAreActive)
							for (int i=0;i<gameOverScreen.buttons.length;i++)
							{
								if (gameOverScreen.buttons[i].isPressed(X,Y) )
								{
									gameOverScreen.buttons[i].onPress();
									invalidate(gameOverScreen.buttons[i].inv);
									switch(i)
									{
										case 0:
											gameOverScreen.needSecondScreen=true;

											gameOverScreen.endCanvas.drawBitmap(fieldBitmap,0,0,whiteP);
											
											restartWithoutFood();
											newFood();
											circleScore.num--;
											
											foodP.set(cellField[food].snakeP);
											foodP.setColor(Color.argb(150,0,255,0));
											gameOverScreen.endCanvas.drawRoundRect(cellField[food].black,roundBlack*3/2,roundBlack*3/2,cellField[food].bigP);
											gameOverScreen.endCanvas.drawRoundRect(cellField[food].white,roundWhite*3/2,roundWhite*3/2,whiteP);
											gameOverScreen.endCanvas.drawRoundRect(cellField[food].color,roundColor*3/2,roundColor*3/2,foodP);

											
											
											break;
										case 1:
									}

								}
							}
					break;
					case MAIN_GAME:
						if(menuButton.isPressed(X,Y))
						{
							menuButton.onPress();
							drawPictureOnCanvasWithoutCircles(menuScreen.startCanvas);

							foodP.set(cellField[food].snakeP);
							foodP.setColor(Color.argb(150,0,255,0));
							menuScreen.startCanvas.drawRoundRect(cellField[food].black,roundBlack*3/2,roundBlack*3/2,cellField[food].bigP);
							menuScreen.startCanvas.drawRoundRect(cellField[food].white,roundWhite*3/2,roundWhite*3/2,whiteP);
							menuScreen.startCanvas.drawRoundRect(cellField[food].color,roundColor*3/2,roundColor*3/2,foodP);

							timer=false;
							menuScreen.slideIn();
							invalidate(menuButton.inv);
						}
					
				}
				
                break;
            case MotionEvent.ACTION_MOVE:
                if ((touched)&&((Math.abs(y-Y)>=delta)||(Math.abs(x-X)>=delta)))
                {
                    int size = dir.size()-1;
                    if(Math.abs(y-Y)>=Math.abs(x-X))
                    {
                        if ((dir.get(size)!=Direction.UP) &&(dir.get(size)!=Direction.DOWN))
                        {
                            if (y>Y)
                                dir.add(Direction.UP);
                            else
                                dir.add(Direction.DOWN);
                            x=X;
                            y=Y;
                        }
                    }
                    else
                    {
                        if ((dir.get(size)!=Direction.LEFT)&&(dir.get(size)!=Direction.RIGHT))
                        {
                            if (x>X)
                                dir.add(Direction.LEFT);
                            else
                                dir.add(Direction.RIGHT);
                            x=X;
                            y=Y;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
				
				if(gmover)
				{
					if(gameOverScreen.buttonsAreActive)
						for(int i=0;i<gameOverScreen.buttons.length;i++)
							if(gameOverScreen.buttons[i].pressed)
							{
								gameOverScreen.slideOut();
							}
				}
				else
				{
					if(startScreen.buttonsAreActive)
						if(startScreen.buttons[0].pressed)
							startScreen.slideOut();
				}
				if(menuButton.pressed)
				{
					menuButton.unpress();
					invalidate(menuButton.inv);
				}
                touched=false;
			
                break;
        }
		
	//	gestureDetector.onTouchEvent(event);
        return true;
    }
}
