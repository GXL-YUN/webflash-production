package cn.enilu.flash.bean.page;



import lombok.Data;

import java.util.List;

@Data
public class PageInfo {

    /**
     * 总数
     */
    private int total;

    /**
     * 每页显示条数，默认 10
     */
    private int size = 10;

    /**
     * 总页数
     */
    private int pages;

    /**
     * 当前页
     */
    private int current = 1;

    /**
     *数据
     */
    private List<Object> list;

}
