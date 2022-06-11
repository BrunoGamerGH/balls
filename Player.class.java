//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.minecraft.world.entity.player;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.ClickEvent.Action;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.Container;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.Entity.MovementEmission;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.EquipmentSlot.Type;
import net.minecraft.world.entity.LivingEntity.Fallsounds;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import net.minecraft.world.level.block.entity.JigsawBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_18_R1.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_18_R1.util.CraftVector;
import org.bukkit.entity.Item;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityExhaustionEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityExhaustionEvent.ExhaustionReason;
import org.bukkit.event.entity.EntityPotionEffectEvent.Cause;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

public abstract class Player extends LivingEntity {
    public static final String UUID_PREFIX_OFFLINE_PLAYER = "OfflinePlayer:";
    public static final int MAX_NAME_LENGTH = 16;
    public static final int MAX_HEALTH = 20;
    public static final int SLEEP_DURATION = 100;
    public static final int WAKE_UP_DURATION = 10;
    public static final int ENDER_SLOT_OFFSET = 200;
    public static final float CROUCH_BB_HEIGHT = 1.5F;
    public static final float SWIMMING_BB_WIDTH = 0.6F;
    public static final float SWIMMING_BB_HEIGHT = 0.6F;
    public static final float DEFAULT_EYE_HEIGHT = 1.62F;
    public static final EntityDimensions STANDING_DIMENSIONS = EntityDimensions.scalable(0.6F, 1.8F);
    private static final Map<Pose, EntityDimensions> POSES;
    private static final int FLY_ACHIEVEMENT_SPEED = 25;
    private static final EntityDataAccessor<Float> DATA_PLAYER_ABSORPTION_ID;
    private static final EntityDataAccessor<Integer> DATA_SCORE_ID;
    protected static final EntityDataAccessor<Byte> DATA_PLAYER_MODE_CUSTOMISATION;
    protected static final EntityDataAccessor<Byte> DATA_PLAYER_MAIN_HAND;
    protected static final EntityDataAccessor<CompoundTag> DATA_SHOULDER_LEFT;
    protected static final EntityDataAccessor<CompoundTag> DATA_SHOULDER_RIGHT;
    private long timeEntitySatOnShoulder;
    private final Inventory inventory = new Inventory(this);
    protected PlayerEnderChestContainer enderChestInventory = new PlayerEnderChestContainer(this);
    public final InventoryMenu inventoryMenu;
    public AbstractContainerMenu containerMenu;
    protected FoodData foodData = new FoodData(this);
    protected int jumpTriggerTime;
    public float oBob;
    public float bob;
    public int takeXpDelay;
    public double xCloakO;
    public double yCloakO;
    public double zCloakO;
    public double xCloak;
    public double yCloak;
    public double zCloak;
    public int sleepCounter;
    protected boolean wasUnderwater;
    private final Abilities abilities = new Abilities();
    public int experienceLevel;
    public int totalExperience;
    public float experienceProgress;
    protected int enchantmentSeed;
    protected final float defaultFlySpeed = 0.02F;
    private int lastLevelUpTime;
    private final GameProfile gameProfile;
    private boolean reducedDebugInfo;
    private ItemStack lastItemInMainHand;
    private final ItemCooldowns cooldowns;
    @Nullable
    public FishingHook fishing;
    public boolean fauxSleeping;
    public int oldLevel = -1;

