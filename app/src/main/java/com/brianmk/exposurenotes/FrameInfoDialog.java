package com.brianmk.exposurenotes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class FrameInfoDialog extends DialogFragment {
    private static final String LOG_TAG = TimePickerDialog.class.getSimpleName();


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View content = inflater.inflate(R.layout.frame_dialog, null);

        builder.setView(content);

        Button okButton = content.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });

        return builder.create();
    }
}
