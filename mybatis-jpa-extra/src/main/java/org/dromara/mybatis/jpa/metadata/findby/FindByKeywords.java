/*
 * Copyright [2024] [MaxKey of copyright http://www.maxkey.top]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 

package org.dromara.mybatis.jpa.metadata.findby;

import java.util.ArrayList;
import java.util.List;

/**
 * Reference https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
 */
public class FindByKeywords {

	/**
	 * keyword list
	 */
	static List<String> keywords;
	/**
	 * findBy
	 */
	public static final String FINDBY = "findBy";
	
	/**
	 * findDistinctBy
	 */
	public static final String FINDDISTINCTBY = "findDistinctBy";
	
	public static class KEY{
		/**
		 *  findDistinctByLastnameAndFirstname <br>
		 *  select distinct …​ where x.lastname = ?1 and x.firstname = ?2
		 */
		public static final String AND 				= "And";
		/**
		 * findByLastnameAndFirstname <br>
		 * where x.lastname = ?1 and x.firstname = ?2
		 */
		public static final String OR 				= "Or";
		/**
		 * findByLastnameOrFirstname <br>
		 * where x.lastname = ?1 or x.firstname = ?2
		 */
		public static final String IS 				= "Is";
		/**
		 * findByFirstname,findByFirstnameIs,findByFirstnameEquals <br>
		 * where x.firstname = ?1
		 */
		public static final String EQUALS 			= "Equals";
		/**
		 * findByStartDateBetween <br>
		 * where x.startDate between ?1 and ?2
		 */
		public static final String BETWEEN 			= "Between";
		/**
		 * findByAgeLessThan <br>
		 * where x.age < ?1
		 */
		public static final String LESS_THAN 		= "LessThan";
		/**
		 * findByAgeLessThanEqual <br>
		 * where x.age <= ?1
		 */
		public static final String LESS_THAN_EQUAL 	= "LessThanEqual";
		/**
		 * findByAgeGreaterThan <br>
		 * where x.age > ?1
		 */
		public static final String GREATER_THAN 		= "GreaterThan";
		/**
		 * findByAgeGreaterThanEqual <br>
		 * where x.age >= ?1
		 */
		public static final String GREATER_THAN_EQUAL = "GreaterThanEqual";
		/**
		 * findByStartDateAfter <br>
		 * where x.startDate > ?1
		 */
		public static final String AFTER 			= "After";
		/**
		 * findByStartDateBefore <br>
		 * where x.startDate < ?1
		 */
		public static final String BEFORE 			= "Before";
		/**
		 * findByAge(Is)Null <br>
		 * where x.age is null
		 */
		public static final String IS_NULL 			= "IsNull";
		/**
		 * same as IsNull
		 */
		public static final String NULL 			= "Null";
		/**
		 * findByAge(Is)NotNull <br>
		 * where x.age is not null
		 */
		public static final String IS_NOT_NULL 		= "IsNotNull";
		/**
		 * findByAge(Is)NotNull <br>
		 * where x.age is not null
		 */
		public static final String NOT_NULL 			= "NotNull";
		/**
		 * findByFirstnameLike <br>
		 * where x.firstname like ?1
		 */
		public static final String LIKE 			= "Like";
		/**
		 * findByFirstnameNotLike <br>
		 * where x.firstname not like ?1
		 */
		public static final String NOT_LIKE 			= "NotLike";
		/**
		 * findByFirstnameStartingWith <br>
		 * where x.firstname like ?1 (parameter bound with appended %)
		 */
		public static final String STARTING_WITH 	= "StartingWith";
		/**
		 * findByFirstnameEndingWith <br>
		 * where x.firstname like ?1 (parameter bound with prepended %)
		 */
		public static final String ENDING_WITH 		= "EndingWith";
		/**
		 * findByFirstnameContaining <br>
		 * where x.firstname like ?1 (parameter bound wrapped in %)
		 */
		public static final String CONTAINING 		= "Containing";
		/**
		 * findByAgeOrderByLastnameDesc <br>
		 * where x.age = ?1 order by x.lastname desc
		 */
		public static final String ORDER_BY 			= "OrderBy";
		/**
		 * findByLastnameNot <br>
		 * where x.lastname <> ?1
		 */
		public static final String NOT 				= "Not";
		/**
		 * findByAgeIn(Collection<Age> ages) <br>
		 * where x.age in ?1
		 */
		public static final String IN 				= "In";
		/**
		 * findByAgeNotIn(Collection<Age> ages) <br>
		 * where x.age not in ?1
		 */
		public static final String NOT_IN 			= "NotIn";
		/**
		 * findByActiveTrue() <br>
		 * where x.active = true
		 */
		public static final String TRUE 			= "True";
		/**
		 * findByActiveFalse() <br>
		 * where x.active = false
		 */
		public static final String FALSE 			= "False";
		/**
		 * findByFirstnameIgnoreCase <br>
		 * where UPPER(x.firstname) = UPPER(?1)
		 */
		public static final String IGNORE_CASE 		= "IgnoreCase";
	}
	static {
		keywords = new ArrayList<>();

		keywords.add(KEY.ORDER_BY);
	
		keywords.add(KEY.AND);

		keywords.add(KEY.OR);
		
		keywords.add(KEY.BETWEEN);

		keywords.add(KEY.LESS_THAN_EQUAL);
		
		keywords.add(KEY.LESS_THAN);

		keywords.add(KEY.GREATER_THAN_EQUAL);
		
		keywords.add(KEY.GREATER_THAN);

		keywords.add(KEY.AFTER);

		keywords.add(KEY.BEFORE);

		keywords.add(KEY.IS_NULL);

		keywords.add(KEY.NULL);

		keywords.add(KEY.IS_NOT_NULL);

		keywords.add(KEY.NOT_NULL);

		keywords.add(KEY.LIKE);

		keywords.add(KEY.NOT_LIKE);

		keywords.add(KEY.STARTING_WITH);
	
		keywords.add(KEY.ENDING_WITH);

		keywords.add(KEY.CONTAINING);

		keywords.add(KEY.NOT_IN);
		
		keywords.add(KEY.NOT);

		keywords.add(KEY.IN);
	
		keywords.add(KEY.TRUE);

		keywords.add(KEY.FALSE);

		keywords.add(KEY.IGNORE_CASE);
		
		keywords.add(KEY.IS);
		
		keywords.add(KEY.EQUALS);
		
	}
	
	public static String startKeyword(String key) {
		for(String keyword : keywords) {
			if(key.startsWith(keyword)) {
				return keyword;
			}
		}
		return null;
	}
}
