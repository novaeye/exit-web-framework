package org.exitsoft.orm.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.exitsoft.common.unit.Fixtures;
import org.exitsoft.orm.core.Page;
import org.exitsoft.orm.core.PageRequest;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.hibernate.HibernateSupportDao;
import org.exitsoft.orm.core.hibernate.property.PropertyFilterRestrictionHolder;
import org.exitsoft.orm.core.hibernate.property.impl.restriction.LikeRestriction;
import org.exitsoft.orm.core.hibernate.property.impl.restriction.NeRestriction;
import org.exitsoft.orm.test.entity.Role;
import org.exitsoft.orm.test.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * 测试HibernateSuperDao的查询方法
 * 
 * @author vincent
 *
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
public class TestHibernateSuperDao {

	private HibernateSupportDao<User, String> dao;
	
	private DataSource dataSource;
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	
	@Autowired
	public void setDataSource(DataSource dataSource) throws Exception {
		this.dataSource = dataSource;
		jdbcTemplate =  new JdbcTemplate(this.dataSource);
		Resource resource = this.applicationContext.getResource("classpath:/h2schma.sql");
		JdbcTestUtils.executeSqlScript(jdbcTemplate, new EncodedResource(resource,"UTF-8"), false);

		Fixtures.loadData(this.dataSource, "/sample-data.xml");
	}
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		dao = new HibernateSupportDao<User, String>(User.class);
		dao.setSessionFactory(sessionFactory);
	}
	
	@Test
	public void testFind() {
		List<User> userList = new ArrayList<User>();
		List<Role> roleList = new ArrayList<Role>();
		
		//---------------------------------------------Criterion test--------------------------------------------------//
		
		userList = dao.findByCriterion(Restrictions.eq("loginName", "admin"));
		Assert.assertEquals(userList.size(), 1);
		
		userList = dao.findByCriterion("loginName",Restrictions.eq("state", 1));
		Assert.assertEquals(userList.size(), 8);
		
		userList = dao.findByCriterion("loginName_ASC,realName_desc",Restrictions.eq("state", 1));
		Assert.assertEquals(userList.size(), 8);
		
		userList = dao.findByCriterion("loginName_asc,realName_DESC",Restrictions.eq("state", 1));
		Assert.assertEquals(userList.size(), 8);
		
		userList = dao.findByCriterion("loginName,realName_DESC",Restrictions.eq("state", 1));
		Assert.assertEquals(userList.size(), 8);
		
		userList = dao.findByCriterion("loginName_asc,realName",Restrictions.eq("state", 1));
		Assert.assertEquals(userList.size(), 8);
		
		roleList = dao.findByCriterion(Role.class, Restrictions.eq("name", "系统管理员"));
		Assert.assertEquals(roleList.size(), 1);
		
		roleList = dao.findByCriterion(Role.class, "name_asc");
		Assert.assertEquals(roleList.size(), 3);
		
		roleList = dao.findByCriterion(Role.class, "name_desc");
		Assert.assertEquals(roleList.size(), 3);
		
		//---------------------------------------------Expression test--------------------------------------------------//
		
		userList = dao.findByExpression("EQS_loginName", "admin");
		Assert.assertEquals(userList.size(), 1);
		
		userList = dao.findByExpression("EQI_state", "1","loginName_ASC,realName_desc");
		Assert.assertEquals(userList.size(), 8);
		
		userList = dao.findByExpression("EQI_state", "1","loginName_asc,realName_DESC");
		Assert.assertEquals(userList.size(), 8);
		
		userList = dao.findByExpression("EQI_state", "1","loginName,realName_DESC");
		Assert.assertEquals(userList.size(), 8);
		
		userList = dao.findByExpression("EQI_state", "1","loginName_asc,realName");
		Assert.assertEquals(userList.size(), 8);
		
		roleList = dao.findByExpression("EQS_name", "系统管理员", Role.class);
		Assert.assertEquals(roleList.size(), 1);
		
		roleList = dao.findByExpression("NES_name", null, "name_asc",Role.class);
		Assert.assertEquals(roleList.size(), 3);
		
		//---------------------------------------------Expressions test--------------------------------------------------//
		
		userList = dao.findByExpressions(new String[]{"EQS_loginName","EQS_realName"}, new String[]{"admin","admin"});
		Assert.assertEquals(userList.size(), 1);
		
		userList = dao.findByExpressions(new String[]{"LIKES_loginName","EQI_state"}, new String[]{"m","1"});
		Assert.assertEquals(userList.size(), 4);
		
		userList = dao.findByExpressions(new String[]{"LIKES_loginName","EQI_state"}, new String[]{"m","1"},"loginName_ASC,realName_desc");
		Assert.assertEquals(userList.size(), 4);
		
		userList = dao.findByExpressions(new String[]{"LIKES_loginName","EQI_state"}, new String[]{"m","1"},"loginName");
		Assert.assertEquals(userList.size(), 4);
		
		userList = dao.findByExpressions(new String[]{"LIKES_loginName","EQI_state"}, new String[]{"m","1"},"realName_asc");
		Assert.assertEquals(userList.size(), 4);
		
		roleList = dao.findByExpressions(new String[]{"LIKES_name"}, new String[]{"系统"},Role.class);
		Assert.assertEquals(roleList.size(), 3);
		
		roleList = dao.findByExpressions(new String[]{"LIKES_name"}, new String[]{"系统"},"name",Role.class);
		Assert.assertEquals(roleList.size(), 3);
		
		//---------------------------------------------PropertyFiter test--------------------------------------------------//
		
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters = PropertyFilterRestrictionHolder.createPropertyFilter(new String[]{"LIKES_loginName","EQI_state"}, new String[]{"m","1"});
		
		userList = dao.findByPropertyFilters(filters);
		Assert.assertEquals(userList.size(), 4);
		
		userList = dao.findByPropertyFilters(filters,"loginName_ASC,realName_desc");
		Assert.assertEquals(userList.size(), 4);
		
		userList = dao.findByPropertyFilters(filters,"loginName");
		Assert.assertEquals(userList.size(), 4);
		
		userList = dao.findByPropertyFilters(filters,"realName_asc");
		Assert.assertEquals(userList.size(), 4);
		
		filters = PropertyFilterRestrictionHolder.createPropertyFilter(new String[]{"LIKES_name"}, new String[]{"系统"});
		
		roleList = dao.findByPropertyFilters(filters, Role.class);
		Assert.assertEquals(roleList.size(), 3);
		
		roleList = dao.findByPropertyFilters(filters,"name", Role.class);
		Assert.assertEquals(roleList.size(), 3);
		
		userList = dao.findByNamedQueryUseJpaStyle("QueryUserResourceJpa", "admin");
		Assert.assertEquals(userList.size(), 1);
		
		userList = dao.findByNamedQuery("QueryUserResource", "admin");
		Assert.assertEquals(userList.size(), 1);
		
		userList = dao.findByQuery("from User u where u.loginName=?", "admin");
		Assert.assertEquals(userList.size(), 1);
		
		userList = dao.findByQueryUseJpaStyle("from User u where u.loginName=?1", "admin");
		Assert.assertEquals(userList.size(), 1);
		
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("loginName", "admin");
		
		userList = dao.findByQuery("from User u where u.loginName = :loginName", values);
		Assert.assertEquals(userList.size(), 1);
		
		userList = dao.findByDetachedCriteria(DetachedCriteria.forClass(User.class).add(Restrictions.eq("loginName", "admin")));
		Assert.assertEquals(userList.size(), 1);
	}
	
	@Test
	public void testFindByProperty() {
		List<User> userList = new ArrayList<User>();
		List<Role> roleList = new ArrayList<Role>();
		
		userList = dao.findByProperty("loginName", "admin");
		Assert.assertEquals(userList.size(),1);
		
		userList = dao.findByProperty("state", 1,NeRestriction.RestrictionName);
		Assert.assertEquals(userList.size(),0);
		
		userList = dao.findByPropertyWithOrderBy("state", 1, "loginName_asc");
		Assert.assertEquals(userList.size(),8);
		
		userList = dao.findByPropertyWithOrderBy("state", 1, "loginName_asc",NeRestriction.RestrictionName);
		Assert.assertEquals(userList.size(),0);
		
		roleList = dao.findByProperty("name","系统",Role.class);
		Assert.assertEquals(roleList.size(),1);
		
		roleList = dao.findByProperty("name","系统",LikeRestriction.RestrictionName,Role.class);
		Assert.assertEquals(roleList.size(),3);
		
		roleList = dao.findByProperty("name","系统",LikeRestriction.RestrictionName,Role.class,"name");
		Assert.assertEquals(roleList.size(),3);
		
	}
	
	@Test
	public void testFindUnique() {
		User user = new User();
		Role role = new Role();
		
		user = dao.findUniqueByCriterions(new Criterion[]{Restrictions.eq("loginName", "admin")});
		Assert.assertEquals(user.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0002");
		
		role = dao.findUniqueByCriterions(new Criterion[]{Restrictions.eq("name", "系统")}, Role.class);
		Assert.assertEquals(role.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0010");
		
		user = dao.findUniqueByExpression("EQS_loginName", "admin");
		Assert.assertEquals(user.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0002");
		
		role = dao.findUniqueByExpression("EQS_name", "系统",Role.class);
		Assert.assertEquals(role.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0010");
		
		user = dao.findUniqueByExpressions(new String[]{"EQS_loginName"}, new String[]{"admin"});
		Assert.assertEquals(user.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0002");
		
		role = dao.findUniqueByExpressions(new String[]{"EQS_name"}, new String[]{"系统"},Role.class);
		Assert.assertEquals(role.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0010");
		
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters = PropertyFilterRestrictionHolder.createPropertyFilter(new String[]{"EQS_loginName"}, new String[]{"admin"});
		
		user = dao.findUniqueByPropertyFilters(filters);
		Assert.assertEquals(user.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0002");
		
		filters = PropertyFilterRestrictionHolder.createPropertyFilter(new String[]{"EQS_name"}, new String[]{"系统"});
		
		role = dao.findUniqueByPropertyFilters(filters, Role.class);
		Assert.assertEquals(role.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0010");
		
		user = dao.findUniqueByNamedQueryUseJapStyle("QueryUserResourceJpa", "admin");
		Assert.assertEquals(user.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0002");
		
		user = dao.findUniqueByNamedQuery("QueryUserResource", "admin");
		Assert.assertEquals(user.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0002");
		
		user = dao.findUniqueByQuery("from User u where u.loginName=?", "admin");
		Assert.assertEquals(user.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0002");
		
		user = dao.findUniqueByQueryUseJpaStyle("from User u where u.loginName=?1", "admin");
		Assert.assertEquals(user.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0002");
		
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("loginName", "admin");
		
		user = dao.findUniqueByQuery("from User u where u.loginName = :loginName", values);
		Assert.assertEquals(user.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0002");
		
		user = dao.findUniqueByDetachedCriteria(DetachedCriteria.forClass(User.class).add(Restrictions.eq("loginName", "admin")));
		Assert.assertEquals(user.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0002");
	}
	
	@Test
	public void testFindUniqueByProperty() {
		User user = new User();
		Role role = new Role();
		
		user = dao.findUniqueByProperty("loginName", "admin");
		Assert.assertEquals(user.getId(),"SJDK3849CKMS3849DJCK2039ZMSK0002");
		
		role = dao.findUniqueByProperty("name", "系统",Role.class);
		Assert.assertEquals(role.getId(),"SJDK3849CKMS3849DJCK2039ZMSK0010");
		
	}
	
	@Test
	public void testFindPage() {
		PageRequest request = new PageRequest(1,2);
		Page<User> user = new Page<User>();
		Page<Role> role = new Page<Role>();
		
		user = dao.findPage(request, Restrictions.eq("state", 1));
		Assert.assertEquals(user.getResult().size(), 2);
		Assert.assertEquals(user.getTotalPages(), 4);
		Assert.assertEquals(user.getTotalItems(), 8);
		
		user = dao.findPage(request, "EQI_state","1");
		Assert.assertEquals(user.getResult().size(), 2);
		Assert.assertEquals(user.getTotalPages(), 4);
		Assert.assertEquals(user.getTotalItems(), 8);
		
		List<PropertyFilter> filters = PropertyFilterRestrictionHolder.createPropertyFilter(new String[]{"EQI_state"}, new String[]{"1"});
		
		user = dao.findPage(request, filters);
		Assert.assertEquals(user.getResult().size(), 2);
		Assert.assertEquals(user.getTotalPages(), 4);
		Assert.assertEquals(user.getTotalItems(), 8);
		
		user = dao.findPage(request, new String[]{"EQI_state"}, new String[]{"1"});
		Assert.assertEquals(user.getResult().size(), 2);
		Assert.assertEquals(user.getTotalPages(), 4);
		Assert.assertEquals(user.getTotalItems(), 8);
		
		user = dao.findPage(request,DetachedCriteria.forClass(User.class).add(Restrictions.eq("state", 1)));
		Assert.assertEquals(user.getResult().size(), 2);
		Assert.assertEquals(user.getTotalPages(), 4);
		Assert.assertEquals(user.getTotalItems(), 8);
		
		role = dao.findPage(request,Role.class,Restrictions.eq("name","系统管理员"));
		Assert.assertEquals(role.getResult().size(), 1);
		Assert.assertEquals(role.getTotalPages(), 1);
		Assert.assertEquals(role.getTotalItems(), 1);
		
		role = dao.findPage(request,"EQS_name","系统管理员",Role.class);
		Assert.assertEquals(role.getResult().size(), 1);
		Assert.assertEquals(role.getTotalPages(), 1);
		Assert.assertEquals(role.getTotalItems(), 1);
		
		filters = PropertyFilterRestrictionHolder.createPropertyFilter(new String[]{"EQS_name"}, new String[]{"系统管理员"});
		
		role = dao.findPage(request,filters,Role.class);
		Assert.assertEquals(role.getResult().size(), 1);
		Assert.assertEquals(role.getTotalPages(), 1);
		Assert.assertEquals(role.getTotalItems(), 1);
		
		role = dao.findPage(request,new String[]{"EQS_name"}, new String[]{"系统管理员"},Role.class);
		Assert.assertEquals(role.getResult().size(), 1);
		Assert.assertEquals(role.getTotalPages(), 1);
		Assert.assertEquals(role.getTotalItems(), 1);
		
		role = dao.findPage(request,DetachedCriteria.forClass(Role.class).add(Restrictions.eq("name", "系统管理员")));
		Assert.assertEquals(role.getResult().size(), 1);
		Assert.assertEquals(role.getTotalPages(), 1);
		Assert.assertEquals(role.getTotalItems(), 1);
		
		user = dao.findPageByNamedQuery(request, "QueryUserResource", "vincent");
		Assert.assertEquals(user.getResult().size(), 1);
		Assert.assertEquals(user.getTotalPages(), 1);
		Assert.assertEquals(user.getTotalItems(), 1);
		
		user = dao.findPageByNamedQueryUseJpaStyle(request, "QueryUserResourceJpa", "vincent");
		Assert.assertEquals(user.getResult().size(), 1);
		Assert.assertEquals(user.getTotalPages(), 1);
		Assert.assertEquals(user.getTotalItems(), 1);
		
	}
	
	@Test
	public void testAllRestriction() {
		List<User> userList = new ArrayList<User>();
		
		userList = dao.findByExpression("EQD_createTime", "2012-08-12");
		
		userList = dao.findByExpression("EQS_wubiCode", null);
		Assert.assertEquals(userList.size(), 1);
		userList = dao.findByExpression("EQS_wubiCode", "");
		Assert.assertEquals(userList.size(),6);
		userList = dao.findByExpression("EQS_wubiCode", "123");
		Assert.assertEquals(userList.size(), 1);
		
		userList = dao.findByExpression("NES_wubiCode", null);
		Assert.assertEquals(userList.size(), 7);
		userList = dao.findByExpression("NES_wubiCode", "");
		Assert.assertEquals(userList.size(), 1);
		userList = dao.findByExpression("NES_wubiCode", "123");
		Assert.assertEquals(userList.size(), 6);
		
		userList = dao.findByExpression("LIKES_loginName", "m");
		Assert.assertEquals(userList.size(), 4);
		userList = dao.findByExpression("RLIKES_loginName", "m");
		Assert.assertEquals(userList.size(), 3);
		userList = dao.findByExpression("LLIKES_loginName", "n");
		Assert.assertEquals(userList.size(), 1);
		
		userList = dao.findByExpression("LEI_state", "1");
		Assert.assertEquals(userList.size(), 8);
		userList = dao.findByExpression("LTI_state", "2");
		Assert.assertEquals(userList.size(), 8);
		
		userList = dao.findByExpression("GEI_state", "1");
		Assert.assertEquals(userList.size(), 8);
		userList = dao.findByExpression("GTI_state", "0");
		Assert.assertEquals(userList.size(), 8);
		
		userList = dao.findByExpression("INS_loginName", "admin,vincent");
		Assert.assertEquals(userList.size(), 2);
		
		userList = dao.findByExpression("NINS_loginName", "admin,vincent");
		Assert.assertEquals(userList.size(), 6);
		
		userList = dao.findByExpression("EQS_loginName","admin|vincent");
		Assert.assertEquals(userList.size(), 2);
		
		userList = dao.findByExpression("EQS_loginName","admin,vincent");
		Assert.assertEquals(userList.size(),0);
		
		userList = dao.findByExpression("EQS_loginName","admin,null");
		Assert.assertEquals(userList.size(),0);
		
		userList = dao.findByExpression("EQS_loginName_OR_realName","null|admin");
		Assert.assertEquals(userList.size(), 1);
		
	}

}
