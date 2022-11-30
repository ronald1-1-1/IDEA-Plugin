package com.example.devtoolslb5;

import com.example.devtoolslb5.analyzer.PatternAnalyzer;
import com.example.devtoolslb5.code.ICode;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import org.apache.batik.gvt.Selectable;

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

            if (pattern == null) return;

            PatternAnalyzer patternAnalyzer = new PatternAnalyzer(pattern);
            Optional<ICode> code = patternAnalyzer.analyze();
            if (code.isPresent()) {
                System.out.println(code.get().getCode());
                editor.getDocument().replaceString(selectionModel.getSelectionStart(), selectionModel.getSelectionEnd(), code.get().getCode());
                selectionModel.removeSelection();
            }

        }
    }
}