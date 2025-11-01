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

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JpaPageResults 前端控件的组装类
 * 需要提供
 *         1、当前页码         page
 *         2、当前页记录数     total
 *         3、总页数        totalPage
 *         4、总记录数         records
 *         5、记录的列表     List<T> rows

 * @author Crystal.Sea
 *
 * @param <T>
 */
public  class JpaPageResults <T>{
    private static final  Logger logger = LoggerFactory.getLogger(JpaPageResults.class);
    
    /**
     * 当前页
     */
    private int page            = 0;
    /**
     * 当前页记录数
     */
    private int total            = 0;
    
    /**
     * 总页数
     */
    private int totalPage        = 0;
    
    /**
     * 总记录数
     */
    private Long records        = 0L;
    
    /**
     * 记录行数据
     */
    private List<T> rows;         
    
    /**
     * 
     */
    public JpaPageResults() {
        logger.debug("JpaPageResults.");
    }
    /**
     * @param currentPage
     * @param pageResults
     * @param recordsCount
     */
    public JpaPageResults(int currentPage,int pageResults,Long recordsCount) {
        pageCount(currentPage,pageResults, recordsCount);
        logger.debug("JpaPageResults : {} , records : {} , totalPage : {}",page,records,totalPage);
    }
    /**
     * 构造函数
     * @param currentPage
     * @param pageResults
     * @param recordsCount
     * @param rows
     */
    public JpaPageResults(int currentPage,int pageResults,Long recordsCount,List<T> rows) {
        pageCount(currentPage,pageResults, recordsCount);
        this.rows = rows;
    }
    
    /**
     * 构造函数
     * @param currentPage
     * @param pageResults
     * @param recordsCount
     * @param rows
     */
    public JpaPageResults(int currentPage,int pageResults,Integer recordsCount,List<T> rows) {
        pageCount(currentPage,pageResults, recordsCount);
        this.rows = rows;
    }
    
    /**
     * 构造函数
     * @param currentPage
     * @param pageResults
     * @param totalPage
     * @param recordsCount
     * @param rows
     */
    public JpaPageResults(int currentPage,int pageResults,int totalPage,Long recordsCount,List<T> rows) {
        pageCount(currentPage,pageResults, recordsCount);
        this.rows = rows;
        this.total = totalPage;
    }
    
    /**
     * 构造函数
     * @param currentPage
     * @param pageResults
     * @param totalPage
     * @param recordsCount
     * @param rows
     */
    public JpaPageResults(int currentPage,int pageResults,int totalPage,Integer recordsCount,List<T> rows) {
        pageCount(currentPage,pageResults, recordsCount);
        this.rows = rows;
        this.total = totalPage;
    }
    
    /**
     * 计算分页信息
     * @param currentPage
     * @param pageResults
     * @param recordsCount
     */
    public void pageCount(int currentPage,int pageResults,Long recordsCount){
        this.page = currentPage;
        //通过总记录数和每页显示记录数计算出当前页记录数
        this.totalPage=(int) ((recordsCount%pageResults>0)?recordsCount/pageResults+1:recordsCount/pageResults);
        this.records=recordsCount;
    }
    
    /**
     * 计算分页信息
     * @param currentPage
     * @param pageResults
     * @param recordsCount
     */
    public void pageCount(int currentPage,int pageResults,Integer recordsCount){
        this.page = currentPage;
        //通过总记录数和每页显示记录数计算出当前页记录数
        this.totalPage =((recordsCount%pageResults>0)?recordsCount/pageResults+1:recordsCount/pageResults);
        this.records = Long.valueOf(recordsCount);
    }
    
    /**
     * 当前页记录数
     * @param resultslist
     * @return
     */
    public static Integer parseRecords( List<?> resultslist){
        return CollectionUtils.isEmpty(resultslist) ? 0 : resultslist.size();
    }
    
    /**
     * parse Object Count to Integer
     * @param totalCount
     * @return
     */
    public static Integer parseCount(Object totalCount){
        Integer retTotalCount=0;
        if(totalCount == null) {
            return retTotalCount;
        }else{
            retTotalCount = Integer.parseInt(totalCount.toString());
        }
        return retTotalCount;
    }
    
    /**
     * @return the page
     */
    public int getPage() {
        return page;
    }
    
    /**
     * @param page the page to set
     */
    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPage() {
        return totalPage;
    }
    
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    
    public int getTotal() {
        return total;
    }
    
    public void setTotal(int total) {
        this.total = total;
    }
    /**
     * @return the records
     */
    public Long getRecords() {
        return records;
    }
    /**
     * @param records the records to set
     */
    public void setRecords(Long records) {
        this.records = records;
    }
    
    /**
     * @return the rows
     */
    public List<T> getRows() {
        return rows;
    }
    
    /**
     * @param rows the rows to set
     */
    public void setRows(List<T> rows) {
        this.rows = rows;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("JpaPageResults [page=");
        builder.append(page);
        builder.append(", total=");
        builder.append(total);
        builder.append(", totalPage=");
        builder.append(totalPage);
        builder.append(", records=");
        builder.append(records);
        builder.append(", rows=");
        builder.append(rows);
        builder.append("]");
        return builder.toString();
    }

}
