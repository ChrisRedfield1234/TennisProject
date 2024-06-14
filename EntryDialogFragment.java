package com.example.tennisproject;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class EntryDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireActivity())
                .setTitle("確認")
                .setMessage("プレイヤーを棄権にしますか")
                .setPositiveButton("棄権する", (dialog, id) -> {
                    // このボタンを押した時の処理を書きます。
                    PlayerManagement callingActivity = (PlayerManagement) getActivity();
                    callingActivity.onReturnValue("true");
                })
                .setNegativeButton("キャンセル", null)
                .create();
    }

}
