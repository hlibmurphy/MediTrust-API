package com.github.edocapi.dto;

import com.github.edocapi.model.Recall;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RecallDto {
    private Long id;
    private String number;
    private Recall.Status status;
}
