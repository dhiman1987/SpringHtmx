package com.dhiman.spring.htmx.spring.htmx;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class TaskEntity {
    @Id
    private String id;
    private String status = "NOT_STARTED";
    private String name;
    private double score;
}
