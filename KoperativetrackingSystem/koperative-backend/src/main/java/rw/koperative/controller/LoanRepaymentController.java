package rw.koperative.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.koperative.dto.RepaymentRequest;
import rw.koperative.model.LoanRepayment;
import rw.koperative.service.LoanRepaymentService;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanRepaymentController {

    private final LoanRepaymentService repaymentService;

    public LoanRepaymentController(LoanRepaymentService repaymentService) {
        this.repaymentService = repaymentService;
    }

    @GetMapping("/{loanId}/schedule")
    public ResponseEntity<List<LoanRepayment>> getSchedule(@PathVariable Long loanId) {
        return ResponseEntity.ok(repaymentService.getSchedule(loanId));
    }

    @PostMapping("/{loanId}/repay")
    public ResponseEntity<LoanRepayment> repay(@PathVariable Long loanId, @Valid @RequestBody RepaymentRequest request) {
        return ResponseEntity.ok(repaymentService.recordPayment(request));
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<LoanRepayment>> getOverdue() {
        return ResponseEntity.ok(repaymentService.getOverdueRepayments());
    }
}
