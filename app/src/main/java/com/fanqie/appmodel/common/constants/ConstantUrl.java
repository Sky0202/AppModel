package com.fanqie.appmodel.common.constants;

/**
 * url公共类
 * <p/>
 * Created by Administrator on 2016/7/28.
 */
public class ConstantUrl {

    /**
     * 通用地址
     */
    public static final String URL = "";
    public static final String IMG_URL = "http://192.168.1.11:9200/";

//    public static final String URL = "http://121.42.251.109:9200/";  //外网
//    public static final String IMG_URL = "http://121.42.251.109:9200/";  //外网

    // 上传图片地址
    public static final String ADD_IMG = URL + "api/UploadFile/AddImg";

    //登录
    public static final String login = URL + "api/Account/Login";

    //用户信息
    public static final String GET_USERINFO = URL + "api/Account/Get";

    // 首页 Banner 图
    public static final String GET_BANNER_IMG = URL + "api/Common/GetBannerImg";

    //版本
    public static final String GET_VERSION_INFO = URL + "api/Common/GetVersioninfo";

    // 获取图片验证码
    public static final String GET_VALIDATECODE = URL + "api/Account/GetValidateCode";

    // 验证图片验证码
    public static final String ISTRUE_VALIDATECODE = URL + "api/Account/IsTrueValidateCode";

    // 修改密码
    public static final String CHANGE_PASSWORD = URL + "api/Account/ChangePassword";

    // 修改个人信息
    public static final String CHANGE_USERINFO = URL + "api/Account/ChangeUserInfo";


}
