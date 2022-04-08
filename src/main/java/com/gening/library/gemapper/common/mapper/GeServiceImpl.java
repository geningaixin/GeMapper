package com.gening.library.gemapper.common.mapper;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author G
 * @version 1.0
 * @className GeServiceImpl
 * @description 通用Service实现类
 * @date 2022/3/18 17:45
 */
public abstract class GeServiceImpl<PO extends GePO, PK> implements GeService<PO, PK> {

    /**
     * 子类实现，获取自身需要用到的Mapper
     *
     * @return {@link GeMapper}
     */
    protected abstract GeMapper<PO, PK> getMapper();

    @Override
    public void insert(PO po) {
        this.getMapper().insert(po);
    }

    @Override
    public void insertToUseGeneratedKey(PO po) {
        this.getMapper().insertToUseGeneratedKey(po);
    }

    @Override
    public void insertSelective(PO po) {
        this.getMapper().insertSelective(po);
    }

    @Override
    public void insertSelectiveToUseGeneratedKey(PO po) {
        this.getMapper().insertSelectiveToUseGeneratedKey(po);
    }

    @Override
    public void update(PO po) {
        this.getMapper().update(po);
    }

    @Override
    public void updateSelective(PO po) {
        this.getMapper().updateSelective(po);
    }

    @Override
    public PO findById(PK id) {
        return this.getMapper().queryById(id);
    }

    @Override
    public List<PO> findAll() {
        return this.getMapper().queryAll();
    }

    @Override
    public PO findOne(PO po) {
        PageHelper.startPage(1, 1);
        List<PO> poList = getMapper().dynamicQuery(po);
        if (!CollectionUtils.isEmpty(poList)) {
            return poList.get(0);
        }
        return null;
    }

    @Override
    public List<PO> findList(PO po) {
        return this.getMapper().dynamicQuery(po);
    }

    @Override
    public List<PO> findList(Integer pageNum, Integer pageSize, PO po) {
        PageHelper.startPage(pageNum, pageSize);
        return getMapper().dynamicQuery(po);
    }

    @Override
    public PageInfo<PO> findPageInfo(Integer currentPage, Integer pageSize, PO po) {
        if (currentPage == null || currentPage < 1) {
            throw new NullPointerException("currentPage must be >= 1");
        }
        if (pageSize == null || pageSize < 1) {
            throw new NullPointerException("pageSize must be >= 1 ");
        }
        PageHelper.startPage(currentPage, pageSize);
        List<PO> poList = getMapper().dynamicQuery(po);
        return new PageInfo<>(poList);
    }

    @Override
    public void delete(PO po) {
        this.getMapper().delete(po);
    }

    @Override
    public void deleteById(PK pk) {
        this.getMapper().deleteById(pk);
    }
}
