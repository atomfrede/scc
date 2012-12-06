/*
*	 SCC - The Sprintercup Companion App provides you with the Meldeergbnis right on your smartphone
*    
*    Copyright (C) 2012  Frederik Hahne <atomfrede@gmail.com>
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package de.atomfrede.android.scc.lap;

import java.util.List;

import android.app.Activity;
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
