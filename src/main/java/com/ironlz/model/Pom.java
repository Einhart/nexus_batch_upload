package com.ironlz.model;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.Scanner;

/**
 * Pom的基本信息封装
 */
public class Pom {

    private static final String GROUP_ID = "/project/groupId";
    private static final String ARTIFACT_ID = "/project/artifactId";
    private static final String VERSION = "/project/version";
    private static final String PACKAGING = "/project/packaging";

    private static final String GROUP_PARENT_ID = "/project/parent/groupId";
    private static final String ARTIFACT_PARENT_ID = "/project/parent/artifactId";
    private static final String VERSION_PARENT = "/project/parent/version";
    private static final String PACKAGING_PARENT = "/project/parent/packaging";
    private static final String PARENT_PACKAGING_DEFAULT = "pom";

    private static final String DEFAULT_PACKAGING = "jar";
    private String groupId;
    private String artifactId;
    private String version;
    private String packaging;
    private File pomFile;

    /**
     * 为jar包生成一个pom
     * @param groupId 组id
     * @param artifactId 项目id
     * @param version 版本
     */
    public Pom(String groupId, String artifactId, String version, File pomFile) {
        this(groupId, artifactId, version, DEFAULT_PACKAGING, pomFile);
    }

    /**
     * 生成一个详细POM信息
     * @param groupId 组id
     * @param artifactId 项目id
     * @param version 版本号
     * @param packaging 打包方式
     */
    public Pom(String groupId, String artifactId, String version, String packaging, File pomFile) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.packaging = packaging;
        this.pomFile = pomFile;
    }

    /**
     * 生成一个Pom信息
     * @param pomFile pom文件
     */
    public Pom(File pomFile){
        if(pomFile == null || !pomFile.exists() || pomFile.getName().equals(".pom")){
            throw new IllegalArgumentException("pom file must be exist and a pom." + pomFile.getAbsolutePath());
        }
        this.pomFile = pomFile;
        File tempFile = getTempPomFile(pomFile);
        parsePom(tempFile, pomFile);
        tempFile.delete();
        if(this.packaging == null || this.packaging.isEmpty()){
            this.packaging = DEFAULT_PACKAGING;
        }
    }

    private void parsePom(File tempFile, File pomFile) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document xmlDoc = builder.parse(tempFile);
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath path = xPathFactory.newXPath();
            groupId = path.evaluate(GROUP_ID, xmlDoc);
            artifactId = path.evaluate(ARTIFACT_ID, xmlDoc);
            version = path.evaluate(VERSION, xmlDoc);
            packaging = "pom";
            if(groupId == null || groupId.isEmpty()){
                groupId = path.evaluate(GROUP_PARENT_ID, xmlDoc);
            }
            if(artifactId == null || artifactId.isEmpty()){
                artifactId = path.evaluate(ARTIFACT_PARENT_ID, xmlDoc);
            }
            if(version == null || version.isEmpty()){
                version = path.evaluate(VERSION_PARENT, xmlDoc);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private File getTempPomFile(File pomFile) {
        Scanner in = null;
        PrintWriter out = null;
        File outFile = null;
        try {
            in = new Scanner(pomFile);
            outFile = new File(pomFile.getName() + ".tmp");
            out = new PrintWriter(outFile);
            while (in.hasNextLine()){
                String line = in.nextLine();
                line = line.replaceAll("&", "");
                out.println(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(in != null){
                in.close();
            }
            if(out != null) {
                out.flush();
                out.close();
            }
        }
        return outFile;
    }

    public static String getDefaultPackaging() {
        return DEFAULT_PACKAGING;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    public String getPackaging() {
        return packaging;
    }

    public File getPomFile() {
        return pomFile;
    }

    @Override
    public String toString() {
        return "Pom{" +
                "groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", version='" + version + '\'' +
                ", packaging='" + packaging + '\'' +
                ", pomFile=" + pomFile +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pom)) return false;
        Pom pom = (Pom) o;
        return Objects.equals(getGroupId(), pom.getGroupId()) &&
                Objects.equals(getArtifactId(), pom.getArtifactId()) &&
                Objects.equals(getVersion(), pom.getVersion()) &&
                Objects.equals(getPackaging(), pom.getPackaging()) &&
                Objects.equals(getPomFile(), pom.getPomFile());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGroupId(), getArtifactId(), getVersion(), getPackaging(), getPomFile());
    }

}
