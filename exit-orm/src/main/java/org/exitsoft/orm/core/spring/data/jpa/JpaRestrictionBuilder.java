package org.exitsoft.orm.core.spring.data.jpa;

import java.util.HashMap;
import java.util.Map;

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
public class JpaRestrictionBuilder{
	
	private static Map<String, PredicateBuilder> predicateBuilders = new HashMap<String, PredicateBuilder>();
	
	static {
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
		
		predicateBuilders.put(eqRestriction.getRestrictionName(), eqRestriction);
		predicateBuilders.put(neRestriction.getRestrictionName(), neRestriction);
		predicateBuilders.put(geRestriction.getRestrictionName(), geRestriction);
		predicateBuilders.put(inRestriction.getRestrictionName(), inRestriction);
		predicateBuilders.put(gtRestriction.getRestrictionName(), gtRestriction);
		predicateBuilders.put(lLikeRestriction.getRestrictionName(), lLikeRestriction);
		predicateBuilders.put(leRestriction.getRestrictionName(), leRestriction);
		predicateBuilders.put(likeRestriction.getRestrictionName(), likeRestriction);
		predicateBuilders.put(ltRestriction.getRestrictionName(), ltRestriction);
		predicateBuilders.put(rLikeRestriction.getRestrictionName(), rLikeRestriction);
		predicateBuilders.put(notInRestriction.getRestrictionName(), notInRestriction);
	}
	
	public static Predicate getRestriction(PropertyFilter filter,JpaBuilderModel restrictionModel) {
		if (!predicateBuilders.containsKey(filter.getRestrictionName())) {
			throw new IllegalArgumentException("找不到约束名:" + filter.getRestrictionName());
		}
		PredicateBuilder predicateBuilder  = predicateBuilders.get(filter.getRestrictionName());
		return predicateBuilder.build(filter,restrictionModel);
	}

	public static Predicate getRestriction(String propertyName, Object value,String restrictionName,JpaBuilderModel model) {
		if (!predicateBuilders.containsKey(restrictionName)) {
			throw new IllegalArgumentException("找不到约束名:" + restrictionName);
		}
		PredicateBuilder predicateBuilder  = predicateBuilders.get(restrictionName);
		return predicateBuilder.build(propertyName, value, model);
	}
	
	/**
	 * 获取属性名字路径
	 * 
	 * @param propertyName 属性名
	 * @param root Query roots always reference entities
	 * 
	 * @return {@link Path}
	 */
	public static Path<?> getPath(String propertyName,Root<?> root) {
		
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
	
}
