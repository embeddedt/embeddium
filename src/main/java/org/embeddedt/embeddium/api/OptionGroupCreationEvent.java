package org.embeddedt.embeddium.api;

import me.jellysquid.mods.sodium.client.gui.options.Option;
import net.minecraft.resources.ResourceLocation;
import org.embeddedt.embeddium.api.eventbus.EmbeddiumEvent;
import org.embeddedt.embeddium.api.eventbus.EventHandlerRegistrar;

import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * Fired when an option group is created, to allow replacing options in that group if desired. (Can be used,
 * for instance, to extend the VSync or fullscreen options.)
 *
 * Adding new options to a group is not allowed, you must use {@link OptionPageCreationEvent} and create
 * a new group.
 */
public class OptionGroupCreationEvent extends EmbeddiumEvent {
    public static final EventHandlerRegistrar<OptionGroupCreationEvent> BUS = new EventHandlerRegistrar<>();

    private final ResourceLocation id;
    private final List<Option<?>> options;

    public OptionGroupCreationEvent(ResourceLocation id, List<Option<?>> options) {
        this.id = id;
        this.options = options;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    /**
     * Call to replace some or all options in the group with new instances.
     * You should check if the option group ID matches what you expect first, to avoid iterating options unnecessarily.
     * @param replacementFn should return a new option to use in place of the old one, return value must be non-null
     */
    public void replaceOptions(UnaryOperator<Option<?>> replacementFn) {
        options.replaceAll(option -> {
            Option<?> newOpt = replacementFn.apply(option);
            Objects.requireNonNull(newOpt);
            return newOpt;
        });
    }
}