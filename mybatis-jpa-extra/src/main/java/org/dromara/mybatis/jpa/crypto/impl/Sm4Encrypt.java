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

package org.dromara.mybatis.jpa.crypto.impl;

import java.sql.SQLException;

import org.dromara.mybatis.jpa.crypto.SymmetricEncrypt;
import org.dromara.mybatis.jpa.crypto.utils.ReciprocalUtils;
import org.dromara.mybatis.jpa.crypto.utils.SM4Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sm4Encrypt implements SymmetricEncrypt{
    private static final  Logger logger = LoggerFactory.getLogger(Sm4Encrypt.class);
    
    static Sm4Encrypt encrypt;
    
    static final String PREFIX         = "{sm4ec}";
    
    static final String PLAIN         = "{plain}";
    
    String salt                       = "3e4a1d42a9b6147e6445158a06d13575";
    
    static final int PREFFIX_LENGTH = 7;
    
    public Sm4Encrypt() {
        
    }
    
    public Sm4Encrypt(String cryptKey) {
        this.salt = ReciprocalUtils.cutSecretKey(cryptKey, ReciprocalUtils.Algorithm.SM4);
    }

    public static Sm4Encrypt getInstance() {
        if (encrypt == null) {
            encrypt = new Sm4Encrypt();
        }
        return encrypt;
    }
    
    @Override
    public String decrypt(String ciphers) throws SQLException {
        if(ciphers == null) {
            return null;
        }
        
        String encodedPasswordString  = ciphers;
        if(encodedPasswordString.startsWith(PREFIX)) {
            try {
                return SM4Utils.decryptHex_ECB(encodedPasswordString.substring(PREFFIX_LENGTH), salt, SM4Utils.Padding.PKCS7);
            } catch (Exception e) {
                logger.error("decryptHex_Sm4 Exception", e);
            }
        }else if(encodedPasswordString.startsWith(PLAIN)) {
            return encodedPasswordString.substring(PREFFIX_LENGTH);
        }
        return encodedPasswordString;
    }

    @Override
    public String encrypt(String simple) throws SQLException {
        return encodeToHex(simple);
    }
    

    public String encode(CharSequence plain,boolean isEncode) {
        if(isEncode) {
            return encodeToHex(plain);
        }else {
            return PLAIN + plain;
        }
    }
    
    private String encodeToHex(CharSequence plain ) {
        if(plain == null) {
            return null;
        }
        
        try {
            return (PREFIX + SM4Utils.encryptHex_ECB(plain.toString(), salt, SM4Utils.Padding.PKCS7));
        } catch (Exception e) {
            logger.error("encryptHex_Sm4 Exception", e);
        }
        return "";
    }
    
    public  String getSalt() {
        return salt;
    }
    
    @Override
    public  void setSalt(String salt) {
        this.salt = salt;
    }

}
