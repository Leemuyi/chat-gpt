# ChatGPT 服务提供
本项目利用百度文本校验 Api 实现敏感词、违禁词校验.使用国内开发者提供的 ChatGPT 接口服务实现文本内容的生成.

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
## 使用
- 如果你只是想使用这个项目
    1. 首先部署 redis 并修改 redis 的配置文件将密码设置成 chatGPTRedisPwd;
    2. 使用`java -jar chat-gpt-0.0.1.jar`启动项目即可,启动后访问 http://localhost:11023/system/init 进行配置相关参数,由于ChatGPT 接口不再使用 OpenAI 的接口提供服务,因此页面上的`Authorization`字段可以随意填写;
    3. 调用`http://localhost:11023/chat/gpt?text=参数` 即可获取生成的文本;
- 如果你想做一些更改请关注`cn.com.mooyea.chatgpt.controller.ChatGptController.java`以及`cn.com.mooyea.chatgpt.common.GeneralCommon.java`两个类即可。

## 鸣谢
感谢夏柔公益 Api 提供的 ChatGPT 接口服务。

感谢百度智云提供的敏感词校验 Api 试用服务.