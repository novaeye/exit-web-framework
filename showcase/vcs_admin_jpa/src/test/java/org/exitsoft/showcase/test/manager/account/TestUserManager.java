package org.exitsoft.showcase.test.manager.account;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.PropertyFilterConstructors;
import org.exitsoft.showcase.common.enumeration.entity.State;
import org.exitsoft.showcase.entity.account.User;
import org.exitsoft.showcase.service.ServiceException;
import org.exitsoft.showcase.service.account.AccountManager;
import org.exitsoft.showcase.test.manager.ManagerTestCaseSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * 测试用户管理所有方法
 * 
 * @author vincent
 *
 */
public class TestUserManager extends ManagerTestCaseSupport{

	@Autowired
	private AccountManager accountManager;
	
	@Test(expected = ServiceException.class)
	public void testInsertUser() {
		User entity = new User();
		entity.setEmail("27637461@qq.com");
		entity.setPassword("123456");
		entity.setRealname("vincent");
		entity.setState(State.Enable.getValue());
		entity.setUsername("vincent");
		
		int beforeRow = countRowsInTable("tb_user");
		accountManager.insertUser(entity);
		int afterRow = countRowsInTable("tb_user");
		
		assertEquals(afterRow, beforeRow + 1);
		
		entity = new User();
		entity.setUsername("admin");
		
		accountManager.insertUser(entity);
	}
	
	@Test
	public void testDeleteUsers() {
		List<String> ids = new ArrayList<String>();
		ids.add("SJDK3849CKMS3849DJCK2039ZMSK0001");
		
		int beforeRow = countRowsInTable("tb_user");
		accountManager.deleteUsers(ids);
		int afterRow = countRowsInTable("tb_user");
		
		assertEquals(afterRow, beforeRow - 1);
	}
	
	@Test
	public void testSearchUserPage() {
		Pageable request = new PageRequest(1,1);
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(PropertyFilterConstructors.createPropertyFilter("EQI_state", "1"));
		Page<User> page = accountManager.searchUserPage(request, filters);
		assertEquals(page.getContent().size(), 1);
		assertEquals(page.getTotalElements(), 2);
		assertEquals(page.getTotalPages(), 2);
	}
	
}
