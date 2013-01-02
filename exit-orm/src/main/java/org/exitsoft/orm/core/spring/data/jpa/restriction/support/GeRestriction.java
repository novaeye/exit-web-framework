package org.exitsoft.orm.core.spring.data.jpa.restriction.support;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.exitsoft.orm.core.spring.data.jpa.restriction.PredicateSingleValueSupport;


/**
 * 大于等于约束 (from object o where o.value >= ?)RestrictionName:GE
 * <p>
 * 表达式:GE属性类型_属性名称[_OR_属性名称...]
 * </p>
 * 
 * @author vincent
 *
 */
public class GeRestriction extends PredicateSingleValueSupport{

	public final static String RestrictionName = "GE";
	
	
	public String getRestrictionName() {
		
		return RestrictionName;
	}

	public Predicate build(Path expression, Object value,CriteriaBuilder builder) {
		
		return builder.greaterThanOrEqualTo(expression, (Comparable) value);
	}

}
