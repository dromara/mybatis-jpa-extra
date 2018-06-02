package org.apache.mybatis.jpa.id;
import java.net.InetAddress;

import org.apache.mybatis.jpa.util.BytesHelper;

public class UUIDHexGenerator implements IdentifierGenerator{
	private String sep = "-";

	public String generate(Object object) {
		return format( getIP() ) + sep
				+ format( getJVM() ) + sep
				+ format( getHiTime() ) + sep
				+ format( getLoTime() ) + sep
				+ format( getCount() );
	}
	
	protected String format(int intValue) {
		String formatted = Integer.toHexString( intValue );
		StringBuilder buf = new StringBuilder( "00000000" );
		buf.replace( 8 - formatted.length(), 8, formatted );
		return buf.toString();
	}

	protected String format(short shortValue) {
		String formatted = Integer.toHexString( shortValue );
		StringBuilder buf = new StringBuilder( "0000" );
		buf.replace( 4 - formatted.length(), 4, formatted );
		return buf.toString();
	}
	
	private static final int IP;
	static {
		int ipadd;
		try {
			ipadd = BytesHelper.toInt( InetAddress.getLocalHost().getAddress() );
		}
		catch (Exception e) {
			ipadd = 0;
		}
		IP = ipadd;
	}

	private static short counter = (short) 0;
	private static final int JVM = (int) ( System.currentTimeMillis() >>> 8 );
	
	/**
	 * Unique across JVMs on this machine (unless they load this class
	 * in the same quater second - very unlikely)
	 */
	protected int getJVM() {
		return JVM;
	}

	/**
	 * Unique in a millisecond for this JVM instance (unless there
	 * are > Short.MAX_VALUE instances created in a millisecond)
	 */
	protected short getCount() {
		synchronized(UUIDHexGenerator.class) {
			if ( counter < 0 ) {
				counter=0;
			}
			return counter++;
		}
	}

	/**
	 * Unique in a local network
	 */
	protected int getIP() {
		return IP;
	}

	/**
	 * Unique down to millisecond
	 */
	protected short getHiTime() {
		return (short) ( System.currentTimeMillis() >>> 32 );
	}

	protected int getLoTime() {
		return (int) System.currentTimeMillis();
	}

}
