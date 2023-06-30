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
public class TaskDto {
  @Size(min = 1, max = 32)
  private String name;
  @Size(max = 128)
  private String description;
  @Future
  private Date dueDate;
  @Size(max = 16)
  private Status status;
  @Size(max = 64)
  private String creatorEmail;
  private String eventName;
  private List<String> assignees;
}
