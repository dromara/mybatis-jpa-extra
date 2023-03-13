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
 

package org.apache.mybatis.jpa.id;

import java.util.Date;
import org.apache.mybatis.jpa.util.MacAddress;
import org.slf4j.LoggerFactory;

public class SerialGenerator  implements IdentifierGenerator{

	/**
	 * 历史时间
	 */
	public static  	String 	OLD_DATETIME="";
	//当前序列
	public static int 		STATIC_SEQUENCE=0;
	//节点
	public static String 	STATIC_NODE_NUMBER="--";
	//配置信息属性
	public String ipAddressNodeValue="";
	
	public String generate(Object object) {
		return  next();
	}
	
	/**
	 * 生成20位的流水号
	 * @return 流水号
	 */
	public  synchronized String next(){
		String currentDateTime=getCurrentSystemDateTime();
		
		if(null==currentDateTime){
			LoggerFactory.getLogger(SerialGenerator.class).error("获取系统日期失败");
			return null;
		}
		
		StringBuffer sequenceNumber=new StringBuffer();
		
		sequenceNumber.append(currentDateTime.substring(0, 8));
		sequenceNumber.append(getNodeNumber());
		sequenceNumber.append(currentDateTime.substring(8));
		sequenceNumber.append(nextSequence());
		return sequenceNumber.toString();
	}
	
	public  final String getNodeNumber(){
		if(STATIC_NODE_NUMBER.equals("--")){
			LoggerFactory.getLogger(SerialGenerator.class).info("ipAddressNodeValue : "+ipAddressNodeValue);
			if(ipAddressNodeValue.indexOf(",")>-1){
				
				String hostIpAddress=MacAddress.getAllHostMacAddress();//获得本机IP
				
				LoggerFactory.getLogger(SerialGenerator.class).info("hostIpAddress : "+hostIpAddress);
				
				String []ipAddressValues=ipAddressNodeValue.split(",");
				for(String ipvalue : ipAddressValues){
					String[] ipNode=ipvalue.split("=");
					if(ipNode!=null&&ipNode.length>0&&hostIpAddress.indexOf(ipNode[0])>-1){
						STATIC_NODE_NUMBER=ipNode[1];
					}
				}
			}else{
				STATIC_NODE_NUMBER="01";
			}
			
			LoggerFactory.getLogger(SerialGenerator.class).info("STATIC_NODE_SEQUENCE_NUMBER : "+STATIC_NODE_NUMBER);
			if(STATIC_NODE_NUMBER.length()!=2){
				LoggerFactory.getLogger(SerialGenerator.class).error("系统节点号必须2位");
			}
		}
		return STATIC_NODE_NUMBER;
	}
	/**
	 * 同一时刻只有一个访问
	 * @return
	 */
	private  final  synchronized String nextSequence(){
		STATIC_SEQUENCE=(STATIC_SEQUENCE+1)%10000;
		return String.format("%04d", STATIC_SEQUENCE);
	}
	
	/**
	 * 获取系统当前日期，格式为yyyyMMddHHmmSS
	 * @return 当前系统日期
	 */
	private   synchronized String  getCurrentSystemDateTime(){
		String currentdatetime=null;
		synchronized(OLD_DATETIME)
		{
			currentdatetime=(new java.text.SimpleDateFormat("yyyyMMddHHmmss")).format(new Date());
			/**
			 * 判断是否是新的时间，如果是新时间则STATIC_SEQUENCE从0开始计数
			 */
			if(!currentdatetime.equals(OLD_DATETIME)){
				STATIC_SEQUENCE=0;
				OLD_DATETIME=currentdatetime;
			}
		}
		return currentdatetime;
	}

	public String getIpAddressNodeValue() {
		return ipAddressNodeValue;
	}

	public void setIpAddressNodeValue(String ipAddressNodeValue) {
		this.ipAddressNodeValue = ipAddressNodeValue;
		getNodeNumber();
	}
	
}
