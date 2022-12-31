//统一管理项目接口的模块
//引入二次封装的axios（带有请求、响应的拦截器）
import * as webClass from './teacher/webClass';
import * as student from './student/student'
import * as classCourse from './teacher/classCourse'
import * as informations from './teacher/informations';
import * as praManagement from './practice/praManagement.js';

export default {
  webClass,
  student,
  classCourse,
  informations,
  praManagement
}