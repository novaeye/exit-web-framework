package org.exitsoft.showcase.service.account;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.PropertyFilters;
import org.exitsoft.orm.core.RestrictionNames;
import org.exitsoft.orm.core.spring.data.jpa.specification.Specifications;
import org.exitsoft.showcase.common.SystemVariableUtils;
import org.exitsoft.showcase.common.enumeration.entity.GroupType;
import org.exitsoft.showcase.common.enumeration.entity.ResourceType;
import org.exitsoft.showcase.dao.account.GroupDao;
import org.exitsoft.showcase.dao.account.ResourceDao;
import org.exitsoft.showcase.dao.account.UserDao;
import org.exitsoft.showcase.entity.account.Group;
import org.exitsoft.showcase.entity.account.Resource;
import org.exitsoft.showcase.entity.account.User;
import org.exitsoft.showcase.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

/**
 * 账户管理业务逻辑
 * 
 * @author vincent
 *
 */
@Service
@Transactional
@SuppressWarnings("unchecked")
public class AccountManager {
	
	//用户数据访问
	@Autowired
	private UserDao userDao;
	
	//资源数据访问
	@Autowired
	private ResourceDao resourceDao;
	
	//组数据访问
	@Autowired
	private GroupDao groupDao;
	
	/**
	 * shrio授权缓存key
	 */
	private final String ShiroAuthorizationCache = "shiroAuthorizationCache";
	
	//------------------------------用户管理-----------------------------------//
	
	/**
	 * 更新当前用户密码
	 * 
	 * @param oldPassword 旧密码
	 * @param newPassword 新密码
	 * 
	 * @return boolean
	 */
	public boolean updateUserPassword(String oldPassword, String newPassword) {
		User user = SystemVariableUtils.getCommonVariableModel().getUser();
		
		oldPassword = new SimpleHash("MD5", oldPassword.toCharArray()).toString();
		if(user.getPassword().equals(oldPassword)) {
			String temp = new SimpleHash("MD5",newPassword).toHex();
			userDao.updatePassword(user.getId(),temp);
			user.setPassword(temp); 
			return true;
		}
		
		return false;
	}
	
	/**
	 * 通过id获取用户实体
	 * @param id 用户id
	 */
	public User getUser(String id) {
		return userDao.findOne(id);
	}
	
	/**
	 * 通过属性过滤器查询用户分页
	 * 
	 * @param request 分页参数
	 * @param filters 属性过滤器集合
	 * 
	 * @return {@link Page}
	 */
	public Page<User> searchUserPage(Pageable pageable,List<PropertyFilter> filters) {
		
		return userDao.findAll(Specifications.get(filters), pageable);
		
	}
	
	/**
	 * 新增用户
	 * 
	 * @param entity 用户实体
	 */
	public void insertUser(User entity) {
		if (!isUsernameUnique(entity.getUsername())) {
			throw new ServiceException("用户名已存在");
		}
		
		String password = new SimpleHash("MD5", entity.getPassword()).toHex();
		
		entity.setPassword(password);
		userDao.save(entity);
	}
	
	/**
	 * 更新用户
	 * 
	 * @param entity 用户实体
	 */
	@CacheEvict(value=ShiroAuthorizationCache,allEntries=true)
	public void updateUser(User entity) {
		userDao.save(entity);
	}
	
	/**
	 * 是否唯一的用户名 如果是返回true,否则返回false
	 * 
	 * @param username 用户名
	 * 
	 * @return boolean
	 */
	public boolean isUsernameUnique(String username) {
		return getUserByUsername(username) == null;
	}
	
	/**
	 * 删除用户
	 * 
	 * @param ids 用户id集合
	 */
	public void deleteUsers(List<String> ids) {
		userDao.delete(userDao.findAll(ids));
	}

	/**
	 * 通过用户名获取用户实体
	 * 
	 * @param username 用户实体
	 * 
	 * @return {@link User}
	 */
	public User getUserByUsername(String username) {
		return userDao.findOne(Specifications.get("username", username));
	}
	
	//------------------------------资源管理-----------------------------------//
	
	/**
	 * 通过id获取资源实体
	 * 
	 * @param id 资源id
	 * 
	 * @return {@link Resource}
	 */
	public Resource getResource(String id) {
		return resourceDao.findOne(id);
	}
	
	/**
	 * 通过id集合获取资源资源
	 * 
	 * @param ids 资源集合
	 * 
	 * @return List
	 */
	public List<Resource> getResources(List<String> ids) {
		return (List<Resource>) resourceDao.findAll(ids);
	}
	
	/**
	 * 通过属性过滤器查询资源分页
	 * 
	 * @param request 分页参数
	 * @param filters 属性过滤器集合
	 * 
	 * @return {@link Page}
	 */
	public Page<Resource> searchResourcePage(Pageable pageable,List<PropertyFilter> filters) {
		return resourceDao.findAll(Specifications.get(filters), pageable);
	}
	
	/**
	 * 保存资源实体
	 * 
	 * @param entity 资源实体
	 */
	public void saveResource(Resource entity) {
		entity.setSort(((int)resourceDao.count()) + 1);
		resourceDao.save(entity);
	}
	
