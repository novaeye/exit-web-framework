package org.exitsoft.orm.test;

import java.util.Date;

import javax.sql.DataSource;

import org.exitsoft.common.unit.Fixtures;
import org.exitsoft.orm.test.entity.User;
import org.junit.AfterClass;
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
	
	
	private UserDao userDao;
	
	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
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
	public void testSaveUser() {
		
		User user = new User();
		user.setCreateTime(new Date());
		user.setLoginName("admin");
		user.setPassword("123456");
		user.setRealName("ds");
		
		userDao.save(user);
		
		long i = userDao.count();
	}
	
}
