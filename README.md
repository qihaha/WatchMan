step 1:
download url: http://archive.apache.org/dist/activemq/activemq-apollo/1.7.1/
download file: apache-apollo-1.7.1-windows-distro.zip 

run cmd :
apollo create watchman_broker
watchman_broker\bin\apollo-broker run

web url:http://127.0.0.1:61680/
login:admin/password

test utils:
download url: http://www.jensd.de/apps/mqttfx/1.7.1/mqttfx-1.7.1-windows-x64.exe

adb push JOSHDTU_LSQADB_V1/watchman.jar /vendor/data
adb shell cldc_vm -int -cp /vendor/data/watchman.jar com.joshvm.watchman.WatchMan

action:
868343045338998,0102040608
868343045338998,020A01
868343045338998,030102

data: