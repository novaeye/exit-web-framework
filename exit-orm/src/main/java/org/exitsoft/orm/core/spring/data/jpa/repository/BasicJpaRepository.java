package org.exitsoft.orm.core.spring.data.jpa.repository;

import java.io.Serializable;
import java.util.List;

import org.exitsoft.orm.core.PropertyFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface BasicJpaRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T>{
	
	public List<T> findAll(List<PropertyFilter> filters);
	
	public List<T> findAll(String expression,String value);
	
	public List<T> findAll(String expression,String value,String orderBy);
	
	public List<T> findAll(String propertyName,Object value);
	
	public List<T> findAll(String propertyName,Object value, String orderBy);
	
	public List<T> findAll(String propertyName,Object value, String orderBy,String restrictionName);
	
	public List<T> findAll(String[] expressions,String[] values);
	
	public List<T> findAll(String[] expressions,String[] values, String orderBy);
	
	public Page<T> findAll(Pageable pageable, List<PropertyFilter> filters);
	
	public T findOne(List<PropertyFilter> filters);
	
	public T findOne(String propertyName,Object value);
	
	public T findOne(String propertyName,Object value,String restrictionName);
	
	public T findOne(String expression,String value);
	
	public T findOne(String[] expressions,String[] values);
	
}
