package org.exitsoft.orm.core.spring.data.jpa.restriction.support;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.exitsoft.orm.core.MatchValue;
import org.exitsoft.orm.core.spring.data.jpa.restriction.PredicateSingleValueSupport;

/**
 * 等于约束 (from object o where o.value = ?) RestrictionName:EQ
 * <p>
 * 表达式:EQ属性类型_属性名称[_OR_属性名称...]
 * </p>
 * 
 * @author vincent
 *
 */
public class EqRestriction extends PredicateSingleValueSupport{
	
	public final static String RestrictionName = "EQ";
	
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

	public String getRestrictionName() {
		return RestrictionName;
	}

	@SuppressWarnings({ "rawtypes"})
	public Predicate build(Path expression, Object value,CriteriaBuilder builder) {
		return value == null ? builder.isNull(expression) : builder.equal(expression, value);
	}

	
}
