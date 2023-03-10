package shop.mtcoding.blog.model;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import shop.mtcoding.blog.dto.board.BoardResp.BoardDetailResponseDto;
import shop.mtcoding.blog.dto.reply.ReplyResp.ReplyDetailRespDto;

interface Dao {
    public List<Reply> findAll();

    public Reply findById(int id);

    public int insert(@Param("comment") String comment, @Param("boardId") int boardId, @Param("userId") int userId);

    public int updateById(@Param("id") int id, @Param("comment") String comment);

    public int deleteById(int id);
}

@Mapper
public interface ReplyRepository extends Dao {
    public List<ReplyDetailRespDto> findAllWithUser(@Param("searchOpt") String searchOpt, @Param("words") String words);
    
    public List<BoardDetailResponseDto> findByBoardIdWithUser(int boardId);
}
