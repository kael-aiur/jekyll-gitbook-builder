package kael.jekyll.gitbook.service;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by kael on 2016/12/13.
 */
public abstract class AbstractBuilder implements StaticBuilder {

    protected abstract ProcessBuilder getCommand();

    @Override
    public void build() {
        ProcessBuilder command = getCommand();
        try {
            beforeBuild(command);
            Process process = command.start();
            postProcess(process);
            afterBuild(command);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    protected void postProcess(Process process) throws InterruptedException, IOException {

        int exit = process.waitFor();
        if(exit == 0){
            InputStream is = process.getInputStream();
            int i = is.read();
            do {
                System.out.print((char)i);
                i = is.read();
                if(i == -1){
                    break;
                }
            }while (true);
            //throw new RuntimeException("unexpected exception with error exit code:"+exit);
        }else{
            InputStream is = process.getErrorStream();
            int i = is.read();
            do {
                System.out.print((char)i);
                i = is.read();
                if(i == -1){
                    break;
                }
            }while (true);
            throw new RuntimeException("unexpected exception with error exit code:"+exit);
        }

    }

    protected void beforeBuild(ProcessBuilder command){

    }
    protected void afterBuild(ProcessBuilder command){

    }
}
