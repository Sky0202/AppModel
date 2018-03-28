package com.fanqie.appmodel.common.base;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanqie.appmodel.R;
import com.fanqie.appmodel.common.utils.CommonUtils;

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

        WebSettings webSettings = webviewBasewebview.getSettings();

        //js相关
        //支持js
        webSettings.setJavaScriptEnabled(true);
        //允许js交互
        webSettings.setDomStorageEnabled(true);
        //支持通过JS打开新窗口，也就是支持js弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //缩放操作

        //支持缩放，默认为true。是下面那个的前提。
        webSettings.setSupportZoom(false);
        //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setBuiltInZoomControls(false);
        //隐藏原生的缩放控件
        webSettings.setDisplayZoomControls(true);
        //将图片调整到适合webview的大小
        webSettings.setUseWideViewPort(true);
        // 缩放至屏幕的大小
        webSettings.setLoadWithOverviewMode(true);

        //防止被操作
        webSettings.setAllowFileAccess(false);

        //允许数据存储
        webSettings.setDatabaseEnabled(true);
        //支持自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
        //设置编码格式
        webSettings.setDefaultTextEncodingName("utf-8");

        //优化处理
        // 不使用缓存
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        if (CommonUtils.isNetworkConnected()) {
            //根据cache-control决定是否从网络上取数据。
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            //没网，则从本地获取，即离线加载
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        //移除危险接口
        webviewBasewebview.removeJavascriptInterface("searchBoxJavaBridge_");
        webviewBasewebview.removeJavascriptInterface("accessibility");
        webviewBasewebview.removeJavascriptInterface("accessibilityTraversal");
        //关闭自动保存密码功能，放置明文存放数据
        webSettings.setSavePassword(false);

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
