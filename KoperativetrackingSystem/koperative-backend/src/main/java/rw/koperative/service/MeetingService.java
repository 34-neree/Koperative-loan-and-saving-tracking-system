package rw.koperative.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.koperative.dto.AttendanceRequest;
import rw.koperative.dto.MeetingRequest;
import rw.koperative.exception.ResourceNotFoundException;
import rw.koperative.model.*;
import rw.koperative.model.enums.FineStatus;
import rw.koperative.repository.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final MeetingAttendanceRepository attendanceRepository;
    private final MemberRepository memberRepository;
    private final FineRepository fineRepository;

    public MeetingService(MeetingRepository meetingRepository, MeetingAttendanceRepository attendanceRepository,
                          MemberRepository memberRepository, FineRepository fineRepository) {
        this.meetingRepository = meetingRepository;
        this.attendanceRepository = attendanceRepository;
        this.memberRepository = memberRepository;
        this.fineRepository = fineRepository;
    }

    public Meeting createMeeting(MeetingRequest request, String createdBy) {
        Meeting meeting = Meeting.builder()
                .title(request.getTitle())
                .meetingDate(request.getMeetingDate())
                .location(request.getLocation())
                .agenda(request.getAgenda())
                .minutes(request.getMinutes())
                .createdBy(createdBy)
                .build();
        return meetingRepository.save(meeting);
    }

    public List<Meeting> getAllMeetings() {
        return meetingRepository.findAllByOrderByMeetingDateDesc();
    }

    public List<MeetingAttendance> submitAttendance(Long meetingId, AttendanceRequest request) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new ResourceNotFoundException("Meeting not found"));

        List<MeetingAttendance> records = new ArrayList<>();
        BigDecimal fineAmount = new BigDecimal("1000"); // Configurable

        for (Map.Entry<Long, Boolean> entry : request.getAttendance().entrySet()) {
            Member member = memberRepository.findById(entry.getKey())
                    .orElseThrow(() -> new ResourceNotFoundException("Member not found: " + entry.getKey()));

            boolean attended = entry.getValue();
            boolean fineIssued = false;

            if (!attended) {
                Fine fine = Fine.builder()
                        .member(member)
                        .reason("Absent from meeting: " + meeting.getTitle())
                        .amount(fineAmount)
                        .status(FineStatus.UNPAID)
                        .referenceId(meetingId)
                        .referenceType("MEETING")
                        .build();
                fineRepository.save(fine);
                fineIssued = true;
            }

            MeetingAttendance attendance = MeetingAttendance.builder()
                    .meeting(meeting)
                    .member(member)
                    .attended(attended)
                    .fineIssued(fineIssued)
                    .build();
            records.add(attendanceRepository.save(attendance));
        }
        return records;
    }

    public List<MeetingAttendance> getAttendance(Long meetingId) {
        return attendanceRepository.findByMeetingId(meetingId);
    }
}
