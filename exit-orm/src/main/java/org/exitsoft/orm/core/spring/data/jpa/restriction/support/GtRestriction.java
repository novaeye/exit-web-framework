package org.exitsoft.orm.core.spring.data.jpa.restriction.support;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.exitsoft.orm.core.spring.data.jpa.restriction.PredicateSingleValueSupport;

/**
 * 大于约束 (from object o where o.value > ?)RestrictionName:GT
 * <p>
 * 表达式:GT属性类型_属性名称[_OR_属性名称...]
 * </p>
 * 
 * @author vincent
 *
 */
public class GtRestriction extends PredicateSingleValueSupport{

	public final static String RestrictionName = "GT";
	
	
	public String getRestrictionName() {
		
		return RestrictionName;
	}


	@Override
	public Predicate build(Path expression, Object value,CriteriaBuilder builder) {
		
		return builder.greaterThan(expression,(Comparable) value);
	}

	
}
