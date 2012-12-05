package de.atomfrede.android.scc.lap;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.atomfrede.android.scc.R;
import de.atomfrede.android.scc.dao.LapEntry;

public class LapEntryListAdapter extends ArrayAdapter<LapEntry> {

	static class ViewHolder {
		public TextView laneText;
	}
	
	public final Activity context;
	public final List<LapEntry> objects;
	
	public LapEntryListAdapter(Activity context, List<LapEntry> objects) {
		super(context,R.layout.list_item_lap_entry, objects);
		this.context = context;
		this.objects = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.list_item_lap_entry, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.laneText = (TextView) rowView
					.findViewById(R.id.lane_text);
			rowView.setTag(viewHolder);
		}
		
		ViewHolder holder = (ViewHolder) rowView.getTag();
		
		LapEntry cEntry = objects.get(position);
		holder.laneText.setText(cEntry.toString());
		
		return rowView;
	}
}
