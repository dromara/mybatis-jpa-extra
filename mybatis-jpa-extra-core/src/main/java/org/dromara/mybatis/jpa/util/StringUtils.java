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

import java.util.ArrayList;
import java.util.List;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

	public static boolean isNumeric(String val) {
		char[] chars = val.toCharArray();
		if (chars.length == 0) {
			return false;
		}
		for (int i = chars.length - 1; i >= 0; i--) {
			char c = chars[i];
			if (c < '0' || c > '9') {
				return false;
			}
		}
		return true;
	}

	public static long lastNumber(String val) {
		long num = 0;
		char[] chars = val.toCharArray();
		if (chars.length == 0) {
			throw new NumberFormatException(val);
		}
		long j = 1;
		for (int i = chars.length - 1; i >= 0; i--, j *= 10) {
			char c = chars[i];
			if (c >= '0' && c <= '9') {
				int bit = c - '0';
				num += bit * j;
			} else {
				break;
			}
		}
		return num;
	}
	
	public static String firstToLowerCase(String str) {
		return str.toLowerCase().charAt(0) + str.substring(1);
	}

	public static List<String> string2List(String string, String split) {
		String[] strs = {};
		if (string != null && !string.equals("")) {
			strs = string.split(split);
		}
		ArrayList<String> resultList = new ArrayList<String>(0);
		for (int i = 0; i < strs.length; i++) {
			if (strs[i] != null&& !strs[i].equals("")) {
				resultList.add(strs[i]);
			}
		}
		resultList.trimToSize();
		return resultList;
	}

	public static String list2String(List<String> list, String split) {
		String string = "";
		if (list == null)
			return string;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) != null && !list.get(i).equals("")) {
				string += list.get(i) + split;
			}
		}
		if (string.length() > 0) {
			string = string.substring(0, string.length() - 1);
		}
		return string;
	}

	public static String lineBreak2Blank(String sql) {
		return 	sql.replaceAll("\r\n+", " \n")
				   .replaceAll("\n+", " \n")
				   .replaceAll("\t", " ")
				   .replaceAll(" +"," ");
	}
}
