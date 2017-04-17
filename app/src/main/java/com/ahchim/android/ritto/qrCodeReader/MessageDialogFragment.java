package com.ahchim.android.ritto.qrCodeReader;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;


/**
 * Created by Gold on 2017. 3. 28..
 */

public class MessageDialogFragment extends DialogFragment {

    public interface MessageDialogListener {
        public void onDialogPositiveClick(android.support.v4.app.DialogFragment dialog);
    }

    private String mTitle;
    private String mMessage;
    private MessageDialogListener mListener;

    public void onCreate(Bundle state) {
        super.onCreate(state);
        setRetainInstance(true);
    }

    public static MessageDialogFragment newInstance(String title, String message, MessageDialogListener listener) {
        MessageDialogFragment fragment = new MessageDialogFragment();
        fragment.mTitle = title;
        fragment.mMessage = message;
        fragment.mListener = listener;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(mMessage)
                .setTitle(mTitle);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(mListener != null) {
                    mListener.onDialogPositiveClick(MessageDialogFragment.this);
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancel버튼 클릭시
            }
        });

        return builder.create();
    }


}
