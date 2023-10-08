package com.baidu.sz.api.service;

import java.util.List;

/**
 * @author liaoqinzhou_sz
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年04月22日 18:15:00
 */
public interface RpcService<T> {

    /**
     * 根据id查询数据
     *
     * @param id
     * @return
     */
    T queryById(Long id);

    /**
     * 查询所有数据
     *
     * @return
     */
    List<T> queryAll();

    /**
     * 根据条件查询一条数据
     *
     * @param record
     * @return
     */
    T queryOne(T record);

    /**
     * 根据条件查询数据列表
     *
     * @param record
     * @return
     */
    List<T> queryListByWhere(T record);

    /**
     * 根据条件查询数据列表
     *
     * @param record
     * @param like
     * @return
     */
    List<T> queryListByWhereAndLike(T record, T like);

    /**
     * 根据条件查询数据数
     *
     * @param record
     * @return
     */
    Long queryCount(T record);

    /**
     * 保存数据
     *
     * @param record
     * @return
     */
    Boolean saveOne(T record);

    /**
     * 批量保存数据
     *
     * @param record
     * @return
     */
    Boolean saveAll(List<T> record);

    /**
     * 更新数据
     *
     * @param record
     * @return
     */
    Boolean updateByKey(T record);

    /**
     * 根据条件更新数据
     *
     * @param where
     * @param set
     * @return
     */
    Boolean updateByWhere(T where, T set);

    /**
     * 根据id删除数据
     *
     * @param id
     * @return
     */
    Boolean deleteById(Long id);

    /**
     * 根据ids批量删除数据
     *
     * @param ids
     * @return
     */
    Boolean deleteByIds(List<Long> ids);

    /**
     * 根据条件删除数据
     *
     * @param record
     * @return
     */
    Boolean deleteByWhere(T record);
}
