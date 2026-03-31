package cn.enilu.flash.bean.page;

import lombok.Data;

import java.util.List;

@Data
public class RequertInfo {


    /**
     * 每页显示条数，默认 10
     */
    private int size ;

    /**
     * 当前页
     */
    private int current;


    /**
     * 查询条件
     */
    private List<Pame> parem;


    public List<Pame> getSqlParem() {
        return parem;
    }
}
