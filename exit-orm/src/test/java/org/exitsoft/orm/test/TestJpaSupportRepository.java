package org.exitsoft.orm.test;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.exitsoft.common.unit.Fixtures;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.PropertyFilters;
import org.exitsoft.orm.core.spring.data.jpa.repository.support.JpaSupportRepository;
import org.exitsoft.orm.core.spring.data.jpa.specification.support.PropertySpecification;
import org.exitsoft.orm.test.entity.User;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@TransactionConfiguration(transactionManager="jpaTransactionManager")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
public class TestJpaSupportRepository {
	
	private JpaSupportRepository<User, String> dao;
	
	@Autowired
	private DataSource dataSource;

	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	private static DataSource dataSourceHandler;
	
	@Before
	public void install() throws Exception {
		if (dataSourceHandler == null) {
			Fixtures.loadData(dataSource, "/sample-data.xml");
			dataSourceHandler = dataSource;
		}
		dao = new JpaSupportRepository<User, String>(User.class, entityManagerFactory.createEntityManager());
	}
	
	@AfterClass
	public static void uninstall() throws Exception {
		Fixtures.deleteData(dataSourceHandler, "/sample-data.xml");
		dataSourceHandler = null;
	}
	
	@Test
	public void testAllRestriction() {
		List<User> userList = new ArrayList<User>();
		
		userList = dao.findAll("EQD_createTime", "2012-08-12");
		Assert.assertEquals(userList.size(), 8);
		
		userList = dao.findAll("EQS_wubiCode", null);
		Assert.assertEquals(userList.size(), 1);
		
		userList = dao.findAll("EQS_wubiCode", "");
		Assert.assertEquals(userList.size(),6);
		
		userList = dao.findAll("EQS_wubiCode", "123");
		Assert.assertEquals(userList.size(), 1);
		
		userList = dao.findAll("NES_wubiCode", null);
		Assert.assertEquals(userList.size(), 7);
		
		userList = dao.findAll("NES_wubiCode", "");
		Assert.assertEquals(userList.size(), 1);
		
		userList = dao.findAll("NES_wubiCode", "123");
		Assert.assertEquals(userList.size(), 6);
		
		userList = dao.findAll("LIKES_loginName", "m");
		Assert.assertEquals(userList.size(), 4);
		
		userList = dao.findAll("RLIKES_loginName", "m");
		Assert.assertEquals(userList.size(), 3);
		
		userList = dao.findAll("LLIKES_loginName", "n");
		Assert.assertEquals(userList.size(), 1);
		
		userList = dao.findAll("LEI_state", "1");
		Assert.assertEquals(userList.size(), 8);
		
		userList = dao.findAll("LTI_state", "2");
		Assert.assertEquals(userList.size(), 8);
		
		userList = dao.findAll("GEI_state", "1");
		Assert.assertEquals(userList.size(), 8);
		
		userList = dao.findAll("GTI_state", "0");
		Assert.assertEquals(userList.size(), 8);
		
		userList = dao.findAll("INS_loginName", "admin,vincent",new Sort(Direction.DESC, "loginName","realName"));
		Assert.assertEquals(userList.size(), 2);
		
		userList = dao.findAll("NINS_loginName", "admin,vincent");
		Assert.assertEquals(userList.size(), 6);
		
		userList = dao.findAll("EQS_loginName","admin|vincent");
		Assert.assertEquals(userList.size(), 2);
		
		userList = dao.findAll("EQS_loginName","admin,vincent");
		Assert.assertEquals(userList.size(),0);
		
		userList = dao.findAll("EQS_loginName","admin,null");
		Assert.assertEquals(userList.size(),0);
		
		userList = dao.findAll("EQS_loginName_OR_realName","null|admin");
		Assert.assertEquals(userList.size(), 1);
	}
	
	@Test
	public void testFindAll() {
		List<User> userList = new ArrayList<User>();
		
		//---------------------------------------------Expression test--------------------------------------------------//
		
		userList = dao.findAll("EQS_loginName", "admin");
		Assert.assertEquals(userList.size(), 1);
		
		userList = dao.findAll("EQI_state", "1");
		Assert.assertEquals(userList.size(), 8);
		
		userList = dao.findAll("EQI_state", "1",new Sort(Direction.DESC, "loginName","realName"));
		Assert.assertEquals(userList.size(), 8);
		
		userList = dao.findAll("EQI_state", "1");
		Assert.assertEquals(userList.size(), 8);
		
		userList = dao.findAll("EQI_state", "1");
		Assert.assertEquals(userList.size(), 8);
		
		//---------------------------------------------Expressions test--------------------------------------------------//
		
		userList = dao.findAll(new String[]{"EQS_loginName","EQS_realName"}, new String[]{"admin","admin"});
		Assert.assertEquals(userList.size(), 1);
		
		userList = dao.findAll(new String[]{"LIKES_loginName","EQI_state"}, new String[]{"m","1"});
		Assert.assertEquals(userList.size(), 4);
		
		userList = dao.findAll(new String[]{"LIKES_loginName","EQI_state"}, new String[]{"m","1"},new Sort(Direction.DESC, "loginName","realName"));
		Assert.assertEquals(userList.size(), 4);
		
		userList = dao.findAll(new String[]{"LIKES_loginName","EQI_state"}, new String[]{"m","1"},new Sort("loginName"));
		Assert.assertEquals(userList.size(), 4);
		
		userList = dao.findAll(new String[]{"LIKES_loginName","EQI_state"}, new String[]{"m","1"},new Sort(Direction.ASC,"realName"));
		Assert.assertEquals(userList.size(), 4);
		
		//---------------------------------------------PropertyFiter test--------------------------------------------------//
		
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters = PropertyFilters.build(new String[]{"LIKES_loginName","EQI_state"}, new String[]{"m","1"});
		
		userList = dao.findAll(filters);
		Assert.assertEquals(userList.size(), 4);
		
		userList = dao.findAll(filters,new Sort(Direction.DESC, "loginName","realName"));
		Assert.assertEquals(userList.size(), 4);
		
		userList = dao.findAll(filters,new Sort("loginName"));
		Assert.assertEquals(userList.size(), 4);
		
		userList = dao.findAll(filters,new Sort(Direction.ASC,"realName"));
		Assert.assertEquals(userList.size(), 4);
		
		Pageable pageable = new PageRequest(1, 2);
		filters = PropertyFilters.build(new String[]{"EQI_state"}, new String[]{"1"});
		Page<User> page = dao.findAll(pageable, filters);
		Assert.assertEquals(page.getContent().size(), 2);
		Assert.assertEquals(page.getTotalPages(), 4);
		Assert.assertEquals(page.getTotalElements(), 8);
	}
	
	@Test
	public void testFindOne() {
		User user = new User();
		
		user = dao.findOne("EQS_loginName", "admin");
		Assert.assertEquals(user.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0002");
		
		user = dao.findOne(new String[]{"EQS_loginName"}, new String[]{"admin"});
		Assert.assertEquals(user.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0002");
		
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters = PropertyFilters.build(new String[]{"EQS_loginName"}, new String[]{"admin"});
		
		user = dao.findOne(filters);
		Assert.assertEquals(user.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0002");
		

		user = dao.findOne(new PropertySpecification<User>("loginName", "admin"));

		Assert.assertEquals(user.getId(),"SJDK3849CKMS3849DJCK2039ZMSK0002");
	}
}
