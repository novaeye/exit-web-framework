package org.exitsoft.orm.core.hibernate.restriction.support;

import org.exitsoft.orm.core.hibernate.restriction.CriterionMultipleValueSupport;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * 不包含约束 (from object o where o.value not in (?,?,?,?,?))RestrictionName:NIN
 * <p>
 * 表达式:NIN属性类型_属性名称[_OR_属性名称...]
 * </p>
 * 
 * @author vincent
 *
 */
public class NinRestriction extends CriterionMultipleValueSupport{

	public final static String RestrictionName = "NIN";
	
	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.hibernate.CriterionBuilder#getRestrictionName()
	 */
	public String getRestrictionName() {
		
		return RestrictionName;
	}

	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.hibernate.restriction.CriterionMultipleValueSupport#buildRestriction(java.lang.String, java.lang.Object[])
	 */
	public Criterion buildRestriction(String propertyName, Object[] values) {
		
		return Restrictions.not(Restrictions.in(propertyName, values));
	}

}

