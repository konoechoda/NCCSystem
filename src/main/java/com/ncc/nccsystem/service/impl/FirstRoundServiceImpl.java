package com.ncc.nccsystem.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncc.nccsystem.constants.SystemConstants;
import com.ncc.nccsystem.domain.entity.Counselors;
import com.ncc.nccsystem.domain.entity.FirstRound;
import com.ncc.nccsystem.domain.entity.Scores;
import com.ncc.nccsystem.domain.vo.DrawingResultVo;
import com.ncc.nccsystem.domain.vo.WrittenTestExcelVo;
import com.ncc.nccsystem.handler.exception.SystemException;
import com.ncc.nccsystem.mapper.FirstRoundMapper;
import com.ncc.nccsystem.service.CounselorsService;
import com.ncc.nccsystem.service.FirstRoundService;
import com.ncc.nccsystem.service.ScoresService;
import com.ncc.nccsystem.service.UserService;
import com.ncc.nccsystem.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * (FirstRound)表服务实现类
 *
 * @author makejava
 * @since 2023-07-13 17:17:04
 */
@Service("firstRoundService")
public class FirstRoundServiceImpl extends ServiceImpl<FirstRoundMapper, FirstRound> implements FirstRoundService {

    @Autowired
    private CounselorsService counselorsService;
    @Autowired
    private UserService userService;

    @Autowired
    private ScoresService scoresService;

    /**
     * 随机分组
     */
    @Override
    public SaResult randomGroup(String groupName) {
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
        //获取所有符合分组条件的辅导员
        List<FirstRound> firstRounds = counselorsService.query()
                .eq("grouping", grouping)
                .list()
                .stream()
                .map(counselors -> {
                    FirstRound firstRound = new FirstRound();
                    firstRound.setCounselorId(counselors.getId());
                    return firstRound;
                })
                .collect(Collectors.toList());

        //随机排序
        Collections.shuffle(firstRounds);
        //初始化序号
        AtomicInteger atomicInteger = new AtomicInteger(0);
        //打上分组标签
        firstRounds.forEach(drawingResultVo -> drawingResultVo.setDrawNumber(groupName + atomicInteger.incrementAndGet()));
        //保存到数据库
        this.saveBatch(firstRounds);
        //将结果转换为VO
        List<DrawingResultVo> drawingResultVos = firstRounds.stream()
                .map(firstRound -> {
                    DrawingResultVo drawingResultVo = new DrawingResultVo();
                    drawingResultVo.setCounselorName(counselorsService.getById(firstRound.getCounselorId()).getName());
                    drawingResultVo.setSchoolName(userService.getById(counselorsService.getById(firstRound.getCounselorId()).getSchoolId()).getUserName());
                    drawingResultVo.setDrawNumber(firstRound.getDrawNumber());
                    return drawingResultVo;
                })
                .collect(Collectors.toList());

        //返回结果
        return SaResult.data(drawingResultVos);
    }

    /**
     * 导入笔试成绩
     */
    @Override
    public SaResult importScore(MultipartFile excelFile) {
        try{
            //读取excel文件
            List<WrittenTestExcelVo> writtenTestExcelVos = EasyExcel.read(excelFile.getInputStream())
                    .head(WrittenTestExcelVo.class)
                    .sheet()
                    .doReadSync();
            //验证数据(参赛人员的个人信息)
            writtenTestExcelVos
                    .forEach(writtenTestExcelVo -> {
                        Counselors counselors = counselorsService.query()
                                .eq("name", writtenTestExcelVo.getCounselorName())
                                .eq("grouping", writtenTestExcelVo.getGrouping())
                                .eq("id_card", writtenTestExcelVo.getIdCard())
                                .one();
                        if (Objects.isNull(counselors)){
                            throw new SystemException(writtenTestExcelVo.getCounselorName()+"的信息出现异常,请检查参赛人员的个人信息是否正确");
                        }
                    });
            //将excel数据转换为Scores对象
            List<Scores> scores = BeanCopyUtils.copyBeanList(writtenTestExcelVos, Scores.class);
            //将数据保存到数据库
            scoresService.saveBatch(scores);
            return SaResult.ok("导入成功");
        }catch (IOException e){
            return SaResult.error("文件读取失败");
        }
    }
}
