package org.satellite.dev.progiple.satemenus.self.menusCreator.converter;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.util.utilities.Utils;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class Converters {
    @Getter
    private final Set<IConverter> converters = new HashSet<>();

    public void register(IConverter converter) {
        converters.add(converter);
    }

    public void unregister(IConverter converter) {
        converters.remove(converter);
    }

    public IConverter find(String id) {
        return Utils.find(converters, c -> c.getId().equalsIgnoreCase(id)).orElse(null);
    }
}
