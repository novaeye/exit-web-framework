package org.exitsoft.showcase.service.account;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.exitsoft.common.utils.CollectionUtils;
import org.exitsoft.showcase.common.enumeration.entity.ResourceType;
import org.exitsoft.showcase.common.model.CommonVariableModel;
import org.exitsoft.showcase.entity.account.Group;
import org.exitsoft.showcase.entity.account.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * apache shiro 的公用授权类
 * 
 * @author vincent
 *
 */
public abstract class AuthorizationRealm extends AuthorizingRealm{

	@Autowired
	private AccountManager accountManager;
	
	private List<String> defaultPermissions;
	
	private List<String> defaultRoles;
	
	/**
	 * 默认permission
	 * 
	 * @param defaultPermissions permission
	 */
	public void setDefaultPermissions(List<String> defaultPermissions) {
		this.defaultPermissions = defaultPermissions;
	}
	
	/**
	 * 默认role
	 * 
	 * @param defaultRoles role
	 */
	public void setDefaultRoles(List<String> defaultRoles) {
		this.defaultRoles = defaultRoles;
	}

	/**
	 * 
	 * 当用户进行访问链接时的授权方法
	 * 
	 */
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        
        CommonVariableModel model = (CommonVariableModel) principals.getPrimaryPrincipal();
        
        Assert.notNull(model, "找不到principals中的CommonVariableModel");
        
        String id = model.getUser().getId();
        
        //加载用户的组信息和资源信息
        List<Resource> authorizationInfo = accountManager.getUserResources(id);
        List<Group> groupsList = accountManager.getUserGroups(id);
        List<Resource> resourcesList = accountManager.mergeResourcesToParent(authorizationInfo, ResourceType.Security);
        
        model.setAuthorizationInfo(authorizationInfo);
        model.setGroupsList(groupsList);
        model.setMenusList(resourcesList);
        
        //添加用户拥有的permission
        addPermissions(info,authorizationInfo);
        //添加用户拥有的role
        addRoles(info,groupsList);
        
        return info;
	}
	
	/**
	 * 通过组集合，将集合中的role字段内容解析后添加到SimpleAuthorizationInfo授权信息中
	 * 
	 * @param info SimpleAuthorizationInfo
	 * @param groupsList 组集合
	 */
	private void addRoles(SimpleAuthorizationInfo info, List<Group> groupsList) {
		
		//解析当前用户组中的role
        List<String> temp = CollectionUtils.extractToList(groupsList, "role", true);
        List<String> roles = getValue(temp,"roles\\[(.*?)\\]");
       
        //添加默认的roles到roels
        if (CollectionUtils.isNotEmpty(defaultRoles)) {
        	CollectionUtils.addAll(roles, defaultRoles.iterator());
        }
        
        //将当前用户拥有的roles设置到SimpleAuthorizationInfo中
        info.addRoles(roles);
		
	}

	/**
	 * 通过资源集合，将集合中的permission字段内容解析后添加到SimpleAuthorizationInfo授权信息中
	 * 
	 * @param info SimpleAuthorizationInfo
	 * @param authorizationInfo 资源集合
	 */
	private void addPermissions(SimpleAuthorizationInfo info,List<Resource> authorizationInfo) {
		//解析当前用户资源中的permissions
        List<String> temp = CollectionUtils.extractToList(authorizationInfo, "permission", true);
        List<String> permissions = getValue(temp,"perms\\[(.*?)\\]");
       
        //添加默认的permissions到permissions
        if (CollectionUtils.isNotEmpty(defaultPermissions)) {
        	CollectionUtils.addAll(permissions, defaultPermissions.iterator());
        }
        
        //将当前用户拥有的permissions设置到SimpleAuthorizationInfo中
        info.addStringPermissions(permissions);
		
	}
	
	/**
	 * 通过正则表达式获取字符串集合的值
	 * 
	 * @param obj 字符串集合
	 * @param regex 表达式
	 * 
	 * @return List
	 */
	private List<String> getValue(List<String> obj,String regex){

        List<String> result = new ArrayList<String>();
        
		if (CollectionUtils.isEmpty(obj)) {
			return result;
		}
		
		Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(StringUtils.join(obj, ","));
        
        while(matcher.find()){
        	result.add(matcher.group(1));
        }
        
		return result;
	}
}
