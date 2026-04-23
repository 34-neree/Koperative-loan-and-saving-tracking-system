package rw.koperative.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.koperative.dto.SavingsRequest;
import rw.koperative.model.Savings;
import rw.koperative.service.SavingsService;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/savings")
public class SavingsController {

    private final SavingsService savingsService;

    public SavingsController(SavingsService savingsService) {
        this.savingsService = savingsService;
    }

    @PostMapping
    public ResponseEntity<Savings> record(@Valid @RequestBody SavingsRequest request, Principal principal) {
        return ResponseEntity.ok(savingsService.recordSavings(request, principal.getName()));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Savings>> getMemberSavings(@PathVariable Long memberId) {
        return ResponseEntity.ok(savingsService.getMemberSavings(memberId));
    }

    @GetMapping("/member/{memberId}/balance")
    public ResponseEntity<Map<String, Object>> getBalance(@PathVariable Long memberId) {
        BigDecimal balance = savingsService.getMemberBalance(memberId);
        return ResponseEntity.ok(Map.of("memberId", memberId, "balance", balance));
    }
}
