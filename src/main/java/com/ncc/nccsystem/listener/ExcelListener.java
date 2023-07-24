package com.ncc.nccsystem.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ExcelListener<T> extends AnalysisEventListener<T> {

    // 返回读取到的excel中的数据(数据缓存)
    List<T> cachedDataList = new ArrayList<>();

    public ExcelListener() {
    }

    /**
    * 这个每一条数据解析都会来调用
    */
    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        log.info("【Excel文件】解析到一条数据{}:", JSON.toJSONString(t));
        cachedDataList .add(t);
    }

    /**
     * 所有数据解析完成了 都会来调用
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        log.info("【Excel文件】Excel所有数据解析完毕！");
    }



    public List<T> getDataList() {
        return cachedDataList;
    }
}
