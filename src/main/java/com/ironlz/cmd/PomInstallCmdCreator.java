package com.ironlz.cmd;

import com.ironlz.model.Pom;
/**
 * 生成cmd的安装命令
 */
public class PomInstallCmdCreator implements CmdCreator{
    private Pom pom;
    private boolean isLocal;
    private String deployUrl;
    private String repositoryId;

    private static final String LOCAL_CMD_PREFIX = "mvn install:install-file";
    private static final String DEPLOY_CMD_PREFIX = "mvn deploy:deploy-file";

    public PomInstallCmdCreator(Pom pom) {
        this(pom, true, null, null);
    }

    public PomInstallCmdCreator(Pom pom, boolean isLocal, String deployUrl, String repositoryId) {

        if(!checkPom(pom)){
            System.out.println("Pom must correct! " + pom);
            throw new IllegalArgumentException("Pom don't satisfied! " + pom);
        }
        this.pom = pom;
        this.isLocal = isLocal;
        if(!isLocal){
            if(checkStrNotEmpty(deployUrl) && checkStrNotEmpty(repositoryId)){
                this.deployUrl = deployUrl;
                this.repositoryId = repositoryId;
            }
            else{
                throw new IllegalArgumentException("Must specified deployUrl and repositoryId");
            }
        }

    }

    /**
     * 检查pom是否合法
     * @param pom 要检查的pom
     * @return true pom合法， false pom不合法
     */
    private boolean checkPom(Pom pom) {
        return pom != null &&
                checkStrNotEmpty(pom.getGroupId()) &&
                checkStrNotEmpty(pom.getArtifactId()) &&
                checkStrNotEmpty(pom.getPackaging()) &&
                checkStrNotEmpty(pom.getVersion()) &&
                (pom.getPomFile() != null) &&
                pom.getPomFile().exists();
    }

    /**
     * 判定Str不为null且长度不为0
     * @param str 要判定的String
     * @return 是否符合条件
     */
    private boolean checkStrNotEmpty(String str){
        return str != null && !str.isEmpty();
    }

    @Override
    public String generatorCmd() {
        if(isLocal){
            return generatorLocalCmd();
        }else{
            return generateDeployCmd();
        }
    }

    /**
     * 生成远程发布命令：
     * mvn deploy:deploy-file -Dfile=protobuf-java-3.9.0.pom -DgroupId=com.google.protobuf -DartifactId=protobuf-java -Dve rsion=3.9.0 -Durl=http://192.168.52.10:8081/repository/maven-releases/ -Dpackaging=pom -DrepositoryId=maven-releases
     * @return 远程发布命令
     */
    private String generateDeployCmd() {
        StringBuilder cmdBuilder = new StringBuilder();
        cmdBuilder.append(DEPLOY_CMD_PREFIX).append(" ")
                .append("-Durl=").append(this.deployUrl).append(" ")
                .append("-DrepositoryId=").append(this.repositoryId).append(" ")
                .append(generatePomArgs(this.pom));
        return cmdBuilder.toString();
    }

    /**
     * 生成本地发布命令
     * @return 本地发布命令
     */
    private String generatorLocalCmd() {
        StringBuilder cmdBuilder = new StringBuilder();
        cmdBuilder.append(LOCAL_CMD_PREFIX).append(" ")
                .append(generatePomArgs(this.pom));
        return cmdBuilder.toString();
    }

    /**
     * 生成pom的安装参数
     * @param pom pom
     * @return pom的安装参数
     */
    private String generatePomArgs(Pom pom){
        StringBuilder builder = new StringBuilder();
        builder.append("-Dfile=").append(pom.getPomFile().getAbsolutePath()).append(" ")
                .append("-DgroupId=").append(pom.getGroupId()).append(" ")
                .append("-DartifactId=").append(pom.getArtifactId()).append(" ")
                .append("-Dversion=").append(pom.getVersion()).append(" ")
                .append("-Dpackaging=").append(pom.getPackaging()).append(" ");
        return builder.toString();
    }
}
