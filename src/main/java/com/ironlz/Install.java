package com.ironlz;

import com.ironlz.cmd.PomInstallService;
import com.ironlz.model.Pom;

import java.io.File;

/**
 * 执行Pom的查找及安装
 */
public class Install {
    private File root;
    private boolean isLocal;
    private String deployUrl;
    private String repositoryId;
    private PomInstallService service;

    public Install(File root) {
        this(root, null, null);
    }

    public Install(File root, String deployUrl, String repositoryId) {
        this.root = root;
        this.isLocal = false;
        if(deployUrl == null || repositoryId == null){
            isLocal = true;
        }
        this.deployUrl = deployUrl;
        this.repositoryId = repositoryId;
        this.service = new PomInstallService();
    }

    /**
     * 开始查找并安装
     * @param needLog 是否打印日志
     */
    public void start(boolean needLog){
        searchAndInstallPom(root, needLog);
    }

    private static int counter = 0;
    /**
     * 执行文件查找、pom生成、安装
     * @param root 查找的起始路径
     * @param needLog 是否打印日志
     */
    private void searchAndInstallPom(File root, boolean needLog) {
        if(root.isFile()){
            boolean isPom = root.getName().endsWith(".pom");
            if(isPom){
                counter++;
                Pom pom = parsePom(root);
                System.out.println("[" + counter + "]:" + "Install pom: " + pom);
                installPom(pom, needLog);
            }
        }else{
            File[] childs = root.listFiles();
            for(File file : childs){
                searchAndInstallPom(file, needLog);
            }
        }
    }

    private void installPom(Pom pom, boolean needLog) {
        if(isLocal){
            service.installPom(pom, needLog);
        }else{
            service.deployPom(pom, deployUrl, repositoryId, needLog);
        }
    }

    /**
     * 解析Pom文件并生成其信息
     * @param root pom文件
     * @return POM信息
     */
    private Pom parsePom(File root) {
        Pom pom = new Pom(root);
        return pom;
    }
}
