package com.rubyapps.addictedtostyle.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rubyapps.addictedtostyle.R;
import com.rubyapps.addictedtostyle.model.GridItem;

public class MyGridViewAdapter extends ArrayAdapter<GridItem>  {
	
	private Context context;
	private List<GridItem> items;
	
	class ViewHolder{
		ImageView imageView;
		TextView txtTitle;
	}

	public MyGridViewAdapter(Context context, int resourceId,
			List<GridItem> items) {
		super(context, resourceId, items);
		this.context = context;
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.grid_item, null);
			holder = new ViewHolder();
			holder.txtTitle = (TextView) convertView
					.findViewById(R.id.textView);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.imageView);

			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
		holder.txtTitle.setText(items.get(position).getName());
		holder.imageView.setImageResource(items.get(position).getImageId());
		return convertView;
	}

}
