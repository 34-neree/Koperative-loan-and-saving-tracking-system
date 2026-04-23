package rw.koperative.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.koperative.dto.FineRequest;
import rw.koperative.exception.ResourceNotFoundException;
import rw.koperative.model.Fine;
import rw.koperative.model.Member;
import rw.koperative.model.enums.FineStatus;
import rw.koperative.repository.FineRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class FineService {

    private final FineRepository fineRepository;
    private final MemberService memberService;

    public FineService(FineRepository fineRepository, MemberService memberService) {
        this.fineRepository = fineRepository;
        this.memberService = memberService;
    }

    public Fine createFine(FineRequest request) {
        Member member = memberService.findMemberOrThrow(request.getMemberId());
        Fine fine = Fine.builder()
                .member(member)
                .reason(request.getReason())
                .amount(request.getAmount())
                .status(FineStatus.UNPAID)
                .build();
        return fineRepository.save(fine);
    }

    public List<Fine> getMemberFines(Long memberId) {
        return fineRepository.findByMemberIdOrderByIssuedDateDesc(memberId);
    }

    public Fine payFine(Long fineId) {
        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new ResourceNotFoundException("Fine not found"));
        fine.setStatus(FineStatus.PAID);
        fine.setPaidDate(LocalDateTime.now());
        return fineRepository.save(fine);
    }

    public List<Fine> getUnpaidFines() {
        return fineRepository.findByStatus(FineStatus.UNPAID);
    }
}
