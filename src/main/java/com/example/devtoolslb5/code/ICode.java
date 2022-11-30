package com.example.devtoolslb5.code;

import com.example.devtoolslb5.tool.GenerateCodeException;

public interface ICode {

    String getCode();
    boolean isSuitable(String pattern);
    void generateCode(String pattern) throws GenerateCodeException;
}
