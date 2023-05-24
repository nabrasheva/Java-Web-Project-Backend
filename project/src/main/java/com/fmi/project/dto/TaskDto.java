package com.fmi.project.dto;

import com.fmi.project.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private String name;
    private String description;
    private LocalDate due_date;
    private Status status;
    private String creator_username;
    private Long event_id;
    private List<String> assignees;
}
