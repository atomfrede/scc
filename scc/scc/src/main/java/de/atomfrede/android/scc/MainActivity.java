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
package de.atomfrede.android.scc;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;

import de.atomfrede.android.scc.about.AboutDialogFragment;

@OptionsMenu(R.menu.activity_main)
@EActivity(R.layout.activity_main)
public class MainActivity extends FragmentActivity {

	private static String TAG = "scc";

	@OptionsItem(R.id.menu_about)
	public void showAboutMenu() {

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag("about_dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		DialogFragment newFragment = AboutDialogFragment.newInstance();

		newFragment.show(ft, "about_dialog");
	}

}