	/**
	 * 通过资源id删除资源
	 * 
	 * @param ids 资源id集合 
	 */
	public void deleteResources(List<String> ids) {
		resourceDao.delete(resourceDao.findAll(ids));
	}
	
	/**
	 * 获取所有菜单类型父类资源
	 * 
	 * @return List
	 */
	public List<Resource> getAllParentMenuResources() {
		List<PropertyFilter> filters = Lists.newArrayList(
				PropertyFilters.build("EQS_parent.id", null),
				PropertyFilters.build("EQS_type", ResourceType.Menu.getValue())
		);
		return resourceDao.findAll(Specifications.get(filters));
	}
	
	/**
	 * 获取所有父类资源
	 * 
	 * @return List
	 */
	public List<Resource> getAllParentResources() {
		
		return resourceDao.findAll(Specifications.get("parent.id", null));
	}
	
	/**
	 * 获取所有资源
	 * 
	 * @param ignoreIdValue 要忽略的id属性值,如果是多个值，使用逗号分割
	 * 
	 * @return List
	 */
	public List<Resource> getAllResources(String... ignoreIdValue) {
		
		if(ArrayUtils.isNotEmpty(ignoreIdValue)) {
			return resourceDao.findAll(Specifications.get("id",ignoreIdValue,RestrictionNames.NIN));
		}
		
		return resourceDao.findAll();
	}
	
	/**
	 * 通过用户id获取该用户下的所有资源
	 * 
	 * @param userId 用户id
	 * 
	 * @return List
	 */
	public List<Resource> getUserResources(String userId) {
		return resourceDao.findUserResources(userId);
	}
	
	/**
	 * 并合子类资源到父类中
	 * 
	 * @param list 资源集合
	 */
	public List<Resource> mergeResourcesToParent(List<Resource> list,ResourceType resourceType) {
		List<Resource> result = new ArrayList<Resource>();
		
		for (Resource r : list) {
			if (r.getParent() == null && !StringUtils.equals(resourceType.getValue(),r.getType())) {
				mergeResourcesToParent(list,r,resourceType);
				result.add(r);
			}
		}
		
		return result;
	}
	
	/**
	 * 遍历list中的数据,如果数据的父类与parent相等，将数据加入到parent的children中
	 * 
	 * @param list 资源集合
	 */
	private void mergeResourcesToParent(List<Resource> list, Resource parent,ResourceType resourceType) {
		if (!parent.getLeaf()) {
			return ;
		}
		
		parent.setChildren(new ArrayList<Resource>());
		
		for (Resource r: list) {
			
			if (!StringUtils.equals(r.getType(), resourceType.getValue()) && StringUtils.equals(r.getParentId(),parent.getId()) ) {
				r.setChildren(null);
				mergeResourcesToParent(list,r,resourceType);
				parent.getChildren().add(r);
			}
			
		}
	}
	
	//------------------------------组管理-----------------------------------//
	
	/**
	 * 通过id获取组实体
	 * 
	 * @param id 组id
	 * 
	 * @return {@link Group}
	 */
	public Group getGroup(String id) {
		return groupDao.findOne(id);
	}
	
	/**
	 * 通过组id，获取组集合
	 * 
	 * @param ids id集合
	 * 
	 * @return List
	 */
	public List<Group> getGroups(List<String> ids) {
		return (List<Group>) groupDao.findAll(ids);
	}
	
	/**
	 * 保存组实体
	 * 
	 * @param entity 组实体
	 */
	@CacheEvict(value=ShiroAuthorizationCache,allEntries=true)
	public void saveGroup(Group entity) {
		groupDao.save(entity);
	}
	
	/**
	 * 删除组实体
	 * @param ids 组id
	 */
	@CacheEvict(value=ShiroAuthorizationCache,allEntries=true)
	public void deleteGroups(List<String> ids) {
		groupDao.delete(groupDao.findAll(ids));
	}

	/**
	 * 通过属性过滤器查询组分页
	 * 
	 * @param request 分页参数
	 * @param filters 属性过滤器集合
	 * 
	 * @return {@link Page}
	 */
	public Page<Group> searchGroupPage(Pageable pageable,List<PropertyFilter> filters) {
		
		return groupDao.findAll(Specifications.get(filters), pageable);
	}
	
	/**
	 * 根据组类型获取所有组信息
	 * 
	 * @param groupType 组类型
	 * @param ignoreIdValue 要忽略的id属性值,如果是多个值，使用逗号分割
	 * 
	 * @return List
	 */
	public List<Group> getAllGroup(GroupType groupType,String... ignoreIdValue) {
		
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		
		if (ArrayUtils.isNotEmpty(ignoreIdValue)) {
			filters.add(PropertyFilters.build("NES_id", StringUtils.join(ignoreIdValue,",")));
		}
		
		filters.add(PropertyFilters.build("EQS_type", groupType.getValue()));
		
		return groupDao.findAll(Specifications.get(filters));
	}

	/**
	 * 通过用户id获取所有资源
	 * 
	 * @param userId 用户id
	 * 
	 * @return List
	 */
	public List<Group> getUserGroups(String userId) {
		return groupDao.findUserGroups(userId);
	}

}
