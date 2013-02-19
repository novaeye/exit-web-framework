package org.exitsoft.showcase.test.manager.account;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.PropertyFilters;
import org.exitsoft.showcase.common.enumeration.entity.GroupType;
import org.exitsoft.showcase.entity.account.Group;
import org.exitsoft.showcase.service.account.AccountManager;
import org.exitsoft.showcase.test.manager.ManagerTestCaseSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

/**
 * 测试组管理所有方法
 * 
 * @author vincent
 *
 */
public class TestGroupManager extends ManagerTestCaseSupport{

	@Autowired
	private AccountManager accountManager;
	
	@Test
	@Transactional(readOnly=true)
	public void testGetGroup() {
		Group group = accountManager.getGroup("SJDK3849CKMS3849DJCK2039ZMSK0003");
		assertEquals(group.getName(), "用户角色");
		assertEquals(group.getChildren().size(),3);
	}

	@Test
	public void testGetGroupsListOfString() {
		List<String> ids = Lists.newArrayList("SJDK3849CKMS3849DJCK2039ZMSK0004",
						   					  "SJDK3849CKMS3849DJCK2039ZMSK0005",
						   					  "SJDK3849CKMS3849DJCK2039ZMSK0006");
		
		List<Group> result = accountManager.getGroups(ids);
		
		assertEquals(result.size(), 3);
	}

	@Test
	public void testSaveGroup() {
		Group entity = new Group();
		entity.setName("test");
		entity.setRemark("...");
		entity.setRole("role[test]");
		entity.setType(GroupType.RoleGorup.getValue());
		entity.setValue("/**");
		
		int before = countRowsInTable("tb_group");
		accountManager.saveGroup(entity);
		int after = countRowsInTable("tb_group");
		
		assertEquals(before + 1, after);
	}

	@Test
	public void testDeleteGroups() {
		int before = countRowsInTable("tb_group");
		accountManager.deleteGroups(Lists.newArrayList("SJDK3849CKMS3849DJCK2039ZMSK0003"));
		int after = countRowsInTable("tb_group");
		
		assertEquals(before - 4, after);
	}

	@Test
	public void testSearchGroupPage() {
		Pageable request = new PageRequest(1,10);
		
		List<PropertyFilter> filters = Lists.newArrayList(
				PropertyFilters.build("LIKES_name", "角色"),
				PropertyFilters.build("EQS_type", "03")
		);
		
		Page<Group> page = accountManager.searchGroupPage(request, filters);
		
		assertEquals(page.getTotalElements(), 4);
		assertEquals(page.getTotalPages(), 1);
	}

	@Test
	public void testGetAllGroupGroupType() {
		List<Group> result = accountManager.getAllGroup(GroupType.RoleGorup);
		assertEquals(result.size(), 4);
		
		result = accountManager.getAllGroup(GroupType.RoleGorup,"SJDK3849CKMS3849DJCK2039ZMSK0004","SJDK3849CKMS3849DJCK2039ZMSK0004");
		assertEquals(result.size(), 3);
	}

	@Test
	public void testGetUserGroups() {
		List<Group> result = accountManager.getUserGroups("SJDK3849CKMS3849DJCK2039ZMSK0001");
		assertEquals(result.size(), 4);
	}
}
