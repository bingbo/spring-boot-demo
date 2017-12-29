# spring-boot-demo

spring-boot-demo

## spring boot 框架应用示例

### 添加boot依赖

```xml
<dependencies>
    <!--web应用基本环境配置 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!--监控-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>

    <!--为应用提供一些开发时特性，包括默认值设置，自动重启，livereload等，没有时会报错。
    最重要的功能就是自动应用代码更改到最新的App上面去，其深层原理是使用了两个ClassLoader，一个Classloader加载那些不会改变的类（第三方Jar包），
    另一个ClassLoader加载会更改的类，称为  restart ClassLoader，这样在有代码更改的时候，
    原来的restart ClassLoader 被丢弃，重新创建一个restart ClassLoader，由于需要加载的类相比较少，所以实现了较快的重启时间（5秒以内）-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <optional>true</optional>
    </dependency>

    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>6.0.6</version>
    </dependency>

    <!--添加mybatis依赖-->
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>1.3.0</version>
    </dependency>
</dependencies>
```

### 通过注解及main函数启动

```java
@SpringBootApplication
@EnableAutoConfiguration
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        //        SpringApplication.run(Application.class, "--server.port=8081");

    }
}
```

> @SpringBootApplication相当于@Configuration、@EnableAutoConfiguration和  @ComponentScan，你也可以同时使用这3个注解。其中@Configuration、@ComponentScan是spring框架的语法，在spring 3.x就有了，用于代码方式创建配置信息和扫描包。@EnableAutoConfiguration是spring boot语法，表示将使用自动配置。你如果下载了spring boot源码，就会看到spring boot实现了很多starter应用，这些starter就是一些配置信息（有点类似于docker，一组环境一种应用的概念），spring boot看到引入的starter包，就可以计算如果自动配置你的应用

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


## Docker中应用与mysql通信

### 添加并运行mysql镜像

```
docker run --name demo-mysql -e MYSQL_ROOT_PASSWORD=123456 -e MYSQL_DATABASE=test -p 3306:3306 -d mysql:5.6
```

可以通过mysql客户端登录进行操作，如`mysql -uroot -h172.20.146.12 -p123456 -Dtest -P3306`

### 通过连接--link形式与mysql通信

```
docker run -p 8080:8080 --name demo-app --link demo-mysql:mysql -d spring-boot-demo
```

> 注意mysql url连接方式：`spring.datasource.url=jdbc:mysql://mysql:3306/test`

## 通过Docker-compose进行整合集成多个docker镜像

### 在根目录下新建docker-compose.yml文件

```yaml
web:
  build: .
  ports:
    - "8080:8080"
  links:
    - mysql

mysql:
  image: mysql:5.6
  environment:
    MYSQL_ROOT_PASSWORD: 123456
    MYSQL_DATABASE: test
  ports:
    - "3306:3306"
  volumes:
    # 挂载 Mysql 数据目录，持久化数据库。
      - ./data/mysql:/var/lib/mysql
    #  - ./config/mysql:/etc/mysql/conf.d
  restart: always
```

### 编译构建web

```
docker-compose build
```

### 启动web服务运行

```bash
docker-compose up
```

> 可通过`docker ps`查看启动的服务


_Docker参考_:https://yeasy.gitbooks.io/docker_practice/content/

## maven-assembly-plugin构建插件的应用

```xml
<assembly xmlns="http://maven.apache.org/ASSEMBLY/2.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/ASSEMBLY/2.0.0 http://maven.apache.org/xsd/assembly-2.0.0.xsd">
    <!--一个装配集合ID，会拼在${project.build.finalName}后面做为包的全名-->
    <id>appServerA</id>
    <!--装配打包格式-->
    <formats>
        <format>tar.gz</format>
        <format>dir</format>
    </formats>
    <!--指定要包含在程序集中的共享组件xml文件位置,当找到多个componentDescriptors时，它们的内容被合并-->
    <componentDescriptors>
        <componentDescriptor>src/main/assembly/assembly_component.xml</componentDescriptor>
    </componentDescriptors>
    <!--是否包含根目录，默认为true -->
    <includeBaseDirectory>true</includeBaseDirectory>
    <!--设置生成的程序集归档的基本目录，默认拿${project.build.finalName}代替-->
    <baseDirectory>/</baseDirectory>

    <!--指定包含哪些文件组-->
    <fileSets>
        <fileSet>
            <!--设置模块目录的绝对或相对位置-->
            <directory>src/main/assembly/bin</directory>
            <!--设置输出目录相对于程序集根目录的根目录-->
            <outputDirectory>bin</outputDirectory>
            <!--与UNIX权限类似，设置所包含文件的文件模式-->
            <fileMode>0755</fileMode>
        </fileSet>
        <fileSet>
            <directory>src/main/resources</directory>
            <outputDirectory>conf</outputDirectory>
            <fileMode>0644</fileMode>
        </fileSet>
        <fileSet>
            <directory>target/extra-conf</directory>
            <outputDirectory>conf/META-INF/spring</outputDirectory>
            <!--定义一组要包含的文件和目录-->
            <includes>
                <include>ctx-*.xml</include>
            </includes>
            <fileMode>0644</fileMode>
        </fileSet>
    </fileSets>
    <!--指定在程序集中包含哪些单个文件-->
    <files>

        <!--指定过滤相应的文件-->
        <file>
            <source>README.md</source>
            <outputDirectory>/</outputDirectory>
            <!--是否被过滤掉-->
            <filtered>true</filtered>
        </file>
        <file>
            <!--原文件-->
            <source>Dockerfile</source>
            <!--输出文件名-->
            <destName>Dockerfile</destName>
            <!--输出目录-->
            <outputDirectory>/</outputDirectory>
            <!--设置输出后的文件权限-->
            <fileMode>0644</fileMode>
        </file>
        <file>
            <source>docker-compose.yml</source>
            <destName>docker-compose.yml</destName>
            <outputDirectory>/</outputDirectory>
            <fileMode>0644</fileMode>
        </file>
    </files>
    <dependencySets>
        <!--依赖关系集允许在程序集中包含和排除项目依赖关系-->
        <dependencySet>
            <!--设置输出目录相对于程序集根目录的根目录-->
            <outputDirectory>lib</outputDirectory>
            <!--定义一组依赖项工件坐标以排除,工件坐标可以以简单的groupId：artifactId形式给出，或者可以以groupId：artifactId：type [：classifier]：version的形式完全限定。另外，可以使用通配符，如*：maven- *-->
            <!--<excludes>-->
                <!--<exclude>com.ibingbo.boot:demo-conf</exclude>-->
            <!--</excludes>-->
            <!--<includes>-->
                <!--<include>com.ibingbo.boot:appServerA</include>-->
            <!--</includes>-->
        </dependencySet>
    </dependencySets>
    <!--定义要包含在程序集中的Maven仓库,包含元信息sha1和md5校验和，会在包中生成一个maven2目录-->
    <repositories>
        <repository>
            <includeMetadata>true</includeMetadata>
            <outputDirectory>maven2</outputDirectory>
        </repository>
    </repositories>
</assembly>
```
