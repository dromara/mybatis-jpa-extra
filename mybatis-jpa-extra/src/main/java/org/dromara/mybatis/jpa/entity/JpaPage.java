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
 

/**
 * 
 */
package org.dromara.mybatis.jpa.entity;

import java.util.UUID;

import org.dromara.mybatis.jpa.id.IdStrategy;
import org.dromara.mybatis.jpa.id.IdentifierGeneratorFactory;
import org.dromara.mybatis.jpa.metadata.MapperMetadata;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Transient;

/**
 * Pagination for database pagination
 * @author Crystal.Sea
 *
 */
public class JpaPage {
	
	public static final int MAX_RESULTS = 10000;

	@JsonIgnore
	@Transient
	protected int rows;
	/**
	 * 
	 */
	@JsonIgnore
	@Transient
	protected int pageSize = 20;
	/**
	 * 
	 */
	@JsonIgnore
	@Transient
	protected int pageNumber = 1;
	/**
	 * 
	 */
	@JsonIgnore
	@Transient
	protected int startRow;
	/**
	 * 
	 */
	@JsonIgnore
	@Transient
	protected int endRow;
	
	@JsonIgnore
	@Transient
	protected String sidx;
	/**
	 * 
	 */
	@JsonIgnore
	@Transient
	protected String sortOrder;
	/**
	 * 
	 */
	@JsonIgnore
	@Transient
	protected String sortKey;
	/**
	 * 
	 */
	@JsonIgnore
	@Transient
	protected String orderBy;
	/**
	 * @serialField pageable
	 */
	@JsonIgnore
	@Transient
	protected boolean pageable = false;
	
	@JsonIgnore
	@Transient
	protected String    pageSelectId;
	
	
	public JpaPage(){}
	
	public JpaPage(int pageNumber ){
		this.pageNumber = pageNumber;
		this.pageable = true;
	}
	
	public JpaPage(int pageNumber , int pageSize){
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.pageable = true;
	}

	
	public String generateId() {
		if(MapperMetadata.getIdentifierGeneratorFactory() != null) {
			return IdentifierGeneratorFactory.generate(IdStrategy.DEFAULT);
		}else {
			return UUID.randomUUID().toString().toLowerCase();
		}
	}
	
	
	/**
	 * calculate StartRow
	 * @param page
	 * @param pageResults
	 * @return
	 */
	protected Integer calculateStartRow(Integer page,Integer pageSize){
		return (page - 1) * pageSize;
	}
	
	public void build() {
		this.pageSelectId= generateId();
		this.startRow= calculateStartRow(this.pageNumber ,this.pageSize);
		this.pageable = true;
	}
	
	@JsonIgnore
	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
		this.pageSize = rows;
		calculate();
	}
	
	@JsonIgnore
	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
		calculate();
	}
	
	@JsonIgnore
	public String getSidx() {
		return sidx;
	}

	public void setSidx(String sidx) {
		this.sidx = sidx;
		setSortKey();
	}
	@JsonIgnore
	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
		setSortKey();
	}

	@JsonIgnore
	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow 	= startRow;
		
	}
	
	@JsonIgnore
	public int getEndRow() {
		return endRow;
	}

	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	public void calculate(int startRow) {
		if(startRow <= pageSize) {
			this.startRow = 0;
			setPageNumber(1);
		}else {
			setPageNumber(startRow/pageSize + (startRow%pageSize == 0 ? 0 : 1));
		}
	}
	
	public void calculate() {
		if (this.pageNumber >= 1 && this.pageSize > 0){
			startRow = (this.pageNumber - 1) * this.pageSize;
			endRow = startRow + this.pageSize;
		}else {
			startRow = 0;
			endRow	 = this.pageSize;
		}
	}
	
	@JsonIgnore
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		if(pageSize == -1 || pageSize > MAX_RESULTS) {
			pageSize = MAX_RESULTS;
		}
		this.pageSize = pageSize;
	}
	
	@JsonIgnore
	public String getSortKey() {
		return sortKey;
	}

	/**
	 * create sortKey from sidx & sord,eg  order by  name asc 
	 */
	public void setSortKey() {
		if(sidx!=null	&&	sortOrder!=null	&&	!sidx.equals("")	&&	!sortOrder.equals("")){
			sortKey=" "+sidx+" "+sortOrder+" ";
			setOrderBy();
		}
		
	}
	
	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}
	@JsonIgnore
	public String getOrderBy() {
		return orderBy;
	}
	
	/**
	 * create ORDER BY 
	 */
	public void setOrderBy() {
		if(sortKey!=null	&&	!sortKey.equals("")){
			orderBy=" ORDER BY  "+sidx+" "+sortOrder+" ";
		}
		
	}
	
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public boolean isPageable() {
		return pageable;
	}

	public void setPageable(boolean pageable) {
		this.pageable = pageable;
	}
	
	public void setPageable() {
		this.setPageable(true);
	}
	
	public String getPageSelectId() {
		return pageSelectId;
	}

	public void setPageSelectId(String pageSelectId) {
		this.pageSelectId = pageSelectId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("JpaPage [rows=");
		builder.append(rows);
		builder.append(", pageSize=");
		builder.append(pageSize);
		builder.append(", pageNumber=");
		builder.append(pageNumber);
		builder.append(", startRow=");
		builder.append(startRow);
		builder.append(", endRow=");
		builder.append(endRow);
		builder.append(", sidx=");
		builder.append(sidx);
		builder.append(", sortOrder=");
		builder.append(sortOrder);
		builder.append(", sortKey=");
		builder.append(sortKey);
		builder.append(", orderBy=");
		builder.append(orderBy);
		builder.append(", pageable=");
		builder.append(pageable);
		builder.append(", pageResultSelectUUID=");
		builder.append(pageSelectId);
		builder.append("]");
		return builder.toString();
	}
	
	
}
