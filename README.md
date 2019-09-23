"# nexus_batch_upload" 

本程序用于批量的将pom声明及其jar包上传到maven仓库，主要针对nexus无法批量上传，上传的jar包无法自动解析依赖等问题。
使用本程序可以完整的将保存依赖解析，避免nexus自动生成pom来替换原始的pom，导致依赖丢失。

Usage：
    程序运行后，会扫描指定目录，查找pom，并将pom和对应的jar包安装到本地仓库或远程仓库。
    被扫描的目录需要在同级目录下存放对应的pom和jar包，pom和jar包的文件应该相同。
    一般被扫描目录是一个maven仓库的拷贝。
    发布jar包到本地:
        java -jar maven_jars_upload-0.1-SNAPSHOT.jar <jars_dir>
    发布jar包到远程仓库：
        java -jar maven_jars_upload-0.1-SNAPSHOT.jar <jars_dir> <remote_repository_url> <remote_repository_id>

version: 0.1-SNAPSHOT
    1、实现基本功能
    2、无错误处理
    3、未作执行失败或成功提示，需自行观察客户端输出