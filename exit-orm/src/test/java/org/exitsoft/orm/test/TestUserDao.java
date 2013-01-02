package org.exitsoft.orm.test;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.exitsoft.common.unit.Fixtures;
import org.exitsoft.orm.test.entity.User;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@TransactionConfiguration(transactionManager="jpaTransactionManager")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
public class TestUserDao {
	
	
	private UserDao dao;
	
	@Autowired
	public void setUserDao(UserDao userDao) {
		this.dao = userDao;
	}

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
	
	@Test
	public void testAllRestriction() {
		List<User> userList = new ArrayList<User>();
		
		userList = dao.findAll("EQD_createTime", "2012-08-12");
		
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
		
		userList = dao.findAll("INS_loginName", "admin,vincent");
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
	
}
