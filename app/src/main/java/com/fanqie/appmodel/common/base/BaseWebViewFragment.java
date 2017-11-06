package com.fanqie.appmodel.common.base;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanqie.appmodel.R;

import butterknife.BindView;


/**
 * 默认webview的fragment
 * <p>
 * Created by Administrator on 2016/8/23.
 */
public abstract class BaseWebViewFragment extends BaseFragment {

    @BindView(R.id.iv_back_basewebview)
    ImageView ivBackBasewebview;
    @BindView(R.id.tv_title_basewebview)
    TextView tvTitleBasewebview;
    @BindView(R.id.rl_toolbar_basewebview)
    RelativeLayout rlToolbarBasewebview;
    @BindView(R.id.webview)
    WebView webview;

    @Override
    public int setContentViewId() {
        return R.layout.fragment_webview;
    }

    @Override
    public void iniData() {

        //初始化webview
        iniWebView();
        //获取url
        getUrl();
        //设置标题栏
        if (!isShowTitleBar()) {
            rlToolbarBasewebview.setVisibility(View.GONE);
        } else {
            tvTitleBasewebview.setText(setTitle());

        }

    }

    /**
     * 初始化webview
     */
    public void iniWebView() {

        //优先使用缓存 不能使用缓存处理 优先缓存会导致无法退出登录
//        webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //支持javascript
        webview.getSettings().setJavaScriptEnabled(true);
        //触摸焦点起作用
        webview.requestFocus();
        //取消滚动条
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        //设置允许js弹出alert对话框
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        //js控制器
        WebChromeClient webChromeClient = setWebChromeClient();

        if (webChromeClient != null) {
            webview.setWebChromeClient(webChromeClient);
        }

    }

    /**
     * 从后台获取数据网络连接 通过接口返回数据
     */
    public void getUrl() {

        //加载url
        if (!setloadUrl().isEmpty()) {
            webview.loadUrl(setloadUrl());
        }

        //webview控制器
        if (setWebViewClient() != null) {
            webview.setWebViewClient(setWebViewClient());
        }

    }

    /**
     * 设置js控制器
     */
    public abstract WebChromeClient setWebChromeClient();

    /**
     * 加载url
     */
    public abstract String setloadUrl();

    /**
     * 设置webview控制器
     */
    public abstract WebViewClient setWebViewClient();

    /**
     * 是否显示标题栏
     */
    public abstract boolean isShowTitleBar();

    /**
     * 设置标题栏内容
     */
    public abstract String setTitle();


}
