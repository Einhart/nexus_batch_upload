package com.ironlz.cmd.exector;

import com.ironlz.cmd.CmdCreator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * cmd任务执行框架
 */
public class CmdExecutorService {
    // private final ExecutorService pool; // 命令执行线程池
    private final CmdExecutor executor; // 命令执行器
    public CmdExecutorService() {
        // this.pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        this.executor = new CmdExecutor();
    }

    /**
     * 安装任务
     */
    private class Task implements Runnable{
        private CmdCreator cmdCreator;
        private boolean needLog;

        public Task(CmdCreator cmdCreator) {
            this(cmdCreator, false);
        }
        public Task(CmdCreator cmdCreator, boolean isNeedLog) {
            if(cmdCreator == null){
                System.out.println("Error: CmdCreator is null!!!");
                throw new IllegalArgumentException("CmdCreator can't be null!");
            }
            this.cmdCreator = cmdCreator;
            this.needLog = isNeedLog;
        }

        @Override
        public void run() {
            String cmdCommond = this.cmdCreator.generatorCmd();
            if(!needLog){
                executor.execCmd(cmdCommond);
            }else{
                String response = executor.execCmd(cmdCommond, needLog);
                System.out.println("Exec: " + cmdCommond + "\n response : " + response);
            }
        }
    }

    /**
     * 执行一个cmd命令，不需要记录响应
     */
    public void exec(CmdCreator cmdCreator){
        this.exec(cmdCreator, false);
    }

    /**
     * 执行一个cmd命令，并且是否需要记录响应
     * @param isNeedLog 是否需要记录响应
     */
    public void exec(CmdCreator cmdCreator, boolean isNeedLog){
        Task task = new Task(cmdCreator, isNeedLog);
        task.run();
    }

}
