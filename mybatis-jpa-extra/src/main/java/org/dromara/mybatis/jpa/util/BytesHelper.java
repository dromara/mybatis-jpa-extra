/*
 * Copyright [2021] [MaxKey of copyright http://www.maxkey.top]
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

import java.util.Arrays;

public final class BytesHelper {

    private BytesHelper() {
    }

    /**
     * Custom algorithm used to generate an int from a series of bytes.
     * <p/>
     * NOTE : this is different than interpreting the incoming bytes as an int value!
     *
     * @param bytes The bytes to use in generating the int.
     *
     * @return The generated int.
     */
    public static int toInt(byte[] bytes) {
        int result = 0;
        for ( int i = 0; i < 4; i++ ) {
            result = ( result << 8 ) - Byte.MIN_VALUE + (int) bytes[i];
        }
        return result;
    }

    /**
     * Interpret a short as its binary form
     *
     * @param shortValue The short to interpret to binary
     *
     * @return The binary
     */
    public static byte[] fromShort(int shortValue) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) ( shortValue >> 8 );
        bytes[1] = (byte) ( ( shortValue << 8 ) >> 8 );
        return bytes;
    }

    /**
     * Interpret an int as its binary form
     *
     * @param intValue The int to interpret to binary
     *
     * @return The binary
     */
    public static byte[] fromInt(int intValue) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ( intValue >> 24 );
        bytes[1] = (byte) ( ( intValue << 8 ) >> 24 );
        bytes[2] = (byte) ( ( intValue << 16 ) >> 24 );
        bytes[3] = (byte) ( ( intValue << 24 ) >> 24 );
        return bytes;
    }

    /**
     * Interpret a long as its binary form
     *
     * @param longValue The long to interpret to binary
     *
     * @return The binary
     */
    public static byte[] fromLong(long longValue) {
        byte[] bytes = new byte[8];
        fromLong(longValue, bytes, 0);
        return bytes;
    }
    
    /**
     * Interpret a long as its binary form
     *
     * @param longValue The long to interpret to binary
     * @param dest the destination array.
     * @param destPos starting position in the destination array.
     * @return The binary
     */
    public static void fromLong(long longValue, byte[] dest, int destPos) {
        
        dest[destPos] = (byte) ( longValue >> 56 );
        dest[destPos + 1] = (byte) ( ( longValue << 8 ) >> 56 );
        dest[destPos + 2] = (byte) ( ( longValue << 16 ) >> 56 );
        dest[destPos + 3] = (byte) ( ( longValue << 24 ) >> 56 );
        dest[destPos + 4] = (byte) ( ( longValue << 32 ) >> 56 );
        dest[destPos + 5] = (byte) ( ( longValue << 40 ) >> 56 );
        dest[destPos + 6] = (byte) ( ( longValue << 48 ) >> 56 );
        dest[destPos + 7] = (byte) ( ( longValue << 56 ) >> 56 );
    }

    /**
     * Interpret the binary representation of a long.
     *
     * @param bytes The bytes to interpret.
     *
     * @return The long
     */
    public static long asLong(byte[] bytes) {
        return asLong(bytes, 0);
    }
    
    /**
     * Interpret the binary representation of a long.
     *
     * @param bytes The bytes to interpret.
     * @param srcPos starting position in the source array.
     *
     * @return The long
     */
    public static long asLong(byte[] bytes, int srcPos) {
        if ( bytes == null ) {
            return 0;
        }
        final int size = srcPos + 8;
        if ( bytes.length < size ) {
            throw new IllegalArgumentException( "Expecting 8 byte values to construct a long" );
        }
        long value = 0;
        for (int i=srcPos; i<size; i++) {
            value = (value << 8) | (bytes[i] & 0xff);
        }
        return value;
    }

    public static String toBinaryString(byte value) {
        String formatted = Integer.toBinaryString( value );
        if ( formatted.length() > 8 ) {
            formatted = formatted.substring( formatted.length() - 8 );
        }
        StringBuilder buf = new StringBuilder( "00000000" );
        buf.replace( 8 - formatted.length(), 8, formatted );
        return buf.toString();
    }

    public static String toBinaryString(int value) {
        String formatted = Long.toBinaryString( value );
        StringBuilder buf = new StringBuilder( repeat( '0', 32 ) );
        buf.replace( 64 - formatted.length(), 64, formatted );
        return buf.toString();
    }

    public static String toBinaryString(long value) {
        String formatted = Long.toBinaryString( value );
        StringBuilder buf = new StringBuilder( repeat( '0', 64 ) );
        buf.replace( 64 - formatted.length(), 64, formatted );
        return buf.toString();
    }
    
    public static String repeat(String string, int times) {
        StringBuilder buf = new StringBuilder( string.length() * times );
        for ( int i = 0; i < times; i++ ) {
            buf.append( string );
        }
        return buf.toString();
    }
    
    public static String repeat(String string, int times, String deliminator) {
        StringBuilder buf = new StringBuilder( ( string.length() * times ) + ( deliminator.length() * ( times - 1 ) ) )
                .append( string );
        for ( int i = 1; i < times; i++ ) {
            buf.append( deliminator ).append( string );
        }
        return buf.toString();
    }

    public static String repeat(char character, int times) {
        char[] buffer = new char[times];
        Arrays.fill( buffer, character );
        return new String( buffer );
    }
}
