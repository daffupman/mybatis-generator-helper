# mybatis-generator-helper(mybatis逆向工程助手)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/daff/mybatis-generator-helper)

## 简介

此项目是基于mybatis的逆向工程封装。mybatis的逆向工程可以帮我们自动生成单表的CRUD，减少一些重复工作，可提高工作效率节约开发时间。在这基础之上，此项目还编写了自定义的插件（lombok、swagger），并提供了注释生成器。集成通用mapper，也可以自定义mapper接口。


## 目录结构

此项目由两个模块组成：
- base-model：该模块可放置一些基础模型，如BaseEntity、BaseMapper等，如果在使用此工具（本项目）时，工作中的项目是有基类的，那么需要将基类复制一份到指定的目录下；否则可无视此模块。
- generator：该模块是项目的核心部分
    - GeneratorMapper：mybatis逆向工程的入口程序，可不予理会
    - mybatis-generator：逆向工程的配置文件。可做数据库信息配置、插件配置、表生成配置等。这是一个需要关注的地方。
    - plugin：插件包，目前只有整合lombok、swagger，和一个自动生成注释的功能。
    
    
##  快速使用

此项目非常简单，使用起来也很简单。一般在初次使用该项目时，只需要修改 `mybatis-generator.xml` 文件即可。修改的内容包括：
- 数据库连接信息；
- 按情况修改entity、mapper的包名；
- 需要生成的数据库表，可使用通配符 `%` 或指定表；
- 在 `comment.properties` 文件指定 `author` 和 `date_pattern` ；

然后直接运行 `GeneratorMapper#main` 即可。最后在 `generate` 目录下输出 `entity` 、 `mapper` 和 `xml` 。将这三部分的内容CV到自己项目中的相应位置。这样，就完事了。
> 【注意】：不建议将此项目的功能整合到手头上正在做的项目，直接在这个项目中生成好代码后，CV到目标项目中即可。另外再次生成的时候会覆盖到原来的代码。

另外在需要继承基类（BaseEntity）的时候，需要额外地多做一些步骤：
- 将BaseEntity类放到 `base-model` 项目下的 `io.daff.base` 文件下，可使用maven命令或插件将 `base-model` 项目发布到本地仓库；
- 在 `mybatis-generator.xml` 中的将javaBean的配置下的 `rootClass` 的注释打开；
- 在 `mybatis-generator.xml` 中的table标签中的 `ignoreColumn` 节点打开，并添加目标类中在基类中已经存储的属性名；
- 在 `lombok.properties` 文件中将 `hasParent` 修改成 `true`。


##  功能

### v0.0.3
- 扩充BaseMapper功能：批量新增，可忽略更新

### v0.0.2
- 集成通用mapper
- 增加批量更新功能

### v0.0.1

- 按需生成entity，mapper接口和mapper映射文件
- 整合lombok和swagger
- 可自定义注释模板
- 可生成继承父类的entity