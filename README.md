# NCCSystem
## 辅导员素质评比大赛系统
###   - 演示视频 
- 用户接口
	- 登录接口: [doLogin](https://github.com/konoechoda/NCCSystem/blob/master/Demo%20video/doLogin.mp4)
	- 注销接口: [Logout](https://github.com/konoechoda/NCCSystem/blob/master/Demo%20video/Logout.mp4)
   	- 笔试评分接口: [writtenJudge](https://github.com/konoechoda/NCCSystem/raw/master/Demo%20video/writtenJudge.mp4)
	- 评分接口: [judge](https://github.com/konoechoda/NCCSystem/raw/master/Demo%20video/judge.mp4)
 - 第一轮
 	- 第一轮随机分组: [randomGroup](https://github.com/konoechoda/NCCSystem/raw/master/Demo%20video/randomGroup.mp4)
  	- 笔试成绩excel导入: [importScore](https://github.com/konoechoda/NCCSystem/raw/master/Demo%20video/importScore.mp4)
- 第二轮
	- 第二轮随机抽签: [secondRoundDraw](https://github.com/konoechoda/NCCSystem/raw/master/Demo%20video/secondRoundDraw.mp4)
 	- 谈心谈话签号顺序: [discussionRound](https://github.com/konoechoda/NCCSystem/raw/master/Demo%20video/discussionRound.mp4)
- 辅导员接口
	- 辅导员报名: [register](https://github.com/konoechoda/NCCSystem/raw/master/Demo%20video/register.mp4)
 	- 删除辅导员报名信息: [delete](https://github.com/konoechoda/NCCSystem/raw/master/Demo%20video/delete.mp4)
  	- 根据姓名查询辅导员报名信息: [queryByName](https://github.com/konoechoda/NCCSystem/raw/master/Demo%20video/queryByName.mp4)
  	- 根据身份证查询辅导员报名信息: [queryByIdCard](https://github.com/konoechoda/NCCSystem/raw/master/Demo%20video/queryByIdCard.mp4)
- 导出最终结果
	- 导出.pdf: [exportPdf](https://github.com/konoechoda/NCCSystem/raw/master/Demo%20video/exportPdf.mp4)

###   - 接口文档地址

采用Swagger生成的接口文档

*ps:Swagger确实是个好东西*
	- [接口文档地址](http://htmlpreview.github.io/?https://github.com/konoechoda/NCCSystem/blob/master/apiDoc/Swagger%20UI.html)
	- [接口文档地址(本地链接)](http://localhost:8082/swagger-ui.html#/)

###   - 项目介绍

#### 项目流程:

​		①  每个高校会有一个高校账号，这个账号只有一个功能：给本校的3位辅导员进行**报名**。

​		②  **第一轮抽签**,并进行**笔试**,主持人导入成绩

​		③  **第二轮抽签**(抽取笔试成绩的**前30名**,**两人一组**,每组人来自**不同**学校),进行**案例研讨**,研讨过后，由现场5位评委对2位老师***进行打分***。

​		④  进行**谈心谈话**,上一环节的选手一个一个按**顺序**上台和学生进行**情景演绎**,演完过后5位**评委打分**。

​		⑤  打分结束后，现场主持人需要立即吧成绩导出成pdf。

###   - 项目难点 不要脸的抄抄文档(

	- 抽签：如何设计第一轮抽签和第二轮抽签
	- 打分：如何设计第一次打分和第二次打分，保证线程安全
	- 解耦：代码尽可能的解耦

###   - 什么?? 你问我项目亮点?!! 我看看能不能凑一点出来

 - 抽签：
   - 第一轮抽签 -> 在获取所有人的报名信息后进行排序处理
   - 第二轮抽签 -> 在获取笔试前30名信息后分组再进行排序处理
 - 打分：打分接口设计有三个,分别用于笔试成绩导入和后两环节打分。
   - ​	excel成绩导入:支持使用excel表格一键式导入笔试成绩
   - ​    挨个导入笔试成绩:同时支持一个一个的导入笔试成绩
   - ​    案例研讨和谈心谈话成绩调用同一个接口,因为可能存在多名评委同时打分的情况.为保证线程安全,该接口采用了消息队列(RabbitMQ)进行处理
 - 结果支持导出为.pdf和.xlsx(默认为.pdf)两种格式,并在服务器本地存储导出结果,防止出意外(三重保障,值得信赖)

