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
                    throw new GenerateCodeException("Wrong option format!");
            }
        }

        String name = modifiedPattern.substring(slashes.get(1)+1, slashes.get(2));

        if (!Pattern.matches("^[A-Za-z]+$", name)){
            throw new GenerateCodeException("Wrong pattern format!");
        }

        List<String[]> fields = new ArrayList<>();

        if (slashes.size() > 3) {
            for (int i = 3; i < slashes.size(); i++) {
                String fieldPattern = modifiedPattern.substring(slashes.get(i-1)+1, slashes.get(i));
                String[] fieldParameters = fieldPattern.split("\\.");
                if (fieldParameters.length != 2 ||
                    Pattern.matches("^[0-9]+.*$", fieldParameters[0]) ||
                    Pattern.matches("^[0-9]+.*$", fieldParameters[1])
                ) {
                    throw new GenerateCodeException("Wrong field pattern!");
                }
                fields.add(fieldParameters);
            }
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
        stringBuilder.append(" {\n\n");

        for (String[] fieldParameters : fields) {
            stringBuilder.append("\tprivate ");
            stringBuilder.append(fieldParameters[0] + " ");
            stringBuilder.append(fieldParameters[1] + ";\n");
        }
        stringBuilder.append("\n");
        stringBuilder.append("\tpublic " + name + "(");
        for (int i = 0; i < fields.size(); i++) {
            String[] fieldParameters = fields.get(i);
            stringBuilder.append(fieldParameters[0] + " ");
            stringBuilder.append(fieldParameters[1]);
            if (i != fields.size() - 1){
                stringBuilder.append(",");
            }
        }
        stringBuilder.append(") {\n");
        for (String[] fieldParameters : fields) {
            stringBuilder.append("\t\tthis.");
            stringBuilder.append(fieldParameters[1] + " = ");
            stringBuilder.append(fieldParameters[1] + ";\n");
        }
        stringBuilder.append("\t}\n");
        stringBuilder.append("}\n");
        code = stringBuilder.toString();
    }
}
