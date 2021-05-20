package uk.joshiejack.penguinlib.client.helpers;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;

import java.util.List;
import java.util.Optional;

public abstract class BakedModelHelper {
    public static void buildCube(Direction side, TextureAtlasSprite sprite, List<BakedQuad> quads) {
        switch (side) {
            case DOWN:
                quads.add(buildQuad(Direction.DOWN, sprite,
                        0, 0, 1, sprite.getU0(), sprite.getV0(),
                        0, 0, 0, sprite.getU0(), sprite.getV1(),
                        1, 0, 0, sprite.getU1(), sprite.getV1(),
                        1, 0, 1, sprite.getU1(), sprite.getV0()
                ));
                break;
            case UP:
                quads.add(buildQuad(Direction.UP, sprite,
                        0, 1, 0, sprite.getU0(), sprite.getV0(),
                        0, 1, 1, sprite.getU0(), sprite.getV1(),
                        1, 1, 1, sprite.getU1(), sprite.getV1(),
                        1, 1, 0, sprite.getU1(), sprite.getV0()
                ));
                break;
            case NORTH:
                quads.add(buildQuad(Direction.NORTH, sprite,
                        1, 1, 0, sprite.getU0(), sprite.getV0(),
                        1, 0, 0, sprite.getU0(), sprite.getV1(),
                        0, 0, 0, sprite.getU1(), sprite.getV1(),
                        0, 1, 0, sprite.getU1(), sprite.getV0()
                ));
                break;
            case SOUTH:
                quads.add(buildQuad(Direction.SOUTH, sprite,
                        0, 1, 1, sprite.getU0(), sprite.getV0(),
                        0, 0, 1, sprite.getU0(), sprite.getV1(),
                        1, 0, 1, sprite.getU1(), sprite.getV1(),
                        1, 1, 1, sprite.getU1(), sprite.getV0()
                ));
                break;
            case WEST:
                quads.add(buildQuad(Direction.WEST, sprite,
                        0, 1, 0, sprite.getU0(), sprite.getV0(),
                        0, 0, 0, sprite.getU0(), sprite.getV1(),
                        0, 0, 1, sprite.getU1(), sprite.getV1(),
                        0, 1, 1, sprite.getU1(), sprite.getV0()
                ));
                break;
            case EAST:
                quads.add(buildQuad(Direction.EAST, sprite,
                        1, 1, 1, sprite.getU0(), sprite.getV0(),
                        1, 0, 1, sprite.getU0(), sprite.getV1(),
                        1, 0, 0, sprite.getU1(), sprite.getV1(),
                        1, 1, 0, sprite.getU1(), sprite.getV0()
                ));
                break;
        }
    }

    public static BakedQuad retexture(BakedQuad original, TextureAtlasSprite sprite) {
        int[] vertexData = original.getVertices();
        int[] newVertexData = new int[vertexData.length];
        System.arraycopy(vertexData, 0, newVertexData, 0, newVertexData.length);
        int vertexSizeInts = DefaultVertexFormats.BLOCK.getIntegerSize();
        Optional<VertexFormatElement> positionElement = DefaultVertexFormats.BLOCK.getElements().stream()
                .filter(e -> VertexFormatElement.Usage.UV.equals(e.getUsage())).findFirst();
        int positionOffset = positionElement.get().getIndex();
        for (int i = positionOffset; i < vertexData.length; i += vertexSizeInts) {
            newVertexData[i + 4] = Float.floatToRawIntBits(sprite.getU(unU(original.getSprite(), Float.intBitsToFloat(vertexData[i + 4]))));
            newVertexData[i + 5] = Float.floatToRawIntBits(sprite.getV(unV(original.getSprite(), Float.intBitsToFloat(vertexData[i + 5]))));
        }

        return new BakedQuad(newVertexData, 0, original.getDirection(), sprite, false);
    }

    private static float unU(TextureAtlasSprite sprite, float u) {
        return (u - sprite.getU0()) / (sprite.getU1() - sprite.getU0()) * 16.0F;
    }

    private static float unV(TextureAtlasSprite sprite, float v) {
        return (v - sprite.getV0()) / (sprite.getV1() - sprite.getV0()) * 16.0F;
    }

    public static BakedQuad buildQuad(Direction side, TextureAtlasSprite sprite,
                                  float x0, float y0, float z0, float u0, float v0,
                                  float x1, float y1, float z1, float u1, float v1,
                                  float x2, float y2, float z2, float u2, float v2,
                                  float x3, float y3, float z3, float u3, float v3) {
        BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
        builder.setApplyDiffuseLighting(true);
        builder.setContractUVs(true);
        builder.setQuadOrientation(side);
        putVertex(builder, side, x0, y0, z0, u0, v0);
        putVertex(builder, side, x1, y1, z1, u1, v1);
        putVertex(builder, side, x2, y2, z2, u2, v2);
        putVertex(builder, side, x3, y3, z3, u3, v3);
        return builder.build();
    }

    private static void putVertex(IVertexConsumer consumer, Direction side, float x, float y, float z, float u, float v) {
        ImmutableList<VertexFormatElement> elements = consumer.getVertexFormat().getElements();
        for (int e = 0; e <= elements.size() - 1; e++) {
            VertexFormatElement element = elements.get(e);
            switch (element.getUsage()) {
                case POSITION:
                    consumer.put(e, x, y, z, 1.0f);
                    break;
                case COLOR:
                    consumer.put(e, 1.0f, 1.0f, 1.0f, 1.0f);
                    break;
                case NORMAL:
                    float offX = (float) side.getStepX();
                    float offY = (float) side.getStepY();
                    float offZ = (float) side.getStepZ();
                    consumer.put(e, offX, offY, offZ, 0.0f);
                    break;
                case UV:
                    if (element.getIndex() == 0) {
                        consumer.put(e, u, v, 0f, 1f);
                        break;
                    }
                default:
                    consumer.put(e);
                    break;
            }
        }
    }
}
