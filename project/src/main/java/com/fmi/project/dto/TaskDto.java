package com.fmi.project.dto;

import com.fmi.project.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private String name;
    private String description;
    private Date due_date;
    private Status status;
    private String creatorUsername;
    private Long event_id;
    private List<String> assignees;
}
