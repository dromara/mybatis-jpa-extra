package org.apache.mybatis.jpa.test.domain;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;

import org.apache.mybatis.jpa.persistence.JpaBaseDomain;
import org.apache.mybatis.jpa.util.JsonDateTimeDeserializer;
import org.apache.mybatis.jpa.util.JsonDateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoDomain extends JpaBaseDomain{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5918892823601207632L;

	public final static String TABLE_KEY="ID";
	
	protected final static Logger _logger = LoggerFactory.getLogger(DemoDomain.class);
	
	/**
	 * Domain id
	 */
	@Id
	@Column
	protected String 	id;

	/**
	 * description
	 */
	protected String  	description;
	
	protected int 		status;
	
	protected String 	createdBy;
	
	@JsonSerialize(using=JsonDateTimeSerializer.class)
	@JsonDeserialize(using=JsonDateTimeDeserializer.class)
	protected Date 		createdDate;
	
	protected String 	modifiedBy;
	
	@JsonSerialize(using=JsonDateTimeSerializer.class)
	@JsonDeserialize(using=JsonDateTimeDeserializer.class)
	protected Date 		modifiedDate;
	
	protected String 	startDate;
	protected String 	endDate;

	
	public DemoDomain() {
		
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}




	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}





	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}




	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}






	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}




	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}




	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}




	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}




	/**
	 * @return the modifiedBy
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}




	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}




	/**
	 * @return the modifiedDate
	 */
	public Date getModifiedDate() {
		return modifiedDate;
	}




	/**
	 * @param modifiedDate the modifiedDate to set
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}




	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}




	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}




	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}




	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}




	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}


	public String generateId() {
		return UUID.randomUUID().toString().toLowerCase();
	}


	@Override
	public String toString() {
		return "BaseDomain [id=" + id 
				+ ", status=" + status + ", sortOrder=" + sortOrder
				+ ", createdBy=" + createdBy + ", createdDate=" + createdDate
				+ ", modifiedBy=" + modifiedBy + ", modifiedDate="
				+ modifiedDate + ", description=" + description
				+ ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}
}
