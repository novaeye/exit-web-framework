package org.exitsoft.common.test.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.exitsoft.common.test.entity.Group;
import org.exitsoft.common.test.entity.UniversallyUniqueIdentifier;
import org.exitsoft.common.test.entity.User;
import org.exitsoft.common.utils.JaxbUtils;
import org.exitsoft.common.utils.ReflectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class TestJaxbUtils {
	
	private User user;
	
	@Before
	public void reloadUser() {
		user = new User();
		user.setId("123");
		user.setGroupsList(new ArrayList<Group>());
		Group g = new Group();
		user.getGroupsList().add(g);
	}
	
	@Test
	public void testMarshaller() {
		
		String xml = "";
		
		xml = JaxbUtils.marshaller(user);
		
		xml = JaxbUtils.marshaller(user,"GBK");
		
		xml = JaxbUtils.marshaller(user, UniversallyUniqueIdentifier.class, "GBK");
		
		List<Group> groups = new ArrayList<Group>();
		groups.add(new Group());
		groups.add(new Group());
		groups.add(new Group());
		groups.add(new Group());
		
		xml = JaxbUtils.marshaller(groups,"groupList",Group.class);
		
		xml = JaxbUtils.marshaller(groups,"groupList",UniversallyUniqueIdentifier.class,"GBK");
		
		List<Field> list = ReflectionUtils.getAccessibleFields(user.getClass(), true);
		list = ReflectionUtils.getAccessibleFields(user.getClass(), false);
	}
	
}
