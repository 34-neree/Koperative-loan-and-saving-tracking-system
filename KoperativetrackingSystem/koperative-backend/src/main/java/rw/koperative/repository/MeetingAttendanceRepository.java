package rw.koperative.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.koperative.model.MeetingAttendance;

import java.util.List;

@Repository
public interface MeetingAttendanceRepository extends JpaRepository<MeetingAttendance, Long> {
    List<MeetingAttendance> findByMeetingId(Long meetingId);
    List<MeetingAttendance> findByMemberId(Long memberId);
}
