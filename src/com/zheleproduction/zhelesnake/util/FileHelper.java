
package com.zheleproduction.zhelesnake.util;

import android.content.*;
import android.graphics.*;
import android.os.*;
import android.widget.*;
import com.zheleproduction.zhelesnake.*;
import java.io.*;
import java.util.*;

public abstract class FileHelper
{
	private final static int HIGHSCORES_MAX_COUNT=10;
	
	private final static String DATA_FOLDER_NAME="data";
	private final static String BITMAP_FOLDER_NAME="bitmaps";
	private final static String HIGHSCORE_FOLDER_NAME="highscore";
	
	private static File internalPath;
	private static File dataPath;
	private static File bitmapPath;
	private static File highscorePath;
	private static Context context;
	
	//дописать txt ко всем дата файлам
	public static void prepareAll(Context c)
	{
		context=c;
		internalPath=context.getFilesDir();
	
		createRootInternalFolder(DATA_FOLDER_NAME);
		createRootInternalFolder(BITMAP_FOLDER_NAME);
		createRootInternalFolder(HIGHSCORE_FOLDER_NAME);
		
		dataPath=new File(internalPath.getAbsolutePath()+"/"+DATA_FOLDER_NAME);
		bitmapPath=new File(internalPath.getAbsolutePath()+"/"+BITMAP_FOLDER_NAME);
		highscorePath=new File(internalPath.getAbsolutePath()+"/"+HIGHSCORE_FOLDER_NAME);
	}
	
	public static void rename(String oldName, String newName)
	{
		File bitmapFile = new File(bitmapPath.getAbsolutePath()+"/"+oldName);
		File dataFile = new File(dataPath.getAbsolutePath()+"/"+oldName);
		File highscoreFile = new File(highscorePath.getAbsolutePath()+"/"+oldName);
		
		if(dataFile.exists())
			dataFile.renameTo(new File(dataPath.getAbsolutePath()+"/"+newName));
		if(bitmapFile.exists())
			bitmapFile.renameTo(new File(bitmapPath.getAbsolutePath()+"/"+newName));
		if(highscoreFile.exists())
			bitmapFile.renameTo(new File(highscorePath.getAbsolutePath()+"/"+newName));
	}
	
	public static void deleteLevel(String fileName)
	{
		File bitmapFile = new File(bitmapPath.getAbsolutePath()+"/"+fileName);
		File dataFile = new File(dataPath.getAbsolutePath()+"/"+fileName);
	    File highscoreFile = new File(highscorePath.getAbsolutePath()+"/"+fileName);
		
		if(dataFile.exists())
			dataFile.delete();
		if(bitmapFile.exists())
			bitmapFile.delete();
		if(highscoreFile.exists())
			highscoreFile.delete();
		
	}
	
	public static void showRootFolder()
	{
		String[] namesArr=internalPath.list();
		String names="";
		for(int i=0;i<namesArr.length;i++)
		{
			names+=namesArr[i]+" ";
		}
		Toast.makeText(context,"Root Folder:\n"+names,Toast.LENGTH_LONG).show();
	}
	
	public static void showDataFolder()
	{
		String[] namesArr=dataPath.list();
		String names="";
		
		for(int i=0;i<namesArr.length;i++)
		{
			names+=namesArr[i]+" ";
		}
		Toast.makeText(context,"Data Folder:\n"+names,Toast.LENGTH_LONG).show();
	}
	
	public static void showBitmapFolder()
	{
		String[] namesArr=bitmapPath.list();
		String names="";
		
		for(int i=0;i<namesArr.length;i++)
		{
			names+=namesArr[i]+" ";
		}
		
		Toast.makeText(context,"Bitmal Folder:\n"+names,Toast.LENGTH_LONG).show();
	}
	
	public static boolean saveLevel(Bitmap bitmap, String data, String name)
	{
		return saveData(data,name) && saveBitmap(bitmap,name);
	}
	
	public static void addHighscore(String name, int time, int score)
	{
		
	}
	
