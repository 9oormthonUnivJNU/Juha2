package com.example.board.repository;

import com.example.board.entity.BoardEntity;
import com.example.board.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    /*
        select * from comment_table where board_id=? order by id desc; 를 실행하기 위해

            findAllByBoardEntity => select * from comment_table where board_id=?
            OrderByIdDesc => order by id desc;

        -> findAllByBoardEntityOrderByIdDesc 이름 형식 지킬 것 (순서, 대소문자)
    */
    // 이렇게 boardId를 기준으로 조회할 때는 BoardEntity를 매개변수로 넘겨주어야 함
    List<CommentEntity> findAllByBoardEntityOrderByIdDesc(BoardEntity boardEntity);
}
