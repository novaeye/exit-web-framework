package org.exitsoft.orm.core.spring.data.jpa.restriction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.exitsoft.orm.core.MatchValue;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.spring.data.jpa.JpaRestrictionBuilder;
import org.exitsoft.orm.core.spring.data.jpa.PredicateBuilder;


/**
 * 处理{@link PropertyFilter#getMatchValue()}的基类，本类对3种值做处理
 * <p>
 * 1.值等于正常值的，如："amdin"，会产生的squall为:property = 'admin'
 * </p>
 * <p>
 * 2.值等于或值的，如："admin_OR_vincent"，会产生的sql为:property = 'admin' or property = 'vincent'
 * </p>
 * <p>
 * 3.值等于与值的,如:"admin_AND_vincent"，会产生的sql为:property = 'admin' and property = 'vincent'
 * </p>
 * 
 * @author vincent
 *
 */
public abstract class PredicateSingleValueSupport implements PredicateBuilder{
	
	//or值分隔符
	private String orValueSeparator = "|";
	//and值分隔符
	private String andValueSeparator = ",";
	
	public PredicateSingleValueSupport() {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.spring.data.jpa.PredicateBuilder#build(org.exitsoft.orm.core.PropertyFilter, javax.persistence.criteria.Root, javax.persistence.criteria.CriteriaQuery, javax.persistence.criteria.CriteriaBuilder)
	 */
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
					predicate.getExpressions().add(build(JpaRestrictionBuilder.getPath(propertyName,root), value, builder));
				}
			} else {
				predicate.getExpressions().add(build(JpaRestrictionBuilder.getPath(filter.getSinglePropertyName(),root), value, builder));
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
