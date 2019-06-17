# project: Android_0330
# module: CheckInSystem
企业签到系统（安卓端）

## 本项目学习至慕课网课程，相关课程地址<br>
[Android基础(静态页面)](https://class.imooc.com/sc/6) <br>
[http请求相关](https://www.imooc.com/video/18688) <br>  

## 已实现： <br>
  1.集成百度地图SDK、api实现动态获取定位 <br>
  2.封装Okhttp请求自建的后端程序完成用户的签到 <br>
  3.请假申请、出差申请的界面实现              
                      
## 细节: <br>
> 登录实现： <br>
>> * 使用全局变量isLogin与登录后的workerId，未登录workerId == -1;（userId仅用作用户管理）<br>
>> * 签到时未登录时则提示登录，登录访问自建api,成功则返回对应的workerId。校验的逻辑放入后端 <br>
>> * 登录成功后可以退出登录<br

> 签到实现：<br>
>> * 请求自建api获取动态的公司坐标，签到范围（半径）<br>
>> * 请求百度云api，获取当前坐标与时间 <br>
>> * 本地处理签到是否有效，有效则请求自建api完成打卡 <br>
>> * 若请请求有错误，前端提示 <br>
