package org.satellite.dev.progiple.satemenus.self.menusCreator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum GeneratorType {
    MENU("menus/"),
    TEMPLATE("templates/");

    public final String path;
}
