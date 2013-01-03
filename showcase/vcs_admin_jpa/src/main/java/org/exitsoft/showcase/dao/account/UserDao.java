package org.exitsoft.showcase.dao.account;

import org.exitsoft.orm.core.spring.data.jpa.repository.BasicJpaRepository;
import org.exitsoft.showcase.entity.account.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 用户数据访问
 * @author vincent
 *
 */
public interface UserDao extends BasicJpaRepository<User, String>{

	/**
	 * 通过用户id更新用户密码
	 * 
	 * @param userId 用户id
	 * @param password 密码
	 */
	@Modifying
	@Query("update User u set u.password = ?1 where u.id = ?2")
	public int updatePassword(String password,String userId);

	
}
