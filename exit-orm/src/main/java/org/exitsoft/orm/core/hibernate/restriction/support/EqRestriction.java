package org.exitsoft.orm.core.hibernate.restriction.support;

import org.apache.commons.lang3.StringUtils;
import org.exitsoft.orm.core.MatchValue;
import org.exitsoft.orm.core.hibernate.restriction.CriterionSingleValueSupport;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * 等于约束 (from object o where o.value = ?) RestrictionName:EQ
 * <p>
 * 表达式:EQ属性类型_属性名称[_OR_属性名称...]
 * </p>
 * 
 * @author vincent
 *
 */
public class EqRestriction extends CriterionSingleValueSupport {

	public final static String RestrictionName = "EQ";

	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.hibernate.CriterionBuilder#getRestrictionName()
	 */
	public String getRestrictionName() {
		return RestrictionName;
	}
	
	/*
	 * 
	 */
	public MatchValue createMatchValueModel(String matchValue,Class<?> type) {
		
		MatchValue matchValueModel = super.getMatchValue(matchValue, type);
		for (int i = 0; i < matchValueModel.getValues().size(); i++) {
			Object value = matchValueModel.getValues().get(i);
			if (value instanceof String && StringUtils.equals(value.toString(),"null")) {
				matchValueModel.getValues().remove(i);
				matchValueModel.getValues().add(i, null);
			}
		}
		return matchValueModel;
	}
	
	
	public Criterion build(String propertyName, Object value) {
		
		return value == null ? Restrictions.isNull(propertyName) : Restrictions.eq(propertyName, value);
		
	}
	
}
