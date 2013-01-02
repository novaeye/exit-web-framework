package org.exitsoft.orm.core.spring.data.jpa.restriction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.exitsoft.orm.core.MatchValue;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.spring.data.jpa.PredicateBuilder;

public abstract class PredicateSingleValueSupport implements PredicateBuilder{
	
	//or值分隔符
	private String orValueSeparator = "|";
	//and值分隔符
	private String andValueSeparator = ",";
	
	public PredicateSingleValueSupport() {
		
	}
	
	public Predicate build(PropertyFilter filter, Root<?> root,CriteriaQuery<?> query, CriteriaBuilder builder) {

		String matchValue = filter.getMatchValue();
		Class<?> propertyType = filter.getPropertyType();
		
		MatchValue matchValueModel = MatchValue.createMatchValueModel(matchValue, propertyType,andValueSeparator,orValueSeparator);
		
		Predicate predicate = null;
		
		if (matchValueModel.hasOrOperate()) {
			predicate = builder.disjunction();
		} else {
			predicate = builder.conjunction();
		}
		
		for (Object value : matchValueModel.getValues()) {
			if (filter.hasMultiplePropertyNames()) {
				for (String propertyName:filter.getPropertyNames()) {
					predicate.getExpressions().add(build(root.get(propertyName), value, builder));
				}
			} else {
				predicate.getExpressions().add(build(root.get(filter.getSinglePropertyName()), value, builder));
			}
		}
		
		return predicate;
	}
	
	/**
	 * 获取值对比模型
	 * 
	 * @param matchValue 值
	 * @param propertyType 值类型
	 * 
	 * @return {@link MatchValue}
	 */
	public MatchValue getMatchValue(String matchValue,Class<?> propertyType) {
		return MatchValue.createMatchValueModel(matchValue, propertyType,andValueSeparator,orValueSeparator);
	}

	/**
	 * 获取and值分隔符
	 * 
	 * @return String
	 */
	public String getAndValueSeparator() {
		return andValueSeparator;
	}

	/**
	 * 设置and值分隔符
	 * @param andValueSeparator and值分隔符
	 */
	public void setAndValueSeparator(String andValueSeparator) {
		this.andValueSeparator = andValueSeparator;
	}
}
