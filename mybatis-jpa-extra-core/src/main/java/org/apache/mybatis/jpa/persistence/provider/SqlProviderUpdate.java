/**
 * 
 */
package org.apache.mybatis.jpa.persistence.provider;

import java.util.List;
import org.apache.ibatis.jdbc.SQL;
import org.apache.mybatis.jpa.persistence.FieldColumnMapper;
import org.apache.mybatis.jpa.persistence.JpaBaseDomain;
import org.apache.mybatis.jpa.persistence.MapperMetadata;
import org.apache.mybatis.jpa.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Crystal.Sea
 *
 */
public class SqlProviderUpdate <T extends JpaBaseDomain>{
	
	private static final Logger _logger 	= 	LoggerFactory.getLogger(SqlProviderUpdate.class);

	/**
	 * @param entity
	 * @return update sql String
	 */
	public String update(T entity) {
		MapperMetadata.buildColumnList(entity.getClass());

		List<FieldColumnMapper> listFields = MapperMetadata.fieldsMap.get(entity.getClass().getSimpleName());
		SQL sql = new SQL();

		sql.UPDATE(MapperMetadata.getTableName(entity.getClass()));
		for (int i = 0; i < listFields.size(); i++) {
			FieldColumnMapper fieldColumnMapper=listFields.get(i);
			
			_logger.trace("Field " +fieldColumnMapper.getFieldName()+" , Type "+ fieldColumnMapper.getFieldType());
			
			if (fieldColumnMapper.isIdColumn()) {
				continue;
			}
			
			if(
				(fieldColumnMapper.getFieldType().equalsIgnoreCase("String")
						||fieldColumnMapper.getFieldType().startsWith("byte")
				)
				&&BeanUtil.getValue(entity, fieldColumnMapper.getFieldName())==null) {
				//skip null field value
				_logger.trace("skip  field value is null ");
			}else {
				sql.SET(fieldColumnMapper.getColumnName() + "=#{" + fieldColumnMapper.getFieldName() + "}");
			}
		}
		
		FieldColumnMapper idFieldColumnMapper=MapperMetadata.getIdColumn(entity.getClass().getSimpleName());
		sql.WHERE(idFieldColumnMapper.getColumnName() + "=#{"+idFieldColumnMapper.getFieldName()+"}");
		_logger.trace("Update SQL : \n" + sql);
		return sql.toString();
	}

}
