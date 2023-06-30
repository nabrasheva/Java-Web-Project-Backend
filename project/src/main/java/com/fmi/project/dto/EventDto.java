package com.fmi.project.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {

  @Size(min = 1, max = 32)
  private String name;

  @Future
  private LocalDate date;

  @Size(min = 5, max = 50)
  private String location;

  @Size(max = 128)
  private String description;
}
