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
 

package org.dromara.mybatis.jpa.crypto;

import java.sql.SQLException;

import org.dromara.mybatis.jpa.crypto.impl.Sm4Encrypt;
import org.junit.jupiter.api.Test;

public class Sm4EncryptTest {

    @Test
    void encrypt() throws SQLException{
        SymmetricEncrypt se = new Sm4Encrypt();
        String simple = "shimingxy";
        String ciphers = se.encrypt(simple);
        
        System.out.println(ciphers);
        
        System.out.println(se.decrypt(ciphers));
    }
}
