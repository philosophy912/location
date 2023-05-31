# 离线地图安装使用说明

### 代码结构说明

本项目后端由springboot，前端用vue进行开发， 数据库采用mysql承载。

### 需求软件

- [JDK](https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html)

![JDK安装软件](/images/jdk.jpg "JDK安装路径")

- nodejs [https://nodejs.org/en]
  
![NODEJS安装软件](/images/nodejs.jpg "NODEJS安装软件")

- mysql [https://downloads.mysql.com/archives/installer]
  
![mysql安装软件](/images/mysql.jpg "mysql安装软件")  

- maven [https://maven.apache.org/download.cgi]

![maven安装软件](/images/maven.jpg "maven安装软件")  


#### jdk安装说明

1. exe文件下载安装后， 进行环境变量配置

开始-运行 (win+R)， 输入命令sysdm.cpl打开【系统属性】， 选择【高级】标签，选择环境变量
![环境变量](/images/syssetting.jpg "环境变量")

![环境变量1](/images/syssetting1.jpg "环境变量1")

![环境变量3](/images/syssetting3.jpg "环境变量3")

这个地方如果按照默认路径安装，这个地方就是【**C:\Program Files\Java\jdk1.8.0_181\bin**】

2. 检查是否配置成功

开始-运行 (win+R)， 输入命令cmd

![检查JDK](/images/check_jdk.jpg "检查JDK")

#### maven安装说明

1. 下载代码后解压缩到任意目录（不要放到中文目录下面）

例如：放到D:\Tools\apache-maven下面，其中apache-maven就是apache-maven-3.9.2-bin.zip文件解压后的文件夹名字

2. 修改该文件夹下面的conf\settings.xml下面，使用resources下面的settings.xml替换原来的文件，并修改第55行，修改为你自己的文件夹（任意文件夹，不要带中文目录)

3. 增加环境变量设置

开始-运行 (win+R)， 输入命令sysdm.cpl打开【系统属性】， 选择【高级】标签，选择环境变量

![环境变量](/images/syssetting.jpg "环境变量")

![环境变量1](/images/syssetting1.jpg "环境变量1")

![环境变量2](/images/syssetting2.jpg "环境变量2")

这个地方如果按照之前的文件夹路径，完整的路径就是【**D:\Tools\apache-maven\bin**】

4. 检查是否配置成功

开始-运行 (win+R)， 输入命令cmd

![检查MAVEN](/images/check_maven.jpg "检查MAVEN")

#### node安装说明

1. exe文件下载安装后

2. 检查是否配置成功

开始-运行 (win+R)， 输入命令cmd

![检查NODE](/images/check_node.jpg "检查NODE")

### 编译软件方式

#### 下载代码

访问网站 [github](https://github.com/philosophy912/location)

#### 编译代码

1. 下载解压代码
   
2. 进入到frontend文件夹
   
开始-运行 (win+R)， 输入命令cmd

```shell

npm install
npm build
```
此时会发现多了一个dist文件夹

拷贝该文件夹下面的所有文件到【backend\src\main\resources\static】文件夹下面

3. 进入到backend文件夹

```shell
mvn clean && mvn package -Dmaven.test.skip=true
```

此时在backend文件夹下面有一个target文件夹，里面有一个文件叫做[map-1.0.0.jar]

4. 运行文件

- 首先需要运行BIGEMAP离线地图服务器

- 再次在命令行中运行

```shell
java -jar map-1.0.0.jar
```
![运行成功](/images/run_success.jpg "运行成功")

### 定制化修改

找到文件【frontend\src\App.vue】

#### 修改为离线地图

修改第25行
```vue
map = BM.map('map', 'bigemap.baidu-map', {crs: BM.CRS.Baidu, center: latlng, zoom: 14, zoomControl: true}); 
```
修改为离线地图的方式
```vue
map = BM.map('map', 'bigemap.dukvnuov', {center: latlng, zoom: 14, zoomControl: true}); 
```

#### 修改默认中心点

修改第26行
```vue
let latlng = BM.latLng(28.77064, 104.62922);
```

其中第一个是纬度、第二个是经度。 每种地图的经纬度信息可能**不一致**，需要特别注意


#### 修改访问端口
找到文件【backend\src\main\resources\application.yml】

#### 修改默认端口

找到第2行
```yml
server:
  port: 8080
```
可以修改端口为80(http网站访问默认端口号)

#### 修改数据库信息

找到第4行
```yml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/ybswj?setUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8
    username: root
    password: root
```

其中

127.0.0.1为数据库的IP地址，这里是本机

3306为数据库mysql的端口号，默认为3306

ybswj是数据库的名字

> 特别注意, 数据库需要手动安装，推荐数据库访问工具[HeidiSQL](https://www.heidisql.com/download.php). 

> 数据库的表是不用创建的，当数据库没有表格的时候会自动建立表格

如果要使用测试数据，请自行添加测试数据

表结构可以参考文件【backend\src\main\java\com\sophia\map】下面的两个java文件

