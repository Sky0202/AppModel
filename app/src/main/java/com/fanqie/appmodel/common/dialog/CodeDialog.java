package com.fanqie.appmodel.common.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fanqie.appmodel.R;
import com.fanqie.appmodel.common.constants.ConstantUrl;
import com.fanqie.appmodel.common.utils.OkhttpUtils;

import java.io.IOException;

import okhttp3.FormBody;


public abstract class CodeDialog extends Dialog {

    private EditText etCode;
    private TextView btn_ok;
    private ImageView ivCode;
    private AlertDialog dialog;
    private Context mContext;

    public CodeDialog(Context context) {
        super(context);
        this.mContext = context;
        showdialog(context);
    }

    public void showdialog(Context context) {

        dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        httpGetCode();

        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_code);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        etCode = (EditText) window.findViewById(R.id.et_code);
        ivCode = (ImageView) window.findViewById(R.id.iv_code_img);
        btn_ok = (TextView) window.findViewById(R.id.tv_confirm_code);

        ivCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpGetCode();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpValidateCode(etCode.getText().toString());
            }
        });

    }

    public abstract void onSure();

    // 获取图片验证码
    private void httpGetCode() {

        OkhttpUtils.RequestCallback callBack = new OkhttpUtils.RequestCallback() {
            @Override
            public void onFail(IOException e) {

            }

            @Override
            public void onSuccess(String data) throws IOException, InterruptedException {
                if (data != null) {

                    Glide.with(mContext)
                            .load(ConstantUrl.IMG_URL + data)
                            .into(ivCode);

                }
            }

            @Override
            public void onError() throws IOException {

            }

            @Override
            public void onNoData() {

            }
        };

//        OkhttpUtils.getInstance().AsynPostNoParams(ConstantUrl.GET_CODE, callBack);

    }

    // 验证图片码
    private void httpValidateCode(String code) {

        OkhttpUtils.RequestCallback callBack = new OkhttpUtils.RequestCallback() {
            @Override
            public void onFail(IOException e) {

            }

            @Override
            public void onSuccess(String data) throws IOException, InterruptedException {
                dialog.cancel();
                onSure();
            }

            @Override
            public void onError() throws IOException {

            }

            @Override
            public void onNoData() {

            }
        };

        FormBody body = new FormBody.Builder()
                .add("img_code", code)
                .build();

//        OkhttpUtils.getInstance().AsynPost(ConstantUrl.ISTRUE_VALIDATECODE, body, callBack);

    }


}
