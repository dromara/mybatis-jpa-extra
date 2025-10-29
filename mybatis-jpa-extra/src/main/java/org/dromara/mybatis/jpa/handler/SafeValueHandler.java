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
 

package org.dromara.mybatis.jpa.handler;

import org.apache.commons.text.StringEscapeUtils;

public class SafeValueHandler {

	public final class NumberSign{
    	public static final String SYMBOL 		= "#";
    	public static final String REPLACE 		= "0x23";
    }
	
    public final class SingleQuote{
    	public static final String SYMBOL 		= "'";
    	public static final String REPLACE 		= "0x27";
    }
    
    public final class Decrement{
    	public static final String SYMBOL 		= "--";
    	public static final String REPLACE 		= "0x2D0x2D";
    }

	public static String valueOf(Object value) {
		String valueType = value.getClass().getSimpleName().toLowerCase();
		if (valueType.equals("string") 
				|| valueType.equals("char")
				|| valueType.startsWith("char")) {
			return safeValue(value);
		} else if (valueType.equals("int") 
				|| valueType.equals("long") 
				|| valueType.equals("integer")
				|| valueType.equals("float") 
				|| valueType.equals("double")) {
			return value + "";
		} else {
			return safeValue(value);
		}
	}
	
	public static String valueOfType(Object value) {
		String valueType = value.getClass().getSimpleName().toLowerCase();
		if (valueType.equals("string") 
				|| valueType.equals("char")
				|| valueType.startsWith("char")) {
			return "'" + safeValue(value) + "'";
		} else if (valueType.equals("int") 
				|| valueType.equals("long") 
				|| valueType.equals("integer")
				|| valueType.equals("float") 
				|| valueType.equals("double")) {
			return value + "";
		} else {
			return "'" + safeValue(value) + "'";
		}
	}
	
	 /**
     * StringEscapeUtils.escapeHtml4
     * " 转义为 &quot;
     * & 转义为 &amp;
     * < 转义为 &lt;
     * > 转义为 &gt;
     * 
     * 以下符号过滤
     * ' 转义为 &apos;
     */
	public static String safeValue(Object value) {
		String replace = "";
		if(value != null) {
			replace = StringEscapeUtils.escapeHtml4(String.valueOf(value));
			if(replace.indexOf(SingleQuote.SYMBOL) > -1) {
				replace = replace.replace(SingleQuote.SYMBOL, SingleQuote.REPLACE);
			}
			if(replace.indexOf(Decrement.SYMBOL) > -1) {
				replace = replace.replace(Decrement.SYMBOL, Decrement.REPLACE);
			}
			if(replace.indexOf(NumberSign.SYMBOL) > -1) {
				replace = replace.replace(NumberSign.SYMBOL, NumberSign.REPLACE);
			}
		}
		return replace;
	}
	
	/**
	 * Column去除 ' 空格 分号 --
	 * @param column
	 * @return
	 */
	public static String safeColumn(String column) {
		return column.replace("'", "")
				.replace(" ", "")
				.replace(";", "")
				.replace(Decrement.SYMBOL, Decrement.REPLACE);
	}
	
}