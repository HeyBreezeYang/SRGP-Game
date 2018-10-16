需要在windows下测试使用, 便捷做法为将对应windows版本jzmq.dll文件拷贝至 ${JAVA_HOME}\bin目录下(目前只编译了win10版本的dll)
linux下在启动时使用参数-Djava.library.path=jzmq安装目录 指定类库地址后方可使用, 内网50测试机的jzmq安装目录为/root/jzmq3.1.0/lib
备注:JZMQ原版的零拷贝模式有bug, 附带的dll中已经修正该bug, 但测试后性能不理想, 不建议使用