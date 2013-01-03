package org.exitsoft.orm.core.spring.data.jpa.restriction.support;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.exitsoft.orm.core.spring.data.jpa.restriction.PredicateMultipleValueSupport;

/**
 * 包含约束 (from object o where o.value in (?,?,?,?,?))RestrictionName:IN
 * <p>
 * 表达式:IN属性类型_属性名称[_OR_属性名称...]
 * </p>
 * 
 * @author vincent
 *
 */
public class InRestriction extends PredicateMultipleValueSupport{

	public final static String RestrictionName = "IN";
	
	
	public String getRestrictionName() {
		return RestrictionName;
	}
	
	@SuppressWarnings("rawtypes")
	public Predicate buildRestriction(Path expression, Object[] values,CriteriaBuilder builder) {
		return expression.in(values);
	}
	
	

}

