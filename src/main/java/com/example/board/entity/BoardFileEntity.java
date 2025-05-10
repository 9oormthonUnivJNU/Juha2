package com.example.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "board_file_table")
public class BoardFileEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String originalFileName;

    @Column
    private String storedFileName;

    // board_file_table(자식) 기준 board_table(부모)와 N:1 관계
    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩 (N+1 문제?)
    @JoinColumn(name = "board_id") // 실제 DB에 만들어지는 컬럼
    private BoardEntity boardEntity;  // 부모 entity 타입으로 선언해야 함 => 실제 테이블에는 board_id 값만 들어가 있음
                                      // boardEntity 객체 이름은 BoardEntity에서 정의한 OneToMany에 매핑됨
    /*
      부모 entity 객체를 조회했을 때 자식 entity 객체를 같이 조회할 수 있음
      FetchType은 EAGER / LAZY 가 있는데
      EAGER 는 즉시 로딩 => 부모 조회 시 자식을 다 같이 가져옴/ 굳이 필요 없는 데이터도 다 가져옴, EAGER는 보통 잘 안씀
      LAZY 는 지연 로딩  => 필요한 상황에 호출해서 사용하겠다는 옵션, 주로 LAZY를 사용함
    */


    // BoardEntity -> BoardFileEntity 변환 메서드
    public static BoardFileEntity toBoardFileEntity(BoardEntity boardEntity, String originalFileName, String storedFileName) {
        BoardFileEntity boardFileEntity = new BoardFileEntity();
        boardFileEntity.setOriginalFileName(originalFileName);
        boardFileEntity.setStoredFileName(storedFileName);
        boardFileEntity.setBoardEntity(boardEntity);  // PK 값이 아닌 부모 entity 객체를 set 하는 것이 중요

        return boardFileEntity;
    }
}

