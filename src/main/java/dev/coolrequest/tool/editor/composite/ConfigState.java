package dev.coolrequest.tool.editor.composite;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;

@Service
@State(name = "CoolRequestToolEditorComposite", storages = @Storage("CoolRequestToolEditorComposite.xml"))
public final class ConfigState implements PersistentStateComponent<CompositeState> {
    private CompositeState state = new CompositeState();

    public static ConfigState getInstance() {
        return ApplicationManager.getApplication().getService(ConfigState.class);
    }

    @Override
    public @NotNull CompositeState getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull CompositeState state) {
        this.state = state;
    }

}
