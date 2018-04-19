package com.brianmk.exposurenotes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;

public class ExportDialog extends DialogFragment {
    private static final String LOG_TAG = ExportDialog.class.getSimpleName();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View content = getActivity().getLayoutInflater().inflate(R.layout.export_dialog, null);
        builder.setView(content);


        Calendar cal = Calendar.getInstance();
        final String filename =
                        Integer.toString(cal.get(cal.DAY_OF_MONTH)) + "-" +
                        Integer.toString(cal.get(cal.MONTH)) + "-" +
                        Integer.toString(cal.get(cal.YEAR)) +  " " +
                        getArguments().getString("film") + ".json";

        // Default filename
        EditText filenameText = content.findViewById(R.id.export_filename);
        filenameText.setText(filename);

        // Export method
        Spinner methodSpinner = content.findViewById(R.id.default_spinner);
        ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(content.getContext(),
                R.array.export_methods, R.layout.default_spinner_item);
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        methodSpinner.setAdapter(activityAdapter);
        methodSpinner.setSelection(0);


        // Export button
        Button exportButton = content.findViewById(R.id.export_dialog_button);
        exportButton.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity)getActivity()).exportFilmRoll(filename + ".json", "dropbox");

                dismiss();
            }
        });

        // Cancel button
        Button cancelButton = content.findViewById(R.id.cancel_dialog_button);
        cancelButton.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return builder.create();
    }
}
