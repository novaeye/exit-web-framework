package org.exitsoft.showcase.test.manager;

import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.exitsoft.common.unit.Fixtures;
import org.hibernate.SessionFactory;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 业务单元测试基类
 * 
 * @author vincent
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-core-test.xml")
public class ManagerTestCaseSupport {
	
	private DataSource dataSource;
	
	private NamedParameterJdbcTemplate jdbcTemplate;

	private HibernateEntityManagerFactory entityManagerFactory;
	
	public NamedParameterJdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	
	public HibernateEntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	@Autowired
	public void setEntityManagerFactory(HibernateEntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	@Autowired
	public void setDataSource(DataSource dataSource) throws Exception {
		this.dataSource = dataSource;
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public EntityManager getTransactionalEntityManager() {
		return EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
	}
	/**
	 * 通过表名计算出表中的总记录数
	 * 
	 * @param tableName 表名
	 * 
	 * @return int
	 */
	protected int countRowsInTable(String tableName) {
		return jdbcTemplate.queryForInt("SELECT COUNT(0) FROM " + tableName,new HashMap<String, Object>());
	}
	
	/**
	 * 
	 * 每个单元测试用例开始先把模拟数据加载到dataSource中
	 * 
	 * @throws Exception
	 */
	@Before
	public void install() throws Exception {
		Fixtures.reloadData(dataSource, "/sample-data.xml");
	}

	@Test
	public void emptyTestMethod() {
		
	}
	
}
