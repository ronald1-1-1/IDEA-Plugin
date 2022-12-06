package com.example.devtoolslb5;

import com.example.devtoolslb5.analyzer.PatternAnalyzer;
import com.example.devtoolslb5.code.ICode;
import com.example.devtoolslb5.tool.GenerateCodeException;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.ui.Messages;

import java.util.Optional;

public class FastEnterCode extends EditorAction {

    public FastEnterCode(EditorActionHandler defaultHandler) {
        super(defaultHandler);
    }

    public FastEnterCode() {
        this(new MyHandler());
    }

    private static class MyHandler extends EditorWriteActionHandler {
        private MyHandler() {
        }

        @Override
        public void executeWriteAction(Editor editor, DataContext dataContext) {
            SelectionModel selectionModel = editor.getSelectionModel();
            String pattern = selectionModel.getSelectedText();

            if (pattern == null) {
                pattern = Messages.showInputDialog("You need to select text to use the generator. Please enter your pattern to the following field.","Unable to use code generator", null);
                if (pattern == null || pattern.isEmpty()) {
                    Messages.showInfoMessage("You need to enter pattern to use the generator. ", "Unable to use code generator");
                    return;
                }
            }
            System.out.println("asdasdasdasd " + pattern);
            PatternAnalyzer patternAnalyzer = new PatternAnalyzer(pattern);

            Optional<ICode> code = Optional.empty();
            try {
                code = patternAnalyzer.analyze();
            }catch (GenerateCodeException e) {
                pattern = Messages.showInputDialog(e.getMessage() + " Please enter your pattern to the following field.", "Unable to use code generator", null);
                if (pattern == null || pattern.isEmpty()) {
                    return;
                }
                patternAnalyzer = new PatternAnalyzer(pattern);
                try {
                    code = patternAnalyzer.analyze();
                } catch (GenerateCodeException e2) {
                    Messages.showInfoMessage(e2.getMessage(), "Unable to use code generator");
                }
            }

            if (!code.isEmpty()) {
                if (selectionModel.getSelectedText() != null) {
                    editor.getDocument().replaceString(selectionModel.getSelectionStart(), selectionModel.getSelectionEnd(), code.get().getCode());
                } else {
                    editor.getDocument().insertString(editor.getCaretModel().getOffset(), code.get().getCode());
                }

                selectionModel.removeSelection();
            }

        }
    }
}