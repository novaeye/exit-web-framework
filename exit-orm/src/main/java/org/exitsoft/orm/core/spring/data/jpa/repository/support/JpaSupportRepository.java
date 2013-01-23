package org.exitsoft.orm.core.spring.data.jpa.repository.support;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import org.exitsoft.common.utils.ConvertUtils;
import org.exitsoft.common.utils.ReflectionUtils;
import org.exitsoft.orm.annotation.StateDelete;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.spring.data.jpa.repository.BasicJpaRepository;
import org.exitsoft.orm.core.spring.data.jpa.specification.Specifications;
import org.exitsoft.orm.core.spring.data.jpa.specification.support.PropertyFilterSpecification;
import org.exitsoft.orm.enumeration.ExecuteMehtod;
import org.exitsoft.orm.strategy.CodeStrategy;
import org.exitsoft.orm.strategy.annotation.ConvertCode;
import org.exitsoft.orm.strategy.annotation.ConvertProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@link BasicJpaRepository}接口实现类，并在{@link SimpleJpaRepository}基础上扩展,包含对{@link PropertyFilter}的支持。或其他查询的支持,
 * 重写了{@link SimpleJpaRepository#save(Object)}和{@link SimpleJpaRepository#delete(Object)}方法，支持@StateDelete注解和@ConvertProperty注解
 * 
 * @author vincent
 *
 * @param <T> ORM对象
 * @param <ID> 主键Id类型
 */
@SuppressWarnings("unchecked")
public class JpaSupportRepository<T, ID extends Serializable>  extends SimpleJpaRepository<T, ID> implements BasicJpaRepository<T, ID>{
	
	private EntityManager entityManager;
	private JpaEntityInformation<T, ?> entityInformation;
	
	public JpaSupportRepository(Class<T> domainClass, EntityManager entityManager) {
		super(domainClass, entityManager);
		this.entityManager = entityManager;
	}
	
	public JpaSupportRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager em) {
		super(entityInformation, em);
		this.entityInformation = entityInformation;
		this.entityManager = em;
	}
	
	/**
	 * 
	 * 将对象执行转码操作
	 * 
	 * @param source 要转码的对象
	 * @param executeMehtods 在什么方法进行转码
	 */
	protected void convertObject(Object source,ExecuteMehtod...executeMethods) {
		if (executeMethods == null) {
			return ;
		}
		
		ConvertCode convertCode = ReflectionUtils.getAnnotation(source.getClass(),ConvertCode.class);
		
		if (convertCode == null) {
			return ;
		}
		
		for (ExecuteMehtod em:executeMethods) {
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
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.jpa.repository.support.SimpleJpaRepository#save(S)
	 */
	@Transactional
	public <S extends T> S save(S entity) {
		
		if (entityInformation.isNew(entity)) {
			convertObject(entity,ExecuteMehtod.Save,ExecuteMehtod.Insert);
			entityManager.persist(entity);
			return entity;
		} else {
			convertObject(entity,ExecuteMehtod.Save,ExecuteMehtod.Update);
			return entityManager.merge(entity);
		}
	}
	
	 /*
	  * (non-Javadoc)
	  * @see org.springframework.data.jpa.repository.support.SimpleJpaRepository#delete(java.lang.Object)
	  */
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
	
	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.spring.data.jpa.repository.BasicJpaRepository#findAll(java.util.List)
	 */
	public List<T> findAll(List<PropertyFilter> filters) {
		
		return findAll(filters,(Sort)null);
	}

	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.spring.data.jpa.repository.BasicJpaRepository#findAll(java.util.List, org.springframework.data.domain.Sort)
	 */
	public List<T> findAll(List<PropertyFilter> filters, Sort sort) {
		
		return findAll(new PropertyFilterSpecification<T>(filters),sort);
	}

	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.spring.data.jpa.repository.BasicJpaRepository#findAll(java.lang.String, java.lang.String)
	 */
	public List<T> findAll(String expression, String value) {
		
		return findAll(expression,value,(Sort)null);
	}

	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.spring.data.jpa.repository.BasicJpaRepository#findAll(java.lang.String, java.lang.String, org.springframework.data.domain.Sort)
	 */
	public List<T> findAll(String expression, String value, Sort sort) {
		
		return findAll(new PropertyFilterSpecification<T>(expression, value),sort);
	}

	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.spring.data.jpa.repository.BasicJpaRepository#findAll(java.lang.String[], java.lang.String[])
	 */
	public List<T> findAll(String[] expressions, String[] values) {
		return findAll(expressions,values,(Sort)null);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.spring.data.jpa.repository.BasicJpaRepository#findAll(java.lang.String[], java.lang.String[], org.springframework.data.domain.Sort)
	 */
	public List<T> findAll(String[] expressions, String[] values, Sort sort) {
		return findAll(Specifications.getByExpression(expressions, values),sort);
	}

	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.spring.data.jpa.repository.BasicJpaRepository#findAll(org.springframework.data.domain.Pageable, java.util.List)
	 */
	public Page<T> findAll(Pageable pageable, List<PropertyFilter> filters) {
		return findAll(Specifications.getByPropertyFilter(filters),pageable);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.spring.data.jpa.repository.BasicJpaRepository#findOne(java.util.List)
	 */
	public T findOne(List<PropertyFilter> filters) {
		
		return findOne(Specifications.getByPropertyFilter(filters));
	}

	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.spring.data.jpa.repository.BasicJpaRepository#findOne(java.lang.String, java.lang.String)
	 */
	public T findOne(String expression, String value) {
		
		return findOne(Specifications.getByExpression(expression, value));
	}

	/*
	 * (non-Javadoc)
	 * @see org.exitsoft.orm.core.spring.data.jpa.repository.BasicJpaRepository#findOne(java.lang.String[], java.lang.String[])
	 */
	public T findOne(String[] expressions, String[] values) {
		return findOne(Specifications.getByExpression(expressions, values));
	}
	
}
