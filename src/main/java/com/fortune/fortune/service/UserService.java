package com.fortune.fortune.service;

import com.fortune.fortune.dto.LoginRequestDto;
import com.fortune.fortune.dto.LoginResponseDto;
import com.fortune.fortune.dto.SignupRequestDto;
import com.fortune.fortune.model.User;
import com.fortune.fortune.repository.UserRepository;
import com.fortune.fortune.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.Optional;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserDetailsServiceImpl userDetailsService ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    public void registerUser(SignupRequestDto signupRequestDto ) {
        // 회원 ID 중복 확인
        String username = signupRequestDto.getUsername();
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자 ID 가 존재합니다.");
        }

        String nickname = signupRequestDto.getNickname();
        Optional<User> found2 = userRepository.findByNickname(nickname);
        if (found2.isPresent()) {
            throw new IllegalArgumentException("중복된 닉네임이 존재합니다.");
        }

        // 패스워드 암호화
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        String dateOfBirth = signupRequestDto.getDateOfBirth();  // 생년월일 받아서 연,월,일 값 추출.
        int[] date = Arrays.stream(dateOfBirth.split("\\."))
                .mapToInt(Integer::parseInt)
                .toArray();
        String zodiacSign = getZodiacSign(date[0]);
        String starPosition = getstarPosition(date[1],date[2]);

        User user = new User(username, password, nickname, dateOfBirth ,zodiacSign, starPosition );
        userRepository.save(user);

    }

    public Authentication getAuthentication(String username) {  // 회원인증
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public LoginResponseDto loginUser(LoginRequestDto requestDto) {  // 로그인
        User user = userRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("아이디를 찾을 수 없습니다. "));
        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        LoginResponseDto loginResponseDto = new LoginResponseDto(user.getZodiacsign(), user.getStarposition(), user.getNickname(), user.isCheckdiary());

        SecurityContextHolder.getContext().setAuthentication(getAuthentication(user.getUsername())); // SecurityContextHolder에 로그인 정보 저장

        return loginResponseDto;

    }


    private String getstarPosition(int Month, int day) {  // 별자리 알고리즘
        String starPosition = "";
        int starday = Month*100 +day;
        if (starday >= 120  && starday <= 218) {
            starPosition = "AQUARIUS";

        } else if (starday >= 219  && starday <= 320 ) {
            starPosition = "PISCES";
            
        } else if (starday >= 321  && starday <= 419) {
            starPosition = "ARIES";

        } else if (starday >= 420  && starday <= 520 ) {
            starPosition = "TAURUS";

        } else if (starday >= 521  && starday <= 621 ) {
            starPosition = "GEMINI";

        } else if (starday >= 622  && starday <= 722 ) {
            starPosition = "CANOER";

        } else if (starday >= 723  && starday <= 822 ) {
            starPosition = "LEO";

        } else if (starday >= 823  && starday <= 923) {
            starPosition = "VIRGO";

        } else if (starday >= 924 && starday <= 1022 ) {
            starPosition = "LIBRA";

        } else if (starday >= 1023 && starday <= 1122 ) {
            starPosition = "SCORPIUS";

        } else if (starday >= 1123 && starday <= 1224 ) {
            starPosition = "SAGITTARIUS";

        } else{
            starPosition = "CAPRICORNUS";
        }
        return starPosition;
    }

    private String getZodiacSign(int year) {  // 띠 알고리즘
        String zodiacSign = "";

        switch(year%12) {
            case 0:
                zodiacSign = "MONKEY";
                break;
            case 1:
                zodiacSign = "CHICKEN";
                break;
            case 2:
                zodiacSign = "DOG";
                break;
            case 3:
                zodiacSign = "PIG";
                break;
            case 4:
                zodiacSign = "RAT";
                break;
            case 5:
                zodiacSign = "COW";
                break;
            case 6:
                zodiacSign = "TIGER";
                break;
            case 7:
                zodiacSign = "RABBIT";
                break;
            case 8:
                zodiacSign = "DRAGON";
                break;
            case 9:
                zodiacSign = "SNAKE";
                break;
            case 10:
                zodiacSign = "HORSE";
                break;
            case 11:
                zodiacSign = "SHEEP";
                break;
        }
        return zodiacSign;


    }
    @Transactional // 메소드 동작이 SQL 쿼리문임을 선언합니다.
    public Long updateBySearch(Long id, boolean checkDiary) {  // 일기작성, 운세확인 유무 초기화
        User user = userRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 아이디가 존재하지 않습니다.")
        );
        user.reset(checkDiary);
        return id;
    }

}