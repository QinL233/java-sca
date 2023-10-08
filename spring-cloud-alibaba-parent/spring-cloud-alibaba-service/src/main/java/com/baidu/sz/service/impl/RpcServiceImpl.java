package com.baidu.sz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baidu.sz.api.service.RpcService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 封装mybatis plus通用实现，避免wrapper在rpc中传输
 *
 * @author liaoqinzhou_sz
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年04月22日 18:10:00
 */
public abstract class RpcServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements RpcService<T> {

    @Override
    public T queryById(Long id) {
        return getById(id);
    }

    @Override
    public List<T> queryAll() {
        return list();
    }

    @Override
    public T queryOne(T record) {
        return getOne(new QueryWrapper<>(record));
    }

    @Override
    public List<T> queryListByWhere(T record) {
        return list(new QueryWrapper<>(record));
    }

    @Override
    public List<T> queryListByWhereAndLike(T record, T like) {
        QueryWrapper<T> wrapper = new QueryWrapper<>(record);
        if (Objects.nonNull(like)) {
            Map<String, Object> map = BeanUtil.beanToMap(like, true, true);
            map.forEach((k, v) -> wrapper.like(k, v));
        }
        return list(wrapper);
    }

    @Override
    public Long queryCount(T record) {
        return count(new QueryWrapper<>(record));
    }

    @Override
    public Boolean saveOne(T record) {
        return saveOrUpdate(record);
    }

    @Override
    public Boolean saveAll(List<T> record) {
        return saveOrUpdateBatch(record);
    }

    @Override
    public Boolean updateByKey(T record) {
        return updateById(record);
    }

    @Override
    public Boolean updateByWhere(T where, T set) {
        return update(set, new UpdateWrapper<>(where));
    }

    @Override
    public Boolean deleteById(Long id) {
        return removeById(id);
    }

    @Override
    public Boolean deleteByIds(List<Long> ids) {
        return removeByIds(ids);
    }

    @Override
    public Boolean deleteByWhere(T record) {
        return remove(new QueryWrapper<>(record));
    }
}