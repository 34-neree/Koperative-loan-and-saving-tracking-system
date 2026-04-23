package rw.koperative.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.koperative.dto.FineRequest;
import rw.koperative.model.Fine;
import rw.koperative.service.FineService;

import java.util.List;

@RestController
@RequestMapping("/api/fines")
public class FineController {

    private final FineService fineService;

    public FineController(FineService fineService) {
        this.fineService = fineService;
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Fine>> getMemberFines(@PathVariable Long memberId) {
        return ResponseEntity.ok(fineService.getMemberFines(memberId));
    }

    @PostMapping
    public ResponseEntity<Fine> create(@Valid @RequestBody FineRequest request) {
        return ResponseEntity.ok(fineService.createFine(request));
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<Fine> pay(@PathVariable Long id) {
        return ResponseEntity.ok(fineService.payFine(id));
    }

    @GetMapping("/unpaid")
    public ResponseEntity<List<Fine>> getUnpaid() {
        return ResponseEntity.ok(fineService.getUnpaidFines());
    }
}
