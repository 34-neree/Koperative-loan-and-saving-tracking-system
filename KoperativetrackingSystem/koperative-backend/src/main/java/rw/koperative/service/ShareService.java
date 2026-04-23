package rw.koperative.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.koperative.dto.ShareRequest;
import rw.koperative.exception.ResourceNotFoundException;
import rw.koperative.model.Member;
import rw.koperative.model.Share;
import rw.koperative.model.ShareConfig;
import rw.koperative.model.enums.ShareStatus;
import rw.koperative.repository.ShareConfigRepository;
import rw.koperative.repository.ShareRepository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ShareService {

    private final ShareRepository shareRepository;
    private final ShareConfigRepository shareConfigRepository;
    private final MemberService memberService;

    public ShareService(ShareRepository shareRepository, ShareConfigRepository shareConfigRepository, MemberService memberService) {
        this.shareRepository = shareRepository;
        this.shareConfigRepository = shareConfigRepository;
        this.memberService = memberService;
    }

    public ShareConfig getConfig() {
        return shareConfigRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Share configuration not found"));
    }

    public ShareConfig updateConfig(BigDecimal sharePrice, Integer minimumShares) {
        ShareConfig config = getConfig();
        if (sharePrice != null) config.setSharePrice(sharePrice);
        if (minimumShares != null) config.setMinimumSharesForLoan(minimumShares);
        return shareConfigRepository.save(config);
    }

    public Share purchaseShares(ShareRequest request) {
        Member member = memberService.findMemberOrThrow(request.getMemberId());
        ShareConfig config = getConfig();

        BigDecimal totalValue = config.getSharePrice().multiply(BigDecimal.valueOf(request.getNumberOfShares()));

        Share share = Share.builder()
                .member(member)
                .numberOfShares(request.getNumberOfShares())
                .sharePriceAtPurchase(config.getSharePrice())
                .totalValue(totalValue)
                .status(ShareStatus.PAID)
                .build();

        return shareRepository.save(share);
    }

    public List<Share> getMemberShares(Long memberId) {
        memberService.findMemberOrThrow(memberId);
        return shareRepository.findByMemberIdOrderByPurchaseDateDesc(memberId);
    }

    public Map<String, Object> checkEligibility(Long memberId) {
        memberService.findMemberOrThrow(memberId);
        ShareConfig config = getConfig();
        Integer totalShares = shareRepository.countTotalSharesByMemberId(memberId);

        Map<String, Object> result = new HashMap<>();
        result.put("memberId", memberId);
        result.put("totalShares", totalShares);
        result.put("minimumRequired", config.getMinimumSharesForLoan());
        result.put("eligible", totalShares >= config.getMinimumSharesForLoan());
        result.put("reason", totalShares >= config.getMinimumSharesForLoan()
                ? "Member meets share capital requirement"
                : "Member needs " + (config.getMinimumSharesForLoan() - totalShares) + " more shares");
        return result;
    }
}
