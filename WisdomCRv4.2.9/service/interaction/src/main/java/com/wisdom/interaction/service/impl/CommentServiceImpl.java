package com.wisdom.interaction.service.impl;

import com.baidu.aip.contentcensor.AipContentCensor;
import com.wisdom.base.util.R;
import com.wisdom.base.util.Sample;
import com.wisdom.base.util.SampleAip;
import com.wisdom.base.util.SnowflakeIdWorker;
import com.wisdom.comment.bean.dto.CommentDTO;
import com.wisdom.interaction.mapper.CommentMapper;
import com.wisdom.interaction.service.CommentService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Value("${APP_ID}")
    public String APP_ID;

    @Value("${API_KEY}")
    public String API_KEY;

    @Value("${SECRET_KEY}")
    public String SECRET_KEY;

    @Override
    public R add(CommentDTO commentDTO) {

        SampleAip sampleAip = new SampleAip(APP_ID, API_KEY, SECRET_KEY);

        int conclusionType = Sample.sampleText(sampleAip.getAip(), commentDTO.getComContent());

        if (conclusionType == 1) {

            long comId = new SnowflakeIdWorker(0, 0).nextId();

            commentDTO.setComId(comId);

            commentMapper.add(commentDTO);

            return R.ok("评论成功");
        }else {
           return R.error("存在违规内容");
        }
    }
}
