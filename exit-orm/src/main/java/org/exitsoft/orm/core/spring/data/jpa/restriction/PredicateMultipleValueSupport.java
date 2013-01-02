package org.exitsoft.orm.core.spring.data.jpa.restriction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.exitsoft.common.utils.ConvertUtils;
import org.exitsoft.orm.core.PropertyFilter;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.Assert;

/**
 * 对{@link PropertyFilter#getMatchValue()}的特殊情况值做处理，例如 in, not in, between的多值情况,
 * 该类值处理一种情况
 * 
 * <p>
 * 	例如:
 * </p>
 * 
 * INI_property = "1,2,3,4";
 * <p>
 * 会产生的sql为: property in (1,2,3,4)
 * 
 * @author vincent
 *
 */
public abstract class PredicateMultipleValueSupport extends PredicateSingleValueSupport{
	
	public Object convertMatchValue(String value, Class<?> type) {
		Assert.notNull(value,"值不能为空");
		String[] result = StringUtils.splitByWholeSeparator(value, getAndValueSeparator());
		
		return  ConvertUtils.convertToObject(result,type);
	}
	
	
	public Predicate build(PropertyFilter filter, Root<?> root,CriteriaQuery<?> query, CriteriaBuilder builder) {
		Object value = convertMatchValue(filter.getMatchValue(), filter.getPropertyType());
		Predicate predicate = null;
		
		if (filter.hasMultiplePropertyNames()) {
			Predicate orDisjunction = builder.disjunction();
			for (String propertyName:filter.getPropertyNames()) {
				orDisjunction.getExpressions().add(build(root.get(propertyName),value,builder));
			}
			predicate = orDisjunction;
		} else {
			predicate = build(root.get(filter.getSinglePropertyName()),value,builder);
		}
		
		return predicate;
	}
	
	@Override
	public Predicate build(Path<?> expression, Object value,CriteriaBuilder builder) {
		
		return buildRestriction(expression,(Object[])value,builder);
	}
	
	public abstract Predicate buildRestriction(Path<?> expression,Object[] values,CriteriaBuilder builder);
}
