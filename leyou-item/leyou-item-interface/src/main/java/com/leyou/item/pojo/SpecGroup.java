package com.leyou.item.pojo;

import javax.persistence.*;
import java.util.List;

/** 规格组实体类, 参数模板化
 * @author shen youjian
 * @date 2018/7/22 14:52
 */
@Table(name = "tb_spec_group")
public class SpecGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long cid;

    private String name;
    /** 该规格组下 对应的规格参数的 key */
    @Transient
    private List<SpecParam> specParamList;

    public List<SpecParam> getSpecParamList() {
        return specParamList;
    }

    public void setSpecParamList(List<SpecParam> specParamList) {
        this.specParamList = specParamList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
