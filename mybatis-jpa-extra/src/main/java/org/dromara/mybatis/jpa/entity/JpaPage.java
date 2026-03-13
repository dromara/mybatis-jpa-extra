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
 

package org.dromara.mybatis.jpa.entity;

import org.dromara.mybatis.jpa.id.IdentifierStrategy;
import org.apache.commons.lang3.StringUtils;
import org.dromara.mybatis.jpa.constants.ConstPage;
import org.dromara.mybatis.jpa.id.IdentifierGeneratorFactory;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Transient;

/**
 * Pagination for database pagination
 * Improvements: input validation, safer calculations, normalized sort/order building,
 * and small fluent helpers.
 */
public class JpaPage {
	
	@JsonIgnore
    @Transient
    protected String    pageSelectId;
	
	/** whether to apply pagination */
    @JsonIgnore
    @Transient
    protected boolean pageable = false;
    
    /** default page size */
    @JsonIgnore
    @Transient
    protected int pageSize = ConstPage.NORMAL_RESULTS;
    /** current page number, 1-based */
    @JsonIgnore
    @Transient
    protected int pageNumber = 1;
    /** asc/desc */
    @JsonIgnore
    @Transient
    protected String sortOrder;
    /** prebuilt sort key (without ORDER BY) */
    @JsonIgnore
    @Transient
    protected String sortKey;
    /** full ORDER BY clause */
    @JsonIgnore
    @Transient
    protected String orderBy;
    
    @JsonIgnore
    @Transient
    protected int rows;
    /** zero-based start row for SQL limit/offset */
    @JsonIgnore
    @Transient
    protected int startRow;
    /** exclusive end row */
    @JsonIgnore
    @Transient
    protected int endRow;
    
    public JpaPage(){}
    
    public JpaPage(int pageNumber ){
        setPageNumber(pageNumber);
        this.pageable = true;
    }
    
    public JpaPage(int pageNumber , int pageSize){
        setPageNumber(pageNumber);
        setPageSize(pageSize);
        this.pageable = true;
    }

    
    public String generateId() {
        return IdentifierGeneratorFactory.generate(IdentifierStrategy.DEFAULT);
    }
    
    @JsonIgnore
    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows < 0) {
            // negative rows don't make sense; keep previous value
            return;
        }
        this.rows = rows;
        // keep pageSize in sync with rows when rows explicitly provided
        this.pageSize = rows;
        calculate();
    }
    
    @JsonIgnore
    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        if (pageNumber <= 0) {
            this.pageNumber = 1;
        } else {
            this.pageNumber = pageNumber;
        }
        calculate();
    }
    
    @JsonIgnore
    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    @JsonIgnore
    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        if (startRow < 0) {
            this.startRow = 0;
        } else {
            this.startRow     = startRow;
        }
        // derive pageNumber from explicit startRow
        calculate(this.startRow);
    }
    
    @JsonIgnore
    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        if (endRow < 0) {
            this.endRow = this.startRow + this.pageSize;
        } else {
            this.endRow = endRow;
        }
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
            endRow     = this.pageSize;
        }
    }
    
    @JsonIgnore
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if(pageSize <= 0) {
            // default to max results when non-positive
            this.pageSize = ConstPage.NORMAL_RESULTS;
        } else if(pageSize > ConstPage.MAX_RESULTS) {
            this.pageSize = ConstPage.MAX_RESULTS;
        } else {
            this.pageSize = pageSize;
        }
        // recalculate dependent fields
        calculate();
    }
    
    @JsonIgnore
    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }
    @JsonIgnore
    public String getOrderBy() {
        return orderBy;
    }
    
    /**
     * build ORDER BY with sortKey, if sortKey is not empty
     */
    public void buildOrderBy() {
        if(StringUtils.isNotBlank(sortKey)){
            orderBy = " ORDER BY " + sortKey + " " + sortOrder + " ";
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
    
    
    /**
     * calculate StartRow
     * @param page page number (1-based)
     * @param pageResults page size
     * @return zero-based start row
     */
    protected Integer calculateStartRow(Integer page,Integer pageSize){
        if (page == null || page <= 1) {
            return 0;
        }
        if (pageSize == null || pageSize <= 0) {
            pageSize = ConstPage.NORMAL_RESULTS;
        }else if(pageSize > ConstPage.MAX_RESULTS) {
        	pageSize = ConstPage.NORMAL_RESULTS;
        }
        return (page - 1) * pageSize;
    }
    
    public void build() {
        this.pageSelectId= generateId();
        this.startRow = calculateStartRow(this.pageNumber ,this.pageSize);
        this.pageable = true;
    }
    
    /**
     * 分页设置
     * @param page 页码
     * @param size 记录数
     */
    public static JpaPage of(int page, int size) {
        return new JpaPage(page,size);
    }
    
    public JpaPage page(int page) {
        setPageNumber(page);
        this.pageable = true;
        return this;
    }
    
    public JpaPage ofPage(int page) {
        return page(page);
    }
    
    public JpaPage size(int size) {
        setPageSize(size);
        this.pageable = true;
        return this;
    }
    
    public JpaPage ofSize(int size) {
        return size(size);
    }

    @Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("JpaPage [pageSelectId=");
		builder.append(pageSelectId);
		builder.append(", pageable=");
		builder.append(pageable);
		builder.append(", pageSize=");
		builder.append(pageSize);
		builder.append(", pageNumber=");
		builder.append(pageNumber);
		builder.append(", sortOrder=");
		builder.append(sortOrder);
		builder.append(", sortKey=");
		builder.append(sortKey);
		builder.append(", orderBy=");
		builder.append(orderBy);
		builder.append(", rows=");
		builder.append(rows);
		builder.append(", startRow=");
		builder.append(startRow);
		builder.append(", endRow=");
		builder.append(endRow);
		builder.append("]");
		return builder.toString();
	}
    
    
}