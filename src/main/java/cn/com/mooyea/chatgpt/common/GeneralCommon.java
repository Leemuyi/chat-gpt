package cn.com.mooyea.chatgpt.common;

import cn.com.mooyea.chatgpt.utils.ProjectPathUtil;
import cn.com.mooyea.chatgpt.utils.RedisTemplateUtil;
import cn.com.mooyea.chatgpt.utils.YamlUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <h1>GeneralCommon<h1>
 * <p>Copyright (C), 星期二,21,2月,2023</p>
 * <br/>
 * <hr>
 * <h3>File Info:</h3>
 * <p>FileName: GeneralCommon</p>
 * <p>Author:   mooye</p>
 * <p>Work_Email： lidy@skyvis.com.cn</p>
 * <p>E-mail： mooyeali@yeah.net</p>
 * <p>Date:     2023/2/21</p>
 * <p>Description: 一些通用的操作</p>
 * <hr>
 * <h3>History:</h3>
 * <hr>
 * <table>
 *  <thead>
 *  <tr><td style='width:100px;' center>Author</td><td style='width:200px;' center>Time</td><td style='width:100px;' center>Version_Number</td><td style='width:100px;' center>Description</td></tr>
 *  </thead>
 *  <tbody>
 *    <tr><td style='width:100px;' center>mooye</td><td style='width:200px;' center>10:54 2023/2/21</td><td style='width:100px;' center>v_1.0.0</td><td style='width:100px;' center>创建</td></tr>
 *  </tbody>
 * </table>
 * <hr>
 * <br/>
 *
 * @author mooye
 */

@Component
@Slf4j
public class GeneralCommon {
	@Resource
	RedisTemplateUtil redisTemplateUtil;
	private static final String CONFIG = ProjectPathUtil.getProjectPath()+"/"+SystemConstant.CONFIG_FILE_NAME;


	@SneakyThrows(value = FileNotFoundException.class)
	public String setBaiduToken(String ak, String sk){
		double now = System.currentTimeMillis()/1000.0;
		String https = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id="+ak+"&client_secret="+sk;
		String res = HttpUtil.get(https);
		log.info(res);
		if (!res.contains(SystemConstant.HTTP_ERROR_DESCRIPTION)){
			JSONObject json = JSONUtil.parseObj(res);
			String token = json.getStr(SystemConstant.ACCESS_TOKEN);
			int expiresIn = json.getInt(SystemConstant.EXPIRES_IN);
			if (new File(CONFIG).exists()){
				Map<String,Object> map = YamlUtil.getConfig(new FileInputStream(CONFIG));
				map.put(SystemConstant.ACCESS_TOKEN,token);
				map.put(SystemConstant.EXPIRES_IN,(expiresIn+ now)*1000);
				YamlUtil.updateConfig(map,CONFIG);
			}else {
				redisTemplateUtil.set(SystemConstant.ACCESS_TOKEN,token,0,30*24*3600);
			}
			return token;
		}else {
			return SystemConstant.SYSTEM_FUN_RETURN;
		}
	}

	@SneakyThrows(value = FileNotFoundException.class)
	public String getBaiduToken(String ak,String sk){
		String token;
		if (new File(CONFIG).exists()){
			Map<String,Object> map = YamlUtil.getConfig(new FileInputStream(CONFIG));
			token = (String) map.get(SystemConstant.ACCESS_TOKEN);
			if (token == null){
				setBaiduToken(ak, sk);
				getBaiduToken(ak, sk);
			}
			long time = (long) map.get(SystemConstant.EXPIRES_IN);
			if (System.currentTimeMillis()<time){
				return token;
			}
		}else {
			token = (String) redisTemplateUtil.get(SystemConstant.ACCESS_TOKEN,0);
			if (token != null){
				return token;
			}
		}
		return setBaiduToken(ak,sk);
	}
	//curl -XPOST "https://wenxin.baidu.com/moduleApi/portal/api/oauth/token?grant_type=client_credentials&client_id={your_ak}&client_secret={your_sk}" -H "Content-Type:application/x-www-form-urlencoded"

	@SneakyThrows(value = FileNotFoundException.class)
	public String setVilgToken(String ak, String sk){
		double now = System.currentTimeMillis()/1000.0;
		String https = "https://wenxin.baidu.com/moduleApi/portal/api/oauth/token?grant_type=client_credentials&client_id="+ak+"&client_secret="+sk;
		String res = HttpRequest.post(https).header(SystemConstant.HTTP_CONTENT_TYPE,SystemConstant.APPLICATION_URLENCODED).execute().body();
		log.info(res);
		if (res.contains(SystemConstant.HTTP_SUCCESS_FLAG)){
			JSONObject json = JSONUtil.parseObj(res);
			String token = json.getStr(SystemConstant.HTTP_RESPONSE_DATA);
			long expiresIn = System.currentTimeMillis()+24*60*60*1000;
			if (new File(CONFIG).exists()){
				Map<String,Object> map = YamlUtil.getConfig(new FileInputStream(CONFIG));
				map.put(SystemConstant.VILG_ACCESS_TOKEN,token);
				map.put(SystemConstant.VILG_EXPIRES_IN,expiresIn);
				YamlUtil.updateConfig(map,CONFIG);
			}else {
				redisTemplateUtil.set(SystemConstant.VILG_ACCESS_TOKEN,token,0,24*3600);
			}
			return token;
		}else {
			return SystemConstant.SYSTEM_FUN_RETURN;
		}
	}

