package com.fanqie.appmodel.common.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.fanqie.appmodel.R;


public abstract class ConfirmDialog extends Dialog {

	private TextView tv_title;
	private TextView btn_ok;
	private TextView btn_cancle;
	private TextView tv_content;
	private AlertDialog dialog;

	public ConfirmDialog(Context context, String content, String ok, String cancle) {
		super(context);

		showdialog(context, content, ok, cancle);
	}
	
	public void showdialog(Context context, String content, String ok, String cancle){

		dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setContentView(R.layout.dialog_confirm);
		window.setBackgroundDrawableResource(android.R.color.transparent);
		window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		window.setGravity(Gravity.CENTER);
		tv_title = (TextView) window.findViewById(R.id.tv_two_dialog_title);
		tv_content = (TextView) window.findViewById(R.id.tv_two_dialog_content);
		tv_content.setText(content);
		btn_ok = (TextView) window.findViewById(R.id.tv_two_dialog_ok);
		btn_ok.setText(ok);
		btn_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onSure();
				dialog.cancel();
			}
		});
		btn_cancle = (TextView) window.findViewById(R.id.tv_two_dialog_cancle);
		btn_cancle.setText(cancle);
		btn_cancle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		
	}

	public abstract void onSure();

}
