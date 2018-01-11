package com.fanqie.appmodel.common.base;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanqie.appmodel.R;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 基础webview类
 * <p>
 * Created by Administrator on 2016/9/6.
 */
public abstract class BaseWebViewActivity extends BaseActivity {

    @BindView(R.id.iv_back_basewebview)
    ImageView ivBackBasewebview;
    @BindView(R.id.tv_title_basewebview)
    TextView tvTitleBasewebview;
    @BindView(R.id.rl_toolbar_basewebview)
    RelativeLayout rlToolbarBasewebview;
    @BindView(R.id.webview_basewebview)
    WebView webviewBasewebview;

    private String url;

    @Override
    public int setContentViewId() {
        return R.layout.activity_basewebview;
    }

    @Override
    public int setBackButton() {
        return 0;
    }

    @OnClick(R.id.iv_back_basewebview)
    public void onViewClicked() {
        if (webviewBasewebview.getOriginalUrl().equals(url)) {
            finish();
        } else {
            webviewBasewebview.goBack();
        }
    }

    @Override
    public void registerPresenter() {
        registerPresenterWeb();
    }

    @Override
    public void unRegisterPresenter() {
        unregisterPresenterWeb();
    }

    @Override
    public void iniIntent(Intent it) {

        //直接通过intent显示页面
//        String title = it.getStringExtra(ConstantString.WEB_TITLE);
//        URL = it.getStringExtra(ConstantString.WEB_URL);
//        tv_title_basewebview.setText(title);
//        DebugLog.i("zzz", "--URL--:" + URL + "--title--:" + title);

    }


    @Override
    public void iniData() {

        iniWebView();
        //设置标题
//        tv_title_basewebview.setText(setTitle());
        //加载url
        webviewBasewebview.loadUrl(url);

    }

    /**
     * 初始化webview
     */
    public void iniWebView() {

        //优先使用缓存 不能使用缓存处理 优先缓存会导致无法退出登录
//        webview_basewebview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //支持javascript
        webviewBasewebview.getSettings().setJavaScriptEnabled(true);
        //禁止侧滑
//      webview_basewebview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //触摸焦点起作用
        webviewBasewebview.requestFocus();
        //取消滚动条
        webviewBasewebview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        //设置允许js弹出alert对话框
        webviewBasewebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        //js控制器
        webviewBasewebview.setWebChromeClient(new WebChromeClient());
        //webview控制器
        webviewBasewebview.setWebViewClient(setWebViewCilent());
        //机械键盘监听返回
        webviewBasewebview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (i == KeyEvent.KEYCODE_BACK && webviewBasewebview.canGoBack()) {  //表示按返回键 时的操作
                        webviewBasewebview.goBack();   //后退
                        return true;
                    }
                }

                return false;

            }
        });


        //传递js方法
//        webview_basewebview.addJavascriptInterface(new DemoJavaScriptInterface(), "demo");

        //调用js方法
        getJsMethod();

    }

//    /**
//     * 设置title
//     */
//    public abstract String setTitle();
//
//    /**
//     * 设置url
//     */
//    public abstract String setUrl();

    /**
     * 注册控制器
     */
    public abstract void registerPresenterWeb();

    /**
     * 注销控制器
     */
    public abstract void unregisterPresenterWeb();

    /**
     * 设置webviewclient
     */
    public abstract WebViewClient setWebViewCilent();

    /**
     * 调用js方法
     */
    public abstract void getJsMethod();

    @Override
    protected void onDestroy() {
        webviewBasewebview.stopLoading();
        super.onDestroy();
    }

}
