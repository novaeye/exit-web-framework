package org.exitsoft.orm.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.exitsoft.common.unit.Fixtures;
import org.exitsoft.orm.core.Page;
import org.exitsoft.orm.core.PageRequest;
import org.exitsoft.orm.core.PageRequest.Sort;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.PropertyFilters;
import org.exitsoft.orm.core.RestrictionNames;
import org.exitsoft.orm.core.hibernate.support.HibernateSupportDao;
import org.exitsoft.orm.test.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

/**
 * 测试HibernateSuperDao的查询方法
 * 
 * @author vincent
 *
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
public class TestHibernateSupportDao {

	private HibernateSupportDao<User, String> dao;
	
	@Autowired
	private DataSource dataSource;
	
	private static DataSource dataSourceHandler;
	
	@Before
	public void install() throws Exception {
		if (dataSourceHandler == null) {
			Fixtures.loadData(dataSource, "/sample-data.xml");
			dataSourceHandler = dataSource;
		}

	}
	
	@AfterClass
	public static void uninstall() throws Exception {
		Fixtures.deleteData(dataSourceHandler, "/sample-data.xml");
		dataSourceHandler = null;
	}
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		dao = new HibernateSupportDao<User, String>(User.class);
		dao.setSessionFactory(sessionFactory);
	}
	
	@Test
	public void testFind() {
		List<User> userList = new ArrayList<User>();
		
		userList = dao.findByQuery("QueryUserResourceJpa", "admin");
		Assert.assertEquals(userList.size(), 1);
		
		userList = dao.findByQuery("QueryUserResource", "admin");
		Assert.assertEquals(userList.size(), 1);
		
		userList = dao.findByQuery("from User u where u.loginName=?", "admin");
		Assert.assertEquals(userList.size(), 1);
		
		userList = dao.findByQuery("from User u where u.loginName=?1", "admin");
		Assert.assertEquals(userList.size(), 1);
		
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("loginName", "admin");
		
		userList = dao.findByQuery("from User u where u.loginName = :loginName", values);
		Assert.assertEquals(userList.size(), 1);
		
	}
	
	@Test
	public void testFindByProperty() {
		List<User> userList = new ArrayList<User>();
		
		userList = dao.findByProperty("loginName", "admin");
		Assert.assertEquals(userList.size(),1);
		
		userList = dao.findByProperty("state", 1,RestrictionNames.NE);
		Assert.assertEquals(userList.size(),0);
		
	}
	
	@Test
	public void testFindUnique() {
		User user = new User();
		
		user = dao.findUniqueByCriterion(new Criterion[]{Restrictions.eq("loginName", "admin")});
		Assert.assertEquals(user.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0002");
		
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters = Lists.newArrayList(PropertyFilters.build("EQS_loginName", "admin"));
		
		user = dao.findUniqueByPropertyFilter(filters);
		Assert.assertEquals(user.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0002");
		
		filters = Lists.newArrayList(PropertyFilters.build("EQS_name","系统"));
		
		user = dao.findUniqueByQuery("QueryUserResourceJpa", "admin");
		Assert.assertEquals(user.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0002");
		
		user = dao.findUniqueByQuery("QueryUserResource", "admin");
		Assert.assertEquals(user.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0002");
		
		user = dao.findUniqueByQuery("from User u where u.loginName=?", "admin");
		Assert.assertEquals(user.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0002");
		
		user = dao.findUniqueByQuery("from User u where u.loginName=?1", "admin");
		Assert.assertEquals(user.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0002");
		
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("loginName", "admin");
		
		user = dao.findUniqueByQuery("from User u where u.loginName = :loginName", values);
		Assert.assertEquals(user.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0002");
		
	}
	
	@Test
	public void testFindUniqueByProperty() {
		User user = new User();
		
		user = dao.findUniqueByProperty("loginName", "admin");
		Assert.assertEquals(user.getId(),"SJDK3849CKMS3849DJCK2039ZMSK0002");
		
		
	}
	
	@Test
	public void testFindPage() {
		PageRequest request = new PageRequest(1,2);
		request.setOrderBy("loginName");
		request.setOrderDir(Sort.DESC);
		Page<User> user = new Page<User>();
		
		user = dao.findPage(request, "from User u");
		Assert.assertEquals(user.getResult().size(), 2);
		Assert.assertEquals(user.getTotalPages(), 4);
		Assert.assertEquals(user.getTotalItems(), 8);
		
		List<PropertyFilter> filters = Lists.newArrayList(PropertyFilters.build("EQI_state", "1"));
		
		user = dao.findPage(request, filters);
		Assert.assertEquals(user.getResult().size(), 2);
		Assert.assertEquals(user.getTotalPages(), 4);
		Assert.assertEquals(user.getTotalItems(), 8);
		
		user = dao.findPageByNamedQuery(request, "QueryUserResource", "vincent");
		Assert.assertEquals(user.getResult().size(), 1);
		Assert.assertEquals(user.getTotalPages(), 1);
		Assert.assertEquals(user.getTotalItems(), 1);
		
		user = dao.findPageByNamedQuery(request, "QueryUserResourceJpa", "vincent");
		Assert.assertEquals(user.getResult().size(), 1);
		Assert.assertEquals(user.getTotalPages(), 1);
		Assert.assertEquals(user.getTotalItems(), 1);
		
	}
}
