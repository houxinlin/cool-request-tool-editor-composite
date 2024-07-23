package dev.coolrequest.tool.editor.composite;

import com.intellij.diff.DiffContentFactory;
import com.intellij.diff.DiffManager;
import com.intellij.diff.DiffRequestPanel;
import com.intellij.diff.contents.DocumentContent;
import com.intellij.diff.requests.SimpleDiffRequest;
import com.intellij.diff.util.DiffUserDataKeys;
import com.intellij.diff.util.Side;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.text.StringUtil;
import dev.coolrequest.tool.CoolToolPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CompositePanel extends JPanel implements CoolToolPanel {
    private Project project;
    private DiffRequestPanel diffRequestPanel;

    @Override
    public JPanel createPanel() {
        Document document1 = EditorFactory.getInstance().createDocument(StringUtil.convertLineSeparators(""));
        Document document2 = EditorFactory.getInstance().createDocument(StringUtil.convertLineSeparators(""));
        DocumentContent documentContent = DiffContentFactory.getInstance().create(project, document1);
        DocumentContent documentContent2 = DiffContentFactory.getInstance().create(project, document2);
        SimpleDiffRequest request = new SimpleDiffRequest("Diff", documentContent, documentContent2, "First", "Second");
        request.putUserData(DiffUserDataKeys.FORCE_READ_ONLY, false);
        request.putUserData(DiffUserDataKeys.FORCE_READ_ONLY_CONTENTS, new boolean[]{false, false});
        request.putUserData(DiffUserDataKeys.SCROLL_TO_LINE, Pair.create(Side.RIGHT, 1));
        request.putUserData(DiffUserDataKeys.SCROLL_TO_LINE, Pair.create(Side.LEFT, 1));
        diffRequestPanel = DiffManager.getInstance().createRequestPanel(project, project, null);
        request.putUserData(DiffUserDataKeys.CONTEXT_ACTIONS, List.of(new ChangeFileTypeAnAction(diffRequestPanel, documentContent, documentContent2)));
        diffRequestPanel.setRequest(request);
        JPanel root = new JPanel(new BorderLayout());
        root.add(diffRequestPanel.getComponent(), BorderLayout.CENTER);
        return root;
    }

    @Override
    public void showTool() {

    }

    @Override
    public void closeTool() {
        if (diffRequestPanel == null) return;
        Disposer.dispose(diffRequestPanel);
    }
}
