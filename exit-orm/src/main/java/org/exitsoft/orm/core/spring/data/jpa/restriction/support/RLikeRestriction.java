package org.exitsoft.orm.core.spring.data.jpa.restriction.support;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.exitsoft.orm.core.spring.data.jpa.restriction.PredicateSingleValueSupport;

/**
 * 右模糊约束 ( from object o where o.value like '?%') RestrictionName:RLIKE
 * <p>
 * 表达式:RLIKE属性类型_属性名称[_OR_属性名称...]
 * </p>
 * 
 * @author vincent
 *
 */
public class RLikeRestriction extends PredicateSingleValueSupport{
	
	public final static String RestrictionName = "RLIKE";
	
	
	public String getRestrictionName() {
		return RestrictionName;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Predicate build(Path expression, Object value,CriteriaBuilder builder) {
		
		return builder.like(expression, value + "%");
	}

	
}

