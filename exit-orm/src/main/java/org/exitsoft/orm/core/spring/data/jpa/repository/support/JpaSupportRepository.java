package org.exitsoft.orm.core.spring.data.jpa.repository.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Predicate.BooleanOperator;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.exitsoft.common.utils.CollectionUtils;
import org.exitsoft.common.utils.ConvertUtils;
import org.exitsoft.common.utils.ReflectionUtils;
import org.exitsoft.orm.annotation.StateDelete;
import org.exitsoft.orm.core.MatchValue;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.spring.data.jpa.JpaRestrictionBuilder;
import org.exitsoft.orm.core.spring.data.jpa.PropertyFilterSpecification;
import org.exitsoft.orm.core.spring.data.jpa.repository.BasicJpaRepository;
import org.exitsoft.orm.enumeration.ExecuteMehtod;
import org.exitsoft.orm.strategy.CodeStrategy;
import org.exitsoft.orm.strategy.annotation.ConvertCode;
import org.exitsoft.orm.strategy.annotation.ConvertProperty;
import org.hibernate.ejb.criteria.CriteriaBuilderImpl;
import org.hibernate.ejb.criteria.predicate.CompoundPredicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.LockMetadataProvider;
import org.springframework.data.jpa.repository.support.PersistenceProvider;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

public class JpaSupportRepository<T, ID extends Serializable>  extends SimpleJpaRepository<T, ID> implements BasicJpaRepository<T, ID>{
	
	private final JpaEntityInformation<T, ?> entityInformation;
	private final EntityManager em;
	private final PersistenceProvider provider;

	private LockMetadataProvider lockMetadataProvider;
	private JpaRestrictionBuilder jpaRestrictionBuilder = new JpaRestrictionBuilder();
	
	public JpaSupportRepository(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
		this.entityInformation = JpaEntityInformationSupport.getMetadata(domainClass, em);
		this.em = em;
		this.provider = PersistenceProvider.fromEntityManager(em);
		
	}
	
	public JpaSupportRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager em) {
		super(entityInformation, em);

		this.entityInformation = entityInformation;
		this.em = em;
		this.provider = PersistenceProvider.fromEntityManager(em);
	}
	
	
	
	/**
	 * 
	 * 将对象执行转码操作
	 * 
	 * @param source 要转码的对象
	 * @param executeMehtods 在什么方法进行转码
	 */
	protected void convertObject(Object source,ExecuteMehtod...executeMehtods) {
		if (executeMehtods == null) {
			return ;
		}
		
		ConvertCode convertCode = ReflectionUtils.getAnnotation(source.getClass(),ConvertCode.class);
		
		if (convertCode == null) {
			return ;
		}
		
		for (ExecuteMehtod em:executeMehtods) {
			if (convertCode.executeMehtod().equals(em)) {
				for (ConvertProperty convertProperty : convertCode.convertPropertys()) {
					
					CodeStrategy strategy = ReflectionUtils.newInstance(convertProperty.strategyClass());
					
					for (String property :convertProperty.propertyNames()) {
						
						Object fromValue = ReflectionUtils.invokeGetterMethod(source, convertCode.fromProperty());
						Object convertValue = strategy.convertCode(fromValue,property);
						ReflectionUtils.invokeSetterMethod(source, property, convertValue);
						
					}
				}
			}
		}
		
	}
	
	@Override
	@Transactional
	public <S extends T> S save(S entity) {
		
		convertObject(entity,ExecuteMehtod.Save);
		
		if (entityInformation.isNew(entity)) {
			convertObject(entity,ExecuteMehtod.Save,ExecuteMehtod.Insert);
			em.persist(entity);
			return entity;
		} else {
			convertObject(entity,ExecuteMehtod.Save,ExecuteMehtod.Update);
			return em.merge(entity);
		}
	}
	
	@Override
	@Transactional
	public void delete(T entity) {
		StateDelete stateDelete = ReflectionUtils.getAnnotation(entity.getClass(),StateDelete.class);
		if (stateDelete != null) {
			Object value = ConvertUtils.convertToObject(stateDelete.value(), stateDelete.type().getValue());
			ReflectionUtils.invokeSetterMethod(entity, stateDelete.propertyName(), value);
			save(entity);
		} else {
			super.delete(entity);
		}
	}
	
	protected Specification<T> createSpecification(final List<PropertyFilter> filters,String orderBy) {
		
		return new PropertyFilterSpecification<T>(filters,orderBy);
	}
	
	@Override
	public List<T> findAll(List<PropertyFilter> filters) {
		
		return findAll(new PropertyFilterSpecification<T>(filters));
	}

	@Override
	public List<T> findAll(String expression, String value) {
		
		return findAll(new PropertyFilterSpecification<T>(expression,value));
	}

	@Override
	public List<T> findAll(String expression, String value, String orderBy) {
		
		return findAll(new PropertyFilterSpecification<T>(expression,value,orderBy));
	}

	@Override
	public List<T> findAll(String propertyName, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> findAll(String propertyName, Object value, String orderBy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> findAll(String propertyName, Object value, String orderBy,String restrictionName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> findAll(String[] expressions, String[] values) {
		return findAll(new PropertyFilterSpecification<T>(expressions,values));
	}

	@Override
	public List<T> findAll(String[] expressions, String[] values, String orderBy) {
		return findAll(new PropertyFilterSpecification<T>(expressions,values,orderBy));
	}

	@Override
	public Page<T> findAll(Pageable pageable, List<PropertyFilter> filters) {
		return findAll(new PropertyFilterSpecification<T>(filters),pageable);
	}

	@Override
	public T findOne(List<PropertyFilter> filters) {
		
		return findOne(new PropertyFilterSpecification(filters));
	}

	@Override
	public T findOne(String propertyName, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T findOne(String propertyName, Object value, String restrictionName) {
		
		return null;
	}

	@Override
	public T findOne(String expression, String value) {
		return findOne(new PropertyFilterSpecification(expression,value));
	}

	@Override
	public T findOne(String[] expressions, String[] values) {
		return findOne(new PropertyFilterSpecification(expressions,values));
	}
	
}
