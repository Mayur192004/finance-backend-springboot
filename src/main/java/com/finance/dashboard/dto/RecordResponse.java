package com.finance.dashboard.dto;

import com.finance.dashboard.entity.FinancialRecord;
import com.finance.dashboard.entity.RecordType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class RecordResponse {
    private Long id;
    private BigDecimal amount;
    private RecordType type;
    private String category;
    private LocalDate date;
    private String notes;
    private Long createdById;
    private String createdByUsername;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static RecordResponse fromEntity(FinancialRecord record) {
        RecordResponse response = new RecordResponse();
        response.setId(record.getId());
        response.setAmount(record.getAmount());
        response.setType(record.getType());
        response.setCategory(record.getCategory());
        response.setDate(record.getDate());
        response.setNotes(record.getNotes());
        response.setCreatedById(record.getCreatedBy().getId());
        response.setCreatedByUsername(record.getCreatedBy().getUsername());
        response.setCreatedAt(record.getCreatedAt());
        response.setUpdatedAt(record.getUpdatedAt());
        return response;
    }
}
