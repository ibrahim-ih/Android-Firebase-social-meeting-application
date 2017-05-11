package com.ngenious.ibrahim.liny.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by ibrahim on 10/05/17.
 */
import com.ngenious.ibrahim.liny.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchFilterDialogFragment extends DialogFragment {
    private static String TAG = "SearchFilterDialogFragment";
    @BindView(R.id.maleCheckBox)CheckBox _maleCheckBox;
    @BindView(R.id.femaleCheckBox)CheckBox _femaleCheckBox;
    @BindView(R.id.ageSeekBar)SeekBar _ageSeekBar;
    @BindView(R.id.distanceSeekBar)SeekBar _distanceSeekBar;
    @BindView(R.id.ageTextView)TextView _ageTextView;
    @BindView(R.id.distanceTextView)TextView _distanceTextView;
    String gender, age, radius;
public static final String LINY_FILTER_PREFS = "";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.filter_dialog_layout, null);
        builder.setTitle("Customize your Search")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveInPrefs();
                        Log.i(TAG, "positiveButton clicked");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                        Log.i(TAG, "negativeButton clicked");
                    }
                })
                .setView(v);

        ButterKnife.bind(this, v);
        getValue();
    return builder.create();
    }

    public static SearchFilterDialogFragment newInstance() {
        
        Bundle args = new Bundle();
        
        SearchFilterDialogFragment fragment = new SearchFilterDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

 public void getValue(){

     if (_maleCheckBox.isChecked() && _femaleCheckBox.isChecked()){
         Log.i(TAG, "gender :" + "all");
         gender = "all";
     }else if(_maleCheckBox.isChecked())
     {
         gender = "male";

         Log.i(TAG, "gender :"+"male" );

     }else  if (_femaleCheckBox.isChecked())
     {
         gender = "female";

         Log.i(TAG, "gender :"+"female");
     }else {
         _maleCheckBox.setChecked(true);
         _femaleCheckBox.setChecked(true);
         gender = "all";

         Log.i(TAG, "gender :"+"no gender selected");
     }
     _ageSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
         @Override
         public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
             progress = progress+18;
             _ageTextView.setText(String.valueOf(progress));
             age = String.valueOf(progress);
         }

         @Override
         public void onStartTrackingTouch(SeekBar seekBar) {

         }

         @Override
         public void onStopTrackingTouch(SeekBar seekBar) {

         }
     });

     _distanceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
         @Override
         public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
             progress = progress+1;
             _distanceTextView.setText(String.valueOf(progress));
             radius = String.valueOf(progress);
         }

         @Override
         public void onStartTrackingTouch(SeekBar seekBar) {

         }

         @Override
         public void onStopTrackingTouch(SeekBar seekBar) {

         }
     });

 }

 private void saveInPrefs(){
     SharedPreferences.Editor
             editor = this.getActivity().getSharedPreferences(LINY_FILTER_PREFS, Context.MODE_PRIVATE)
             .edit();
     editor.putString("gender", gender);
     editor.putString("age", age);
     editor.putString("radius",radius);
     editor.apply();
 }


}
