package com.ncc.nccsystem.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncc.nccsystem.constants.SystemConstants;
import com.ncc.nccsystem.domain.entity.Counselors;
import com.ncc.nccsystem.domain.entity.User;
import com.ncc.nccsystem.domain.vo.CounselorsVo;
import com.ncc.nccsystem.enums.AppHttpCodeEnum;
import com.ncc.nccsystem.handler.exception.SystemException;
import com.ncc.nccsystem.mapper.CounselorsMapper;
import com.ncc.nccsystem.service.CounselorsService;
import com.ncc.nccsystem.service.UserService;
import com.ncc.nccsystem.utils.BeanCopyUtils;
import com.ncc.nccsystem.utils.IDCardValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * (Counselors)表服务实现类
 *
 * @author makejava
 * @since 2023-07-12 22:02:42
 */
@Service("counselorsService")
public class CounselorsServiceImpl extends ServiceImpl<CounselorsMapper, Counselors> implements CounselorsService {

    //token 名称（同时也是 cookie 名称）
    @Value("${satoken.token-name}")
    private String tokenName;

    @Autowired
    private UserService userService;

    /**
     * 报名
     */
    @Override
    public SaResult register(Counselors counselors) {
        //获取当前学校账号的id
        String schoolId = StpUtil.getSession().getId();
        //查询当前学校的报名人数
        List<Counselors> counselorsList = this.lambdaQuery().eq(Counselors::getSchoolId, schoolId.replace(tokenName+":login:session:","")).list();
        //判断报名人数是否已满
        if (counselorsList.size() >= SystemConstants.REGISTRATION_LIMIT) {
            return SaResult.error("报名人数已满");
        }
        //判断姓名是否为空
        if (counselors.getName() == null || counselors.getName().equals("")) {
            return SaResult.error("姓名不能为空");
        }
        //判断身份证号是否为空
        if (counselors.getIdCard() == null || counselors.getIdCard().equals("")) {
            return SaResult.error("身份证号不能为空");
        }
        //判断组别是否为空
        if (counselors.getGrouping() == null || counselors.getGrouping().equals("")) {
            return SaResult.error("组别不能为空");
        }
        //验证身份证是否符合格式
        boolean isValid = new IDCardValidatorUtils().validateIDCard(counselors.getIdCard());
        if(!isValid){
            return SaResult.error("身份证格式不正确");
        }
        //根据身份证号查询是否已经存在
        Counselors c = this.lambdaQuery().eq(Counselors::getIdCard, counselors.getIdCard()).one();
        if (c != null) {
            return SaResult.error("该辅导员已经报名，请勿重复报名");
        }
        //将学校id设置到辅导员信息中
        counselors.setSchoolId(Long.parseLong(schoolId.replace(tokenName+":login:session:","")));
        //插入数据
        this.save(counselors);
        return SaResult.ok("报名成功");
    }

    /**
     * 根据姓名和身份证号查询辅导员信息
     */
    @Override
    public SaResult queryByIdCard(String idCard) {
        //查询信息
        Counselors counselors = this.lambdaQuery().eq(Counselors::getIdCard, idCard).one();
        //判断是否存在
        if (counselors == null) {
            throw new SystemException(AppHttpCodeEnum.NO_INFORMATION);
        }
        CounselorsVo counselorsVo = BeanCopyUtils.copyBean(counselors, CounselorsVo.class);
        //查询学校名称
        User user = userService.getById(counselors.getSchoolId());
        counselorsVo.setSchoolName(user.getUserName());

        return SaResult.data(counselorsVo);
    }

    @Override
    public SaResult delete(String name, String idCard) {
        //获取当前学校账号的id
        String schoolId = StpUtil.getSession().getId();
        //根据姓名,身份证号,学校id查询信息
        Counselors counselors = this.query()
                .eq("name", name)
                .eq("id_card", idCard)
                .eq("school_id",schoolId.replace(tokenName+":login:session:",""))
                .one();
        //判断是否存在
        if (counselors == null) {
            return SaResult.error("未在该校报名人员中找到该辅导员");
        }
        //删除信息
        this.removeById(counselors.getId());
        return SaResult.ok("删除成功");
    }

    /**
     * 根据姓名查询辅导员信息
     */
    @Override
    public SaResult queryByName(String name) {
        List<Counselors> counselorsList = this.lambdaQuery().eq(Counselors::getName, name).list();
        if (counselorsList == null || counselorsList.size() == 0) {
            throw new SystemException(AppHttpCodeEnum.NO_INFORMATION);
        }
        List<CounselorsVo> counselorsVoList = counselorsList.stream()
                .map(counselors -> BeanCopyUtils.copyBean(counselors, CounselorsVo.class))
                .collect(java.util.stream.Collectors.toList());
        return SaResult.data(counselorsVoList);
    }

    /**
     * 根据分数查询辅导员信息
     */
    public List<Counselors> selectNumberOfCountByScore(int number, String groupName) {
        CounselorsMapper counselorsMapper = getBaseMapper();
        return counselorsMapper.selectNumberOfCountByScore(number, groupName);
    }
}