    static {
        POSES = ImmutableMap.builder().put(Pose.STANDING, STANDING_DIMENSIONS).put(Pose.SLEEPING, SLEEPING_DIMENSIONS).put(Pose.FALL_FLYING, EntityDimensions.scalable(0.6F, 0.6F)).put(Pose.SWIMMING, EntityDimensions.scalable(0.6F, 0.6F)).put(Pose.SPIN_ATTACK, EntityDimensions.scalable(0.6F, 0.6F)).put(Pose.CROUCHING, EntityDimensions.scalable(0.6F, 1.5F)).put(Pose.DYING, EntityDimensions.fixed(0.2F, 0.2F)).build();
        DATA_PLAYER_ABSORPTION_ID = SynchedEntityData.defineId(Player.class, EntityDataSerializers.FLOAT);
        DATA_SCORE_ID = SynchedEntityData.defineId(Player.class, EntityDataSerializers.INT);
        DATA_PLAYER_MODE_CUSTOMISATION = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BYTE);
        DATA_PLAYER_MAIN_HAND = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BYTE);
        DATA_SHOULDER_LEFT = SynchedEntityData.defineId(Player.class, EntityDataSerializers.COMPOUND_TAG);
        DATA_SHOULDER_RIGHT = SynchedEntityData.defineId(Player.class, EntityDataSerializers.COMPOUND_TAG);
    }

    public CraftHumanEntity getBukkitEntity() {
        return (CraftHumanEntity)super.getBukkitEntity();
    }

    public Player(Level world, BlockPos blockposition, float f, GameProfile gameprofile) {
        super(EntityType.PLAYER, world);
        this.lastItemInMainHand = ItemStack.EMPTY;
        this.cooldowns = this.createItemCooldowns();
        this.setUUID(createPlayerUUID(gameprofile));
        this.gameProfile = gameprofile;
        this.inventoryMenu = new InventoryMenu(this.inventory, !world.isClientSide, this);
        this.containerMenu = this.inventoryMenu;
        this.moveTo((double)blockposition.getX() + 0.5D, (double)(blockposition.getY() + 1), (double)blockposition.getZ() + 0.5D, f, 0.0F);
        this.rotOffs = 180.0F;
    }

    public boolean blockActionRestricted(Level world, BlockPos blockposition, GameType enumgamemode) {
        if (!enumgamemode.isBlockPlacingRestricted()) {
            return false;
        } else if (enumgamemode == GameType.SPECTATOR) {
            return true;
        } else if (this.mayBuild()) {
            return false;
        } else {
            ItemStack itemstack = this.getMainHandItem();
            return itemstack.isEmpty() || !itemstack.hasAdventureModeBreakTagForBlock(world.getTagManager(), new BlockInWorld(world, blockposition, false));
        }
    }

    public static Builder createAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.ATTACK_DAMAGE, 1.0D).add(Attributes.MOVEMENT_SPEED, 0.10000000149011612D).add(Attributes.ATTACK_SPEED).add(Attributes.LUCK);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_PLAYER_ABSORPTION_ID, 0.0F);
        this.entityData.define(DATA_SCORE_ID, 0);
        this.entityData.define(DATA_PLAYER_MODE_CUSTOMISATION, (byte)0);
        this.entityData.define(DATA_PLAYER_MAIN_HAND, (byte)1);
        this.entityData.define(DATA_SHOULDER_LEFT, new CompoundTag());
        this.entityData.define(DATA_SHOULDER_RIGHT, new CompoundTag());
    }

    public void tick() {
        this.noPhysics = this.isSpectator();
        if (this.isSpectator()) {
            this.onGround = false;
        }

        if (this.takeXpDelay > 0) {
            --this.takeXpDelay;
        }

        if (this.isSleeping()) {
            ++this.sleepCounter;
            if (this.sleepCounter > 100) {
                this.sleepCounter = 100;
            }

            if (!this.level.isClientSide && this.level.isDay()) {
                this.stopSleepInBed(false, true);
            }
        } else if (this.sleepCounter > 0) {
            ++this.sleepCounter;
            if (this.sleepCounter >= 110) {
                this.sleepCounter = 0;
            }
        }

        this.updateIsUnderwater();
        super.tick();
        if (!this.level.isClientSide && this.containerMenu != null && !this.containerMenu.stillValid(this)) {
            this.closeContainer();
            this.containerMenu = this.inventoryMenu;
        }

        this.moveCloak();
        if (!this.level.isClientSide) {
            this.foodData.tick(this);
            this.awardStat(Stats.PLAY_TIME);
            this.awardStat(Stats.TOTAL_WORLD_TIME);
            if (this.isAlive()) {
                this.awardStat(Stats.TIME_SINCE_DEATH);
            }

            if (this.isDiscrete()) {
                this.awardStat(Stats.CROUCH_TIME);
            }

            if (!this.isSleeping()) {
                this.awardStat(Stats.TIME_SINCE_REST);
            }
        }

        int i = 29999999;
        double d0 = Mth.clamp(this.getX(), -2.9999999E7D, 2.9999999E7D);
        double d1 = Mth.clamp(this.getZ(), -2.9999999E7D, 2.9999999E7D);
        if (d0 != this.getX() || d1 != this.getZ()) {
            this.setPos(d0, this.getY(), d1);
        }

        ++this.attackStrengthTicker;
        ItemStack itemstack = this.getMainHandItem();
        if (!ItemStack.matches(this.lastItemInMainHand, itemstack)) {
            if (!ItemStack.isSameIgnoreDurability(this.lastItemInMainHand, itemstack)) {
                this.resetAttackStrengthTicker();
            }

            this.lastItemInMainHand = itemstack.copy();
        }

        this.turtleHelmetTick();
        this.cooldowns.tick();
        this.updatePlayerPose();
    }

    public boolean isSecondaryUseActive() {
        return this.isShiftKeyDown();
    }

    protected boolean wantsToStopRiding() {
        return this.isShiftKeyDown();
    }

    protected boolean isStayingOnGroundSurface() {
        return this.isShiftKeyDown();
    }

    protected boolean updateIsUnderwater() {
        this.wasUnderwater = this.isEyeInFluid(FluidTags.WATER);
        return this.wasUnderwater;
    }

    private void turtleHelmetTick() {
        ItemStack itemstack = this.getItemBySlot(EquipmentSlot.HEAD);
        if (itemstack.is(Items.TURTLE_HELMET) && !this.isEyeInFluid(FluidTags.WATER)) {
            this.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 200, 0, false, false, true), Cause.TURTLE_HELMET);
        }

    }

    protected ItemCooldowns createItemCooldowns() {
        return new ItemCooldowns();
    }

    private void moveCloak() {
        this.xCloakO = this.xCloak;
        this.yCloakO = this.yCloak;
        this.zCloakO = this.zCloak;
        double d0 = this.getX() - this.xCloak;
        double d1 = this.getY() - this.yCloak;
        double d2 = this.getZ() - this.zCloak;
        double d3 = 10.0D;
        if (d0 > 10.0D) {
            this.xCloak = this.getX();
            this.xCloakO = this.xCloak;
        }

        if (d2 > 10.0D) {
            this.zCloak = this.getZ();
            this.zCloakO = this.zCloak;
        }

        if (d1 > 10.0D) {
            this.yCloak = this.getY();
            this.yCloakO = this.yCloak;
        }

        if (d0 < -10.0D) {
            this.xCloak = this.getX();
            this.xCloakO = this.xCloak;
        }

        if (d2 < -10.0D) {
            this.zCloak = this.getZ();
            this.zCloakO = this.zCloak;
        }

        if (d1 < -10.0D) {
            this.yCloak = this.getY();
            this.yCloakO = this.yCloak;
        }

        this.xCloak += d0 * 0.25D;
        this.zCloak += d2 * 0.25D;
        this.yCloak += d1 * 0.25D;
    }

    protected void updatePlayerPose() {
        if (this.canEnterPose(Pose.SWIMMING)) {
            Pose entitypose;
            if (this.isFallFlying()) {
                entitypose = Pose.FALL_FLYING;
            } else if (this.isSleeping()) {
                entitypose = Pose.SLEEPING;
            } else if (this.isSwimming()) {
                entitypose = Pose.SWIMMING;
            } else if (this.isAutoSpinAttack()) {
                entitypose = Pose.SPIN_ATTACK;
            } else if (this.isShiftKeyDown() && !this.abilities.flying) {
                entitypose = Pose.CROUCHING;
            } else {
                entitypose = Pose.STANDING;
            }

            Pose entitypose1;
            if (!this.isSpectator() && !this.isPassenger() && !this.canEnterPose(entitypose)) {
                if (this.canEnterPose(Pose.CROUCHING)) {
                    entitypose1 = Pose.CROUCHING;
                } else {
                    entitypose1 = Pose.SWIMMING;
                }
            } else {
                entitypose1 = entitypose;
            }

            this.setPose(entitypose1);
        }

    }

    public int getPortalWaitTime() {
        return this.abilities.invulnerable ? 1 : 80;
    }

    protected SoundEvent getSwimSound() {
        return SoundEvents.PLAYER_SWIM;
    }

    protected SoundEvent getSwimSplashSound() {
        return SoundEvents.PLAYER_SPLASH;
    }

    protected SoundEvent getSwimHighSpeedSplashSound() {
        return SoundEvents.PLAYER_SPLASH_HIGH_SPEED;
    }

    public int getDimensionChangingDelay() {
        return 10;
    }

    public void playSound(SoundEvent soundeffect, float f, float f1) {
        this.level.playSound(this, this.getX(), this.getY(), this.getZ(), soundeffect, this.getSoundSource(), f, f1);
    }

    public void playNotifySound(SoundEvent soundeffect, SoundSource soundcategory, float f, float f1) {
    }

    public SoundSource getSoundSource() {
        return SoundSource.PLAYERS;
    }

    public int getFireImmuneTicks() {
        return 20;
    }

    public void handleEntityEvent(byte b0) {
        if (b0 == 9) {
            this.completeUsingItem();
        } else if (b0 == 23) {
            this.reducedDebugInfo = false;
        } else if (b0 == 22) {
            this.reducedDebugInfo = true;
        } else if (b0 == 43) {
            this.addParticlesAroundSelf(ParticleTypes.CLOUD);
        } else {
            super.handleEntityEvent(b0);
        }

    }

    private void addParticlesAroundSelf(ParticleOptions particleparam) {
        for(int i = 0; i < 5; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level.addParticle(particleparam, this.getRandomX(1.0D), this.getRandomY() + 1.0D, this.getRandomZ(1.0D), d0, d1, d2);
        }

    }

    public void closeContainer() {
        this.containerMenu = this.inventoryMenu;
    }

    public void rideTick() {
        if (!this.level.isClientSide && this.wantsToStopRiding() && this.isPassenger()) {
            this.stopRiding();
            this.setShiftKeyDown(false);
        } else {
            double d0 = this.getX();
            double d1 = this.getY();
            double d2 = this.getZ();
            super.rideTick();
            this.oBob = this.bob;
            this.bob = 0.0F;
            this.checkRidingStatistics(this.getX() - d0, this.getY() - d1, this.getZ() - d2);
        }

    }

    protected void serverAiStep() {
        super.serverAiStep();
        this.updateSwingTime();
        this.yHeadRot = this.getYRot();
    }

    public void aiStep() {
        if (this.jumpTriggerTime > 0) {
            --this.jumpTriggerTime;
        }

        if (this.level.getDifficulty() == Difficulty.PEACEFUL && this.level.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION)) {
            if (this.getHealth() < this.getMaxHealth() && this.tickCount % 20 == 0) {
                this.heal(1.0F, RegainReason.REGEN);
            }

            if (this.foodData.needsFood() && this.tickCount % 10 == 0) {
                this.foodData.setFoodLevel(this.foodData.getFoodLevel() + 1);
            }
        }

        this.inventory.tick();
        this.oBob = this.bob;
        super.aiStep();
        this.flyingSpeed = 0.02F;
        if (this.isSprinting()) {
            this.flyingSpeed = (float)((double)this.flyingSpeed + 0.005999999865889549D);
        }

        this.setSpeed((float)this.getAttributeValue(Attributes.MOVEMENT_SPEED));
        float f;
        if (this.onGround && !this.isDeadOrDying() && !this.isSwimming()) {
            f = Math.min(0.1F, (float)this.getDeltaMovement().horizontalDistance());
        } else {
            f = 0.0F;
        }

        this.bob += (f - this.bob) * 0.4F;
        if (this.getHealth() > 0.0F && !this.isSpectator()) {
            AABB axisalignedbb;
            if (this.isPassenger() && !this.getVehicle().isRemoved()) {
                axisalignedbb = this.getBoundingBox().minmax(this.getVehicle().getBoundingBox()).inflate(1.0D, 0.0D, 1.0D);
            } else {
                axisalignedbb = this.getBoundingBox().inflate(1.0D, 0.5D, 1.0D);
            }

            List<Entity> list = this.level.getEntities(this, axisalignedbb);
            List<Entity> list1 = Lists.newArrayList();

            for(int i = 0; i < list.size(); ++i) {
                Entity entity = (Entity)list.get(i);
                if (entity.getType() == EntityType.EXPERIENCE_ORB) {
                    list1.add(entity);
                } else if (!entity.isRemoved()) {
                    this.touch(entity);
                }
            }

            if (!list1.isEmpty()) {
                this.touch((Entity)Util.getRandom(list1, this.random));
            }
        }

        this.playShoulderEntityAmbientSound(this.getShoulderEntityLeft());
        this.playShoulderEntityAmbientSound(this.getShoulderEntityRight());
        if (!this.level.isClientSide && (this.fallDistance > 0.5F || this.isInWater()) || this.abilities.flying || this.isSleeping() || this.isInPowderSnow) {
            this.removeEntitiesOnShoulder();
        }

    }

    private void playShoulderEntityAmbientSound(@Nullable CompoundTag nbttagcompound) {
        if (nbttagcompound != null && (!nbttagcompound.contains("Silent") || !nbttagcompound.getBoolean("Silent")) && this.level.random.nextInt(200) == 0) {
            String s = nbttagcompound.getString("id");
            EntityType.byString(s).filter((entitytypes) -> {
                return entitytypes == EntityType.PARROT;
            }).ifPresent((entitytypes) -> {
                if (!Parrot.imitateNearbyMobs(this.level, this)) {
                    this.level.playSound((Player)null, this.getX(), this.getY(), this.getZ(), Parrot.getAmbient(this.level, this.level.random), this.getSoundSource(), 1.0F, Parrot.getPitch(this.level.random));
                }

            });
        }

    }

    private void touch(Entity entity) {
        entity.playerTouch(this);
    }

    public int getScore() {
        return (Integer)this.entityData.get(DATA_SCORE_ID);
    }

    public void setScore(int i) {
        this.entityData.set(DATA_SCORE_ID, i);
    }

    public void increaseScore(int i) {
        int j = this.getScore();
        this.entityData.set(DATA_SCORE_ID, j + i);
    }

    public void die(DamageSource damagesource) {
        super.die(damagesource);
        this.reapplyPosition();
        if (!this.isSpectator()) {
            this.dropAllDeathLoot(damagesource);
        }

        if (damagesource != null) {
            this.setDeltaMovement((double)(-Mth.cos((this.hurtDir + this.getYRot()) * 0.017453292F) * 0.1F), 0.10000000149011612D, (double)(-Mth.sin((this.hurtDir + this.getYRot()) * 0.017453292F) * 0.1F));
        } else {
            this.setDeltaMovement(0.0D, 0.1D, 0.0D);
        }

        this.awardStat(Stats.DEATHS);
        this.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_DEATH));
        this.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
        this.clearFire();
        this.setSharedFlagOnFire(false);
    }

    protected void dropEquipment() {
        super.dropEquipment();
        if (!this.level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
            this.destroyVanishingCursedItems();
            this.inventory.dropAll();
        }

    }

    protected void destroyVanishingCursedItems() {
        for(int i = 0; i < this.inventory.getContainerSize(); ++i) {
            ItemStack itemstack = this.inventory.getItem(i);
            if (!itemstack.isEmpty() && EnchantmentHelper.hasVanishingCurse(itemstack)) {
                this.inventory.removeItemNoUpdate(i);
            }
        }

    }

    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return damagesource == DamageSource.ON_FIRE ? SoundEvents.PLAYER_HURT_ON_FIRE : (damagesource == DamageSource.DROWN ? SoundEvents.PLAYER_HURT_DROWN : (damagesource == DamageSource.SWEET_BERRY_BUSH ? SoundEvents.PLAYER_HURT_SWEET_BERRY_BUSH : (damagesource == DamageSource.FREEZE ? SoundEvents.PLAYER_HURT_FREEZE : SoundEvents.PLAYER_HURT)));
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.PLAYER_DEATH;
    }

    @Nullable
    public ItemEntity drop(ItemStack itemstack, boolean flag) {
        return this.drop(itemstack, false, flag);
    }

    @Nullable
    public ItemEntity drop(ItemStack itemstack, boolean flag, boolean flag1) {
        if (itemstack.isEmpty()) {
            return null;
        } else {
            if (this.level.isClientSide) {
                this.swing(InteractionHand.MAIN_HAND);
            }

            double d0 = this.getEyeY() - 0.30000001192092896D;
            ItemEntity entityitem = new ItemEntity(this.level, this.getX(), d0, this.getZ(), itemstack);
            entityitem.setPickUpDelay(40);
            if (flag1) {
                entityitem.setThrower(this.getUUID());
            }

            float f;
            float f1;
            if (flag) {
                f = this.random.nextFloat() * 0.5F;
                f1 = this.random.nextFloat() * 6.2831855F;
                entityitem.setDeltaMovement((double)(-Mth.sin(f1) * f), 0.20000000298023224D, (double)(Mth.cos(f1) * f));
            } else {
                f = 0.3F;
                f1 = Mth.sin(this.getXRot() * 0.017453292F);
                float f2 = Mth.cos(this.getXRot() * 0.017453292F);
                float f3 = Mth.sin(this.getYRot() * 0.017453292F);
                float f4 = Mth.cos(this.getYRot() * 0.017453292F);
                float f5 = this.random.nextFloat() * 6.2831855F;
                float f6 = 0.02F * this.random.nextFloat();
                entityitem.setDeltaMovement((double)(-f3 * f2 * 0.3F) + Math.cos((double)f5) * (double)f6, (double)(-f1 * 0.3F + 0.1F + (this.random.nextFloat() - this.random.nextFloat()) * 0.1F), (double)(f4 * f2 * 0.3F) + Math.sin((double)f5) * (double)f6);
            }

            org.bukkit.entity.Player player = (org.bukkit.entity.Player)this.getBukkitEntity();
            Item drop = (Item)entityitem.getBukkitEntity();
            PlayerDropItemEvent event = new PlayerDropItemEvent(player, drop);
            this.level.getCraftServer().getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                return entityitem;
            } else {
                org.bukkit.inventory.ItemStack cur = player.getInventory().getItemInHand();
                if (flag1 && (cur == null || cur.getAmount() == 0)) {
                    player.getInventory().setItemInHand(drop.getItemStack());
                } else if (flag1 && cur.isSimilar(drop.getItemStack()) && cur.getAmount() < cur.getMaxStackSize() && drop.getItemStack().getAmount() == 1) {
                    cur.setAmount(cur.getAmount() + 1);
                    player.getInventory().setItemInHand(cur);
                } else {
                    player.getInventory().addItem(new org.bukkit.inventory.ItemStack[]{drop.getItemStack()});
                }

                return null;
            }
        }
    }

    public float getDestroySpeed(BlockState iblockdata) {
        float f = this.inventory.getDestroySpeed(iblockdata);
        if (f > 1.0F) {
            int i = EnchantmentHelper.getBlockEfficiency(this);
            ItemStack itemstack = this.getMainHandItem();
            if (i > 0 && !itemstack.isEmpty()) {
                f += (float)(i * i + 1);
            }
        }

        if (MobEffectUtil.hasDigSpeed(this)) {
            f *= 1.0F + (float)(MobEffectUtil.getDigSpeedAmplification(this) + 1) * 0.2F;
        }

        if (this.hasEffect(MobEffects.DIG_SLOWDOWN)) {
            float f1;
            switch(this.getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier()) {
            case 0:
                f1 = 0.3F;
                break;
            case 1:
                f1 = 0.09F;
                break;
            case 2:
                f1 = 0.0027F;
                break;
            case 3:
            default:
                f1 = 8.1E-4F;
            }

            f *= f1;
        }

        if (this.isEyeInFluid(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(this)) {
            f /= 5.0F;
        }

        if (!this.onGround) {
            f /= 5.0F;
        }

        return f;
    }

    public boolean hasCorrectToolForDrops(BlockState iblockdata) {
        return !iblockdata.requiresCorrectToolForDrops() || this.inventory.getSelected().isCorrectToolForDrops(iblockdata);
    }

    public void readAdditionalSaveData(CompoundTag nbttagcompound) {
        super.readAdditionalSaveData(nbttagcompound);
        this.setUUID(createPlayerUUID(this.gameProfile));
        ListTag nbttaglist = nbttagcompound.getList("Inventory", 10);
        this.inventory.load(nbttaglist);
        this.inventory.selected = nbttagcompound.getInt("SelectedItemSlot");
        this.sleepCounter = nbttagcompound.getShort("SleepTimer");
        this.experienceProgress = nbttagcompound.getFloat("XpP");
        this.experienceLevel = nbttagcompound.getInt("XpLevel");
        this.totalExperience = nbttagcompound.getInt("XpTotal");
        this.enchantmentSeed = nbttagcompound.getInt("XpSeed");
        if (this.enchantmentSeed == 0) {
            this.enchantmentSeed = this.random.nextInt();
        }

        this.setScore(nbttagcompound.getInt("Score"));
        this.foodData.readAdditionalSaveData(nbttagcompound);
        this.abilities.loadSaveData(nbttagcompound);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double)this.abilities.getWalkingSpeed());
        if (nbttagcompound.contains("EnderItems", 9)) {
            this.enderChestInventory.fromTag(nbttagcompound.getList("EnderItems", 10));
        }

        if (nbttagcompound.contains("ShoulderEntityLeft", 10)) {
            this.setShoulderEntityLeft(nbttagcompound.getCompound("ShoulderEntityLeft"));
        }

        if (nbttagcompound.contains("ShoulderEntityRight", 10)) {
            this.setShoulderEntityRight(nbttagcompound.getCompound("ShoulderEntityRight"));
        }

    }

    public void addAdditionalSaveData(CompoundTag nbttagcompound) {
        super.addAdditionalSaveData(nbttagcompound);
        nbttagcompound.putInt("DataVersion", SharedConstants.getCurrentVersion().getWorldVersion());
        nbttagcompound.put("Inventory", this.inventory.save(new ListTag()));
        nbttagcompound.putInt("SelectedItemSlot", this.inventory.selected);
        nbttagcompound.putShort("SleepTimer", (short)this.sleepCounter);
        nbttagcompound.putFloat("XpP", this.experienceProgress);
        nbttagcompound.putInt("XpLevel", this.experienceLevel);
        nbttagcompound.putInt("XpTotal", this.totalExperience);
        nbttagcompound.putInt("XpSeed", this.enchantmentSeed);
        nbttagcompound.putInt("Score", this.getScore());
        this.foodData.addAdditionalSaveData(nbttagcompound);
        this.abilities.addSaveData(nbttagcompound);
        nbttagcompound.put("EnderItems", this.enderChestInventory.createTag());
        if (!this.getShoulderEntityLeft().isEmpty()) {
            nbttagcompound.put("ShoulderEntityLeft", this.getShoulderEntityLeft());
        }

        if (!this.getShoulderEntityRight().isEmpty()) {
            nbttagcompound.put("ShoulderEntityRight", this.getShoulderEntityRight());
        }

    }

    public boolean isInvulnerableTo(DamageSource damagesource) {
        return super.isInvulnerableTo(damagesource) ? true : (damagesource == DamageSource.DROWN ? !this.level.getGameRules().getBoolean(GameRules.RULE_DROWNING_DAMAGE) : (damagesource.isFall() ? !this.level.getGameRules().getBoolean(GameRules.RULE_FALL_DAMAGE) : (damagesource.isFire() ? !this.level.getGameRules().getBoolean(GameRules.RULE_FIRE_DAMAGE) : (damagesource == DamageSource.FREEZE ? !this.level.getGameRules().getBoolean(GameRules.RULE_FREEZE_DAMAGE) : false))));
    }

    public boolean hurt(DamageSource damagesource, float f) {
        if (this.isInvulnerableTo(damagesource)) {
            return false;
        } else if (this.abilities.invulnerable && !damagesource.isBypassInvul()) {
            return false;
        } else {
            this.noActionTime = 0;
            if (this.isDeadOrDying()) {
                return false;
            } else {
                if (damagesource.scalesWithDifficulty()) {
                    if (this.level.getDifficulty() == Difficulty.PEACEFUL) {
                        return false;
                    }

                    if (this.level.getDifficulty() == Difficulty.EASY) {
                        f = Math.min(f / 2.0F + 1.0F, f);
                    }

                    if (this.level.getDifficulty() == Difficulty.HARD) {
                        f = f * 3.0F / 2.0F;
                    }
                }

                boolean damaged = super.hurt(damagesource, f);
                if (damaged) {
                    this.removeEntitiesOnShoulder();
                }

                return damaged;
            }
        }
    }

    protected void blockUsingShield(LivingEntity entityliving) {
        super.blockUsingShield(entityliving);
        if (entityliving.getMainHandItem().getItem() instanceof AxeItem) {
            this.disableShield(true);
        }

    }

    public boolean canBeSeenAsEnemy() {
        return !this.getAbilities().invulnerable && super.canBeSeenAsEnemy();
    }

    public boolean canHarmPlayer(Player entityhuman) {
        Team team;
        if (entityhuman instanceof ServerPlayer) {
            ServerPlayer thatPlayer = (ServerPlayer)entityhuman;
            team = thatPlayer.getBukkitEntity().getScoreboard().getPlayerTeam(thatPlayer.getBukkitEntity());
            if (team == null || team.allowFriendlyFire()) {
                return true;
            }
        } else {
            OfflinePlayer thisPlayer = entityhuman.level.getCraftServer().getOfflinePlayer(entityhuman.getScoreboardName());
            team = entityhuman.level.getCraftServer().getScoreboardManager().getMainScoreboard().getPlayerTeam(thisPlayer);
            if (team == null || team.allowFriendlyFire()) {
                return true;
            }
        }

        if (this instanceof ServerPlayer) {
            return !team.hasPlayer(((ServerPlayer)this).getBukkitEntity());
        } else {
            return !team.hasPlayer(this.level.getCraftServer().getOfflinePlayer(this.getScoreboardName()));
        }
    }

    protected void hurtArmor(DamageSource damagesource, float f) {
        this.inventory.hurtArmor(damagesource, f, Inventory.ALL_ARMOR_SLOTS);
    }

    protected void hurtHelmet(DamageSource damagesource, float f) {
        this.inventory.hurtArmor(damagesource, f, Inventory.HELMET_SLOT_ONLY);
    }

    protected void hurtCurrentlyUsedShield(float f) {
        if (this.useItem.is(Items.SHIELD)) {
            if (!this.level.isClientSide) {
                this.awardStat(Stats.ITEM_USED.get(this.useItem.getItem()));
            }

            if (f >= 3.0F) {
                int i = 1 + Mth.floor(f);
                InteractionHand enumhand = this.getUsedItemHand();
                this.useItem.hurtAndBreak(i, this, (entityhuman) -> {
                    entityhuman.broadcastBreakEvent(enumhand);
                });
                if (this.useItem.isEmpty()) {
                    if (enumhand == InteractionHand.MAIN_HAND) {
                        this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    } else {
                        this.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                    }

                    this.useItem = ItemStack.EMPTY;
                    this.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + this.level.random.nextFloat() * 0.4F);
                }
            }
        }

    }

    protected boolean damageEntity0(DamageSource damagesource, float f) {
        return super.damageEntity0(damagesource, f);
    }

    protected boolean onSoulSpeedBlock() {
        return !this.abilities.flying && super.onSoulSpeedBlock();
    }

    public void openTextEdit(SignBlockEntity tileentitysign) {
    }

    public void openMinecartCommandBlock(BaseCommandBlock commandblocklistenerabstract) {
    }

    public void openCommandBlock(CommandBlockEntity tileentitycommand) {
    }

    public void openStructureBlock(StructureBlockEntity tileentitystructure) {
    }

    public void openJigsawBlock(JigsawBlockEntity tileentityjigsaw) {
    }

    public void openHorseInventory(AbstractHorse entityhorseabstract, Container iinventory) {
    }

    public OptionalInt openMenu(@Nullable MenuProvider itileinventory) {
        return OptionalInt.empty();
    }

    public void sendMerchantOffers(int i, MerchantOffers merchantrecipelist, int j, int k, boolean flag, boolean flag1) {
    }

    public void openItemGui(ItemStack itemstack, InteractionHand enumhand) {
    }

    public InteractionResult interactOn(Entity entity, InteractionHand enumhand) {
        if (this.isSpectator()) {
            if (entity instanceof MenuProvider) {
                this.openMenu((MenuProvider)entity);
            }

            return InteractionResult.PASS;
        } else {
            ItemStack itemstack = this.getItemInHand(enumhand);
            ItemStack itemstack1 = itemstack.copy();
            InteractionResult enuminteractionresult = entity.interact(this, enumhand);
            if (enuminteractionresult.consumesAction()) {
                if (this.abilities.instabuild && itemstack == this.getItemInHand(enumhand) && itemstack.getCount() < itemstack1.getCount()) {
                    itemstack.setCount(itemstack1.getCount());
                }

                return enuminteractionresult;
            } else {
                if (!itemstack.isEmpty() && entity instanceof LivingEntity) {
                    if (this.abilities.instabuild) {
                        itemstack = itemstack1;
                    }

                    InteractionResult enuminteractionresult1 = itemstack.interactLivingEntity(this, (LivingEntity)entity, enumhand);
                    if (enuminteractionresult1.consumesAction()) {
                        if (itemstack.isEmpty() && !this.abilities.instabuild) {
                            this.setItemInHand(enumhand, ItemStack.EMPTY);
                        }

                        return enuminteractionresult1;
                    }
                }

                return InteractionResult.PASS;
            }
        }
    }

    public double getMyRidingOffset() {
        return -0.35D;
    }

    public void removeVehicle() {
        super.removeVehicle();
        this.boardingCooldown = 0;
    }

    protected boolean isImmobile() {
        return super.isImmobile() || this.isSleeping();
    }

    public boolean isAffectedByFluids() {
        return !this.abilities.flying;
    }

    protected Vec3 maybeBackOffFromEdge(Vec3 vec3d, MoverType enummovetype) {
        if (!this.abilities.flying && (enummovetype == MoverType.SELF || enummovetype == MoverType.PLAYER) && this.isStayingOnGroundSurface() && this.isAboveGround()) {
            double d0 = vec3d.x;
            double d1 = vec3d.z;
            double var7 = 0.05D;

            while(true) {
                while(d0 != 0.0D && this.level.noCollision(this, this.getBoundingBox().move(d0, (double)(-this.maxUpStep), 0.0D))) {
                    if (d0 < 0.05D && d0 >= -0.05D) {
                        d0 = 0.0D;
                    } else if (d0 > 0.0D) {
                        d0 -= 0.05D;
                    } else {
                        d0 += 0.05D;
                    }
                }

                while(true) {
                    while(d1 != 0.0D && this.level.noCollision(this, this.getBoundingBox().move(0.0D, (double)(-this.maxUpStep), d1))) {
                        if (d1 < 0.05D && d1 >= -0.05D) {
                            d1 = 0.0D;
                        } else if (d1 > 0.0D) {
                            d1 -= 0.05D;
                        } else {
                            d1 += 0.05D;
                        }
                    }

                    while(true) {
                        while(d0 != 0.0D && d1 != 0.0D && this.level.noCollision(this, this.getBoundingBox().move(d0, (double)(-this.maxUpStep), d1))) {
                            if (d0 < 0.05D && d0 >= -0.05D) {
                                d0 = 0.0D;
                            } else if (d0 > 0.0D) {
                                d0 -= 0.05D;
                            } else {
                                d0 += 0.05D;
                            }

                            if (d1 < 0.05D && d1 >= -0.05D) {
                                d1 = 0.0D;
                            } else if (d1 > 0.0D) {
                                d1 -= 0.05D;
                            } else {
                                d1 += 0.05D;
                            }
                        }

                        vec3d = new Vec3(d0, vec3d.y, d1);
                        return vec3d;
                    }
                }
            }
        } else {
            return vec3d;
        }
    }

    private boolean isAboveGround() {
        return this.onGround || this.fallDistance < this.maxUpStep && !this.level.noCollision(this, this.getBoundingBox().move(0.0D, (double)(this.fallDistance - this.maxUpStep), 0.0D));
    }

    public void attack(Entity entity) {
        if (entity.isAttackable() && !entity.skipAttackInteraction(this)) {
            float f = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
            float f1;
            if (entity instanceof LivingEntity) {
                f1 = EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity)entity).getMobType());
            } else {
                f1 = EnchantmentHelper.getDamageBonus(this.getMainHandItem(), MobType.UNDEFINED);
            }

            float f2 = this.getAttackStrengthScale(0.5F);
            f *= 0.2F + f2 * f2 * 0.8F;
            f1 *= f2;
            if (f > 0.0F || f1 > 0.0F) {
                boolean flag = f2 > 0.9F;
                boolean flag1 = false;
                byte b0 = 0;
                int i = b0 + EnchantmentHelper.getKnockbackBonus(this);
                if (this.isSprinting() && flag) {
                    this.level.playSound((Player)null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, this.getSoundSource(), 1.0F, 1.0F);
                    ++i;
                    flag1 = true;
                }

                boolean flag2 = flag && this.fallDistance > 0.0F && !this.onGround && !this.onClimbable() && !this.isInWater() && !this.hasEffect(MobEffects.BLINDNESS) && !this.isPassenger() && entity instanceof LivingEntity;
                flag2 = flag2 && !this.isSprinting();
                if (flag2) {
                    f *= 1.5F;
                }

                f += f1;
                boolean flag3 = false;
                double d0 = (double)(this.walkDist - this.walkDistO);
                if (flag && !flag2 && !flag1 && this.onGround && d0 < (double)this.getSpeed()) {
                    ItemStack itemstack = this.getItemInHand(InteractionHand.MAIN_HAND);
                    if (itemstack.getItem() instanceof SwordItem) {
                        flag3 = true;
                    }
                }

                float f3 = 0.0F;
                boolean flag4 = false;
                int j = EnchantmentHelper.getFireAspect(this);
                if (entity instanceof LivingEntity) {
                    f3 = ((LivingEntity)entity).getHealth();
                    if (j > 0 && !entity.isOnFire()) {
                        EntityCombustByEntityEvent combustEvent = new EntityCombustByEntityEvent(this.getBukkitEntity(), entity.getBukkitEntity(), 1);
                        Bukkit.getPluginManager().callEvent(combustEvent);
                        if (!combustEvent.isCancelled()) {
                            flag4 = true;
                            entity.setSecondsOnFire(combustEvent.getDuration(), false);
                        }
                    }
                }

                Vec3 vec3d = entity.getDeltaMovement();
                boolean flag5 = entity.hurt(DamageSource.playerAttack(this), f);
                if (flag5) {
                    if (i > 0) {
                        if (entity instanceof LivingEntity) {
                            ((LivingEntity)entity).knockback((double)((float)i * 0.5F), (double)Mth.sin(this.getYRot() * 0.017453292F), (double)(-Mth.cos(this.getYRot() * 0.017453292F)));
                        } else {
                            entity.push((double)(-Mth.sin(this.getYRot() * 0.017453292F) * (float)i * 0.5F), 0.1D, (double)(Mth.cos(this.getYRot() * 0.017453292F) * (float)i * 0.5F));
                        }

                        this.setDeltaMovement(this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                        this.setSprinting(false);
                    }

                    if (flag3) {
                        float f4 = 1.0F + EnchantmentHelper.getSweepingDamageRatio(this) * f;
                        List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(1.0D, 0.25D, 1.0D));
                        Iterator iterator = list.iterator();

                        label179:
                        while(true) {
                            LivingEntity entityliving;
                            do {
                                do {
                                    do {
                                        do {
                                            if (!iterator.hasNext()) {
                                                this.level.playSound((Player)null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, this.getSoundSource(), 1.0F, 1.0F);
                                                this.sweepAttack();
                                                break label179;
                                            }

                                            entityliving = (LivingEntity)iterator.next();
                                        } while(entityliving == this);
                                    } while(entityliving == entity);
                                } while(this.isAlliedTo(entityliving));
                            } while(entityliving instanceof ArmorStand && ((ArmorStand)entityliving).isMarker());

                            if (this.distanceToSqr(entityliving) < 9.0D && entityliving.hurt(DamageSource.playerAttack(this).sweep(), f4)) {
                                entityliving.knockback(0.4000000059604645D, (double)Mth.sin(this.getYRot() * 0.017453292F), (double)(-Mth.cos(this.getYRot() * 0.017453292F)));
                            }
                        }
                    }

                    if (entity instanceof ServerPlayer && entity.hurtMarked) {
                        boolean cancelled = false;
                        org.bukkit.entity.Player player = (org.bukkit.entity.Player)entity.getBukkitEntity();
                        Vector velocity = CraftVector.toBukkit(vec3d);
                        PlayerVelocityEvent event = new PlayerVelocityEvent(player, velocity.clone());
                        this.level.getCraftServer().getPluginManager().callEvent(event);
                        if (event.isCancelled()) {
                            cancelled = true;
                        } else if (!velocity.equals(event.getVelocity())) {
                            player.setVelocity(event.getVelocity());
                        }

                        if (!cancelled) {
                            ((ServerPlayer)entity).connection.send(new ClientboundSetEntityMotionPacket(entity));
                            entity.hurtMarked = false;
                            entity.setDeltaMovement(vec3d);
                        }
                    }

                    if (flag2) {
                        this.level.playSound((Player)null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, this.getSoundSource(), 1.0F, 1.0F);
                        this.crit(entity);
                    }

                    if (!flag2 && !flag3) {
                        if (flag) {
                            this.level.playSound((Player)null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_ATTACK_STRONG, this.getSoundSource(), 1.0F, 1.0F);
                        } else {
                            this.level.playSound((Player)null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_ATTACK_WEAK, this.getSoundSource(), 1.0F, 1.0F);
                        }
                    }

                    if (f1 > 0.0F) {
                        this.magicCrit(entity);
                    }

                    this.setLastHurtMob(entity);
                    if (entity instanceof LivingEntity) {
                        EnchantmentHelper.doPostHurtEffects((LivingEntity)entity, this);
                    }

                    EnchantmentHelper.doPostDamageEffects(this, entity);
                    ItemStack itemstack1 = this.getMainHandItem();
                    Object object = entity;
                    if (entity instanceof EnderDragonPart) {
                        object = ((EnderDragonPart)entity).parentMob;
                    }

                    if (!this.level.isClientSide && !itemstack1.isEmpty() && object instanceof LivingEntity) {
                        itemstack1.hurtEnemy((LivingEntity)object, this);
                        if (itemstack1.isEmpty()) {
                            this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                        }
                    }

                    if (entity instanceof LivingEntity) {
                        float f5 = f3 - ((LivingEntity)entity).getHealth();
                        this.awardStat(Stats.DAMAGE_DEALT, Math.round(f5 * 10.0F));
                        if (j > 0) {
                            EntityCombustByEntityEvent combustEvent = new EntityCombustByEntityEvent(this.getBukkitEntity(), entity.getBukkitEntity(), j * 4);
                            Bukkit.getPluginManager().callEvent(combustEvent);
                            if (!combustEvent.isCancelled()) {
                                entity.setSecondsOnFire(combustEvent.getDuration(), false);
                            }
                        }

                        if (this.level instanceof ServerLevel && f5 > 2.0F) {
                            int k = (int)((double)f5 * 0.5D);
                            ((ServerLevel)this.level).sendParticles(ParticleTypes.DAMAGE_INDICATOR, entity.getX(), entity.getY(0.5D), entity.getZ(), k, 0.1D, 0.0D, 0.1D, 0.2D);
                        }
                    }

                    this.causeFoodExhaustion(this.level.spigotConfig.combatExhaustion, ExhaustionReason.ATTACK);
                } else {
                    this.level.playSound((Player)null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_ATTACK_NODAMAGE, this.getSoundSource(), 1.0F, 1.0F);
                    if (flag4) {
                        entity.clearFire();
                    }

                    if (this instanceof ServerPlayer) {
                        ((ServerPlayer)this).getBukkitEntity().updateInventory();
                    }
                }
            }
        }

    }

    protected void doAutoAttackOnTouch(LivingEntity entityliving) {
        this.attack(entityliving);
    }

    public void disableShield(boolean flag) {
        float f = 0.25F + (float)EnchantmentHelper.getBlockEfficiency(this) * 0.05F;
        if (flag) {
            f += 0.75F;
        }

        if (this.random.nextFloat() < f) {
            this.getCooldowns().addCooldown(Items.SHIELD, 100);
            this.stopUsingItem();
            this.level.broadcastEntityEvent(this, (byte)30);
        }

    }

    public void crit(Entity entity) {
    }

    public void magicCrit(Entity entity) {
    }

    public void sweepAttack() {
        double d0 = (double)(-Mth.sin(this.getYRot() * 0.017453292F));
        double d1 = (double)Mth.cos(this.getYRot() * 0.017453292F);
        if (this.level instanceof ServerLevel) {
            ((ServerLevel)this.level).sendParticles(ParticleTypes.SWEEP_ATTACK, this.getX() + d0, this.getY(0.5D), this.getZ() + d1, 0, d0, 0.0D, d1, 0.0D);
        }

    }

    public void respawn() {
    }

    public void remove(RemovalReason entity_removalreason) {
        super.remove(entity_removalreason);
        this.inventoryMenu.removed(this);
        if (this.containerMenu != null) {
            this.containerMenu.removed(this);
        }

    }

    public boolean isLocalPlayer() {
        return false;
    }

    public GameProfile getGameProfile() {
        return this.gameProfile;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public Abilities getAbilities() {
        return this.abilities;
    }

    public void updateTutorialInventoryAction(ItemStack itemstack, ItemStack itemstack1, ClickAction clickaction) {
    }

    public Either<Player.BedSleepingProblem, Unit> startSleepInBed(BlockPos blockposition) {
        return this.startSleepInBed(blockposition, false);
    }

    public Either<Player.BedSleepingProblem, Unit> startSleepInBed(BlockPos blockposition, boolean force) {
        this.startSleeping(blockposition);
        this.sleepCounter = 0;
        return Either.right(Unit.INSTANCE);
    }

    public void stopSleepInBed(boolean flag, boolean flag1) {
        super.stopSleeping();
        if (this.level instanceof ServerLevel && flag1) {
            ((ServerLevel)this.level).updateSleepingPlayerList();
        }

        this.sleepCounter = flag ? 0 : 100;
    }

    public void stopSleeping() {
        this.stopSleepInBed(true, true);
    }

    public static Optional<Vec3> findRespawnPositionAndUseSpawnBlock(ServerLevel worldserver, BlockPos blockposition, float f, boolean flag, boolean flag1) {
        BlockState iblockdata = worldserver.getBlockState(blockposition);
        Block block = iblockdata.getBlock();
        if (block instanceof RespawnAnchorBlock && (Integer)iblockdata.getValue(RespawnAnchorBlock.CHARGE) > 0 && RespawnAnchorBlock.canSetSpawn(worldserver)) {
            Optional<Vec3> optional = RespawnAnchorBlock.findStandUpPosition(EntityType.PLAYER, worldserver, blockposition);
            if (!flag1 && optional.isPresent()) {
                worldserver.setBlock(blockposition, (BlockState)iblockdata.setValue(RespawnAnchorBlock.CHARGE, (Integer)iblockdata.getValue(RespawnAnchorBlock.CHARGE) - 1), 3);
            }

            return optional;
        } else if (block instanceof BedBlock && BedBlock.canSetSpawn(worldserver)) {
            return BedBlock.findStandUpPosition(EntityType.PLAYER, worldserver, blockposition, f);
        } else if (!flag) {
            return Optional.empty();
        } else {
            boolean flag2 = block.isPossibleToRespawnInThis();
            boolean flag3 = worldserver.getBlockState(blockposition.above()).getBlock().isPossibleToRespawnInThis();
            return flag2 && flag3 ? Optional.of(new Vec3((double)blockposition.getX() + 0.5D, (double)blockposition.getY() + 0.1D, (double)blockposition.getZ() + 0.5D)) : Optional.empty();
        }
    }

    public boolean isSleepingLongEnough() {
        return this.isSleeping() && this.sleepCounter >= 100;
    }

    public int getSleepTimer() {
        return this.sleepCounter;
    }

    public void displayClientMessage(Component ichatbasecomponent, boolean flag) {
    }

    public void awardStat(ResourceLocation minecraftkey) {
        this.awardStat(Stats.CUSTOM.get(minecraftkey));
    }

    public void awardStat(ResourceLocation minecraftkey, int i) {
        this.awardStat(Stats.CUSTOM.get(minecraftkey), i);
    }

    public void awardStat(Stat<?> statistic) {
        this.awardStat((Stat)statistic, 1);
    }

    public void awardStat(Stat<?> statistic, int i) {
    }

    public void resetStat(Stat<?> statistic) {
    }

    public int awardRecipes(Collection<Recipe<?>> collection) {
        return 0;
    }

    public void awardRecipesByKey(ResourceLocation[] aminecraftkey) {
    }

    public int resetRecipes(Collection<Recipe<?>> collection) {
        return 0;
    }

    public void jumpFromGround() {
        super.jumpFromGround();
        this.awardStat(Stats.JUMP);
        if (this.isSprinting()) {
            this.causeFoodExhaustion(this.level.spigotConfig.jumpSprintExhaustion, ExhaustionReason.JUMP_SPRINT);
        } else {
            this.causeFoodExhaustion(this.level.spigotConfig.jumpWalkExhaustion, ExhaustionReason.JUMP);
        }

    }

    public void travel(Vec3 vec3d) {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        double d3;
        if (this.isSwimming() && !this.isPassenger()) {
            d3 = this.getLookAngle().y;
            double d4 = d3 < -0.2D ? 0.085D : 0.06D;
            if (d3 <= 0.0D || this.jumping || !this.level.getBlockState(new BlockPos(this.getX(), this.getY() + 1.0D - 0.1D, this.getZ())).getFluidState().isEmpty()) {
                Vec3 vec3d1 = this.getDeltaMovement();
                this.setDeltaMovement(vec3d1.add(0.0D, (d3 - vec3d1.y) * d4, 0.0D));
            }
        }

        if (this.abilities.flying && !this.isPassenger()) {
            d3 = this.getDeltaMovement().y;
            float f = this.flyingSpeed;
            this.flyingSpeed = this.abilities.getFlyingSpeed() * (float)(this.isSprinting() ? 2 : 1);
            super.travel(vec3d);
            Vec3 vec3d2 = this.getDeltaMovement();
            this.setDeltaMovement(vec3d2.x, d3 * 0.6D, vec3d2.z);
            this.flyingSpeed = f;
            this.resetFallDistance();
            if (this.getSharedFlag(7) && !CraftEventFactory.callToggleGlideEvent(this, false).isCancelled()) {
                this.setSharedFlag(7, false);
            }
        } else {
            super.travel(vec3d);
        }

        this.checkMovementStatistics(this.getX() - d0, this.getY() - d1, this.getZ() - d2);
    }

    public void updateSwimming() {
        if (this.abilities.flying) {
            this.setSwimming(false);
        } else {
            super.updateSwimming();
        }

    }

    protected boolean freeAt(BlockPos blockposition) {
        return !this.level.getBlockState(blockposition).isSuffocating(this.level, blockposition);
    }

    public float getSpeed() {
        return (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED);
    }

    public void checkMovementStatistics(double d0, double d1, double d2) {
        if (!this.isPassenger()) {
            int i;
            if (this.isSwimming()) {
                i = Math.round((float)Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2) * 100.0F);
                if (i > 0) {
                    this.awardStat(Stats.SWIM_ONE_CM, i);
                    this.causeFoodExhaustion(this.level.spigotConfig.swimMultiplier * (float)i * 0.01F, ExhaustionReason.SWIM);
                }
            } else if (this.isEyeInFluid(FluidTags.WATER)) {
                i = Math.round((float)Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2) * 100.0F);
                if (i > 0) {
                    this.awardStat(Stats.WALK_UNDER_WATER_ONE_CM, i);
                    this.causeFoodExhaustion(this.level.spigotConfig.swimMultiplier * (float)i * 0.01F, ExhaustionReason.WALK_UNDERWATER);
                }
            } else if (this.isInWater()) {
                i = Math.round((float)Math.sqrt(d0 * d0 + d2 * d2) * 100.0F);
                if (i > 0) {
                    this.awardStat(Stats.WALK_ON_WATER_ONE_CM, i);
                    this.causeFoodExhaustion(this.level.spigotConfig.swimMultiplier * (float)i * 0.01F, ExhaustionReason.WALK_ON_WATER);
                }
            } else if (this.onClimbable()) {
                if (d1 > 0.0D) {
                    this.awardStat(Stats.CLIMB_ONE_CM, (int)Math.round(d1 * 100.0D));
                }
            } else if (this.onGround) {
                i = Math.round((float)Math.sqrt(d0 * d0 + d2 * d2) * 100.0F);
                if (i > 0) {
                    if (this.isSprinting()) {
                        this.awardStat(Stats.SPRINT_ONE_CM, i);
                        this.causeFoodExhaustion(this.level.spigotConfig.sprintMultiplier * (float)i * 0.01F, ExhaustionReason.SPRINT);
                    } else if (this.isCrouching()) {
                        this.awardStat(Stats.CROUCH_ONE_CM, i);
                        this.causeFoodExhaustion(this.level.spigotConfig.otherMultiplier * (float)i * 0.01F, ExhaustionReason.CROUCH);
                    } else {
                        this.awardStat(Stats.WALK_ONE_CM, i);
                        this.causeFoodExhaustion(this.level.spigotConfig.otherMultiplier * (float)i * 0.01F, ExhaustionReason.WALK);
                    }
                }
            } else if (this.isFallFlying()) {
                i = Math.round((float)Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2) * 100.0F);
                this.awardStat(Stats.AVIATE_ONE_CM, i);
            } else {
                i = Math.round((float)Math.sqrt(d0 * d0 + d2 * d2) * 100.0F);
                if (i > 25) {
                    this.awardStat(Stats.FLY_ONE_CM, i);
                }
            }
        }

    }

    private void checkRidingStatistics(double d0, double d1, double d2) {
        if (this.isPassenger()) {
            int i = Math.round((float)Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2) * 100.0F);
            if (i > 0) {
                Entity entity = this.getVehicle();
                if (entity instanceof AbstractMinecart) {
                    this.awardStat(Stats.MINECART_ONE_CM, i);
                } else if (entity instanceof Boat) {
                    this.awardStat(Stats.BOAT_ONE_CM, i);
                } else if (entity instanceof Pig) {
                    this.awardStat(Stats.PIG_ONE_CM, i);
                } else if (entity instanceof AbstractHorse) {
                    this.awardStat(Stats.HORSE_ONE_CM, i);
                } else if (entity instanceof Strider) {
                    this.awardStat(Stats.STRIDER_ONE_CM, i);
                }
            }
        }

    }

    public boolean causeFallDamage(float f, float f1, DamageSource damagesource) {
        if (this.abilities.mayfly) {
            return false;
        } else {
            if (f >= 2.0F) {
                this.awardStat(Stats.FALL_ONE_CM, (int)Math.round((double)f * 100.0D));
            }

            return super.causeFallDamage(f, f1, damagesource);
        }
    }

    public boolean tryToStartFallFlying() {
        if (!this.onGround && !this.isFallFlying() && !this.isInWater() && !this.hasEffect(MobEffects.LEVITATION)) {
            ItemStack itemstack = this.getItemBySlot(EquipmentSlot.CHEST);
            if (itemstack.is(Items.ELYTRA) && ElytraItem.isFlyEnabled(itemstack)) {
                this.startFallFlying();
                return true;
            }
        }

        return false;
    }

    public void startFallFlying() {
        if (!CraftEventFactory.callToggleGlideEvent(this, true).isCancelled()) {
            this.setSharedFlag(7, true);
        } else {
            this.setSharedFlag(7, true);
            this.setSharedFlag(7, false);
        }

    }

    public void stopFallFlying() {
        if (!CraftEventFactory.callToggleGlideEvent(this, false).isCancelled()) {
            this.setSharedFlag(7, true);
            this.setSharedFlag(7, false);
        }

    }

    protected void doWaterSplashEffect() {
        if (!this.isSpectator()) {
            super.doWaterSplashEffect();
        }

    }

    public Fallsounds getFallSounds() {
        return new Fallsounds(SoundEvents.PLAYER_SMALL_FALL, SoundEvents.PLAYER_BIG_FALL);
    }

    public void killed(ServerLevel worldserver, LivingEntity entityliving) {
        this.awardStat(Stats.ENTITY_KILLED.get(entityliving.getType()));
    }

    public void makeStuckInBlock(BlockState iblockdata, Vec3 vec3d) {
        if (!this.abilities.flying) {
            super.makeStuckInBlock(iblockdata, vec3d);
        }

    }

    public void giveExperiencePoints(int i) {
        this.increaseScore(i);
        this.experienceProgress += (float)i / (float)this.getXpNeededForNextLevel();
        this.totalExperience = Mth.clamp(this.totalExperience + i, 0, 2147483647);

        while(this.experienceProgress < 0.0F) {
            float f = this.experienceProgress * (float)this.getXpNeededForNextLevel();
            if (this.experienceLevel > 0) {
                this.giveExperienceLevels(-1);
                this.experienceProgress = 1.0F + f / (float)this.getXpNeededForNextLevel();
            } else {
                this.giveExperienceLevels(-1);
                this.experienceProgress = 0.0F;
            }
        }

        while(this.experienceProgress >= 1.0F) {
            this.experienceProgress = (this.experienceProgress - 1.0F) * (float)this.getXpNeededForNextLevel();
            this.giveExperienceLevels(1);
            this.experienceProgress /= (float)this.getXpNeededForNextLevel();
        }

    }

    public int getEnchantmentSeed() {
        return this.enchantmentSeed;
    }

    public void onEnchantmentPerformed(ItemStack itemstack, int i) {
        this.experienceLevel -= i;
        if (this.experienceLevel < 0) {
            this.experienceLevel = 0;
            this.experienceProgress = 0.0F;
            this.totalExperience = 0;
        }

        this.enchantmentSeed = this.random.nextInt();
    }

    public void giveExperienceLevels(int i) {
        this.experienceLevel += i;
        if (this.experienceLevel < 0) {
            this.experienceLevel = 0;
            this.experienceProgress = 0.0F;
            this.totalExperience = 0;
        }

        if (i > 0 && this.experienceLevel % 5 == 0 && (float)this.lastLevelUpTime < (float)this.tickCount - 100.0F) {
            float f = this.experienceLevel > 30 ? 1.0F : (float)this.experienceLevel / 30.0F;
            this.level.playSound((Player)null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_LEVELUP, this.getSoundSource(), f * 0.75F, 1.0F);
            this.lastLevelUpTime = this.tickCount;
        }

    }

    public int getXpNeededForNextLevel() {
        return this.experienceLevel >= 30 ? 112 + (this.experienceLevel - 30) * 9 : (this.experienceLevel >= 15 ? 37 + (this.experienceLevel - 15) * 5 : 7 + this.experienceLevel * 2);
    }

    public void causeFoodExhaustion(float f) {
        this.causeFoodExhaustion(f, ExhaustionReason.UNKNOWN);
    }

    public void causeFoodExhaustion(float f, ExhaustionReason reason) {
        if (!this.abilities.invulnerable && !this.level.isClientSide) {
            EntityExhaustionEvent event = CraftEventFactory.callPlayerExhaustionEvent(this, reason, f);
            if (!event.isCancelled()) {
                this.foodData.addExhaustion(event.getExhaustion());
            }
        }

    }

    public FoodData getFoodData() {
        return this.foodData;
    }

    public boolean canEat(boolean flag) {
        return this.abilities.invulnerable || flag || this.foodData.needsFood();
    }

    public boolean isHurt() {
        return this.getHealth() > 0.0F && this.getHealth() < this.getMaxHealth();
    }

    public boolean mayBuild() {
        return this.abilities.mayBuild;
    }

    public boolean mayUseItemAt(BlockPos blockposition, Direction enumdirection, ItemStack itemstack) {
        if (this.abilities.mayBuild) {
            return true;
        } else {
            BlockPos blockposition1 = blockposition.relative(enumdirection.getOpposite());
            BlockInWorld shapedetectorblock = new BlockInWorld(this.level, blockposition1, false);
            return itemstack.hasAdventureModePlaceTagForBlock(this.level.getTagManager(), shapedetectorblock);
        }
    }

    protected int getExperienceReward(Player entityhuman) {
        if (!this.level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY) && !this.isSpectator()) {
            int i = this.experienceLevel * 7;
            return i > 100 ? 100 : i;
        } else {
            return 0;
        }
    }

    protected boolean isAlwaysExperienceDropper() {
        return true;
    }

    public boolean shouldShowName() {
        return true;
    }

    protected MovementEmission getMovementEmission() {
        return this.abilities.flying || this.onGround && this.isDiscrete() ? MovementEmission.NONE : MovementEmission.ALL;
    }

    public void onUpdateAbilities() {
    }

    public Component getName() {
        return new TextComponent(this.gameProfile.getName());
    }

    public PlayerEnderChestContainer getEnderChestInventory() {
        return this.enderChestInventory;
    }

    public ItemStack getItemBySlot(EquipmentSlot enumitemslot) {
        return enumitemslot == EquipmentSlot.MAINHAND ? this.inventory.getSelected() : (enumitemslot == EquipmentSlot.OFFHAND ? (ItemStack)this.inventory.offhand.get(0) : (enumitemslot.getType() == Type.ARMOR ? (ItemStack)this.inventory.armor.get(enumitemslot.getIndex()) : ItemStack.EMPTY));
    }

    public void setItemSlot(EquipmentSlot enumitemslot, ItemStack itemstack) {
        this.setItemSlot(enumitemslot, itemstack, false);
    }

    public void setItemSlot(EquipmentSlot enumitemslot, ItemStack itemstack, boolean silent) {
        this.verifyEquippedItem(itemstack);
        if (enumitemslot == EquipmentSlot.MAINHAND) {
            this.equipEventAndSound(itemstack, silent);
            this.inventory.items.set(this.inventory.selected, itemstack);
        } else if (enumitemslot == EquipmentSlot.OFFHAND) {
            this.equipEventAndSound(itemstack, silent);
            this.inventory.offhand.set(0, itemstack);
        } else if (enumitemslot.getType() == Type.ARMOR) {
            this.equipEventAndSound(itemstack, silent);
            this.inventory.armor.set(enumitemslot.getIndex(), itemstack);
        }

    }

    public boolean addItem(ItemStack itemstack) {
        this.equipEventAndSound(itemstack);
        return this.inventory.add(itemstack);
    }

    public Iterable<ItemStack> getHandSlots() {
        return Lists.newArrayList(new ItemStack[]{this.getMainHandItem(), this.getOffhandItem()});
    }

    public Iterable<ItemStack> getArmorSlots() {
        return this.inventory.armor;
    }

    public boolean setEntityOnShoulder(CompoundTag nbttagcompound) {
        if (!this.isPassenger() && this.onGround && !this.isInWater() && !this.isInPowderSnow) {
            if (this.getShoulderEntityLeft().isEmpty()) {
                this.setShoulderEntityLeft(nbttagcompound);
                this.timeEntitySatOnShoulder = this.level.getGameTime();
                return true;
            } else if (this.getShoulderEntityRight().isEmpty()) {
                this.setShoulderEntityRight(nbttagcompound);
                this.timeEntitySatOnShoulder = this.level.getGameTime();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    protected void removeEntitiesOnShoulder() {
        if (this.timeEntitySatOnShoulder + 20L < this.level.getGameTime()) {
            if (this.spawnEntityFromShoulder(this.getShoulderEntityLeft())) {
                this.setShoulderEntityLeft(new CompoundTag());
            }

            if (this.spawnEntityFromShoulder(this.getShoulderEntityRight())) {
                this.setShoulderEntityRight(new CompoundTag());
            }
        }

    }

    private boolean spawnEntityFromShoulder(CompoundTag nbttagcompound) {
        return !this.level.isClientSide && !nbttagcompound.isEmpty() ? (Boolean)EntityType.create(nbttagcompound, this.level).map((entity) -> {
            if (entity instanceof TamableAnimal) {
                ((TamableAnimal)entity).setOwnerUUID(this.uuid);
            }

            entity.setPos(this.getX(), this.getY() + 0.699999988079071D, this.getZ());
            return ((ServerLevel)this.level).addWithUUID(entity, SpawnReason.SHOULDER_ENTITY);
        }).orElse(true) : true;
    }

    public abstract boolean isSpectator();

    public boolean isSwimming() {
        return !this.abilities.flying && !this.isSpectator() && super.isSwimming();
    }

    public abstract boolean isCreative();

    public boolean isPushedByFluid() {
        return !this.abilities.flying;
    }

    public Scoreboard getScoreboard() {
        return this.level.getScoreboard();
    }

    public Component getDisplayName() {
        MutableComponent ichatmutablecomponent = PlayerTeam.formatNameForTeam(this.getTeam(), this.getName());
        return this.decorateDisplayNameComponent(ichatmutablecomponent);
    }

    private MutableComponent decorateDisplayNameComponent(MutableComponent ichatmutablecomponent) {
        String s = this.getGameProfile().getName();
        return ichatmutablecomponent.withStyle((chatmodifier) -> {
            return chatmodifier.withClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/tell " + s + " ")).withHoverEvent(this.createHoverEvent()).withInsertion(s);
        });
    }

    public String getScoreboardName() {
        return this.getGameProfile().getName();
    }

    public float getStandingEyeHeight(Pose entitypose, EntityDimensions entitysize) {
        switch($SWITCH_TABLE$net$minecraft$world$entity$EntityPose()[entitypose.ordinal()]) {
        case 2:
        case 4:
        case 5:
            return 0.4F;
        case 3:
        default:
            return 1.62F;
        case 6:
            return 1.27F;
        }
    }

    public void setAbsorptionAmount(float f) {
        if (f < 0.0F) {
            f = 0.0F;
        }

        this.getEntityData().set(DATA_PLAYER_ABSORPTION_ID, f);
    }

    public float getAbsorptionAmount() {
        return (Float)this.getEntityData().get(DATA_PLAYER_ABSORPTION_ID);
    }

    public static UUID createPlayerUUID(GameProfile gameprofile) {
        UUID uuid = gameprofile.getId();
        if (uuid == null) {
            uuid = createPlayerUUID(gameprofile.getName());
        }

        return uuid;
    }

    public static UUID createPlayerUUID(String s) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + s).getBytes(StandardCharsets.UTF_8));
    }

    public boolean isModelPartShown(PlayerModelPart playermodelpart) {
        return ((Byte)this.getEntityData().get(DATA_PLAYER_MODE_CUSTOMISATION) & playermodelpart.getMask()) == playermodelpart.getMask();
    }

    public SlotAccess getSlot(int i) {
        if (i >= 0 && i < this.inventory.items.size()) {
            return SlotAccess.forContainer(this.inventory, i);
        } else {
            int j = i - 200;
            return j >= 0 && j < this.enderChestInventory.getContainerSize() ? SlotAccess.forContainer(this.enderChestInventory, j) : super.getSlot(i);
        }
    }

    public boolean isReducedDebugInfo() {
        return this.reducedDebugInfo;
    }

    public void setReducedDebugInfo(boolean flag) {
        this.reducedDebugInfo = flag;
    }

    public void setRemainingFireTicks(int i) {
        super.setRemainingFireTicks(this.abilities.invulnerable ? Math.min(i, 1) : i);
    }

    public HumanoidArm getMainArm() {
        return (Byte)this.entityData.get(DATA_PLAYER_MAIN_HAND) == 0 ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
    }

    public void setMainArm(HumanoidArm enummainhand) {
        this.entityData.set(DATA_PLAYER_MAIN_HAND, (byte)(enummainhand == HumanoidArm.LEFT ? 0 : 1));
    }

    public CompoundTag getShoulderEntityLeft() {
        return (CompoundTag)this.entityData.get(DATA_SHOULDER_LEFT);
    }

    public void setShoulderEntityLeft(CompoundTag nbttagcompound) {
        this.entityData.set(DATA_SHOULDER_LEFT, nbttagcompound);
    }

    public CompoundTag getShoulderEntityRight() {
        return (CompoundTag)this.entityData.get(DATA_SHOULDER_RIGHT);
    }

    public void setShoulderEntityRight(CompoundTag nbttagcompound) {
        this.entityData.set(DATA_SHOULDER_RIGHT, nbttagcompound);
    }

    public float getCurrentItemAttackStrengthDelay() {
        return (float)(1.0D / this.getAttributeValue(Attributes.ATTACK_SPEED) * 20.0D);
    }

    public float getAttackStrengthScale(float f) {
        return Mth.clamp(((float)this.attackStrengthTicker + f) / this.getCurrentItemAttackStrengthDelay(), 0.0F, 1.0F);
    }

    public void resetAttackStrengthTicker() {
        this.attackStrengthTicker = 0;
    }

    public ItemCooldowns getCooldowns() {
        return this.cooldowns;
    }

    protected float getBlockSpeedFactor() {
        return !this.abilities.flying && !this.isFallFlying() ? super.getBlockSpeedFactor() : 1.0F;
    }

    public float getLuck() {
        return (float)this.getAttributeValue(Attributes.LUCK);
    }

    public boolean canUseGameMasterBlocks() {
        return this.abilities.instabuild && this.getPermissionLevel() >= 2;
    }

    public boolean canTakeItem(ItemStack itemstack) {
        EquipmentSlot enumitemslot = Mob.getEquipmentSlotForItem(itemstack);
        return this.getItemBySlot(enumitemslot).isEmpty();
    }

    public EntityDimensions getDimensions(Pose entitypose) {
        return (EntityDimensions)POSES.getOrDefault(entitypose, STANDING_DIMENSIONS);
    }

    public ImmutableList<Pose> getDismountPoses() {
        return ImmutableList.of(Pose.STANDING, Pose.CROUCHING, Pose.SWIMMING);
    }

    public ItemStack getProjectile(ItemStack itemstack) {
        if (!(itemstack.getItem() instanceof ProjectileWeaponItem)) {
            return ItemStack.EMPTY;
        } else {
            Predicate<ItemStack> predicate = ((ProjectileWeaponItem)itemstack.getItem()).getSupportedHeldProjectiles();
            ItemStack itemstack1 = ProjectileWeaponItem.getHeldProjectile(this, predicate);
            if (!itemstack1.isEmpty()) {
                return itemstack1;
            } else {
                predicate = ((ProjectileWeaponItem)itemstack.getItem()).getAllSupportedProjectiles();

                for(int i = 0; i < this.inventory.getContainerSize(); ++i) {
                    ItemStack itemstack2 = this.inventory.getItem(i);
                    if (predicate.test(itemstack2)) {
                        return itemstack2;
                    }
                }

                return this.abilities.instabuild ? new ItemStack(Items.ARROW) : ItemStack.EMPTY;
            }
        }
    }

    public ItemStack eat(Level world, ItemStack itemstack) {
        this.getFoodData().eat(itemstack.getItem(), itemstack);
        this.awardStat(Stats.ITEM_USED.get(itemstack.getItem()));
        world.playSound((Player)null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
        if (this instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)this, itemstack);
        }

        return super.eat(world, itemstack);
    }

    protected boolean shouldRemoveSoulSpeed(BlockState iblockdata) {
        return this.abilities.flying || super.shouldRemoveSoulSpeed(iblockdata);
    }

    public Vec3 getRopeHoldPosition(float f) {
        double d0 = 0.22D * (this.getMainArm() == HumanoidArm.RIGHT ? -1.0D : 1.0D);
        float f1 = Mth.lerp(f * 0.5F, this.getXRot(), this.xRotO) * 0.017453292F;
        float f2 = Mth.lerp(f, this.yBodyRotO, this.yBodyRot) * 0.017453292F;
        double d1;
        if (!this.isFallFlying() && !this.isAutoSpinAttack()) {
            if (this.isVisuallySwimming()) {
                return this.getPosition(f).add((new Vec3(d0, 0.2D, -0.15D)).xRot(-f1).yRot(-f2));
            } else {
                double d2 = this.getBoundingBox().getYsize() - 1.0D;
                d1 = this.isCrouching() ? -0.2D : 0.07D;
                return this.getPosition(f).add((new Vec3(d0, d2, d1)).yRot(-f2));
            }
        } else {
            Vec3 vec3d = this.getViewVector(f);
            Vec3 vec3d1 = this.getDeltaMovement();
            d1 = vec3d1.horizontalDistanceSqr();
            double d3 = vec3d.horizontalDistanceSqr();
            float f3;
            if (d1 > 0.0D && d3 > 0.0D) {
                double d4 = (vec3d1.x * vec3d.x + vec3d1.z * vec3d.z) / Math.sqrt(d1 * d3);
                double d5 = vec3d1.x * vec3d.z - vec3d1.z * vec3d.x;
                f3 = (float)(Math.signum(d5) * Math.acos(d4));
            } else {
                f3 = 0.0F;
            }

            return this.getPosition(f).add((new Vec3(d0, -0.11D, 0.85D)).zRot(-f3).xRot(-f1).yRot(-f2));
        }
    }

    public boolean isAlwaysTicking() {
        return true;
    }

    public boolean isScoping() {
        return this.isUsingItem() && this.getUseItem().is(Items.SPYGLASS);
    }

    public boolean shouldBeSaved() {
        return false;
    }

    public static enum BedSleepingProblem {
        NOT_POSSIBLE_HERE,
        NOT_POSSIBLE_NOW(new TranslatableComponent("block.minecraft.bed.no_sleep")),
        TOO_FAR_AWAY(new TranslatableComponent("block.minecraft.bed.too_far_away")),
        OBSTRUCTED(new TranslatableComponent("block.minecraft.bed.obstructed")),
        OTHER_PROBLEM,
        NOT_SAFE(new TranslatableComponent("block.minecraft.bed.not_safe"));

        @Nullable
        private final Component message;

        private BedSleepingProblem() {
            this.message = null;
        }

        private BedSleepingProblem(Component ichatbasecomponent) {
            this.message = ichatbasecomponent;
        }

        @Nullable
        public Component getMessage() {
            return this.message;
        }
    }
}
