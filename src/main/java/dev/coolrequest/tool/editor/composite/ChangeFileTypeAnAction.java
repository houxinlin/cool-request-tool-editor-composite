package dev.coolrequest.tool.editor.composite;

import com.intellij.diff.DiffContentFactory;
import com.intellij.diff.DiffRequestPanel;
import com.intellij.diff.contents.DocumentContent;
import com.intellij.diff.requests.SimpleDiffRequest;
import com.intellij.diff.util.DiffUserDataKeys;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.ex.ComboBoxAction;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;

public class ChangeFileTypeAnAction extends ComboBoxAction {
    private String fileTypeName = "Select File Type";
    private final DiffRequestPanel diffRequestPanel;

    private DocumentContent documentContent1;
    private DocumentContent documentContent2;

    public ChangeFileTypeAnAction(DiffRequestPanel diffRequestPanel, DocumentContent documentContent1, DocumentContent documentContent2) {
        this.diffRequestPanel = diffRequestPanel;
        this.documentContent1 = documentContent1;
        this.documentContent2 = documentContent2;
    }

    @Override
    protected @NotNull DefaultActionGroup createPopupActionGroup(@NotNull JComponent button, @NotNull DataContext dataContext) {
        DefaultActionGroup group = new DefaultActionGroup();
        for (FileType registeredFileType : FileTypeManager.getInstance().getRegisteredFileTypes()) {
            group.add(new CustomComboBoxItemAction(registeredFileType.getName(), registeredFileType));
        }
        return group;
    }


    public class CustomComboBoxItemAction extends AnAction {
        private final FileType fileType;

        public CustomComboBoxItemAction(String item, FileType fileType) {
            super(item);
            this.fileType = fileType;
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
            Project project = e.getProject();
            if (project != null) {
                fileTypeName = fileType.getName();
                ConfigState.getInstance().getState().fileTypeName = fileTypeName;
                FileType newFileType = FileTypeManager.getInstance().getStdFileType(fileType.getName());
                documentContent1 = DiffContentFactory.getInstance().create(project, documentContent1.getDocument(), newFileType);
                documentContent2 = DiffContentFactory.getInstance().create(project, documentContent2.getDocument(), newFileType);
                SimpleDiffRequest simpleDiffRequest = new SimpleDiffRequest("Diff", documentContent1, documentContent2, "First", "Second");
                simpleDiffRequest.putUserData(DiffUserDataKeys.CONTEXT_ACTIONS, List.of(ChangeFileTypeAnAction.this));
                diffRequestPanel.setRequest(simpleDiffRequest);
            }
        }
    }

    @Override
    public void updateCustomComponent(@NotNull JComponent component, @NotNull Presentation presentation) {
        super.updateCustomComponent(component, presentation);
        String name = ConfigState.getInstance().getState().fileTypeName;
        if (name == null) name = fileTypeName;
        presentation.setText(name);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {

    }
}
