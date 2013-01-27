package org.exitsoft.showcase.dao.foundation;

import java.util.List;

import org.exitsoft.showcase.entity.foundation.DataDictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 数据字典数据访问
 * 
 * @author vincent
 *
 */
public interface DataDictionaryDao extends JpaRepository<DataDictionary, String>, JpaSpecificationExecutor<DataDictionary>{

	List<DataDictionary> findAllByCategoryCode(String code);

	List<DataDictionary> findByCategoryCodeAndValueNot(String code,String ignoreValue);

}
