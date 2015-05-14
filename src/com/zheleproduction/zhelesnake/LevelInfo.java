package com.zheleproduction.zhelesnake;

public class LevelInfo
{
	public static boolean arcadeMode;
	public static String currentLevelName;
	public static int kwidth,kheight;
	public static int[] snake,blocks;
	public static Direction dir;
	public static boolean needLevelChoice;
	
	public static int[] defSnake={1,2};
	public static int[] emptyArr={};
	
	public static void setDefault()
	{
		kwidth=3;
		kheight=3;
		arcadeMode=true;
		snake = defSnake;
		blocks= emptyArr;
		
		dir=Direction.RIGHT;
	}
	
	public static void setDefault(int w, int h)
	{
		kwidth=w;
		kheight=h;
		arcadeMode=true;
		snake = defSnake;
		blocks= emptyArr;

		dir=Direction.RIGHT;
	}
	
	
	public static void setEmpty()
	{
		kwidth=10;
		kheight=10;
		snake = emptyArr;
		blocks= emptyArr;
		dir=Direction.RIGHT;
	}
	public static void setEmpty(int w, int h)
	{
		kwidth=w;
		kheight=h;
		snake = emptyArr;
		blocks= emptyArr;
		dir=Direction.RIGHT;
	}
}
