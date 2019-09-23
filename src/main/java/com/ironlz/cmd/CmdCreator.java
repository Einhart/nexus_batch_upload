package com.ironlz.cmd;

/**
 * cmd命令生成接口
 */
public interface CmdCreator {

    /**
     * 生成cmd命令
     * @return 生成的cmd命令
     */
    String generatorCmd();
}
