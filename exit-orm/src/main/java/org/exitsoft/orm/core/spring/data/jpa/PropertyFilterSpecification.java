package org.exitsoft.orm.core.spring.data.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.PropertyFilterUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;

public class PropertyFilterSpecification<T> implements Specification<T> {

	private List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
	
	private String orderBy;
	
	private JpaRestrictionBuilder restrictionBuilder = new JpaRestrictionBuilder();
	
	public PropertyFilterSpecification() {
		
	}
	
	public PropertyFilterSpecification(List<PropertyFilter> filters) {
		this.filters.addAll(filters);
	}
	
	public PropertyFilterSpecification(List<PropertyFilter> filters,String orderBy) {
		this(filters);
		this.orderBy = orderBy;
	}
	
	public PropertyFilterSpecification(PropertyFilter filter) {
		this.filters.add(filter);
	}
	
	public PropertyFilterSpecification(PropertyFilter filter,String orderBy) {
		this(filter);
		this.orderBy = orderBy;
	}
	
	public PropertyFilterSpecification(String expression,String matchValue) {
		this(PropertyFilterUtils.createPropertyFilter(expression, matchValue));
	}
	
	public PropertyFilterSpecification(String expression,String matchValue,String orderBy) {
		this(PropertyFilterUtils.createPropertyFilter(expression, matchValue),orderBy);
	}
	
	public PropertyFilterSpecification(String[] expressions, String[] matchValues) {
		this(PropertyFilterUtils.createPropertyFilters(expressions, matchValues));
	}
	
	public PropertyFilterSpecification(String[] expressions, String[] matchValues,String orderBy) {
		this(PropertyFilterUtils.createPropertyFilters(expressions, matchValues),orderBy);
	}
	
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,CriteriaBuilder builder) {
		
		restrictionBuilder.setSpecificationProperty(root,query,builder);
		
		List<Predicate> list = new ArrayList<Predicate>();
		
		for (PropertyFilter filter : filters) {
			list.add(restrictionBuilder.getRestriction(filter));
		}
		
		restrictionBuilder.clearSpecificationProperty();
		
		if (StringUtils.isNotEmpty(orderBy)) {
			setOrderByToCriteriaQuery(root,builder);
		}
		
		return list.size() > 0 ? builder.and(list.toArray(new Predicate[list.size()])) : null;
		
	}

	private void setOrderByToCriteriaQuery(Root<T> root, CriteriaBuilder builder) {
		String[] orderBys = null;
		if (StringUtils.contains(orderBy,",")) {
			orderBys = StringUtils.splitByWholeSeparator(orderBy, ",");
		} else {
			orderBys = new String[]{orderBy};
		}
		
		for (String ob: orderBys) {
		
			if (StringUtils.contains(ob, "_")) {
				String[] temp = StringUtils.splitByWholeSeparator(ob, "_");
				String property = temp[0];
				String dir = temp[1];
				
				Direction direction = Direction.fromString(dir.toLowerCase());
				
				if (direction == null) {
					throw new IllegalAccessError("orderBy规则错误，当前为:" + dir + " 应该为:ASC或者:DESC");
				}
				if (direction.equals(Direction.ASC)) {
					builder.asc(root.get(property));
				} else {
					builder.desc(root.get(property));
				}
				
			} else {
				builder.desc(root.get(ob));
			}
		}
		//query.orderBy(Order)
		
	}

	public List<PropertyFilter> getFilters() {
		return filters;
	}

	public void setFilters(List<PropertyFilter> filters) {
		this.filters = filters;
	}
	
}
