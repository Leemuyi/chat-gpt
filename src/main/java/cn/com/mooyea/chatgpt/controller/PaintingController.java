package cn.com.mooyea.chatgpt.controller;

import cn.com.mooyea.chatgpt.common.GeneralCommon;
import cn.com.mooyea.chatgpt.common.SystemConstant;
import cn.com.mooyea.chatgpt.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <h1>PaintingController<h1>
 * <p>Copyright (C), 星期四,23,2月,2023</p>
 * <br/>
 * <hr>
 * <h3>File Info:</h3>
 * <p>FileName: PaintingController</p>
 * <p>Author:   mooye</p>
 * <p>Work_Email： lidy@skyvis.com.cn</p>
 * <p>E-mail： mooyeali@yeah.net</p>
 * <p>Date:     2023/2/23</p>
 * <p>Description: 绘画</p>
 * <hr>
 * <h3>History:</h3>
 * <hr>
 * <table>
 *  <thead>
 *  <tr><td style='width:100px;' center>Author</td><td style='width:200px;' center>Time</td><td style='width:100px;' center>Version_Number</td><td style='width:100px;' center>Description</td></tr>
 *  </thead>
 *  <tbody>
 *    <tr><td style='width:100px;' center>mooye</td><td style='width:200px;' center>11:38 2023/2/23</td><td style='width:100px;' center>v_0.0.1</td><td style='width:100px;' center>创建</td></tr>
 *  </tbody>
 * </table>
 * <hr>
 * <br/>
 *
 * @author mooye
 */

@RestController
@RequestMapping("/painting")
@Slf4j
public class PaintingController {

	@Resource
	GeneralCommon generalCommon;
	@GetMapping("/painting")
	public Result<?> painting(String des){
		String ak = generalCommon.readConfig(SystemConstant.CONFIG_VILG_AK);
		String sk = generalCommon.readConfig(SystemConstant.CONFIG_VILG_SK);
		String taskId = generalCommon.requestPaintingInterface(ak,sk,des);
		if (taskId != null){
			String imgUrl = generalCommon.queryTask(ak,sk,taskId);
			if (imgUrl != null){
				return Result.OK("绘制成功!",imgUrl);
			}
			return Result.error("服务异常!");
		}
		return Result.error("服务异常!");
	}
}
