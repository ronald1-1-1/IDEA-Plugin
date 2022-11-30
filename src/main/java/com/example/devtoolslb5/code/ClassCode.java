package com.example.devtoolslb5.code;

import com.example.devtoolslb5.tool.GenerateCodeException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ClassCode implements ICode {

    private String code;
    private boolean isStatic;
    private boolean isPublic;

    public ClassCode() {
        isStatic = false;
        isPublic = true;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public boolean isSuitable(String pattern) {
        return pattern.startsWith("c/");
    }

    @Override
    public void generateCode(String pattern) throws GenerateCodeException {
        String modifiedPattern = pattern.replaceAll(" ", "");

        if (!modifiedPattern.startsWith("c/")) {
            throw new GenerateCodeException("This is not class code pattern:" + pattern);
        }

        List<Integer> slashes = new ArrayList<Integer>();
        for (int i = 0; i < modifiedPattern.length(); i++) {
            if (modifiedPattern.charAt(i) == '/') {
                slashes.add(i);
            }
        }

        if (slashes.size() < 3) {
            throw new GenerateCodeException("Wrong pattern format!");
        }

        String options = modifiedPattern.substring(slashes.get(0)+1, slashes.get(1));

        for (int i = 0; i < options.length(); i++) {
            char option = options.charAt(i);
            switch (option) {
                case  ('p'):
                    isPublic = false;
                    break;
                case  ('P'):
                    isPublic = true;
                    break;
                case  ('s'):
                    isStatic = true;
                    break;
                default:
                    throw new GenerateCodeException("Wrong option!");
            }
        }

        String name = modifiedPattern.substring(slashes.get(1)+1, slashes.get(2));

        if (!Pattern.matches("^[A-Za-z]+$", name)){
            throw new GenerateCodeException("Wrong pattern format!");
        }

        StringBuilder stringBuilder = new StringBuilder();
        if (isPublic) {
            stringBuilder.append("public ");
        }else {
            stringBuilder.append("private ");
        }

        if (isStatic) {
            stringBuilder.append("static ");
        }

        stringBuilder.append("class ");
        stringBuilder.append(name);
        stringBuilder.append(" {\n\n\n}");

        code = stringBuilder.toString();
    }
}
