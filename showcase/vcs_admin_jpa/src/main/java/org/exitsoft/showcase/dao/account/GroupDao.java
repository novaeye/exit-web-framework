package org.exitsoft.showcase.dao.account;

import java.util.List;

import org.exitsoft.orm.core.spring.data.jpa.repository.BasicJpaRepository;
import org.exitsoft.showcase.entity.account.Group;
import org.springframework.data.jpa.repository.Query;

/**
 * 部门数据访问
 * 
 * @author vincent
 *
 */
public interface GroupDao extends BasicJpaRepository<Group, String>{

	/**
	 * 通过用户id获取所有资源
	 * 
	 * @param userId 用户id
	 * 
	 * @return List
	 */
	@Query("select gl from User u left join u.groupsList gl  where u.id=?1 and gl.type= '03'")
	List<Group> findUserGroupsByUserId(String userId);

}
