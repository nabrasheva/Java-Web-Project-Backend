package com.fmi.project.dto;

import com.fmi.project.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private String name;
    private String description;
    private LocalDate due_date;
    private Status status;
}
