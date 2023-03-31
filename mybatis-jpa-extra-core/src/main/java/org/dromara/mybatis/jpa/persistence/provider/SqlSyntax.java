package org.dromara.mybatis.jpa.persistence.provider;

public class SqlSyntax {
	
	public  final  static String SELECT 	= "select";
	
	public  final  static String DISTINCT 	= "distinct";
	
	public  final  static String FROM 		= "from";
	
	public  final  static String GROUPBY	= "group by";
	
	public  final  static String ORDERBY 	= "order by";
	
	public  final  static String HAVING 	= "having";
	
	public class Functions{
		
		public static final String COUNT_ALL 	= "count(*)";
		
		public static final String COUNT_ONE 	= "count(1)";
	}
	
}
