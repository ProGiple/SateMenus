package org.satellite.dev.progiple.satemenus.menus.params.actions;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IMenuAction {
    @NotNull List<String> actions();
    boolean cancelEventIfError();
}
