// __BEGIN_LICENSE__
// Copyright (C) 2008-2010 United States Government as represented by
// the Administrator of the National Aeronautics and Space Administration.
// All Rights Reserved.
// __END_LICENSE__

package gov.nasa.arc.geocam.talk.activity;

import gov.nasa.arc.geocam.talk.R;
import roboguice.activity.RoboPreferenceActivity;
import android.os.Bundle;

/**
 * The Class SettingsActivity.
 */
public class SettingsActivity extends RoboPreferenceActivity {
	
	/* (non-Javadoc)
	 * @see roboguice.activity.RoboPreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}

}
