/*
 * Copyright [2025] [MaxKey of copyright http://www.maxkey.top]
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
 

/**
 * 
 */
package org.dromara.mybatis.jpa.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Crystal.Sea
 *
 */
public class ConstCaseType{
    
    public static final int     NORMAL                  = 0;
    public static final int     LOWERCASE               = 1;
    public static final int     UPPERCASE               = 2;
        
    public static final String  NORMAL_CHAR             = "normal";
    public static final String  LOWERCASE_CHAR          = "lowercase";
    public static final String  UPPERCASE_CHAR          = "uppercase";
    
    static final Map<String  , Integer> caseTypeMap;
    
    static {
        caseTypeMap = new HashMap<>();
        caseTypeMap.put(NORMAL_CHAR, NORMAL);
        caseTypeMap.put(LOWERCASE_CHAR, LOWERCASE);
        caseTypeMap.put(UPPERCASE_CHAR, UPPERCASE);
    }
    
    public static int getCaseType(String caseType) {
        caseType = caseType.toLowerCase();
        if(caseTypeMap.containsKey(caseType)) {
            return caseTypeMap.get(caseType);
        }
        return NORMAL;
    }

}
