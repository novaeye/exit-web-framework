package org.exitsoft.showcase.dao.account;

import java.util.List;

import org.exitsoft.showcase.entity.account.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * 资源数据访问
 * 
 * @author vincent
 *
 */
public interface ResourceDao extends JpaRepository<Resource, String>,JpaSpecificationExecutor<Resource>{

	/**
	 * 通过用户id获取该用户下的所有资源
	 * 
	 * @param userId 用户id
	 * 
	 * @return List
	 */
	@Query("select distinct rl from User u left join u.groupsList gl left join gl.resourcesList rl where u.id=?1 and gl.type= '03' order by rl.sort")
	List<Resource> findUserResourcesByUserId(String userId);

	
}
