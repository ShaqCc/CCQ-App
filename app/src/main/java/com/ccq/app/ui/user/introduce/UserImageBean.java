package com.ccq.app.ui.user.introduce;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/7/22 14:07
 * 描述：个人中心图片列表
 * 版本：
 *
 **************************************************/

public class UserImageBean implements Serializable{

    /**
     * code : 0
     * message : 获取成功
     * data : [{"id":15,"userid":213,"resid":0,"addtime":"2018-07-22T15:14:53+08:00","sortid":1,"resources":{"id":0,"name":"","osspath":"","localpath":"","addtime":"0001-01-01T00:00:00Z","userid":0,"iscompleted":0,"cimage_id":0,"typeid":0}},{"id":16,"userid":213,"resid":97,"addtime":"2018-07-22T15:21:46+08:00","sortid":2,"resources":{"id":97,"name":"1526997570754.jpg","osspath":"http://img10.chanchequan.com/upload/2018/07/22/1532244106289416371.jpg","localpath":"./upload/2018/07/22/1532244106289416371.jpg","addtime":"2018-07-22T15:31:02+08:00","userid":0,"iscompleted":1,"cimage_id":0,"typeid":1}}]
     */

    private int code;
    private String message;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable,Comparable<DataBean>{
        /**
         * id : 15
         * userid : 213
         * resid : 0
         * addtime : 2018-07-22T15:14:53+08:00
         * sortid : 1
         * resources : {"id":0,"name":"","osspath":"","localpath":"","addtime":"0001-01-01T00:00:00Z","userid":0,"iscompleted":0,"cimage_id":0,"typeid":0}
         */

        private int id;
        private int userid;
        private int resid;
        private String addtime;
        private int sortid;
        private ResourcesBean resources;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public int getResid() {
            return resid;
        }

        public void setResid(int resid) {
            this.resid = resid;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public int getSortid() {
            return sortid;
        }

        public void setSortid(int sortid) {
            this.sortid = sortid;
        }

        public ResourcesBean getResources() {
            return resources;
        }

        public void setResources(ResourcesBean resources) {
            this.resources = resources;
        }

        @Override
        public int compareTo(@NonNull DataBean o) {
            return o.getSortid();
        }

        public static class ResourcesBean implements Serializable{
            /**
             * id : 0
             * name :
             * osspath :
             * localpath :
             * addtime : 0001-01-01T00:00:00Z
             * userid : 0
             * iscompleted : 0
             * cimage_id : 0
             * typeid : 0
             */

            private int id;
            private String name;
            private String osspath;
            private String localpath;
            private String addtime;
            private int userid;
            private int iscompleted;
            private int cimage_id;
            private int typeid;

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

            public String getOsspath() {
                return osspath;
            }

            public void setOsspath(String osspath) {
                this.osspath = osspath;
            }

            public String getLocalpath() {
                return localpath;
            }

            public void setLocalpath(String localpath) {
                this.localpath = localpath;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public int getUserid() {
                return userid;
            }

            public void setUserid(int userid) {
                this.userid = userid;
            }

            public int getIscompleted() {
                return iscompleted;
            }

            public void setIscompleted(int iscompleted) {
                this.iscompleted = iscompleted;
            }

            public int getCimage_id() {
                return cimage_id;
            }

            public void setCimage_id(int cimage_id) {
                this.cimage_id = cimage_id;
            }

            public int getTypeid() {
                return typeid;
            }

            public void setTypeid(int typeid) {
                this.typeid = typeid;
            }
        }
    }
}
