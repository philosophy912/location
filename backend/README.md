# ybswj

使用说明

- 打开[nodejs官方网站](https://nodejs.org/en)下载并安装

- 通过[github网站](https://github.com/philosophy912/location)下载源代码

选择**Code**并选择Download ZIP下载

运行说明

- 首先运行**BIGEMAP离线地图服务器**服务

- 解压缩zip包后，进入到backend文件夹

- 进入命令行模式执行如下命令

```shell
npm install

npm run serve

```

- 执行完成后打开网站 [ http://192.168.112.207:8080] 可以访问查看效果

特别说明：

目前采用了在线地图进行开发（百度地图）

如果需要设定经纬度，请自行访问[百度地图开放平台拾取坐标系统](http://api.map.baidu.com/lbsapi/getpoint/index.html)获取经纬度信息

目前显示的地图中心点为宜宾市大观楼， 如果需要修改请在[src/app.vue]中修改第16行的地址
```javascript
 let latlng = BM.latLng(28.77064, 104.62922);
```

当前的mark点数据来源于[public/locations.json]文件夹
