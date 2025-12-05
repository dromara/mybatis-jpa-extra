package org.dromara.mybatis.jpa.test.entity;

import jakarta.persistence.*;
import org.dromara.mybatis.jpa.entity.JpaEntity;

import java.io.Serializable;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/9/23 15:59
 */

@Entity
@Table(name = "test_user")
public class TestUser extends JpaEntity implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3616223152327185739L;

	@Id
    @Column
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    /*
     * 标识来自哪个数据源
     */
    @Column(name = "data_source")
    private String dataSource;

    // 构造函数
    public TestUser() {}

    public TestUser(String name, String email, String dataSource) {
        this.name = name;
        this.email = email;
        this.dataSource = dataSource;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDataSource() { return dataSource; }
    public void setDataSource(String dataSource) { this.dataSource = dataSource; }

    @Override
    public String toString() {
        return "TestUser{id=" + id + ", name='" + name + "', email='" + email + "', dataSource='" + dataSource + "'}";
    }
}
