package org.exitsoft.orm.core.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.hibernate.restriction.support.EqRestriction;
import org.exitsoft.orm.core.hibernate.restriction.support.GeRestriction;
import org.exitsoft.orm.core.hibernate.restriction.support.GtRestriction;
import org.exitsoft.orm.core.hibernate.restriction.support.InRestriction;
import org.exitsoft.orm.core.hibernate.restriction.support.LLikeRestriction;
import org.exitsoft.orm.core.hibernate.restriction.support.LeRestriction;
import org.exitsoft.orm.core.hibernate.restriction.support.LikeRestriction;
import org.exitsoft.orm.core.hibernate.restriction.support.LtRestriction;
import org.exitsoft.orm.core.hibernate.restriction.support.NeRestriction;
import org.exitsoft.orm.core.hibernate.restriction.support.NinRestriction;
import org.exitsoft.orm.core.hibernate.restriction.support.RLikeRestriction;
import org.hibernate.criterion.Criterion;

/**
 * Hibernate约束捆绑者，帮助HibernateDao对buildCriterion方法创建相对的Criterion对象给Hibernate查询
 * 
 * @author vincent
 *
 */
public class HibernateRestrictionBuilder {
	
	private static Map<String, CriterionBuilder> criterionBuilders = new HashMap<String, CriterionBuilder>();
	
	static {
		
		CriterionBuilder eqRestriction = new EqRestriction();
		CriterionBuilder neRestriction = new NeRestriction();
		CriterionBuilder geRestriction = new GeRestriction();
		CriterionBuilder gtRestriction = new GtRestriction();
		CriterionBuilder inRestriction = new InRestriction();
		CriterionBuilder lLikeRestriction = new LLikeRestriction();
		CriterionBuilder leRestriction = new LeRestriction();
		CriterionBuilder likeRestriction = new LikeRestriction();
		CriterionBuilder ltRestriction = new LtRestriction();
		CriterionBuilder notInRestriction = new NinRestriction();
		CriterionBuilder rLikeRestriction = new RLikeRestriction();
		
		criterionBuilders.put(eqRestriction.getRestrictionName(), eqRestriction);
		criterionBuilders.put(neRestriction.getRestrictionName(), neRestriction);
		criterionBuilders.put(geRestriction.getRestrictionName(), geRestriction);
		criterionBuilders.put(inRestriction.getRestrictionName(), inRestriction);
		criterionBuilders.put(gtRestriction.getRestrictionName(), gtRestriction);
		criterionBuilders.put(lLikeRestriction.getRestrictionName(), lLikeRestriction);
		criterionBuilders.put(leRestriction.getRestrictionName(), leRestriction);
		criterionBuilders.put(likeRestriction.getRestrictionName(), likeRestriction);
		criterionBuilders.put(ltRestriction.getRestrictionName(), ltRestriction);
		criterionBuilders.put(rLikeRestriction.getRestrictionName(), rLikeRestriction);
		criterionBuilders.put(notInRestriction.getRestrictionName(), notInRestriction);
	}
	
	public static Criterion getRestriction(PropertyFilter filter) {
		
		if (!criterionBuilders.containsKey(filter.getRestrictionName())) {
			throw new IllegalArgumentException("找不到约束名:" + filter.getRestrictionName());
		}
		
		CriterionBuilder criterionBuilder = criterionBuilders.get(filter.getRestrictionName());
		return criterionBuilder.build(filter);
	}

	
	public static Criterion getRestriction(String propertyName, Object value,String restrictionName) {
		
		if (!criterionBuilders.containsKey(restrictionName)) {
			throw new IllegalArgumentException("找不到约束名:" + restrictionName);
		}
		
		CriterionBuilder restriction = criterionBuilders.get(restrictionName);
		return restriction.build(propertyName, value);
	}
	
	
	
}
