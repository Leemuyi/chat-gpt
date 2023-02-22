package cn.com.mooyea.chatgpt.controller;

import cn.com.mooyea.chatgpt.common.GeneralCommon;
import cn.com.mooyea.chatgpt.utils.ProjectPathUtil;
import cn.com.mooyea.chatgpt.utils.RedisTemplateUtil;
import cn.com.mooyea.chatgpt.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * <h1>ChatGptController<h1>
 * <p>Copyright (C), 星期二,21,2月,2023</p>
 * <br/>
 * <hr>
 * <h3>File Info:</h3>
 * <p>FileName: ChatGptController</p>
 * <p>Author:   mooye</p>
 * <p>Work_Email： lidy@skyvis.com.cn</p>
 * <p>E-mail： mooyeali@yeah.net</p>
 * <p>Date:     2023/2/21</p>
 * <p>Description: ChatGPT 业务请求类</p>
 * <hr>
 * <h3>History:</h3>
 * <hr>
 * <table>
 *  <thead>
 *  <tr><td style='width:100px;' center>Author</td><td style='width:200px;' center>Time</td><td style='width:100px;' center>Version_Number</td><td style='width:100px;' center>Description</td></tr>
 *  </thead>
 *  <tbody>
 *    <tr><td style='width:100px;' center>mooye</td><td style='width:200px;' center>10:15 2023/2/21</td><td style='width:100px;' center>v_1.0.0</td><td style='width:100px;' center>创建</td></tr>
 *  </tbody>
 * </table>
 * <hr>
 * <br/>
 *
 * @author mooye
 */

@RestController
@Slf4j
@RequestMapping("/chat")
public class ChatGptController {

	private static final String CONFIG = ProjectPathUtil.getProjectPath()+"/config.yaml";
	@Resource
	RedisTemplateUtil redisTemplateUtil;
	@Resource
	GeneralCommon generalCommon;

	@GetMapping("/gpt")
	public Result<?> chatGpt(String text){
		String authenticator = generalCommon.readConfig("chat_gpt_auth");
		String ak = generalCommon.readConfig("ak");
		String sk = generalCommon.readConfig("sk");
		String baiduToken = generalCommon.readConfig("token");
		// 调用百度文本校验接口校验
		String token = generalCommon.getBaiduToken(ak,sk);
		int checkFlag = generalCommon.textVerification(token,text);
		String warning = "";
		switch (checkFlag){
			case 2:
			case 4:
			case 5:
				return Result.error("抱歉，我无法回答这个问题（违规,已记录）！！");
			case 3:
				warning = "请注意言行(提问方式疑似违规)!";
				break;
			default:
				warning = "";
		}
		// 调用 ChatGPT 接口生成文本返回
		String res = generalCommon.getResponseText(text);
		if (Objects.equals(res,"0")){
			return Result.error("抱歉，我无法回答这个问题（违规,已记录）！！");
		}
		if (!Objects.equals("",warning)){
			res = warning+"\n"+res;
		}
		return Result.OK("成功!",res);
	}


}
