package com.zheleproduction.zhelesnake.myInterface;

public class EditorMenuScreen extends MenuScreen
{
	public EditorMenuScreen(float w,float h)
	{
		super(w,h);
	}

	@Override
	void initButtons()
	{
		buttons=new LabeledButton[4];
		buttons[0]=new LabeledButton("Play");
		buttons[1]=new LabeledButton("Main Menu");
		buttons[2]=new LabeledButton("Resume");
		buttons[3]=new LabeledButton("Level Menu");
		buttons[0].setBlackRect(1f/12*w,h/2f-w/12f,w/2f-1f/12*w,h/2f+w/12f);
		buttons[1].setBlackRect(w/2f+1f/12*w,h/2f-w/12f,w-1f/12*w,h/2f+w/12f);
		buttons[2].setBlackRect(w/2f-1f/6*w,h/2f+2*w/12f,w/2f+1f/6*w,h/2f+4*w/12f);
	    buttons[3].setBlackRect(w/2f-1f/6*w,h/2f-4*w/12f,w/2f+1f/6*w,h/2f-2*w/12f);
	}
}
