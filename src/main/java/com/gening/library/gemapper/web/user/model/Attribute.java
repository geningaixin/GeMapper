package com.gening.library.gemapper.web.user.model;

import com.gening.library.gemapper.core.annotation.Table;
import com.gening.library.gemapper.common.mapper.GePO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author G
 */
@Getter
@Setter
@Table(name = "attribute")
public class Attribute extends GePO {

    private Integer id;

    private String name;

    private Integer userId;
}
