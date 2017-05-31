package com.jhzy.receptionevaluation.ui.bean.dispensingdrug;

/**
 * Created by bigyu2012 on 2017/4/27.
 */

public class TagsBean {
        /**
         * timeid : 1
         * type : 早餐前
         * isCompleted : 0
         */

        private int timeid;
        private String type;
        private int isCompleted;

    @Override
    public String toString() {
        return type;
    }

    public int getTimeid() {
            return timeid;
        }

        public void setTimeid(int timeid) {
            this.timeid = timeid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getIsCompleted() {
            return isCompleted;
        }

        public void setIsCompleted(int isCompleted) {
            this.isCompleted = isCompleted;
        }
    }
