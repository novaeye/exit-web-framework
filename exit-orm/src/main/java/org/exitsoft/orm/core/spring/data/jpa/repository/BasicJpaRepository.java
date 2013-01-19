package org.exitsoft.orm.core.spring.data.jpa.repository;

import java.io.Serializable;
import java.util.List;

import org.exitsoft.orm.core.PropertyFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 针对spring data jpa所提供的接口{@link JpaRepository}再次扩展，支持{@link PropertyFilter}查询和其他方式查询
 * @author vincent
 *
 * @param <T> ORM对象
 * @param <ID> 主键Id类型
 */
public interface BasicJpaRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T>{
	
	/**
	 * 通过属性过滤器查询全部对象
	 * 
	 * @param filters 属性过滤器集合
	 * 
	 * @return List
	 */
	public List<T> findAll(List<PropertyFilter> filters);
	
	/**
	 * 通过属性过滤器查询全部对象
	 * 
	 * @param filters 属性过滤器集合
	 * @param sort 排序形式
	 * 
	 * @return List
	 */
	public List<T> findAll(List<PropertyFilter> filters, Sort sort);
	
	/**
	 * 通过条件表达式查询全部对象
	 * <pre>
	 * 如：
	 * findAll("EQS_propertyName","vincent")
	 * </pre>
	 * 
	 * @param expression 表达式
	 * @param value 值
	 * 
	 * @return List
	 */
	public List<T> findAll(String expression,String value);
	
	/**
	 * 通过条件表达式查询全部对象
	 * <pre>
	 * 如：
	 * findAll("EQS_propertyName","vincent",new Sort(Direction.DESC, "propertyName"))
	 * </pre>
	 * 
	 * @param expression 表达式
	 * @param value 表达式
	 * @param sort 排序形式
	 * 
	 * @return List
	 */
	public List<T> findAll(String expression,String value,Sort sort);
	
	/**
	 * 通过表达式和对比值查询全部对象
	 * <pre>
	 * 如：
	 * findAll(new String[]{"EQS_propertyName1","NEI_propertyName2"},new String[]{"vincent","vincent|admin"})
	 * 对比值长度与表达式长度必须相等
	 * </pre>
	 * @param expressions 表达式数组
	 * @param values 值数组
	 * 
	 * @return List
	 */
	public List<T> findAll(String[] expressions,String[] values);
	
	/**
	 * 通过表达式和对比值查询全部对象
	 * <pre>
	 * 如：
	 * findAll(new String[]{"EQS_propertyName1","NEI_propertyName2"},new String[]{"vincent","vincent|admin"}，new Sort(Direction.DESC, "propertyName1"))
	 * 对比值长度与表达式长度必须相等
	 * </pre>
	 * @param expressions 表达式数组
	 * @param values 值数组
	 * @param 排序形式
	 * 
	 * @return List
	 */
	public List<T> findAll(String[] expressions,String[] values, Sort sort);
	
	/**
	 * 通过属性过滤器查询对象分页
	 * 
	 * @param pageable 分页参数
	 * @param filters 属性过滤器集合
	 * 
	 * @return {@link Page}
	 */
	public Page<T> findAll(Pageable pageable, List<PropertyFilter> filters);
	
	/**
	 * 通过属性过滤器查询单个对象
	 * 
	 * @param filters 属性过滤器
	 * 
	 * @return Object
	 */
	public T findOne(List<PropertyFilter> filters);
	
	/**
	 * 通过表达式查询单个对象
	 * <pre>
	 * 如：
	 * findOne("EQS_propertyName","vincent")
	 * </pre>
	 * 
	 * @param expression 表达式
	 * @param value 值
	 * 
	 * @return Object
	 */
	public T findOne(String expression,String value);
	
	/**
	 * 通过表达式和对比值查询单个对象
	 * <pre>
	 * 如：
	 * findOne(new String[]{"EQS_propertyName1","NEI_propertyName2"},new String[]{"vincent","vincent|admin"})
	 * 对比值长度与表达式长度必须相等
	 * </pre>
	 * 
	 * @param expressions 表达式数组
	 * @param values 对比值数组
	 * 
	 * @return Object
	 */
	public T findOne(String[] expressions,String[] values);
	
}
