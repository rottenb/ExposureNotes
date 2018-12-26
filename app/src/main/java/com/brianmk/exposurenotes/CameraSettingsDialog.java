package com.brianmk.exposurenotes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class CameraSettingsDialog extends DialogFragment {
    private static final String LOG_TAG = CameraSettingsDialog.class.getSimpleName();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View content = getActivity().getLayoutInflater().inflate(R.layout.camera_settings_dialog, null);
        builder.setView(content);

        // Film formats
        Spinner formatSpinner = content.findViewById(R.id.default_spinner);
        ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(content.getContext(),
                R.array.format_types, R.layout.default_spinner_item);
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        formatSpinner.setAdapter(activityAdapter);
        formatSpinner.setSelection(0);

        // Save button
        Button saveButton = content.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save data stuff goes here
                dismiss();
            }
        });

        // Cancel button
        Button cancelButton = content.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return builder.create();
    }
}
