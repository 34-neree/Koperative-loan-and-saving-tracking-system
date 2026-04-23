package rw.koperative.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.koperative.dto.NotificationRequest;
import rw.koperative.model.Notification;
import rw.koperative.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/send")
    public ResponseEntity<Notification> send(@Valid @RequestBody NotificationRequest request) {
        return ResponseEntity.ok(notificationService.sendNotification(request));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Notification>> getMemberNotifications(@PathVariable Long memberId) {
        return ResponseEntity.ok(notificationService.getMemberNotifications(memberId));
    }
}
