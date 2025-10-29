/*
 * Copyright [2024] [MaxKey of copyright http://www.maxkey.top]
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
 

package org.dromara.mybatis.jpa.interceptor.builder;

import org.dromara.mybatis.jpa.entity.JpaPage;

public class SelectPageSql {
	/**
	 * select 语句标记
	 */
	boolean selectTrack;
	
	/**
     * 是否分页标识
     */
    boolean pageable;
    
    /**
     * 数据分页对象
     */
	JpaPage page;

	public SelectPageSql() {
	}

	public SelectPageSql(boolean selectTrack, JpaPage page) {
		super();
		this.selectTrack = selectTrack;
		this.page = page;
	}

	public boolean isSelectTrack() {
		return selectTrack;
	}

	public void setSelectTrack(boolean selectTrack) {
		this.selectTrack = selectTrack;
	}

	public JpaPage getPage() {
		return page;
	}

	public void setPage(JpaPage page) {
		this.page = page;
	}

	public boolean isPageable() {
		return pageable;
	}

	public void setPageable(boolean pageable) {
		this.pageable = pageable;
	}

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SelectPageSql [selectTrack=");
        builder.append(selectTrack);
        builder.append(", pageable=");
        builder.append(pageable);
        builder.append(", page=");
        builder.append(page);
        builder.append("]");
        return builder.toString();
    }
	
}
