package de.atomfrede.android.scc.competition;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.atomfrede.android.scc.R;
import de.atomfrede.android.scc.dao.Competition;

public class ListItemAdapter extends ArrayAdapter<Competition> {

	List<Competition> competitions;
	private final Activity context;

	static class ViewHolder {
		public TextView competitionHeadingText;
		public TextView competitionNameText;
	}

	public ListItemAdapter(Activity context, List<Competition> objects) {
		super(context, R.layout.list_item_competition, objects);
		this.competitions = objects;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.list_item_competition, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.competitionHeadingText = (TextView) rowView
					.findViewById(R.id.competition_heading_text);
			viewHolder.competitionNameText = (TextView) rowView
					.findViewById(R.id.competition_name_text);
			rowView.setTag(viewHolder);
		}
		ViewHolder holder = (ViewHolder) rowView.getTag();
		
		Competition cCompetition = competitions.get(position);
		String headerText = context.getResources().getString(R.id.competition_heading_text).replace("$i$", cCompetition.getCompetitionNumber()+"");
		
		return rowView;

	}

}
