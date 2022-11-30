package com.example.devtoolslb5.analyzer;

import com.example.devtoolslb5.code.ClassCode;
import com.example.devtoolslb5.code.ICode;

import java.util.Optional;

public class PatternAnalyzer {

    private String pattern;

    public PatternAnalyzer(String pattern){
        this.pattern = pattern;
    }

    public Optional<ICode> analyze() {
        ICode code = new ClassCode();
        code.generateCode(pattern);

        return Optional.of(code);
    }
}
