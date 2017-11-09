package com.fanqie.appmodel.common.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanqie.appmodel.R;


/**
 * Created by Administrator on 2017/5/18.
 * <p>
 * 选择性别 dialog
 */

public abstract class ChooseSexdialog extends Dialog {

    private AlertDialog dialog;

    public ChooseSexdialog(Context context) {
        super(context);
        showDialog(context);
    }

    public void showDialog(Context context) {

        dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_choose_sex);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        TextView tvDialogNan = (TextView) window.findViewById(R.id.tv_dialog_nan);
        TextView tvDialogNv = (TextView) window.findViewById(R.id.tv_dialog_nv);
        Button btnCancel = (Button) window.findViewById(R.id.btn_cancel);

        // 选择 男
        tvDialogNan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChoose("男");
                dialog.cancel();
            }
        });

        // 选择 女
        tvDialogNv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChoose("女");
                dialog.cancel();
            }
        });

        // 取消
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

    }

    public abstract void onChoose(String sex);

}
