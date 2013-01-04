package org.exitsoft.orm.core.hibernate.restriction.support;

import org.exitsoft.orm.core.hibernate.restriction.CriterionSingleValueSupport;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 * 右模糊约束 ( from object o where o.value like '?%') RestrictionName:RLIKE
 * <p>
 * 表达式:RLIKE属性类型_属性名称[_OR_属性名称...]
 * </p>
 * 
 * @author vincent
 *
 */
public class RLikeRestriction extends CriterionSingleValueSupport{
	
	public final static String RestrictionName = "RLIKE";
	
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
		
		return Restrictions.like(propertyName, value.toString(), MatchMode.START);
	}

}

