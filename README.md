开发板：josh平台网关设备M680，基于javaee的类android系统  
开发语言：java  
通信协议：MQTT    
硬件开发资料参考官网：http://www.joshvm.com/

### 下载mqtt安装包，并运行:
download url: http://archive.apache.org/dist/activemq/activemq-apollo/1.7.1/   
download file: apache-apollo-1.7.1-windows-distro.zip   
```
apollo create watchman_broker
watchman_broker\bin\apollo-broker run
```
web url:http://127.0.0.1:61680/
login:admin/password

### 调试运行:
下载mqtt操作工具（windows）: http://www.jensd.de/apps/mqttfx/1.7.1/mqttfx-1.7.1-windows-x64.exe
运行之后连接上面搭建好的mqtt服务，就可以收发消息了   
将打包好的apk上传到开发板，并且运行：  
```
adb push JOSHDTU_LSQADB_V1/watchman.jar /vendor/data
adb shell cldc_vm -int -cp /vendor/data/watchman.jar com.joshvm.watchman.WatchMan
```

### 下发的指令:
868343045338998,0102040608
868343045338998,020A01
868343045338998,030102

### 上报的数据:
```
[data] gpio:868343045338998,04,10:0-1616287733800;11:0-1616287733800;14:0-1616287733800;15:0-1616287733800;16:0-1616287733800;17:0-1616287733800;20:0-1616287733800;23:0-161628773380
[data] model:868343045338998,05,0102
[data] uart:868343045338998,01,02:310
[data] gps:868343045338998,03,40.91242633,117.9687162
[data] uart:868343045338998,01,03:1428
[data] gps:868343045338998,03,40.91137981,117.96920418
[data] uart:868343045338998,01,02:309
[data] model:868343045338998,05,0102
[data] uart:868343045338998,01,03:1065
[data] gps:868343045338998,03,40.91138415,117.96920593
[data] gpio:868343045338998,04,10:0-1616287746143;11:0-1616287746143;14:0-1616287746143;15:0-1616287746143;16:0-1616287746143;17:0-1616287746143;20:0-1616287746143;23:0-1616287746143
[data] uart:868343045338998,01,02:0
[data] gps:868343045338998,03,40.91138277,117.96921048
[data] uart:868343045338998,01,03:1080
[data] model:868343045338998,05,0102
[data] gpio:868343045338998,04,10:0-1616287752356;11:0-1616287752356;14:0-1616287752356;15:0-1616287752356;16:0-1616287752356;17:0-1616287752356;20:0-1616287752356;23:0-1616287752356
[data] gps:868343045338998,03,40.91138283,117.9692074
[data] uart:868343045338998,01,03:1382
[data] gps:868343045338998,03,40.91138248,117.96920619
[data] model:868343045338998,05,0102
[data] gpio:868343045338998,04,10:0-1616287758535;11:0-1616287758535;14:0-1616287758535;15:0-1616287758535;16:0-1616287758535;17:0-1616287758535;20:0-1616287758535;23:0-1616287758535
[data] uart:868343045338998,01,02:0
[data] gps:868343045338998,03,40.91138294,117.96920611
[data] uart:868343045338998,01,03:1105
[data] gpio:868343045338998,04,10:0-1616287764710;11:0-1616287764710;14:0-1616287764710;15:0-1616287764710;16:0-1616287764710;17:0-1616287764710;20:0-1616287764710;23:0-1616287764710
[data] uart:868343045338998,01,02:306
[data] model:868343045338998,05,0102
```
