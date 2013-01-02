package org.exitsoft.orm.core.spring.data.jpa.restriction.support;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.exitsoft.orm.core.spring.data.jpa.restriction.PredicateSingleValueSupport;

/**
 * 小于约束 ( from object o where o.value < ?) RestrictionName:LT
 * <p>
 * 表达式:LT属性类型_属性名称[_OR_属性名称...]
 * </p>
 * 
 * @author vincent
 *
 */
public class LtRestriction extends PredicateSingleValueSupport{

	public final static String RestrictionName = "LT";
	
	
	public String getRestrictionName() {
		return RestrictionName;
	}


	@Override
	public Predicate build(Path expression, Object value,CriteriaBuilder builder) {
		return builder.lessThan(expression, (Comparable)value);
	}


}
