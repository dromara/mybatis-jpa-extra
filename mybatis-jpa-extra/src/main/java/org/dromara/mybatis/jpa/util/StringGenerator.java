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
 

package org.dromara.mybatis.jpa.util;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.dromara.mybatis.jpa.crypto.utils.Base64Utils;


public class StringGenerator {

    private static final char[] DEFAULT_CODEC 		= "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    static final char[] DEFAULT_CODE_NUMBER 		= "1234567890".toCharArray();

    static final char[] DEFAULT_CODE_LOWERCASE 		= "abcdefghijklmnopqrstuvwxyz".toCharArray();

    static final char[] DEFAULT_CODE_UPPERCASE 		= "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    static final char[] DEFAULT_CODE_LETTERS 		= "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    static final char[] DEFAULT_CODE_NUMBER_LETTERS = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    static final String UUID_REGEX 					=  "^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$";
    
    private char[] codec 							= DEFAULT_CODEC;
    
    private Random random 							= new SecureRandom();

    private int length;
    
    /**
     * Create a generator with the default length (6).
     */
    public StringGenerator() {
        this(6);
    }

    /**
     * Create a generator of random strings of the length provided.
     * 
     * @param length the length of the strings generated
     */
    public StringGenerator(int length) {
        this.length = length;
    }

    public StringGenerator(char[] defaultCode, int length) {
        this.codec = defaultCode;
        this.length = length;
    }

    /**
     * randomGenerate.
     * @return
     */
    public String randomGenerate() {
        byte[] verifierBytes = new byte[length];
        random.nextBytes(verifierBytes);
        return getString(verifierBytes);
    }

    public String uuidGenerate() {
        return UUID.randomUUID().toString().toLowerCase();
    }

    /**
     * uniqueGenerate.
     * @return
     */
    public String uniqueGenerate() {
        StringBuilder uniqueString = new StringBuilder("");

        this.length = 9;
        String randomString = randomGenerate();
        uniqueString.append(randomString.subSequence(0, 4));

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmssSSS");
        String dateString = Base64Utils.encodeBase64(dateFormat.format(currentDate).getBytes());
        dateString = dateString.substring(0, dateString.length() - 1);
        uniqueString.append(dateString);

        uniqueString.append(randomString.subSequence(5, 8));

        return uniqueString.toString();
    }
    

    /**
     * Convert these random bytes to a verifier string. The length of the byte array
     * can be {@link #setLength(int) configured}. The default implementation mods
     * the bytes to fit into the ASCII letters 1-9, A-Z, a-z .
     * 
     * @param verifierBytes The bytes.
     * @return The string.
     */
    protected String getString(byte[] verifierBytes) {
        char[] chars = new char[verifierBytes.length];
        for (int i = 0; i < verifierBytes.length; i++) {
            chars[i] = codec[((verifierBytes[i] & 0xFF) % codec.length)];
        }
        return new String(chars);
    }

    /**
     * The random value generator used to create token secrets.
     * 
     * @param random The random value generator used to create token secrets.
     */
    public void setRandom(Random random) {
        this.random = random;
    }

    /**
     * The length of string to generate.
     * 
     * @param length the length to set
     */
    public void setLength(int length) {
        this.length = length;
    }

    public void setCodec(char[] codec) {
        this.codec = codec;
    }

    public static boolean uuidMatches(String uuidString) {
        return uuidString.matches(UUID_REGEX);
    }

}
