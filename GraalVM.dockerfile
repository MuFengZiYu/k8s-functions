#FROM springci/graalvm-ce:stable-java11-0.10.x AS graalvm
# 对外端口
# 创建一个可以从本地主机或其他容器挂载的挂载点，一般用来存放数据库和需要保持的数据等
# VOLUME 指定了临时文件目录为/tmp。
# 其效果是在主机 /var/lib/docker 目录下创建了一个临时文件，并链接到容器的/tmp
#VOLUME /tmp
# 将jar包添加到容器中并更名为 testDemo.jar.我们在base(项目入口)的pom.xml中指定了打包后的jar名

#COPY target/k8sfunctions.jar k8sfunctions.jar
#COPY reflect.json   reflect.json
#RUN java -agentlib:native-image-agent=trace-output=trace.json -jar micronaut.jar  com.example.Application

#RUN native-image-configure generate --trace-input=trace.json --output-dir=/outputdir
#RUN  native-image   --no-server -cp k8sfunctions.jar org.wh.k8sfunctions.K8sFunctionsApplication

FROM  frolvlad/alpine-glibc:alpine-3.12
EXPOSE 8080
RUN apk add libstdc++
COPY  target/k8sfunctions k8sfunctions
#ENTRYPOINT ["./org.wh.k8sfunctions.K8sFunctionsApplication"]

