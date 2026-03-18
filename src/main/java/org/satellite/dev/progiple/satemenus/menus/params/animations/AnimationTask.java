package org.satellite.dev.progiple.satemenus.menus.params.animations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.novasparkle.lunaspring.API.util.utilities.tasks.LunaTask;
import org.satellite.dev.progiple.satemenus.SateMenus;
import org.satellite.dev.progiple.satemenus.menus.menus.AnimatedMenu;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public class AnimationTask extends LunaTask {
    private final AnimationStage stage;
    private final AnimatedMenu menu;
    private final List<Integer> order;

    public AnimationTask(AnimationStage stage, AnimatedMenu menu) {
        this.stage = stage;
        this.menu = menu;
        this.order = new ArrayList<>(stage.getMap().keySet());
    }

    @Override @SuppressWarnings("all")
    @SneakyThrows
    public void start() {
        int previousTimeMillis = 0;
        for (int rawTimeMillis : order) {
            int timeMillis = rawTimeMillis - previousTimeMillis;
            previousTimeMillis = rawTimeMillis;

            if (timeMillis > 0) Thread.sleep(timeMillis);
            if (!stage.getAnimation().equals(menu.getPlayingAnimation())) return;

            this.stage.processTick(menu, rawTimeMillis);
        }

        this.stage.getAnimation().stop(menu);
    }

    public void handle() {
        order.sort(Comparator.comparingInt(i -> i));

        int first = order.get(0);
        if (first <= 0) {
            this.stage.processTick(menu, first);
        }

        order.removeIf(i -> i <= 0);
        this.runTaskAsynchronously(SateMenus.getInstance());
    }
}
