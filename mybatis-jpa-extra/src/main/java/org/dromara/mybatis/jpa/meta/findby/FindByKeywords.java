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
 

package org.dromara.mybatis.jpa.meta.findby;

import java.util.ArrayList;
import java.util.List;

/**
 * Reference https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
 */
public class FindByKeywords {

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
		public static final String And 				= "And";
		/**
		 * findByLastnameAndFirstname <br>
		 * where x.lastname = ?1 and x.firstname = ?2
		 */
		public static final String Or 				= "Or";
		/**
		 * findByLastnameOrFirstname <br>
		 * where x.lastname = ?1 or x.firstname = ?2
		 */
		public static final String Is 				= "Is";
		/**
		 * findByFirstname,findByFirstnameIs,findByFirstnameEquals <br>
		 * where x.firstname = ?1
		 */
		public static final String Equals 			= "Equals";
		/**
		 * findByStartDateBetween <br>
		 * where x.startDate between ?1 and ?2
		 */
		public static final String Between 			= "Between";
		/**
		 * findByAgeLessThan <br>
		 * where x.age < ?1
		 */
		public static final String LessThan 		= "LessThan";
		/**
		 * findByAgeLessThanEqual <br>
		 * where x.age <= ?1
		 */
		public static final String LessThanEqual 	= "LessThanEqual";
		/**
		 * findByAgeGreaterThan <br>
		 * where x.age > ?1
		 */
		public static final String GreaterThan 		= "GreaterThan";
		/**
		 * findByAgeGreaterThanEqual <br>
		 * where x.age >= ?1
		 */
		public static final String GreaterThanEqual = "GreaterThanEqual";
		/**
		 * findByStartDateAfter <br>
		 * where x.startDate > ?1
		 */
		public static final String After 			= "After";
		/**
		 * findByStartDateBefore <br>
		 * where x.startDate < ?1
		 */
		public static final String Before 			= "Before";
		/**
		 * findByAge(Is)Null <br>
		 * where x.age is null
		 */
		public static final String IsNull 			= "IsNull";
		/**
		 * same as IsNull
		 */
		public static final String Null 			= "Null";
		/**
		 * findByAge(Is)NotNull <br>
		 * where x.age is not null
		 */
		public static final String IsNotNull 		= "IsNotNull";
		/**
		 * findByAge(Is)NotNull <br>
		 * where x.age is not null
		 */
		public static final String NotNull 			= "NotNull";
		/**
		 * findByFirstnameLike <br>
		 * where x.firstname like ?1
		 */
		public static final String Like 			= "Like";
		/**
		 * findByFirstnameNotLike <br>
		 * where x.firstname not like ?1
		 */
		public static final String NotLike 			= "NotLike";
		/**
		 * findByFirstnameStartingWith <br>
		 * where x.firstname like ?1 (parameter bound with appended %)
		 */
		public static final String StartingWith 	= "StartingWith";
		/**
		 * findByFirstnameEndingWith <br>
		 * where x.firstname like ?1 (parameter bound with prepended %)
		 */
		public static final String EndingWith 		= "EndingWith";
		/**
		 * findByFirstnameContaining <br>
		 * where x.firstname like ?1 (parameter bound wrapped in %)
		 */
		public static final String Containing 		= "Containing";
		/**
		 * findByAgeOrderByLastnameDesc <br>
		 * where x.age = ?1 order by x.lastname desc
		 */
		public static final String OrderBy 			= "OrderBy";
		/**
		 * findByLastnameNot <br>
		 * where x.lastname <> ?1
		 */
		public static final String Not 				= "Not";
		/**
		 * findByAgeIn(Collection<Age> ages) <br>
		 * where x.age in ?1
		 */
		public static final String In 				= "In";
		/**
		 * findByAgeNotIn(Collection<Age> ages) <br>
		 * where x.age not in ?1
		 */
		public static final String NotIn 			= "NotIn";
		/**
		 * findByActiveTrue() <br>
		 * where x.active = true
		 */
		public static final String True 			= "True";
		/**
		 * findByActiveFalse() <br>
		 * where x.active = false
		 */
		public static final String False 			= "False";
		/**
		 * findByFirstnameIgnoreCase <br>
		 * where UPPER(x.firstname) = UPPER(?1)
		 */
		public static final String IgnoreCase 		= "IgnoreCase";
	}
	static {
		keywords = new ArrayList<>();

		keywords.add(KEY.OrderBy);
	
		keywords.add(KEY.And);

		keywords.add(KEY.Or);
		
		keywords.add(KEY.Between);

		keywords.add(KEY.LessThanEqual);
		
		keywords.add(KEY.LessThan);

		keywords.add(KEY.GreaterThanEqual);
		
		keywords.add(KEY.GreaterThan);

		keywords.add(KEY.After);

		keywords.add(KEY.Before);

		keywords.add(KEY.IsNull);

		keywords.add(KEY.Null);

		keywords.add(KEY.IsNotNull);

		keywords.add(KEY.NotNull);

		keywords.add(KEY.Like);

		keywords.add(KEY.NotLike);

		keywords.add(KEY.StartingWith);
	
		keywords.add(KEY.EndingWith);

		keywords.add(KEY.Containing);

		keywords.add(KEY.NotIn);
		
		keywords.add(KEY.Not);

		keywords.add(KEY.In);
	
		keywords.add(KEY.True);

		keywords.add(KEY.False);

		keywords.add(KEY.IgnoreCase);
		
		keywords.add(KEY.Is);
		
		keywords.add(KEY.Equals);
		
		

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
