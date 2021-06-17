package org.ywb.ono.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yuwenbo
 * @date 2021/6/11 上午9:50
 * @since 1.0.0
 */
@Data
@ApiModel("分页参数")
public class Pageable {

    @ApiModelProperty("第几页，默认为1")
    private Integer page = 1;

    @ApiModelProperty("每页显示的总条数，默认10")
    private Integer pageSize = 10;

    @ApiModelProperty("是否统计总数，0不统计，1统计，默认统计")
    private Integer isSearchCount = 1;
}
