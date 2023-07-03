package com.fmi.project.dto;

import com.fmi.project.enums.Status;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
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
public class UpdateTaskDto {
    @Size(max = 128)
    private String description;
    @Future
    private Date dueDate;
    //@Size(max = 16)
    private Status status;
    private List<String> assignees;

}
