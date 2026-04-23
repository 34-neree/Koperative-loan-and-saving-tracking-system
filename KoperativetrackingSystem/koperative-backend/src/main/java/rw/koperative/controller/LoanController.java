package rw.koperative.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.koperative.dto.LoanRequest;
import rw.koperative.model.Loan;
import rw.koperative.service.LoanService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/apply")
    public ResponseEntity<Loan> apply(@Valid @RequestBody LoanRequest request) {
        return ResponseEntity.ok(loanService.applyForLoan(request));
    }

    @PutMapping("/{id}/review")
    public ResponseEntity<Loan> review(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.reviewLoan(id));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Loan> approve(@PathVariable Long id, @RequestBody(required = false) Map<String, Object> body) {
        BigDecimal amount = body != null && body.get("amountApproved") != null
                ? new BigDecimal(body.get("amountApproved").toString()) : null;
        return ResponseEntity.ok(loanService.approveLoan(id, amount));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Loan> reject(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.rejectLoan(id));
    }

    @PutMapping("/{id}/disburse")
    public ResponseEntity<Loan> disburse(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.disburseLoan(id));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Loan>> getMemberLoans(@PathVariable Long memberId) {
        return ResponseEntity.ok(loanService.getMemberLoans(memberId));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Loan>> getPending() {
        return ResponseEntity.ok(loanService.getPendingLoans());
    }
}
