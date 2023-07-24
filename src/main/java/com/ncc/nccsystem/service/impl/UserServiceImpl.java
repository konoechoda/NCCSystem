package com.ncc.nccsystem.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncc.nccsystem.domain.entity.Counselors;
import com.ncc.nccsystem.domain.entity.JudgeScores;
import com.ncc.nccsystem.domain.entity.Scores;
import com.ncc.nccsystem.domain.entity.User;
import com.ncc.nccsystem.domain.vo.ScoresVo;
import com.ncc.nccsystem.mapper.UserMapper;
import com.ncc.nccsystem.service.CounselorsService;
import com.ncc.nccsystem.service.JudgeScoresService;
import com.ncc.nccsystem.service.ScoresService;
import com.ncc.nccsystem.service.UserService;
import com.ncc.nccsystem.utils.BeanCopyUtils;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2023-07-12 17:19:33
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private ScoresService scoresService;

    @Autowired
    private CounselorsService counselorsService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private JudgeScoresService judgeScoresService;

    /**
     * 用户登录
     */
    @Override
    public SaResult doLogin(User user) {
        //通过user的account查询数据库中的user
        User u = this.lambdaQuery().eq(User::getAccount, user.getAccount()).one();
        //判断用户是否存在
        if (u == null) {
            return SaResult.error("账号不存在");
        }
        //判断密码是否正确
        if (!SaSecureUtil.md5(user.getPassword()).equals(u.getPassword())) {
            return SaResult.error("密码错误");
        }
        //登录
        StpUtil.login(u.getId());
        //获取当前会话的token值
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        //返回token值
        return SaResult.data(tokenInfo.getTokenValue());
    }

    /**
     * 用户注册(用于数据库数据初始化)
     */
    @Override
    public SaResult doRegister(User user) {
        //判断账号是否已经存在
        User u = this.lambdaQuery().eq(User::getAccount, user.getAccount()).one();
        if (u != null) {
            return SaResult.error("账号已存在");
        }
        //密码加密
        user.setPassword(SaSecureUtil.md5(user.getPassword()));
        //注册
        this.save(user);
        return SaResult.data("注册成功");
    }

    /**
     * 用户注销
     */
    @Override
    public SaResult logout() {
        //注销
        StpUtil.logout();
        return SaResult.data("注销成功");
    }

    /**
     * 评分
     */
    @Override
    public SaResult judge(ScoresVo scoresVo, String type) {

        if(scoresVo.getScore() == null){
            return SaResult.error("评分为空");
        }

        JudgeScores one = judgeScoresService.query()
                .eq("user_id", StpUtil.getLoginIdAsInt())
                .eq("id_card", scoresVo.getIdCard())
                .one();

        switch (type){
            case "discussion_score":
                if(one != null){
                    if(one.getDiscussionScore() != null){
                        return SaResult.error("您已经完成该辅导员评分");
                    }
                }
                break;
            case "interview_score":
                if(one != null){
                    if(one.getInterviewScore() != null){
                        return SaResult.error("您已经完成该辅导员评分");
                    }
                }
                break;
            default:
                return SaResult.error("参数异常");
        }

        //查询报名信息
        Scores scores = scoresService.query()
                .eq("counselor_name",scoresVo.getCounselorName())
                .eq("grouping", scoresVo.getGrouping())
                .eq("id_card", scoresVo.getIdCard())
                .one();
        //如果有报名信息但score表单中没有该辅导员的信息,则添加该辅导员的信息
        if (scores == null){
            Counselors counselors = counselorsService.query()
                    .eq("name", scoresVo.getCounselorName())
                    .eq("grouping", scoresVo.getGrouping())
                    .eq("id_card", scoresVo.getIdCard())
                    .one();
            if (counselors != null) {
                scores = BeanCopyUtils.copyBean(scoresVo, Scores.class);
                //上传数据库
                scoresService.save(scores);
            }else{
                return SaResult.error("无报名信息");
            }
        }


        //消息唯一ID
        String messageId = UUID.randomUUID().toString();
        //消息内容
        String messageData = "评分: " + JSON.toJSONString(scoresVo);
        //当前时间
        String current = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        Map<String,Object> map = new HashMap<>();
        //将内容封装到map中
        map.put("messageId",messageId);
        map.put("data",messageData);
        map.put("current",current);

        map.put("ScoresVo",scoresVo);
        map.put("type",type);
        map.put("id",StpUtil.getLoginIdAsInt());

        //将消息携带绑定键值：judge 发送到交换机NCCSystemDirectExchange
        rabbitTemplate.convertAndSend("NCCSystemDirectExchange", "judge", map, new CorrelationData(UUID.randomUUID().toString()));
        return SaResult.ok("评分成功");
    }

    @Override
    public SaResult writtenJudge(ScoresVo scoresVo) {

        if(scoresVo.getScore() == null){
            return SaResult.error("评分为空");
        }

        //查询报名信息
        Scores scores = scoresService.query()
                .eq("counselor_name",scoresVo.getCounselorName())
                .eq("grouping", scoresVo.getGrouping())
                .eq("id_card", scoresVo.getIdCard())
                .one();
        //如果有报名信息但score表单中没有该辅导员的信息,则添加该辅导员的信息
        if (scores == null){
            Counselors counselors = counselorsService.query()
                    .eq("name", scoresVo.getCounselorName())
                    .eq("grouping", scoresVo.getGrouping())
                    .eq("id_card", scoresVo.getIdCard())
                    .one();
            if (counselors != null) {
                scores = BeanCopyUtils.copyBean(scoresVo, Scores.class);
                //上传数据库
                scoresService.save(scores);
            }else{
                return SaResult.error("无报名信息");
            }
        }
        //写入笔试成绩
        scores.setWrittenTestScore(scoresVo.getScore());
        //更新数据库
        scoresService.updateById(scores);

        return SaResult.ok("评分成功");
    }

    /**
     * 评分消息接收处理
     */
    @Override
    public SaResult processJudge(ScoresVo scoresVo, String type, Integer id) {

        JudgeScores judgeScores = new JudgeScores();

        judgeScores.setUserId(id);
        judgeScores.setIdCard(scoresVo.getIdCard());

        JudgeScores one = judgeScoresService.query()
                .eq("user_id", id)
                .eq("id_card", scoresVo.getIdCard())
                .one();

        switch (type){
            case "discussion_score":
                if(one != null){
                    if(one.getDiscussionScore() != null){
                        return SaResult.error("您已经完成该辅导员评分");
                    }
                    one.setDiscussionScore(scoresVo.getScore());
                }
                judgeScores.setDiscussionScore(scoresVo.getScore());
                break;
            case "interview_score":
                if(one != null){
                    if(one.getInterviewScore() != null){
                        return SaResult.error("您已经完成该辅导员评分");
                    }
                    one.setInterviewScore(scoresVo.getScore());
                }
                judgeScores.setInterviewScore(scoresVo.getScore());
                break;
            default:
                return SaResult.error("参数异常");
        }

        //上传数据库
        if(one != null){
            judgeScoresService.updateById(one);
        }else {
            judgeScoresService.save(judgeScores);
        }
        return SaResult.ok("评分成功");
    }

}
