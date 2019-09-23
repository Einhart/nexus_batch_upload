package com.ironlz.cmd.exector;

import java.io.IOException;
import java.util.Scanner;

/**
 * cmd命令执行器
 */
public class CmdExecutor {

    private static final String CMD_PREFIX = "cmd /c";

    /**
     * 执行命令并且不需要响应
     * @param cmd 要执行的命令
     */
    public void execCmd(String cmd){
        execCmd(cmd, false);
    }


    /**
     * 执行cmd命令
     * @param cmd cmd命令
     * @param hasResp 是否返回响应
     * @return 命令
     */
    public String execCmd(String cmd, boolean hasResp){
        String jCmd = generateCmdStr(cmd);
        System.out.println("Exec: " + jCmd);
        String resp = null;
        try {
            Process process = Runtime.getRuntime().exec(jCmd);
            if(hasResp){
                Scanner in = new Scanner(process.getInputStream());
                StringBuilder builder = new StringBuilder();
                while(in.hasNextLine()){
                    String line = in.nextLine();
                    System.out.println(line);
                    builder.append(line);
                }
                in.close();
                resp = builder.toString();
            }
            process.destroy();
        } catch (IOException e){
            System.out.println("IOException happened while exec " + cmd);
            e.printStackTrace();
        }
        return resp;
    }

    /**
     * 生成一个cmd执行命令
     * @param cmd 命令
     * @return 生成的完整的cmd命令
     */
    private String generateCmdStr(String cmd){
        return CMD_PREFIX + " " + cmd;
    }
}
