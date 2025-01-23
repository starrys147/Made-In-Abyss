package com.altnoir.mia.content.worldgen.centralholegen;

import com.altnoir.mia.Config;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

//@Mod.EventBusSubscriber(modid = MIA.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HoleGenerator {

    //public static final String ABYSS_HOLE_MARKER = "abyss_hole_marker.dat";
    public static final Logger LOGGER = LogUtils.getLogger();
    public List<HolePlacer> placers = new ArrayList<>(List.of());

    private HashMap<String, SimplexNoise> noiseGeneratorMap = new HashMap<>();
    @SubscribeEvent
    public void onChunkLoaded(ChunkEvent.Load event) {
        if(!event.getLevel().isClientSide() && event.isNewChunk()) {
            var chunk = event.getChunk();
            //MIA.LOGGER.debug(chunk.getPos().x + "," + chunk.getPos().z);
            var level = (ServerLevel)event.getLevel();
            var levelA = event.getLevel();
            String dimStr = level.dimension().location().toString();
            //MIA.LOGGER.debug(dimStr);
            if(Config.holeGeneratorConfig.has(dimStr)) {
                //MIA.LOGGER.debug("ok");
                JsonObject data = Config.holeGeneratorConfig.getAsJsonObject(dimStr);
                List<Pair<Integer, Integer>> vitalRads = new ArrayList<>(List.of());
                for(int h = levelA.getMinBuildHeight(); h < levelA.getMaxBuildHeight(); ++h) {
                    if(data.has(h + "")) {
                        vitalRads.add(new Pair<>(h, data.get(h + "").getAsInt()));
                    }
                }
                //MIA.LOGGER.debug("check point 1");
                List<Integer> rads = new ArrayList<>(List.of());
                int leftC = 0, rightC = 1;
                if(vitalRads.size() < 1) {
                    return;
                }
                //MIA.LOGGER.debug("check point 2");
                for (int h = levelA.getMinBuildHeight(); h < levelA.getMaxBuildHeight(); ++h) {
                    if (h < vitalRads.get(0).getFirst() || h > vitalRads.get(vitalRads.size() - 1).getFirst()) {
                        rads.add(0);
                    } else if (h == vitalRads.get(leftC).getFirst()) {
                        rads.add(vitalRads.get(leftC).getSecond());
                    } else if (h > vitalRads.get(leftC).getFirst() && h < vitalRads.get(rightC).getFirst()){
                        int r = vitalRads.get(leftC).getSecond() +
                                (vitalRads.get(rightC).getSecond() - vitalRads.get(leftC).getSecond()) *
                                        (h - vitalRads.get(leftC).getFirst()) / (vitalRads.get(rightC).getFirst() - vitalRads.get(leftC).getFirst());
                        rads.add(r);
                    } else if(h == vitalRads.get(rightC).getFirst()) {
                        rads.add(vitalRads.get(rightC).getSecond());
                        leftC = rightC; rightC++;
                    }
                }
                //MIA.LOGGER.debug("check point 3");

                placers.add(new HolePlacer(levelA, chunk, rads, data.has("mix_width")? data.get("mix_width").getAsInt() : 0, noiseGeneratorMap.get(dimStr)));
                //MIA.LOGGER.debug("check point 4");
            }
        }
    }

    @SubscribeEvent
    public void onServerTicked(TickEvent.ServerTickEvent event) {
        if(event.phase == TickEvent.Phase.END) {
            if (placers == null || placers.isEmpty()) return;
            LOGGER.debug(placers.size() + "");
            for (int i = placers.size() - 1; i >= 0; --i) {
                if (placers.get(i).tryPlace()) {
                    placers.remove(i);
                }
            }
        }
    }

    @SubscribeEvent
    public void onLevelLoad(LevelEvent.Load event) {
        if(!event.getLevel().isClientSide()) {
            var level = (ServerLevel) event.getLevel();
            noiseGeneratorMap.put(level.dimension().location().toString(), new SimplexNoise(level.random));
        }
    }
    @SubscribeEvent
    public void onLevelUnload(LevelEvent.Unload event) {
        if(!event.getLevel().isClientSide()) {
            var level = (ServerLevel) event.getLevel();
            String dimStr = level.dimension().location().toString();
            noiseGeneratorMap.remove(dimStr);
        }
    }

    public class HolePlacer {
        private Random random = new Random();

        public LevelAccessor level;
        public ChunkAccess chunk;
        public List<Integer> rList;
        private int mixWidth;
        private SimplexNoise noise;
        private static final double PI = 3.1416;

        public HolePlacer(LevelAccessor level, ChunkAccess chunk, List<Integer> rList, int mixWidth, @Nullable SimplexNoise noise) {
            this.level = level;
            this.chunk = chunk;
            this.rList = rList;
            this.mixWidth = mixWidth;
            random.setSeed(chunk.getPos().toLong());

            this.noise = noise == null? new SimplexNoise(level.getRandom()) : noise;
        }

        //true if successful
        public boolean tryPlace() {
            if(chunk.getStatus() == ChunkStatus.FULL) {
                int listOffset = -level.getMinBuildHeight();
                int xMin = chunk.getPos().getMinBlockX(), zMin = chunk.getPos().getMinBlockZ();
                for (int h = level.getMaxBuildHeight() - 1; h >= level.getMinBuildHeight(); --h) {
                    long r1 = rList.get(h + listOffset);
                    long r2 = (r1 - mixWidth) * (r1 - mixWidth);
                    long R2 = r1 * r1;
                    r2 = Math.min(R2, r2);
                    for (int x = xMin; x < xMin + 16; ++x) {
                        for (int z = zMin; z < zMin + 16; ++z) {
                            //MIA.LOGGER.debug("check point p[" + x + "," + z + "]");
                            var targetPos = new BlockPos(x, h, z);
                            long nowR2 = (long) x * x + (long) z * z;
                            if (nowR2 < r2) {
                                if(!level.getBlockState(targetPos).isAir())
                                    level.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 0);
                            } else if(nowR2 < R2) {
                                if(!level.getBlockState(targetPos).isAir()) {
                                    double nowR = Math.sqrt(nowR2);
                                    /*if(possibleBool((r1 - nowR) / (double)mixWidth)) {
                                        level.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 0);
                                    }*/
                                    double px = z == 0? ((x > 0)? PI : -PI) : 2.0 * (Math.atan((double)x / (double)z) + ((z < 0)? PI : 0.0));
                                    double nowMixWidth = Math.abs(noise.getValue(px, (double)h / 64.0)) * mixWidth;
                                    if(r1 - nowR > nowMixWidth) {
                                        level.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 0);
                                    }
                                }
                            }
                        }
                    }
                }
                return true;
                //MinecraftForge.EVENT_BUS.unregister(this);
            }
            return false;
        }

        private boolean possibleBool(double p) {
            return random.nextDouble() < p;
        }
    }
}

