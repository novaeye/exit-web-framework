package org.exitsoft.orm.core.hibernate.restriction.support;

import org.exitsoft.orm.core.hibernate.restriction.CriterionSingleValueSupport;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * 大于等于约束 (from object o where o.value >= ?)RestrictionName:GE
 * <p>
 * 表达式:GE属性类型_属性名称[_OR_属性名称...]
 * </p>
 * 
 * @author vincent
 *
 */
public class GeRestriction extends CriterionSingleValueSupport{
	
	public final static String RestrictionName = "GE";
	
	
	public String getRestrictionName() {
		
		return RestrictionName;
	}

	
	public Criterion build(String propertyName, Object value) {
		
		return Restrictions.ge(propertyName, value);
	}

}
