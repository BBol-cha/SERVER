package project.BBolCha.domain.board.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import project.BBolCha.domain.board.dto.service.response.*;
import project.BBolCha.domain.board.entity.*;
import project.BBolCha.domain.user.entity.QUser;

import javax.persistence.EntityManager;

import java.util.List;

import static project.BBolCha.domain.board.entity.QBoard.*;
import static project.BBolCha.domain.board.entity.QHint.*;
import static project.BBolCha.domain.board.entity.QLike.*;
import static project.BBolCha.domain.board.entity.QTag.*;
import static project.BBolCha.domain.user.entity.QUser.*;

public class BoardRepositoryImpl implements BoardQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public BoardRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<BoardResponse.Detail> getPageBoardsAsDto(Pageable pageable) {
        List<BoardResponse.Detail> content = queryFactory
                .select(Projections.fields(BoardResponse.Detail.class,
                        board.id.as("id"),
                        board.user.name.as("authorName"),
                        board.title.as("title"),
                        board.content.as("content"),
                        board.correct.as("correct"),
                        board.contentImageUrl.as("contentImageUrl"),
                        board.like.size().as("likeCount"),
                        board.viewCount.as("viewCount"),
                        board.createdAt.as("createdAt"),
                        board.updatedAt.as("updatedAt"),

                        board.tag.horror,
                        board.tag.daily,
                        board.tag.romance,
                        board.tag.fantasy,
                        board.tag.sf,

                        board.hint.hintOne,
                        board.hint.hintTwo,
                        board.hint.hintThree,
                        board.hint.hintFour,
                        board.hint.hintFive
                ))
                .from(board)
                .leftJoin(board.user, user)
                .leftJoin(board.tag, tag)
                .leftJoin(board.hint, hint)
                .leftJoin(board.like, like)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(board.count())
                .from(board);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
