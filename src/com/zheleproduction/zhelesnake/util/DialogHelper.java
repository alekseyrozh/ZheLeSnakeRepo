package com.zheleproduction.zhelesnake.util;

import android.app.*;
import android.content.*;
import android.view.*;
import com.zheleproduction.zhelesnake.*;
//import com.zheleproduction.ProDraw.Redactor;

public class DialogHelper
{
	Context savedContext;
	AlertDialog dialog;
	boolean dialogInited;
	
	public DialogHelper(Context context)
	{
		savedContext=context;
		dialogInited=false;
		dialog=null;
	}
	public void showDialog(String title,String message, String positiveButton, String negativeButton,boolean cancelable)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(savedContext);
		if(title!=null)
			builder.setTitle(title);
		
		builder.setCancelable(cancelable);

		if(message!=null)
			builder.setMessage(message);
		
		builder.setNegativeButton(negativeButton, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface p1, int p2)
			{
				onNegativeButtonClick();
			}
		});


		builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface p1, int p2)
			{
				onPositiveButtonClick();
			}
		});

		dialog = builder.create();
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
			{
				@Override
				public void onDismiss(DialogInterface p1)
				{
					onDialogDismissed();
				}
			});
		Redactor.dialogIsClosed=false;
		dialog.show();
	}
	
	
	public void showEmptyDialog(String title,String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(savedContext);
		builder.setTitle(title).setMessage(message);
		dialog = builder.create();
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
			{
				@Override
				public void onDismiss(DialogInterface p1)
				{
					onDialogDismissed();
				}
			});
		dialog.show();
	}
	
	public void showNumberPickDialog(View v)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(savedContext);
		builder.setTitle("Size choice");
		
		builder.setView(v);
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					onNegativeButtonClick();
				}
			});


		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
				    onPositiveButtonClick();
				}


			});


		dialog = builder.create();
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
			{
				@Override
				public void onDismiss(DialogInterface p1)
				{
					onDialogDismissed();
				}
			});
		Redactor.dialogIsClosed=false;
		dialog.show();
	}
	
	public void showDialog(String title, String message ,String positiveButton, String negativeButton, boolean cancelable,View /*inflated*/dialogLayout)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(savedContext);
		builder.setTitle(title).setCancelable(cancelable);
	    
		if(message!=null)
			builder.setMessage(message);

		builder.setView(dialogLayout);
		builder.setNegativeButton(negativeButton, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					onNegativeButtonClick();
				}
			});


		builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
				    onPositiveButtonClick();
				}


			});


		dialog = builder.create();
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
			{
				@Override
				public void onDismiss(DialogInterface p1)
				{
					onDialogDismissed();
				}
			});
		Redactor.dialogIsClosed=false;
		dialog.show();
	}
	

	public void showOneButtonDialog(String title,String message, String positiveButton,boolean cancelable)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(savedContext);

		if(title!=null)
			builder.setTitle(title);

		builder.setCancelable(cancelable);

		if(message!=null)
			builder.setMessage(message);

		builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					onPositiveButtonClick();
				}
			});

		dialog = builder.create();
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
			{
				@Override
				public void onDismiss(DialogInterface p1)
				{
					onDialogDismissed();
				}
			});
		Redactor.dialogIsClosed=false;
		dialog.show();
	}
	

	public void showDialogWithNeutral(String title,String message, String positiveButton, String neutralButton, String negativeButton,boolean cancelable)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(savedContext);

		if(title!=null)
			builder.setTitle(title);

		builder.setCancelable(cancelable);

		if(message!=null)
			builder.setMessage(message);

		builder.setNegativeButton(negativeButton, new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					onNegativeButtonClick();
				}


			});
        builder.setNeutralButton(neutralButton, new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					onNeutralButtonClick();
				}


			});

		builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					onPositiveButtonClick();
				}
			});

		dialog = builder.create();
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
			{
				@Override
				public void onDismiss(DialogInterface p1)
				{
					onDialogDismissed();
				}
			});
		Redactor.dialogIsClosed=false;
		dialog.show();
	}
	
	public void showDialogWithNeutral(String title,String message, String positiveButton, String neutralButton, String negativeButton,View dialogLayout, boolean cancelable)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(savedContext);

		if(title!=null)
			builder.setTitle(title);

		builder.setCancelable(cancelable);

		if(message!=null)
			builder.setMessage(message);

		builder.setView(dialogLayout);
		builder.setNegativeButton(negativeButton, new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					onNegativeButtonClick();
				}


			});
        builder.setNeutralButton(neutralButton, new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					onNeutralButtonClick();
				}

			
		});

		builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					onPositiveButtonClick();
				}
			});

		dialog = builder.create();
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
			{
				@Override
				public void onDismiss(DialogInterface p1)
				{
					onDialogDismissed();
				}
		});
		Redactor.dialogIsClosed=false;
		dialog.show();
	}

	public void showOneButtonDialog(String title,String message, String positiveButton, View dialogLayout, boolean cancelable)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(savedContext);

		if(title!=null)
			builder.setTitle(title);

		builder.setCancelable(cancelable);

		if(message!=null)
			builder.setMessage(message);

		builder.setView(dialogLayout);

		builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					onPositiveButtonClick();
				}
			});

		dialog = builder.create();
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
			{
				@Override
				public void onDismiss(DialogInterface p1)
				{
					onDialogDismissed();
				}
			});
		Redactor.dialogIsClosed=false;
		dialog.show();
	}
	
	public void showOverwriteDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(savedContext);
		builder.setTitle("Warning!");
		builder.setCancelable(false);
		builder.setMessage("File with this name already exists.\nOverwrite it?");
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					onNegativeButtonClick();
				}
			});
			
		builder.setPositiveButton("Overwrite", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					onPositiveButtonClick();
				}
			});

		builder.setNeutralButton("Change the name", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					onNeutralButtonClick();
				}
			});
		
		dialog = builder.create();
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
			{
				@Override
				public void onDismiss(DialogInterface p1)
				{
					onDialogDismissed();
				}
			});
		Redactor.dialogIsClosed=false;
		dialog.show();
	}
	
	final public void dismiss()
	{
		if(dialog!=null)
			dialog.dismiss();
	}
	
	
	
    protected void onNegativeButtonClick(){}
    protected void onPositiveButtonClick(){}
    protected void onNeutralButtonClick(){}
    protected void onDialogDismissed(){};
	
}
