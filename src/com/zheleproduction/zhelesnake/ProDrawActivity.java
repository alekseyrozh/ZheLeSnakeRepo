//package ru.ZheLeProduction.ZheLeSnake;
package com.zheleproduction.zhelesnake;


import android.app.*;
import android.content.pm.*;
import android.os.*;
import android.view.*;


public class ProDrawActivity extends Activity
{
	public static Activity activity;

/*	public static 	int kwidth;
	public static 	int kheight;
	public static 	int[] snake;
	public static 	int[] blocks;
	public static 	Direction dir;*/
 
	ProDraw myView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
							 WindowManager.LayoutParams.FLAG_FULLSCREEN);
	//	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
      //  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

/*	    Intent intent=getIntent();
	//	boolean isEmpty=intent.getBooleanExtra("isEmpty",true);
	    kwidth=intent.getIntExtra("kwidth",5);
		kheight=intent.getIntExtra("kheight",5);
		snake =intent.getIntArrayExtra("snake");
		if(snake==null)
		{
			snake=new int[]{2,3};
		}
		blocks=intent.getIntArrayExtra("blocks");
		if(blocks==null)
			blocks=new int[0];
		
		dir=Direction.intToDirection(intent.getIntExtra("direction",2));
*/
     	myView=new ProDraw(this);
	    setContentView(myView);


	//	setContentView(R.layout.prodraw_layout);
		
		activity=this;
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);


		
		
	//	Toast.makeText(this,"proDrawActivity is created",Toast.LENGTH_SHORT).show();
    }

	@Override
	protected void onRestart()
	{
	//	Intent intent=getIntent();
		//	boolean isEmpty=intent.getBooleanExtra("isEmpty",true);
/*	    kwidth=intent.getIntExtra("kwidth",5);
		kheight=intent.getIntExtra("kheight",5);
		snake =intent.getIntArrayExtra("snake");
		if(snake==null)
		{
			snake=new int[]{2,3};
		}
		blocks=intent.getIntArrayExtra("blocks");
		if(blocks==null)
			blocks=new int[0];

		dir=Direction.intToDirection(intent.getIntExtra("direction",2));
		*/
	//	Toast.makeText(this,"info: "+Arrays.toString(snake),Toast.LENGTH_SHORT).show();
		
		
		myView.onRestart();
		super.onRestart();
	}

	@Override
	protected void onStop()
	{
		myView.onStop();
		super.onStop();
	}
	
	
	
	
	
    void logMemory(String s) 
	{
	//	Toast.makeText(this,s+ String.format("Total memory = %s", 
		//						   (int) (Runtime.getRuntime().totalMemory() / 1024)),Toast.LENGTH_LONG).show();
	}
	


	public void onBackPressed()
	{
		//startActivity(new Intent(this,MainActivity.class));
	//	finish();
	/*	switch(MainActivity.GAME_PHASE)
		{
			case MAIN_GAME:
				
		}*/
	}
/*	@Override
	protected void onStop()
	{
		Toast.makeText(this,"stop",Toast.LENGTH_SHORT).show();
		
		super.onStop();
	
	//	finish();
		//close();
	   // destroy();
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		Toast.makeText(this,"destroy",Toast.LENGTH_SHORT).show();
		
		super.onDestroy();
		
	}*/
	
	
	
}
