package rw.koperative.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.koperative.dto.ShareRequest;
import rw.koperative.model.Share;
import rw.koperative.model.ShareConfig;
import rw.koperative.service.ShareService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shares")
public class ShareController {

    private final ShareService shareService;

    public ShareController(ShareService shareService) {
        this.shareService = shareService;
    }

    @GetMapping("/config")
    public ResponseEntity<ShareConfig> getConfig() {
        return ResponseEntity.ok(shareService.getConfig());
    }

    @PutMapping("/config")
    public ResponseEntity<ShareConfig> updateConfig(@RequestBody Map<String, Object> body) {
        BigDecimal price = body.get("sharePrice") != null ? new BigDecimal(body.get("sharePrice").toString()) : null;
        Integer min = body.get("minimumSharesForLoan") != null ? Integer.parseInt(body.get("minimumSharesForLoan").toString()) : null;
        return ResponseEntity.ok(shareService.updateConfig(price, min));
    }

    @PostMapping
    public ResponseEntity<Share> purchase(@Valid @RequestBody ShareRequest request) {
        return ResponseEntity.ok(shareService.purchaseShares(request));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Share>> getMemberShares(@PathVariable Long memberId) {
        return ResponseEntity.ok(shareService.getMemberShares(memberId));
    }

    @GetMapping("/eligibility/{memberId}")
    public ResponseEntity<Map<String, Object>> checkEligibility(@PathVariable Long memberId) {
        return ResponseEntity.ok(shareService.checkEligibility(memberId));
    }
}
