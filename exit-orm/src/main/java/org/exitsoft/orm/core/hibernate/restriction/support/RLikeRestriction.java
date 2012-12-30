package org.exitsoft.orm.core.hibernate.restriction.support;

import org.exitsoft.orm.core.hibernate.restriction.PropertyValueRestrictionSupport;
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
public class RLikeRestriction extends PropertyValueRestrictionSupport{
	
	public final static String RestrictionName = "RLIKE";
	
	
	public String getRestrictionName() {
		return RestrictionName;
	}

	
	public Criterion build(String propertyName, Object value) {
		
		return Restrictions.like(propertyName, value.toString(), MatchMode.START);
	}

}

