package com.example.board.entity;

import com.example.board.dto.BoardDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    // DTO에 담긴 값들을 Entity 객체로 옮겨 닮는 작업 (DTO -> Entity)
    public static BoardEntity toSaveEntity(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(0);

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
}
