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
 

package org.dromara.mybatis.jpa.query;

import java.util.ArrayList;
import java.util.List;

/**
 * Reference https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
 */
public class JpaFindByKeywords {

	static List<String> keywords;
	/**
	 * findBy
	 */
	public static final String FINDBY = "findBy";
	
	/**
	 * findDistinctBy
	 */
	public static final String FINDDISTINCTBY = "findDistinctBy";
	
	static {
		keywords = new ArrayList<>();
		/**
		 *  findDistinctByLastnameAndFirstname
		 *  select distinct …​ where x.lastname = ?1 and x.firstname = ?2
		 */
		keywords.add("And");
		/**
		 * findByLastnameAndFirstname
		 * where x.lastname = ?1 and x.firstname = ?2
		 */
		keywords.add("Or");
		/**
		 * findByLastnameOrFirstname
		 * where x.lastname = ?1 or x.firstname = ?2
		 */
		keywords.add("Is");
		/**
		 * findByFirstname,findByFirstnameIs,findByFirstnameEquals
		 * where x.firstname = ?1
		 */
		keywords.add("Equals");
		/**
		 * findByStartDateBetween
		 * where x.startDate between ?1 and ?2
		 */
		keywords.add("Between");
		/**
		 * findByAgeLessThan
		 * where x.age < ?1
		 */
		keywords.add("LessThan");
		/**
		 * findByAgeLessThanEqual
		 * where x.age <= ?1
		 */
		keywords.add("LessThanEqual");
		/**
		 * findByAgeGreaterThan
		 * where x.age > ?1
		 */
		keywords.add("GreaterThan");
		/**
		 * findByAgeGreaterThanEqual
		 * where x.age >= ?1
		 */
		keywords.add("GreaterThanEqual");
		/**
		 * findByStartDateAfter
		 * where x.startDate > ?1
		 */
		keywords.add("After");
		/**
		 * findByStartDateBefore
		 * where x.startDate < ?1
		 */
		keywords.add("Before");
		/**
		 * findByAge(Is)Null
		 * where x.age is null
		 */
		keywords.add("IsNull");
		/**
		 * same as IsNull
		 */
		keywords.add("Null");
		/**
		 * findByAge(Is)NotNull
		 * where x.age is not null
		 */
		keywords.add("IsNotNull");
		/**
		 * findByAge(Is)NotNull
		 * where x.age is not null
		 */
		keywords.add("NotNull");
		/**
		 * findByFirstnameLike
		 * where x.firstname like ?1
		 */
		keywords.add("Like");
		/**
		 * findByFirstnameNotLike
		 * where x.firstname not like ?1
		 */
		keywords.add("NotLike");
		/**
		 * findByFirstnameStartingWith
		 * where x.firstname like ?1 (parameter bound with appended %)
		 */
		keywords.add("StartingWith");
		/**
		 * findByFirstnameEndingWith
		 * where x.firstname like ?1 (parameter bound with prepended %)
		 */
		keywords.add("EndingWith");
		/**
		 * findByFirstnameContaining
		 * where x.firstname like ?1 (parameter bound wrapped in %)
		 */
		keywords.add("Containing");
		/**
		 * findByAgeOrderByLastnameDesc
		 * where x.age = ?1 order by x.lastname desc
		 */
		keywords.add("OrderBy");
		/**
		 * findByLastnameNot
		 * where x.lastname <> ?1
		 */
		keywords.add("Not");
		/**
		 * findByAgeIn(Collection<Age> ages)
		 * where x.age in ?1
		 */
		keywords.add("In");
		/**
		 * findByAgeNotIn(Collection<Age> ages)
		 * where x.age not in ?1
		 */
		keywords.add("NotIn");
		/**
		 * findByActiveTrue()
		 * where x.active = true
		 */
		keywords.add("True");
		/**
		 * findByActiveFalse()
		 * where x.active = false
		 */
		keywords.add("False");
		/**
		 * findByFirstnameIgnoreCase
		 * where UPPER(x.firstname) = UPPER(?1)
		 */
		keywords.add("IgnoreCase");
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
