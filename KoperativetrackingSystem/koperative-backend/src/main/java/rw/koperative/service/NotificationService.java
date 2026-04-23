package rw.koperative.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.koperative.dto.NotificationRequest;
import rw.koperative.model.Member;
import rw.koperative.model.Notification;
import rw.koperative.model.enums.NotificationChannel;
import rw.koperative.model.enums.NotificationStatus;
import rw.koperative.repository.NotificationRepository;

import java.util.List;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberService memberService;

    public NotificationService(NotificationRepository notificationRepository, MemberService memberService) {
        this.notificationRepository = notificationRepository;
        this.memberService = memberService;
    }

    public Notification sendNotification(NotificationRequest request) {
        Member member = memberService.findMemberOrThrow(request.getMemberId());
        NotificationChannel channel = request.getChannel() != null
                ? NotificationChannel.valueOf(request.getChannel().toUpperCase())
                : NotificationChannel.SYSTEM;

        Notification notification = Notification.builder()
                .member(member)
                .phone(member.getPhone())
                .message(request.getMessage())
                .channel(channel)
                .status(NotificationStatus.SENT) // Stub — mark as sent
                .build();

        return notificationRepository.save(notification);
    }

    public List<Notification> getMemberNotifications(Long memberId) {
        return notificationRepository.findByMemberIdOrderBySentAtDesc(memberId);
    }
}
