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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class StrUtils {
	public static final char UNDERLINE = '_';

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

	public static List<String> stringToList(String string, String split) {
		String[] strs = {};
		if (string != null && !string.equals("")) {
			strs = string.split(split);
		}
		ArrayList<String> resultList = new ArrayList<>(0);
		for (int i = 0; i < strs.length; i++) {
			if (strs[i] != null&& !strs[i].equals("")) {
				resultList.add(strs[i]);
			}
		}
		resultList.trimToSize();
		return resultList;
	}

	public static String listToString(List<String> list, String split) {
		StringBuilder string = new StringBuilder("");
		if (CollectionUtils.isNotEmpty(list)) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) != null && !list.get(i).equals("")) {
					string.append(list.get(i)).append( split );
				}
			}
			//删除最后一个分隔符
			if (StringUtils.endsWith(string,split)) {
				string = string.deleteCharAt(string.length() - 1);
			}
		}
		return string.toString();
	}

	public static String lineBreakToBlank(String sql) {
		return 	sql.replaceAll("\r\n+", " \n")
				   .replaceAll("\n+", " \n")
				   .replaceAll("\t", " ")
				   .replaceAll(" +"," ");
	}
	
    /**
     * 驼峰格式字符串转换为下划线格式字符串
     *
     * @param param
     * @return
     */
    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(UNDERLINE);
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    /**
     * 下划线格式字符串转换为驼峰格式字符串
     *
     * @param param
     * @return
     */
    public static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    /**
     * 下划线格式字符串转换为驼峰格式字符串2
     *
     * @param param
     * @return
     */
    public static String underlineToCamel2(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        StringBuilder sb = new StringBuilder(param);
        Matcher mc = Pattern.compile("_").matcher(param);
        int i = 0;
        while (mc.find()) {
            int position = mc.end() - (i++);
            sb.replace(position - 1, position + 1, sb.substring(position, position + 1).toUpperCase());
        }
        return sb.toString();
    }
}
