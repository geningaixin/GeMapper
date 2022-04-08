package com.gening.library.gemapper.web.user.model;

import com.gening.library.gemapper.core.annotation.*;
import com.gening.library.gemapper.common.mapper.GePO;
import com.gening.library.gemapper.core.enums.SqlKeyType;
import com.gening.library.gemapper.web.user.enums.Sex;
import com.gening.library.gemapper.web.user.enums.State;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @author G
 */
@Getter
@Setter
@ToString
@Table(name = "user")
public class UserInfo extends GePO {

    @SqlKey(type = SqlKeyType.AUTO)
    private Integer id;

    private String userName;

    private String passWord;

    private String realName;

    private Date createTime;

    private State state;

    private Sex sex;

    @OneToOne(modelClz = Attribute.class)
    private Attribute attribute;

    @OneToMany(modelClz = Attribute.class)
    private List<Attribute> attributes;
}
