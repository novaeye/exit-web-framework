package org.exitsoft.orm.core.spring.data.jpa.restriction.support;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.exitsoft.orm.core.spring.data.jpa.restriction.PredicateSingleValueSupport;

/**
 * 模糊约束 ( from object o where o.value like '%?%') RestrictionName:LIKE
 * <p>
 * 表达式:LIKE属性类型_属性名称[_OR_属性名称...]
 * </p>
 * 
 * @author vincent
 *
 */
public class LikeRestriction extends PredicateSingleValueSupport{

	public final static String RestrictionName = "LIKE";
	
	
	public String getRestrictionName() {
		return RestrictionName;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Predicate build(Path expression, Object value,CriteriaBuilder builder) {
		
		return builder.like(expression, "%" + value + "%");
	}

	

}

