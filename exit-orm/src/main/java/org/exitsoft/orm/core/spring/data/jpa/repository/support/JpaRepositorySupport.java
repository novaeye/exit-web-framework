package org.exitsoft.orm.core.spring.data.jpa.repository.support;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import org.exitsoft.common.utils.ConvertUtils;
import org.exitsoft.common.utils.ReflectionUtils;
import org.exitsoft.orm.annotation.StateDelete;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.spring.data.jpa.repository.BasicJpaRepository;
import org.exitsoft.orm.enumeration.ExecuteMehtod;
import org.exitsoft.orm.strategy.CodeStrategy;
import org.exitsoft.orm.strategy.annotation.ConvertCode;
import org.exitsoft.orm.strategy.annotation.ConvertProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.LockMetadataProvider;
import org.springframework.data.jpa.repository.support.PersistenceProvider;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

public class JpaRepositorySupport<T, ID extends Serializable>  extends SimpleJpaRepository<T, ID> implements BasicJpaRepository<T, ID>{
	
	private final JpaEntityInformation<T, ?> entityInformation;
	private final EntityManager em;
	private final PersistenceProvider provider;

	private LockMetadataProvider lockMetadataProvider;
	
	public JpaRepositorySupport(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
		this.entityInformation = JpaEntityInformationSupport.getMetadata(domainClass, em);
		this.em = em;
		this.provider = PersistenceProvider.fromEntityManager(em);
		
	}
	
	public JpaRepositorySupport(JpaEntityInformation<T, ?> entityInformation, EntityManager em) {
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

	@Override
	public List<T> findAll(List<PropertyFilter> filters) {
		
		return null;
	}

	@Override
	public List<T> findAll(String expression, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> findAll(String expression, String value, String orderBy) {
		// TODO Auto-generated method stub
		return null;
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
	public List<T> findAll(String propertyName, Object value, String orderBy,
			String restrictionName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> findAll(String[] expressions, String[] values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> findAll(String[] expressions, String[] values, String orderBy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<T> findAll(Pageable pageable, List<PropertyFilter> filters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T findOne(List<PropertyFilter> filters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T findOne(String propertyName, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T findOne(String propertyName, Object value, String restrictionName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T findOne(String expression, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T findOne(String[] expressions, String[] values) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
