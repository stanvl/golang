package com.kingthy.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;
import java.util.Date;
/**
 * @author:xumin
 * @Description:
 * @Date:18:14 2017/11/2
 */
@Data
@ToString
public class ReplyComment   implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键
     */
    @JsonIgnore
    private Integer id;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date createDate;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 最后修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date modifyDate;

    /**
     * 最后修改人
     */
    private String modifier;

    /**
     * 版本
     */
    @JsonIgnore
    private Integer version;

    /**
     * 删除标识
     */
    @JsonIgnore
    private Boolean delFlag;

    /**
     * 业务主键
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select uuid_short()")
    private String uuid;

    private String buyersUuid;

    private String content;



}