package org.exitsoft.orm.core.hibernate.restriction.support;

import org.exitsoft.orm.core.hibernate.restriction.CriterionSingleValueSupport;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * 小于约束 ( from object o where o.value < ?) RestrictionName:LT
 * <p>
 * 表达式:LT属性类型_属性名称[_OR_属性名称...]
 * </p>
 * 
 * @author vincent
 *
 */
public class LtRestriction extends CriterionSingleValueSupport{

	public final static String RestrictionName = "LT";
	
	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.hibernate.CriterionBuilder#getRestrictionName()
	 */
	public String getRestrictionName() {
		return RestrictionName;
	}

	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.hibernate.CriterionBuilder#build(java.lang.String, java.lang.Object)
	 */
	public Criterion build(String propertyName, Object value) {
		return Restrictions.lt(propertyName, value);
	}

}
