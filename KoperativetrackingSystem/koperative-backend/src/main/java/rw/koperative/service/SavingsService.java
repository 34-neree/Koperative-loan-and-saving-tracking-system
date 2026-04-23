package rw.koperative.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.koperative.dto.SavingsRequest;
import rw.koperative.exception.BadRequestException;
import rw.koperative.model.Member;
import rw.koperative.model.Savings;
import rw.koperative.model.enums.TransactionType;
import rw.koperative.repository.SavingsRepository;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Service
@Transactional
public class SavingsService {

    private final SavingsRepository savingsRepository;
    private final MemberService memberService;

    public SavingsService(SavingsRepository savingsRepository, MemberService memberService) {
        this.savingsRepository = savingsRepository;
        this.memberService = memberService;
    }

    public Savings recordSavings(SavingsRequest request, String recordedBy) {
        Member member = memberService.findMemberOrThrow(request.getMemberId());
        TransactionType type = TransactionType.valueOf(request.getTransactionType().toUpperCase());

        if (type == TransactionType.WITHDRAWAL) {
            BigDecimal balance = savingsRepository.calculateBalanceByMemberId(member.getId());
            if (balance.compareTo(request.getAmount()) < 0) {
                throw new BadRequestException("Insufficient savings balance. Current: " + balance);
            }
        }

        Savings savings = Savings.builder()
                .member(member)
                .amount(request.getAmount())
                .transactionType(type)
                .recordedBy(recordedBy)
                .notes(request.getNotes())
                .build();

        return savingsRepository.save(savings);
    }

    public List<Savings> getMemberSavings(Long memberId) {
        memberService.findMemberOrThrow(memberId);
        return savingsRepository.findByMemberIdOrderByTransactionDateDesc(memberId);
    }

    public BigDecimal getMemberBalance(Long memberId) {
        memberService.findMemberOrThrow(memberId);
        return savingsRepository.calculateBalanceByMemberId(memberId);
    }
}
