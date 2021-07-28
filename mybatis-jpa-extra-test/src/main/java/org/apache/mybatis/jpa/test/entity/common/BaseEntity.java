package org.apache.mybatis.jpa.test.entity.common;

import org.apache.mybatis.jpa.id.IdStrategy;
import org.apache.mybatis.jpa.persistence.JpaBaseEntity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * 生成唯一主键
 * <pre>
 *     实体类            数据表字段            描述
 *     id               id                  ID 主键
 *     deleted          deleted             是否已删除：0-正常，1-删除
 *     createdBy        createdBy           创建人
 *     createdAt        createdAt           创建时间
 *     modifiedBy       modifiedBy          修改人
 *     modifiedAt       modifiedAt          修改时间
 * </pre>
 *
 * @author YL
 */
@MappedSuperclass
public abstract class BaseEntity extends JpaBaseEntity implements Serializable {
    private static final long serialVersionUID = 4467847034990626380L;

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = IdStrategy.SNOWFLAKEID)
    private String id;

    @Column(name = "deleted", columnDefinition = "NUMBER default 0")
    private boolean deleted = false;

    @Column(name = "createdBy", updatable = false)
    private String createdBy;

    @Column(name = "createdAt", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "modifiedBy")
    private String modifiedBy;

    @Column(name = "modifiedAt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;

    public BaseEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}
