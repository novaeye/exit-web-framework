package org.exitsoft.common.test.mapper;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.exitsoft.common.mapper.BeanMapper;
import org.exitsoft.common.mapper.JaxbMapper;
import org.exitsoft.common.mapper.jaxb.wrapper.MapWrapper;
import org.exitsoft.common.test.entity.Group;
import org.exitsoft.common.test.entity.UniversallyUniqueIdentifier;
import org.exitsoft.common.test.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class TestJaxbMapper {
	
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
		
		xml = JaxbMapper.marshaller(user);
		
		xml = JaxbMapper.marshaller(user,"GBK");
		
		xml = JaxbMapper.marshaller(user, UniversallyUniqueIdentifier.class, "GBK");
		
		List<Group> groups = new ArrayList<Group>();
		groups.add(new Group());
		groups.add(new Group());
		groups.add(new Group());
		groups.add(new Group());
		
		xml = JaxbMapper.marshaller(groups,"groupList",Group.class);
		
		xml = JaxbMapper.marshaller(groups,"groupList",UniversallyUniqueIdentifier.class,"GBK");
		
		Map<String, Object> map = BeanMapper.toMap(user);
		
		xml = JaxbMapper.marshaller(map, "map");
		
		List<MapWrapper> list = new ArrayList<MapWrapper>();
		
		list.add(new MapWrapper(BeanMapper.toMap(user)));
		list.add(new MapWrapper(BeanMapper.toMap(user)));
		list.add(new MapWrapper(BeanMapper.toMap(user)));
		
		xml = JaxbMapper.marshaller(list, "list", MapWrapper.class);
	}
	
}
