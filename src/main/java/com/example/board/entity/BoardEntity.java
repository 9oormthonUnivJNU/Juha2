package com.example.board.entity;

import com.example.board.dto.BoardDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

// Entity = DB의 테이블 역할을 하는 클래스 (JPA에서 테이블을 생성)
@Entity
@Getter
@Setter
@Table(name = "board_table") // BoardEntity에서 정의한데로 생성되는 테이블의 이름을 정의
public class BoardEntity extends BaseEntity {  // BaseEntity를 상속하여 Board 테이블이 생성될 때 Base 테이블도 같이 생성됨
    @Id  // PK 컬럼 지정 (필수)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment 설정
    private Long id;

    @Column(length = 20, nullable = false) // 크기 20byte, NotNull
    private String boardWriter;

    @Column // (디폴트) 크기 255, Null 가능
    private String boardPass;

    @Column
    private String boardTitle;

    @Column(length = 500)
    private String boardContents;

    @Column
    private int boardHits;

    @Column
    private int fileAttached;  // 1(파일 있으면) or 0(파일 없으면)

    // BoardEntity 와 BoardFileEntity 참조 관계 설정 (외래키)
    // BoardFileEntity의 boardEntity 객체 매핑, cascade 설정, 지연 로딩 설정
    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardFileEntity> boardFileEntityList = new ArrayList<>();  // board 하나에 file을 list로 가질 수 있도록 참조 관계 설정
    // 실제 DB에 List 타입 컬럼이 정의되는 것은 아님

    // BoardEntity와 CommentEntity 참조 관계 설정
    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CommentEntity> commentEntityList = new ArrayList<>();


    // DTO에 담긴 값들을 Entity 객체로 옮겨 닮는 작업 (DTO -> Entity)
    public static BoardEntity toSaveEntity(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(0);
        boardEntity.setFileAttached(0); // 파일 없음

        return boardEntity;
    }

    public static BoardEntity toUpdateEntity(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setId(boardDTO.getId());  // DB에 존재하는 해당 게시물의 id
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(boardDTO.getBoardHits());  // DB에 존재하는 게시물의 조회수

        return boardEntity;
    }

    public static BoardEntity toSaveFileEntity(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(0);
        boardEntity.setFileAttached(1); // 파일 있음

        return boardEntity;
    }
}
