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
 

package org.dromara.mybatis.jpa.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 加密注解 <br>
 * 支持SM4 , AES , DES , DESede<br>
 * algorithm-加密算法，默认加密算法AES
 */
@Target( {FIELD, METHOD} )
@Retention( RUNTIME )
public @interface Encrypted {
    /**
     * @return Encrypt algorithm , SM4 , AES , DES , DESede
     */
    String algorithm() default "AES" ;
    
    /**
     * When true try to use DB encryption rather than local java encryption.
     */
    boolean dbEncrypt() default false;
    
}