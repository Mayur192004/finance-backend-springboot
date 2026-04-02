package com.finance.dashboard.service;

import com.finance.dashboard.dto.RecordRequest;
import com.finance.dashboard.dto.RecordResponse;
import com.finance.dashboard.entity.FinancialRecord;
import com.finance.dashboard.entity.RecordType;
import com.finance.dashboard.entity.User;
import com.finance.dashboard.repository.FinancialRecordRepository;
import com.finance.dashboard.repository.UserRepository;
import com.finance.dashboard.specification.FinancialRecordSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FinancialRecordService {

    private final FinancialRecordRepository recordRepository;
    private final UserRepository userRepository;

    public RecordResponse createRecord(RecordRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FinancialRecord record = FinancialRecord.builder()
                .amount(request.getAmount())
                .type(request.getType())
                .category(request.getCategory())
                .date(request.getDate())
                .notes(request.getNotes())
                .createdBy(user)
                .build();

        return RecordResponse.fromEntity(recordRepository.save(record));
    }

    public Page<RecordResponse> getRecords(RecordType type, String category, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Specification<FinancialRecord> spec = Specification.where(FinancialRecordSpecification.isNotDeleted())
                .and(FinancialRecordSpecification.hasType(type))
                .and(FinancialRecordSpecification.hasCategory(category))
                .and(FinancialRecordSpecification.dateBetween(startDate, endDate));

        return recordRepository.findAll(spec, pageable).map(RecordResponse::fromEntity);
    }

    public RecordResponse getRecordById(Long id) {
        FinancialRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found"));
                
        if (record.isDeleted()) {
            throw new RuntimeException("Record not found (deleted)");
        }
        
        return RecordResponse.fromEntity(record);
    }

    public RecordResponse updateRecord(Long id, RecordRequest request) {
        FinancialRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found"));

        if (record.isDeleted()) {
            throw new RuntimeException("Cannot update a deleted record");
        }

        record.setAmount(request.getAmount());
        record.setType(request.getType());
        record.setCategory(request.getCategory());
        record.setDate(request.getDate());
        record.setNotes(request.getNotes());

        return RecordResponse.fromEntity(recordRepository.save(record));
    }

    public void deleteRecord(Long id) {
        FinancialRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found"));
        // Soft delete
        record.setDeleted(true);
        recordRepository.save(record);
    }
}
