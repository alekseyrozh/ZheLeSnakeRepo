//package ru.ZheLeProduction.ZheLeSnake;
package com.zheleproduction.zhelesnake;
 

import android.annotation.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.view.ViewTreeObserver.*;
import android.widget.*;
import com.zheleproduction.zhelesnake.util.*;
import android.widget.RadioGroup.*;
import android.animation.*;


public class MainActivity extends Activity
{
 //   public static boolean reda=false;
  //  boolean gme = false;
    public static Phase GAME_PHASE=Phase.MAIN_MENU;
//	TextView tv;
	ProgressDialog pd;
	GridView gridview;
	String fileName;
//	Handler h;
	Context context;
	DialogHelper oneLevelDialog;
	int selectedItemIndex;
	ImageTextAdapter adapter;
	
	enum Phase 
	{
		START_SCREEN,PLAY_SCREEN, LEVEL_CHOICER, MAIN_GAME, GAME_OVER_SCREEN, MENU_SCREEN, MAIN_MENU, REDACTOR, MENU_REDACTOR;
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
							 WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	FileHelper.prepareAll(this);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
		
		
		
	//	Intent intent=getIntent
		setContentView(R.layout.main);
		
		context=this;
		selectedItemIndex=-1;
		logMemory("1:");
	}

	@Override
	protected void onRestart()
	{
		if(LevelInfo.needLevelChoice)
			prepareLevelMenu();
		else
			setContentView(R.layout.main);
		super.onRestart();
		
		selectedItemIndex=-1;
	}
	
	public void toMenu(View v)
	{
		GAME_PHASE=Phase.MAIN_MENU;
		setContentView(R.layout.main);
	}

    @Override
	public void onBackPressed()
	{
	/*	switch(GAME_PHASE)
		{
			case PLAY_SCREEN:
				GAME_PHASE=Phase.MAIN_MENU;
				setContentView(R.layout.main);
			break;
			case START_SCREEN:
			    finish();
			break;
			case LEVEL_CHOICER:
				setContentView(R.layout.start);
				GAME_PHASE=Phase.PLAY_SCREEN;
			break;
		}
		*/
	}
	void logMemory(String s) 
	{
	//	Toast.makeText(this,s+ String.format("Total memory = %s", 
			//										 (int) (Runtime.getRuntime().totalMemory() / 1024)),Toast.LENGTH_SHORT).show();
	}

