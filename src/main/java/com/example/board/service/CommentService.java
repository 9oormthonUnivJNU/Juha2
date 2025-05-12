package com.example.board.service;

import com.example.board.dto.CommentDTO;
import com.example.board.entity.BoardEntity;
import com.example.board.entity.CommentEntity;
import com.example.board.repository.BoardRepository;
import com.example.board.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository; // 부모 테이블에 접근(외래키 참조)

    public Long save(CommentDTO commentDTO) {
        /* 부모 entity(BoardEntity) 조회 */
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(commentDTO.getBoardId());

        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            // toSaveEntity에 boardEntity도 같이 보내야 함(메서드 내부에서 부모 entity를 생성해야 하기 때문)
            CommentEntity commentEntity = CommentEntity.toSaveEntity(commentDTO, boardEntity);

            return commentRepository.save(commentEntity).getId();
        } else {
            return null;
        }
    }

    @Transactional
    public List<CommentDTO> findAll(Long boardId) {
        // select * from comment_table where board_id=? order by id desc;
        BoardEntity boardEntity = boardRepository.findById(boardId).get();  // BoardEntity 가져오기
        List<CommentEntity> commentEntityList = commentRepository.findAllByBoardEntityOrderByIdDesc((boardEntity));

        // EntityList -> DTOList 변환
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (CommentEntity commentEntity : commentEntityList) {
            CommentDTO commentDTO = CommentDTO.toCommentDTO(commentEntity);
            commentDTOList.add(commentDTO);
        }

        return commentDTOList;
    }
}