	public static Bundle readHighscores()
	{
		Bundle bun=new Bundle();
		FileInputStream fis=null;
		ObjectInputStream is=null;
		Scanner sc=null;
		try 
		{
			fis = new FileInputStream(highscorePath.getAbsolutePath()+"/"+LevelInfo.currentLevelName);
			is = new ObjectInputStream(fis);
		    String s=(String) is.readObject();

			sc=new Scanner(s);
		//	if(sc.nextInt()<HIGHSCORES_MAX_COUNT)
		    int n=sc.nextInt();
			bun.putInt("count",n);
			int[] time= new int[n];
			int[] score=new int[n];
			float[] koef=new float[n];
			
			for(int i=0;i<n;i++)
			{
				time[i]=sc.nextInt();
				score[i]=sc.nextInt();
				koef[i]=(float)score[i]/time[i];
			}
			
			
			bun.putIntArray("time",time);
			bun.putIntArray("score",score);
			bun.putFloatArray("koef",koef);
			
			Toast.makeText(context,sc.next(),Toast.LENGTH_SHORT).show();
		/*	bun.putInt("kwidth",sc.nextInt());
			bun.putInt("kheight",sc.nextInt());

			int[] snake=new int[sc.nextInt()];
		    for(int i=0;i<snake.length;i++)
				snake[i]=sc.nextInt();
			bun.putIntArray("snake",snake);

			int[] blocks=new int[sc.nextInt()];
		    for(int i=0;i<blocks.length;i++)
				blocks[i]=sc.nextInt();
			bun.putIntArray("blocks",blocks);

			bun.putInt("direction",sc.nextInt());*/
		}
	    catch(Exception e) 
		{
			Toast.makeText(context, "Ocurred error with reading:\n"+e.getMessage(), Toast.LENGTH_LONG).show();
		}
		finally
		{
			try
			{
				if (fis != null)
					fis.close();
			}
			catch (IOException e)
			{}

			try
			{
				if (is != null)
					is.close();
			}
			catch (IOException e)
			{}

			sc.close();
		}
		return bun;
	}
	
	private static int getHighscorePosition(int koef)
	{
		int i=0;
		return i;
	}
	
	public static void deleteHighscore()
	{
		
	}
	
	//unused
	public static void getLevelPreviews(String[] names,Bitmap[] bitmaps)
	{
		names=dataPath.list();
	}
	
	public static String[] getLevelNames()
	{
		return dataPath.list();
	}
	
	public static boolean doesNameExist(String name)
	{
		String[] names=dataPath.list();
		for(String s:names)
		{
			if(s.equals(name))
				return true;
		}
		return false;
	}
	
	public static Bitmap[] getLevelBitmaps(int x,int y)
	{
		String[] names=bitmapPath.list();
		Bitmap[] bitmaps = new Bitmap[names.length];
		
		for(int i=0;i<names.length;i++)
		{
			bitmaps[i]=LargeImageHelper.getCompressedBitmap(names[i],x,y);
		}
		return bitmaps;
	}
	

	
	public static Bundle readLevelData(String name) 
	{
		Bundle bun=new Bundle();
		FileInputStream fis=null;
		ObjectInputStream is=null;
		Scanner sc=null;
		try 
		{
			fis = new FileInputStream(dataPath.getAbsolutePath()+"/"+name);
			is = new ObjectInputStream(fis);
		    String s=(String) is.readObject();//kwidth
			
			sc=new Scanner(s);
			bun.putInt("kwidth",sc.nextInt());
			bun.putInt("kheight",sc.nextInt());
			
			int[] snake=new int[sc.nextInt()];
		    for(int i=0;i<snake.length;i++)
				snake[i]=sc.nextInt();
			bun.putIntArray("snake",snake);
			
			int[] blocks=new int[sc.nextInt()];
		    for(int i=0;i<blocks.length;i++)
				blocks[i]=sc.nextInt();
			bun.putIntArray("blocks",blocks);
	       
			bun.putInt("direction",sc.nextInt());
			/*
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

			 fileString+="\n";
			 switch(pointer.dir)
			 {
			 case LEFT:
			 fileString+=Integer.toString(0);
			 break;
			 case UP:
			 fileString+=Integer.toString(1);
			 break;
			 case RIGHT:
			 fileString+=Integer.toString(2);
			 break;
			 case DOWN:
			 fileString+=Integer.toString(3);
			 break;
			 }
			 return fileString;
			*/
			
			
		/*	for(int i=0;i<s.length();i++)
			{
				
			}*/
		//	Toast.makeText(context,sc.nextInt()+" ",Toast.LENGTH_SHORT).show();
			 //	is.rea
		//	array[1] =is.readInt();//kheight
			
			/*int[] snake=new int[is.readInt()];
			for(int i=0;i<snake.length;i++)
				snake[i]=is.readInt();
			array[2]=snake;
			
			int[] blocks=new int[is.readInt()];
			for(int i=0;i<blocks.length;i++)
				blocks[i]=is.readInt();
			array[3]=blocks;
			array[4]=is.readInt();*/
			/*	recPoint = (ArrayList<Integer>) is.readObject();
			recTime = (ArrayList<String>) is.readObject();*/
		//	is.close();
		}
	    catch(Exception e) 
		{
			Toast.makeText(context, "Ocurred error with reading:\n"+e.getMessage(), Toast.LENGTH_LONG).show();
		}
		finally
		{
			try
			{
				if (fis != null)
					fis.close();
			}
			catch (IOException e)
			{}
			
			try
			{
				if (is != null)
					is.close();
			}
			catch (IOException e)
			{}
			
			sc.close();
		}
		return bun;
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
			 }
            sc.close();
		}
		catch (IOException e)
		{
			Toast.makeText(savedContext,e.getMessage(),Toast.LENGTH_SHORT).show();
			bool=false;
		}
		return bool;
	}
