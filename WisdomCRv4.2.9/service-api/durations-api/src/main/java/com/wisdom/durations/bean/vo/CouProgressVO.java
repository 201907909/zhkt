package com.wisdom.durations.bean.vo;

import com.wisdom.chapter.bean.vo.ChapterVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class CouProgressVO {

    private String couIntroduction;
    private Float compreEvScore;
    private List<ChapterVO> chapterVOList;
}
