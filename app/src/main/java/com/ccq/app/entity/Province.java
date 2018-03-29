package com.ccq.app.entity;

import java.util.List;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2018/3/29.
 ****************************************/

public class Province {

    /**
     * city : [{"code":"ah","fashows":1,"id":13,"name":"安徽省","searchshows":1,"shows":1,"sort":"A"}]
     * sort : A
     */

    private String sort;
    private List<CityBean> city;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public List<CityBean> getCity() {
        return city;
    }

    public void setCity(List<CityBean> city) {
        this.city = city;
    }

    public static class CityBean {
        /**
         * code : ah
         * fashows : 1
         * id : 13
         * name : 安徽省
         * searchshows : 1
         * shows : 1
         * sort : A
         */

        private String code;
        private int fashows;
        private int id;
        private String name;
        private int searchshows;
        private int shows;
        private String sort;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getFashows() {
            return fashows;
        }

        public void setFashows(int fashows) {
            this.fashows = fashows;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSearchshows() {
            return searchshows;
        }

        public void setSearchshows(int searchshows) {
            this.searchshows = searchshows;
        }

        public int getShows() {
            return shows;
        }

        public void setShows(int shows) {
            this.shows = shows;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }
    }
}
