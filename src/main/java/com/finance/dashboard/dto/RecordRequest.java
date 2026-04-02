package com.finance.dashboard.dto;

import com.finance.dashboard.entity.RecordType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RecordRequest {
    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    @NotNull
    private RecordType type;

    @NotBlank
    private String category;

    @NotNull
    private LocalDate date;

    private String notes;
}
