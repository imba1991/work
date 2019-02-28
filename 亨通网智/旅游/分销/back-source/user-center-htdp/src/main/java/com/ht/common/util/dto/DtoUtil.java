package com.ht.common.util.dto;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.ht.common.annotations.dto.AddColumn;
import com.ht.common.annotations.dto.QueryColumn;
import com.ht.common.annotations.dto.QueryTable;
import com.ht.common.annotations.dto.UpdateColumn;

/**
 * @author qiujunda
 * @date 2019年1月4日
 * @description
 */
@Component
public class DtoUtil {
	public static String ADD="add";
	public static String UPDATE="update";
	public static String QUERY="query";
	
	public String copy(Object source,Object destination,
			List<String> exceptions,String copyType)  {
		String result="";
		Class sourceClass=source.getClass();
		String tableName=destination.getClass().getSimpleName();
		String sqlTableName =convertFirstCharToLower(tableName);
		if(sourceClass.isAnnotationPresent(QueryTable.class)) {
			QueryTable queryTable=(QueryTable) sourceClass.getAnnotation(QueryTable.class);
			tableName =StringUtils.isBlank(queryTable.tableName())?tableName:queryTable.tableName();
		}
		Field[] sourceFields=sourceClass.getFields();
		for (Field field : sourceFields) {
			String fieldName=field.getName();
//			System.out.println(fieldName);
			if(exceptions!=null&&exceptions.contains(fieldName))
				continue;
			if(copyType.equals(ADD)&&field.isAnnotationPresent(AddColumn.class)) {
				copyColumn(source, destination, field);
			}
			else if(copyType.equals(UPDATE)&&field.isAnnotationPresent(UpdateColumn.class)) {
				copyColumn(source, destination, field);
			}
			else if(copyType.equals(QUERY)&&field.isAnnotationPresent(QueryColumn.class)) {
				if(result.equals("")) {
					result="select "+sqlTableName+" from "+tableName+" "+sqlTableName+" where 1=1"; 
				}
				String apendSql=getQuerySql(source, field);
				if(StringUtils.isNotBlank(apendSql)) {
					result+=" and "+sqlTableName+"."+getQuerySql(source, field);
				}
			}
		}
		return result;
	}
	
	private String getQuerySql(Object source,Field field) {
		String sql="";
		Class sourceClass=source.getClass();
		String fieldName=field.getName();
		QueryColumn queryColumn=field.getAnnotation(QueryColumn.class);
		
		String methodString=fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		try {
			Method method=sourceClass.getMethod("get"+methodString);
			Object sourceValue=method.invoke(source);
			String values=""+sourceValue;
			String columnName=queryColumn.column();
			if(StringUtils.isNotBlank(columnName)) {
				fieldName=columnName;
			}
			if(!field.getType().getSimpleName().equals("Integer")) {
				values="'"+values+"'";
				values=values.replaceAll(",", "','");
			}
			if(sourceValue==null) {
				return sql;
			}
			switch (queryColumn.value()) {
			case EQ:{
				sql=fieldName+"="+values;
				break;
			}
			case LIKE:{
				values=values.replaceAll("'", "");
				values="'%"+values+"%'";
				sql=fieldName+" like "+values;
				break;
			}
			case NOTLIKE:{
				sql=fieldName+" not like %"+values+"%";
				break;
			}
			case CONTAIN:{
				sql=fieldName=" in("+values+")";
				break;
			}
			case GR:{
				sql=fieldName+">"+values;
				break;
			}
			case LE:{
				sql=fieldName+"<"+values;
				break;
			}
			case GREQ:{
				sql=fieldName+">="+values;
				break;
			}
			case LEEQ:{
				sql=fieldName+"<="+values;
				break;
			}
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sql;
	}
	
	
	private void copyColumn(Object source,Object destination,Field field ) {
		Class sourceClass=source.getClass();
		Class destinationClass=destination.getClass();
		String fieldName=field.getName();
		String methodString=fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		try {
			Method method=sourceClass.getMethod("get"+methodString);
			Object sourceValue=method.invoke(source);
			Field desFiled=destinationClass.getField(fieldName);
			Class fieldTypeClass=desFiled.getType();
			Method desMethod=destinationClass.getMethod("set"+methodString,fieldTypeClass);
			desMethod.invoke(destination, sourceValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 首字母大写
	 * @param character
	 * @return
	 */
	public String convertFirstCharToUpper(String character) {
		return character.substring(0, 1).toUpperCase() + character.substring(1);
	}
	/**
	 * 首字母小写
	 * @param character
	 * @return
	 */
	public String convertFirstCharToLower(String character) {
		return character.substring(0, 1).toLowerCase() + character.substring(1);
	}
}
