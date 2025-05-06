package com.example.board.repository;

import com.example.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// repository는 기본적으로 entity 클래스만 받는다.
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {  // BoardEntity 클래스와 PK 타입(Long) 상속
    // UPDATE board_table SET board_hits=board_hits+1 WHERE id=?;  (MySQL 기준, id 값에 해당하는 게시물의 조회수+1)
    @Modifying // update나 delete 같은 쿼리를 실행할 때 붙여야 하는 애노테이션
    @Query(value = "update BoardEntity b set b.boardHits=b.boardHits+1 where b.id=:id") // entity를 기준으로 작성한 쿼리문
    void updateHits(@Param("id") Long id);  // :id는 파라미터로 받는 "id"
    // @Query(value = "~~~", nativeQuery = true) 설정하면 value = "~~~"에 실제 DB에서 사용되는 쿼리 주입 가능
}
