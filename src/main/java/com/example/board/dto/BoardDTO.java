package com.example.board.dto;

import com.example.board.entity.BoardEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

// lombok
@Getter
@Setter
@ToString
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자
// DTO (Data Transfer Object)
public class BoardDTO {
    private Long id;
    private String boardWriter;
    private String boardPass;
    private String boardTitle;
    private String boardContents;
    private int boardHits; // 조회수
    private LocalDateTime boardCreateTime; // 글 작성 시간
    private LocalDateTime boardUpdateTime; // 글 수정 시간

    // save.html -> Controller 파일 담는 용도
    private MultipartFile boardFile; // 실제 파일을 받을 수 있는 인터페이스 타입
    // Service (비즈니스 로직) 에서 설정해줄 부분들
    private String originalFileName; // 원본 파일 이름
    private String storedFileName;   // 서버 저장용 파일 이름 (혹시 모를 파일 이름 중복 예방 및 관리)
    private int fileAttached; // 파일 첨부 여부 (첨부 1, 미첨부 0) 플래그 값 (boolean 타입으로 하면 entity 에서 좀 복잡해져서 int 사용)

    // paging에 필요한 BoardDTO 객체 초기화
    public BoardDTO(Long id, String boardWriter, String boardTitle, int boardHits, LocalDateTime boardCreateTime) {
        this.id = id;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardHits = boardHits;
        this.boardCreateTime = boardCreateTime;
    }

    public static BoardDTO toBoardDTO(BoardEntity boardEntity) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(boardEntity.getId());
        boardDTO.setBoardWriter(boardEntity.getBoardWriter());
        boardDTO.setBoardPass(boardEntity.getBoardPass());
        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
        boardDTO.setBoardContents(boardEntity.getBoardContents());
        boardDTO.setBoardHits(boardEntity.getBoardHits());
        boardDTO.setBoardCreateTime(boardEntity.getCreateTime());
        boardDTO.setBoardUpdateTime(boardEntity.getUpdateTime());

        // fileAttached 여부에 따라 파일을 보여줄지 결정
        int boardFileAttached = boardEntity.getFileAttached();
        if (boardFileAttached == 0) {
            boardDTO.setFileAttached(boardFileAttached); // 0
        } else {
            boardDTO.setFileAttached(boardFileAttached); // 1
            /*
              파일 이름 가져가기 Entity -> DTO
              originalFileName, storedFileName -> board_file_table(BoardFileEntity)에만 있음
              하지만 BoardEntity(board_table) 와 BoardFileEntity(board_file_table) 간 연관 관계를 맺어 놓았기 때문에
              서로 다른 테이블에 있는 값들을 가져올 때는 JOIN 을 사용하면 쉬움
              select * from board_table b join board_file_table bf on b.id = bf.board_id where b.id = ?;
            */
            // BoardEntity에서 file table에 참조 관계를 맺기 위해 JPA 문법에 맞게 BoardFileEntityList를 정의해 놓았기 때문에 해당 getter를 사용할 수 있음 (부모 -> 자식 객체 직접 접근)
            boardDTO.setOriginalFileName(boardEntity.getBoardFileEntityList().get(0).getOriginalFileName());  // 0번 인덱스의 BoardFileEntity 객체의 originalFileName
            boardDTO.setStoredFileName(boardEntity.getBoardFileEntityList().get(0).getStoredFileName());  // 실제로는 storedFileName만 사용되는 경우가 많음

            /*
                중요!
                BoardEntity가 BoardFileEntity에 접근하고 있기 때문에
                toBoardDTO()를 호출하는 메서드 (여기서는 BoardService 의 findById(), findAll() 메서드)에
                @Transactional 을 붙여 트랜잭션 처리를 해야 함
            */
        }

        return boardDTO;
    }
}
