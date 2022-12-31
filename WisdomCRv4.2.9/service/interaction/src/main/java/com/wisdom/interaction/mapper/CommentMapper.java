package com.wisdom.interaction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wisdom.comment.bean.dto.CommentDTO;
import com.wisdom.comment.domain.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    void add(CommentDTO commentDTO);
}
