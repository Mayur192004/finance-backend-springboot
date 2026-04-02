package com.finance.dashboard.specification;

import com.finance.dashboard.entity.FinancialRecord;
import com.finance.dashboard.entity.RecordType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class FinancialRecordSpecification {

    public static Specification<FinancialRecord> hasType(RecordType type) {
        return (root, query, cb) -> type == null ? null : cb.equal(root.get("type"), type);
    }

    public static Specification<FinancialRecord> hasCategory(String category) {
        return (root, query, cb) -> category == null ? null : cb.equal(root.get("category"), category);
    }

    public static Specification<FinancialRecord> dateBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) -> {
            if (startDate != null && endDate != null) {
                return cb.between(root.get("date"), startDate, endDate);
            } else if (startDate != null) {
                return cb.greaterThanOrEqualTo(root.get("date"), startDate);
            } else if (endDate != null) {
                return cb.lessThanOrEqualTo(root.get("date"), endDate);
            }
            return null;
        };
    }

    public static Specification<FinancialRecord> isNotDeleted() {
        return (root, query, cb) -> cb.equal(root.get("deleted"), false);
    }
}
