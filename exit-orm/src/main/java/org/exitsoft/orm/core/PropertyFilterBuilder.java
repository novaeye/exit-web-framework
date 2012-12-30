package org.exitsoft.orm.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.springframework.util.Assert;

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
	 * 通过表达式和对比值创建属性过滤器集合,要求表达式与值必须相等
	 * <p>
	 * 	如：
	 * </p>
	 * <code>
	 * 	PropertyFilerRestriction.createrPropertyFilter(new String[]{"EQS_propertyName1","NEI_propertyName2"},new String[]{"vincent","vincent_OR_admin"})
	 * </code>
	 * <p>
	 * 	对比值长度与表达式长度必须相等
	 * </p>
	 * 
	 * @param expressions 表达式
	 * @param matchValues 对比值
	 * 
	 * @return List
	 */
	public List<PropertyFilter> createPropertyFilters(String[] expressions,String[] matchValues) {
		if (ArrayUtils.isEmpty(expressions) && ArrayUtils.isEmpty(matchValues)) {
			return Collections.emptyList();
		}
		
		if (expressions.length != matchValues.length) {
			throw new IllegalAccessError("expressions中的值与matchValues不匹配，matchValues的长度为:" + matchValues.length + "而expressions的长度为:" + expressions.length);
		}
		
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		
		for (int i = 0; i < expressions.length; i++) {
			filters.add(createPropertyFilter(expressions[i], matchValues[i]));
		}
		
		return filters;
	}
	
	/**
	 * 通过表达式和对比值创建属性过滤器
	 * <p>
	 * 	如：
	 * </p>
	 * <code>
	 * 	PropertyFilerRestriction.createrPropertyFilter("EQS_propertyName","vincent")
	 * </code>
	 * 
	 * @param expressions 表达式
	 * @param matchValues 对比值
	 * 
	 * @return {@link PropertyFilter}
	 */
	public PropertyFilter createPropertyFilter(String expression,String matchValue) {
		
		Assert.hasText(expression, "表达式不能为空");
		
		String restrictionsNameAndClassType = StringUtils.substringBefore(expression, "_");
		
		String restrictionsName = StringUtils.substring(restrictionsNameAndClassType, 0,restrictionsNameAndClassType.length() - 1);
		String classType = StringUtils.substring(restrictionsNameAndClassType, restrictionsNameAndClassType.length() - 1, restrictionsNameAndClassType.length());
				
		if (!restrictionsMap.containsKey(restrictionsName)) {
			throw new IllegalAccessError("[" + expression + "]表达式找不到相应的约束名称,获取的值为:" + restrictionsName);

		}
		
		PropertyType propertyType = null;
		try {
			propertyType = PropertyType.valueOf(classType);
		} catch (Exception e) {
			throw new IllegalAccessError("[" + expression + "]表达式找不到相应的属性类型,获取的值为:" + classType);
		}
		
		String[] propertyNames = null;
		
		if (StringUtils.contains(expression,"_OR_")) {
			String temp = StringUtils.substringAfter(expression, restrictionsNameAndClassType + "_");
			propertyNames = StringUtils.splitByWholeSeparator(temp, "_OR_");
		} else {
			propertyNames = new String[1];
			propertyNames[0] = StringUtils.substringAfterLast(expression, "_");
		}
		
		return new PropertyFilter(restrictionsName, propertyType, propertyNames,matchValue);
	}
	
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
