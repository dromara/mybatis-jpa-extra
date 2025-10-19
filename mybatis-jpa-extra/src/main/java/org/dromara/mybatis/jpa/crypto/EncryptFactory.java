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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dromara.mybatis.jpa.crypto.impl.AesEncrypt;
import org.dromara.mybatis.jpa.crypto.impl.DesEncrypt;
import org.dromara.mybatis.jpa.crypto.impl.DesedeEncrypt;
import org.dromara.mybatis.jpa.crypto.impl.Sm4Encrypt;
import org.dromara.mybatis.jpa.crypto.utils.ReciprocalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Encrypted just SM4 , AES , DES , DESede
 */
public class EncryptFactory {
	private static final Logger logger 	= 	LoggerFactory.getLogger(EncryptFactory.class);
	
	Map<String , SymmetricEncrypt> encryptor = new HashMap<>();
	
	public EncryptFactory(){
		encryptor.put(ReciprocalUtils.Algorithm.AES.toLowerCase(), new AesEncrypt());
		encryptor.put(ReciprocalUtils.Algorithm.DES.toLowerCase(), new DesEncrypt());
		encryptor.put(ReciprocalUtils.Algorithm.TRIPLE_DES.toLowerCase(), new DesedeEncrypt());
		encryptor.put(ReciprocalUtils.Algorithm.SM4.toLowerCase(), new Sm4Encrypt());
		logger.debug("Encryptor {}",encryptor);
	}
	
	
	public EncryptFactory(String cryptKey){
		if(StringUtils.isBlank(cryptKey)) {
			encryptor.put(ReciprocalUtils.Algorithm.AES.toLowerCase(), new AesEncrypt());
			encryptor.put(ReciprocalUtils.Algorithm.DES.toLowerCase(), new DesEncrypt());
			encryptor.put(ReciprocalUtils.Algorithm.TRIPLE_DES.toLowerCase(), new DesedeEncrypt());
			encryptor.put(ReciprocalUtils.Algorithm.SM4.toLowerCase(), new Sm4Encrypt());
		}else {
			encryptor.put(ReciprocalUtils.Algorithm.AES.toLowerCase(), new AesEncrypt(cryptKey));
			encryptor.put(ReciprocalUtils.Algorithm.DES.toLowerCase(), new DesEncrypt(cryptKey));
			encryptor.put(ReciprocalUtils.Algorithm.TRIPLE_DES.toLowerCase(), new DesedeEncrypt(cryptKey));
			encryptor.put(ReciprocalUtils.Algorithm.SM4.toLowerCase(), new Sm4Encrypt(cryptKey));
		}
		logger.debug("Encryptor {}",encryptor);
	}
	
	public SymmetricEncrypt getEncryptor(String algorithm) {
		return encryptor.get(algorithm.toLowerCase());
	}
}
