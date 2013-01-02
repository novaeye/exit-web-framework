package org.exitsoft.orm.core;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.criterion.Criterion;

/**
 * 属性过滤器捆绑者
 * 
 * @author vincent
 *
 */
public abstract class PropertyFilterBuilder<T,R> {
	
	//存储约束的map
	private Map<String, T> restrictionsMap = new HashMap<String, T>();
	
	
	/**
	 * 通过{@link PropertyFilter} 创建约束
	 * 
	 * @param filter 属性过滤器
	 * 
	 * @return {@link Criterion}
	 */
	public abstract R getRestriction(PropertyFilter filter);
	
	/**
	 * 创建约束
	 * 
	 * @param propertyName 属性名称
	 * @param value 值
	 * @param restrictionName 约束名称
	 * 
	 * @return {@link Criterion}
	 */
	public abstract R getRestriction(String propertyName,Object value,String restrictionName);
	
	/**
	 * 获取约束的Map
	 * 
	 * @return Map
	 */
	public Map<String, T> getRestrictionsMap() {
		return restrictionsMap;
	}
	
	/**
	 * 设置约束的Map
	 * 
	 * @return Map
	 */
	public void setRestrictionsMap(Map<String, T> map) {
		restrictionsMap.putAll(map);
	}
	
	
}
