package com.fanqie.appmodel.common.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/20.
 */

public class ProvinceBean {

    /**
     * areaId : 110000
     * areaName : 北京市
     * cities : [{"areaId":"110100","areaName":"北京市","counties":[{"areaId":"110101","areaName":"东城区"},
     * {"areaId":"110102","areaName":"西城区"},{"areaId":"110105","areaName":"朝阳区"},{"areaId":"110106","areaName":"丰台区"},]
     */

    private String areaId;
    private String areaName;
    private List<CitiesBean> cities;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public List<CitiesBean> getCities() {
        return cities;
    }

    public void setCities(List<CitiesBean> cities) {
        this.cities = cities;
    }

    public static class CitiesBean {
        /**
         * areaId : 110100
         * areaName : 北京市
         * counties : [{"areaId":"110101","areaName":"东城区"},{"areaId":"110102","areaName":"西城区"},
         * {"areaId":"110105","areaName":"朝阳区"},{"areaId":"110106","areaName":"丰台区"},
         * {"areaId":"110107","areaName":"石景山区"},{"areaId":"110108","areaName":"海淀区"}]
         */

        private String areaId;
        private String areaName;
        private List<CountiesBean> counties;

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public List<CountiesBean> getCounties() {
            return counties;
        }

        public void setCounties(List<CountiesBean> counties) {
            this.counties = counties;
        }

        public static class CountiesBean {
            /**
             * areaId : 110101
             * areaName : 东城区
             */

            private String areaId;
            private String areaName;

            public String getAreaId() {
                return areaId;
            }

            public void setAreaId(String areaId) {
                this.areaId = areaId;
            }

            public String getAreaName() {
                return areaName;
            }

            public void setAreaName(String areaName) {
                this.areaName = areaName;
            }
        }
    }
}
