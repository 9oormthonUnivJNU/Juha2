package com.example.board.dto;

import com.example.board.entity.CommentEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CommentDTO {
    private Long id;
    private String commentWriter;
    private String commentContents;
    private Long boardId;
    private LocalDateTime commentCreatedTime;

    public static CommentDTO toCommentDTO(CommentEntity commentEntity) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(commentEntity.getId());
        commentDTO.setCommentWriter(commentEntity.getCommentWriter());
        commentDTO.setCommentContents(commentEntity.getCommentContents());
        commentDTO.setCommentCreatedTime(commentEntity.getCreateTime());
        // CommentEntity의 Board_Id -> BoardEntity의 id 를 참조
        commentDTO.setBoardId(commentEntity.getBoardEntity().getId()); // 이 구조에서는 Service 메서드에 @Transactional 트랜잭션 처리해줘야 함

        return commentDTO;
    }
}
