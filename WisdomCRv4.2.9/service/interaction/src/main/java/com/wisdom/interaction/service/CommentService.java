package com.wisdom.interaction.service;

import com.wisdom.base.util.R;
import com.wisdom.comment.bean.dto.CommentDTO;

public interface CommentService {
    R add(CommentDTO commentDTO);
}
