package org.exitsoft.showcase.vcsadmin.dao.account;

import java.util.List;

import org.exitsoft.orm.core.hibernate.support.HibernateSupportDao;
import org.exitsoft.showcase.vcsadmin.entity.account.Resource;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.stereotype.Repository;

/**
 * 资源数据访问
 * 
 * @author vincent
 *
 */
@Repository
public class ResourceDao extends HibernateSupportDao<Resource, String>{

	@SuppressWarnings("unchecked")
	public List<Resource> getUserResources(String userId) {
		Query query = createQueryByNamedQuery(Resource.UserResources, userId);
		query.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return query.list();
	}

	
}
