package com.yanhuan.html.controller;

import com.yanhuan.html.model.EarlyWarnAreaConfParam;
import com.yanhuan.html.util.PdfUtil;
import com.yanhuan.html.util.ThymeleafUtil;
import com.yanhuan.html.util.WordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试
 *
 * @author Yan
 */
@Slf4j
@RestController
@RequestMapping("/export")
public class PdfTestController {
    /**
     * 获取预警方案
     */
    @GetMapping("/pdf/single")
    public void pdf(HttpServletResponse response) {
        Map<String, Object> map = this.buildSingleTestData();
        try {
            PdfUtil.exportPdf(response, "应急预案", "earlyWarningPlan", map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取预警方案
     */
    @GetMapping("/doc/single")
    public void word(HttpServletResponse response) {
        Map<String, Object> map = this.buildSingleTestData();
        try {
            WordUtil.exportDoc(response, "应急预案", "earlyWarningPlan", map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取预警方案
     */
    @GetMapping("/pdf/all")
    public void pdfAll(HttpServletResponse response) {
        HashMap<String, Map<String, Object>> map = new HashMap<>();
        for (int i = 1; i <= 10; i++) {
            map.put("应急预案" + 1, this.buildSingleTestData());
        }
        try {
            PdfUtil.exportPdfBatch(response, "应急预案", "earlyWarningPlan", map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取预警方案
     */
    @GetMapping("/doc/all")
    public void wordAll(HttpServletResponse response) {
        HashMap<String, Map<String, Object>> map = new HashMap<>();
        for (int i = 1; i <= 10; i++) {
            map.put("应急预案" + 1, this.buildSingleTestData());
        }
        try {
            WordUtil.exportDocBatch(response, "应急预案", "earlyWarningPlan", map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 构建单条测试数据
     */
    private Map<String, Object> buildSingleTestData() {
        EarlyWarnAreaConfParam param1 = new EarlyWarnAreaConfParam();
        param1.setDefenseAreaName("东门防区");
        param1.setAreaName("东门门禁");
        param1.setEventTypeName("人脸识别事件");

        EarlyWarnAreaConfParam param2 = new EarlyWarnAreaConfParam();
        param2.setDefenseAreaName("西门防区");
        param2.setAreaName("西门门禁");
        param2.setEventTypeName("越界事件");

        EarlyWarnAreaConfParam param3 = new EarlyWarnAreaConfParam();
        param3.setDefenseAreaName("南门防区");
        param3.setAreaName("南门车闸");
        param3.setEventTypeName("过车事件");

        HashMap<String, Object> map = new HashMap<>();
        map.put("planTitle", "应急预案一");
        map.put("earlyWarnAreaConfParamList", Arrays.asList(param1, param2, param3));
        map.put("planDescribe", ThymeleafUtil.richTextToHtmlStr(""));
        return map;
    }
}
