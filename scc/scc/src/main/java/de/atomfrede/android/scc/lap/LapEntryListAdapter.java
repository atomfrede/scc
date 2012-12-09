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
import android.widget.ImageView;
import android.widget.TextView;
import de.atomfrede.android.scc.R;
import de.atomfrede.android.scc.application.SccApplication;
import de.atomfrede.android.scc.dao.LapDao;
import de.atomfrede.android.scc.dao.LapEntry;

public class LapEntryListAdapter extends ArrayAdapter<LapEntry> {

	static class ViewHolder {
		public TextView laneText;
		public TextView nameText;
		public TextView yearText;
		public TextView clubText;
		public TextView timeText;
		public ImageView doneMarkerImageView;
	}

	public Activity context;
	public List<LapEntry> objects;
	public long lapId;
	public LapDao mLapDao;

	public LapEntryListAdapter(Activity context, long lapId, List<LapEntry> objects) {
		super(context, R.layout.list_item_lap_entry, objects);
		this.context = context;
		mLapDao = ((SccApplication)context.getApplication()).lapDao;
		this.lapId = lapId;
		this.objects = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.list_item_lap_entry, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.laneText = (TextView) rowView.findViewById(R.id.lane_text);
			viewHolder.nameText = (TextView) rowView.findViewById(R.id.name_text);
			viewHolder.yearText = (TextView) rowView.findViewById(R.id.year_text);
			viewHolder.clubText = (TextView) rowView.findViewById(R.id.club_text);
			viewHolder.timeText = (TextView) rowView.findViewById(R.id.time_text);
			viewHolder.doneMarkerImageView = (ImageView) rowView.findViewById(R.id.done_marker_image);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();

		LapEntry cEntry = objects.get(position);
		boolean isDone = false;
		if (cEntry.getLap().getIsDone()) {
			isDone = true;
		}
		holder.laneText.setText(context.getResources().getString(R.string.lane).replace("$i$", cEntry.getLane() + ""));
		holder.timeText.setText(cEntry.getTime());
		holder.nameText.setText(cEntry.getFirstname() + " " + cEntry.getLastname());
		holder.clubText.setText(cEntry.getClub());
		holder.yearText.setText("(" + cEntry.getYear() + ")");
		holder.doneMarkerImageView.setVisibility(View.GONE);

		if (isDone) {
			holder.laneText.setTextColor(R.color.lap_done_grey);
			holder.timeText.setTextColor(R.color.lap_done_grey);
			holder.nameText.setTextColor(R.color.lap_done_grey);
			holder.clubText.setTextColor(R.color.lap_done_grey);
			holder.yearText.setTextColor(R.color.lap_done_grey);
			holder.doneMarkerImageView.setVisibility(View.VISIBLE);
		}

		return rowView;
	}
}
