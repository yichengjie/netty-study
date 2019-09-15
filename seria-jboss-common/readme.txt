1.注意：使用jboss序列化对象，必须实现implements Serializable接口，否则客户与服务端无任何反应
2.总结：尽量的将encode放在encoder [outBound]写在encoder前面