package com.example.devtoolslb5.analyzer;

import com.example.devtoolslb5.code.ClassCode;
import com.example.devtoolslb5.code.ICode;
import com.example.devtoolslb5.tool.GenerateCodeException;
import com.intellij.openapi.ui.Messages;

import java.util.Optional;

public class PatternAnalyzer {

    private String pattern;

    public PatternAnalyzer(String pattern){
        this.pattern = pattern;
    }

    public Optional<ICode> analyze() throws GenerateCodeException{
        ICode code = new ClassCode();
        if (code.isSuitable(pattern)) {
            code.generateCode(pattern);
        } else {
            throw new GenerateCodeException("Wrong pattern format!");
        }


        if (code.getCode() == null) {
            return Optional.empty();
        }
        return Optional.of(code);
    }
}
