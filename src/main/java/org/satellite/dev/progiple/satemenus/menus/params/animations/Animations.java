package org.satellite.dev.progiple.satemenus.menus.params.animations;

import lombok.experimental.UtilityClass;
import org.novasparkle.lunaspring.API.configuration.IConfig;
import org.satellite.dev.progiple.satemenus.menus.params.templates.Template;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@UtilityClass
public class Animations {
    private final Map<String, IAnimation> animations = new HashMap<>();

    public IAnimation get(String id) {
        return animations.get(id);
    }

    public Set<String> keySet() {
        return animations.keySet();
    }

    public void register(String id, IAnimation animation) {
        animations.put(id, animation);
    }

    public void unregister(String id) {
        animations.remove(id);
    }

    public void loadFromDir(File directory) {
        if (!directory.exists() || !directory.isDirectory() || directory.listFiles() == null) return;

        File[] files = directory.listFiles();
        for (File file : files) register(file.getName().replace(".yml", ""), new Animation(new IConfig(file)));
    }
}
