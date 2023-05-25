package project.BBolCha.domain.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.BBolCha.domain.board.dto.service.response.BoardResponse;

public interface BoardQueryDslRepository {
    Page<BoardResponse.Detail> getPageBoardsAsDto(Pageable pageable);
}
