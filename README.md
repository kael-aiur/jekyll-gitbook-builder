# 简介

这是一个可以用来构建jekyll和gitbook的官网的构建器。

jekyll是用来生成静态网页的，而gitbook是用来写电子书的，github支持使用jekyll，但是不支持gitbook，因此在使用jekyll的时候，连接文档需要外链到gitbook官网，gitbook的内容是公开的，而且墙外网络不稳定，所以我们有时候也想搭建内网的静态网站，但是如果每次修改完都手动build这些静态文件，那是一件非常痛苦的事情，所以我就做了这个工具，当然，这个工具依赖的东西比较多：

* git
* jekyll
* gitbook
* github(或其他git服务如gitlab)

构建过程：

* 构建器基于netty开发，默认监听8000端口的http请求
* 使用构建器部署之后，需要在github(或者其他git服务如gitlab)上创建一个webhook来做事件推送
* 每次有新的代码推送到github上之后，github的webhook就会推送一个消息到构建器
* 构建器接收消息之后，根据请求参数判断得出当前是jekyll工程还是gitbook工程更新
* 根据配置使用git更新需要构建的分支的最新代码
* 构建静态资源并输出到指定文件夹

最后构建输出的文件夹就是一个可以直接使用tomcat或者其他web容器部署的静态网站。

只要我们把构建结果的输出目录指定为web容器的服务目录，就可以直接push到git仓库，剩下的部署更新都是全自动化的了。

## 配置说明

```
# git的bash路径
git.bash=C:\\Program Files\\Git\\bin\\git.exe
# jekyll的bash路径
jekyll.bash=C:\\Ruby23-x64\\bin\\jekyll.bat
# jekyll的JEKYLL_ENV值，可以用来指定构建环境，在jekyll的模板中通过这个环境变量进行生成静态资源
jekyll.profile=local
# jekyll源码工程的远程仓库名
jekyll.repository=origin
# jekyll源码工程用来构建站点的分支
jekyll.branch=master
# jekyll源码工程的本地目录，用于git pull
jekyll.git.repo=E:\\test\\jekyll-test
# jekyll源码工程的本地目录，用于jekyll build
jekyll.source=E:\\test\\jekyll-test
# jekyll编译结果的输出目录
jekyll.target=D:\\workspace\\tomcat\\tomcat-8.0.35-prod\\webapps\\ROOT
# jekyll的工程名，用来判断当前源码更新是否jekyll，要求和jekyll源码工程同名
jekyll.project.name=jekyll-test

# gitbook的bash路径
gitbook.base=C:\\Users\\kael\\AppData\\Roaming\\npm\\gitbook.cmd
# gitbook源码工程的远程仓库名
gitbook.repository=origin
# gitbook源码工程用来构建电子书的分支
gitbook.branch=master
# gitbook源码工程的本地目录，用于git pull
gitbook.git.repo=E:\\test\\gitbook-test
# gitbook源码工程的本地目录，用于git build
gitbook.source=E:\\test\\gitbook-test
# gitbook编译结果的输出目录
gitbook.target=D:\\workspace\\tomcat\\tomcat-8.0.35-prod\\webapps\\ROOT\\doc
# gitbook的工程名，用来判断当前源码更新是否jekyll，要求和jekyll源码工程同名
gitbook.project.name=gitbook-test
# 默认密码，用来标识请求是否受信任，需要把这个值填到webhook的Secret Token中
security.secret.key=kaelkaelkael
```