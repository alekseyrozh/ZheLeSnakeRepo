
package com.zheleproduction.zhelesnake.util;

import android.content.*;
import android.graphics.*;
import android.os.*;
import android.widget.*;
import com.zheleproduction.zhelesnake.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.sql.*;

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
	
	
	
	public static boolean createEmptyHighscoreFile(String name)
	{
		FileOutputStream fos=null;
		ObjectOutputStream oos=null;

		try
		{
			fos=new FileOutputStream(highscorePath.getAbsolutePath()+"/"+name);
			oos=new ObjectOutputStream(fos);
			oos.writeObject("0");

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
	
	public static void showHighscoreFile(String name)
	{
		FileInputStream fis=null;
		ObjectInputStream is=null;
		Scanner sc=null;
		try 
		{
			fis = new FileInputStream(highscorePath.getAbsolutePath()+"/"+name);
			is = new ObjectInputStream(fis);
			String s=(String) is.readObject();

			sc=new Scanner(s);

			StringBuilder data=new StringBuilder();
			while(sc.hasNext())
				data.append(sc.next());
			Toast.makeText(context, data.toString(), Toast.LENGTH_LONG).show();
			
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
	}
	public static void showHighscoreFolder()
	{
		String[] namesArr=highscorePath.list();
		String names="";

		for(int i=0;i<namesArr.length;i++)
		{
			names+=namesArr[i]+" ";
		}
		Toast.makeText(context,"Highscore Folder:\n"+names,Toast.LENGTH_LONG).show();
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
		return saveData(data,name) && saveBitmap(bitmap,name) && createEmptyHighscoreFile(name);
	}
	
	public static void addHighscore(String name, int time, int score)
	{
		
	}
	
	/*
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
	*/
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
	
	

	public static void processHighscores(String playerName, String levelName,int score,int time)
	{
		float coef=(float)score/time;
		
		String[] playerNames;
		int[] scores;
		int[] times;
		float[] coefs;
		
		//loading data
		Bundle info=loadHighscoresBundle(levelName);
		
		Toast.makeText(context,info.getString("s"),Toast.LENGTH_SHORT).show();
		
		playerNames=info.getStringArray("Names");
		scores=info.getIntArray("Scores");
		times=info.getIntArray("Times");
		coefs=new float[playerNames.length];
		for(int i=0;i<playerNames.length;i++)
			coefs[i]=(float)scores[i]/Math.max(times[i],1);
		
		int pos=coefs.length;
		for(int i=coefs.length-1;i>=0;i--)
		{
			if(coefs[i]>=coef)
			{
				pos=i+1;
				break;
			}
		}
		
		StringBuilder additPlayerName=new StringBuilder();
		int additScore=-2;
		int additTime=-2;
		
		int last=coefs.length-1;
		//shifting
		if(pos<=coefs.length-1)
		{
			if(coefs.length<HIGHSCORES_MAX_COUNT)
			{
				additPlayerName.append(playerNames[last]);
				additScore=scores[last];
				additTime=times[last];
			}
			for(int i=coefs.length-2;i>pos;i--)
			{
				playerNames[i+1]=playerNames[i];
				scores[i+1]=scores[i];
				times[i+1]=times[i];
			}
			
			//inserting highscore
			playerNames[pos]=playerName;
			scores[pos]=score;
			times[pos]=time;
			
		}
		else
		{
			if(coefs.length<HIGHSCORES_MAX_COUNT)
			{
				additPlayerName.append(playerName);
				additScore=score;
				additTime=time;
			}
		}
		
		boolean needAddit=!(additTime==-2);
		
	//	showingtable
		StringBuilder sb=new StringBuilder();
		sb.append(playerNames.length+"\n");

		for(int i=0;i<playerNames.length;i++)
		{
			sb.append(playerNames[i]+" "+scores[i]+" "+times[i]);
			if(i!=playerNames.length-1)
				sb.append("\n");
		}

		if(needAddit)
		{
			sb.append(additPlayerName+" "+additScore+" "+additTime);
		}
		
		Toast.makeText(context,sb.toString(),Toast.LENGTH_SHORT).show();
		
		
		rewriteHighscoreFile(levelName,playerNames,scores,times,needAddit,additPlayerName.toString(),additScore,additTime);
    }
	
	private static void rewriteHighscoreFile(String levelName,String[] playerNames,int[] scores,int[] times,boolean needAddit,String additPlayerName,int additScore,int additTime)
	{
		RewriteHighscoresTask rewriteTask=new RewriteHighscoresTask();
		rewriteTask.execute(levelName,playerNames,scores,times,needAddit,additPlayerName,additScore,additTime);
	}
	
	private static class RewriteHighscoresTask extends AsyncTask<Object,Void,Void>
	{
		@Override
		protected Void doInBackground(Object[] p)
		{
		    rewriteHighscoreFileTaskMethod((String)p[0],(String[])p[1],(int[]) p[2],(int[]) p[3],(boolean)p[4],(String) p[5],(int)p[6],(int) p[7]);
			return null;
		}
	private static void rewriteHighscoreFileTaskMethod(String levelName,String[] playerNames,int[] scores,int[] times,boolean needAddit,String additPlayerName,int additScore,int additTime)
	{
		FileOutputStream fos=null;
		ObjectOutputStream oos=null;

		StringBuilder sb=new StringBuilder();
		int k=0;
		if(needAddit)
			k=1;
		sb.append((playerNames.length+k)+"\n");
		
		for(int i=0;i<playerNames.length;i++)
		{
			sb.append(playerNames[i]+"\n"+scores[i]+" "+times[i]);
			if(i!=playerNames.length-1)
				sb.append("\n");
		}
		
		if(needAddit)
		{
			sb.append(additPlayerName+" "+additScore+" "+additTime);
		}
		
		//Toast.makeText(context,"saved data: "+sb.toString(),Toast.LENGTH_SHORT).show();
		
		try
		{
			fos=new FileOutputStream(highscorePath.getAbsolutePath()+"/"+levelName);
			oos=new ObjectOutputStream(fos);
			oos.writeObject(sb.toString());

			fos.flush();
		}
		catch (IOException e)
		{
		//	Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
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
	}
	};
	
	private static Bundle loadHighscoresBundle(String levelName)
	{
		ReadHighscoresTask readTask=new ReadHighscoresTask();
		Bundle bun=null;
		readTask.execute(levelName);
		try
		{
			bun = readTask.get();
//	if(bun==null)
	//		Toast.makeText(context,"null",Toast.LENGTH_LONG).show();
		//	return null;
		}
		catch (ExecutionException e)
		{
			Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
		}
		catch (InterruptedException e)
		{
			Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
		}
		
		return bun;
	}
	
    private static class ReadHighscoresTask extends AsyncTask<String,Void,Bundle>
	{
		@Override
		protected Bundle doInBackground(String[] p1)
		{
		    return readHighscoresFromFile(p1[0]);
		}

		private Bundle readHighscoresFromFile(String levelName)
		{
			Bundle bun=new Bundle();

			FileInputStream fis=null;
			ObjectInputStream is=null;
			Scanner sc=null;
			try 
			{
				fis = new FileInputStream(highscorePath.getAbsolutePath()+"/"+levelName);
				is = new ObjectInputStream(fis);
				String s=(String) is.readObject();

				sc=new Scanner(s);
				
				int count=sc.nextInt();
			  	bun.putInt("Count",count);
				String[] names=new String[count];
				int[] scores=new int[count];
				int[] times=new int[count];
				for(int i=0;i<count;i++)
				{
					names[i]=sc.nextLine();
					scores[i]=sc.nextInt();
					times[i]=sc.nextInt();
				}
				
			
				bun.putStringArray("Names",names);
				bun.putIntArray("Scores",scores);
				bun.putIntArray("Times",times);
				
				bun.putString("s",s);

			}
			catch(Exception e) 
			{
			//	Toast.makeText(context, "Ocurred error with reading:\n"+e.getMessage(), Toast.LENGTH_LONG).show();
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
		    String s=(String) is.readObject();
			
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
