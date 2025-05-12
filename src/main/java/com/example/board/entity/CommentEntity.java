package com.example.board.entity;

import com.example.board.dto.CommentDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "comment_table")
public class CommentEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String commentWriter;

    @Column
    private String commentContents;

    /* Board:Comment = 1:N 관계 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")  // comment_table.board_id -> board_table 참조 (id를 참조함)
    private BoardEntity boardEntity;

    public static CommentEntity toSaveEntity(CommentDTO commentDTO, BoardEntity boardEntity) {
        CommentEntity commentEntity = new CommentEntity();  // entity의 기본생성자는 private으로 하자 (외부에 노출시키지 않기)
        commentEntity.setCommentWriter(commentDTO.getCommentWriter());
        commentEntity.setCommentContents(commentDTO.getCommentContents());
        // 게시글 번호로 조회한 부모 엔티티 값
        commentEntity.setBoardEntity(boardEntity);

        return commentEntity;
    }
}
