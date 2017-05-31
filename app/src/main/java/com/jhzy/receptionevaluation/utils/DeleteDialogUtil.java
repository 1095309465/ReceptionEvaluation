package com.jhzy.receptionevaluation.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jhzy.receptionevaluation.R;

/**
 * Created by sxmd on 2017/3/6.
 */

public class DeleteDialogUtil implements View.OnClickListener {

    private Dialog dialog;
    private OnDeleteClickListener onDeleteClickListener;
    private TextView tv_info;

    public DeleteDialogUtil(Context mContext, String content, OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.delete_dialog_layout, null);
        initView(contentView);
        tv_info.setText(content);
        dialog = new Dialog(mContext, R.style.mydialog);
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });

    }

    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }


    public void initView(View view) {
        tv_info = (TextView) view.findViewById(R.id.tv_info);
        TextView btn_cancle = (TextView) view.findViewById(R.id.btn_cancle);
        TextView btn_ok = (TextView) view.findViewById(R.id.btn_ok);
        btn_cancle.setOnClickListener(this);
        btn_ok.setOnClickListener(this);

    }

    public interface OnDeleteClickListener {
        void onDeleteOk();

        void onDeleteCancle();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancle://取消
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteCancle();
                }

                dismiss();

                break;
            case R.id.btn_ok://确认
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteOk();
                }
                dismiss();

                break;

        }

    }
}
