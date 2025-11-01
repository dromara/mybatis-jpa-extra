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
 

package org.dromara.mybatis.jpa.crypto.utils;

import java.nio.charset.StandardCharsets;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reciprocal cipher or Symmetric-key algorithm
 * 
 * algorithm Support DES,DESede,Blowfish and AES
 * 
 * default key value use ReciprocalUtils.defaultKey
 * 
 * generateKey is generate random key for algorithm
 * 
 * @author Crystal.Sea
 * 
 */
public final class ReciprocalUtils {
    private static final  Logger logger = LoggerFactory.getLogger(ReciprocalUtils.class);
    
    public static final String DEFAULT_KEY         = "l0JqT7NvIzP9oRaG4kFc1QmD_bWu3x8E5yS2h6"; //

    public final class Algorithm {
        public static final String DES             = "DES";
        public static final String TRIPLE_DES     = "DESede";
        public static final String BLOWFISH     = "Blowfish";
        public static final String AES             = "AES";
        public static final String SM4             = "SM4";
    }

/*
    static {
        if(System.getProperty("java.version").startsWith("1.8")) {
            try {
                Security.addProvider((Provider)InstanceUtil.newInstance("com.sun.crypto.provider.SunJCE"));
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        //else not need to add see jdk-17\conf\security\java.security,SunJCE
    }
*/   
    public static byte[] encode(byte[] simpleBytes, SecretKey secretKey, String algorithm) {
        // Create the ciphers
        Cipher ecipher;
        byte[] byteFinal = null;
        try {
            ecipher = Cipher.getInstance(secretKey.getAlgorithm());
            // Encode the string into bytes using utf-8
            ecipher.init(Cipher.ENCRYPT_MODE, secretKey);
            // Encrypt
            byteFinal = ecipher.doFinal(simpleBytes);
            return byteFinal;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteFinal;
    }

    /**
     * @param simple
     * @param secretKey must length
     * @return
     * @throws Exception
     */
    public static byte[] encode(String simple, String secretKey, String algorithm) {
        if (StringUtils.isNotBlank(simple) && checkKeyLength(secretKey, algorithm)) {
            SecretKey key = generatorKey(secretKey, algorithm);
            return encode(simple.getBytes(StandardCharsets.UTF_8), key, algorithm);
        }
        return null;
    }

    public static byte[] decoder(byte[] ciphersBytes, SecretKey secretKey, String algorithm) {
        Cipher cipher;
        byte[] byteFinal = null;
        try {
            cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byteFinal = cipher.doFinal(ciphersBytes);
            return byteFinal;
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return null;
    }

    public static String decoder(byte[] ciphersBytes, String secretKey, String algorithm) {
        if (checkKeyLength(secretKey, algorithm)) {
            SecretKey key = generatorKey(secretKey, algorithm);
            byte[] decoderBytes = decoder(ciphersBytes, key, algorithm);
            
            return decoderBytes == null ? null : new String(decoderBytes, StandardCharsets.UTF_8);
        }
        return null;
    }

    public static String generatorDefaultKey(String secretKey,String algorithm) {
        try {
            secretKey = secretKey + DEFAULT_KEY;
            if (algorithm.equals(Algorithm.DES)) {
                secretKey = secretKey.substring(0, 8);
            } else if (algorithm.equals(Algorithm.AES) || algorithm.equals(Algorithm.BLOWFISH)) {
                secretKey = secretKey.substring(0, 16);
            } else if (algorithm.equals(Algorithm.TRIPLE_DES)) {
                secretKey = secretKey.substring(0, 24);
            }
           return secretKey;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static SecretKey generatorKey(String secretKey, String algorithm) {
        try {
            return new SecretKeySpec(secretKey.getBytes(), algorithm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encode2Hex(String simple, String secretKey, String algorithm) {
        if (checkKeyLength(secretKey, algorithm)) {
            byte[] cipher = encode(simple, secretKey, algorithm);
            // Encode bytes to HEX to get a string
            return HexUtils.bytes2HexString(cipher);
        }
        return null;
    }

    public static String decoderHex(String ciphers, String secretKey, String algorithm) {
        if(StringUtils.isBlank(ciphers))return "";
        
        if (checkKeyLength(secretKey, algorithm)) {
            byte[] byteSimple = HexUtils.hex2Bytes(ciphers);

            return decoder(byteSimple, secretKey, algorithm);
        }
        return null;
    }
    
    public static String encode2Hex(String simple, String secretKey) {
        String key = generatorDefaultKey(secretKey + DEFAULT_KEY,Algorithm.TRIPLE_DES);
        return encode2Hex(simple,key, Algorithm.TRIPLE_DES);
    }

    public static String decoderHex(String ciphers, String secretKey) {
        String key = generatorDefaultKey(secretKey + DEFAULT_KEY,Algorithm.TRIPLE_DES);
        return decoderHex(ciphers,key,Algorithm.TRIPLE_DES);
    }
    
    private static boolean checkKeyLength(String secretKey, String algorithm) {
        boolean lengthCheck = false;
        if (algorithm.equals(Algorithm.DES)) {
            if (secretKey.length() == 8) {
                lengthCheck = true;
            } else {
                logger.error("DES key length is  {} ,must lequal 8",secretKey.getBytes().length);
            }
        } else if (algorithm.equals(Algorithm.TRIPLE_DES)) {
            if (secretKey.length() == 24) {
                lengthCheck = true;
            } else {
                logger.error("DESede key length is  {} ,must equal 24",secretKey.getBytes().length);
            }
        } else if (algorithm.equals(Algorithm.AES)) {
            if (secretKey.length() == 16) {
                lengthCheck = true;
            } else {
                logger.error("AES key length is  {} ,must equal 16",secretKey.getBytes().length);
            }
        } else if (algorithm.equals(Algorithm.BLOWFISH)) {
            if (secretKey.length() <= 16) {
                lengthCheck = true;
            } else {
                logger.error("Blowfish key length is {} ,must be less then 16",secretKey.getBytes().length);
            }
        }
        return lengthCheck;
    }
    
    
    public static String cutSecretKey(String secretKey, String algorithm) {
        if (algorithm.equals(Algorithm.DES)) {
            return secretKey.substring(0, 8);
        } else if (algorithm.equals(Algorithm.TRIPLE_DES)) {
            return secretKey.substring(0, 24);
        } else if (algorithm.equals(Algorithm.AES)) {
            return secretKey.substring(0, 16);
        } else if (algorithm.equals(Algorithm.BLOWFISH)) {
            return secretKey.substring(0, 16);
        } else if (algorithm.equals(Algorithm.SM4)) {
            return secretKey.substring(0, 32);
        }else {
            return secretKey;
        }
    }

    /**
     * @param simple
     * @param secretKey must length is 16
     * @return
     */
    public static String aesEncode(String simple, String secretKey) {
        return encode2Hex(simple, secretKey, Algorithm.AES);
    }

    public static String aesDecoder(String ciphers, String secretKey) {
        return decoderHex(ciphers, secretKey, Algorithm.AES);
    }
/**
    public static String generateKey(String algorithm) {
        if (algorithm.equals(Algorithm.DES)) {
            return (new StringGenerator(8)).randomGenerate();
        } else if (algorithm.equals(Algorithm.AES)) {
            return (new StringGenerator(16)).randomGenerate();
        } else if (algorithm.equals(Algorithm.BLOWFISH)) {
            return (new StringGenerator(16)).randomGenerate();
        } else if (algorithm.equals(Algorithm.TRIPLE_DES)) {
            return (new StringGenerator(24)).randomGenerate();
        } else {
            return (new StringGenerator()).uniqueGenerate();
        }
    }
    **/
}
