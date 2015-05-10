package com.zheleproduction.zhelesnake.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.zheleproduction.zhelesnake.*;



public class ImageTextAdapter extends BaseAdapter
{
    Bitmap[] bitmaps;
	String[] names;
    Context	mContext;
	public ImageTextAdapter(Context context,Bitmap[] b/*, int columnWidth*/)
	{
		mContext=context;
		FileHelper.prepareAll(context);
	    //SaveMaster.getLevelPreviews(names,bitmaps);
		names=FileHelper.getLevelNames();
		bitmaps=b;//FileHelper.getLevelBitmaps(columnWidth,columnWidth);
	}
	
	@Override
	public int getCount()
	{
		return bitmaps.length;
	}

	@Override
	public Object getItem(int p)
	{
		return bitmaps[p];
	}

	@Override
	public long getItemId(int p1)
	{
		return p1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View grid;
		LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		if(convertView == null)
		{
			grid = new View(mContext);
       		grid=inflater.inflate(R.layout.cellgrid,parent,false);
		}
		else
		{
			grid=convertView;
		}
		
		ImageView imageView=(ImageView) grid.findViewById(R.id.imagepart);
		TextView textView =(TextView) grid.findViewById(R.id.textpart);
		imageView.setImageBitmap(bitmaps[position]);

		textView.setText(names[position]);
		return grid;
	}
}
