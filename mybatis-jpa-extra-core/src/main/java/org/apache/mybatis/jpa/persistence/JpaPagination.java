/**
 * 
 */
package org.apache.mybatis.jpa.persistence;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Pagination for database pagination
 * @author Crystal.Sea
 *
 */
public class JpaPagination {
	
	
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
	protected int pageNumber=1;
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
	protected boolean pageable=false;
	
	@JsonIgnore
	@Transient
	protected String    pageResultSelectUUID;
	
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
		this.startRow = startRow;
	}
	@JsonIgnore
	public int getEndRow() {
		return endRow;
	}

	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	public void calculate() {
		if (this.pageNumber >= 1 && this.pageSize > 0){
				startRow = (this.pageNumber - 1) * this.pageSize;
				endRow = startRow + this.pageSize;
			}
	}
	@JsonIgnore
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
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
	
	public void setPageResultSelectUUID(String pageResultSelectUUID) {
		this.pageResultSelectUUID = pageResultSelectUUID;
	}

	public String getPageResultSelectUUID() {
		return pageResultSelectUUID;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("JpaPagination [rows=");
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
		builder.append(pageResultSelectUUID);
		builder.append("]");
		return builder.toString();
	}
	
	
}
