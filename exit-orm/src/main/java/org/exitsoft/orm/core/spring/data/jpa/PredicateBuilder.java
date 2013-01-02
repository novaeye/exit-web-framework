package org.exitsoft.orm.core.spring.data.jpa;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.exitsoft.orm.core.PropertyFilter;

public interface PredicateBuilder {

	/**
	 * 获取Jpa的约束标准
	 * 
	 * @param filter 属性过滤器
	 * 
	 * @return {@link Predicate}
	 * 
	 */
	public Predicate build(PropertyFilter filter,Root<?> root, CriteriaQuery<?> query,CriteriaBuilder builder);
	
	/**
	 * 获取Predicate标准的约束名称
	 * 
	 * @return String
	 */
	public String getRestrictionName();
	
	/**
	 * 获取Jpa的约束标准
	 * 
	 * @param propertyName 属性名
	 * @param value 值
	 * 
	 * @return {@link Predicate}
	 * 
	 */
	public Predicate build(Path<?> expression, Object value,CriteriaBuilder builder);
}
