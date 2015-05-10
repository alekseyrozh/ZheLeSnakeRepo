package com.zheleproduction.zhelesnake;

public enum Direction
{
	UP,RIGHT,DOWN,LEFT;

	static int directionToInt(Direction dir)
	{
		int a=-1;
		switch(dir)
		{
			case LEFT:
				a= 0;
				break;
			case UP:
				a= 1;
				break;
			case RIGHT:
				a= 2;
				break;
			case DOWN:
				a= 3;
		}
		return a;
	}
	static Direction intToDirection(int a)
	{
		Direction dir=Direction.RIGHT;
		switch(a)
		{
			case 0:
				dir=Direction.LEFT;
				break;
			case 1:
				dir=Direction.UP;
				break;
			case 2:
				dir=Direction.RIGHT;
				break;
			case 3:
				dir=Direction.DOWN;
				break;
		}
		return dir;
	}
}
