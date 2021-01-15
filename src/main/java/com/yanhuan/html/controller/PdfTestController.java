package com.yanhuan.html.controller;

import cn.hutool.core.util.StrUtil;
import com.yanhuan.html.util.PdfUtil;
import com.yanhuan.html.util.ThymeleafUtil;
import com.yanhuan.html.util.WordUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
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
        Map<String, Object> map = new HashMap<>();
        map.put("planTitle", result.getPlanTitle());
        map.put("earlyWarnAreaConfParamList", result.getEarlyWarnAreaConfParamList());
        String substring = result.getPlanDescribe().substring(1, result.getPlanDescribe().length() - 1);
        String s = StringEscapeUtils.unescapeJava(substring);
        map.put("planDescribe", s);
        log.error("描述：{}", substring);
        log.error("描述去掉转移：{}", s);
        try {
            WordUtil.exportDoc(response, result.getPlanTitle(), "earlyWarningPlan", map);
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
        for (EarlyWarnPlanResult result : pageInfo.getList()) {
            Map<String, Object> variable = new HashMap<>();
            variable.put("planTitle", result.getPlanTitle());
            variable.put("earlyWarnAreaConfParamList", result.getEarlyWarnAreaConfParamList());
            if (StrUtil.isNotBlank(result.getPlanDescribe())) {
                variable.put("planDescribe", ThymeleafUtil.richTextToHtmlStr(result.getPlanDescribe()));
            }
            map.put(result.getPlanTitle(), variable);
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
        for (EarlyWarnPlanResult result : pageInfo.getList()) {
            Map<String, Object> variable = new HashMap<>();
            variable.put("planTitle", result.getPlanTitle());
            variable.put("earlyWarnAreaConfParamList", result.getEarlyWarnAreaConfParamList());
            if (StrUtil.isNotBlank(result.getPlanDescribe())) {
                variable.put("planDescribe", ThymeleafUtil.richTextToHtmlStr(result.getPlanDescribe()));
            }
            map.put(result.getPlanTitle(), variable);
        }
        try {
            WordUtil.exportDocBatch(response, "应急预案", "earlyWarningPlan", map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 构建单挑测试数据
     *
     * @return
     */
    private Map<String, Object> buildSingleTestData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("planTitle", "应急预案一");
        map.put("earlyWarnAreaConfParamList", result.getEarlyWarnAreaConfParamList());
        map.put("planDescribe",ThymeleafUtil.richTextToHtmlStr(result.getPlanDescribe()));
        return map;
    }
}
