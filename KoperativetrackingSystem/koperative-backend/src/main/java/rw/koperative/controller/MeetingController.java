package rw.koperative.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.koperative.dto.AttendanceRequest;
import rw.koperative.dto.MeetingRequest;
import rw.koperative.model.Meeting;
import rw.koperative.model.MeetingAttendance;
import rw.koperative.service.MeetingService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/meetings")
public class MeetingController {

    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @PostMapping
    public ResponseEntity<Meeting> create(@Valid @RequestBody MeetingRequest request, Principal principal) {
        return ResponseEntity.ok(meetingService.createMeeting(request, principal.getName()));
    }

    @GetMapping
    public ResponseEntity<List<Meeting>> getAll() {
        return ResponseEntity.ok(meetingService.getAllMeetings());
    }

    @PostMapping("/{id}/attendance")
    public ResponseEntity<List<MeetingAttendance>> submitAttendance(
            @PathVariable Long id, @RequestBody AttendanceRequest request) {
        return ResponseEntity.ok(meetingService.submitAttendance(id, request));
    }

    @GetMapping("/{id}/attendance")
    public ResponseEntity<List<MeetingAttendance>> getAttendance(@PathVariable Long id) {
        return ResponseEntity.ok(meetingService.getAttendance(id));
    }
}
