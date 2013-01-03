package org.exitsoft.orm.core.spring.data.jpa;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.PropertyFilterBuilder;
import org.exitsoft.orm.core.spring.data.jpa.restriction.support.EqRestriction;
import org.exitsoft.orm.core.spring.data.jpa.restriction.support.GeRestriction;
import org.exitsoft.orm.core.spring.data.jpa.restriction.support.GtRestriction;
import org.exitsoft.orm.core.spring.data.jpa.restriction.support.InRestriction;
import org.exitsoft.orm.core.spring.data.jpa.restriction.support.LLikeRestriction;
import org.exitsoft.orm.core.spring.data.jpa.restriction.support.LeRestriction;
import org.exitsoft.orm.core.spring.data.jpa.restriction.support.LikeRestriction;
import org.exitsoft.orm.core.spring.data.jpa.restriction.support.LtRestriction;
import org.exitsoft.orm.core.spring.data.jpa.restriction.support.NeRestriction;
import org.exitsoft.orm.core.spring.data.jpa.restriction.support.NinRestriction;
import org.exitsoft.orm.core.spring.data.jpa.restriction.support.RLikeRestriction;

/**
 * jpa约束捆绑者，，将所有的{@link PredicateBuilder}实现类添加到{@link PropertyFilterBuilder#getRestrictionsMap()}中，让
 * 
 * @author vincent
 *
 */
public class JpaRestrictionBuilder extends PropertyFilterBuilder<PredicateBuilder, Predicate>{
	
	private Root<?> root;
	private CriteriaQuery<?> query;
	private CriteriaBuilder builder;
	
	public JpaRestrictionBuilder() {
		PredicateBuilder eqRestriction = new EqRestriction();
		PredicateBuilder neRestriction = new NeRestriction();
		PredicateBuilder geRestriction = new GeRestriction();
		PredicateBuilder gtRestriction = new GtRestriction();
		PredicateBuilder inRestriction = new InRestriction();
		PredicateBuilder lLikeRestriction = new LLikeRestriction();
		PredicateBuilder leRestriction = new LeRestriction();
		PredicateBuilder likeRestriction = new LikeRestriction();
		PredicateBuilder ltRestriction = new LtRestriction();
		PredicateBuilder notInRestriction = new NinRestriction();
		PredicateBuilder rLikeRestriction = new RLikeRestriction();
		
		getRestrictionsMap().put(eqRestriction.getRestrictionName(), eqRestriction);
		getRestrictionsMap().put(neRestriction.getRestrictionName(), neRestriction);
		getRestrictionsMap().put(geRestriction.getRestrictionName(), geRestriction);
		getRestrictionsMap().put(inRestriction.getRestrictionName(), inRestriction);
		getRestrictionsMap().put(gtRestriction.getRestrictionName(), gtRestriction);
		getRestrictionsMap().put(lLikeRestriction.getRestrictionName(), lLikeRestriction);
		getRestrictionsMap().put(leRestriction.getRestrictionName(), leRestriction);
		getRestrictionsMap().put(likeRestriction.getRestrictionName(), likeRestriction);
		getRestrictionsMap().put(ltRestriction.getRestrictionName(), ltRestriction);
		getRestrictionsMap().put(rLikeRestriction.getRestrictionName(), rLikeRestriction);
		getRestrictionsMap().put(notInRestriction.getRestrictionName(), notInRestriction);
	}
	
	public JpaRestrictionBuilder(Root<?> root, CriteriaQuery<?> query,CriteriaBuilder builder) {
		this();
		this.root = root;
		this.query = query;
		this.builder = builder;
	}

	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.PropertyFilterBuilder#getRestriction(org.exitsoft.orm.core.PropertyFilter)
	 */
	public Predicate getRestriction(PropertyFilter filter) {
		if (!getRestrictionsMap().containsKey(filter.getRestrictionName())) {
			throw new IllegalArgumentException("找不到约束名:" + filter.getRestrictionName());
		}
		PredicateBuilder predicateBuilder  = getRestrictionsMap().get(filter.getRestrictionName());
		return predicateBuilder.build(filter,root,query,builder);
	}

	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.PropertyFilterBuilder#getRestriction(java.lang.String, java.lang.Object, java.lang.String)
	 */
	public Predicate getRestriction(String propertyName, Object value,String restrictionName) {
		if (!getRestrictionsMap().containsKey(restrictionName)) {
			throw new IllegalArgumentException("找不到约束名:" + restrictionName);
		}
		PredicateBuilder predicateBuilder  = getRestrictionsMap().get(restrictionName);
		return predicateBuilder.build(getPath(propertyName, root), value, builder);
	}
	
	/**
	 * 获取属性名字路径
	 * 
	 * @param propertyName 属性名
	 * @param root Query roots always reference entities
	 * 
	 * @return {@link Path}
	 */
	protected Path<?> getPath(String propertyName,Root<?> root) {
		
		Path<?> path = null;
		
		if (StringUtils.contains(propertyName, ".")) {
			String[] propertys = StringUtils.splitByWholeSeparator(propertyName, ".");
			path = root.get(propertys[0]);
			for (int i = 1; i < propertys.length; i++) {
				path = path.get(propertys[i]);
			}
		} else {
			path = root.get(propertyName);
		}
		
		return path;
	}
	
	public void setSpecificationProperty(Root<?> root, CriteriaQuery<?> query,CriteriaBuilder builder) {
		this.root = root;
		this.builder = builder;
		this.query = query;
	}

	public void clearSpecificationProperty() {
		this.root = null;
		this.builder = null;
		this.query = null;
	}
	
}
