//package ru.ZheLeProduction.ZheLeSnake;
package com.zheleproduction.zhelesnake;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.zheleproduction.zhelesnake.myInterface.EditorMenuScreen;
import com.zheleproduction.zhelesnake.myInterface.FullButton;
import com.zheleproduction.zhelesnake.myInterface.LabeledButton;
import com.zheleproduction.zhelesnake.myInterface.MenuButton;
import com.zheleproduction.zhelesnake.util.DialogHelper;
import com.zheleproduction.zhelesnake.util.FileHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

//import myapp.rozh.ru.snake.ProDraw.*;

public class Redactor extends View
{
	//arrowP.setPathEffect(new ComposePathEffect(new PathDashPathEffect(pathArrow, r, time, 	PathEffect.Style.ROTATE),new CornerPathEffect(r/2)));
	//ComposePathEffect cPE=new ComposePathEffect(arrowP);
    Context savedContext;
	Canvas savedCanvas=new Canvas();
	Iterator<Integer> snakeIt;
	Path path;
//	Path reversePath;
	Path pathArrow;
//	boolean needOnlyMenuButton;
//	Paint textPaint=new Paint();
	
	//Picture pic=new Picture();
//	Bitmap cache;
	
	Bitmap bitmap;
	Canvas bitmapCanvas;
	
	CornerPathEffect cornerPE;
	
	Paint curPathPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
	Paint[] arrowP;
    Paint pnt = new Paint();
//	Paint firstLayerP=new Paint();
//	Paint secondLayerP=new Paint();
//	Paint thirdLayerP=new Paint();
	Paint whiteP = new Paint();
	Paint redButtP=new Paint();
	Paint blackButtP=new Paint();
	Paint cleanButtP=new Paint();
	Paint blackP=new Paint();
	Paint cursorP=new Paint();
	Paint lineP=new Paint();
	Paint grayP=new Paint();
	FullButton[] buttSn;
	FullButton[] buttBl;
	
//	double curTime;
//	double lastTime;
	boolean newBlMode;
	Cell[] fullField;
    int kwidth, kheight;
//	int kArrowShift=40;
	int kStages=6;
	long prevTime;
	float dTime;
	
	int blockStage;
    int stx,sty;
    int width,height;
    int kblocks;
	int ksnake;
//	int arrButDir;
    float arrowPos;
	float arrowSpeed=-1f;//потом *r
	
	int arrowStage;
	int gradShift;
	int lastSnake;
	float arrowShift;
	float dist;
    boolean[] field;
	
	int shrinkSn,shrinkBl;

	boolean move;
    boolean head=false;
    boolean tail=false;
	boolean cleanMode;
	boolean snRedAct;
	boolean blRedAct;
	boolean blocksEdit;
	public static boolean dialogIsClosed ;
	
	int out,out1;
    
	int k;
	int blDown=-1;
	int stageButt;
	
    //static ArrayList<Direction> dir = new ArrayList<Direction>();

    //рисование квадратов
    int r;
    int pdelr;//раница между половиной r и половиной рисующейся стороны.
    int pdel;//разница полусторон вложенных квадратов

    float x,y;
	float xp,yp;

    //	boolean touched = false;

    ArrayList<Integer> blocks = new ArrayList<Integer>();
	LinkedList<Integer> snake = new LinkedList<Integer>();
	
	
	//для поля
	float roundBlack;
	float roundWhite;
	float roundColor;

	//кнопки
	float buttRoundBlack;
	float buttRoundWhite;
	float buttRoundColor;
	
	boolean bm=false;
    boolean frame=false;
    boolean firstdraw = true;
    boolean blred;
	boolean blredf;
	boolean snred;
	boolean snredh;
	boolean snredt;
	boolean timer=false;
	boolean blRedNew;
	boolean firstSnRed;
	boolean firstBlRed;
	boolean right;
	boolean drawGreen;
  //  boolean neVert;
	
    //int[] mass;
    ProTimer protimer=new ProTimer(100000,33);
//    RectF block=new RectF();
//	RectF snakeBl=new RectF();
    float normWidthBl;
	float normWidthWhiteBl;
	float normWidthColorBl;
//    RectF shared = new RectF();
//	RectF rect=new RectF();
//	Rect rec=new Rect();
//	RectF but1=new RectF();
	RectF workSpace=new RectF();
   
	
//	float delta;
  //  RectF but= new RectF();
	Rect rectBlTest=new Rect();
	Rect rectSnTest=new Rect();
	Rect cursor=new Rect();
	RectF cursorF=new RectF();

	LabeledButton clearButton=new LabeledButton("Clear");
	LabeledButton saveButton=new LabeledButton("Save");
	
	public static EditorMenuScreen editorMenuScreen;
	MenuButton menuButton=new MenuButton();
	
	GestureDetector gd;
	
	Pointer pointer;
	
	class Pointer
	{
		Bitmap bitmap;
		boolean rotating;
    	Paint transP;
		float translateX,translateY;
		Direction dir;
		float ang;
	
		Pointer()
		{
			transP=new Paint();
			transP.setAlpha(150);
			dir=Direction.RIGHT;
			ang=0;
			rotating=false;
		}
		
		boolean notBack()
		{
			boolean b=true;
			int x=0;
			switch(dir)
			{
				case RIGHT:
					x=1;
				break;
				case LEFT:
					x=-1;
				break;
				case UP:
					x=-kwidth;
				break;
				case DOWN:
					x=kwidth;
			}
			if(lastSnake+x==snake.get(ksnake-2))
				b=false;
			return b;
		}
		
		void loadBitmap(int width,int r)
		{
			bitmap= FileHelper.LargeImageHelper.decodeResourseCompressedBitmap(R.drawable.arrow,width,width);
			translateX=(r-bitmap.getWidth())/2f;
			translateY=(r-bitmap.getHeight())/2f;
		}
		
		void recycleBitmap()
		{
			if(bitmap!=null)
				bitmap.recycle();
		}

		
		void draw(Canvas canvas)
		{
			
			float a=0;
			if(rotating)
				a=ang;
			else
			switch(dir)
			{
				case LEFT:
					a=180;
					break;
				case DOWN:
					a=90;
					break;
				case UP:
					a=-90;
				    break;
			}
			
		//	a=-30;
			
			canvas.save();
	
		    canvas.translate((lastSnake%kwidth)*r+stx,(lastSnake/kwidth)*r+sty);
			canvas.translate(translateX,translateY);
			canvas.rotate(a,r/2f-translateX,r/2-translateY);
			
		    canvas.drawBitmap(bitmap,0,0,transP);
			canvas.restore();
		}
		
		void draw(Direction dir,Canvas canvas)
		{
			canvas.save();

			int a=0;
			switch(dir)
			{
				case LEFT:
					a=180;
				break;
				case DOWN:
					a=90;
				break;
				case UP:
					a=-90;
			}
			
		    canvas.translate((lastSnake%kwidth)*r+stx,(lastSnake/kwidth)*r+sty);
			canvas.translate(translateX,translateY);
			canvas.rotate(a,r/2f-translateX,r/2-translateY);

		    canvas.drawBitmap(bitmap,0,0,transP);
			canvas.restore();
		}
		
	}
	
	class MyTask extends AsyncTask<Object,Void,Void>
	{
		boolean b;
		Bitmap bit;
		@Override
		protected Void doInBackground(Object[] p1)
		{
			b=FileHelper.saveLevel((Bitmap)p1[0],(String)p1[1],(String)p1[2]);
			bit=(Bitmap)p1[0];
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			if(b)
				Toast.makeText(savedContext,"Saved successfully",Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(savedContext,"Something went wrong",Toast.LENGTH_SHORT).show();
			Vibrator v = (Vibrator) savedContext.getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(100);
			bit.recycle();
			super.onPostExecute(result);
		}
	}
	
	
	
	String prepareDataFile()
	{
		StringBuilder sb=new StringBuilder();
		sb.append(Integer.toString(kwidth)+" "+Integer.toString(kheight)+"\n");
		sb.append(Integer.toString(ksnake)+"\n");
		snakeIt=snake.iterator();
		while(snakeIt.hasNext())
		{
			sb.append(Integer.toString(snakeIt.next())+" ");
		}
		sb.append("\n"+Integer.toString(kblocks)+"\n");
		for(int i=0;i<kblocks;i++)
			sb.append(Integer.toString(blocks.get(i))+" ");

		sb.append("\n");
		switch(pointer.dir)
		{
			case LEFT:
				sb.append(Integer.toString(0));
				break;
			case UP:
				sb.append(Integer.toString(1));
				break;
			case RIGHT:
				sb.append(Integer.toString(2));
				break;
			case DOWN:
				sb.append(Integer.toString(3));
				break;
		}
		return sb.toString();
	}
	
	Bitmap prepareBitmap()
	{
		Bitmap tmpBit=Bitmap.createBitmap(bitmap);
		Canvas tmpCanvas=new Canvas(tmpBit);
		pointer.draw(tmpCanvas);
	//	Bitmap bit=Bitmap.createBitmap(tmpBit,stx,sty,width-stx*2,height-2*sty);
	Bitmap bit=Bitmap.createBitmap(tmpBit,(int)workSpace.left,(int)workSpace.top,(int)workSpace.width(),(int)workSpace.height());
	
	tmpBit.recycle();
		Canvas c=new Canvas(bit);
		Paint p=new Paint(Paint.ANTI_ALIAS_FLAG);
		p.setStyle(Paint.Style.STROKE);
		float bitWid=bit.getWidth();
		float rad=bitWid/3f;
		float strWid=(float)(2*(Math.sqrt(2)-1)*rad);
		rad+=strWid/2;
		p.setStrokeWidth(strWid);
		RectF rec=new RectF(-strWid/2,-strWid/2,bitWid+strWid/2f,bitWid+strWid/2f);
		p.setColor(Color.rgb(0x52,0x47,0x47));
		c.drawRoundRect(rec, rad,rad,p);
		return bit;
	}
	
