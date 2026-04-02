package com.finance.dashboard.service;

import com.finance.dashboard.dto.DashboardSummary;
import com.finance.dashboard.dto.RecordResponse;
import com.finance.dashboard.entity.FinancialRecord;
import com.finance.dashboard.entity.RecordType;
import com.finance.dashboard.repository.FinancialRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final FinancialRecordRepository recordRepository;

    public DashboardSummary getSummary() {
        List<FinancialRecord> allRecords = recordRepository.findAll();
        List<FinancialRecord> activeRecords = allRecords.stream()
                .filter(r -> !r.isDeleted())
                .collect(Collectors.toList());

        BigDecimal totalIncome = activeRecords.stream()
                .filter(r -> r.getType() == RecordType.INCOME)
                .map(FinancialRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = activeRecords.stream()
                .filter(r -> r.getType() == RecordType.EXPENSE)
                .map(FinancialRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal netBalance = totalIncome.subtract(totalExpenses);

        Map<String, BigDecimal> categoryWiseTotals = activeRecords.stream()
                .collect(Collectors.groupingBy(
                        FinancialRecord::getCategory,
                        Collectors.mapping(FinancialRecord::getAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));

        List<RecordResponse> recentActivity = recordRepository.findAll(
                PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt"))
        ).stream().filter(r -> !r.isDeleted()).map(RecordResponse::fromEntity).collect(Collectors.toList());

        return DashboardSummary.builder()
                .totalIncome(totalIncome)
                .totalExpenses(totalExpenses)
                .netBalance(netBalance)
                .categoryWiseTotals(categoryWiseTotals)
                .recentActivity(recentActivity)
                .build();
    }
}
