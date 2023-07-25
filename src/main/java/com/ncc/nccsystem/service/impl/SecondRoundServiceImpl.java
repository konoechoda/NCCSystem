package com.ncc.nccsystem.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncc.nccsystem.constants.SystemConstants;
import com.ncc.nccsystem.domain.entity.Counselors;
import com.ncc.nccsystem.domain.entity.SecondRound;
import com.ncc.nccsystem.domain.vo.SecondRoundVo;
import com.ncc.nccsystem.mapper.SecondRoundMapper;
import com.ncc.nccsystem.service.CounselorsService;
import com.ncc.nccsystem.service.SecondRoundService;
import com.ncc.nccsystem.utils.BeanCopyUtils;
import com.ncc.nccsystem.utils.GroupingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * (SecondRound)表服务实现类
 *
 * @author makejava
 * @since 2023-07-15 15:10:54
 */
@Service("secondRoundService")
public class SecondRoundServiceImpl extends ServiceImpl<SecondRoundMapper, SecondRound> implements SecondRoundService {
    @Autowired
    private CounselorsService counselorsService;

    /**
     * 第二轮抽签
     */
    @Override
    public SaResult secondRoundDraw(String groupName) {
        //根据分组名称获取分组
        String grouping;
        switch (groupName) {
            case SystemConstants.UNDERGRADUATE_GROUP:
                grouping = SystemConstants.UNDERGRADUATE_DATABASE;
                break;
            case SystemConstants.SPECIALTY_GROUP:
                grouping = SystemConstants.SPECIALTY_DATABASE;
                break;
            default:
               return SaResult.error("分组名称错误");
        }

        //查询是否已经分组
        List<SecondRound> s = this.query().eq("grouping", grouping).list();
        
        if (s.size() > 0) {
            //返回分组结果
            List<SecondRoundVo> sVo = BeanCopyUtils.copyBeanList(s, SecondRoundVo.class);

            int pageSize = SystemConstants.DRAWING_LIMIT; // 每页的大小

            //将List分页
            List<List<SecondRoundVo>> paginatedList = IntStream.range(0, (sVo.size() + pageSize - 1) / pageSize)
                    .mapToObj(i -> sVo.subList(i * pageSize, Math.min((i + 1) * pageSize, sVo.size())))
                    .collect(Collectors.toList());

            return SaResult.data(paginatedList);
        }

        
        //抽取该分组下笔试成绩前三十名
        List<Counselors> counselors = counselorsService.selectNumberOfCountByScore(SystemConstants.SECOND_ROUND_LIMIT, grouping);

        //打乱顺序
        Collections.shuffle(counselors);
        //抽签
        List<Counselors> counselorsList = GroupingUtils.partitionList(counselors, SystemConstants.REGISTRATION_LIMIT);

        List<SecondRoundVo> secondRoundVos = BeanCopyUtils.copyBeanList(counselorsList, SecondRoundVo.class);

        int temp = 1;
        for (int i = 0; i < secondRoundVos.size(); i+=2) {
            secondRoundVos.get(i).setDrawNumber("A" + temp);
            secondRoundVos.get(i+1).setDrawNumber("B" + temp);
            temp++;
        }
        //将抽签结果存入数据库
        List<SecondRound> secondRounds = BeanCopyUtils.copyBeanList(secondRoundVos, SecondRound.class);
        this.saveBatch(secondRounds);

        int pageSize = SystemConstants.DRAWING_LIMIT; // 每页的大小

        //将List分页
        List<List<SecondRoundVo>> paginatedList = IntStream.range(0, (secondRoundVos.size() + pageSize - 1) / pageSize)
                .mapToObj(i -> secondRoundVos.subList(i * pageSize, Math.min((i + 1) * pageSize, secondRoundVos.size())))
                .collect(Collectors.toList());

        return SaResult.data(paginatedList);
    }

    @Override
    public SaResult interviewRound(String groupName) {
        //根据分组名称获取分组
        String grouping;
        switch (groupName) {
            case SystemConstants.UNDERGRADUATE_GROUP:
                grouping = SystemConstants.UNDERGRADUATE_DATABASE;
                break;
            case SystemConstants.SPECIALTY_GROUP:
                grouping = SystemConstants.SPECIALTY_DATABASE;
                break;
            default:
                return SaResult.error("分组名称错误");
        }
        //根据分组名称获取数据
        List<SecondRound> secondRound = this.query().eq("grouping", grouping).list();
        List<SecondRoundVo> secondRoundVos = BeanCopyUtils.copyBeanList(secondRound, SecondRoundVo.class);
        //分页
        int pageSize = 1; // 每页的大小
        List<List<SecondRoundVo>> paginatedList = IntStream.range(0, (secondRoundVos.size() + pageSize - 1) / pageSize)
                .mapToObj(i -> secondRoundVos.subList(i * pageSize, Math.min((i + 1) * pageSize, secondRoundVos.size())))
                .collect(Collectors.toList());
        return SaResult.data(paginatedList);
    }
}
