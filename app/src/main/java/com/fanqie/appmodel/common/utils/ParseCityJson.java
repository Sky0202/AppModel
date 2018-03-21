package com.fanqie.appmodel.common.utils;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.fanqie.appmodel.common.bean.ProvinceBean;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zpw on 2017/11/20.
 * 解析省市列表的工具类
 */

public class ParseCityJson {

    private ArrayList<String> proviences = new ArrayList<>();
    private ArrayList<ArrayList<String>> cities = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> countries = new ArrayList<>();

    public ArrayList<String> getProviences() {
        return proviences;
    }

    public ArrayList<ArrayList<String>> getCities() {
        return cities;
    }

    public ArrayList<ArrayList<ArrayList<String>>> getCountries() {
        return countries;
    }

    /**
     * 创建时间：2018/3/21 13:29
     * <p>
     * 描述：解析数据
     *
     * @author zpw
     */
    public void initJsonData(Context context) {

        try {
            //获取assets目录下的json文件数据
            InputStream inputStream = context.getAssets().open("city.json");
            String jsonData = ConvertUtils.toString(inputStream);
            List<ProvinceBean> provinceBeans = JSON.parseArray(jsonData, ProvinceBean.class);

            /**
             * 添加省份数据
             *
             * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
             * PickerView会通过getPickerViewText方法获取字符串显示出来。
             */

            //遍历省份
            for (int i = 0; i < provinceBeans.size(); i++) {

                //该省的城市列表（第二级）
                ArrayList<String> cityList = new ArrayList<>();
                //该省的所有地区列表（第三极）
                ArrayList<ArrayList<String>> countryList = new ArrayList<>();

                proviences.add(provinceBeans.get(i).getAreaName());

                //遍历该省份的所有城市
                for (int c = 0; c < provinceBeans.get(i).getCities().size(); c++) {
                    String CityName = provinceBeans.get(i).getCities().get(c).getAreaName();
                    //添加城市
                    cityList.add(CityName);

                    //该城市的所有地区列表
                    ArrayList<String> cityAreaList = new ArrayList<>();

                    //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                    if (provinceBeans.get(i).getCities().get(c).getCounties() == null
                            || provinceBeans.get(i).getCities().get(c).getCounties().size() == 0) {
                        cityAreaList.add("");
                    } else {

                        //该城市对应地区所有数据
                        for (int d = 0; d < provinceBeans.get(i).getCities().get(c).getCounties().size(); d++) {
                            String AreaName = provinceBeans.get(i).getCities().get(c).getCounties().get(d).getAreaName();
                            //添加该城市所有地区数据
                            cityAreaList.add(AreaName);
                        }
                    }
                    //添加该省所有地区数据
                    countryList.add(cityAreaList);
                }

                /**
                 * 添加城市数据
                 */
                cities.add(cityList);

                /**
                 * 添加地区数据
                 */
                countries.add(countryList);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
