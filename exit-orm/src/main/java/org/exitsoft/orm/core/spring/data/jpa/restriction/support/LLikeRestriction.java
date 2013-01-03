package org.exitsoft.orm.core.spring.data.jpa.restriction.support;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.exitsoft.orm.core.spring.data.jpa.restriction.PredicateSingleValueSupport;

/**
 * 左模糊约束 ( from object o where o.value like '%?') RestrictionName:LLIKE
 * <p>
 * 表达式:LLIKE属性类型_属性名称[_OR_属性名称...]
 * </p>
 * 
 * @author vincent
 *
 */
public class LLikeRestriction extends PredicateSingleValueSupport{

	public final static String RestrictionName = "LLIKE";
	
	
	public String getRestrictionName() {
		return RestrictionName;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Predicate build(Path expression, Object value,CriteriaBuilder builder) {
		
		return builder.like(expression, "%" + value);
	}



}

