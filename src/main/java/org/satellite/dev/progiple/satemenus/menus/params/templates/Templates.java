package org.satellite.dev.progiple.satemenus.menus.params.templates;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.novasparkle.lunaspring.API.configuration.IConfig;
import org.novasparkle.lunaspring.API.util.utilities.Utils;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@UtilityClass
public class Templates {
    @Getter
    private final Set<Template> templates = new HashSet<>();

    public Template getTemplate(String id) {
        return Utils.find(templates, t -> t.id().equals(id)).orElse(null);
    }

    public List<String> getIds() {
        return templates.stream().map(Template::id).toList();
    }

    public void register(Template template) {
        templates.removeIf(t -> t.id().equals(template.id()));
        templates.add(template);
    }

    public void unregister(Template template) {
        templates.remove(template);
    }

    public void loadFromDir(File directory) {
        if (!directory.exists() || !directory.isDirectory() || directory.listFiles() == null) return;

        File[] files = directory.listFiles();
        for (File file : files) register(Template.convert(new IConfig(file)));
    }
}
