package org.dromara.mybatis.jpa.test.dao.service;

import java.util.List;

import org.dromara.mybatis.jpa.IJpaService;
import org.dromara.mybatis.jpa.test.entity.Students;

public interface StudentsService extends IJpaService<Students>{

	public List<Students> findByStdNo(String stdNo);
	
	public List<Students> findByStdNoIs(String stdNo);
	
	public List<Students> findByStdNoEquals(String stdNo);
	
	public List<Students> findByStdAgeBetween(int ageStart,int ageEnd);
	
	public List<Students> findByStdAgeLessThan(int ageLessThan);
	
	public List<Students> findByStdAgeLessThanEqual(int ageLessThanEqual);
	
	public List<Students> findByStdAgeAfter(int ageAfter);
	
	public List<Students> findByStdAgeBefore(int ageBefore);
	
	public List<Students> findByImagesNull();
	
	public List<Students> findByImagesIsNull();
	
	public List<Students> findByImagesIsNotNull();
	
	public List<Students> findByImagesNotNull();
	
	public List<Students> findByStdNameLike(String stdName);
	
	public List<Students> findByStdNameNotLike(String stdName);
	
	public List<Students> findByStdNameStartingWith(String stdName);
	
	public List<Students> findByStdNameEndingWith(String stdName);
	
	public List<Students> findByStdNameContaining(String stdName);
	
	public List<Students> findByStdGenderOrderByStdAge(String stdGender);
	
	public List<Students> findByStdGenderIsOrderByStdAge(String stdGender);
	
	public List<Students> findByStdMajorIn(String... stdMajors) ;
	
	public List<Students> findByStdMajorNotIn(List<String> stdMajors);
	
	public List<Students> findByDeletedTrue();
	
	public List<Students> findByDeletedFalse();
	
	public List<Students> findByStdGenderIgnoreCase(String stdGender);
	
	public List<Students> findByStdNoNot(String stdNo);
	
	public List<Students> findByStdMajorAndStdClass(String stdMajor,String stdClass);
	
	public int updatePassword(Students std);
}
