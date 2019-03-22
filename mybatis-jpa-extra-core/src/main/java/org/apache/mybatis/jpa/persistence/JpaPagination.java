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
	protected int pageResults = 20;
	/**
	 * 
	 */
	@JsonIgnore
	protected int page=1;
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
	protected String sord;
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
		this.pageResults = rows;
		calculate();
	}
	@JsonIgnore
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
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
	public String getSord() {
		return sord;
	}

	public void setSord(String sord) {
		this.sord = sord;
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
		if (this.page >= 1 && this.pageResults > 0){
				startRow = (this.page - 1) * this.pageResults;
				endRow = startRow + this.pageResults;
			}
	}
	@JsonIgnore
	public int getPageResults() {
		return pageResults;
	}

	public void setPageResults(int pageResults) {
		this.pageResults = pageResults;
	}
	@JsonIgnore
	public String getSortKey() {
		return sortKey;
	}

	/**
	 * create sortKey from sidx & sord,eg  order by  name asc 
	 */
	public void setSortKey() {
		if(sidx!=null	&&	sord!=null	&&	!sidx.equals("")	&&	!sord.equals("")){
			sortKey=" "+sidx+" "+sord+" ";
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
			orderBy=" ORDER BY  "+sidx+" "+sord+" ";
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
		return "Pagination [rows=" + rows + ", pageResults=" + pageResults
				+ ", page=" + page + ", startRow=" + startRow + ", endRow="
				+ endRow + ", sidx=" + sidx + ", sord=" + sord + ", sortKey="
				+ sortKey + ", orderBy=" + orderBy + ", pageable=" + pageable
				+ "]";
	}
	
	
}
