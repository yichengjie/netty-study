1.下载protobuf，解压后将protoc.exe复制到C:\Windows\System32文件夹中
2.编写对应的protobuf文件
3.执行命令生成java文件:
	`protoc ./SubscribeReq.proto --java_out=../src/main/java`	