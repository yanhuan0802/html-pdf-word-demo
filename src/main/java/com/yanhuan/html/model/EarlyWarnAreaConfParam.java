package com.yanhuan.html.model;

import lombok.Data;


/**
 * 预警方案区域配置
 *
 * @author Yan
 */
@Data
public class EarlyWarnAreaConfParam {

    private String eventTypeName;

    private String areaName;

    private String defenseAreaName;
}
