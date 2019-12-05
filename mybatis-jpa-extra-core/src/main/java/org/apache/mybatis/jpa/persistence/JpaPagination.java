/**
 * 
 */
package org.apache.mybatis.jpa.persistence;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Pagination for database pagination
 * @author Crystal.Sea
 *
 */
public class JpaPagination {
	
	@JsonIgnore
	protected int rows;
	/**
	 * 
	 */
	@JsonIgnore
	protected int pageSize = 20;
	/**
	 * 
	 */
	@JsonIgnore
	protected int pageNumber=1;
	/**
	 * 
	 */
	@JsonIgnore
	protected int startRow;
	/**
	 * 
	 */
	@JsonIgnore
	protected int endRow;
	
	@JsonIgnore
	protected String sidx;
	/**
	 * 
	 */
	@JsonIgnore
	protected String sortOrder;
	/**
	 * 
	 */
	@JsonIgnore
	protected String sortKey;
	/**
	 * 
	 */
	@JsonIgnore
	protected String orderBy;
	/**
	 * @serialField pageable
	 */
	@JsonIgnore
	protected boolean pageable=false;
	
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Pagination [rows=" + rows + ", pageResults=" + pageSize
				+ ", page=" + pageNumber + ", startRow=" + startRow + ", endRow="
				+ endRow + ", sidx=" + sidx + ", sord=" + sortOrder + ", sortKey="
				+ sortKey + ", orderBy=" + orderBy + ", pageable=" + pageable
				+ "]";
	}
	
	
}
