package com.ironlz;


import java.io.File;

public class Main {
    private static String deployUrl;
    private static String repositoryId;
    private static File searchRoot;
    public static void main(String[] args) {
        searchRoot = new File(".");
        deployUrl = null;
        repositoryId = null;
        boolean isLocal = true;
        if(args != null && args.length != 0){
            searchRoot = new File(args[0]);
            if(!searchRoot.exists() || searchRoot.isFile()){

                throw new IllegalArgumentException("The path must exist and be a directory.");
            }
        }
        if (args.length >= 3){
            deployUrl = args[1];
            repositoryId = args[2];
            isLocal = false;
        }

        searchAndInstall(searchRoot, isLocal);

    }

    /**
     * 查找并执行本地安装
     * @param searchRoot 查找的起始路径
     */
    private static void searchAndInstall(File searchRoot, boolean isLocal) {
        Install install = null;
        if(isLocal){
            install = new Install(searchRoot);
        }
        else{
            install = new Install(searchRoot, deployUrl, repositoryId);
        }
        install.start(true);
    }
}
