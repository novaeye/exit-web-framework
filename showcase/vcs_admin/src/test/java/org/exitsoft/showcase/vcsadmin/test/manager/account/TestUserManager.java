package org.exitsoft.showcase.vcsadmin.test.manager.account;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.exitsoft.orm.core.Page;
import org.exitsoft.orm.core.PageRequest;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.PropertyFilters;
import org.exitsoft.showcase.vcsadmin.common.enumeration.entity.State;
import org.exitsoft.showcase.vcsadmin.entity.account.User;
import org.exitsoft.showcase.vcsadmin.service.account.AccountManager;
import org.exitsoft.showcase.vcsadmin.test.manager.ManagerTestCaseSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

/**
 * 测试用户管理所有方法
 * 
 * @author vincent
 *
 */
public class TestUserManager extends ManagerTestCaseSupport{

	@Autowired
	private AccountManager accountManager;

	@Test
	@Transactional(readOnly=true)
	public void testGetUser() {
		User user = accountManager.getUser("SJDK3849CKMS3849DJCK2039ZMSK0001");
		assertEquals(user.getUsername(),"admin");
	}

	@Test
	public void testSearchUserPage() {
		PageRequest request = new PageRequest();
		
		List<PropertyFilter> filters = Lists.newArrayList(
				PropertyFilters.build("LIKES_realname", "管理员"),
				PropertyFilters.build("EQI_state", "1")
		);
		
		Page<User> page = accountManager.searchUserPage(request, filters);
		
		assertEquals(page.getTotalItems(), 2);
		assertEquals(page.getTotalPages(), 1);
	}

	@Test
	public void testInsertUser() {
		User entity = new User();
		
		entity.setEmail("test@test.com");
		entity.setPassword("123");
		entity.setRealname("测试");
		entity.setUsername("test");
		entity.setState(State.Enable.getValue());
		
		int before = countRowsInTable("tb_user");
		accountManager.insertUser(entity);
		int after = countRowsInTable("tb_user");
		
		assertEquals(before + 1, after);
	}

	@Test
	@Transactional
	public void testUpdateUser() {
		User entity = accountManager.getUser("SJDK3849CKMS3849DJCK2039ZMSK0001");
		entity.setUsername("modify");
		entity.setPassword("321");
		entity.setRealname("vincent");
		
		accountManager.updateUser(entity);
		
		getSessionFactory().getCurrentSession().flush();
		getSessionFactory().getCurrentSession().clear();
		
		entity = accountManager.getUser("SJDK3849CKMS3849DJCK2039ZMSK0001");
		
		assertEquals(entity.getUsername(), "admin");
		assertEquals(entity.getPassword(), "123456");
		assertEquals(entity.getRealname(), "vincent");
	}

	@Test
	public void testIsUsernameUnique() {
		assertEquals(accountManager.isUsernameUnique("admin"), false);
	}

	@Test
	public void testDeleteUsers() {
		int before = countRowsInTable("tb_user");
		accountManager.deleteUsers(Lists.newArrayList("SJDK3849CKMS3849DJCK2039ZMSK0001"));
		int after = countRowsInTable("tb_user");
		
		assertEquals(before - 1, after);
	}

	@Test
	public void testGetUserByUsername() {
		User entity = accountManager.getUserByUsername("admin");
		assertEquals(entity.getUsername(), "admin");
		assertEquals(entity.getRealname(), "系统管理员");
	}
	
}
