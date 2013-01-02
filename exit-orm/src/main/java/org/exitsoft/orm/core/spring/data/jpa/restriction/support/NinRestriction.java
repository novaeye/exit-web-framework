package org.exitsoft.orm.core.spring.data.jpa.restriction.support;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.exitsoft.orm.core.spring.data.jpa.restriction.PredicateMultipleValueSupport;

/**
 * 不包含约束 (from object o where o.value not in (?,?,?,?,?))RestrictionName:NIN
 * <p>
 * 表达式:NIN属性类型_属性名称[_OR_属性名称...]
 * </p>
 * 
 * @author vincent
 *
 */
public class NinRestriction extends PredicateMultipleValueSupport{

	public final static String RestrictionName = "NIN";
	
	
	public String getRestrictionName() {
		
		return RestrictionName;
	}

	public Predicate buildRestriction(Path<?> expression, Object[] values,CriteriaBuilder builder) {

		return builder.not(expression.in(values));
	}




}

