package com.gening.library.gemapper.common.mapper;

import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author G
 * @version 1.0
 * @className GeService
 * @description 通用Service接口
 * @date 2022/3/18 17:45
 */
public interface GeService<PO extends GePO, PK> {

    /**
     * 新增对象，保存空值（会替换掉数据库中的默认值）
     *
     * @param po 实体类
     */
    void insert(PO po);

    /**
     * 新增对象，保存空值（会替换掉数据库中的默认值），并返回生成主键
     *
     * @param po 实体类
     */
    void insertToUseGeneratedKey(PO po);

    /**
     * 新增对象，不保存空值
     *
     * @param po 实体类
     */
    void insertSelective(PO po);

    /**
     * 新增对象，不保存空值，，并返回生成主键
     *
     * @param po 实体类
     */
    void insertSelectiveToUseGeneratedKey(PO po);

    /**
     * 修改对象，保存空值
     *
     * @param po 修改实体
     */
    void update(PO po);

    /**
     * 修改对象，不保存空值
     *
     * @param po 修改实体
     */
    void updateSelective(PO po);

    /**
     * 根据主键进行查询
     *
     * @param id 主键值
     * @return {@link PO}
     */
    PO findById(PK id);

    /**
     * 查询所有数据
     *
     * @return {@link List<PO>}
     */
    List<PO> findAll();

    /**
     * 动态条件查询符合条件的单条数据
     *
     * @param po 条件封装实体类
     * @return {@link PO}
     */
    PO findOne(PO po);

    /**
     * 动态条件查询符合条件的集合数据
     *
     * @param po 条件封装实体类
     * @return {@link List<PO>}
     */
    List<PO> findList(PO po);

    /**
     * 通过分页进行动态条件查询符合条件的集合数据
     *
     * @param pageNum  开始页码
     * @param pageSize 每页数量
     * @param po       条件封装实体类
     * @return {@link List<PO>}
     */
    List<PO> findList(Integer pageNum, Integer pageSize, PO po);

    /**
     * 通过分页进行动态条件查询符合条件的分页对象数据
     *
     * @param currentPage 开始页码
     * @param pageSize    每页数量
     * @param po          条件封装实体类
     * @return {@link PageInfo}
     */
    PageInfo<PO> findPageInfo(Integer currentPage, Integer pageSize, PO po);

    /**
     * 根据条件删除数据
     *
     * @param po 条件封装实体类
     */
    void delete(PO po);

    /**
     * 根据主键删除数据
     *
     * @param pk 主键
     */
    void deleteById(PK pk);
}
