# spring-boot-demo

spring-boot-demo

spring boot 框架应用示例

## 与Docker整合

1. 在根目录下新建一个Dockerfile文件

	```
	# 基于哪个镜像
	FROM java:8
	# 将本地文件夹挂载到当前容器
	VOLUME /tmp
	# 拷贝文件到容器
	ADD target/spring-boot-demo-1.0-SNAPSHOT.jar app.jar
	# 打开服务端口
	EXPOSE 8080
	# 配置容器启动后执行的命令
	ENTRYPOINT ["java","-jar","/app.jar"]
	```

2. 在根目录下招待docker构建镜像命令

	```
	docker build -t spring-boot-demo .
	```

3. 执行`docker images`查看所有的镜像

	```
	docker images
	REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
	spring-boot-demo    latest              f08d6a99f38d        8 minutes ago       663 MB
	nginx               latest              3448f27c273f        2 weeks ago         109 MB
	java                8                   d23bdf5b1b1b        4 months ago        643 MB
	```

4. 运行容器

	```
	docker run -d -p 8080:8080 -t spring-boot-demo
	```
	
	> 其中-d表示后台运行容器，这也就自然地解决的Spring Boot不支持后台运行应用程序的问题。-p 8080:8080表示将容器内部的8080端口映射到宿主机器的8080端口，这样就可以通过宿主机器直接访问应用

4. 最后即可访问http://localhost:8080/user/index


## 使用maven创建Docker镜像

1. 在pom的build节点下添加插件

    ```xml
    <plugin>
        <groupId>com.spotify</groupId>
        <artifactId>docker-maven-plugin</artifactId>
        <version>0.4.13</version>
        <configuration>
            <!--imageName指定了镜像的名字-->
            <imageName>${project.artifactId}</imageName>
            <!--dockerDirectory指定Dockerfile的位置-->
            <dockerDirectory>.</dockerDirectory>
            <!--resources是指那些需要和Dockerfile放在一起，在构建镜像时使用的文件，一般应用jar包需要纳入-->
            <resources>
                <resource>
                    <targetPath>/</targetPath>
                    <directory>${project.build.directory}</directory>
                    <include>${project.build.finalName}.jar</include>
                </resource>
            </resources>
        </configuration>
    </plugin>
    ```
    
2. 执行`mvn package docker:build`命令构建
3. 执行`docker images`查看所有的镜像

	```
	docker images
	REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
	spring-boot-demo    latest              f08d6a99f38d        8 minutes ago       663 MB
	nginx               latest              3448f27c273f        2 weeks ago         109 MB
	java                8                   d23bdf5b1b1b        4 months ago        643 MB
	```

4. 运行容器

	```
	docker run -d -p 8080:8080 -t spring-boot-demo
	```


