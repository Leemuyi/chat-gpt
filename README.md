# ChatGPT 服务提供

![License](https://img.shields.io/badge/license-MIT-yellow)
![Java](https://img.shields.io/badge/Java-OpenJdk11+-<yellow>)
![Maven](https://img.shields.io/badge/Maven-3.x-<yellow>)

![Language](https://img.shields.io/badge/Language-Java-<yellow>)
![Language](https://img.shields.io/badge/Language-HTML-<yellow>)
![Language](https://img.shields.io/badge/Language-JavaScript-<yellow>)

[![项目地址](https://img.shields.io/badge/项目地址-GitHub-<yellow>)](https://github.com/Leemuyi/chat-gpt)
## 项目简介
本项目利用百度文本校验 Api 实现敏感词、违禁词校验.使用国内开发者提供的 ChatGPT 接口服务实现文本内容的生成。使用 ERNIE-ViLG AI 作画大模型实现绘图。

## 部分 Api 说明
### 调用百度鉴权接口获取 token,接口为:
```shell
  curl -i -k 'https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=【百度云应用的AK】&client_secret=【百度云应用的SK】'
 ```
将获取到的 token 进行存储,每 30 天自动更新一次
详细接口介绍参阅[官方文档](https://ai.baidu.com/ai-doc/REFERENCE/Ck3dwjhhu)
### 百度文本校验 Api
```shell
curl -i -k 'https://aip.baidubce.com/rest/2.0/solution/v1/text_censor/v2/user_defined?access_token=【调用鉴权接口获取的token】' --data 'text=不要侮辱伟大的乐侃' -H 'Content-Type:application/x-www-form-urlencoded'
```
详细说明参阅百度[官方文档](https://cloud.baidu.com/doc/ANTIPORN/s/Vk3h6xaga)
### ERNIE-ViLG AI 作画大模型

**注意,该服务需要付费,但官方提供了 200pic的免费调用量,后续加个为 9.9RMB/50pic**

#### 获取 token
```shell
curl -XPOST "https://wenxin.baidu.com/moduleApi/portal/api/oauth/token?grant_type=client_credentials&client_id={your_ak}&client_secret={your_sk}" -H "Content-Type:application/x-www-form-urlencoded"
```
该token 有效期为 24 小时
#### 创建预测任务
```shell
curl -i -k "https://wenxin.baidu.com/moduleApi/portal/api/rest/1.0/ernievilg/v1/txt2img?access_token=${access_token}" --data-urlencode "text=睡莲" --data-urlencode "style=油画" --data-urlencode "resolution=1024*1024" --data-urlencode "num=4" #分辨率和数量非必须参数，默认resolution=1024*1024、number=4
```
#### 查询任务
```shell
curl -i -k "https://wenxin.baidu.com/moduleApi/portal/api/rest/1.0/ernievilg/v1/getImg?access_token={access_token}" --data-urlencode "taskId={task_id}"
```
注意,查询任务接口需要轮询,ERNIE-ViLG AI绘制图片时长不稳定

详细说明参阅[文心大模型文档](https://wenxin.baidu.com/wenxin/docs#Pl6llwf92)
## 使用
- 如果你只是想使用这个项目
    1. 首先部署 redis 并修改 redis 的配置文件将密码设置成 chatGPTRedisPwd;
    2. 使用`java -jar chat-gpt-0.0.1.jar`启动项目即可,启动后访问 http://localhost:11023/system/init 进行配置相关参数,由于ChatGPT 接口不再使用 OpenAI 的接口提供服务,因此页面上的`Authorization`字段可以随意填写;
    3. 调用`http://localhost:11023/chat/gpt?text=参数` 即可获取生成的文本;
    4. 调用`http://localhost:11023/painting/painting?des=参数` 即可获取生成的图片;
- 如果你想做一些更改请关注`cn.com.mooyea.chatgpt.controller.ChatGptController.java`、`cn.com.mooyea.chatgpt.common.GeneralCommon.java`、`cn.com.mooyea.chatgpt.common.PaintingController.java`等。

## 鸣谢
感谢夏柔公益 Api 提供的 ChatGPT 接口服务。

感谢百度智云提供的敏感词校验 Api 试用服务.