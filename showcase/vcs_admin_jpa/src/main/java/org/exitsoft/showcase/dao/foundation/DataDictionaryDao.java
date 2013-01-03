package org.exitsoft.showcase.dao.foundation;

import java.util.List;

import org.exitsoft.orm.core.spring.data.jpa.repository.BasicJpaRepository;
import org.exitsoft.showcase.entity.foundation.DataDictionary;

/**
 * 数据字典数据访问
 * 
 * @author vincent
 *
 */
public interface DataDictionaryDao extends BasicJpaRepository<DataDictionary, String>{

	List<DataDictionary> findByCategoryCode(String code);

	List<DataDictionary> findByCategoryCodeAndValueNot(String code,String ignoreValue);

}
