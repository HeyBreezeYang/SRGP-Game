package com.manager.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DJL on 2017/6/20.
 *
 * @ClassName manage
 * @Description
 */
public class ManageException extends Exception{
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    private int code;
    private List<Throwable> manyThrow=new ArrayList<>();
    private static final long serialVersionUID = -1115095680220640425L;
    private StringBuilder excMsg=new StringBuilder();
    public ManageException(){
        super();
    }
    public ManageException(String massage){
        super(massage);
    }
    public ManageException(String massage,int code){
        super(massage);
        this.code=code;
    }
    public ManageException(List<Throwable> manyThrow){
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
