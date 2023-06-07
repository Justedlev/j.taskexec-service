package com.justedlev.taskexec.repository.entity;

import com.justedlev.taskexec.common.Auditable;
import com.justedlevhub.api.type.TaskMode;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "task")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Task extends Auditable implements Serializable {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "cron")
    private String cron;
    @Column(name = "task_name")
    private String taskName;
    @Builder.Default
    @Column(name = "mode")
    @Enumerated(EnumType.STRING)
    private TaskMode mode = TaskMode.NONE;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Task task = (Task) o;
        return id != null && Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
