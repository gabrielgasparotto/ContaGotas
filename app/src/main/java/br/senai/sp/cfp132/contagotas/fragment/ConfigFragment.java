package br.senai.sp.cfp132.contagotas.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import br.senai.sp.cfp132.contagotas.R;


public class ConfigFragment extends PreferenceFragment{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.config);
	}

}
