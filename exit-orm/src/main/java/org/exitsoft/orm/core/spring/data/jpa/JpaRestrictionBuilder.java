package org.exitsoft.orm.core.spring.data.jpa;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
	
	public Predicate getRestriction(PropertyFilter filter) {
		if (!getRestrictionsMap().containsKey(filter.getRestrictionName())) {
			throw new IllegalArgumentException("找不到约束名:" + filter.getRestrictionName());
		}
		PredicateBuilder predicateBuilder  = getRestrictionsMap().get(filter.getRestrictionName());
		return predicateBuilder.build(filter,root,query,builder);
	}

	public Predicate getRestriction(String propertyName, Object value,String restrictionName) {
		if (!getRestrictionsMap().containsKey(restrictionName)) {
			throw new IllegalArgumentException("找不到约束名:" + restrictionName);
		}
		PredicateBuilder predicateBuilder  = getRestrictionsMap().get(restrictionName);
		return predicateBuilder.build(root.get(propertyName), value, builder);
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
