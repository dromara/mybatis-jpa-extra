package org.apache.mybatis.jpa.test.entity.common;

import org.apache.mybatis.jpa.id.IdStrategy;
import org.apache.mybatis.jpa.persistence.JpaBaseEntity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * 生成唯一主键
 * <pre>
 *     实体类            数据表字段            描述
 *     id               id                  ID 主键
 * </pre>
 *
 * @author YL
 */
@MappedSuperclass
public abstract class BaseEntityId extends JpaBaseEntity implements Serializable {
    private static final long serialVersionUID = -4168199084304201400L;

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = IdStrategy.SNOWFLAKEID)
    private String id;

    public BaseEntityId() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
