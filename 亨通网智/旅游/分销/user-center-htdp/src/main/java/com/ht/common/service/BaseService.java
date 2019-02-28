package com.ht.common.service;

import java.io.Serializable;
import java.util.List;

import com.ht.common.page.Page;


/**
 * @author qiujunda
 * @date 2019年1月3日
 * @description
 */
public interface BaseService<D,E,ID extends Serializable> {

	/**
	 * @description 根据主键获取实体
	 * 有可能返回null，使用的时候需要非空判断
	 */
	public E getById(ID id);
	
	/**
	 * @description 根据主键数组删除
	 * @throws Exception 异常消息就是失败原因，并回滚事务
	 */
	public void deleteById(ID id) ;
	
	/**
	 * @description 根据主键数组删除，一个失败全部撤销
	 * @throws Exception 异常消息就是失败原因，并回滚事务
	 */
	public void deleteByIds(ID[] ids) ;
	
	/**
	 * @description 新增本信息，根据dto@AddColumn注解自动注入属性
	 * @throws Exception 异常消息就是失败原因，并回滚事务
	 */
	public E add(D dto) ;
	
	/**
	 * @description 修改信息，根据dto@UpdateColumn注解自动注入属性
	 * @throws Exception 异常消息就是失败原因，并回滚事务
	 */
	public E update(D dto);
	
	/**
	 * @description 分页查询信息，查询条件按dto对象@QueryColumn注解属性自动生成查询sql
	 * @param userInfoDTO 查询对象参数
	 */
	public Page<E> queryPage(Page<E> page,D dto);
	
	/**
	 * @description 查询所有信息，慎用，查询条件按dto对象@QueryColumn注解属性自动生成查询sql
	 * @param  查询对象参数
	 */
	public List<E> query(D dto);
	
	/**
	 * @description 查询数量，查询条件按dto对象@QueryColumn注解属性自动生成查询sql
	 * @param userInfoDTO 查询对象参数
	 */
	public Long queryCount(D dto);
	
}
