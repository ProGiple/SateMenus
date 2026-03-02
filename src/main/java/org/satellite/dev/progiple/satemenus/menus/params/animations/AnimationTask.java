package org.satellite.dev.progiple.satemenus.menus.params.animations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.novasparkle.lunaspring.API.util.utilities.tasks.LunaTask;
import org.satellite.dev.progiple.satemenus.menus.menus.AnimatedMenu;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor @Getter
public class AnimationTask extends LunaTask {
    private final AnimationStage stage;
    private final AnimatedMenu menu;

    @Override @SuppressWarnings("all")
    @SneakyThrows
    public void start() {
        List<Integer> order = new ArrayList<>(stage.getMap().keySet());
        order.sort(Comparator.comparingInt(i -> i));

        int previousTick = 0;
        for (int rawTick : order) {
            int tick = rawTick - previousTick;
            previousTick = rawTick;

            if (tick > 0) Thread.sleep(tick * 50L);
            if (!this.menu.getPlayingAnimations().contains(stage.getAnimation())) break;

            this.stage.processTick(menu, rawTick);
        }

        this.stage.getAnimation().stop(menu);
    }
}
