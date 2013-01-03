package org.exitsoft.showcase.dao.foundation;

import org.exitsoft.orm.core.spring.data.jpa.repository.BasicJpaRepository;
import org.exitsoft.showcase.entity.foundation.DictionaryCategory;

/**
 * 字典类别数据访问
 * 
 * @author vincent
 *
 */
public interface DictionaryCategoryDao extends BasicJpaRepository<DictionaryCategory, String>{

}