	public void oneLevelDialogButtonClick(View view)
	{
		switch(view.getId())
		{
			case R.id.PlayButton:
				Intent intent=new Intent(context,ProDrawActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

				Bundle bun=FileHelper.readLevelData(fileName);

				LevelInfo.kwidth=bun.getInt("kwidth");
				LevelInfo.kheight=bun.getInt("kheight");
				LevelInfo.snake=bun.getIntArray("snake");
				LevelInfo.blocks=bun.getIntArray("blocks");
				LevelInfo.dir=Direction.intToDirection(bun.getInt("direction"));

				LevelInfo.currentLevelName=FileHelper.getLevelNames()[selectedItemIndex];
				LevelInfo.arcadeMode=false;
				
				startActivity(intent);
				oneLevelDialog.dismiss();
			break;
			case R.id.EditButton:

				Intent intent1=new Intent(context,RedactorActivity.class);
				Bundle bun1=FileHelper.readLevelData(fileName);

				LevelInfo.kwidth=bun1.getInt("kwidth");
				LevelInfo.kheight=bun1.getInt("kheight");
			//	Toast.makeText(context,"kheightInFile;"+LevelInfo.kheight,Toast.LENGTH_SHORT).show();

				LevelInfo.snake=bun1.getIntArray("snake");
				LevelInfo.blocks=bun1.getIntArray("blocks");
				LevelInfo.dir=Direction.intToDirection(bun1.getInt("direction"));

				intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			
				startActivity(intent1);
				oneLevelDialog.dismiss();
			break;
			case R.id.DeleteButton:
				FileHelper.deleteLevel(fileName);
		//		prepareLevelMenu();
			  //  if(selectedItemIndex>=0)
			//	{
				//	gridview.removeViewAt(1);         
				//	adapter.notifyDataSetChanged();
				//	gridview.invalidateViews();
				
				//gridview.setAdapter(adapter);
				oneLevelDialog.dismiss();
				prepareLevelMenu();
					
			//	}
			break;
			case R.id.RenameButton:
				showRenameDialog(fileName);
				
			//	FileHelper.deleteLevel(fileName);
				//		prepareLevelMenu();
			   // if(selectedItemIndex>=0)
			//	{
					//	gridview.removeViewAt(1);         
					//	adapter.notifyDataSetChanged();
					//	gridview.invalidateViews();

					//gridview.setAdapter(adapter)
			//	}
			break;
		}
	}
	
	private void showRenameDialog(final String oldName)
	{
		LayoutInflater inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     	View saveDialogLayout=inflater.inflate(R.layout.save_dialog,null);
		final EditText saveNameField=(EditText) saveDialogLayout.findViewById(R.id.levelName);
		
		saveNameField.setText(oldName);
	//	final String oldName2=oldName;
		
	    DialogHelper saveDialog=new DialogHelper(this)
		{

			@Override
            protected void onDialogDismissed()
			{
				
			}

			@Override
            protected void onNegativeButtonClick()
			{

			}

			@Override
            protected void onPositiveButtonClick()
			{
				
				final String fileNameEdit=saveNameField.getText().toString();     
				if(!fileNameEdit.isEmpty())
				{
					if(fileNameEdit.equals(oldName))
					{
						DialogHelper dh=new DialogHelper(context);
						dh.showOneButtonDialog("Information","You didn't change the name.","I know",true);
					}
					else
					if(FileHelper.doesNameExist(fileNameEdit))
					{
						DialogHelper dh=new DialogHelper(context)
						{
							@Override
							protected void onDialogDismissed()
							{
								
							}
							@Override
							protected void onPositiveButtonClick()
							{
								FileHelper.rename(oldName,fileNameEdit);
								prepareLevelMenu();
								oneLevelDialog.dismiss();
							}
							
							@Override
							protected void onNeutralButtonClick()
							{
								showRenameDialog(oldName);
							}
						};
						dh.showOverwriteDialog();
					}
					else
					{
			
						FileHelper.rename(oldName,fileNameEdit);
						prepareLevelMenu();
						oneLevelDialog.dismiss();
			
					}
				}
				else
				{
					DialogHelper emptyFieldDialog=new DialogHelper(context)
					{
						@Override
                        protected void onDialogDismissed()
						{
					//		super.onDialogDismissed();
							showRenameDialog(oldName);
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
		saveDialog.showDialog("Rename Dialog","Choose the name","Rename","Cancel",true,saveDialogLayout);
	}
	
    public void onPlayButtonClick(View view)
	{
		GAME_PHASE=Phase.PLAY_SCREEN;
		setContentView(R.layout.start);
    }
//зашли в play
	public void onArcadeButtonClick(View view)
	{
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View sDialogLayout=inflater.inflate(R.layout.number_pick_dialog,null);

		final NumberPicker leftNumPick=(NumberPicker)sDialogLayout.findViewById(R.id.leftNumPick);
		final NumberPicker rightNumPick=(NumberPicker)sDialogLayout.findViewById(R.id.rightNumPick);
		final CheckBox box=(CheckBox) sDialogLayout.findViewById(R.id.box);

		final TextView widthText=(TextView) sDialogLayout.findViewById(R.id.width);
		final TextView heightText=(TextView) sDialogLayout.findViewById(R.id.height);


		final LinearLayout leftLay=(LinearLayout) sDialogLayout.findViewById(R.id.leftLay);
		final LinearLayout rightLay=(LinearLayout) sDialogLayout.findViewById(R.id.rightLay);
		box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
			{

				@Override
				public void onCheckedChanged(CompoundButton p1, boolean p2)
				{
					// TODO: Implement this method
					if(p2)
					{
						//	ObjectAnimator posAnimator=ObjectAnimator.ofFloat(leftLay, "translationX", leftLay.getWidth()/2f+leftNumPick.getPaddingRight()+leftLay.getPaddingRight());
						//	ObjectAnimator alphaAnimator=ObjectAnimator.ofFloat(rightNumPick, "alpha", 0);
						//	ObjectAnimator alphaTextWAnimator=ObjectAnimator.ofFloat(widthText, "alpha", 0);
						//	ObjectAnimator alphaTextHAnimator=ObjectAnimator.ofFloat(heightText, "alpha", 0);
						//	leftLay.animate().translationX(leftLay.getWidth()/2f+leftNumPick.getPaddingRight()+leftLay.getPaddingRight());
						leftLay.animate().translationXBy((rightLay.getX()-leftLay.getX())/2f);
						rightLay.animate().translationXBy(-(rightLay.getX()-leftLay.getX())/2f);

						rightNumPick.animate().alpha(0);
						heightText.animate().alpha(0);
						widthText.animate().alpha(0);
						rightNumPick.setEnabled(false);

						//	alphaTextWAnimator.start();
						//	alphaTextHAnimator.start();
						//	posAnimator.start();
						//	alphaAnimator.start();
					}
					else
					{
						rightNumPick.setEnabled(true);
						rightNumPick.animate().alpha(1);
						heightText.animate().alpha(1);
						widthText.animate().alpha(1);
						leftLay.animate().translationX(0);
						rightLay.animate().translationX(0);
						/*ObjectAnimator posAnimator=ObjectAnimator.ofFloat(leftLay, "translationX", 0);
						 ObjectAnimator alphaAnimator=ObjectAnimator.ofFloat(rightNumPick, "alpha",1 );
						 ObjectAnimator alphaTextWAnimator=ObjectAnimator.ofFloat(widthText, "alpha", 1);
						 ObjectAnimator alphaTextHAnimator=ObjectAnimator.ofFloat(heightText, "alpha", 1);

						 alphaTextWAnimator.start();
						 alphaTextHAnimator.start();
						 posAnimator.start();
						 alphaAnimator.start();*/
					}
				}
			});
		leftNumPick.setMinValue(3);
		leftNumPick.setMaxValue(25);
		rightNumPick.setMinValue(3);
		rightNumPick.setMaxValue(25);

		DialogHelper pickDialog = new DialogHelper(context)
		{
			@Override
			public void onPositiveButtonClick()
			{
				Intent intent=new Intent(context,ProDrawActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				
				if(!box.isChecked())
			    	LevelInfo.setDefault(leftNumPick.getValue(),rightNumPick.getValue());
				else
					LevelInfo.setDefault(leftNumPick.getValue(),leftNumPick.getValue());

				startActivity(intent);	
			}
		};

		pickDialog.showNumberPickDialog(sDialogLayout);
		
	/*	
		Intent intent=new Intent(this,ProDrawActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		LevelInfo.setDefault();
		startActivity(intent);*/
//	finish();
	}
	
	public void addLevel(View view)
	{
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View sDialogLayout=inflater.inflate(R.layout.number_pick_dialog,null);

		final NumberPicker leftNumPick=(NumberPicker)sDialogLayout.findViewById(R.id.leftNumPick);
		final NumberPicker rightNumPick=(NumberPicker)sDialogLayout.findViewById(R.id.rightNumPick);
		final CheckBox box=(CheckBox) sDialogLayout.findViewById(R.id.box);
		
		final TextView widthText=(TextView) sDialogLayout.findViewById(R.id.width);
		final TextView heightText=(TextView) sDialogLayout.findViewById(R.id.height);

		
		final LinearLayout leftLay=(LinearLayout) sDialogLayout.findViewById(R.id.leftLay);
		final LinearLayout rightLay=(LinearLayout) sDialogLayout.findViewById(R.id.rightLay);
		box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
			{

				@Override
				public void onCheckedChanged(CompoundButton p1, boolean p2)
				{
					// TODO: Implement this method
					if(p2)
					{
					//	ObjectAnimator posAnimator=ObjectAnimator.ofFloat(leftLay, "translationX", leftLay.getWidth()/2f+leftNumPick.getPaddingRight()+leftLay.getPaddingRight());
					//	ObjectAnimator alphaAnimator=ObjectAnimator.ofFloat(rightNumPick, "alpha", 0);
					//	ObjectAnimator alphaTextWAnimator=ObjectAnimator.ofFloat(widthText, "alpha", 0);
					//	ObjectAnimator alphaTextHAnimator=ObjectAnimator.ofFloat(heightText, "alpha", 0);
					//	leftLay.animate().translationX(leftLay.getWidth()/2f+leftNumPick.getPaddingRight()+leftLay.getPaddingRight());
						leftLay.animate().translationXBy((rightLay.getX()-leftLay.getX())/2f);
						rightLay.animate().translationXBy(-(rightLay.getX()-leftLay.getX())/2f);
					
						rightNumPick.animate().alpha(0);
						heightText.animate().alpha(0);
						widthText.animate().alpha(0);
						rightNumPick.setEnabled(false);
						
						//	alphaTextWAnimator.start();
					//	alphaTextHAnimator.start();
					//	posAnimator.start();
					//	alphaAnimator.start();
					}
					else
					{
						rightNumPick.setEnabled(true);
						rightNumPick.animate().alpha(1);
						heightText.animate().alpha(1);
						widthText.animate().alpha(1);
						leftLay.animate().translationX(0);
						rightLay.animate().translationX(0);
						/*ObjectAnimator posAnimator=ObjectAnimator.ofFloat(leftLay, "translationX", 0);
						ObjectAnimator alphaAnimator=ObjectAnimator.ofFloat(rightNumPick, "alpha",1 );
						ObjectAnimator alphaTextWAnimator=ObjectAnimator.ofFloat(widthText, "alpha", 1);
						ObjectAnimator alphaTextHAnimator=ObjectAnimator.ofFloat(heightText, "alpha", 1);

						alphaTextWAnimator.start();
						alphaTextHAnimator.start();
						posAnimator.start();
						alphaAnimator.start();*/
					}
				}
		});
		leftNumPick.setMinValue(3);
		leftNumPick.setMaxValue(25);
		rightNumPick.setMinValue(3);
		rightNumPick.setMaxValue(25);
		
		DialogHelper pickDialog = new DialogHelper(context)
		{
			@Override
			public void onPositiveButtonClick()
			{
				GAME_PHASE=Phase.REDACTOR;
				Intent intent=new Intent(context,RedactorActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				if(!box.isChecked())
			    	LevelInfo.setEmpty(leftNumPick.getValue(),rightNumPick.getValue());
				else
					LevelInfo.setEmpty(leftNumPick.getValue(),leftNumPick.getValue());
				
				startActivity(intent);
			}
		};
		
		pickDialog.showNumberPickDialog(sDialogLayout);
		
		
	/*	GAME_PHASE=Phase.REDACTOR;
		Intent intent=new Intent(this,RedactorActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		LevelInfo.setEmpty();
		startActivity(intent);*/
	//	finish();
	}
	
	public void onCompanyButtonClick(View view)
	{
	//	startActivity(new Intent(this,ProDrawActivity.class));
	}
	
	

	class MyTask extends AsyncTask<Integer, Integer, Void> 
	{
		String[] names;
		Bitmap[] bitmaps;
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			pd.setIndeterminate(false);
		}

		@Override
		protected Void doInBackground(Integer... params) 
		{
			  names=FileHelper.getLevelNames();
		      bitmaps = new Bitmap[names.length];

			for(int i=0;i<names.length;i++)
			{
				bitmaps[i]=FileHelper.LargeImageHelper.getCompressedBitmap(names[i],params[0],params[0]);
				publishProgress(i+1);
			}
			
			return null;
		}
		
		@Override
    	protected void onProgressUpdate(Integer... values)
		{
    	 	super.onProgressUpdate(values);
     	    pd.setProgress(values[0]);
   		}

		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
			adapter=new ImageTextAdapter(getApplicationContext(),bitmaps/*,gridview.getColumnWidth()*/);
			gridview.setAdapter(adapter);
			pd.dismiss();
			
		}
	}
	
	private void prepareLevelMenu()
	{
		GAME_PHASE=Phase.LEVEL_CHOICER;
		setContentView(R.layout.level_menu);

	    pd = new ProgressDialog(this);
		pd.setTitle("Loading levels");
		pd.setMessage("Loading...");
		pd.setCancelable(false);
		// меняем стиль на индикатор
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		// устанавливаем максимум
		pd.setMax(FileHelper.getLevelNames().length);
		// включаем анимацию ожидания
		pd.setIndeterminate(true);
		pd.show();

	    gridview=(GridView) findViewById(R.id.gridview);
		gridview.setOnItemClickListener(gridviewOnItemClickListener);
	    ViewTreeObserver vto=gridview.getViewTreeObserver();
	    vto.addOnGlobalLayoutListener(
			new OnGlobalLayoutListener() 
			{ 
				@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
				public void onGlobalLayout() { 
					gridview.getViewTreeObserver().removeGlobalOnLayoutListener(this); 
				 	if(adapter!=null)
						adapter.recycle();
					MyTask loadBitmaps=new MyTask();
					loadBitmaps.execute(gridview.getColumnWidth());
					//gridview.setAdapter(new ImageTextAdapter(getApplicationContext()/*,gridview.getColumnWidth())*/);
				} 
			});
		logMemory("t:");
	}
	
	public void onMyLevelsButtonClick(View view)
	{
		prepareLevelMenu();
	}
	
	
	
	private GridView.OnItemClickListener gridviewOnItemClickListener= new GridView.OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int pos, long id)
		{
		//	GAME_PHASE=Phase.MAIN_GAME;
		//	startActivity(new Intent(getApplicationContext(),ProDrawActivity.class));
		//	finish();
			selectedItemIndex=pos;
			LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View sDialogLayout=inflater.inflate(R.layout.one_level_dialog,null);
			ImageView image =  (ImageView) sDialogLayout.findViewById(R.id.imagepart);
			TextView name =  (TextView) sDialogLayout.findViewById(R.id.textpart);
			
		//	ImageView gridImage=(ImageView)v.findViewById(R.id.imagepart);
			TextView gridName=(TextView) v.findViewById(R.id.textpart);
			
			fileName=gridName.getText().toString();
	    //	image.setImageDrawable( gridImage.getDrawable());
			name.setText(fileName);
			
			int reqW=gridview.getWidth()*2/3;
			
			image.setImageBitmap(FileHelper.LargeImageHelper.getCompressedBitmap(fileName,reqW,reqW));
		//	image.setImageBitmap(new Bitmap());
			
		/*	View gridImage=(ImageView)v.findViewById(R.id.imagepart);
			
			ImageView imageView=(ImageView) grid.findViewById(R.id.imagepart);
			TextView textView =(TextView) grid.findViewById(R.id.textpart);
			imageView.setImageBitmap(bitmaps[position]);
			*/
			
			
	    	oneLevelDialog=new DialogHelper(context)
			{

                @Override
                protected void onNegativeButtonClick()
                {

                }

                @Override
                protected void onPositiveButtonClick()
				{
				/*	Intent intent=new Intent(context,ProDrawActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					
					
					Bundle bun=FileHelper.readLevelData(fileName);
					
					LevelInfo.kwidth=bun.getInt("kwidth");
					LevelInfo.kheight=bun.getInt("kheight");
					LevelInfo.snake=bun.getIntArray("snake");
					LevelInfo.blocks=bun.getIntArray("blocks");
					LevelInfo.dir=Direction.intToDirection(bun.getInt("direction"));
					*/
				/*	intent.putExtra("kwidth",bun.getInt("kwidth"));		
					intent.putExtra("kheight",bun.getInt("kheight"));
					intent.putExtra("snake",bun.getIntArray("snake"));
					intent.putExtra("blocks",bun.getIntArray("blocks"));
					intent.putExtra("direction",bun.getInt("direction"));
				    Toast.makeText(context,"info: "+Arrays.toString(bun.getIntArray("snake")),Toast.LENGTH_SHORT).show();*/
				//	startActivity(intent);
				}

			/*	@Override
                protected void onNeutralButtonClick()
				{
					Intent intent=new Intent(context,RedactorActivity.class);
					Bundle bun=FileHelper.readLevelData(fileName);
					
				/*	intent.putExtra("kwidth",bun.getInt("kwidth"));		
					intent.putExtra("kheight",bun.getInt("kheight"));
					intent.putExtra("snake",bun.getIntArray("snake"));
					intent.putExtra("blocks",bun.getIntArray("blocks"));
					intent.putExtra("direction",bun.getInt("direction"));
					LevelInfo.kwidth=bun.getInt("kwidth");
					LevelInfo.kheight=bun.getInt("kheight");
					Toast.makeText(context,"kheightInFile;"+LevelInfo.kheight,Toast.LENGTH_SHORT).show();
					
					LevelInfo.snake=bun.getIntArray("snake");
					LevelInfo.blocks=bun.getIntArray("blocks");
					LevelInfo.dir=Direction.intToDirection(bun.getInt("direction"));
					
				    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}*/
				
				@Override
                protected void onDialogDismissed()
				{
					
				}
			};
			oneLevelDialog.showOneButtonDialog("Level options",null,"Cancel",sDialogLayout,true);
		}
	};
	
	
	public void onExitButtonClick(View view)
	{
		if(RedactorActivity.activity!=null)
			RedactorActivity.activity.finish();
		if(ProDrawActivity.activity!=null)
			ProDrawActivity.activity.finish();
		finish();
		System.exit(0);
	}
	
	public void onReturnButtonClick(View view)
	{
		GAME_PHASE=Phase.MAIN_MENU;
		setContentView(R.layout.main);
	}
	
    public void onRedactorButtonClick(View view)
    {
		try
		{
			GAME_PHASE=Phase.REDACTOR;
			Intent intent =new Intent(this,RedactorActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			LevelInfo.setEmpty();
			startActivity(intent);
		}
		catch(Exception e)
		{
			Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
		}
    }

	
}
