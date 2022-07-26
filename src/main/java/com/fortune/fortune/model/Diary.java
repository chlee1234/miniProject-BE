package com.fortune.fortune.model;

import com.fortune.fortune.dto.DiaryRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor // 기본 생성자를 만들어줍니다.
@Setter
@Getter // get 함수를 일괄적으로 만들어줍니다.
@Entity // DB 테이블 역할을 합니다.
public class Diary extends Timestamped {// 생성,수정 시간을 자동으로 만들어줍니다.

    // ID가 자동으로 생성 및 증가합니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column  // 일기 내용
    private String contents;

    @Column(nullable = false)
    private Long userid;

    @Column(nullable = false)
    private String nickname;

    @Column // 랜덤으로 나온 운세
    private String fortune;

    public Diary(DiaryRequestDto requestDto, String fortune, Long userid, String nickname) {
        this.contents = requestDto.getContents();
        this.fortune = fortune;
        this.userid = userid;
        this.nickname = nickname;
    }
}
