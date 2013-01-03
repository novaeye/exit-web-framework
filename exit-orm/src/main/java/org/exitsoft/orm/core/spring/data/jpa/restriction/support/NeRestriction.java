package org.exitsoft.orm.core.spring.data.jpa.restriction.support;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;


/**
 * 不等于约束 ( from object o where o.value <> ?) RestrictionName:NE
 * <p>
 * 表达式:NE属性类型_属性名称[_OR_属性名称...]
 * </p>
 * 
 * @author vincent
 *
 */
public class NeRestriction extends EqRestriction{
	
	public final static String RestrictionName = "NE";


	
	public String getRestrictionName() {
		
		return RestrictionName;
	}

	@SuppressWarnings("rawtypes")
	public Predicate build(Path expression, Object value,CriteriaBuilder builder) {
		
		return value == null ? builder.isNotNull(expression) : builder.notEqual(expression, value);
	}
	
}
