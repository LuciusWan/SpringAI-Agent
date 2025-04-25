package com.lucius.springaitest.VO;

import org.springframework.ai.tool.annotation.ToolParam;

import java.util.List;

public class CourseQuery {
    @ToolParam(required = false, description = "课程类型：编程、设计、自媒体、其它")
    private String type;
    @ToolParam(required = false, description = "学历要求：0-无、1-初中、2-高中、3-大专、4-本科及本科以上")
    private Integer edu;
    @ToolParam(required = false, description = "排序方式")
    private List<Sort> sorts;

    public CourseQuery() {
    }

    public CourseQuery(String type, Integer edu, List<Sort> sorts) {
        this.type = type;
        this.edu = edu;
        this.sorts = sorts;
    }

    /**
     * 获取
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * 设置
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取
     * @return edu
     */
    public Integer getEdu() {
        return edu;
    }

    /**
     * 设置
     * @param edu
     */
    public void setEdu(Integer edu) {
        this.edu = edu;
    }

    /**
     * 获取
     * @return sorts
     */
    public List<Sort> getSorts() {
        return sorts;
    }

    /**
     * 设置
     * @param sorts
     */
    public void setSorts(List<Sort> sorts) {
        this.sorts = sorts;
    }

    public String toString() {
        return "CourseQuery{type = " + type + ", edu = " + edu + ", sorts = " + sorts + "}";
    }

    public static class Sort {
        @ToolParam(required = false, description = "排序字段: price或duration")
        private String field;
        @ToolParam(required = false, description = "是否是升序: true/false")
        private Boolean asc;

        public Sort() {
        }

        public Sort(String field, Boolean asc) {
            this.field = field;
            this.asc = asc;
        }

        /**
         * 获取
         * @return field
         */
        public String getField() {
            return field;
        }

        /**
         * 设置
         * @param field
         */
        public void setField(String field) {
            this.field = field;
        }

        /**
         * 获取
         * @return asc
         */
        public Boolean getAsc() {
            return asc;
        }

        /**
         * 设置
         * @param asc
         */
        public void setAsc(Boolean asc) {
            this.asc = asc;
        }

        public String toString() {
            return "Sort{field = " + field + ", asc = " + asc + "}";
        }
    }
}