/**
 * 
 */
package org.apache.mybatis.jpa.persistence.provider;

import java.util.List;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
public class SqlProviderInsert <T extends JpaBaseDomain>{
	
	private static final Logger _logger 	= 	LoggerFactory.getLogger(SqlProviderInsert.class);
	
	/**
	 * @param entity
	 * @return insert sql String
	 */
	public String insert(T entity) {
		MapperMetadata.buildColumnList(entity.getClass());
		List<FieldColumnMapper> listFields = MapperMetadata.fieldsMap.get(entity.getClass().getSimpleName());
		SQL sql = new SQL();
		sql.INSERT_INTO(MapperMetadata.getTableName(entity.getClass()));
		for (int i = 0; i < listFields.size(); i++) {
			FieldColumnMapper fieldColumnMapper=listFields.get(i);
			if(fieldColumnMapper.getFieldType().equalsIgnoreCase("String")
					&&BeanUtil.getValue(entity, fieldColumnMapper.getFieldName())==null
					&&fieldColumnMapper.getGeneratedValue()==null) {
				//skip null field value
			}else {
				//have GeneratedValue and (value is null or eq "")
				if(fieldColumnMapper.getGeneratedValue()!=null 
						&& (
								BeanUtil.get(entity, fieldColumnMapper.getFieldName()) == null
								|| BeanUtil.get(entity, fieldColumnMapper.getFieldName()) == ""
						)) {
					GeneratedValue generatedValue=listFields.get(i).getGeneratedValue();
					if(generatedValue.strategy()==GenerationType.AUTO) {
						if(MapperMetadata.identifierGeneratorFactory.getGeneratorStrategyMap().containsKey(generatedValue.generator().toLowerCase())) {
							BeanUtil.set(entity, fieldColumnMapper.getFieldName(), MapperMetadata.identifierGeneratorFactory.generate(generatedValue.generator().toLowerCase()));
							sql.VALUES(fieldColumnMapper.getColumnName(),"#{" + fieldColumnMapper.getFieldName() + "}");
						}
					}else if(generatedValue.strategy()==GenerationType.SEQUENCE){
						sql.VALUES(fieldColumnMapper.getColumnName(),generatedValue.generator()+".NEXTVAL");
					}else if(generatedValue.strategy()==GenerationType.IDENTITY){
						//skip
					}else if(generatedValue.strategy()==GenerationType.TABLE){
						//TODO
					}
				}else {
					sql.VALUES(fieldColumnMapper.getColumnName(),"#{" + fieldColumnMapper.getFieldName() + "}");
				}
			}
		}
		_logger.trace("Insert SQL : \n" + sql);
		return sql.toString();
	}
	
}
