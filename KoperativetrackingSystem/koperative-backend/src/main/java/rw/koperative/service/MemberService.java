package rw.koperative.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.koperative.dto.MemberRequest;
import rw.koperative.dto.MemberResponse;
import rw.koperative.exception.BadRequestException;
import rw.koperative.exception.ResourceNotFoundException;
import rw.koperative.model.Member;
import rw.koperative.model.enums.FineStatus;
import rw.koperative.model.enums.MemberStatus;
import rw.koperative.repository.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final SavingsRepository savingsRepository;
    private final ShareRepository shareRepository;
    private final FineRepository fineRepository;

    public MemberService(MemberRepository memberRepository, SavingsRepository savingsRepository,
                         ShareRepository shareRepository, FineRepository fineRepository) {
        this.memberRepository = memberRepository;
        this.savingsRepository = savingsRepository;
        this.shareRepository = shareRepository;
        this.fineRepository = fineRepository;
    }

    public List<MemberResponse> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public MemberResponse getMemberById(Long id) {
        Member member = findMemberOrThrow(id);
        return toProfileResponse(member);
    }

    public MemberResponse createMember(MemberRequest request) {
        if (memberRepository.existsByNationalId(request.getNationalId())) {
            throw new BadRequestException("Member with national ID " + request.getNationalId() + " already exists");
        }

        Member member = Member.builder()
                .nationalId(request.getNationalId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .cell(request.getCell())
                .sector(request.getSector())
                .district(request.getDistrict())
                .nextOfKin(request.getNextOfKin())
                .nextOfKinPhone(request.getNextOfKinPhone())
                .status(MemberStatus.ACTIVE)
                .build();

        return toResponse(memberRepository.save(member));
    }

    public MemberResponse updateMember(Long id, MemberRequest request) {
        Member member = findMemberOrThrow(id);
        member.setFirstName(request.getFirstName());
        member.setLastName(request.getLastName());
        member.setPhone(request.getPhone());
        member.setEmail(request.getEmail());
        member.setCell(request.getCell());
        member.setSector(request.getSector());
        member.setDistrict(request.getDistrict());
        member.setNextOfKin(request.getNextOfKin());
        member.setNextOfKinPhone(request.getNextOfKinPhone());
        return toResponse(memberRepository.save(member));
    }

    public MemberResponse updateStatus(Long id, String status) {
        Member member = findMemberOrThrow(id);
        member.setStatus(MemberStatus.valueOf(status.toUpperCase()));
        return toResponse(memberRepository.save(member));
    }

    public List<MemberResponse> searchMembers(String query) {
        return memberRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public Member findMemberOrThrow(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID: " + id));
    }

    private MemberResponse toResponse(Member m) {
        return MemberResponse.builder()
                .id(m.getId())
                .nationalId(m.getNationalId())
                .firstName(m.getFirstName())
                .lastName(m.getLastName())
                .phone(m.getPhone())
                .email(m.getEmail())
                .cell(m.getCell())
                .sector(m.getSector())
                .district(m.getDistrict())
                .nextOfKin(m.getNextOfKin())
                .nextOfKinPhone(m.getNextOfKinPhone())
                .membershipDate(m.getMembershipDate())
                .status(m.getStatus().name())
                .createdAt(m.getCreatedAt())
                .build();
    }

    private MemberResponse toProfileResponse(Member m) {
        MemberResponse response = toResponse(m);
        response.setTotalSavings(savingsRepository.calculateBalanceByMemberId(m.getId()));
        response.setTotalShares(shareRepository.countTotalSharesByMemberId(m.getId()));

        BigDecimal unpaidFines = m.getFines() != null ?
                m.getFines().stream()
                        .filter(f -> f.getStatus() == FineStatus.UNPAID)
                        .map(f -> f.getAmount())
                        .reduce(BigDecimal.ZERO, BigDecimal::add) : BigDecimal.ZERO;
        response.setTotalFinesUnpaid(unpaidFines);

        return response;
    }
}
