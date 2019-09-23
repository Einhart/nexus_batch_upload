package com.ironlz.cmd;

import com.ironlz.cmd.exector.CmdExecutorService;
import com.ironlz.model.Pom;

import java.io.File;

/**
 * 执行Pom的安装
 */
public class PomInstallService {
    private final CmdExecutorService cmdService = new CmdExecutorService();

    /**
     * 将pom安装到本地
     * @param pom 要安装的pom
     */
    public void installPom(Pom pom, boolean needLog){
        File jarFile = getJarFile(pom);
        if(jarFile.exists()){
            Pom jarPom = pom;
            if(!jarPom.getPackaging().equals("jar")){
                jarPom = new Pom(pom.getGroupId(), pom.getArtifactId(), pom.getVersion(), jarFile);
            }
            CmdCreator jarPomCreator = new PomInstallCmdCreator(jarPom);
            cmdService.exec(jarPomCreator, needLog);
        }
        CmdCreator pomCreator = new PomInstallCmdCreator(pom);
        cmdService.exec(pomCreator, needLog);
    }

    /**
     * 发布pom到远程仓库
     * @param pom 要发布的pom
     * @param url 远程仓库地址
     * @param repositoryId 远程仓库id
     * @param needLog 是否需要日志
     */
    public void deployPom(Pom pom, String url, String repositoryId, boolean needLog){
        File jarFile = getJarFile(pom);
        if(jarFile.exists()){
            Pom jarPom = pom;
            if(!jarPom.getPackaging().equals("jar")){
                jarPom = new Pom(pom.getGroupId(), pom.getArtifactId(), pom.getVersion(), jarFile);
            }
            CmdCreator jarPomCreator = new PomInstallCmdCreator(jarPom, false, url, repositoryId);
            cmdService.exec(jarPomCreator, needLog);
        }
        pom = new Pom(pom.getGroupId(), pom.getArtifactId(), pom.getVersion(), "pom", pom.getPomFile());
        CmdCreator pomCreator = new PomInstallCmdCreator(pom, false, url, repositoryId);
        cmdService.exec(pomCreator, needLog);
    }

    private File getJarFile(Pom pom) {
        File pomFile = pom.getPomFile();
        String name = pomFile.getName();
        int suffixIndex = name.lastIndexOf(".pom");
        name = name.substring(0, suffixIndex);
        return new File(pomFile.getParentFile(), name + ".jar");
    }

}
