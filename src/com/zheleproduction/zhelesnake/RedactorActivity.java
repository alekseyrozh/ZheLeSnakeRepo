//package ru.ZheLeProduction.ZheLeSnake;
package com.zheleproduction.zhelesnake;
import android.app.*;
import android.content.pm.*;
import android.os.*;
import android.view.*;
import android.widget.*;


public class RedactorActivity extends Activity
{
/*	public static 	int kwidth;
	public static 	int kheight;
	public static 	int[] snake;
	public static 	int[] blocks;
	public static 	Direction dir;
	*/
	public static Redactor red;
	public static Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
							 WindowManager.LayoutParams.FLAG_FULLSCREEN);

	//	Intent intent=getIntent();
		//	boolean isEmpty=intent.getBooleanExtra("isEmpty",true);
	    
/*		kwidth=(int)intent.getIntExtra("kwidth",5);
		kheight=(int)intent.getIntExtra("kheight",5);
    	snake =intent.getIntArrayExtra("snake");
		
       // snake=new int[2];
       // snake[0]=1;
       // snake[1]=2;

		if(snake==null)
		{
			snake=new int[0];
		}
		blocks=intent.getIntArrayExtra("blocks");
		if(blocks==null)
			blocks=new int[0];
		dir=Direction.intToDirection((int)intent.getIntExtra("direction",2));
		*/
							 
	    red=new Redactor(this);
        setContentView(red);
		//setContentView(R.layout.redactor);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

		
		activity=this;
		
	//	Toast.makeText(this,"RedactorActivity is created",Toast.LENGTH_SHORT).show();
    }

/*	@Override
	protected void onStop()
	{
		super.onStop();
		finish();
	}*/

	

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
			Toast.makeText(this,"restarted",Toast.LENGTH_SHORT).show();
		

		red.onRestart();
		super.onRestart();
	}

	@Override
	protected void onStop()
	{
		red.onStop();
		super.onStop();
	}


	
	public void onBackPressed()
	{
	//	startActivity(new Intent(this,MainActivity.class));
	//	finish();
	/*	switch(MainActivity.GAME_PHASE)
		{
			
		}*/
	}
}
