package rw.koperative.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.koperative.config.JwtTokenProvider;
import rw.koperative.dto.LoginRequest;
import rw.koperative.dto.LoginResponse;
import rw.koperative.dto.RegisterRequest;
import rw.koperative.exception.BadRequestException;
import rw.koperative.model.Member;
import rw.koperative.model.User;
import rw.koperative.model.enums.MemberStatus;
import rw.koperative.model.enums.UserRole;
import rw.koperative.repository.MemberRepository;
import rw.koperative.repository.UserRepository;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository, MemberRepository memberRepository,
                       PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadRequestException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Invalid username or password");
        }

        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getRole().name());

        String fullName = "";
        Long memberId = null;
        if (user.getMember() != null) {
            fullName = user.getMember().getFirstName() + " " + user.getMember().getLastName();
            memberId = user.getMember().getId();
        }

        return LoginResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().name())
                .memberId(memberId)
                .fullName(fullName)
                .build();
    }

    public LoginResponse register(RegisterRequest request) {
        // Check if username already exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new BadRequestException("Username '" + request.getUsername() + "' is already taken");
        }

        // Check if national ID already exists
        if (memberRepository.findByNationalId(request.getNationalId()).isPresent()) {
            throw new BadRequestException("A member with this National ID already exists");
        }

        // Create the member profile
        Member member = Member.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .nationalId(request.getNationalId())
                .phone(request.getPhone())
                .email(request.getEmail())
                .district(request.getDistrict())
                .sector(request.getSector())
                .cell(request.getCell())
                .status(MemberStatus.ACTIVE)
                .build();
        member = memberRepository.save(member);

        // Create the user account
        User user = User.builder()
                .member(member)
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.MEMBER)
                .build();
        userRepository.save(user);

        // Auto-login after registration
        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getRole().name());
        String fullName = member.getFirstName() + " " + member.getLastName();

        return LoginResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().name())
                .memberId(member.getId())
                .fullName(fullName)
                .build();
    }

    public User getCurrentUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("User not found"));
    }
}
