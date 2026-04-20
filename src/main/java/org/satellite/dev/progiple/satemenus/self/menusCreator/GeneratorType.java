package org.satellite.dev.progiple.satemenus.self.menusCreator;

import lombok.RequiredArgsConstructor;
import org.novasparkle.lunaspring.API.configuration.IConfig;
import org.satellite.dev.progiple.satemenus.menus.Menus;
import org.satellite.dev.progiple.satemenus.menus.params.ITemplated;
import org.satellite.dev.progiple.satemenus.menus.params.MenuSettings;
import org.satellite.dev.progiple.satemenus.menus.params.templates.Template;
import org.satellite.dev.progiple.satemenus.menus.params.templates.Templates;

@RequiredArgsConstructor
public enum GeneratorType {
    MENU("menus/") {
        @Override
        public void save(ITemplated templated) {
            Menus.register((MenuSettings) templated);
        }

        @Override
        public MenuSettings generate(IConfig config) {
            return new MenuSettings(config);
        }
    },
    TEMPLATE("templates/") {
        @Override
        public void save(ITemplated templated) {
            Templates.register((Template) templated);
        }

        @Override
        public Template generate(IConfig config) {
            return Template.convert(config);
        }
    };

    public final String path;

    abstract public void save(ITemplated templated);
    abstract public ITemplated generate(IConfig config);
}
