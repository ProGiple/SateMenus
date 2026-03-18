package org.satellite.dev.progiple.satemenus.menus.params.animations;

import lombok.experimental.UtilityClass;
import org.novasparkle.lunaspring.API.configuration.IConfig;
import org.novasparkle.lunaspring.API.util.utilities.reflection.AnnotationScanner;
import org.novasparkle.lunaspring.API.util.utilities.reflection.ClassEntry;
import org.satellite.dev.progiple.satemenus.SateMenus;
import org.satellite.dev.progiple.satemenus.menus.params.animations.actions.AnimationAction;
import org.satellite.dev.progiple.satemenus.menus.params.animations.actions.impl.PlaceAction;
import org.satellite.dev.progiple.satemenus.menus.params.templates.Template;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@UtilityClass
public class Animations {
    private final Map<String, IAnimation> animations = new HashMap<>();
    private final Map<String, Class<?>> animationActions = new HashMap<>();
    private final Class<?> mainAnimationAction = PlaceAction.class;

    public Class<?> getAnimationAction(String name) {
        if (name != null) name = name.toLowerCase();
        return animationActions.getOrDefault(name, mainAnimationAction);
    }

    public void register(String actionId, Class<?> clazz) {
        animationActions.put(actionId, clazz);
    }

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
