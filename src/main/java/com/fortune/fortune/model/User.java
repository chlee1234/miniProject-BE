package com.fortune.fortune.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter // get 함수를 일괄적으로 만들어줍니다.
@NoArgsConstructor // 기본 생성자를 만들어줍니다.
@Entity // DB 테이블 역할을 합니다.
public class User {

    // ID가 자동으로 생성 및 증가합니다.
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    // nullable: null 허용 여부
    // unique: 중복 허용 여부 (false 일때 중복 허용)
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String dateofbirth;

    @Column(nullable = false)  //띠
    private  String zodiacsign;

    @Column(nullable = false)  //별자리
    private  String starposition;

    @Column(nullable = false) // 일기 작성 유무
    private boolean checkdiary;

    @Column(nullable = false) // 운세 확인 유무
    private FortuneEnum fortuneEnum;




    public User(String username, String password, String nickname, String dateofbirth, String zodiacsign, String starposition) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.dateofbirth = dateofbirth;
        this.zodiacsign = zodiacsign;
        this.starposition = starposition;
        this.fortuneEnum = FortuneEnum.NOT_FORTUNE;
    }
    public void reset(boolean checkDiary){  // 운세보기, 일기작성 부분 초기화
        this.checkdiary = checkDiary;
        this.fortuneEnum = FortuneEnum.NOT_FORTUNE;
    }
    public void updateByCheckfortune(){  // 운세보기
        this.fortuneEnum = FortuneEnum.FORTUNE;
    } // 운세 확인하면 FORTUNE으로 값 변경
}
