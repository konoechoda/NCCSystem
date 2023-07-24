package com.ncc.nccsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ncc.nccsystem.domain.entity.Counselors;

import java.util.List;


/**
 * (Counselors)表数据库访问层
 *
 * @author makejava
 * @since 2023-07-12 22:02:41
 */
public interface CounselorsMapper extends BaseMapper<Counselors> {

    List<Counselors> selectNumberOfCountByScore(int number, String groupName);
}
