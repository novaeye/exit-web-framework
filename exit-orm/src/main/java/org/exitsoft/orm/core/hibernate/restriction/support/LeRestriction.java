package org.exitsoft.orm.core.hibernate.restriction.support;

import org.exitsoft.orm.core.RestrictionNames;
import org.exitsoft.orm.core.hibernate.restriction.CriterionSingleValueSupport;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * 小于等于约束 ( from object o where o.value <= ?) RestrictionName:LR
 * <p>
 * 表达式:LE属性类型_属性名称[_OR_属性名称...]
 * </p>
 * 
 * @author vincent
 *
 */
public class LeRestriction extends CriterionSingleValueSupport{
	
	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.hibernate.CriterionBuilder#getRestrictionName()
	 */
	public String getRestrictionName() {
		
		return RestrictionNames.LE;
	}

	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.hibernate.CriterionBuilder#build(java.lang.String, java.lang.Object)
	 */
	public Criterion build(String propertyName, Object value) {
		return Restrictions.le(propertyName, value);
	}

}
