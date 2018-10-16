package com.design.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DJL on 2017/5/25.
 *
 * @ClassName PlatformException
 * @Description 平台自定义异常类
 */
public class PlatformException extends Exception{
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    private int code;
    private List<Throwable> manyThrow=new ArrayList<>();
    private static final long serialVersionUID = -1115095680220640442L;
    private StringBuilder excMsg=new StringBuilder();
    public PlatformException(){
        super();
    }
    public PlatformException(String massage){
        super(massage);
    }
    public PlatformException(String massage,int code){
        super(massage);
        this.code=code;
    }
    public PlatformException(List<Throwable> manyThrow){
        this.manyThrow.addAll(manyThrow);
    }
    @Override
    public String getMessage() {
        if(manyThrow.isEmpty()){
            return super.getMessage();
        }else{
            excMsg.append("异常包含：");
            for (Throwable aManyThrow : manyThrow) {
                excMsg.append(aManyThrow.getMessage());
                excMsg.append(" ");
            }
            return excMsg.toString();
        }
    }
}
