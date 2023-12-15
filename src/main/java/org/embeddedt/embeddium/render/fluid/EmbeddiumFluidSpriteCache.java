package org.embeddedt.embeddium.render.fluid;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

public class EmbeddiumFluidSpriteCache {
    // Cache the sprites array to avoid reallocating it on every call
    private final Sprite[] sprites = new Sprite[3];
    private final Object2ObjectOpenHashMap<Identifier, Sprite> spriteCache = new Object2ObjectOpenHashMap<>();

    private Sprite getTexture(Identifier identifier) {
        Sprite sprite = spriteCache.get(identifier);

        if (sprite == null) {
            sprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(identifier);
            spriteCache.put(identifier, sprite);
        }

        return sprite;
    }

    public Sprite[] getSprites(BlockRenderView world, BlockPos pos, FluidState fluidState) {
        sprites[0] = getTexture(fluidState.getFluid().getAttributes().getStillTexture(world, pos));
        sprites[1] = getTexture(fluidState.getFluid().getAttributes().getFlowingTexture(world, pos));
        Identifier overlay = fluidState.getFluid().getAttributes().getOverlayTexture();
        sprites[2] = overlay != null ? getTexture(overlay) : null;
        return sprites;
    }
}
