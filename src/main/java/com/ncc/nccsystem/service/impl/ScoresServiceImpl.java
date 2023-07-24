package com.ncc.nccsystem.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncc.nccsystem.constants.SystemConstants;
import com.ncc.nccsystem.domain.entity.Scores;
import com.ncc.nccsystem.domain.vo.FinalScoresVo;
import com.ncc.nccsystem.mapper.ScoresMapper;
import com.ncc.nccsystem.service.JudgeScoresService;
import com.ncc.nccsystem.service.ScoresService;
import com.ncc.nccsystem.utils.BeanCopyUtils;
import com.ncc.nccsystem.utils.TestFileUtil;
import com.spire.xls.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * (Scores)表服务实现类
 *
 * @author makejava
 * @since 2023-07-14 21:24:12
 */
@Service("scoresService")
public class ScoresServiceImpl extends ServiceImpl<ScoresMapper, Scores> implements ScoresService {

    @Autowired
    private JudgeScoresService judgeScoresService;

    /**
     * 导出成绩表
     */
    @Override
    public SaResult exportPdf(String groupName, HttpServletResponse response) {

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

        //查询笔试的前30名并按照最终成绩排序
        List<Scores> scoresList = this.query()
                .eq("grouping",grouping)
                .orderByDesc("written_test_score")
                .last("LIMIT " + SystemConstants.SECOND_ROUND_LIMIT)
                .list()
                .stream()
                .sorted(Comparator.comparingDouble(Scores::getFinalScore).reversed())
                .collect(Collectors.toList());

        //如果没有最终成绩，计算最终成绩
        if(scoresList.get(0).getFinalScore() == 0.0) {

            List<Scores> s = judgeScoresService.calculateFinalScore();

            scoresList.forEach(scores -> s.stream()
                    .filter(scores1 -> Objects.equals(scores.getIdCard(), scores1.getIdCard()))
                    .findFirst()
                    .ifPresent(scores1 -> {
                        scores.setDiscussionScore(scores1.getDiscussionScore());
                        scores.setInterviewScore(scores1.getInterviewScore());
                    })
            );

            scoresList.forEach(scores -> scores.setFinalScore(scores.getWrittenTestScore() * 0.2 + scores.getDiscussionScore() * 0.4 + scores.getInterviewScore() * 0.4));
            //批量更新
            this.updateBatchById(scoresList);
            //重新查询,防止小数位数问题导致的数据不一致
            //按最终得分降序查询
            scoresList = this.query()
                    .eq("grouping",grouping)
                    .orderByDesc("final_score")
                    .last("LIMIT " + SystemConstants.SECOND_ROUND_LIMIT)
                    .list();
        }

        List<FinalScoresVo> finalScoresVos = BeanCopyUtils.copyBeanList(scoresList, FinalScoresVo.class);

        // 模板注意 用{} 来表示你要用的变量 如果本来就有"{","}" 特殊字符 用"\{","\}"代替
        // 填充list 的时候还要注意 模板中{.} 多了个点 表示list
        // 如果填充list的对象是map,必须包涵所有list的key,哪怕数据为null，必须使用map.put(key,null)
        String templateFileName =
                TestFileUtil.getPath() + "static" + File.separator + "export" + File.separator +"template"+ File.separator + "FinalScores.xlsx";
        // 方案1 一下子全部放到内存里面 并填充
//        String fileName = TestFileUtil.getPath() + "listFill" + System.currentTimeMillis() + ".xlsx";
        // 这里 会填充到第一个sheet， 然后文件流会自动关闭
//        EasyExcel.write(fileName).withTemplate(templateFileName).sheet().doFill(data());

        String currentThreadName = "FinalScores"+ System.currentTimeMillis();
        // 方案2 分多次 填充 会使用文件缓存（省内存）
        String fileName = TestFileUtil.getPath() + currentThreadName + ".xlsx";
        try (ExcelWriter excelWriter = EasyExcel.write(fileName).withTemplate(templateFileName).build()) {
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            excelWriter.fill(finalScoresVos, writeSheet);
        }

        String fileName1 = "target" + File.separator +"classes"+ File.separator + currentThreadName;
        Workbook workbook = new Workbook();
        workbook.loadFromFile(fileName1+".xlsx");
        workbook.saveToFile(fileName1+".pdf",com.spire.xls.FileFormat.PDF);

        try {
            // path是指想要下载的文件的路径
            File file = new File(TestFileUtil.getPath() + currentThreadName + ".pdf");

            // 获取文件名
            String filename = file.getName();
            // 获取文件后缀名
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
            System.out.println("文件后缀名：" + ext);

            // 将文件写入输入流
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStream fis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();

            // 清空response
            response.reset();
            // 设置response的Header
            response.setCharacterEncoding("UTF-8");
            //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
            //attachment表示以附件方式下载   inline表示在线打开   "Content-Disposition: inline; filename=文件名.mp3"
            // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            // 告知浏览器文件的大小
            response.addHeader("Content-Length", "" + file.length());
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            outputStream.write(buffer);
            outputStream.flush();

            return SaResult.ok();

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
