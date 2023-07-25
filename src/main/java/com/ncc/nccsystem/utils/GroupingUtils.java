package com.ncc.nccsystem.utils;

import com.ncc.nccsystem.constants.SystemConstants;
import com.ncc.nccsystem.domain.entity.Counselors;

import java.util.*;

public class GroupingUtils {

    private GroupingUtils() {

    }

    /**
     * 将学生列表分成numGroups个子列表，每个子列表中的学生来自不同的学校
     *
     * @param counselorsList 学生列表
     * @param numGroups      子列表个数(<=3)
     * @return 分组结果
     */
    public static List<Counselors> partitionList(List<Counselors> counselorsList, int numGroups) {
        //处理counselorsList为空的情况
        if (counselorsList == null || counselorsList.size() == 0) {
            return new ArrayList<>();
        }
        if(counselorsList.size() % numGroups != 0){
            //添加学校id为空白的counselors,补充到numGroups的倍数
            int addNum = numGroups - counselorsList.size() % numGroups;
            for (int i = 0; i < addNum; i++) {
                Counselors c = new Counselors();
                c.setSchoolId(SystemConstants.BLANK_SCHOOL_ID);
                counselorsList.add(c);
            }
        }
        // 学校集合，用于检查学校是否已被选择
        List<Set<Long>> selectedSchools = new ArrayList<>();

        // 结果列表，包含numGroups个子列表
        List<List<Counselors>> resultList = new ArrayList<>();
        for (int i = 0; i < numGroups; i++) {
            resultList.add(new ArrayList<>());
            selectedSchools.add(new HashSet<>());
        }

        // 遍历学生列表，将学生分配到不同的子列表中
        for (Counselors counselors : counselorsList) {
            boolean added = false;
            // 在已选择的学校中查找
            for (int i = 0; i < numGroups; i++) {
                if (!selectedSchools.get(i).contains(counselors.getSchoolId())) {
                    resultList.get(i).add(counselors);
                    selectedSchools.get(i).add(counselors.getSchoolId());
                    added = true;
                    break;
                }
            }
            // 如果所有子列表都有来自该学校的学生，则随机选择一个子列表添加学生
            if (!added) {
                int randomIndex = (int) (Math.random() * numGroups);
                resultList.get(randomIndex).add(counselors);
            }
        }

        //清空resultList中的空白counselors(基本不会出现该情况)
        // for (int i = 0; i < resultList.size(); i++) {
        //     for (int j = 0; j < resultList.get(i).size(); j++) {
        //         if(Objects.equals(resultList.get(i).get(j).getSchoolId(), SystemConstants.BLANK_SCHOOL_ID)){
        //             resultList.get(i).remove(j);
        //         }
        //     }
        // }

        //调整resultList,保证三个list不会出现首尾相同的情况
        for (int i = 0; i < resultList.size()-1; i++) {
            if(Objects.equals(resultList.get(i).get(resultList.get(i).size() - 1).getSchoolId(), resultList.get(i + 1).get(0).getSchoolId())){
                //交换resultList.get(i)最后两个元素的位置
                Collections.swap(resultList.get(i),resultList.get(i).size()-1,resultList.get(i).size()-2);
                if(Objects.equals(resultList.get(i).get(resultList.get(i).size() - 1).getSchoolId(), resultList.get(i + 1).get(0).getSchoolId())){
                    //交换resultList.get(i+1)前面两个元素的位置
                    Collections.swap(resultList.get(i+1),0,1);
                }
            }
        }
        List<Counselors> counselors = new ArrayList<>();
        for (List<Counselors> list : resultList) {
            counselors.addAll(list);
        }
        return counselors;
    }
}