*/
	public static boolean needRewriteHighscores()
	{
		boolean b=false;
		
		return b;
	}
	
	private static void createRootInternalFolder(String folderName)
	{
		File folder=new File(internalPath.getAbsolutePath()+"/"+folderName);
		if(!folder.exists())
			folder.mkdir();
	}
	

	private static boolean saveBitmap(Bitmap bitmap, String name)
	{
		FileOutputStream fos=null;
		try
		{
			fos=new FileOutputStream(bitmapPath.getAbsolutePath()+"/"+name);
			bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
			fos.flush();
		}
		catch (Exception e)
		{
			Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
			return false;
		}
		finally
		{
			try
			{
				if(fos!=null)
					fos.close();
			}
			catch (IOException e)
			{}
		}
		return true;
	}
	
	
	private static boolean saveData(String data, String name)
	{
		FileOutputStream fos=null;
		ObjectOutputStream oos=null;
		
		try
		{
			fos=new FileOutputStream(dataPath.getAbsolutePath()+"/"+name);
			oos=new ObjectOutputStream(fos);
			oos.writeObject(data);
			
			
			fos.flush();
		}
		catch (IOException e)
		{
			Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
			return false;
		}
		finally
		{
			try
			{
				if(fos!=null)
					fos.close();
				if(oos!=null)
					oos.close();
			}
			catch (IOException e)
			{}
		}
		return true;
	}
	
	public static boolean saveHighscore(String name,int time,int score)
	{
		FileOutputStream fos=null;
		ObjectOutputStream oos=null;
		StringBuilder data=new StringBuilder();
		data.append(1+" ");
		data.append(time+" ");
		data.append(score+" ");
		data.append(name);
		try
		{
			fos=new FileOutputStream(highscorePath.getAbsolutePath()+"/"+LevelInfo.currentLevelName);
			oos=new ObjectOutputStream(fos);
			oos.writeObject(data.toString());

			fos.flush();
		}
		catch (IOException e)
		{
			Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
			return false;
		}
		finally
		{
			try
			{
				if(fos!=null)
					fos.close();
				if(oos!=null)
					oos.close();
			}
			catch (IOException e)
			{}
		}
		return true;
	}
	
	public static class LargeImageHelper
	{
		public static Bitmap getCompressedBitmap(String name,int reqWidth, int reqHeight) 
		{
			File file = new File(bitmapPath.getAbsolutePath()+"/"+name);
			String path=file.getAbsolutePath();
			
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);
			
			options.inSampleSize = calculateInSampleSize(options, reqWidth,	 reqHeight);
		
			options.inJustDecodeBounds = false;
			options.inPreferredConfig = Bitmap.Config.RGB_565;
		
			Bitmap b=BitmapFactory.decodeFile(path, options);
			
			return Bitmap.createScaledBitmap(b,reqWidth,reqHeight,true);
		}

		public static Bitmap decodeResourseCompressedBitmap(int id,int reqWidth, int reqHeight) 
		{
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeResource(context.getResources(),id,options);

			options.inSampleSize = calculateInSampleSize(options, reqWidth,	 reqHeight);

			options.inJustDecodeBounds = false;
			options.inPreferredConfig = Bitmap.Config.RGB_565;

			Bitmap b=BitmapFactory.decodeResource(context.getResources(), id, options);
			
			return Bitmap.createScaledBitmap(b,reqWidth,reqHeight,true);
		}
		
		private static int calculateInSampleSize(BitmapFactory.Options options,
												int reqWidth, int reqHeight) 
		{
			final int height = options.outHeight;
			final int width = options.outWidth;
			int inSampleSize = 1;

			if (height > reqHeight || width > reqWidth)
			{

				final int halfHeight = height / 2;
				final int halfWidth = width / 2;

				while ((halfHeight / inSampleSize) > reqHeight
					   && (halfWidth / inSampleSize) > reqWidth) 
				{
					inSampleSize *= 2;
				}
			}
			return inSampleSize;
		}
		
	}
}