	@SneakyThrows(value = FileNotFoundException.class)
	public String getVilgToken(String ak,String sk){
		String token;
		if (new File(CONFIG).exists()){
			Map<String,Object> map = YamlUtil.getConfig(new FileInputStream(CONFIG));
			token = (String) map.get(SystemConstant.VILG_ACCESS_TOKEN);
			if (token == null){
				setVilgToken(ak, sk);
				getVilgToken(ak, sk);
			}
			long time = (long) map.get(SystemConstant.VILG_EXPIRES_IN);
			if (System.currentTimeMillis()<time){
				return token;
			}
		}else {
			token = (String) redisTemplateUtil.get(SystemConstant.VILG_ACCESS_TOKEN,0);
			if (token != null){
				return token;
			}
		}
		return setVilgToken(ak,sk);
	}

	public String readConfig(String key){
		File configFile = new File(CONFIG);
		if (configFile.exists()){
			try {
				Map<String,Object> yaml = YamlUtil.getConfig(new FileInputStream(configFile));
				return (String) yaml.get(key);
			} catch (FileNotFoundException e) {
				return (String) redisTemplateUtil.get(key,0);
			}
		}else {
			return (String) redisTemplateUtil.get(key,0);
		}
	}

	public int textVerification(String token,String text){
		String url = "https://aip.baidubce.com/rest/2.0/solution/v1/text_censor/v2/user_defined?access_token="+token;
		String param = "text=" + text;
		String res = HttpUtil.post(url,param);
		log.info(res);
		if (!res.contains(SystemConstant.HTTP_ERROR_CODE)){
			JSONObject json = JSONUtil.parseObj(res);
			return json.getInt(SystemConstant.HTTP_CONCLUSION_TYPE);
		}else {
			return 5;
		}

	}

//	curl -i -k "https://wenxin.baidu.com/moduleApi/portal/api/rest/1.0/ernievilg/v1/txt2img?access_token=${access_token}" --data-urlencode "text=睡莲" --data-urlencode "style=油画" --data-urlencode "resolution=1024*1024" --data-urlencode "num=4" #分辨率和数量非必须参数，默认resolution=1024*1024、number=4

	public String requestPaintingInterface(String ak,String sk,String description){
		String url = "https://wenxin.baidu.com/moduleApi/portal/api/rest/1.0/ernievilg/v1/txt2img";
		// 获取 token
		String token = getVilgToken(ak, sk);
		Map<String,Object> paramMap = new HashMap<>(8){{
			put("access_token",token);
			put("text",description);
			put("style","二次元");
			put("resolution","1536*1024");
			put("num",1);
		}};
		String result = HttpRequest.post(url)
				.header(SystemConstant.HTTP_CONTENT_TYPE, SystemConstant.APPLICATION_URLENCODED)
				.form(paramMap)
				.timeout(60000)
				.execute().body();
		if (result != null){
			JSONObject json = JSONUtil.parseObj(result);
			if (Objects.equals(json.get(SystemConstant.MSG),SystemConstant.HTTP_SUCCESS_FLAG) && json.getInt(SystemConstant.HTTP_CODE)==0){
				JSONObject data = JSONUtil.parseObj(json.get(SystemConstant.HTTP_RESPONSE_DATA));
				return data.getStr(SystemConstant.TASK_ID);
			}
			return null;
		}
		return null;
	}

//	curl -i -k "https://wenxin.baidu.com/moduleApi/portal/api/rest/1.0/ernievilg/v1/getImg?access_token={access_token}" --data-urlencode "taskId={task_id}"
	@SneakyThrows(value = InterruptedException.class)
	public String queryTask(String ak, String sk, String taskId) {
		String url = "https://wenxin.baidu.com/moduleApi/portal/api/rest/1.0/ernievilg/v1/getImg";
		String token = getVilgToken(ak,sk);
		Map<String,Object> paramMap = new HashMap<>(8){{
			put("access_token",token);
			put("taskId",taskId);
		}};
		String result = HttpRequest.post(url).form(paramMap)
				.timeout(20000)
				.execute().body();
		log.info("查询任务结果:{}",result);
		if (result != null){
			JSONObject json = JSONUtil.parseObj(result);
			if (json.getInt(SystemConstant.HTTP_CODE)==0){
				JSONObject data = JSONUtil.parseObj(json.getStr(SystemConstant.HTTP_RESPONSE_DATA));
				if (data.getInt("status") == 1){
					return data.getStr("img");
				}else {
					Thread.sleep(30*1000L);
					return queryTask(ak, sk, taskId);
				}
			}
			return null;
		}
		return null;
	}

	public String getResponseText(String text){
		String responseText;
		String url = "https://v1.apigpt.cn/?q="+text+"&apitype=sql";
		String res = HttpUtil.get(url);
		log.info("请求结果:{}",res);
		if (res != null && res.contains(SystemConstant.HTML_TITLE)){
			return text+"\nApi服务异常,请稍后再试!";
		}
		JSONObject json = JSONUtil.parseObj(res);
		if (Objects.equals(json.getInt(SystemConstant.HTTP_CODE), HttpStatus.HTTP_OK)){
			if (json.containsValue(SystemConstant.HTTP_RESPONSE_MSG)){
				return SystemConstant.SYSTEM_FUN_RETURN;
			}
			responseText = json.getStr(SystemConstant.HTTP_RESPONSE_BODY)==null?"网络异常,请稍后再试!":json.getStr(SystemConstant.HTTP_RESPONSE_BODY);
		}else {
			responseText = "网络异常,请稍后再试!";
		}
		return json.getStr(SystemConstant.HTTP_REQUEST_PARAM_QUESTIONS)+responseText;
	}

//	public static void main(String[] args) {
//		log.info(new GeneralCommon().getResponseText("你好骚啊?"));
//	}
}