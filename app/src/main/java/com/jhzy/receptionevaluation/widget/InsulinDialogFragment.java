package com.jhzy.receptionevaluation.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.jhzy.receptionevaluation.R;

/**
 * Created by nakisaRen
 * on 17/5/3.
 */

public class InsulinDialogFragment extends DialogFragment {
    private EditText editText;
    private TextView ok;
    private TextView cancel;


    public interface InsulinDialogListener{
        void onDialogPositiveClick(DialogFragment dialogFragment);
        void onDialogNegativeClick(DialogFragment dialogFragment);
    }

    public void setListener(InsulinDialogListener listener){
        this.listener = listener;
    }

    InsulinDialogListener listener;

    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        initView(builder);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override public void onDismiss(DialogInterface dialogInterface) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        return builder.create();
    }


    private void initView(final AlertDialog.Builder builder) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.insulin_dialog, null);
        editText = (EditText) view.findViewById(R.id.insulin_dialog_edit);
        ok = ((TextView) view.findViewById(R.id.insulin_dialog_ok));
        cancel = ((TextView) view.findViewById(R.id.insulin_dialog_cancel));
        ok.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                String s = editText.getText().toString();
                if(listener != null
                    && !TextUtils.isEmpty(s)
                    && !s.startsWith(".")
                    && Double.valueOf(s)>0){
                    listener.onDialogPositiveClick(InsulinDialogFragment.this);
                }else{
                    Toast.makeText(getActivity(), "请输入大于0的剂量", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if(listener != null){
                    listener.onDialogNegativeClick(InsulinDialogFragment.this);
                }
            }
        });
        builder.setView(view);
    }

    public String getAmount(){
        return editText.getText().toString();
    }



    public interface TextClass{
        void setData();

        void setMustData();
    }

    class Text implements TextClass{

        @Override public void setData() {

        }


        @Override public void setMustData() {

        }
    }
}
