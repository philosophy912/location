# 使用方法


### 安装方式
参考https://www.bilibili.com/video/BV1h5411w7oY/ 安装python环境

修改 data_import.py的以下部分数据[38行-40行]
```python
import_file = "临港局单位纳税人.xls"
server_ip = "127.0.0.1"
server_port = "8080"
```
其中```import_file```表示在当前文件夹下面的文件名字，就是每日导出的数据，请注意不要修改表头，否则可能导致无法导入

```server_ip```表示后台服务器的ip地址，如果是本地开发则不用修改

```server_port```表示后台服务器的端口好， 如果是本地开发则不用修改

### 执行方式

命令行输入```python data_import.py```则开始执行 【第一次执行的时候需要有网络环境】

特别注意，当文档填写有错误的时候，命令行会提示错误，请特别注意
