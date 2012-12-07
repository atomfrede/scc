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
package de.atomfrede.android.scc.about;

import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.atomfrede.android.scc.R;

public class AboutDialogFragment extends DialogFragment {

	public final static String TAG = "AboutDialog";

	public static AboutDialogFragment newInstance() {
		AboutDialogFragment f = new AboutDialogFragment();
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_Dialog);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialog_about, container, false);

		Resources resources = getActivity().getResources();

		getDialog().setTitle(resources.getString(R.string.about_header));
		// Look for all text views and fill them with data
		String app_ver = "";
		try {
			app_ver = this.getActivity().getPackageManager().getPackageInfo(this.getActivity().getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			Log.v(TAG, e.getMessage());
		}
		String appName = resources.getString(R.string.app_name);
		String homepage = resources.getString(R.string.link_homepage);
		String androidAnnotations = resources.getString(R.string.link_android_annotations);
		String viewpagerIndicator = resources.getString(R.string.link_viewpager_indicator);
		String greendao = resources.getString(R.string.link_viewpager_green_dao);

		TextView versionText = (TextView) v.findViewById(R.id.version_text);
		versionText.setText(app_ver);

		TextView appNameText = (TextView) v.findViewById(R.id.app_name_text);
		appNameText.setText(appName);

		TextView homepageText = (TextView) v.findViewById(R.id.homepage_link_text);
		homepageText.setText(Html.fromHtml(homepage));
		homepageText.setMovementMethod(LinkMovementMethod.getInstance());

		TextView androidAnnotationsText = (TextView) v.findViewById(R.id.android_annotations_text);
		androidAnnotationsText.setText(Html.fromHtml(androidAnnotations));
		androidAnnotationsText.setMovementMethod(LinkMovementMethod.getInstance());

		TextView viewpagerText = (TextView) v.findViewById(R.id.viewpager_text);
		viewpagerText.setText(Html.fromHtml(viewpagerIndicator));
		viewpagerText.setMovementMethod(LinkMovementMethod.getInstance());

		TextView greendaoText = (TextView) v.findViewById(R.id.greendao_text);
		greendaoText.setText(Html.fromHtml(greendao));
		greendaoText.setMovementMethod(LinkMovementMethod.getInstance());

		return v;
	}
}