	void save()
	{
	if(ksnake>0)
	{
		boolean bool=true;
		if(ksnake>1)
			if(!pointer.notBack())
				bool=false;
		if(bool)
		{
		LayoutInflater inflater=(LayoutInflater) savedContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     	View saveDialogLayout=inflater.inflate(R.layout.save_dialog,null);
		final EditText saveNameField=(EditText) saveDialogLayout.findViewById(R.id.levelName);
		
	    DialogHelper saveDialog=new DialogHelper(savedContext)
		{

			@Override
            protected void onDialogDismissed()
			{
				dialogIsClosed=true;
			}

			@Override
            protected void onNegativeButtonClick()
			{

			}

			@Override
            protected void onPositiveButtonClick()
			{
				
				final String fileName=saveNameField.getText().toString();     
				if(!fileName.isEmpty())
				{
					
					
					if(FileHelper.doesNameExist(fileName))
					{
						DialogHelper dh=new DialogHelper(savedContext)
						{
							@Override
							protected void onDialogDismissed()
							{

							}
							@Override
							protected void onPositiveButtonClick()
							{
								String fileString=prepareDataFile();

								//preparingBitmap
								Bitmap bit=prepareBitmap();
								
								MyTask mt=new MyTask();
								mt.execute(bit,fileString,fileName);	
							}

							@Override
							protected void onNeutralButtonClick()
							{
								save();
							}
						};
						dh.showOverwriteDialog();
					}
					else
					{
		        		String fileString=prepareDataFile();

					//preparingBitmap
						Bitmap bit=prepareBitmap();
							
						MyTask mt=new MyTask();
						mt.execute(bit,fileString,fileName);
					
					}
					
					//	FileHelper.showRootFolder();
					//	FileHelper.showDataFolder();
					//	FileHelper.showBitmapFolder();
				}
				else
				{
					DialogHelper emptyFieldDialog=new DialogHelper(savedContext)
					{
						@Override
                        protected void onDialogDismissed()
						{
					//		super.onDialogDismissed();
							dialogIsClosed=true;
							save();
						}
						@Override
                        protected void onPositiveButtonClick()
						{
						}
					};
					emptyFieldDialog.showOneButtonDialog("Warning!","Name field cannot be empty!","Ok",true);
				}
			}
		};
		saveDialog.showDialog("Save Dialog","Choose the name","Save","Cancel",true,saveDialogLayout);
	}
		else
		{
			DialogHelper dh=new DialogHelper(savedContext)
			{

				@Override
				protected void onDialogDismissed()
				{
					//	super.onDialogDismissed();
					dialogIsClosed=true;
				}
				@Override
				protected void onPositiveButtonClick()
				{
					// TODO: Implement this method
				}

			};
			dh.showOneButtonDialog("Warning!","Change the direction of the snake","Yes, sir!",true);	
		}
	}
		else
		{
			DialogHelper dh=new DialogHelper(savedContext)
			{

				@Override
                protected void onDialogDismissed()
				{
					//	super.onDialogDismissed();
					dialogIsClosed=true;
					editorMenuScreen.buttons[0].unpress();
				}
				@Override
                protected void onPositiveButtonClick()
				{
					// TODO: Implement this method
				}

			};
			dh.showOneButtonDialog("Warning!","You forgot to create a snake!","It's my blame:(",true);
		}
	/*	AlertDialog.Builder builder = new AlertDialog.Builder(savedContext);
		builder.setTitle("Saving...").setCancelable(false);
		
		final View lay=RedactorActivity.activity.getLayoutInflater().inflate(R.layout.save_dialog,null);
		
		builder.setView(lay);
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					dialogIsClosed=true;
				}
			});
		
		
		builder.setPositiveButton("Save", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
				    EditText et=(EditText) lay.findViewById(R.id.levelName);
				
					String fileName=et.getText().toString();     
					
					
					String fileString="";
					fileString=(Integer.toString(kwidth)+" "+Integer.toString(kheight)+"\n");
					fileString+=(Integer.toString(ksnake)+"\n");
					snakeIt=snake.iterator();
					while(snakeIt.hasNext())
					{
						fileString+=(Integer.toString(snakeIt.next())+" ");
					}
					fileString+=("\n"+Integer.toString(kblocks)+"\n");
					for(int i=0;i<kblocks;i++)
						fileString+=(Integer.toString(blocks.get(i))+" ");
					
					Bitmap bit=Bitmap.createBitmap(bitmap,stx,sty,width-stx*2,height-2*sty);
					Canvas c=new Canvas(bit);
					Paint p=new Paint(Paint.ANTI_ALIAS_FLAG);
					p.setStyle(Paint.Style.STROKE);
					p.setStrokeWidth((float)((Math.sqrt(2)-1)*bit.getWidth()/2f));
					RectF rec=new RectF(0,0,bit.getWidth(),bit.getHeight());
					p.setColor(Color.rgb(0x52,0x47,0x47));
					c.drawRoundRect(rec, rec.height()/4f,rec.height()/4f,p);
					//	Canvas c=new Canvas(bit);
				 //   Paint p=new Paint(Paint.ANTI_ALIAS_FLAG);
				//	p.setColor(Color.argb(100,255,255,255));
				  //  RectF rect=new RectF(0,0,bit.getWidth(),bit.getHeight());
				//	c.drawRoundRect(rect,bit.getWidth()/4f,bit.getWidth()/4f,p);
				//	p.setColor(Color.argb(100,0,0,0));
			       // c.drawRect(rect,p);
					
					FileHelper.saveLevel(bit,fileString,fileName);
					FileHelper.showRootFolder();
					FileHelper.showDataFolder();
					FileHelper.showBitmapFolder();
					
					*/
				/*	String folderName="testFolder";
					
					File internalPath = savedContext.getFilesDir();
				    File folder=new File(internalPath.getAbsolutePath()+"/"+folderName);
					folder.mkdir();*/
					
					
					//	FileOutputStream fos=new FileOutputStream(folder.getAbsolutePath() + "/" + fileName);
						
						//
						//	b.compress(Bitmap.CompressFormat.PNG,100,fos);
						//
						
					/*		
						ObjectOutputStream oos=new ObjectOutputStream(fos);
						oos.writeObject(fileString);
						oos.close();
						
						fos.flush();
						fos.close();
						
						String[] namesArr=folder.list();
						String names="";
						for(int i=0;i<namesArr.length;i++)
						{
							names+=namesArr[i]+" ";
						
						}
						Toast.makeText(savedContext,names,Toast.LENGTH_SHORT).show();
						
				*/

					/*File f=new File(savedContext.getDir(null,savedContext.MODE_PRIVATE),name);

					try
					{
						f.createNewFile();
						PrintWriter pw=new PrintWriter(f);
						pw.append(Integer.toString(kwidth)+" "+Integer.toString(kheight)+"\n");
						pw.append(Integer.toString(ksnake)+"\n");
						snakeIt=snake.iterator();
						while(snakeIt.hasNext())
						{
							pw.append(Integer.toString(snakeIt.next())+" ");
						}
						pw.append("\n"+Integer.toString(kblocks)+"\n");
						for(int i=0;i<kblocks;i++)
							pw.append(Integer.toString(blocks.get(i))+" ");
						pw.close();
						Toast.makeText(savedContext,"Saved",Toast.LENGTH_SHORT).show();
						
					}
					catch (IOException e)
					{
						Toast.makeText(savedContext,e.getMessage(),Toast.LENGTH_SHORT).show();
						//bool=false;
						Toast.makeText(savedContext,"isn't saved",Toast.LENGTH_SHORT).show();
					}*/
				/*	dialogIsClosed=true;
				}

			
		});
		
	
		AlertDialog dialog = builder.create();
		dialog.show();*/
	}
	

	
/*	boolean read()
	{
		boolean bool=true;
		File f=new File(savedContext.getDir(null,savedContext.MODE_PRIVATE),"myfile.txt");
		try
		{
			f.createNewFile();
			Scanner sc=new Scanner(f);
	
			kwidth=sc.nextInt();
			kheight=sc.nextInt();
			snake.clear();
	        
			ksnake=sc.nextInt();
			int buf=0;
			
			for(int i=0;i<ksnake;i++)
			{
			   buf=sc.nextInt();
			   snake.add(buf);
			}
			
			if(ksnake>0)
			   lastSnake=buf;
			blocks.clear();
			kblocks=sc.nextInt();
			for(int i=0;i<kblocks;i++)
			{
				blocks.add(sc.nextInt());
			}
			
			switch(sc.nextInt())
			{
				case 0:
				
				case 1:
					
				case 2:
				
				case 3:
			}
			
			
            sc.close();
		}
		catch (IOException e)
		{
			Toast.makeText(savedContext,e.getMessage(),Toast.LENGTH_SHORT).show();
			bool=false;
		}
		
		invalidateBit();

		return bool;
	}*/
    void clear()
    {
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(savedContext);
		builder.setMessage("What do you want to clear?").setTitle("Clear").setCancelable(false);

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					// TODO: Implement this method
                    dialogIsClosed=true;
				}


			});


		builder.setPositiveButton("Blocks", new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					// TODO: Implement this metho
					if(kblocks>0)
					{
						for(int i=0;i<kblocks;i++)
							field[(int)blocks.get(i)]=false;
						blocks.clear();
						kblocks=0;
					}
				
					dialogIsClosed=true;
					
					invalidateBit();
					invalidate();

				}


			});

		builder.setNeutralButton("All", new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					// TODO: Implement this metho
					boolean b=false;
					if(kblocks>0)
					{
						for(int i=0;i<kblocks;i++)
							field[(int)blocks.get(i)]=false;
						blocks.clear();
						kblocks=0;
						b=true;
					}
					
					if(ksnake>0)
					{
						snakeIt=snake.iterator();
						while(snakeIt.hasNext())
							field[(int)snakeIt.next()]=false;
						snake.clear();
						ksnake=0;
						b=true;
					}
					if(b)
					{
						invalidateBit();
						invalidate();
					}
					dialogIsClosed=true;
				}


			});
		AlertDialog dialog = builder.create();
		dialog.show();
    }
	
	
    void onButt2()
	{
		invalidate();
	}
 

    public Redactor(Context context)
    {
        super(context);
    //    Log.d("redactor1","constructor");

		FileHelper.prepareAll(context);
	
		
		savedContext=context;
		curPathPaint.setColor(Color.YELLOW);
		curPathPaint.setAlpha(150);
		
		gd=new GestureDetector(savedContext, new MyGestureListener());
		pointer=new Pointer();
		
		grayP.setColor(Color.GRAY);
		cleanButtP.setColor(Color.rgb(200,230,120));
		cleanButtP.setAntiAlias(true);
		lineP.setAntiAlias(true);
		lineP.setColor(Color.YELLOW);
		lineP.setAlpha(150);
		lineP.setStyle(Paint.Style.STROKE);
		pnt.setColor(Color.rgb(200,230,120));
		pnt.setAntiAlias(true);
		whiteP.setColor(Color.WHITE);
		whiteP.setAntiAlias(true);
		blackP.setColor(Color.BLACK);
		blackP.setAntiAlias(true);
		redButtP.setColor(Color.RED);
		redButtP.setAlpha(150);
		redButtP.setAntiAlias(true);
		cursorP.setColor(Color.WHITE);
		cursorP.setAlpha(150);
		cursorP.setAntiAlias(true);
		blackButtP.setColor(Color.BLACK);
		blackButtP.setAlpha(150);
		blackButtP.setAntiAlias(true);
 
      //  field=new boolean[kwidth*kheight];
	//	fullField=new Cell[kwidth*kheight];
		
		buttBl=new FullButton[2*kStages-1];
		buttSn=new FullButton[2*kStages-1];
	//	arrowP=new Paint[kArrowShift];
		dTime=0;
		arrowPos=0;
		prevTime=System.currentTimeMillis();
		for(int i=0;i<buttBl.length;i++)
		{
			buttBl[i]=new FullButton();
			buttSn[i]=new FullButton();
		}
		

	//	pointer.dir=Direction.RIGHT;
		
		
		//path
		pathArrow= new Path();
		path=new Path();

        createLevel();

     //   Log.d("arr","kwidth:"+kwidth+" kheight:" + kheight);
     //   Log.d("arr","ksnake:"+ksnake+" kblocks:" + kblocks);
      //  Log.d("arr","snake:"+snake);
     //   Log.d("arr","blocks:"+blocks);
       // Log.d("arr","field:"+ Arrays.toString(field));

        //	pnt.setTextAlign(Paint.Align.CENTER);
		
	  //  createLevelByIntent();
	
		MainActivity.GAME_PHASE=MainActivity.Phase.REDACTOR;

        dialogIsClosed=true;
	    invalidate();
    }
	
	public void onRestart()
	{
		createLevel();
		resetGraphycs();
		menuButton.unpress();
		MainActivity.GAME_PHASE=MainActivity.Phase.REDACTOR;
	//	snred=false;
	//	snredh=false;
	//	snredt=false;
	//	move=true;
		setComponents();
		dialogIsClosed=true;
		
		logMemory("");
	}
	
	public void onStop()
	{
		pointer.recycleBitmap();
		bitmap.recycle();
	}
	
	private void createLevel()
	{
		getMemory();
		setComponents();
	}

	private void setComponents()
    {
        //blocks.clear();
		//snake.clear();
       // kblocks=0;
	    blocksEdit=false;
		k=-1;
		//ksnake=0;
		//	arrButDir=1;
		stageButt=0;
		blred=false;
		blredf=false;
		snred=false;
		snredh=false;
		snredt=false;
		firstSnRed=false;
		firstBlRed=false;
		newBlMode=true;
		blRedNew=true;
		move=true;
		shrinkBl=0;
		shrinkSn=0;
		gradShift=0;
		arrowStage=0;
		blockStage=0;

		//lastSnake=-1;
		cleanMode=false;
		snRedAct=false;
		blRedAct=false;
		right=false;
		drawGreen=false;
		//	textPaint.setTextAlign(Paint.Align.CENTER);

        snake.clear();
		ksnake=LevelInfo.snake.length;
		for(int i=0;i<ksnake;i++)
		{
			snake.add(LevelInfo.snake[i]);
		}
		if(ksnake>0)
	    	lastSnake=LevelInfo.snake[ksnake-1];
        
		blocks.clear();
        kblocks=LevelInfo.blocks.length;
		for(int i=0;i<kblocks;i++)
		{
			blocks.add(LevelInfo.blocks[i]);
        }
		
		
		for(int i=0;i<field.length;i++)
        {
            field[i]=false;
        }
		snakeIt=snake.iterator();
        while(snakeIt.hasNext())
        {
            field[(int)snakeIt.next()]=true;
        }
        for(int i:blocks)
        {
            field[i]=true;
        }
		
		pointer.dir=(LevelInfo.dir);
    }
	
	void getMemory()
	{
		kwidth=LevelInfo.kwidth;
		kheight=LevelInfo.kheight;
		
		field=new boolean[kwidth*kheight];

		fullField=new Cell[kwidth*kheight];
        for(int i=0;i<fullField.length;i++)
            fullField[i]=new Cell();
	}
	
	
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
		path.reset();
		path.moveTo(bxToX(snake.get(0))+r/2,bxToY(snake.get(0))+r/2);
	    snakeIt=snake.iterator();
		while(snakeIt.hasNext())
		{
			int i=snakeIt.next();
	    	path.lineTo(bxToX(i)+r/2,bxToY(i)+r/2);
		}
	//	curPathPaint.setPathEffect(new ComposePathEffect(new PathDashPathEffect(pathArrow,r, dTime, PathDashPathEffect.Style.ROTATE),new CornerPathEffect(r/2)));

	//	for(int i=0;i<kArrowShift;i++)
		//	arrowP[i].setPathEffect(new ComposePathEffect(new PathDashPathEffect(pathArrow,dist, i*arrowShift, PathDashPathEffect.Style.ROTATE),new CornerPathEffect(r/3)));
		
	}
	Rect rectFToRect(RectF rectF)
	{
		Rect r=new Rect();
		r.left=(int)rectF.left-1;
	    r.right=(int)rectF.right+1;
		r.bottom=(int)rectF.bottom+1;
	    r.top=(int)rectF.top-1;
		return r;
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
	
	//line methods
	double k(float x,float y, float x2, float y2)
	{
		double k;
			k=(y2-y)/(x2-x);
		return k;
	}

	int findXr(double x)
	{
		return ((int)(x-stx))/r;
	}

	int findYr(double y)
	{
		return ((int)(y-sty))/r;
	}
	
	boolean lineHead(float x1,float y1,float x2,float y2,boolean b)
	{
		boolean re=true;
		double koef=k(x1,y1,x2,y2);
		
		metka:
		if(	Math.abs(koef)<1)
		{
		    int k;
			int v;
		    if(x2<x1)
	     	{
				k=-1;
				v=-1;
	    	}
			else
			{
		        k=1;
			    v=0;
			}

			int kolx=-findXr(x1)+findXr(x2);

			double part;
			double newy=sty;
			double oldy=y1;
			boolean two;
			int place;

			for(int i=0;i!=kolx;i+=k)
			{
				part=(k*(findXr(x1)*r+stx)+k*(i+1+v)*r-k*x1)/(x2-x1);
				newy=(y1+k*(y2-y1)*part);

				if(findYr(newy)!=findYr(oldy))
					two=true;
				else
					two=false;

	         	place=findYr(oldy)*kwidth+findXr(x1)+i;
		    	if(oldy>=sty&&oldy<height-sty&&((findXr(x1)+i)>=0)&&(findXr(x1)+i)<kwidth)
				{
					if(!field[place])
						addSnakeHead(b,place);
					else
					{
						int oi;
						if(!b)
							oi=snake.get(0);
						else
							oi=lastSnake;
					if(place!=oi)
					{
						re=false;
						break metka;
					}}
			    }
		    	else
				{
					re=false;
			    	break metka;
				}

				if(two)
				{
					place=findYr(newy)*kwidth+findXr(x1)+i;
					if(newy>=sty&&newy<height-sty&&((findXr(x1)+i)>=0)&&(findXr(x1)+i)<kwidth)
					{
						if(!field[place])
							addSnakeHead(b,place);
						else
						{
							int oi;
							if(!b)
								oi=snake.get(0);
							else
								oi=lastSnake;
						if(place!=oi)
						{
							re=false;
							break metka;
						}}
					}
					else
					{
						re=false;
						break metka;
					}
				}
				oldy=newy;
			}
			place=(findYr(oldy))*kwidth+findXr(x1)+kolx;
			if(findYr(oldy)>=0&&findYr(oldy)<kheight&&(findXr(x1)+kolx>=0)&&(findXr(x1)+kolx)<kwidth)
			{
				if(!field[place])
					addSnakeHead(b,place);
				else
				{
					int oi;
					if(!b)
						oi=snake.get(0);
					else
						oi=lastSnake;
				if(place!=oi)
				{
					re=false;
					break metka;
				}}
			}
			else
				break metka;
			if(findYr(newy)!=findYr(y2))
			{
				place=findYr(y2)*kwidth+findXr(x1)+kolx;
				if(y2>=sty&&y2<height-sty&&((findXr(x1)+kolx)>=0)&&(findXr(x1)+kolx)<kwidth)
				{
					if(!field[place])
						addSnakeHead(b,place);
					else
					{
						int oi;
						if(b)
							oi=snake.get(0);
						else
							oi=lastSnake;
					if(place!=oi)
					{
						re=false;
						break metka;
					}}
				}
				else
				{
					re=false;
					break metka;
				}
			}

		}
		//вторая прямая(2 случай)
		else
		{
			int k;
			int v;
		    if(y2<y1)
	     	{
				k=-1;
				v=-1;
	    	}
			else
			{
		        k=1;
			    v=0;
			}


			int koly=-findYr(y1)+findYr(y2);

			double part;
			double newx=stx;
			double oldx=x1;
			boolean two;
            int place;

			for(int i=0;i!=koly;i+=k)
			{
				part=(k*(findYr(y1)*r+sty)+k*(i+1+v)*r-k*y1)/(y2-y1);
				newx=(x1+k*(x2-x1)*part);
				if(findXr(newx)!=findXr(oldx))
					two=true;
				else
					two=false;
				place=findXr(oldx)+(findYr(y1)+i)*kwidth;
				if(findXr(oldx)>=0&&findXr(oldx)<kwidth&&(findYr(y1)+i)>=0&&(findYr(y1)+i)<kheight)
				{
	      			if(!field[place])
						addSnakeHead(b,place);
					else
					{
						int oi;
						if(!b)
							oi=snake.get(0);
						else
							oi=lastSnake;
				    	if(place!=oi)
				    	{
				    		re=false;
				    		break metka;
				    	}
					}
				}
			    else
				{
					re=false;
					break metka;
				}

				if(two)
				{
					place=findXr(newx)+(findYr(y1)+i)*kwidth;
					if(findXr(newx)>=0&&findXr(newx)<kwidth&&(findYr(y1)+i)>=0&&(findYr(y1)+i)<kheight)
					{
						if(!field[place])
							addSnakeHead(b,place);
						else
						{
							int oi;
							if(!b)
								oi=snake.get(0);
							else
								oi=lastSnake;
						if(place!=oi)
						{
							re=false;
					    	break metka;
						}}
					}
					else
					{
						re=false;
						break metka;
					}
				}
				oldx=newx;
			}

			place =(findYr(y1)+koly)*kwidth+findXr(oldx);
			if(findXr(oldx)>=0&&findXr(oldx)<kwidth&&(findYr(y1)+koly)>=0&&(findYr(y1)+koly)<kheight)
			{
				if(!field[place])
					addSnakeHead(b,place);
				else
				{
					int oi;
					if(!b)
				        oi=snake.get(0);
					else
					    oi=lastSnake;
			    	if(place!=oi)
			    	{
					    re=false;
				    	break metka;
			    	}
				}
			}
			else
			{
				re=false;
				break metka;
			}

			if(findXr(newx)!=findXr(x2))
			{

			    place =(findYr(y1)+koly)*kwidth+findXr(x2);
				if(findXr(x2)>=0&&findXr(x2)<kwidth&&(findYr(y1)+koly)>=0&&(findYr(y1)+koly)<kheight)
				{
					if(!field[place])
						addSnakeHead(b,place);
					else
					{
						int oi;
						if(!b)
							oi=snake.get(0);
						else
							oi=lastSnake;
					if(place!=oi)
					{
						re=false;
						break metka;
					}}
				}
				else
				{
					re=false;
					break metka;
				}
			}
		}
	//	newPath();
		return re;
	}
	
	
	void lineBlock(float x1,float y1,float x2,float y2)
	{
		double koef=k(x1,y1,x2,y2);
		
		metka:
		if(	Math.abs(koef)<1)
		{
		    int k;
			int v;
		    if(x2<x1)
	     	{
				k=-1;
				v=-1;
	    	}
			else
			{
		        k=1;
			    v=0;
			}

			int kolx=-findXr(x1)+findXr(x2);

			double part;
			double newy=sty;
			double oldy=y1;
			boolean two;
			int place;

			for(int i=0;i!=kolx;i+=k)
			{
				part=(k*(findXr(x1)*r+stx)+k*(i+1+v)*r-k*x1)/(x2-x1);
				newy=(y1+k*(y2-y1)*part);

				if(findYr(newy)!=findYr(oldy))
					two=true;
				else
					two=false;

	         	place=findYr(oldy)*kwidth+findXr(x1)+i;
		    	if(oldy>=sty&&oldy<height-sty&&((findXr(x1)+i)>=0)&&(findXr(x1)+i)<kwidth)
				{
						newblock(place);
			    }
		
				if(two)
				{
					place=findYr(newy)*kwidth+findXr(x1)+i;
					if(newy>=sty&&newy<height-sty&&((findXr(x1)+i)>=0)&&(findXr(x1)+i)<kwidth)
					{
							newblock(place);
					}
				}
				oldy=newy;
			}
			place=(findYr(oldy))*kwidth+findXr(x1)+kolx;
			if(findYr(oldy)>=0&&findYr(oldy)<kheight&&(findXr(x1)+kolx>=0)&&(findXr(x1)+kolx)<kwidth)
			{
				newblock(place);
			}
			if(findYr(newy)!=findYr(y2))
			{
				place=findYr(y2)*kwidth+findXr(x1)+kolx;
				if(y2>=sty&&y2<height-sty&&((findXr(x1)+kolx)>=0)&&(findXr(x1)+kolx)<kwidth)
				{
					newblock(place);
			    }
			}

		}
		//вторая прямая(2 случай)
		else
		{
			int k;
			int v;
		    if(y2<y1)
	     	{
				k=-1;
				v=-1;
	    	}
			else
			{
		        k=1;
			    v=0;
			}


			int koly=-findYr(y1)+findYr(y2);

			double part;
			double newx=stx;
			double oldx=x1;
			boolean two;
            int place;

			for(int i=0;i!=koly;i+=k)
			{
				part=(k*(findYr(y1)*r+sty)+k*(i+1+v)*r-k*y1)/(y2-y1);
				newx=(x1+k*(x2-x1)*part);
				if(findXr(newx)!=findXr(oldx))
					two=true;
				else
					two=false;
				place=findXr(oldx)+(findYr(y1)+i)*kwidth;
				if(findXr(oldx)>=0&&findXr(oldx)<kwidth&&(findYr(y1)+i)>=0&&(findYr(y1)+i)<kheight)
				{
					newblock(place);
				}
	
				if(two)
				{
					place=findXr(newx)+(findYr(y1)+i)*kwidth;
					if(findXr(newx)>=0&&findXr(newx)<kwidth&&(findYr(y1)+i)>=0&&(findYr(y1)+i)<kheight)
					{
						newblock(place);
					}
				}
				oldx=newx;
			}

			place =(findYr(y1)+koly)*kwidth+findXr(oldx);
			if(findXr(oldx)>=0&&findXr(oldx)<kwidth&&(findYr(y1)+koly)>=0&&(findYr(y1)+koly)<kheight)
			{
				newblock(place);
			}
			
			if(findXr(newx)!=findXr(x2))
			{

			    place =(findYr(y1)+koly)*kwidth+findXr(x2);
				if(findXr(x2)>=0&&findXr(x2)<kwidth&&(findYr(y1)+koly)>=0&&(findYr(y1)+koly)<kheight)
				{
					newblock(place);
			    }
			}
		}
	}
	
	void lineClear(float x1,float y1,float x2,float y2)
    {
		double koef=k(x1,y1,x2,y2);

		metka:
		if(	Math.abs(koef)<1)
		{
		    int k;
			int v;
		    if(x2<x1)
	     	{
				k=-1;
				v=-1;
	    	}
			else
			{
		        k=1;
			    v=0;
			}

			int kolx=-findXr(x1)+findXr(x2);

			double part;
			double newy=sty;
			double oldy=y1;
			boolean two;
			int place;

			for(int i=0;i!=kolx;i+=k)
			{
				part=(k*(findXr(x1)*r+stx)+k*(i+1+v)*r-k*x1)/(x2-x1);
				newy=(y1+k*(y2-y1)*part);

				if(findYr(newy)!=findYr(oldy))
					two=true;
				else
					two=false;

	         	place=findYr(oldy)*kwidth+findXr(x1)+i;
		    	if(oldy>=sty&&oldy<height-sty&&((findXr(x1)+i)>=0)&&(findXr(x1)+i)<kwidth)
				{
					if(field[place])
				    	if(blocks.contains(place))
				        	deleteblock(place);
			    }

				if(two)
				{
					place=findYr(newy)*kwidth+findXr(x1)+i;
					if(newy>=sty&&newy<height-sty&&((findXr(x1)+i)>=0)&&(findXr(x1)+i)<kwidth)
					{
						if(field[place])
							if(blocks.contains(place))
								deleteblock(place);
					}
				}
				oldy=newy;
			}
			place=(findYr(oldy))*kwidth+findXr(x1)+kolx;
			if(findYr(oldy)>=0&&findYr(oldy)<kheight&&(findXr(x1)+kolx>=0)&&(findXr(x1)+kolx)<kwidth)
			{
				if(field[place])
					if(blocks.contains(place))
						deleteblock(place);
			}
			if(findYr(newy)!=findYr(y2))
			{
				place=findYr(y2)*kwidth+findXr(x1)+kolx;
				if(y2>=sty&&y2<height-sty&&((findXr(x1)+kolx)>=0)&&(findXr(x1)+kolx)<kwidth)
				{
					if(field[place])
				    	if(blocks.contains(place))
				        	deleteblock(place); }
		    	}

		}
		//вторая прямая(2 случай)
		else
		{
			int k;
			int v;
		    if(y2<y1)
	     	{
				k=-1;
				v=-1;
	    	}
			else
			{
		        k=1;
			    v=0;
			}


			int koly=-findYr(y1)+findYr(y2);

			double part;
			double newx=stx;
			double oldx=x1;
			boolean two;
            int place;

			for(int i=0;i!=koly;i+=k)
			{
				part=(k*(findYr(y1)*r+sty)+k*(i+1+v)*r-k*y1)/(y2-y1);
				newx=(x1+k*(x2-x1)*part);
				if(findXr(newx)!=findXr(oldx))
					two=true;
				else
					two=false;
				place=findXr(oldx)+(findYr(y1)+i)*kwidth;
				if(findXr(oldx)>=0&&findXr(oldx)<kwidth&&(findYr(y1)+i)>=0&&(findYr(y1)+i)<kheight)
				{
					if(field[place])
				    	if(blocks.contains(place))
				        	deleteblock(place);
				}

				if(two)
				{
					place=findXr(newx)+(findYr(y1)+i)*kwidth;
					if(findXr(newx)>=0&&findXr(newx)<kwidth&&(findYr(y1)+i)>=0&&(findYr(y1)+i)<kheight)
					{
						if(field[place])
							if(blocks.contains(place))
								deleteblock(place);
					}
				}
				oldx=newx;
			}

			place =(findYr(y1)+koly)*kwidth+findXr(oldx);
			if(findXr(oldx)>=0&&findXr(oldx)<kwidth&&(findYr(y1)+koly)>=0&&(findYr(y1)+koly)<kheight)
			{
				if(field[place])
					if(blocks.contains(place))
						deleteblock(place);
			}

			if(findXr(newx)!=findXr(x2))
			{

			    place =(findYr(y1)+koly)*kwidth+findXr(x2);
				if(findXr(x2)>=0&&findXr(x2)<kwidth&&(findYr(y1)+koly)>=0&&(findYr(y1)+koly)<kheight)
				{
					if(field[place])
				    	if(blocks.contains(place))
				        	deleteblock(place);
			    }
			}
		}
	}
	
	//edit blocks
    void newblock(int Bx)
    {
            if(!field[Bx])
            {
                blocks.add(Bx);
                kblocks++;
                field[Bx]=true;
			    //canvRef=true;
				invalidateBit(areaRect(Bx));
				invalidate(areaRect(Bx));
			//	invalidateBit();
			//	invalidate();
            }
    }
	
	void deleteblock(int blDown)
	{
			field[blDown]=false;
			kblocks--;
			blocks.remove(blocks.indexOf(blDown));
	     //	canvRef=true;
	    	invalidateBit(areaRect(blDown));
			invalidate(areaRect(blDown));
		   // invalidateBit();
		//	invalidate();
	}
/*	void editblock(int blDow,int Bx)
	{
		if(!field[Bx])
		{
			field[Bx]=true;
	    	field[blDow]=false;
			int ind=blocks.indexOf(blDow);
	//	canvRef=true;
        //	out1=blDow;
			blocks.set(ind ,Bx);
		//	invalidate(areaRect(blDow));
		//	invalidate(areaRect(Bx));
	    	invalidateBit();
			
		}
	}*/
	
	
	//edit snake
	void newsnake(int Bx)
    {
		if(!field[Bx])
		{
            if(!head)
            {
                head = true;
			//	lastSnake=Bx;
            }
            else
            {
				snakeIt=snake.iterator();
				int i;
                while(snakeIt.hasNext())
				{
				   i=snakeIt.next();
                    field[i]=false;
				}
                snake.clear();
                ksnake=0;
            }
			snake.add(Bx);
			lastSnake=Bx;
			ksnake++;
		//	canvRef=true;
			field[Bx]=true;
			newPath();
			if(!snred)
		    	invalidateBit(areaRect(Bx));
			else
				invalidateBit();
			invalidate();
		}
    }
/*
	void deletesnake(int blDown)
	{
		field[blDown]=false;
		ksnake--;
		canvRef=true;
		newPath();
		snake.remove(snake.indexOf(blDown));
		invalidateBit(areaRect(blDown));
	}*/
	
	//плохо
	void deleteSnakeIndex(int in)
	{
		int inte=snake.get(in);
		field[inte]=false;
		ksnake--;
		snake.remove(in);
		if(in==ksnake)
			lastSnake=snake.get(ksnake-1);
		newPath();
		invalidateBit(areaRect(inte));
	}
	void addsnake(int Bx)
	{
			field[Bx]=true;
            snake.add(Bx);
		//	if(ksnake>0)
		    	lastSnake=Bx;
            ksnake++;
			newPath();
     	   // canvRef=true;
	      //  invalidate(areaRect(Bx))
		  if(snred)
		  {
		     invalidateBit();
			 invalidate();
		  }
		  else
		  {
			  invalidateBit(areaRect(Bx));
			  invalidate(areaRect(Bx));
		  }
		 
	}
	void addsnaket(int Bx)
	{
		field[Bx]=true;
		snake.add(0,Bx);
		ksnake++;
		newPath();
		invalidateBit(areaRect(Bx));
	//	canvRef=true;
		//invalidate(areaRect(Bx));
		invalidate();
	}
	void addSnakeHead(boolean b, int Bx)
	{
		field[Bx]=true;
		if(b)
		{
			snake.add(Bx);
			lastSnake=Bx;
		}
		else
	    	snake.add(0,Bx);
		ksnake++;
		newPath();
		//new path в фци line
		invalidateBit(areaRect(Bx));
		invalidate(areaRect(Bx));
	}
	
	
	protected void invalidateBit()
	{
		bitmapCanvas.drawColor(Color.LTGRAY);
//	bitmapCanvas.drawColor(Color.BLUE);
		bitmapCanvas.drawRect(workSpace,grayP);
        //поле
        bitmapCanvas.drawRect(stx,sty,width-stx,height-sty,pnt);

        //черный слой с тенью

        for(int i=0;i<kblocks;i++)
        {
			bitmapCanvas.drawRoundRect(fullField[blocks.get(i)].black,roundBlack,roundBlack,fullField[blocks.get(i)].bigP);
        }

		int ne;
		snakeIt=snake.iterator();
		while(snakeIt.hasNext())
		{
		    ne=snakeIt.next();
			bitmapCanvas.drawRoundRect(fullField[ne].black,roundBlack,roundBlack,fullField[ne].bigP);
		}


		//белый слой
		for(int i=0;i<kblocks;i++)
        {
			bitmapCanvas.drawRoundRect(fullField[blocks.get(i)].white,roundWhite,roundWhite,whiteP);
        }

		snakeIt=snake.iterator();
		while(snakeIt.hasNext())
        {
			bitmapCanvas.drawRoundRect(fullField[snakeIt.next()].white,roundWhite,roundWhite,whiteP);
		}

		//цветной слой
		for(int i=0;i<kblocks;i++)
        {
			bitmapCanvas.drawRoundRect(fullField[blocks.get(i)].color,roundColor,roundColor,fullField[blocks.get(i)].blockP);
        }


		snakeIt=snake.iterator();
		while(snakeIt.hasNext())
        {
		    ne=snakeIt.next();
		//	fullField[snake.get((i)%ksnake)].snakeP.setColor(Color.argb(170,(int)((255f/kwidth/kheight)*(kwidth*kheight-i)),0,0));
		
		    bitmapCanvas.drawRoundRect(fullField[ne].color,roundColor,roundColor,fullField[ne].snakeP);
		}



		//кружок на хвосте

		if(ksnake>1)
		{	
			//  pnt.setColor(Color.BLUE);
			bitmapCanvas.drawRoundRect(fullField[lastSnake].color,roundColor*2,roundColor*2,pnt);
        }

       // Log.d("last",lastSnake+"");
		//кружок на голове

		if(ksnake>0)
		{
			//	 pnt.setColor(Color.CYAN);
			bitmapCanvas.drawRoundRect(fullField[snake.get(0)].color,roundColor*2,roundColor*2,pnt);
		}

		if(ksnake>1)
	        bitmapCanvas.drawPath(path,lineP);


	//	invalidate();
	}
	
	protected void invalidateBit(Rect rect)
	{
	//	bitmapCanvas.drawRect(rect,blackP);
		bitmapCanvas.save();
		bitmapCanvas.clipRect(rect);
       
		bitmapCanvas.drawColor(Color.LTGRAY);

		bitmapCanvas.drawRect(workSpace,grayP);
        //поле
        bitmapCanvas.drawRect(stx,sty,width-stx,height-sty,pnt);

	//	bitmapCanvas.drawRect(rect,whiteP);
        //черный слой с тенью

        for(int i=0;i<kblocks;i++)
        {
			bitmapCanvas.drawRoundRect(fullField[blocks.get(i)].black,roundBlack,roundBlack,fullField[blocks.get(i)].bigP);
        }
		/*
		 for(int i=0;i<ksnake;i++)
		 {
		 canvas.drawRoundRect(fullField[snake.get(i)].black,roundBlack,roundBlack,fullField[snake.get(i)].bigP);
		 }*/
		//занаво созд или обнулять можно??
		int ne;
		snakeIt=snake.iterator();
		while(snakeIt.hasNext())
		{
		    ne=snakeIt.next();
			bitmapCanvas.drawRoundRect(fullField[ne].black,roundBlack,roundBlack,fullField[ne].bigP);
		}


		//белый слой
		for(int i=0;i<kblocks;i++)
        {
			bitmapCanvas.drawRoundRect(fullField[blocks.get(i)].white,roundWhite,roundWhite,whiteP);
        }

		snakeIt=snake.iterator();
		while(snakeIt.hasNext())
        {
			bitmapCanvas.drawRoundRect(fullField[snakeIt.next()].white,roundWhite,roundWhite,whiteP);
		}

		//цветной слой
		for(int i=0;i<kblocks;i++)
        {
			bitmapCanvas.drawRoundRect(fullField[blocks.get(i)].color,roundColor,roundColor,fullField[blocks.get(i)].blockP);
        }


		snakeIt=snake.iterator();
		while(snakeIt.hasNext())
        {
		    ne=snakeIt.next();
			//	fullField[snake.get((i/*+gradShift*/)%ksnake)].snakeP.setColor(Color.argb(170,(int)((255f/kwidth/kheight)*(kwidth*kheight-i)),0,0));
		
			
			  bitmapCanvas.drawRoundRect(fullField[ne].color,roundColor,roundColor,fullField[ne].snakeP);
		}



		//кружок на хвосте

		if(ksnake>1)
		{	
			//  pnt.setColor(Color.BLUE);
			bitmapCanvas.drawRoundRect(fullField[lastSnake].color,roundColor*2,roundColor*2,pnt);
        }
		//кружок на голове

		if(ksnake>0)
		{
			//	 pnt.setColor(Color.CYAN);
			bitmapCanvas.drawRoundRect(fullField[snake.get(0)].color,roundColor*2,roundColor*2,pnt);
		}
		if(ksnake>1)
	    	bitmapCanvas.drawPath(path,lineP);
		bitmapCanvas.restore();
		
	}


	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
          //  Log.d("redactor1","sizeChanged");
	    	width=w;
            height=h;
			
			
			
			editorMenuScreen=new EditorMenuScreen(w,h);
			editorMenuScreen.slideAnim.addListener(	new Animator.AnimatorListener()
			{
				@Override
				public void onAnimationStart(Animator animation) 
				{
					MainActivity.GAME_PHASE=MainActivity.Phase.MENU_REDACTOR;
					
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
					editorMenuScreen.buttonsAreActive=true;
			
				}

				@Override
				public void onAnimationCancel(Animator animation) 
				{

				}
			});

		editorMenuScreen.reverseSlideAnim.addListener(	new Animator.AnimatorListener() 
			{
				@Override
				public void onAnimationStart(Animator animation) 
				{
					//	for(int i=0;i<gameOverScreen.buttons.length;i++)
					editorMenuScreen.buttonsAreActive=false;

				}

				@Override
				public void onAnimationRepeat(Animator animation) 
				{

				}

				@Override
				public void onAnimationEnd(Animator animation) 
				{
					for(int i=0;i<editorMenuScreen.buttons.length;i++)
						if(editorMenuScreen.buttons[i].pressed)
						{
							//	Toast.makeText(savedContext,Integer.toString(i),Toast.LENGTH_SHORT).show();

							editorMenuScreen.buttons[i].unpress();
							switch(i)
							{
								case 0:
								break;
								case 1:
								//	Toast.makeText(savedContext,"work",Toast.LENGTH_SHORT).show();
									MainActivity.GAME_PHASE=MainActivity.Phase.MAIN_MENU;
									LevelInfo.needLevelChoice=false;
									Intent intent=new Intent(savedContext,MainActivity.class);
									intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
									
									savedContext.startActivity(intent);
								//	RedactorActivity.activity.finish();
									break;
								case 2:
									MainActivity.GAME_PHASE=MainActivity.Phase.REDACTOR;
									menuButton.unpress();

									snred=false;
									snredh=false;
									snredt=false;
									move=true;
									dialogIsClosed=true;
									break;
								case 3:
									MainActivity.GAME_PHASE=MainActivity.Phase.LEVEL_CHOICER;
									Intent intent1=new Intent(savedContext,MainActivity.class);
									intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
									
									LevelInfo.needLevelChoice=true;
									savedContext.startActivity(intent1);
								//	RedactorActivity.activity.finish();
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
			
            int min=Math.min(width,height);
			int max=Math.max(width,height);
			int kmax=Math.max(kwidth,kheight);
			r=min/kmax;
			int tx=(max-kmax*r)/2;
			if (width<height)
            {
                //r=width/kwidth;
                stx=(width-kwidth*r)/2;
                sty=(height-kheight*r)/2;
				
				workSpace.left=0;
				workSpace.right=width;
				workSpace.top=tx;
				workSpace.bottom=height-tx;
				
				
				clearButton.setBlackRect(width*1/20f,height-tx*3/4,width*7/20f,height-tx/4);
				saveButton.setBlackRect(width*8/20f,height-tx*3/4,width*14/20f,height-tx/4);
				menuButton.setBlackRect(14f/20*width+3f/20*width-saveButton.black.height()/2, saveButton.black.top,
										14f/20*width+3f/20*width+saveButton.black.height()/2,saveButton.black.bottom);
				
				
				//черный квадр кнопки
                buttBl[0].black.left=width/8f;
                buttBl[0].black.right=buttBl[0].black.left+2/3f*tx;
                buttBl[0].black.top=tx/6f;
                buttBl[0].black.bottom=5/6f*tx;
				buttBl[0].roundBlack=buttBl[0].black.width()/3;
			
				buttSn[0].black.left=width-buttBl[0].black.right;
                buttSn[0].black.right=width-buttBl[0].black.left;
                buttSn[0].black.top=buttBl[0].black.top;
                buttSn[0].black.bottom=buttBl[0].black.bottom;
				buttSn[0].roundBlack=buttBl[0].roundBlack;
				
			
		    }
            else
            {
               // r=height/kwidth;
                stx=(-kwidth*r+width)/2;
                sty=(height-kheight*r)/2;
                
				workSpace.left=tx;
				workSpace.right=width-tx;
				workSpace.top=0;
				workSpace.bottom=height;
				
				//черный квадр кнопки
                buttBl[0].black.left=tx/6f;
				buttBl[0].black.right=5/6f*tx;
                buttBl[0].black.top=height/8f;
				buttBl[0].black.bottom=buttBl[0].black.top+2*tx/3f;
				buttBl[0].roundBlack=buttBl[0].black.width()/3;
				
				buttSn[0].black.left=width-buttBl[0].black.right;
                buttSn[0].black.right=width-buttBl[0].black.left;
                buttSn[0].black.top=buttBl[0].black.top;
                buttSn[0].black.bottom=buttBl[0].black.bottom;
				buttSn[0].roundBlack=buttBl[0].black.width()/3;
				
				clearButton.setBlackRect(tx/6,height-tx*5f/8,5f/6*tx,height-tx*3f/8);
				saveButton.setBlackRect(width-5f/6*tx,height-tx*5f/8,width-tx/6f,height-tx*3f/8);
				
            }
			
		    pdelr=r/32;
            pdel=r/16;
            normWidthBl=buttSn[0].black.width();
			
		    
	//		delta=block.width()/10;		
			
			//белый квадр кнопки
			buttBl[0].white.left=buttBl[0].black.left+normWidthBl/16;
			buttBl[0].white.right=buttBl[0].black.right-normWidthBl/16;
			buttBl[0].white.top=buttBl[0].black.top+normWidthBl/16;
			buttBl[0].white.bottom=buttBl[0].black.bottom-normWidthBl/16;
			buttBl[0].roundWhite=buttBl[0].white.width()/3;

			buttSn[0].white.left=buttSn[0].black.left+normWidthBl/16;
			buttSn[0].white.right=buttSn[0].black.right-normWidthBl/16;
			buttSn[0].white.top=buttSn[0].black.top+normWidthBl/16;
			buttSn[0].white.bottom=buttSn[0].black.bottom-normWidthBl/16;
			buttSn[0].roundWhite=buttSn[0].white.width()/3;
	
			
			normWidthWhiteBl=buttSn[0].white.width();
			
			//цветной квадрат кнопки
			
			buttBl[0].color.left=buttBl[0].white.left+normWidthBl/16;
			buttBl[0].color.right=buttBl[0].white.right-normWidthBl/16;
			buttBl[0].color.top=buttBl[0].white.top+normWidthBl/16;
			buttBl[0].color.bottom=buttBl[0].white.bottom-normWidthBl/16;
			buttBl[0].roundColor=buttBl[0].color.width()/3;

			buttSn[0].color.left=buttSn[0].white.left+normWidthBl/16;
			buttSn[0].color.right=buttSn[0].white.right-normWidthBl/16;
			buttSn[0].color.top=buttSn[0].white.top+normWidthBl/16;
			buttSn[0].color.bottom=buttSn[0].white.bottom-normWidthBl/16;
			buttSn[0].roundColor=buttSn[0].color.width()/3;
			
			normWidthColorBl=buttSn[0].color.width();
			//мутим все для кнопок
			
			for(int i=1;i<kStages;i++)
		    {
				//черный блок
				buttBl[i].black.left=buttBl[0].black.left+i*normWidthBl/2/kStages;
				buttBl[i].black.right=buttBl[0].black.right-i*normWidthBl/2/kStages;
				buttBl[i].black.top=buttBl[0].black.top+i*normWidthBl/2/kStages;
				buttBl[i].black.bottom=buttBl[0].black.bottom-i*normWidthBl/2/kStages;
				buttBl[i].roundBlack=buttBl[i].black.width()/3;
				
			    buttBl[i+kStages-1].black.set(buttBl[0].black);
				buttBl[i+kStages-1].roundBlack=buttBl[0].black.width()/3;
				
				buttSn[i].black.left=buttSn[0].black.left+i*normWidthBl/2/kStages;
				buttSn[i].black.right=buttSn[0].black.right-i*normWidthBl/2/kStages;
				buttSn[i].black.top=buttSn[0].black.top+i*normWidthBl/2/kStages;
				buttSn[i].black.bottom=buttSn[0].black.bottom-i*normWidthBl/2/kStages;
				buttSn[i].roundBlack=buttSn[i].black.width()/3;
				
				buttSn[i+kStages-1].black.set(buttSn[0].black);
				buttSn[i+kStages-1].roundBlack=buttSn[0].black.width()/3;
				
				//белый блок
				buttBl[i].white.left=buttBl[0].white.left+i*normWidthWhiteBl/2/kStages;
				buttBl[i].white.right=buttBl[0].white.right-i*normWidthWhiteBl/2/kStages;
				buttBl[i].white.top=buttBl[0].white.top+i*normWidthWhiteBl/2/kStages;
				buttBl[i].white.bottom=buttBl[0].white.bottom-i*normWidthWhiteBl/2/kStages;
				buttBl[i].roundWhite=buttBl[i].white.width()/3;

			    buttBl[i+kStages-1].white.left=buttBl[0].white.left-(i+1)*(-normWidthWhiteBl+normWidthBl)/2/kStages;
				buttBl[i+kStages-1].white.right=buttBl[0].white.right+(1+i)*(-normWidthWhiteBl+normWidthBl)/2/kStages;
				buttBl[i+kStages-1].white.top=buttBl[0].white.top-(i+1)*(-normWidthWhiteBl+normWidthBl)/2/kStages;
				buttBl[i+kStages-1].white.bottom=buttBl[0].white.bottom+(i+1)*(-normWidthWhiteBl+normWidthBl)/2/kStages;
				buttBl[i+kStages-1].roundWhite=buttBl[i+kStages-1].white.width()/3;
		
				buttSn[i].white.left=buttSn[0].white.left+i*normWidthWhiteBl/2/kStages;
				buttSn[i].white.right=buttSn[0].white.right-i*normWidthWhiteBl/2/kStages;
				buttSn[i].white.top=buttSn[0].white.top+i*normWidthWhiteBl/2/kStages;
				buttSn[i].white.bottom=buttSn[0].white.bottom-i*normWidthWhiteBl/2/kStages;
				buttSn[i].roundWhite=buttSn[i].white.width()/3;


			    buttSn[i+kStages-1].white.left=buttSn[0].white.left-(i+1)*(-normWidthWhiteBl+normWidthBl)/2/kStages;
				buttSn[i+kStages-1].white.right=buttSn[0].white.right+(1+i)*(-normWidthWhiteBl+normWidthBl)/2/kStages;
				buttSn[i+kStages-1].white.top=buttSn[0].white.top-(i+1)*(-normWidthWhiteBl+normWidthBl)/2/kStages;
				buttSn[i+kStages-1].white.bottom=buttSn[0].white.bottom+(i+1)*(-normWidthWhiteBl+normWidthBl)/2/kStages;
				buttSn[i+kStages-1].roundWhite=buttSn[i+kStages-1].white.width()/3;
				
				//цветной слой кнопки
				buttBl[i].color.left=buttBl[0].color.left+i*normWidthColorBl/2/kStages;
				buttBl[i].color.right=buttBl[0].color.right-i*normWidthColorBl/2/kStages;
				buttBl[i].color.top=buttBl[0].color.top+i*normWidthColorBl/2/kStages;
				buttBl[i].color.bottom=buttBl[0].color.bottom-i*normWidthColorBl/2/kStages;
				buttBl[i].roundColor=buttBl[i].color.width()/3;

			    buttBl[i+kStages-1].color.left=buttBl[0].color.left-(i+1)*(-normWidthColorBl+normWidthBl)/2/kStages;
				buttBl[i+kStages-1].color.right=buttBl[0].color.right+(1+i)*(-normWidthColorBl+normWidthBl)/2/kStages;
				buttBl[i+kStages-1].color.top=buttBl[0].color.top-(i+1)*(-normWidthColorBl+normWidthBl)/2/kStages;
				buttBl[i+kStages-1].color.bottom=buttBl[0].color.bottom+(i+1)*(-normWidthColorBl+normWidthBl)/2/kStages;
				buttBl[i+kStages-1].roundColor=buttBl[i+kStages-1].color.width()/3;

				buttSn[i].color.left=buttSn[0].color.left+i*normWidthColorBl/2/kStages;
				buttSn[i].color.right=buttSn[0].color.right-i*normWidthColorBl/2/kStages;
				buttSn[i].color.top=buttSn[0].color.top+i*normWidthColorBl/2/kStages;
				buttSn[i].color.bottom=buttSn[0].color.bottom-i*normWidthColorBl/2/kStages;
				buttSn[i].roundColor=buttSn[i].color.width()/3;


			    buttSn[i+kStages-1].color.left=buttSn[0].color.left-(i+1)*(-normWidthColorBl+normWidthBl)/2/kStages;
				buttSn[i+kStages-1].color.right=buttSn[0].color.right+(1+i)*(-normWidthColorBl+normWidthBl)/2/kStages;
				buttSn[i+kStages-1].color.top=buttSn[0].color.top-(i+1)*(-normWidthColorBl+normWidthBl)/2/kStages;
				buttSn[i+kStages-1].color.bottom=buttSn[0].color.bottom+(i+1)*(-normWidthColorBl+normWidthBl)/2/kStages;
				buttSn[i+kStages-1].roundColor=buttSn[i+kStages-1].color.width()/3;
				
			}
           
		//	pointer.decodeSampledBitmapFromResource((int)(r),r);

			
			//заполняем Full массив поля
			for(int i=0;i<kwidth*kheight;i++)
			{
				fullField[i].setRectsAndP(i,kwidth,kheight,r,stx,sty);
			}
			
			roundBlack=fullField[0].black.width()/3;
			roundWhite=fullField[0].white.width()/3;
			roundColor=fullField[0].color.width()/3;
			
	    	pointer.loadBitmap((int)fullField[0].color.width(),r);
			
			rectBlTest=rectFToRect(buttBl[0].black);
			rectSnTest=rectFToRect(buttSn[0].black);

		//	pathArrow.reset();
			pathArrow.rewind();
			pathArrow.lineTo(-r/2+pdelr+2*pdel+roundColor,-r/2+pdelr+2*pdel);
			pathArrow.lineTo(r/2-pdelr-2*pdel-roundColor,0);
			pathArrow.lineTo(-r/2+pdelr+2*pdel+roundColor,r/2-pdelr-2*pdel);
			pathArrow.close();
/*			

			but.set(width/4,height-100,width/2,height);
			but1.set(width/2,height-100,width*3/4,height);
			*/
		    bitmap= Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
			bitmapCanvas=new Canvas(bitmap);
			
			dist=r;
			arrowSpeed*=r;
		//	arrowShift=dist/kArrowShift;
			lineP.setStrokeWidth(r/16);
			cornerPE=new CornerPathEffect(r/3);
			lineP.setPathEffect(cornerPE);

            if(ksnake>0)
			    newPath();
	     	invalidateBit();
			
			
			logMemory("1:");
		    super.onSizeChanged(w, h, oldw, oldh);
	}
	
	private void resetGraphycs()
	{
		int min=Math.min(width,height);
		int max=Math.max(width,height);
		int kmax=Math.max(kwidth,kheight);
		r=min/kmax;
		int tx=(max-kmax*r)/2;
		if (width<height)
		{
			//r=width/kwidth;
			stx=(width-kwidth*r)/2;
			sty=(height-kheight*r)/2;

			workSpace.left=0;
			workSpace.right=width;
			workSpace.top=tx;
			workSpace.bottom=height-tx;


			clearButton.setBlackRect(width*1/20f,height-tx*3/4,width*7/20f,height-tx/4);
			saveButton.setBlackRect(width*8/20f,height-tx*3/4,width*14/20f,height-tx/4);
			menuButton.setBlackRect(14f/20*width+3f/20*width-saveButton.black.height()/2, saveButton.black.top,
										14f/20*width+3f/20*width+saveButton.black.height()/2,saveButton.black.bottom);
				
			//черный квадр кнопки
			buttBl[0].black.left=width/8f;
			buttBl[0].black.right=buttBl[0].black.left+2/3f*tx;
			buttBl[0].black.top=tx/6f;
			buttBl[0].black.bottom=5/6f*tx;
			buttBl[0].roundBlack=buttBl[0].black.width()/3;

			buttSn[0].black.left=width-buttBl[0].black.right;
			buttSn[0].black.right=width-buttBl[0].black.left;
			buttSn[0].black.top=buttBl[0].black.top;
			buttSn[0].black.bottom=buttBl[0].black.bottom;
			buttSn[0].roundBlack=buttBl[0].roundBlack;


		}
		else
		{
			// r=height/kwidth;
			stx=(-kwidth*r+width)/2;
			sty=(height-kheight*r)/2;

			workSpace.left=tx;
			workSpace.right=width-tx;
			workSpace.top=0;
			workSpace.bottom=height;

			//черный квадр кнопки
			buttBl[0].black.left=tx/6f;
			buttBl[0].black.right=5/6f*tx;
			buttBl[0].black.top=height/8f;
			buttBl[0].black.bottom=buttBl[0].black.top+2*tx/3f;
			buttBl[0].roundBlack=buttBl[0].black.width()/3;

			buttSn[0].black.left=width-buttBl[0].black.right;
			buttSn[0].black.right=width-buttBl[0].black.left;
			buttSn[0].black.top=buttBl[0].black.top;
			buttSn[0].black.bottom=buttBl[0].black.bottom;
			buttSn[0].roundBlack=buttBl[0].black.width()/3;

			clearButton.setBlackRect(tx/6,height-tx*5f/8,5f/6*tx,height-tx*3f/8);
			saveButton.setBlackRect(width-5f/6*tx,height-tx*5f/8,width-tx/6f,height-tx*3f/8);

		}

		pdelr=r/32;
		pdel=r/16;
		normWidthBl=buttSn[0].black.width();


		//		delta=block.width()/10;		

		//белый квадр кнопки
		buttBl[0].white.left=buttBl[0].black.left+normWidthBl/16;
		buttBl[0].white.right=buttBl[0].black.right-normWidthBl/16;
		buttBl[0].white.top=buttBl[0].black.top+normWidthBl/16;
		buttBl[0].white.bottom=buttBl[0].black.bottom-normWidthBl/16;
		buttBl[0].roundWhite=buttBl[0].white.width()/3;

		buttSn[0].white.left=buttSn[0].black.left+normWidthBl/16;
		buttSn[0].white.right=buttSn[0].black.right-normWidthBl/16;
		buttSn[0].white.top=buttSn[0].black.top+normWidthBl/16;
		buttSn[0].white.bottom=buttSn[0].black.bottom-normWidthBl/16;
		buttSn[0].roundWhite=buttSn[0].white.width()/3;


		normWidthWhiteBl=buttSn[0].white.width();

		//цветной квадрат кнопки

		buttBl[0].color.left=buttBl[0].white.left+normWidthBl/16;
		buttBl[0].color.right=buttBl[0].white.right-normWidthBl/16;
		buttBl[0].color.top=buttBl[0].white.top+normWidthBl/16;
		buttBl[0].color.bottom=buttBl[0].white.bottom-normWidthBl/16;
		buttBl[0].roundColor=buttBl[0].color.width()/3;

		buttSn[0].color.left=buttSn[0].white.left+normWidthBl/16;
		buttSn[0].color.right=buttSn[0].white.right-normWidthBl/16;
		buttSn[0].color.top=buttSn[0].white.top+normWidthBl/16;
		buttSn[0].color.bottom=buttSn[0].white.bottom-normWidthBl/16;
		buttSn[0].roundColor=buttSn[0].color.width()/3;

		normWidthColorBl=buttSn[0].color.width();
		//мутим все для кнопок

		for(int i=1;i<kStages;i++)
		{
			//черный блок
			buttBl[i].black.left=buttBl[0].black.left+i*normWidthBl/2/kStages;
			buttBl[i].black.right=buttBl[0].black.right-i*normWidthBl/2/kStages;
			buttBl[i].black.top=buttBl[0].black.top+i*normWidthBl/2/kStages;
			buttBl[i].black.bottom=buttBl[0].black.bottom-i*normWidthBl/2/kStages;
			buttBl[i].roundBlack=buttBl[i].black.width()/3;

			buttBl[i+kStages-1].black.set(buttBl[0].black);
			buttBl[i+kStages-1].roundBlack=buttBl[0].black.width()/3;

			buttSn[i].black.left=buttSn[0].black.left+i*normWidthBl/2/kStages;
			buttSn[i].black.right=buttSn[0].black.right-i*normWidthBl/2/kStages;
			buttSn[i].black.top=buttSn[0].black.top+i*normWidthBl/2/kStages;
			buttSn[i].black.bottom=buttSn[0].black.bottom-i*normWidthBl/2/kStages;
			buttSn[i].roundBlack=buttSn[i].black.width()/3;

			buttSn[i+kStages-1].black.set(buttSn[0].black);
			buttSn[i+kStages-1].roundBlack=buttSn[0].black.width()/3;

			//белый блок
			buttBl[i].white.left=buttBl[0].white.left+i*normWidthWhiteBl/2/kStages;
			buttBl[i].white.right=buttBl[0].white.right-i*normWidthWhiteBl/2/kStages;
			buttBl[i].white.top=buttBl[0].white.top+i*normWidthWhiteBl/2/kStages;
			buttBl[i].white.bottom=buttBl[0].white.bottom-i*normWidthWhiteBl/2/kStages;
			buttBl[i].roundWhite=buttBl[i].white.width()/3;

			buttBl[i+kStages-1].white.left=buttBl[0].white.left-(i+1)*(-normWidthWhiteBl+normWidthBl)/2/kStages;
			buttBl[i+kStages-1].white.right=buttBl[0].white.right+(1+i)*(-normWidthWhiteBl+normWidthBl)/2/kStages;
			buttBl[i+kStages-1].white.top=buttBl[0].white.top-(i+1)*(-normWidthWhiteBl+normWidthBl)/2/kStages;
			buttBl[i+kStages-1].white.bottom=buttBl[0].white.bottom+(i+1)*(-normWidthWhiteBl+normWidthBl)/2/kStages;
			buttBl[i+kStages-1].roundWhite=buttBl[i+kStages-1].white.width()/3;

			buttSn[i].white.left=buttSn[0].white.left+i*normWidthWhiteBl/2/kStages;
			buttSn[i].white.right=buttSn[0].white.right-i*normWidthWhiteBl/2/kStages;
			buttSn[i].white.top=buttSn[0].white.top+i*normWidthWhiteBl/2/kStages;
			buttSn[i].white.bottom=buttSn[0].white.bottom-i*normWidthWhiteBl/2/kStages;
			buttSn[i].roundWhite=buttSn[i].white.width()/3;


			buttSn[i+kStages-1].white.left=buttSn[0].white.left-(i+1)*(-normWidthWhiteBl+normWidthBl)/2/kStages;
			buttSn[i+kStages-1].white.right=buttSn[0].white.right+(1+i)*(-normWidthWhiteBl+normWidthBl)/2/kStages;
			buttSn[i+kStages-1].white.top=buttSn[0].white.top-(i+1)*(-normWidthWhiteBl+normWidthBl)/2/kStages;
			buttSn[i+kStages-1].white.bottom=buttSn[0].white.bottom+(i+1)*(-normWidthWhiteBl+normWidthBl)/2/kStages;
			buttSn[i+kStages-1].roundWhite=buttSn[i+kStages-1].white.width()/3;

			//цветной слой кнопки
			buttBl[i].color.left=buttBl[0].color.left+i*normWidthColorBl/2/kStages;
			buttBl[i].color.right=buttBl[0].color.right-i*normWidthColorBl/2/kStages;
			buttBl[i].color.top=buttBl[0].color.top+i*normWidthColorBl/2/kStages;
			buttBl[i].color.bottom=buttBl[0].color.bottom-i*normWidthColorBl/2/kStages;
			buttBl[i].roundColor=buttBl[i].color.width()/3;

			buttBl[i+kStages-1].color.left=buttBl[0].color.left-(i+1)*(-normWidthColorBl+normWidthBl)/2/kStages;
			buttBl[i+kStages-1].color.right=buttBl[0].color.right+(1+i)*(-normWidthColorBl+normWidthBl)/2/kStages;
			buttBl[i+kStages-1].color.top=buttBl[0].color.top-(i+1)*(-normWidthColorBl+normWidthBl)/2/kStages;
			buttBl[i+kStages-1].color.bottom=buttBl[0].color.bottom+(i+1)*(-normWidthColorBl+normWidthBl)/2/kStages;
			buttBl[i+kStages-1].roundColor=buttBl[i+kStages-1].color.width()/3;

			buttSn[i].color.left=buttSn[0].color.left+i*normWidthColorBl/2/kStages;
			buttSn[i].color.right=buttSn[0].color.right-i*normWidthColorBl/2/kStages;
			buttSn[i].color.top=buttSn[0].color.top+i*normWidthColorBl/2/kStages;
			buttSn[i].color.bottom=buttSn[0].color.bottom-i*normWidthColorBl/2/kStages;
			buttSn[i].roundColor=buttSn[i].color.width()/3;


			buttSn[i+kStages-1].color.left=buttSn[0].color.left-(i+1)*(-normWidthColorBl+normWidthBl)/2/kStages;
			buttSn[i+kStages-1].color.right=buttSn[0].color.right+(1+i)*(-normWidthColorBl+normWidthBl)/2/kStages;
			buttSn[i+kStages-1].color.top=buttSn[0].color.top-(i+1)*(-normWidthColorBl+normWidthBl)/2/kStages;
			buttSn[i+kStages-1].color.bottom=buttSn[0].color.bottom+(i+1)*(-normWidthColorBl+normWidthBl)/2/kStages;
			buttSn[i+kStages-1].roundColor=buttSn[i+kStages-1].color.width()/3;

			pathArrow.reset();
			pathArrow.lineTo(-r/2+pdelr+2*pdel+roundColor,-r/2+pdelr+2*pdel);
			pathArrow.lineTo(r/2-pdelr-2*pdel-roundColor,0);
			pathArrow.lineTo(-r/2+pdelr+2*pdel+roundColor,r/2-pdelr-2*pdel);
			pathArrow.close();
		}

		//	pointer.decodeSampledBitmapFromResource((int)(r),r);


		//заполняем Full массив поля
		for(int i=0;i<kwidth*kheight;i++)
		{
			fullField[i].setRectsAndP(i,kwidth,kheight,r,stx,sty);
		}

		roundBlack=fullField[0].black.width()/3;
		roundWhite=fullField[0].white.width()/3;
		roundColor=fullField[0].color.width()/3;

		pointer.recycleBitmap();
		pointer.loadBitmap((int)fullField[0].color.width(),r);

		rectBlTest=rectFToRect(buttBl[0].black);
		rectSnTest=rectFToRect(buttSn[0].black);

		pathArrow.rewind();
		pathArrow.lineTo(-r/2+pdelr+2*pdel+roundColor,-r/2+pdelr+2*pdel);
		pathArrow.lineTo(r/2-pdelr-2*pdel-roundColor,0);
		pathArrow.lineTo(-r/2+pdelr+2*pdel+roundColor,r/2-pdelr-2*pdel);
		pathArrow.close();

		bitmap= Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
		bitmapCanvas=new Canvas(bitmap);

		dist=r;
		arrowSpeed=-r;
		//	arrowShift=dist/kArrowShift;
		lineP.setStrokeWidth(r/16);
		cornerPE=new CornerPathEffect(r/3);
		lineP.setPathEffect(cornerPE);

		if(ksnake>0)
			newPath();
		invalidateBit();
		
	}
	
	void logMemory(String s) 
	{
		Toast.makeText(savedContext,s+ String.format(" memory = %s", 
													 (int) (Runtime.getRuntime().totalMemory() / 1024)),Toast.LENGTH_SHORT).show();
	}
	
	
    @Override
    protected void onDraw(Canvas canvas)
    {
      //  Log.d("redactor1","draw");
		boolean b=false;
		switch(MainActivity.GAME_PHASE) 
		{
			case MENU_REDACTOR:
				menuButton.draw(canvas);
				editorMenuScreen.draw(canvas);
				dTime=0;
			case MAIN_GAME:
			case MAIN_MENU:
				b=true;
		}
		
		if(!b)
		{
			dTime=(System.currentTimeMillis()-prevTime)/1000f;
      	    if (firstdraw)
       		{
		  	    savedCanvas=canvas;
				protimer.start();
		   	    firstdraw=false;
        	}
			canvas.drawBitmap(bitmap,0,0,blackP);
			
			
	//линии 
	 		if(ksnake>0)
				pointer.draw(canvas);
	    	if(ksnake>1)
		    	canvas.drawPath(path,curPathPaint);
			
			
			
    //черный слой кнопок
	
	
	

		
	//	canvas.drawRect(clearButton.full.black,blackButtP);
	    	clearButton.draw(canvas);
			saveButton.draw(canvas);
			menuButton.draw(canvas);
			
    		canvas.drawRoundRect(buttBl[blockStage].black,buttBl[blockStage].roundBlack,buttBl[blockStage].roundBlack,blackP);
			canvas.drawRoundRect(buttSn[stageButt].black,buttSn[stageButt].roundBlack,buttSn[stageButt].roundBlack,blackP);
		//белый слой кнопок
			canvas.drawRoundRect(buttBl[blockStage].white,buttBl[blockStage].roundWhite,buttBl[blockStage].roundWhite,whiteP);
			canvas.drawRoundRect(buttSn[stageButt].white,buttSn[stageButt].roundWhite,buttSn[stageButt].roundWhite,whiteP);
		//цветной слой кнопок
			if(drawGreen)
	    		canvas.drawRoundRect(buttBl[blockStage].color,buttBl[blockStage].roundColor,buttBl[blockStage].roundColor,cleanButtP);
	   		else
				canvas.drawRoundRect(buttBl[blockStage].color,buttBl[blockStage].roundColor,buttBl[blockStage].roundColor,blackButtP);
		
			canvas.drawRoundRect(buttSn[stageButt].color,buttSn[stageButt].roundColor,buttSn[stageButt].roundColor,redButtP);
		
    //	canvas.drawRect(but1,whiteP);
    //	bitmapCanvas.drawRect(but,whiteP);
	//	bitmapCanvas.drawRect(but1,blackP);
  //можно перенести в тач
      	   if(snred)
       	   {
				cursorF.set(cursor);
		  		canvas.drawRoundRect(cursorF,(r-2*pdelr-2*pdel)/4f,(r-2*pdelr-2*pdel)/4f,cursorP);
    	   }
		   
		   
		}
		
	
		
		prevTime=System.currentTimeMillis();
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
			//тут лолжно быть число крат расст между arrows;
			
		//	arrowStage=(arrowStage-1+kArrowShift)%(kArrowShift);
	    if(move)
		{
	    /*	if(arrowStage>0)
		    	arrowStage--;
			else
				arrowStage=kArrowShift-1;*/
			arrowPos +=arrowSpeed*dTime;
			if (arrowPos>=dist)
				arrowPos-=dist;
		//	Toast.makeText(savedContext,Float.toString(arrowPos),Toast.LENGTH_SHORT).show();
			curPathPaint.setPathEffect(new ComposePathEffect(new PathDashPathEffect(pathArrow,r,arrowPos, PathDashPathEffect.Style.ROTATE), cornerPE));
			
	    	invalidate();
		}
	
			if(timer)
			{
		    	nextFrame();
				invalidate(rectSnTest);
				invalidate(rectBlTest);
			}
			
        }

        @Override
        public void onFinish()
        {
            this.start();
        }
    }

    private void nextFrame()
    {
		if(blred)
		{
			if(firstBlRed)
		   	{
				if(blockStage>=kStages)
				{
					blockStage=0;
					
				}
				if(right)
				{
					if(blockStage>=1)
						right=false;
				}
				else
				{
					if(blockStage<=kStages-2)
						right=true;
				}
				firstBlRed=false;
			}
		
			if(right)
			{
				blockStage++;
				stageButt=blockStage+kStages-1;
			    
				if(blockStage==kStages-1)
				{
					right=false;
					drawGreen=cleanMode;
				}
			}
			else
			{
				blockStage--;
				stageButt=blockStage+kStages-1;
			    
				if(blockStage==0)
				{
					timer=false;
					stageButt=0;
					blred=false;
				}
			}
		}
		
	    if(snred)
	    {
		   	if(firstSnRed)
		   	{
			   	blockStage=kStages-1;
				stageButt=0;
			    firstSnRed=false;
				blred=false;
		   	} 
		    blockStage++;
		   	stageButt++;
		   	if(blockStage==buttSn.length-1)
			   	timer=false;
    	}
	   	else
	   	{
			if(!blred)
			{
				if(stageButt>1)
				{
		        	blockStage--;
		         	stageButt--;
				}
		    	else
		    	{
			    	blockStage=0;
					stageButt=0;
		    		timer=false;
		    	}
			}
			
		}
    }

	class SaveFromMenuTask extends AsyncTask<Object,Void,Void>
	{
		boolean b;
		Intent intent;
		Bitmap bit;
		@Override
		protected Void doInBackground(Object[] p1)
		{
			intent =(Intent)p1[3];
			b=FileHelper.saveLevel((Bitmap)p1[0],(String)p1[1],(String)p1[2]);
			bit=(Bitmap)p1[0];
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			if(b)
				Toast.makeText(savedContext,"Saved successfully",Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(savedContext,"Something went wrong",Toast.LENGTH_SHORT).show();
			Vibrator v = (Vibrator) savedContext.getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(100);
			
			
		//	MainActivity.appMode=MainActivity.AppMode.LOADGAME;
		 //   editorMenuScreen.reverseSlideAnim.cancel();
		//	MainActivity.GAME_PHASE=MainActivity.Phase.MAIN_GAME;
			//savedContext.startActivity(new Intent(savedContext,ProDrawActivity.class));
			savedContext.startActivity(intent);
			bit.recycle();
		//	RedactorActivity.activity.finish();
			
			
			super.onPostExecute(result);
		}
	}
	
	
	private Intent createSavingIntent()
	{
		Intent intent = new Intent(savedContext,ProDrawActivity.class);

		int[] snakeArr=new int[ksnake];
		int[] blocksArr=new int[kblocks];
		
		int i=0;
		snakeIt=snake.iterator();
		while(snakeIt.hasNext())
		{
			snakeArr[i]=snakeIt.next();
			i++;
		}
		
		for(int j=0;j<kblocks;j++)
		{
			blocksArr[j]=blocks.get(j);
		}
		
		
	//	intent.putExtra("isEmpty",false);
		LevelInfo.kwidth=kwidth;
		LevelInfo.kheight=kheight;
		LevelInfo.snake=snakeArr;
		LevelInfo.blocks=blocksArr;
		LevelInfo.dir=pointer.dir;
		
		return intent;
	}
	
	void showSaveDialogFromMenu()
	{
		LayoutInflater inflater=(LayoutInflater) savedContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View saveDialogLayout=inflater.inflate(R.layout.save_dialog,null);
		final EditText saveNameField=(EditText) saveDialogLayout.findViewById(R.id.levelName);
		final DialogHelper saveDialog=new DialogHelper(savedContext)
		{
			boolean positiveButtonClicked=false;
			@Override
            protected void onDialogDismissed()
			{
			//	super.onDialogDismissed();
				dialogIsClosed=true;
				if(!positiveButtonClicked)
					editorMenuScreen.buttons[0].unpress();
			}
			@Override
            protected void onNegativeButtonClick()
			{

			}

			@Override
            protected void onPositiveButtonClick()
			{

				positiveButtonClicked=true;
				//
				final String fileName=saveNameField.getText().toString();     
				if(!fileName.isEmpty())
				{
					//preparing data file

			/*	Intent intent =	createSavingIntent();     

				 //preparing data file
				 String fileString=prepareDataFile();

				 //preparingBitmap
				 Bitmap bit=prepareBitmap();

				 SaveFromMenuTask mt=new SaveFromMenuTask();
				 mt.execute(bit,fileString,fileName,intent);

				 editorMenuScreen.needSecondScreen=true;
				 editorMenuScreen.slideOut();*/
				 
				 
				if(FileHelper.doesNameExist(fileName))
					{
						DialogHelper dh=new DialogHelper(savedContext)
						{
							@Override
							protected void onDialogDismissed()
							{

							}
							@Override
							protected void onPositiveButtonClick()
							{
								

								//preparingBitmap
								
								 Intent intent =	createSavingIntent();     

								 //preparing data file
								 String fileString=prepareDataFile();

								 //preparingBitmap
								 Bitmap bit=prepareBitmap();
							
								 SaveFromMenuTask mt=new SaveFromMenuTask();
								 mt.execute(bit,fileString,fileName,intent);

								 editorMenuScreen.needSecondScreen=true;
								 editorMenuScreen.slideOut();
								
							}

							@Override
							protected void onNeutralButtonClick()
							{
								showSaveDialogFromMenu();
							}
						};
						dh.showOverwriteDialog();
					}
					else
					{
						Intent intent =	createSavingIntent();     

						//preparing data file
						String fileString=prepareDataFile();

						//preparingBitmap
						Bitmap bit=prepareBitmap();

						SaveFromMenuTask mt=new SaveFromMenuTask();
						mt.execute(bit,fileString,fileName,intent);

						editorMenuScreen.needSecondScreen=true;
						editorMenuScreen.slideOut();
						
					}
				}
				else
				{
					DialogHelper emptyFieldDialog=new DialogHelper(savedContext)
					{
						@Override
                        protected void onDialogDismissed()
						{
						//	super.onDialogDismissed();
							dialogIsClosed=true;
							showSaveDialogFromMenu();
						}
						@Override
                        protected void onPositiveButtonClick()
						{
						
						}
					};
					emptyFieldDialog.showOneButtonDialog("Warning!","Name field cannot be empty!","Ok",true);
				}
	
			}
		};
		saveDialog.showDialog("Save Dialog","Choose the name","Save","Cancel",true,saveDialogLayout);
		
	}

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float X=event.getX(), Y=event.getY();
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
				x=X;
                y=Y;
				switch(MainActivity.GAME_PHASE)
				{
				   case REDACTOR:
				   blocksEdit=false;
				   if(menuButton.isPressed(x,y))
				   {
					   menuButton.onPress();
						snred=false;
						snredh=false;
						snredt=false;
						move=true;
						
						dialogIsClosed=false;
			
					   //drawPictureOnCanvasWithoutCircles(menuScreen.startCanvas);

					 //  foodP.set(cellField[food].snakeP);
					  // foodP.setColor(Color.argb(150,0,255,0));
					  // editorMenuScreen.startCanvas.drawRoundRect(cellField[food].black,roundBlack*3/2,roundBlack*3/2,cellField[food].bigP);
					  // editorMenuScreen.startCanvas.drawRoundRect(cellField[food].white,roundWhite*3/2,roundWhite*3/2,whiteP);
					   //editorMenuScreen.startCanvas.drawRoundRect(cellField[food].color,roundColor*3/2,roundColor*3/2,foodP);
					  // timer=false;
					   editorMenuScreen.startCanvas.drawBitmap(bitmap,0,0,blackP);
					   if(ksnake>1)
					   //	canvas.drawPath(path,arrowP[arrowStage]);
						   editorMenuScreen.startCanvas.drawPath(path,curPathPaint);
					   //черный слой кнопок





					   //	canvas.drawRect(clearButton.full.black,blackButtP);
					   clearButton.draw(editorMenuScreen.startCanvas);
					   saveButton.draw(editorMenuScreen.startCanvas);
					   menuButton.draw(editorMenuScreen.startCanvas);

					   editorMenuScreen.startCanvas.drawRoundRect(buttBl[blockStage].black,buttBl[blockStage].roundBlack,buttBl[blockStage].roundBlack,blackP);
					   editorMenuScreen.startCanvas.drawRoundRect(buttSn[stageButt].black,buttSn[stageButt].roundBlack,buttSn[stageButt].roundBlack,blackP);
					   //белый слой кнопок
					   editorMenuScreen.startCanvas.drawRoundRect(buttBl[blockStage].white,buttBl[blockStage].roundWhite,buttBl[blockStage].roundWhite,whiteP);
					   editorMenuScreen.startCanvas.drawRoundRect(buttSn[stageButt].white,buttSn[stageButt].roundWhite,buttSn[stageButt].roundWhite,whiteP);
					   //цветной слой кнопок
					   if(drawGreen)
						   editorMenuScreen.startCanvas.drawRoundRect(buttBl[blockStage].color,buttBl[blockStage].roundColor,buttBl[blockStage].roundColor,cleanButtP);
					   else
						   editorMenuScreen.startCanvas.drawRoundRect(buttBl[blockStage].color,buttBl[blockStage].roundColor,buttBl[blockStage].roundColor,blackButtP);

					   editorMenuScreen.startCanvas.drawRoundRect(buttSn[stageButt].color,buttSn[stageButt].roundColor,buttSn[stageButt].roundColor,redButtP);
					   
					   
					 //  deltaT=0;
					   editorMenuScreen.slideIn();
					   invalidate(menuButton.inv);
					   
				   }
				   else
              	   if(buttBl[0].black.contains(x,y))
				   {
                      blred=true;
				      firstBlRed=true;
				   	  cleanMode=!cleanMode;
				  	  timer=true;
					  
			   	   }
				   else
				   {
				   	 if(buttSn[0].black.contains(x,y))
				 	 {
						snred=true;
						cursor.left=(int)(x-r/2f+pdelr+pdel);
						cursor.right=(int)(x+r/2f-pdelr-pdel);
						cursor.top=(int)(y-r/2f+pdelr+pdel);
						cursor.bottom=(int)(y+r/2f-pdelr-pdel);
						invalidate(cursor);
						firstSnRed=true;
						timer=true;
						
					 }
					 else
			    	 {
			        	if(clearButton.black.contains(x,y))
						{
							
						    clearButton.onPress();
							invalidate(clearButton.inv);
							dialogIsClosed=false;
                            clear();
							
							
						}
			         	else
						if(saveButton.black.contains(x,y))
						{
							/*if(save())
								Toast.makeText(savedContext,"Saved",Toast.LENGTH_SHORT).show();
							else
								Toast.makeText(savedContext,"Isn't saved",Toast.LENGTH_SHORT).show();
							*/
							saveButton.onPress();
							invalidate(saveButton.inv);
							dialogIsClosed=false;
							
							save();
							
						}
						else
			        	{
						 	if((x<stx+kwidth*r)&&(x>stx)&&(y>sty)&&(y<sty+kheight*r))
							{
								blocksEdit=true;
								int Dx=(((int)x-stx)/r)+(((int)y-sty)/r)*kwidth;
								blDown=Dx;
								if(cleanMode)
								{
									yp=Y;
									xp=X;
									if(field[Dx])
										if(blocks.contains(Dx))
										{
											deleteblock(Dx);
											invalidateBit(areaRect(Dx));
											invalidate(areaRect(Dx));
										}
									
								}	
								else
								{
									newblock(Dx);
								    xp=X;
									yp=Y;
								}
								
									if(ksnake>0)
									{
							    		if(snake.get(0)==Dx)
							    		{
											if(ksnake==1)
									        	snredh=true;
											else
												snredt=true;
											move=false;
											xp=X;
											yp=Y;
											blocksEdit=false;
								    	}
										else
										{
											if(lastSnake==Dx)
											{
												snredh=true;
												move=false;
												xp=X;
												yp=Y;
												blocksEdit=false;
											}
										}
									}
								
								
							}
			        	}
                    }
                }
				if(snred)
                {
                    x=X+r/2;
                    y=Y-r/2;
                  //  invalidate(cursor);
                }
				break;
				case MENU_REDACTOR:
						if(editorMenuScreen.buttonsAreActive)
							for (int i=0;i<editorMenuScreen.buttons.length;i++)
							{
								if (editorMenuScreen.buttons[i].isPressed(X,Y) )
								{
									editorMenuScreen.buttons[i].onPress();
									invalidate(editorMenuScreen.buttons[i].inv);
									switch(i)
									{
										case 0:
											//	startScreen.needSecondScreen=true;
											if(ksnake>0)
											{
											final DialogHelper dm=new DialogHelper(savedContext)
											{
												boolean positiveButtonClicked=false;
												@Override
                                                protected void onDialogDismissed()
												{
												//	super.onDialogDismissed();
													dialogIsClosed=false;
													if(positiveButtonClicked==false)
												    	editorMenuScreen.buttons[0].unpress();
												}
														
												@Override
                                                protected void onNegativeButtonClick()
												{
												//	editorMenuScreen.buttons[0].unpress();
												}
												
												@Override
                                                protected void onNeutralButtonClick()
												{
												//	Toast.makeText(savedContext,"neu",Toast.LENGTH_SHORT).show();
													editorMenuScreen.needSecondScreen=true;
													editorMenuScreen.slideOut();
													Intent intent =	createSavingIntent();
													savedContext.startActivity(intent);
												//	RedactorActivity.activity.finish();
												}

												@Override
                                                protected void onPositiveButtonClick()
												{
													positiveButtonClicked=true;
												//	editorMenuScreen.needSecondScreen=true;
												//	editorMenuScreen.slideOut();
												    showSaveDialogFromMenu();
												}
											};
											
											dm.showDialogWithNeutral("Message", "Do you want to save level or play without saving?" ,"Save","Don't save","Cancel",true);
											}
											else
											{
												DialogHelper dh=new DialogHelper(savedContext)
												{

													@Override
                                                    protected void onDialogDismissed()
													{
													//	super.onDialogDismissed();
															dialogIsClosed=true;
															editorMenuScreen.buttons[0].unpress();
													}
													@Override
                                                    protected void onPositiveButtonClick()
													{
														// TODO: Implement this method
													}
													
												};
												dh.showOneButtonDialog("Warning!","You forgot to create a snake!","It's my blame:(",true);
											}
											
											break;
										case 1:
											DialogHelper dl=new DialogHelper(savedContext)
											{
												boolean positiveButtonClicked=false;
												@Override
                                                protected void onDialogDismissed()
												{
										//		super.onDialogDismissed();
													dialogIsClosed=true;
												//	Toast.makeText(savedContext,"diss",Toast.LENGTH_SHORT).show();
													if(!positiveButtonClicked)
														editorMenuScreen.buttons[1].unpress();
												}
												
												@Override
                                                protected void onPositiveButtonClick()
												{
													positiveButtonClicked=true;
													editorMenuScreen.endCanvas.drawColor(Color.BLACK);
													editorMenuScreen.needSecondScreen=true;
													editorMenuScreen.slideOut();
												}
											};

											dl.showDialog(null, "Are you sure?", "Yes","No",true);
											break;
										case 2:
											//		menuScreen.slideOut();
											editorMenuScreen.slideOut();
										break;
										case 3:
											editorMenuScreen.slideOut();
									}

								}
							}
					break;
				}
                break;
            case MotionEvent.ACTION_MOVE:
				
			if(pointer.rotating)
			{
				float dX=(X-(stx+(lastSnake%kwidth)*r+r/2f));
				float dY=(sty+(lastSnake/kwidth)*r+r/2f-Y);
				
				int k;
				if(dX>=0)
					k=0;
				else
					k=180;
					
				
				pointer.ang=(k-(float)(Math.atan(dY/dX)/(Math.PI*2)*360)+360)%360;
			}
			else
                if(snred)
                {
                    x=X+r/2;
                    y=Y-r/2;
					invalidate(cursor);
					cursor.left=(int)(x-r/2f+pdelr+pdel);
					cursor.right=(int)(x+r/2f-pdelr-pdel);
					cursor.top=(int)(y-r/2f+pdelr+pdel);
					cursor.bottom=(int)(y+r/2f-pdelr-pdel);
					
					invalidate(cursor);
             
                }
                else
                {
				
                    if(snredt)
                    {
						if(Math.abs( (findYr(Y)-findYr(yp) ))+Math.abs( (findXr(X)-findXr(xp) ))<=1)
						{
					    	if((X<stx+kwidth*r)&&(X>stx)&&(Y>sty)&&(Y<sty+kheight*r))
					    	{
						    	int Bx=(((int)X-stx)/r)+(((int)Y-sty)/r)*kwidth;
                                if(!field[Bx])
                                {
                                    addsnaket(Bx);
								//head=true;
							//	snredt=lineHead(xp,yp,X,Y,false);
                                }
						    	else
						    	{
								//голова змейки в конце списка
							    	if(Bx!=snake.get(0))
							    	{
								    	if (ksnake>=2) 
									    	if (Bx!=snake.get(1))
											{
							                    snredt=false;
												prevTime=System.currentTimeMillis();
												move=true;
											}
									   	    else
									    	{
										   	    invalidate(areaRect(snake.get(0)));
											    deleteSnakeIndex(0);
									        }
						        	}
						        }
						    }
					    	else
							{
					    	    snredt=false;
								prevTime=System.currentTimeMillis();
								move=true;
							}
						}
						else
						{
							snredt=lineHead(xp,yp,X,Y,false);
							if( !snredt)
								prevTime=System.currentTimeMillis();
							move = !snredt;
						}
                    }
					else
					{
						if(snredh)
						{
							if(Math.abs( (findYr(Y)-findYr(yp) ))+Math.abs( (findXr(X)-findXr(xp) ))<=1)
							{
						    	if((X<stx+kwidth*r)&&(X>stx)&&(Y>sty)&&(Y<sty+kheight*r))
						    	{
								    int Bx=(((int)X-stx)/r)+(((int)Y-sty)/r)*kwidth;
							    	if(!field[Bx])
							    	{
								        addsnake(Bx);
									    tail=true;
							    	}
							    	else
							    	{
									    //голова змейки вконце списка
								    	if(Bx!=lastSnake)
										{
											if(ksnake>=2)
											{
												if(Bx!=snake.get(ksnake-2))
												{
									            	snredh=false;
													prevTime=System.currentTimeMillis();
													move=true;
												}
												else
												{	
												    invalidate(areaRect(lastSnake));
										            deleteSnakeIndex(ksnake-1);
												}
											}
											else
											{
												prevTime=System.currentTimeMillis();
												move=true;
												snredh=false;
											}
										}
							    	}
							    }
						    	else
								{
							    	snredh=false;
									prevTime=System.currentTimeMillis();
									move=true;
								}
						   	}
							else
							{
								snredh=lineHead(xp,yp,X,Y,true);
								if(!snredh)
									prevTime=System.currentTimeMillis();
								move=!snredh;
							}
						}
						else
						{
							if((X<stx+kwidth*r)&&(X>stx)&&(Y>sty)&&(Y<sty+kheight*r))
						   	{
								if (dialogIsClosed&&blocksEdit)
								{
						    		if(cleanMode)
						    		{
								 	   lineClear(xp,yp,X,Y);
						     		}
					    			else
						    		{
							    		lineBlock(xp,yp,X,Y);
						    		}
								}
					    	}
						}
					}
				}
				xp=X;
				yp=Y;
                break;
            case MotionEvent.ACTION_UP:
						if(snred)
						{
							if((x<stx+kwidth*r)&&(x>stx)&&(y>sty)&&(y<sty+kheight*r))
							{
                                int Bx = (((int) x - stx) / r) + (((int) y - sty) / r) * kwidth;
                                newsnake(Bx);
							}
							timer=true;
						}
            case MotionEvent.ACTION_CANCEL:
            //    blred=false;
			//	blredf=false;
			//	Toast.makeText(savedContext,"577",Toast.LENGTH_LONG).show();
			    pointer.rotating=false;
				if(pointer.ang<135||pointer.ang>315)
				{
					if(pointer.ang>45&&pointer.ang<315)
					    pointer.dir=Direction.DOWN;
					else
						pointer.dir=Direction.RIGHT;
				}
				else
				{
					if(pointer.ang>225)
					    pointer.dir=Direction.UP;
				    else
						pointer.dir=Direction.LEFT;
				}
				
							
						
					
			    snred=false;
				snredh=false;
				snredt=false;
				move=true;
			
				saveButton.unpress();
				clearButton.unpress();
				prevTime=System.currentTimeMillis();
			//	blRedNew=false;
                break;
			
        }
		gd.onTouchEvent(event);
        return true;
    }
	class MyGestureListener extends GestureDetector.SimpleOnGestureListener
	{
		@Override
		public void onLongPress(MotionEvent e)
		{
		//	super.onLongPress(e);
			if(ksnake>0)
			{
				float X=e.getX();
				float Y=e.getY();
			
				if(stx+(lastSnake%kwidth)*r<=X&&stx+(lastSnake%kwidth+1)*r>=X&&sty+(lastSnake/kwidth)*r<=Y&&sty+(lastSnake/kwidth+1)*r>=Y)
				{
					pointer.rotating=true;
					snred=false;
					snredh=false;
					snredt=false;
					move=true;
					
					blocksEdit=false;
					
				//	Toast.makeText(savedContext,"onLong",Toast.LENGTH_SHORT).show();
					Vibrator v = (Vibrator) savedContext.getSystemService(Context.VIBRATOR_SERVICE);
					v.vibrate(100);
				}
				prevTime=System.currentTimeMillis();
			}
			
		}
	}
	
}

