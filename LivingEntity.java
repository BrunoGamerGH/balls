//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.minecraft.world.entity;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.BlockUtil.FoundRectangle;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddMobPacket;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.Brain.Provider;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.FrostWalkerEnchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HoneyBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.scores.PlayerTeam;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.SpigotTimings;
import org.bukkit.craftbukkit.v1_18_R2.attribute.CraftAttributeMap;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.Item;
import org.bukkit.event.entity.ArrowBodyCountChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.EntityExhaustionEvent.ExhaustionReason;
import org.bukkit.event.entity.EntityPotionEffectEvent.Action;
import org.bukkit.event.entity.EntityPotionEffectEvent.Cause;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.slf4j.Logger;
import org.spigotmc.AsyncCatcher;
import org.spigotmc.SpigotConfig;

public abstract class LivingEntity extends Entity {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final UUID SPEED_MODIFIER_SPRINTING_UUID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
    private static final UUID SPEED_MODIFIER_SOUL_SPEED_UUID = UUID.fromString("87f46a96-686f-4796-b035-22e16ee9e038");
    private static final UUID SPEED_MODIFIER_POWDER_SNOW_UUID = UUID.fromString("1eaf83ff-7207-4596-b37a-d7a07b3ec4ce");
    private static final AttributeModifier SPEED_MODIFIER_SPRINTING;
    public static final int HAND_SLOTS = 2;
    public static final int ARMOR_SLOTS = 4;
    public static final int EQUIPMENT_SLOT_OFFSET = 98;
    public static final int ARMOR_SLOT_OFFSET = 100;
    public static final int SWING_DURATION = 6;
    public static final int PLAYER_HURT_EXPERIENCE_TIME = 100;
    private static final int DAMAGE_SOURCE_TIMEOUT = 40;
    public static final double MIN_MOVEMENT_DISTANCE = 0.003D;
    public static final double DEFAULT_BASE_GRAVITY = 0.08D;
    public static final int DEATH_DURATION = 20;
    private static final int WAIT_TICKS_BEFORE_ITEM_USE_EFFECTS = 7;
    private static final int TICKS_PER_ELYTRA_FREE_FALL_EVENT = 10;
    private static final int FREE_FALL_EVENTS_PER_ELYTRA_BREAK = 2;
    public static final int USE_ITEM_INTERVAL = 4;
    private static final double MAX_LINE_OF_SIGHT_TEST_RANGE = 128.0D;
    protected static final int LIVING_ENTITY_FLAG_IS_USING = 1;
    protected static final int LIVING_ENTITY_FLAG_OFF_HAND = 2;
    protected static final int LIVING_ENTITY_FLAG_SPIN_ATTACK = 4;
    protected static final EntityDataAccessor<Byte> DATA_LIVING_ENTITY_FLAGS;
    public static final EntityDataAccessor<Float> DATA_HEALTH_ID;
    private static final EntityDataAccessor<Integer> DATA_EFFECT_COLOR_ID;
    private static final EntityDataAccessor<Boolean> DATA_EFFECT_AMBIENCE_ID;
    public static final EntityDataAccessor<Integer> DATA_ARROW_COUNT_ID;
    private static final EntityDataAccessor<Integer> DATA_STINGER_COUNT_ID;
    private static final EntityDataAccessor<Optional<BlockPos>> SLEEPING_POS_ID;
    protected static final float DEFAULT_EYE_HEIGHT = 1.74F;
    protected static final EntityDimensions SLEEPING_DIMENSIONS;
    public static final float EXTRA_RENDER_CULLING_SIZE_WITH_BIG_HAT = 0.5F;
    private final AttributeMap attributes;
    public CombatTracker combatTracker = new CombatTracker(this);
    public final Map<MobEffect, MobEffectInstance> activeEffects = Maps.newHashMap();
    private final NonNullList<ItemStack> lastHandItemStacks;
    private final NonNullList<ItemStack> lastArmorItemStacks;
    public boolean swinging;
    private boolean discardFriction;
    public InteractionHand swingingArm;
    public int swingTime;
    public int removeArrowTime;
    public int removeStingerTime;
    public int hurtTime;
    public int hurtDuration;
    public float hurtDir;
    public int deathTime;
    public float oAttackAnim;
    public float attackAnim;
    protected int attackStrengthTicker;
    public float animationSpeedOld;
    public float animationSpeed;
    public float animationPosition;
    public int invulnerableDuration;
    public final float timeOffs;
    public final float rotA;
    public float yBodyRot;
    public float yBodyRotO;
    public float yHeadRot;
    public float yHeadRotO;
    public float flyingSpeed;
    @Nullable
    public Player lastHurtByPlayer;
    protected int lastHurtByPlayerTime;
    protected boolean dead;
    protected int noActionTime;
    protected float oRun;
    protected float run;
    protected float animStep;
    protected float animStepO;
    protected float rotOffs;
    protected int deathScore;
    public float lastHurt;
    protected boolean jumping;
    public float xxa;
    public float yya;
    public float zza;
    protected int lerpSteps;
    protected double lerpX;
    protected double lerpY;
    protected double lerpZ;
    protected double lerpYRot;
    protected double lerpXRot;
    protected double lyHeadRot;
    protected int lerpHeadSteps;
    public boolean effectsDirty;
    @Nullable
    public LivingEntity lastHurtByMob;
    public int lastHurtByMobTimestamp;
    private LivingEntity lastHurtMob;
    private int lastHurtMobTimestamp;
    private float speed;
    private int noJumpDelay;
    private float absorptionAmount;
    protected ItemStack useItem;
    protected int useItemRemaining;
    protected int fallFlyTicks;
    private BlockPos lastPos;
    private Optional<BlockPos> lastClimbablePos;
    @Nullable
    private DamageSource lastDamageSource;
    private long lastDamageStamp;
    protected int autoSpinAttackTicks;
    private float swimAmount;
    private float swimAmountO;
    protected Brain<?> brain;
    public int expToDrop;
    public boolean forceDrops;
    public ArrayList<org.bukkit.inventory.ItemStack> drops = new ArrayList();
    public final CraftAttributeMap craftAttributes;
    public boolean collides = true;
    public Set<UUID> collidableExemptions = new HashSet();
    public boolean bukkitPickUpLoot;
    private boolean isTickingEffects = false;
    private List<LivingEntity.ProcessableEffect> effectsToProcess = Lists.newArrayList();

    static {
        SPEED_MODIFIER_SPRINTING = new AttributeModifier(SPEED_MODIFIER_SPRINTING_UUID, "Sprinting speed boost", 0.30000001192092896D, Operation.MULTIPLY_TOTAL);
        DATA_LIVING_ENTITY_FLAGS = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BYTE);
        DATA_HEALTH_ID = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.FLOAT);
        DATA_EFFECT_COLOR_ID = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
        DATA_EFFECT_AMBIENCE_ID = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);
        DATA_ARROW_COUNT_ID = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
        DATA_STINGER_COUNT_ID = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
        SLEEPING_POS_ID = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        SLEEPING_DIMENSIONS = EntityDimensions.fixed(0.2F, 0.2F);
    }

    public float getBukkitYaw() {
        return this.getYHeadRot();
    }

    public void inactiveTick() {
        super.inactiveTick();
        ++this.noActionTime;
    }

    protected LivingEntity(EntityType<? extends LivingEntity> entitytypes, Level world) {
        super(entitytypes, world);
        this.lastHandItemStacks = NonNullList.withSize(2, ItemStack.EMPTY);
        this.lastArmorItemStacks = NonNullList.withSize(4, ItemStack.EMPTY);
        this.discardFriction = false;
        this.invulnerableDuration = 20;
        this.flyingSpeed = 0.02F;
        this.effectsDirty = true;
        this.useItem = ItemStack.EMPTY;
        this.lastClimbablePos = Optional.empty();
        this.attributes = new AttributeMap(DefaultAttributes.getSupplier(entitytypes));
        this.craftAttributes = new CraftAttributeMap(this.attributes);
        this.entityData.set(DATA_HEALTH_ID, (float)this.getAttribute(Attributes.MAX_HEALTH).getValue());
        this.blocksBuilding = true;
        this.rotA = (float)((Math.random() + 1.0D) * 0.009999999776482582D);
        this.reapplyPosition();
        this.timeOffs = (float)Math.random() * 12398.0F;
        this.setYRot((float)(Math.random() * 6.2831854820251465D));
        this.yHeadRot = this.getYRot();
        this.maxUpStep = 0.6F;
        NbtOps dynamicopsnbt = NbtOps.INSTANCE;
        this.brain = this.makeBrain(new Dynamic(dynamicopsnbt, (Tag)dynamicopsnbt.createMap(ImmutableMap.of(dynamicopsnbt.createString("memories"), (Tag)dynamicopsnbt.emptyMap()))));
    }

    public Brain<?> getBrain() {
        return this.brain;
    }

    protected Provider<?> brainProvider() {
        return Brain.provider(ImmutableList.of(), ImmutableList.of());
    }

    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return this.brainProvider().makeBrain(dynamic);
    }

    public void kill() {
        this.hurt(DamageSource.OUT_OF_WORLD, 3.4028235E38F);
    }

    public boolean canAttackType(EntityType<?> entitytypes) {
        return true;
    }

    protected void defineSynchedData() {
        this.entityData.define(DATA_LIVING_ENTITY_FLAGS, (byte)0);
        this.entityData.define(DATA_EFFECT_COLOR_ID, 0);
        this.entityData.define(DATA_EFFECT_AMBIENCE_ID, false);
        this.entityData.define(DATA_ARROW_COUNT_ID, 0);
        this.entityData.define(DATA_STINGER_COUNT_ID, 0);
        this.entityData.define(DATA_HEALTH_ID, 1.0F);
        this.entityData.define(SLEEPING_POS_ID, Optional.empty());
    }

    public static Builder createLivingAttributes() {
        return AttributeSupplier.builder().add(Attributes.MAX_HEALTH).add(Attributes.KNOCKBACK_RESISTANCE).add(Attributes.MOVEMENT_SPEED).add(Attributes.ARMOR).add(Attributes.ARMOR_TOUGHNESS);
    }

    protected void checkFallDamage(double d0, boolean flag, BlockState iblockdata, BlockPos blockposition) {
        if (!this.isInWater()) {
            this.updateInWaterStateAndDoWaterCurrentPushing();
        }

        if (!this.level.isClientSide && flag && this.fallDistance > 0.0F) {
            this.removeSoulSpeed();
            this.tryAddSoulSpeed();
        }

        if (!this.level.isClientSide && this.fallDistance > 3.0F && flag) {
            float f = (float)Mth.ceil(this.fallDistance - 3.0F);
            if (!iblockdata.isAir()) {
                double d1 = Math.min((double)(0.2F + f / 15.0F), 2.5D);
                int i = (int)(150.0D * d1);
                if (this instanceof ServerPlayer) {
                    ((ServerLevel)this.level).sendParticles((ServerPlayer)this, new BlockParticleOption(ParticleTypes.BLOCK, iblockdata), this.getX(), this.getY(), this.getZ(), i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, false);
                } else {
                    ((ServerLevel)this.level).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, iblockdata), this.getX(), this.getY(), this.getZ(), i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D);
                }
            }
        }

        super.checkFallDamage(d0, flag, iblockdata, blockposition);
    }

    public boolean canBreatheUnderwater() {
        return this.getMobType() == MobType.UNDEAD;
    }

    public float getSwimAmount(float f) {
        return Mth.lerp(f, this.swimAmountO, this.swimAmount);
    }

    public void baseTick() {
        this.oAttackAnim = this.attackAnim;
        if (this.firstTick) {
            this.getSleepingPos().ifPresent(this::setPosToBed);
        }

        if (this.canSpawnSoulSpeedParticle()) {
            this.spawnSoulSpeedParticle();
        }

        super.baseTick();
        this.level.getProfiler().push("livingEntityBaseTick");
        if (this.fireImmune() || this.level.isClientSide) {
            this.clearFire();
        }

        if (this.isAlive()) {
            boolean flag = this instanceof Player;
            if (this.isInWall()) {
                this.hurt(DamageSource.IN_WALL, 1.0F);
            } else if (flag && !this.level.getWorldBorder().isWithinBounds(this.getBoundingBox())) {
                double d0 = this.level.getWorldBorder().getDistanceToBorder(this) + this.level.getWorldBorder().getDamageSafeZone();
                if (d0 < 0.0D) {
                    double d1 = this.level.getWorldBorder().getDamagePerBlock();
                    if (d1 > 0.0D) {
                        this.hurt(DamageSource.IN_WALL, (float)Math.max(1, Mth.floor(-d0 * d1)));
                    }
                }
            }

            if (this.isEyeInFluid(FluidTags.WATER) && !this.level.getBlockState(new BlockPos(this.getX(), this.getEyeY(), this.getZ())).is(Blocks.BUBBLE_COLUMN)) {
                boolean flag1 = !this.canBreatheUnderwater() && !MobEffectUtil.hasWaterBreathing(this) && (!flag || !((Player)this).getAbilities().invulnerable);
                if (flag1) {
                    this.setAirSupply(this.decreaseAirSupply(this.getAirSupply()));
                    if (this.getAirSupply() == -20) {
                        this.setAirSupply(0);
                        Vec3 vec3d = this.getDeltaMovement();

                        for(int i = 0; i < 8; ++i) {
                            double d2 = this.random.nextDouble() - this.random.nextDouble();
                            double d3 = this.random.nextDouble() - this.random.nextDouble();
                            double d4 = this.random.nextDouble() - this.random.nextDouble();
                            this.level.addParticle(ParticleTypes.BUBBLE, this.getX() + d2, this.getY() + d3, this.getZ() + d4, vec3d.x, vec3d.y, vec3d.z);
                        }

                        this.hurt(DamageSource.DROWN, 2.0F);
                    }
                }

                if (!this.level.isClientSide && this.isPassenger() && this.getVehicle() != null && !this.getVehicle().rideableUnderWater()) {
                    this.stopRiding();
                }
            } else if (this.getAirSupply() < this.getMaxAirSupply()) {
                this.setAirSupply(this.increaseAirSupply(this.getAirSupply()));
            }

            if (!this.level.isClientSide) {
                BlockPos blockposition = this.blockPosition();
                if (!Objects.equal(this.lastPos, blockposition)) {
                    this.lastPos = blockposition;
                    this.onChangedBlock(blockposition);
                }
            }
        }

        if (this.isAlive() && (this.isInWaterRainOrBubble() || this.isInPowderSnow)) {
            if (!this.level.isClientSide && this.wasOnFire) {
                this.playEntityOnFireExtinguishedSound();
            }

            this.clearFire();
        }

        if (this.hurtTime > 0) {
            --this.hurtTime;
        }

        if (this.invulnerableTime > 0 && !(this instanceof ServerPlayer)) {
            --this.invulnerableTime;
        }

        if (this.isDeadOrDying() && this.level.shouldTickDeath(this)) {
            this.tickDeath();
        }

        if (this.lastHurtByPlayerTime > 0) {
            --this.lastHurtByPlayerTime;
        } else {
            this.lastHurtByPlayer = null;
        }

        if (this.lastHurtMob != null && !this.lastHurtMob.isAlive()) {
            this.lastHurtMob = null;
        }

        if (this.lastHurtByMob != null) {
            if (!this.lastHurtByMob.isAlive()) {
                this.setLastHurtByMob((LivingEntity)null);
            } else if (this.tickCount - this.lastHurtByMobTimestamp > 100) {
                this.setLastHurtByMob((LivingEntity)null);
            }
        }

        this.tickEffects();
        this.animStepO = this.animStep;
        this.yBodyRotO = this.yBodyRot;
        this.yHeadRotO = this.yHeadRot;
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
        this.level.getProfiler().pop();
    }

    public boolean canSpawnSoulSpeedParticle() {
        return this.tickCount % 5 == 0 && this.getDeltaMovement().x != 0.0D && this.getDeltaMovement().z != 0.0D && !this.isSpectator() && EnchantmentHelper.hasSoulSpeed(this) && this.onSoulSpeedBlock();
    }

    protected void spawnSoulSpeedParticle() {
        Vec3 vec3d = this.getDeltaMovement();
        this.level.addParticle(ParticleTypes.SOUL, this.getX() + (this.random.nextDouble() - 0.5D) * (double)this.getBbWidth(), this.getY() + 0.1D, this.getZ() + (this.random.nextDouble() - 0.5D) * (double)this.getBbWidth(), vec3d.x * -0.2D, 0.1D, vec3d.z * -0.2D);
        float f = this.random.nextFloat() * 0.4F + this.random.nextFloat() > 0.9F ? 0.6F : 0.0F;
        this.playSound(SoundEvents.SOUL_ESCAPE, f, 0.6F + this.random.nextFloat() * 0.4F);
    }

    protected boolean onSoulSpeedBlock() {
        return this.level.getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).is(BlockTags.SOUL_SPEED_BLOCKS);
    }

    protected float getBlockSpeedFactor() {
        return this.onSoulSpeedBlock() && EnchantmentHelper.getEnchantmentLevel(Enchantments.SOUL_SPEED, this) > 0 ? 1.0F : super.getBlockSpeedFactor();
    }

    protected boolean shouldRemoveSoulSpeed(BlockState iblockdata) {
        return !iblockdata.isAir() || this.isFallFlying();
    }

    protected void removeSoulSpeed() {
        AttributeInstance attributemodifiable = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attributemodifiable != null && attributemodifiable.getModifier(SPEED_MODIFIER_SOUL_SPEED_UUID) != null) {
            attributemodifiable.removeModifier(SPEED_MODIFIER_SOUL_SPEED_UUID);
        }

    }

    protected void tryAddSoulSpeed() {
        if (!this.getBlockStateOn().isAir()) {
            int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.SOUL_SPEED, this);
            if (i > 0 && this.onSoulSpeedBlock()) {
                AttributeInstance attributemodifiable = this.getAttribute(Attributes.MOVEMENT_SPEED);
                if (attributemodifiable == null) {
                    return;
                }

                attributemodifiable.addTransientModifier(new AttributeModifier(SPEED_MODIFIER_SOUL_SPEED_UUID, "Soul speed boost", (double)(0.03F * (1.0F + (float)i * 0.35F)), Operation.ADDITION));
                if (this.getRandom().nextFloat() < 0.04F) {
                    ItemStack itemstack = this.getItemBySlot(EquipmentSlot.FEET);
                    itemstack.hurtAndBreak(1, this, (entityliving) -> {
                        entityliving.broadcastBreakEvent(EquipmentSlot.FEET);
                    });
                }
            }
        }

    }

    protected void removeFrost() {
        AttributeInstance attributemodifiable = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attributemodifiable != null && attributemodifiable.getModifier(SPEED_MODIFIER_POWDER_SNOW_UUID) != null) {
            attributemodifiable.removeModifier(SPEED_MODIFIER_POWDER_SNOW_UUID);
        }

    }

    protected void tryAddFrost() {
        if (!this.getBlockStateOn().isAir()) {
            int i = this.getTicksFrozen();
            if (i > 0) {
                AttributeInstance attributemodifiable = this.getAttribute(Attributes.MOVEMENT_SPEED);
                if (attributemodifiable == null) {
                    return;
                }

                float f = -0.05F * this.getPercentFrozen();
                attributemodifiable.addTransientModifier(new AttributeModifier(SPEED_MODIFIER_POWDER_SNOW_UUID, "Powder snow slow", (double)f, Operation.ADDITION));
            }
        }

    }

    protected void onChangedBlock(BlockPos blockposition) {
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FROST_WALKER, this);
        if (i > 0) {
            FrostWalkerEnchantment.onEntityMoved(this, this.level, blockposition, i);
        }

        if (this.shouldRemoveSoulSpeed(this.getBlockStateOn())) {
            this.removeSoulSpeed();
        }

        this.tryAddSoulSpeed();
    }

    public boolean isBaby() {
        return false;
    }

    public float getScale() {
        return this.isBaby() ? 0.5F : 1.0F;
    }

    protected boolean isAffectedByFluids() {
        return true;
    }

    public boolean rideableUnderWater() {
        return false;
    }

    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime == 20 && !this.level.isClientSide()) {
            this.level.broadcastEntityEvent(this, (byte)60);
            this.remove(RemovalReason.KILLED);
        }

    }

    protected boolean shouldDropExperience() {
        return !this.isBaby();
    }

    protected boolean shouldDropLoot() {
        return !this.isBaby();
    }

    protected int decreaseAirSupply(int i) {
        int j = EnchantmentHelper.getRespiration(this);
        return j > 0 && this.random.nextInt(j + 1) > 0 ? i : i - 1;
    }

    protected int increaseAirSupply(int i) {
        return Math.min(i + 4, this.getMaxAirSupply());
    }

    protected int getExperienceReward(Player entityhuman) {
        return 0;
    }

    protected boolean isAlwaysExperienceDropper() {
        return false;
    }

    public Random getRandom() {
        return this.random;
    }

    @Nullable
    public LivingEntity getLastHurtByMob() {
        return this.lastHurtByMob;
    }

    public int getLastHurtByMobTimestamp() {
        return this.lastHurtByMobTimestamp;
    }

    public void setLastHurtByPlayer(@Nullable Player entityhuman) {
        this.lastHurtByPlayer = entityhuman;
        this.lastHurtByPlayerTime = this.tickCount;
    }

    public void setLastHurtByMob(@Nullable LivingEntity entityliving) {
        this.lastHurtByMob = entityliving;
        this.lastHurtByMobTimestamp = this.tickCount;
    }

    @Nullable
    public LivingEntity getLastHurtMob() {
        return this.lastHurtMob;
    }

    public int getLastHurtMobTimestamp() {
        return this.lastHurtMobTimestamp;
    }

    public void setLastHurtMob(Entity entity) {
        if (entity instanceof LivingEntity) {
            this.lastHurtMob = (LivingEntity)entity;
        } else {
            this.lastHurtMob = null;
        }

        this.lastHurtMobTimestamp = this.tickCount;
    }

    public int getNoActionTime() {
        return this.noActionTime;
    }

    public void setNoActionTime(int i) {
        this.noActionTime = i;
    }

    public boolean shouldDiscardFriction() {
        return this.discardFriction;
    }

    public void setDiscardFriction(boolean flag) {
        this.discardFriction = flag;
    }

    protected void equipEventAndSound(ItemStack itemstack) {
        this.equipEventAndSound(itemstack, false);
    }

    protected void equipEventAndSound(ItemStack itemstack, boolean silent) {
        SoundEvent soundeffect = itemstack.getEquipSound();
        if (!itemstack.isEmpty() && soundeffect != null && !this.isSpectator() && !silent) {
            this.gameEvent(GameEvent.EQUIP);
            this.playSound(soundeffect, 1.0F, 1.0F);
        }

    }

    public void addAdditionalSaveData(CompoundTag nbttagcompound) {
        nbttagcompound.putFloat("Health", this.getHealth());
        nbttagcompound.putShort("HurtTime", (short)this.hurtTime);
        nbttagcompound.putInt("HurtByTimestamp", this.lastHurtByMobTimestamp);
        nbttagcompound.putShort("DeathTime", (short)this.deathTime);
        nbttagcompound.putFloat("AbsorptionAmount", this.getAbsorptionAmount());
        nbttagcompound.put("Attributes", this.getAttributes().save());
        if (!this.activeEffects.isEmpty()) {
            ListTag nbttaglist = new ListTag();
            Iterator iterator = this.activeEffects.values().iterator();

            while(iterator.hasNext()) {
                MobEffectInstance mobeffect = (MobEffectInstance)iterator.next();
                nbttaglist.add(mobeffect.save(new CompoundTag()));
            }

            nbttagcompound.put("ActiveEffects", nbttaglist);
        }

        nbttagcompound.putBoolean("FallFlying", this.isFallFlying());
        this.getSleepingPos().ifPresent((blockposition) -> {
            nbttagcompound.putInt("SleepingX", blockposition.getX());
            nbttagcompound.putInt("SleepingY", blockposition.getY());
            nbttagcompound.putInt("SleepingZ", blockposition.getZ());
        });
        DataResult<Tag> dataresult = this.brain.serializeStart(NbtOps.INSTANCE);
        Logger logger = LOGGER;
        java.util.Objects.requireNonNull(logger);
        logger.getClass();
        dataresult.resultOrPartial(logger::error).ifPresent((nbtbase) -> {
            nbttagcompound.put("Brain", nbtbase);
        });
    }

    public void readAdditionalSaveData(CompoundTag nbttagcompound) {
        this.setAbsorptionAmount(nbttagcompound.getFloat("AbsorptionAmount"));
        if (nbttagcompound.contains("Attributes", 9) && this.level != null && !this.level.isClientSide) {
            this.getAttributes().load(nbttagcompound.getList("Attributes", 10));
        }

        if (nbttagcompound.contains("ActiveEffects", 9)) {
            ListTag nbttaglist = nbttagcompound.getList("ActiveEffects", 10);

            for(int i = 0; i < nbttaglist.size(); ++i) {
                CompoundTag nbttagcompound1 = nbttaglist.getCompound(i);
                MobEffectInstance mobeffect = MobEffectInstance.load(nbttagcompound1);
                if (mobeffect != null) {
                    this.activeEffects.put(mobeffect.getEffect(), mobeffect);
                }
            }
        }

        if (nbttagcompound.contains("Bukkit.MaxHealth")) {
            Tag nbtbase = nbttagcompound.get("Bukkit.MaxHealth");
            if (nbtbase.getId() == 5) {
                this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(((FloatTag)nbtbase).getAsDouble());
            } else if (nbtbase.getId() == 3) {
                this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(((IntTag)nbtbase).getAsDouble());
            }
        }

        if (nbttagcompound.contains("Health", 99)) {
            this.setHealth(nbttagcompound.getFloat("Health"));
        }

        this.hurtTime = nbttagcompound.getShort("HurtTime");
        this.deathTime = nbttagcompound.getShort("DeathTime");
        this.lastHurtByMobTimestamp = nbttagcompound.getInt("HurtByTimestamp");
        if (nbttagcompound.contains("Team", 8)) {
            String s = nbttagcompound.getString("Team");
            PlayerTeam scoreboardteam = this.level.getScoreboard().getPlayerTeam(s);
            boolean flag = scoreboardteam != null && this.level.getScoreboard().addPlayerToTeam(this.getStringUUID(), scoreboardteam);
            if (!flag) {
                LOGGER.warn("Unable to add mob to team \"{}\" (that team probably doesn't exist)", s);
            }
        }

        if (nbttagcompound.getBoolean("FallFlying")) {
            this.setSharedFlag(7, true);
        }

        if (nbttagcompound.contains("SleepingX", 99) && nbttagcompound.contains("SleepingY", 99) && nbttagcompound.contains("SleepingZ", 99)) {
            BlockPos blockposition = new BlockPos(nbttagcompound.getInt("SleepingX"), nbttagcompound.getInt("SleepingY"), nbttagcompound.getInt("SleepingZ"));
            this.setSleepingPos(blockposition);
            this.entityData.set(DATA_POSE, Pose.SLEEPING);
            if (!this.firstTick) {
                this.setPosToBed(blockposition);
            }
        }

        if (nbttagcompound.contains("Brain", 10)) {
            this.brain = this.makeBrain(new Dynamic(NbtOps.INSTANCE, nbttagcompound.get("Brain")));
        }

    }

    protected void tickEffects() {
        Iterator iterator = this.activeEffects.keySet().iterator();
        this.isTickingEffects = true;

        try {
            while(iterator.hasNext()) {
                MobEffect mobeffectlist = (MobEffect)iterator.next();
                MobEffectInstance mobeffect = (MobEffectInstance)this.activeEffects.get(mobeffectlist);
                if (!mobeffect.tick(this, () -> {
                    this.onEffectUpdated(mobeffect, true, (Entity)null);
                })) {
                    if (!this.level.isClientSide) {
                        EntityPotionEffectEvent event = CraftEventFactory.callEntityPotionEffectChangeEvent(this, mobeffect, (MobEffectInstance)null, Cause.EXPIRATION);
                        if (!event.isCancelled()) {
                            iterator.remove();
                            this.onEffectRemoved(mobeffect);
                        }
                    }
                } else if (mobeffect.getDuration() % 600 == 0) {
                    this.onEffectUpdated(mobeffect, false, (Entity)null);
                }
            }
        } catch (ConcurrentModificationException var11) {
        }

        this.isTickingEffects = false;
        Iterator var14 = this.effectsToProcess.iterator();

        while(var14.hasNext()) {
            LivingEntity.ProcessableEffect e = (LivingEntity.ProcessableEffect)var14.next();
            if (e.effect != null) {
                this.addEffect(e.effect, e.cause);
            } else {
                this.removeEffect(e.type, e.cause);
            }
        }

        this.effectsToProcess.clear();
        if (this.effectsDirty) {
            if (!this.level.isClientSide) {
                this.updateInvisibilityStatus();
                this.updateGlowingStatus();
            }

            this.effectsDirty = false;
        }

        int i = (Integer)this.entityData.get(DATA_EFFECT_COLOR_ID);
        boolean flag = (Boolean)this.entityData.get(DATA_EFFECT_AMBIENCE_ID);
        if (i > 0) {
            boolean flag1;
            if (this.isInvisible()) {
                flag1 = this.random.nextInt(15) == 0;
            } else {
                flag1 = this.random.nextBoolean();
            }

            if (flag) {
                flag1 &= this.random.nextInt(5) == 0;
            }

            if (flag1 && i > 0) {
                double d0 = (double)(i >> 16 & 255) / 255.0D;
                double d1 = (double)(i >> 8 & 255) / 255.0D;
                double d2 = (double)(i >> 0 & 255) / 255.0D;
                this.level.addParticle(flag ? ParticleTypes.AMBIENT_ENTITY_EFFECT : ParticleTypes.ENTITY_EFFECT, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), d0, d1, d2);
            }
        }

    }

    protected void updateInvisibilityStatus() {
        if (this.activeEffects.isEmpty()) {
            this.removeEffectParticles();
            this.setInvisible(false);
        } else {
            Collection<MobEffectInstance> collection = this.activeEffects.values();
            this.entityData.set(DATA_EFFECT_AMBIENCE_ID, areAllEffectsAmbient(collection));
            this.entityData.set(DATA_EFFECT_COLOR_ID, PotionUtils.getColor(collection));
            this.setInvisible(this.hasEffect(MobEffects.INVISIBILITY));
        }

    }

    private void updateGlowingStatus() {
        boolean flag = this.isCurrentlyGlowing();
        if (this.getSharedFlag(6) != flag) {
            this.setSharedFlag(6, flag);
        }

    }

    public double getVisibilityPercent(@Nullable Entity entity) {
        double d0 = 1.0D;
        if (this.isDiscrete()) {
            d0 *= 0.8D;
        }

        if (this.isInvisible()) {
            float f = this.getArmorCoverPercentage();
            if (f < 0.1F) {
                f = 0.1F;
            }

            d0 *= 0.7D * (double)f;
        }

        if (entity != null) {
            ItemStack itemstack = this.getItemBySlot(EquipmentSlot.HEAD);
            EntityType<?> entitytypes = entity.getType();
            if (entitytypes == EntityType.SKELETON && itemstack.is(Items.SKELETON_SKULL) || entitytypes == EntityType.ZOMBIE && itemstack.is(Items.ZOMBIE_HEAD) || entitytypes == EntityType.CREEPER && itemstack.is(Items.CREEPER_HEAD)) {
                d0 *= 0.5D;
            }
        }

        return d0;
    }

    public boolean canAttack(LivingEntity entityliving) {
        return entityliving instanceof Player && this.level.getDifficulty() == Difficulty.PEACEFUL ? false : entityliving.canBeSeenAsEnemy();
    }

    public boolean canAttack(LivingEntity entityliving, TargetingConditions pathfindertargetcondition) {
        return pathfindertargetcondition.test(this, entityliving);
    }

    public boolean canBeSeenAsEnemy() {
        return !this.isInvulnerable() && this.canBeSeenByAnyone();
    }

    public boolean canBeSeenByAnyone() {
        return !this.isSpectator() && this.isAlive();
    }

    public static boolean areAllEffectsAmbient(Collection<MobEffectInstance> collection) {
        Iterator iterator = collection.iterator();

        MobEffectInstance mobeffect;
        do {
            if (!iterator.hasNext()) {
                return true;
            }

            mobeffect = (MobEffectInstance)iterator.next();
        } while(!mobeffect.isVisible() || mobeffect.isAmbient());

        return false;
    }

    protected void removeEffectParticles() {
        this.entityData.set(DATA_EFFECT_AMBIENCE_ID, false);
        this.entityData.set(DATA_EFFECT_COLOR_ID, 0);
    }

    public boolean removeAllEffects() {
        return this.removeAllEffects(Cause.UNKNOWN);
    }

    public boolean removeAllEffects(Cause cause) {
        if (this.level.isClientSide) {
            return false;
        } else {
            Iterator<MobEffectInstance> iterator = this.activeEffects.values().iterator();

            boolean flag;
            for(flag = false; iterator.hasNext(); flag = true) {
                MobEffectInstance effect = (MobEffectInstance)iterator.next();
                EntityPotionEffectEvent event = CraftEventFactory.callEntityPotionEffectChangeEvent(this, effect, (MobEffectInstance)null, cause, Action.CLEARED);
                if (!event.isCancelled()) {
                    this.onEffectRemoved(effect);
                    iterator.remove();
                }
            }

            return flag;
        }
    }

    public Collection<MobEffectInstance> getActiveEffects() {
        return this.activeEffects.values();
    }

    public Map<MobEffect, MobEffectInstance> getActiveEffectsMap() {
        return this.activeEffects;
    }

    public boolean hasEffect(MobEffect mobeffectlist) {
        return this.activeEffects.containsKey(mobeffectlist);
    }

    @Nullable
    public MobEffectInstance getEffect(MobEffect mobeffectlist) {
        return (MobEffectInstance)this.activeEffects.get(mobeffectlist);
    }

    public final boolean addEffect(MobEffectInstance mobeffect) {
        return this.addEffect(mobeffect, (Entity)null);
    }

    public boolean addEffect(MobEffectInstance mobeffect, Cause cause) {
        return this.addEffect(mobeffect, (Entity)null, cause);
    }

    public boolean addEffect(MobEffectInstance mobeffect, @Nullable Entity entity) {
        return this.addEffect(mobeffect, entity, Cause.UNKNOWN);
    }

    public boolean addEffect(MobEffectInstance mobeffect, @Nullable Entity entity, Cause cause) {
        AsyncCatcher.catchOp("effect add");
        if (this.isTickingEffects) {
            this.effectsToProcess.add(new LivingEntity.ProcessableEffect(mobeffect, cause));
            return true;
        } else if (!this.canBeAffected(mobeffect)) {
            return false;
        } else {
            MobEffectInstance mobeffect1 = (MobEffectInstance)this.activeEffects.get(mobeffect.getEffect());
            boolean override = false;
            if (mobeffect1 != null) {
                override = (new MobEffectInstance(mobeffect1)).update(mobeffect);
            }

            EntityPotionEffectEvent event = CraftEventFactory.callEntityPotionEffectChangeEvent(this, mobeffect1, mobeffect, cause, override);
            if (event.isCancelled()) {
                return false;
            } else if (mobeffect1 == null) {
                this.activeEffects.put(mobeffect.getEffect(), mobeffect);
                this.onEffectAdded(mobeffect, entity);
                return true;
            } else if (event.isOverride()) {
                mobeffect1.update(mobeffect);
                this.onEffectUpdated(mobeffect1, true, entity);
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean canBeAffected(MobEffectInstance mobeffect) {
        if (this.getMobType() == MobType.UNDEAD) {
            MobEffect mobeffectlist = mobeffect.getEffect();
            if (mobeffectlist == MobEffects.REGENERATION || mobeffectlist == MobEffects.POISON) {
                return false;
            }
        }

        return true;
    }

    public void forceAddEffect(MobEffectInstance mobeffect, @Nullable Entity entity) {
        if (this.canBeAffected(mobeffect)) {
            MobEffectInstance mobeffect1 = (MobEffectInstance)this.activeEffects.put(mobeffect.getEffect(), mobeffect);
            if (mobeffect1 == null) {
                this.onEffectAdded(mobeffect, entity);
            } else {
                this.onEffectUpdated(mobeffect, true, entity);
            }
        }

    }

    public boolean isInvertedHealAndHarm() {
        return this.getMobType() == MobType.UNDEAD;
    }

    @Nullable
    public MobEffectInstance removeEffectNoUpdate(@Nullable MobEffect mobeffectlist) {
        return this.c(mobeffectlist, Cause.UNKNOWN);
    }

    @Nullable
    public MobEffectInstance c(@Nullable MobEffect mobeffectlist, Cause cause) {
        if (this.isTickingEffects) {
            this.effectsToProcess.add(new LivingEntity.ProcessableEffect(mobeffectlist, cause));
            return null;
        } else {
            MobEffectInstance effect = (MobEffectInstance)this.activeEffects.get(mobeffectlist);
            if (effect == null) {
                return null;
            } else {
                EntityPotionEffectEvent event = CraftEventFactory.callEntityPotionEffectChangeEvent(this, effect, (MobEffectInstance)null, cause);
                return event.isCancelled() ? null : (MobEffectInstance)this.activeEffects.remove(mobeffectlist);
            }
        }
    }

    public boolean removeEffect(MobEffect mobeffectlist) {
        return this.removeEffect(mobeffectlist, Cause.UNKNOWN);
    }

    public boolean removeEffect(MobEffect mobeffectlist, Cause cause) {
        MobEffectInstance mobeffect = this.c(mobeffectlist, cause);
        if (mobeffect != null) {
            this.onEffectRemoved(mobeffect);
            return true;
        } else {
            return false;
        }
    }

    protected void onEffectAdded(MobEffectInstance mobeffect, @Nullable Entity entity) {
        this.effectsDirty = true;
        if (!this.level.isClientSide) {
            mobeffect.getEffect().addAttributeModifiers(this, this.getAttributes(), mobeffect.getAmplifier());
        }

    }

    protected void onEffectUpdated(MobEffectInstance mobeffect, boolean flag, @Nullable Entity entity) {
        this.effectsDirty = true;
        if (flag && !this.level.isClientSide) {
            MobEffect mobeffectlist = mobeffect.getEffect();
            mobeffectlist.removeAttributeModifiers(this, this.getAttributes(), mobeffect.getAmplifier());
            mobeffectlist.addAttributeModifiers(this, this.getAttributes(), mobeffect.getAmplifier());
        }

    }

    protected void onEffectRemoved(MobEffectInstance mobeffect) {
        this.effectsDirty = true;
        if (!this.level.isClientSide) {
            mobeffect.getEffect().removeAttributeModifiers(this, this.getAttributes(), mobeffect.getAmplifier());
        }

    }

    public void heal(float f) {
        this.heal(f, RegainReason.CUSTOM);
    }

    public void heal(float f, RegainReason regainReason) {
        float f1 = this.getHealth();
        if (f1 > 0.0F) {
            EntityRegainHealthEvent event = new EntityRegainHealthEvent(this.getBukkitEntity(), (double)f, regainReason);
            if (this.valid) {
                this.level.getCraftServer().getPluginManager().callEvent(event);
            }

            if (!event.isCancelled()) {
                this.setHealth((float)((double)this.getHealth() + event.getAmount()));
            }
        }

    }

    public float getHealth() {
        return this instanceof ServerPlayer ? (float)((ServerPlayer)this).getBukkitEntity().getHealth() : (Float)this.entityData.get(DATA_HEALTH_ID);
    }

    public void setHealth(float f) {
        if (this instanceof ServerPlayer) {
            CraftPlayer player = ((ServerPlayer)this).getBukkitEntity();
            if (f < 0.0F) {
                player.setRealHealth(0.0D);
            } else if ((double)f > player.getMaxHealth()) {
                player.setRealHealth(player.getMaxHealth());
            } else {
                player.setRealHealth((double)f);
            }

            player.updateScaledHealth(false);
        } else {
            this.entityData.set(DATA_HEALTH_ID, Mth.clamp(f, 0.0F, this.getMaxHealth()));
        }
    }

    public boolean isDeadOrDying() {
        return this.getHealth() <= 0.0F;
    }

    public boolean hurt(DamageSource damagesource, float f) {
        if (this.isInvulnerableTo(damagesource)) {
            return false;
        } else if (this.level.isClientSide) {
            return false;
        } else if (!this.isRemoved() && !this.dead && !(this.getHealth() <= 0.0F)) {
            if (damagesource.isFire() && this.hasEffect(MobEffects.FIRE_RESISTANCE)) {
                return false;
            } else {
                if (this.isSleeping() && !this.level.isClientSide) {
                    this.stopSleeping();
                }

                this.noActionTime = 0;
                boolean flag = f > 0.0F && this.isDamageSourceBlocked(damagesource);
                float f2 = 0.0F;
                this.animationSpeed = 1.5F;
                boolean flag1 = true;
                if ((float)this.invulnerableTime > (float)this.invulnerableDuration / 2.0F) {
                    if (f <= this.lastHurt) {
                        return false;
                    }

                    if (!this.damageEntity0(damagesource, f - this.lastHurt)) {
                        return false;
                    }

                    this.lastHurt = f;
                    flag1 = false;
                } else {
                    if (!this.damageEntity0(damagesource, f)) {
                        return false;
                    }

                    this.lastHurt = f;
                    this.invulnerableTime = this.invulnerableDuration;
                    this.hurtDuration = 10;
                    this.hurtTime = this.hurtDuration;
                }

                if (this instanceof Animal) {
                    ((Animal)this).resetLove();
                    if (this instanceof TamableAnimal) {
                        ((TamableAnimal)this).setOrderedToSit(false);
                    }
                }

                this.hurtDir = 0.0F;
                Entity entity1 = damagesource.getEntity();
                if (entity1 != null) {
                    if (entity1 instanceof LivingEntity && !damagesource.isNoAggro()) {
                        this.setLastHurtByMob((LivingEntity)entity1);
                    }

                    if (entity1 instanceof Player) {
                        this.lastHurtByPlayerTime = 100;
                        this.lastHurtByPlayer = (Player)entity1;
                    } else if (entity1 instanceof Wolf) {
                        Wolf entitywolf = (Wolf)entity1;
                        if (entitywolf.isTame()) {
                            this.lastHurtByPlayerTime = 100;
                            LivingEntity entityliving = entitywolf.getOwner();
                            if (entityliving != null && entityliving.getType() == EntityType.PLAYER) {
                                this.lastHurtByPlayer = (Player)entityliving;
                            } else {
                                this.lastHurtByPlayer = null;
                            }
                        }
                    }
                }

                if (flag1) {
                    if (flag) {
                        this.level.broadcastEntityEvent(this, (byte)29);
                    } else if (damagesource instanceof EntityDamageSource && ((EntityDamageSource)damagesource).isThorns()) {
                        this.level.broadcastEntityEvent(this, (byte)33);
                    } else {
                        byte b0;
                        if (damagesource == DamageSource.DROWN) {
                            b0 = 36;
                        } else if (damagesource.isFire()) {
                            b0 = 37;
                        } else if (damagesource == DamageSource.SWEET_BERRY_BUSH) {
                            b0 = 44;
                        } else if (damagesource == DamageSource.FREEZE) {
                            b0 = 57;
                        } else {
                            b0 = 2;
                        }

                        this.level.broadcastEntityEvent(this, b0);
                    }

                    if (damagesource != DamageSource.DROWN && (!flag || f > 0.0F)) {
                        this.markHurt();
                    }

                    if (entity1 != null) {
                        double d0 = entity1.getX() - this.getX();

                        double d1;
                        for(d1 = entity1.getZ() - this.getZ(); d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D) {
                            d0 = (Math.random() - Math.random()) * 0.01D;
                        }

                        this.hurtDir = (float)(Mth.atan2(d1, d0) * 57.2957763671875D - (double)this.getYRot());
                        this.knockback(0.4000000059604645D, d0, d1);
                    } else {
                        this.hurtDir = (float)((int)(Math.random() * 2.0D) * 180);
                    }
                }

                if (this.isDeadOrDying()) {
                    if (!this.checkTotemDeathProtection(damagesource)) {
                        SoundEvent soundeffect = this.getDeathSound();
                        if (flag1 && soundeffect != null) {
                            this.playSound(soundeffect, this.getSoundVolume(), this.getVoicePitch());
                        }

                        this.die(damagesource);
                    }
                } else if (flag1) {
                    this.playHurtSound(damagesource);
                }

                boolean flag2 = !flag || f > 0.0F;
                if (flag2) {
                    this.lastDamageSource = damagesource;
                    this.lastDamageStamp = this.level.getGameTime();
                }

                if (this instanceof ServerPlayer) {
                    CriteriaTriggers.ENTITY_HURT_PLAYER.trigger((ServerPlayer)this, damagesource, f, f, flag);
                    if (f2 > 0.0F && f2 < 3.4028235E37F) {
                        ((ServerPlayer)this).awardStat(Stats.DAMAGE_BLOCKED_BY_SHIELD, Math.round(f2 * 10.0F));
                    }
                }

                if (entity1 instanceof ServerPlayer) {
                    CriteriaTriggers.PLAYER_HURT_ENTITY.trigger((ServerPlayer)entity1, this, damagesource, f, f, flag);
                }

                return flag2;
            }
        } else {
            return false;
        }
    }

    protected void blockUsingShield(LivingEntity entityliving) {
        entityliving.blockedByShield(this);
    }

    protected void blockedByShield(LivingEntity entityliving) {
        entityliving.knockback(0.5D, entityliving.getX() - this.getX(), entityliving.getZ() - this.getZ());
    }

    private boolean checkTotemDeathProtection(DamageSource damagesource) {
        if (damagesource.isBypassInvul()) {
            return false;
        } else {
            ItemStack itemstack = null;
            InteractionHand[] aenumhand = InteractionHand.values();
            int i = aenumhand.length;
            ItemStack itemstack1 = ItemStack.EMPTY;

            for(int j = 0; j < i; ++j) {
                InteractionHand enumhand = aenumhand[j];
                itemstack1 = this.getItemInHand(enumhand);
                if (itemstack1.is(Items.TOTEM_OF_UNDYING)) {
                    itemstack = itemstack1.copy();
                    break;
                }
            }

            EntityResurrectEvent event = new EntityResurrectEvent((org.bukkit.entity.LivingEntity)this.getBukkitEntity());
            event.setCancelled(itemstack == null);
            this.level.getCraftServer().getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                if (!itemstack1.isEmpty()) {
                    itemstack1.shrink(1);
                }

                if (itemstack != null && this instanceof ServerPlayer) {
                    ServerPlayer entityplayer = (ServerPlayer)this;
                    entityplayer.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING));
                    CriteriaTriggers.USED_TOTEM.trigger(entityplayer, itemstack);
                }

                this.setHealth(1.0F);
                this.removeAllEffects(Cause.TOTEM);
                this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1), Cause.TOTEM);
                this.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1), Cause.TOTEM);
                this.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0), Cause.TOTEM);
                this.level.broadcastEntityEvent(this, (byte)35);
            }

            return !event.isCancelled();
        }
    }

    @Nullable
    public DamageSource getLastDamageSource() {
        if (this.level.getGameTime() - this.lastDamageStamp > 40L) {
            this.lastDamageSource = null;
        }

        return this.lastDamageSource;
    }

    protected void playHurtSound(DamageSource damagesource) {
        SoundEvent soundeffect = this.getHurtSound(damagesource);
        if (soundeffect != null) {
            this.playSound(soundeffect, this.getSoundVolume(), this.getVoicePitch());
        }

    }

    public boolean isDamageSourceBlocked(DamageSource damagesource) {
        Entity entity = damagesource.getDirectEntity();
        boolean flag = false;
        if (entity instanceof AbstractArrow) {
            AbstractArrow entityarrow = (AbstractArrow)entity;
            if (entityarrow.getPierceLevel() > 0) {
                flag = true;
            }
        }

        if (!damagesource.isBypassArmor() && this.isBlocking() && !flag) {
            Vec3 vec3d = damagesource.getSourcePosition();
            if (vec3d != null) {
                Vec3 vec3d1 = this.getViewVector(1.0F);
                Vec3 vec3d2 = vec3d.vectorTo(this.position()).normalize();
                vec3d2 = new Vec3(vec3d2.x, 0.0D, vec3d2.z);
                if (vec3d2.dot(vec3d1) < 0.0D) {
                    return true;
                }
            }
        }

        return false;
    }

    private void breakItem(ItemStack itemstack) {
        if (!itemstack.isEmpty()) {
            if (!this.isSilent()) {
                this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ITEM_BREAK, this.getSoundSource(), 0.8F, 0.8F + this.level.random.nextFloat() * 0.4F, false);
            }

            this.spawnItemParticles(itemstack, 5);
        }

    }

    public void die(DamageSource damagesource) {
        if (!this.isRemoved() && !this.dead) {
            Entity entity = damagesource.getEntity();
            LivingEntity entityliving = this.getKillCredit();
            if (this.deathScore >= 0 && entityliving != null) {
                entityliving.awardKillScore(this, this.deathScore, damagesource);
            }

            if (this.isSleeping()) {
                this.stopSleeping();
            }

            if (!this.level.isClientSide && this.hasCustomName() && SpigotConfig.logNamedDeaths) {
                LOGGER.info("Named entity {} died: {}", this, this.getCombatTracker().getDeathMessage().getString());
            }

            this.dead = true;
            this.getCombatTracker().recheckStatus();
            if (this.level instanceof ServerLevel) {
                if (entity != null) {
                    entity.killed((ServerLevel)this.level, this);
                }

                this.dropAllDeathLoot(damagesource);
                this.createWitherRose(entityliving);
            }

            this.level.broadcastEntityEvent(this, (byte)3);
            this.setPose(Pose.DYING);
        }

    }

    protected void createWitherRose(@Nullable LivingEntity entityliving) {
        if (!this.level.isClientSide) {
            boolean flag = false;
            if (entityliving instanceof WitherBoss) {
                if (this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                    BlockPos blockposition = this.blockPosition();
                    BlockState iblockdata = Blocks.WITHER_ROSE.defaultBlockState();
                    if (this.level.getBlockState(blockposition).isAir() && iblockdata.canSurvive(this.level, blockposition)) {
                        flag = CraftEventFactory.handleBlockFormEvent(this.level, blockposition, iblockdata, 3, this);
                    }
                }

                if (!flag) {
                    ItemEntity entityitem = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), new ItemStack(Items.WITHER_ROSE));
                    EntityDropItemEvent event = new EntityDropItemEvent(this.getBukkitEntity(), (Item)entityitem.getBukkitEntity());
                    CraftEventFactory.callEvent(event);
                    if (event.isCancelled()) {
                        return;
                    }

                    this.level.addFreshEntity(entityitem);
                }
            }
        }

    }

    protected void dropAllDeathLoot(DamageSource damagesource) {
        Entity entity = damagesource.getEntity();
        int i;
        if (entity instanceof Player) {
            i = EnchantmentHelper.getMobLooting((LivingEntity)entity);
        } else {
            i = 0;
        }

        boolean flag = this.lastHurtByPlayerTime > 0;
        this.dropEquipment();
        if (this.shouldDropLoot() && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            this.dropFromLootTable(damagesource, flag);
            this.dropCustomDeathLoot(damagesource, i, flag);
        }

        CraftEventFactory.callEntityDeathEvent(this, this.drops);
        this.drops = new ArrayList();
        this.dropExperience();
    }

    protected void dropEquipment() {
    }

    public int getExpReward() {
        if (!(this.level instanceof ServerLevel) || !this.isAlwaysExperienceDropper() && (this.lastHurtByPlayerTime <= 0 || !this.shouldDropExperience() || !this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT))) {
            return 0;
        } else {
            int i = this.getExperienceReward(this.lastHurtByPlayer);
            return i;
        }
    }

    protected void dropExperience() {
        ExperienceOrb.award((ServerLevel)this.level, this.position(), this.expToDrop);
        this.expToDrop = 0;
    }

    protected void dropCustomDeathLoot(DamageSource damagesource, int i, boolean flag) {
    }

    public ResourceLocation getLootTable() {
        return this.getType().getDefaultLootTable();
    }

    protected void dropFromLootTable(DamageSource damagesource, boolean flag) {
        ResourceLocation minecraftkey = this.getLootTable();
        LootTable loottable = this.level.getServer().getLootTables().get(minecraftkey);
        net.minecraft.world.level.storage.loot.LootContext.Builder loottableinfo_builder = this.createLootContext(flag, damagesource);
        loottable.getRandomItems(loottableinfo_builder.create(LootContextParamSets.ENTITY), this::spawnAtLocation);
    }

    protected net.minecraft.world.level.storage.loot.LootContext.Builder createLootContext(boolean flag, DamageSource damagesource) {
        net.minecraft.world.level.storage.loot.LootContext.Builder loottableinfo_builder = (new net.minecraft.world.level.storage.loot.LootContext.Builder((ServerLevel)this.level)).withRandom(this.random).withParameter(LootContextParams.THIS_ENTITY, this).withParameter(LootContextParams.ORIGIN, this.position()).withParameter(LootContextParams.DAMAGE_SOURCE, damagesource).withOptionalParameter(LootContextParams.KILLER_ENTITY, damagesource.getEntity()).withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, damagesource.getDirectEntity());
        if (flag && this.lastHurtByPlayer != null) {
            loottableinfo_builder = loottableinfo_builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, this.lastHurtByPlayer).withLuck(this.lastHurtByPlayer.getLuck());
        }

        return loottableinfo_builder;
    }

    public void knockback(double d0, double d1, double d2) {
        d0 *= 1.0D - this.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
        if (d0 > 0.0D) {
            this.hasImpulse = true;
            Vec3 vec3d = this.getDeltaMovement();
            Vec3 vec3d1 = (new Vec3(d1, 0.0D, d2)).normalize().scale(d0);
            this.setDeltaMovement(vec3d.x / 2.0D - vec3d1.x, this.onGround ? Math.min(0.4D, vec3d.y / 2.0D + d0) : vec3d.y, vec3d.z / 2.0D - vec3d1.z);
        }

    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return SoundEvents.GENERIC_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.GENERIC_DEATH;
    }

    private SoundEvent getFallDamageSound(int i) {
        return i > 4 ? this.getFallSounds().big() : this.getFallSounds().small();
    }

    public LivingEntity.Fallsounds getFallSounds() {
        return new LivingEntity.Fallsounds(SoundEvents.GENERIC_SMALL_FALL, SoundEvents.GENERIC_BIG_FALL);
    }

    protected SoundEvent getDrinkingSound(ItemStack itemstack) {
        return itemstack.getDrinkingSound();
    }

    public SoundEvent getEatingSound(ItemStack itemstack) {
        return itemstack.getEatingSound();
    }

    public void setOnGround(boolean flag) {
        super.setOnGround(flag);
        if (flag) {
            this.lastClimbablePos = Optional.empty();
        }

    }

    public Optional<BlockPos> getLastClimbablePos() {
        return this.lastClimbablePos;
    }

    public boolean onClimbable() {
        if (this.isSpectator()) {
            return false;
        } else {
            BlockPos blockposition = this.blockPosition();
            BlockState iblockdata = this.getFeetBlockState();
            if (iblockdata.is(BlockTags.CLIMBABLE)) {
                this.lastClimbablePos = Optional.of(blockposition);
                return true;
            } else if (iblockdata.getBlock() instanceof TrapDoorBlock && this.trapdoorUsableAsLadder(blockposition, iblockdata)) {
                this.lastClimbablePos = Optional.of(blockposition);
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean trapdoorUsableAsLadder(BlockPos blockposition, BlockState iblockdata) {
        if ((Boolean)iblockdata.getValue(TrapDoorBlock.OPEN)) {
            BlockState iblockdata1 = this.level.getBlockState(blockposition.below());
            if (iblockdata1.is(Blocks.LADDER) && iblockdata1.getValue(LadderBlock.FACING) == iblockdata.getValue(TrapDoorBlock.FACING)) {
                return true;
            }
        }

        return false;
    }

    public boolean isAlive() {
        return !this.isRemoved() && this.getHealth() > 0.0F;
    }

    public boolean causeFallDamage(float f, float f1, DamageSource damagesource) {
        boolean flag = super.causeFallDamage(f, f1, damagesource);
        int i = this.calculateFallDamage(f, f1);
        if (i > 0) {
            if (!this.hurt(damagesource, (float)i)) {
                return true;
            } else {
                this.playSound(this.getFallDamageSound(i), 1.0F, 1.0F);
                this.playBlockFallSound();
                return true;
            }
        } else {
            return flag;
        }
    }

    protected int calculateFallDamage(float f, float f1) {
        MobEffectInstance mobeffect = this.getEffect(MobEffects.JUMP);
        float f2 = mobeffect == null ? 0.0F : (float)(mobeffect.getAmplifier() + 1);
        return Mth.ceil((f - 3.0F - f2) * f1);
    }

    protected void playBlockFallSound() {
        if (!this.isSilent()) {
            int i = Mth.floor(this.getX());
            int j = Mth.floor(this.getY() - 0.20000000298023224D);
            int k = Mth.floor(this.getZ());
            BlockState iblockdata = this.level.getBlockState(new BlockPos(i, j, k));
            if (!iblockdata.isAir()) {
                SoundType soundeffecttype = iblockdata.getSoundType();
                this.playSound(soundeffecttype.getFallSound(), soundeffecttype.getVolume() * 0.5F, soundeffecttype.getPitch() * 0.75F);
            }
        }

    }

    public void animateHurt() {
        this.hurtDuration = 10;
        this.hurtTime = this.hurtDuration;
        this.hurtDir = 0.0F;
    }

    public int getArmorValue() {
        return Mth.floor(this.getAttributeValue(Attributes.ARMOR));
    }

    protected void hurtArmor(DamageSource damagesource, float f) {
    }

    protected void hurtHelmet(DamageSource damagesource, float f) {
    }

    protected void hurtCurrentlyUsedShield(float f) {
    }

    protected float getDamageAfterArmorAbsorb(DamageSource damagesource, float f) {
        if (!damagesource.isBypassArmor()) {
            f = CombatRules.getDamageAfterAbsorb(f, (float)this.getArmorValue(), (float)this.getAttributeValue(Attributes.ARMOR_TOUGHNESS));
        }

        return f;
    }

    protected float getDamageAfterMagicAbsorb(DamageSource damagesource, float f) {
        if (damagesource.isBypassMagic()) {
            return f;
        } else if (f <= 0.0F) {
            return 0.0F;
        } else {
            int i = EnchantmentHelper.getDamageProtection(this.getArmorSlots(), damagesource);
            if (i > 0) {
                f = CombatRules.getDamageAfterMagicAbsorb(f, (float)i);
            }

            return f;
        }
    }

    protected boolean damageEntity0(final DamageSource damagesource, float f) {
        if (!this.isInvulnerableTo(damagesource)) {
            boolean human = this instanceof Player;
            float originalDamage = f;
            Function<Double, Double> hardHat = new Function<Double, Double>() {
                public Double apply(Double f) {
                    return damagesource.isDamageHelmet() && !LivingEntity.this.getItemBySlot(EquipmentSlot.HEAD).isEmpty() ? -(f - f * 0.75D) : -0.0D;
                }
            };
            float hardHatModifier = ((Double)hardHat.apply((double)f)).floatValue();
            f += hardHatModifier;
            Function<Double, Double> blocking = new Function<Double, Double>() {
                public Double apply(Double f) {
                    return -(LivingEntity.this.isDamageSourceBlocked(damagesource) ? f : 0.0D);
                }
            };
            float blockingModifier = ((Double)blocking.apply((double)f)).floatValue();
            f += blockingModifier;
            Function<Double, Double> armor = new Function<Double, Double>() {
                public Double apply(Double f) {
                    return -(f - (double)LivingEntity.this.getDamageAfterArmorAbsorb(damagesource, f.floatValue()));
                }
            };
            float armorModifier = ((Double)armor.apply((double)f)).floatValue();
            f += armorModifier;
            Function<Double, Double> resistance = new Function<Double, Double>() {
                public Double apply(Double f) {
                    if (!damagesource.isBypassMagic() && LivingEntity.this.hasEffect(MobEffects.DAMAGE_RESISTANCE) && damagesource != DamageSource.OUT_OF_WORLD) {
                        int i = (LivingEntity.this.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier() + 1) * 5;
                        int j = 25 - i;
                        float f1 = f.floatValue() * (float)j;
                        return -(f - (double)(f1 / 25.0F));
                    } else {
                        return -0.0D;
                    }
                }
            };
            float resistanceModifier = ((Double)resistance.apply((double)f)).floatValue();
            f += resistanceModifier;
            Function<Double, Double> magic = new Function<Double, Double>() {
                public Double apply(Double f) {
                    return -(f - (double)LivingEntity.this.getDamageAfterMagicAbsorb(damagesource, f.floatValue()));
                }
            };
            float magicModifier = ((Double)magic.apply((double)f)).floatValue();
            f += magicModifier;
            Function<Double, Double> absorption = new Function<Double, Double>() {
                public Double apply(Double f) {
                    return -Math.max(f - Math.max(f - (double)LivingEntity.this.getAbsorptionAmount(), 0.0D), 0.0D);
                }
            };
            float absorptionModifier = ((Double)absorption.apply((double)f)).floatValue();
            EntityDamageEvent event = CraftEventFactory.handleLivingEntityDamageEvent(this, damagesource, (double)originalDamage, (double)hardHatModifier, (double)blockingModifier, (double)armorModifier, (double)resistanceModifier, (double)magicModifier, (double)absorptionModifier, hardHat, blocking, armor, resistance, magic, absorption);
            if (damagesource.getEntity() instanceof Player) {
                ((Player)damagesource.getEntity()).resetAttackStrengthTicker();
            }

            if (event.isCancelled()) {
                return false;
            } else {
                f = (float)event.getFinalDamage();
                float f2;
                if (event.getDamage(DamageModifier.RESISTANCE) < 0.0D) {
                    f2 = (float)(-event.getDamage(DamageModifier.RESISTANCE));
                    if (f2 > 0.0F && f2 < 3.4028235E37F) {
                        if (this instanceof ServerPlayer) {
                            ((ServerPlayer)this).awardStat(Stats.DAMAGE_RESISTED, Math.round(f2 * 10.0F));
                        } else if (damagesource.getEntity() instanceof ServerPlayer) {
                            ((ServerPlayer)damagesource.getEntity()).awardStat(Stats.DAMAGE_DEALT_RESISTED, Math.round(f2 * 10.0F));
                        }
                    }
                }

                if (damagesource.isDamageHelmet() && !this.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
                    this.hurtHelmet(damagesource, f);
                }

                if (!damagesource.isBypassArmor()) {
                    f2 = (float)(event.getDamage() + event.getDamage(DamageModifier.BLOCKING) + event.getDamage(DamageModifier.HARD_HAT));
                    this.hurtArmor(damagesource, f2);
                }

                if (event.getDamage(DamageModifier.BLOCKING) < 0.0D) {
                    this.level.broadcastEntityEvent(this, (byte)29);
                    this.hurtCurrentlyUsedShield((float)(-event.getDamage(DamageModifier.BLOCKING)));
                    Entity entity = damagesource.getDirectEntity();
                    if (entity instanceof LivingEntity) {
                        this.blockUsingShield((LivingEntity)entity);
                    }
                }

                absorptionModifier = (float)(-event.getDamage(DamageModifier.ABSORPTION));
                this.setAbsorptionAmount(Math.max(this.getAbsorptionAmount() - absorptionModifier, 0.0F));
                if (absorptionModifier > 0.0F && absorptionModifier < 3.4028235E37F && this instanceof Player) {
                    ((Player)this).awardStat(Stats.DAMAGE_ABSORBED, Math.round(absorptionModifier * 10.0F));
                }

                if (absorptionModifier > 0.0F && absorptionModifier < 3.4028235E37F && damagesource.getEntity() instanceof ServerPlayer) {
                    ((ServerPlayer)damagesource.getEntity()).awardStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(absorptionModifier * 10.0F));
                }

                if (!(f > 0.0F) && human) {
                    if (event.getDamage(DamageModifier.BLOCKING) < 0.0D) {
                        if (this instanceof ServerPlayer) {
                            CriteriaTriggers.ENTITY_HURT_PLAYER.trigger((ServerPlayer)this, damagesource, f, originalDamage, true);
                            f2 = (float)(-event.getDamage(DamageModifier.BLOCKING));
                            if (f2 > 0.0F && f2 < 3.4028235E37F) {
                                ((ServerPlayer)this).awardStat(Stats.DAMAGE_BLOCKED_BY_SHIELD, Math.round(originalDamage * 10.0F));
                            }
                        }

                        if (damagesource.getEntity() instanceof ServerPlayer) {
                            CriteriaTriggers.PLAYER_HURT_ENTITY.trigger((ServerPlayer)damagesource.getEntity(), this, damagesource, f, originalDamage, true);
                        }

                        return false;
                    } else {
                        return originalDamage > 0.0F;
                    }
                } else {
                    if (human) {
                        ((Player)this).causeFoodExhaustion(damagesource.getFoodExhaustion(), ExhaustionReason.DAMAGED);
                        if (f < 3.4028235E37F) {
                            ((Player)this).awardStat(Stats.DAMAGE_TAKEN, Math.round(f * 10.0F));
                        }
                    }

                    float f3 = this.getHealth();
                    this.setHealth(f3 - f);
                    this.getCombatTracker().recordDamage(damagesource, f3, f);
                    if (!human) {
                        this.setAbsorptionAmount(this.getAbsorptionAmount() - f);
                    }

                    this.gameEvent(GameEvent.ENTITY_DAMAGED, damagesource.getEntity());
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    public CombatTracker getCombatTracker() {
        return this.combatTracker;
    }

    @Nullable
    public LivingEntity getKillCredit() {
        return (LivingEntity)(this.combatTracker.getKiller() != null ? this.combatTracker.getKiller() : (this.lastHurtByPlayer != null ? this.lastHurtByPlayer : (this.lastHurtByMob != null ? this.lastHurtByMob : null)));
    }

    public final float getMaxHealth() {
        return (float)this.getAttributeValue(Attributes.MAX_HEALTH);
    }

    public final int getArrowCount() {
        return (Integer)this.entityData.get(DATA_ARROW_COUNT_ID);
    }

    public final void setArrowCount(int i) {
        this.setArrowCount(i, false);
    }

    public final void setArrowCount(int i, boolean flag) {
        ArrowBodyCountChangeEvent event = CraftEventFactory.callArrowBodyCountChangeEvent(this, this.getArrowCount(), i, flag);
        if (!event.isCancelled()) {
            this.entityData.set(DATA_ARROW_COUNT_ID, event.getNewAmount());
        }
    }

    public final int getStingerCount() {
        return (Integer)this.entityData.get(DATA_STINGER_COUNT_ID);
    }

    public final void setStingerCount(int i) {
        this.entityData.set(DATA_STINGER_COUNT_ID, i);
    }

    private int getCurrentSwingDuration() {
        return MobEffectUtil.hasDigSpeed(this) ? 6 - (1 + MobEffectUtil.getDigSpeedAmplification(this)) : (this.hasEffect(MobEffects.DIG_SLOWDOWN) ? 6 + (1 + this.getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier()) * 2 : 6);
    }

    public void swing(InteractionHand enumhand) {
        this.swing(enumhand, false);
    }

    public void swing(InteractionHand enumhand, boolean flag) {
        if (!this.swinging || this.swingTime >= this.getCurrentSwingDuration() / 2 || this.swingTime < 0) {
            this.swingTime = -1;
            this.swinging = true;
            this.swingingArm = enumhand;
            if (this.level instanceof ServerLevel) {
                ClientboundAnimatePacket packetplayoutanimation = new ClientboundAnimatePacket(this, enumhand == InteractionHand.MAIN_HAND ? 0 : 3);
                ServerChunkCache chunkproviderserver = ((ServerLevel)this.level).getChunkSource();
                if (flag) {
                    chunkproviderserver.broadcastAndSend(this, packetplayoutanimation);
                } else {
                    chunkproviderserver.broadcast(this, packetplayoutanimation);
                }
            }
        }

    }

    public void handleEntityEvent(byte b0) {
        switch(b0) {
        case 2:
        case 33:
        case 36:
        case 37:
        case 44:
        case 57:
            this.animationSpeed = 1.5F;
            this.invulnerableTime = 20;
            this.hurtDuration = 10;
            this.hurtTime = this.hurtDuration;
            this.hurtDir = 0.0F;
            if (b0 == 33) {
                this.playSound(SoundEvents.THORNS_HIT, this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            }

            DamageSource damagesource;
            if (b0 == 37) {
                damagesource = DamageSource.ON_FIRE;
            } else if (b0 == 36) {
                damagesource = DamageSource.DROWN;
            } else if (b0 == 44) {
                damagesource = DamageSource.SWEET_BERRY_BUSH;
            } else if (b0 == 57) {
                damagesource = DamageSource.FREEZE;
            } else {
                damagesource = DamageSource.GENERIC;
            }

            SoundEvent soundeffect = this.getHurtSound(damagesource);
            if (soundeffect != null) {
                this.playSound(soundeffect, this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            }

            this.hurt(DamageSource.GENERIC, 0.0F);
            this.lastDamageSource = damagesource;
            this.lastDamageStamp = this.level.getGameTime();
            break;
        case 3:
            SoundEvent soundeffect1 = this.getDeathSound();
            if (soundeffect1 != null) {
                this.playSound(soundeffect1, this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            }

            if (!(this instanceof Player)) {
                this.setHealth(0.0F);
                this.die(DamageSource.GENERIC);
            }
            break;
        case 4:
        case 5:
        case 6:
        case 7:
        case 8:
        case 9:
        case 10:
        case 11:
        case 12:
        case 13:
        case 14:
        case 15:
        case 16:
        case 17:
        case 18:
        case 19:
        case 20:
        case 21:
        case 22:
        case 23:
        case 24:
        case 25:
        case 26:
        case 27:
        case 28:
        case 31:
        case 32:
        case 34:
        case 35:
        case 38:
        case 39:
        case 40:
        case 41:
        case 42:
        case 43:
        case 45:
        case 53:
        case 56:
        case 58:
        case 59:
        default:
            super.handleEntityEvent(b0);
            break;
        case 29:
            this.playSound(SoundEvents.SHIELD_BLOCK, 1.0F, 0.8F + this.level.random.nextFloat() * 0.4F);
            break;
        case 30:
            this.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + this.level.random.nextFloat() * 0.4F);
            break;
        case 46:
            boolean flag = true;

            for(int i = 0; i < 128; ++i) {
                double d0 = (double)i / 127.0D;
                float f = (this.random.nextFloat() - 0.5F) * 0.2F;
                float f1 = (this.random.nextFloat() - 0.5F) * 0.2F;
                float f2 = (this.random.nextFloat() - 0.5F) * 0.2F;
                double d1 = Mth.lerp(d0, this.xo, this.getX()) + (this.random.nextDouble() - 0.5D) * (double)this.getBbWidth() * 2.0D;
                double d2 = Mth.lerp(d0, this.yo, this.getY()) + this.random.nextDouble() * (double)this.getBbHeight();
                double d3 = Mth.lerp(d0, this.zo, this.getZ()) + (this.random.nextDouble() - 0.5D) * (double)this.getBbWidth() * 2.0D;
                this.level.addParticle(ParticleTypes.PORTAL, d1, d2, d3, (double)f, (double)f1, (double)f2);
            }

            return;
        case 47:
            this.breakItem(this.getItemBySlot(EquipmentSlot.MAINHAND));
            break;
        case 48:
            this.breakItem(this.getItemBySlot(EquipmentSlot.OFFHAND));
            break;
        case 49:
            this.breakItem(this.getItemBySlot(EquipmentSlot.HEAD));
            break;
        case 50:
            this.breakItem(this.getItemBySlot(EquipmentSlot.CHEST));
            break;
        case 51:
            this.breakItem(this.getItemBySlot(EquipmentSlot.LEGS));
            break;
        case 52:
            this.breakItem(this.getItemBySlot(EquipmentSlot.FEET));
            break;
        case 54:
            HoneyBlock.showJumpParticles(this);
            break;
        case 55:
            this.swapHandItems();
            break;
        case 60:
            this.makePoofParticles();
        }

    }

    private void makePoofParticles() {
        for(int i = 0; i < 20; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level.addParticle(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), d0, d1, d2);
        }

    }

    private void swapHandItems() {
        ItemStack itemstack = this.getItemBySlot(EquipmentSlot.OFFHAND);
        this.setItemSlot(EquipmentSlot.OFFHAND, this.getItemBySlot(EquipmentSlot.MAINHAND));
        this.setItemSlot(EquipmentSlot.MAINHAND, itemstack);
    }

    protected void outOfWorld() {
        this.hurt(DamageSource.OUT_OF_WORLD, 4.0F);
    }

    protected void updateSwingTime() {
        int i = this.getCurrentSwingDuration();
        if (this.swinging) {
            ++this.swingTime;
            if (this.swingTime >= i) {
                this.swingTime = 0;
                this.swinging = false;
            }
        } else {
            this.swingTime = 0;
        }

        this.attackAnim = (float)this.swingTime / (float)i;
    }

    @Nullable
    public AttributeInstance getAttribute(Attribute attributebase) {
        return this.getAttributes().getInstance(attributebase);
    }

    public double getAttributeValue(Attribute attributebase) {
        return this.getAttributes().getValue(attributebase);
    }

    public double getAttributeBaseValue(Attribute attributebase) {
        return this.getAttributes().getBaseValue(attributebase);
    }

    public AttributeMap getAttributes() {
        return this.attributes;
    }

    public MobType getMobType() {
        return MobType.UNDEFINED;
    }

    public ItemStack getMainHandItem() {
        return this.getItemBySlot(EquipmentSlot.MAINHAND);
    }

    public ItemStack getOffhandItem() {
        return this.getItemBySlot(EquipmentSlot.OFFHAND);
    }

    public boolean isHolding(net.minecraft.world.item.Item item) {
        return this.isHolding((itemstack) -> {
            return itemstack.is(item);
        });
    }

    public boolean isHolding(Predicate<ItemStack> predicate) {
        return predicate.test(this.getMainHandItem()) || predicate.test(this.getOffhandItem());
    }

    public ItemStack getItemInHand(InteractionHand enumhand) {
        if (enumhand == InteractionHand.MAIN_HAND) {
            return this.getItemBySlot(EquipmentSlot.MAINHAND);
        } else if (enumhand == InteractionHand.OFF_HAND) {
            return this.getItemBySlot(EquipmentSlot.OFFHAND);
        } else {
            throw new IllegalArgumentException("Invalid hand " + enumhand);
        }
    }

    public void setItemInHand(InteractionHand enumhand, ItemStack itemstack) {
        if (enumhand == InteractionHand.MAIN_HAND) {
            this.setItemSlot(EquipmentSlot.MAINHAND, itemstack);
        } else {
            if (enumhand != InteractionHand.OFF_HAND) {
                throw new IllegalArgumentException("Invalid hand " + enumhand);
            }

            this.setItemSlot(EquipmentSlot.OFFHAND, itemstack);
        }

    }

    public boolean hasItemInSlot(EquipmentSlot enumitemslot) {
        return !this.getItemBySlot(enumitemslot).isEmpty();
    }

    public abstract Iterable<ItemStack> getArmorSlots();

    public abstract ItemStack getItemBySlot(EquipmentSlot var1);

    public void setItemSlot(EquipmentSlot enumitemslot, ItemStack itemstack, boolean silent) {
        this.setItemSlot(enumitemslot, itemstack);
    }

    public abstract void setItemSlot(EquipmentSlot var1, ItemStack var2);

    protected void verifyEquippedItem(ItemStack itemstack) {
        CompoundTag nbttagcompound = itemstack.getTag();
        if (nbttagcompound != null) {
            itemstack.getItem().verifyTagAfterLoad(nbttagcompound);
        }

    }

    public float getArmorCoverPercentage() {
        Iterable<ItemStack> iterable = this.getArmorSlots();
        int i = 0;
        int j = 0;

        for(Iterator iterator = iterable.iterator(); iterator.hasNext(); ++i) {
            ItemStack itemstack = (ItemStack)iterator.next();
            if (!itemstack.isEmpty()) {
                ++j;
            }
        }

        return i > 0 ? (float)j / (float)i : 0.0F;
    }

    public void setSprinting(boolean flag) {
        super.setSprinting(flag);
        AttributeInstance attributemodifiable = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attributemodifiable.getModifier(SPEED_MODIFIER_SPRINTING_UUID) != null) {
            attributemodifiable.removeModifier(SPEED_MODIFIER_SPRINTING);
        }

        if (flag) {
            attributemodifiable.addTransientModifier(SPEED_MODIFIER_SPRINTING);
        }

    }

    protected float getSoundVolume() {
        return 1.0F;
    }

    public float getVoicePitch() {
        return this.isBaby() ? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.5F : (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
    }

    protected boolean isImmobile() {
        return this.isDeadOrDying();
    }

    public void push(Entity entity) {
        if (!this.isSleeping()) {
            super.push(entity);
        }

    }

    private void dismountVehicle(Entity entity) {
        Vec3 vec3d;
        if (this.isRemoved()) {
            vec3d = this.position();
        } else if (!entity.isRemoved() && !this.level.getBlockState(entity.blockPosition()).is(BlockTags.PORTALS)) {
            vec3d = entity.getDismountLocationForPassenger(this);
        } else {
            double d0 = Math.max(this.getY(), entity.getY());
            vec3d = new Vec3(this.getX(), d0, this.getZ());
        }

        this.dismountTo(vec3d.x, vec3d.y, vec3d.z);
    }

    public boolean shouldShowName() {
        return this.isCustomNameVisible();
    }

    protected float getJumpPower() {
        return 0.42F * this.getBlockJumpFactor();
    }

    public double getJumpBoostPower() {
        return this.hasEffect(MobEffects.JUMP) ? (double)(0.1F * (float)(this.getEffect(MobEffects.JUMP).getAmplifier() + 1)) : 0.0D;
    }

    protected void jumpFromGround() {
        double d0 = (double)this.getJumpPower() + this.getJumpBoostPower();
        Vec3 vec3d = this.getDeltaMovement();
        this.setDeltaMovement(vec3d.x, d0, vec3d.z);
        if (this.isSprinting()) {
            float f = this.getYRot() * 0.017453292F;
            this.setDeltaMovement(this.getDeltaMovement().add((double)(-Mth.sin(f) * 0.2F), 0.0D, (double)(Mth.cos(f) * 0.2F)));
        }

        this.hasImpulse = true;
    }

    protected void goDownInWater() {
        this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.03999999910593033D, 0.0D));
    }

    protected void jumpInLiquid(TagKey<Fluid> tagkey) {
        this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.03999999910593033D, 0.0D));
    }

    protected float getWaterSlowDown() {
        return 0.8F;
    }

    public boolean canStandOnFluid(FluidState fluid) {
        return false;
    }

    public void travel(Vec3 vec3d) {
        if (this.isEffectiveAi() || this.isControlledByLocalInstance()) {
            double d0 = 0.08D;
            boolean flag = this.getDeltaMovement().y <= 0.0D;
            if (flag && this.hasEffect(MobEffects.SLOW_FALLING)) {
                d0 = 0.01D;
                this.resetFallDistance();
            }

            FluidState fluid = this.level.getFluidState(this.blockPosition());
            double d1;
            float f;
            float f2;
            Vec3 vec3d1;
            if (this.isInWater() && this.isAffectedByFluids() && !this.canStandOnFluid(fluid)) {
                d1 = this.getY();
                f = this.isSprinting() ? 0.9F : this.getWaterSlowDown();
                float f1 = 0.02F;
                f2 = (float)EnchantmentHelper.getDepthStrider(this);
                if (f2 > 3.0F) {
                    f2 = 3.0F;
                }

                if (!this.onGround) {
                    f2 *= 0.5F;
                }

                if (f2 > 0.0F) {
                    f += (0.54600006F - f) * f2 / 3.0F;
                    f1 += (this.getSpeed() - f1) * f2 / 3.0F;
                }

                if (this.hasEffect(MobEffects.DOLPHINS_GRACE)) {
                    f = 0.96F;
                }

                this.moveRelative(f1, vec3d);
                this.move(MoverType.SELF, this.getDeltaMovement());
                vec3d1 = this.getDeltaMovement();
                if (this.horizontalCollision && this.onClimbable()) {
                    vec3d1 = new Vec3(vec3d1.x, 0.2D, vec3d1.z);
                }

                this.setDeltaMovement(vec3d1.multiply((double)f, 0.800000011920929D, (double)f));
                Vec3 vec3d2 = this.getFluidFallingAdjustedMovement(d0, flag, this.getDeltaMovement());
                this.setDeltaMovement(vec3d2);
                if (this.horizontalCollision && this.isFree(vec3d2.x, vec3d2.y + 0.6000000238418579D - this.getY() + d1, vec3d2.z)) {
                    this.setDeltaMovement(vec3d2.x, 0.30000001192092896D, vec3d2.z);
                }
            } else {
                Vec3 vec3d4;
                if (this.isInLava() && this.isAffectedByFluids() && !this.canStandOnFluid(fluid)) {
                    d1 = this.getY();
                    this.moveRelative(0.02F, vec3d);
                    this.move(MoverType.SELF, this.getDeltaMovement());
                    if (this.getFluidHeight(FluidTags.LAVA) <= this.getFluidJumpThreshold()) {
                        this.setDeltaMovement(this.getDeltaMovement().multiply(0.5D, 0.800000011920929D, 0.5D));
                        vec3d4 = this.getFluidFallingAdjustedMovement(d0, flag, this.getDeltaMovement());
                        this.setDeltaMovement(vec3d4);
                    } else {
                        this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
                    }

                    if (!this.isNoGravity()) {
                        this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -d0 / 4.0D, 0.0D));
                    }

                    vec3d4 = this.getDeltaMovement();
                    if (this.horizontalCollision && this.isFree(vec3d4.x, vec3d4.y + 0.6000000238418579D - this.getY() + d1, vec3d4.z)) {
                        this.setDeltaMovement(vec3d4.x, 0.30000001192092896D, vec3d4.z);
                    }
                } else if (this.isFallFlying()) {
                    vec3d4 = this.getDeltaMovement();
                    if (vec3d4.y > -0.5D) {
                        this.fallDistance = 1.0F;
                    }

                    Vec3 vec3d5 = this.getLookAngle();
                    f = this.getXRot() * 0.017453292F;
                    double d2 = Math.sqrt(vec3d5.x * vec3d5.x + vec3d5.z * vec3d5.z);
                    double d3 = vec3d4.horizontalDistance();
                    double d4 = vec3d5.length();
                    double d5 = Math.cos((double)f);
                    d5 = d5 * d5 * Math.min(1.0D, d4 / 0.4D);
                    vec3d4 = this.getDeltaMovement().add(0.0D, d0 * (-1.0D + d5 * 0.75D), 0.0D);
                    double d6;
                    if (vec3d4.y < 0.0D && d2 > 0.0D) {
                        d6 = vec3d4.y * -0.1D * d5;
                        vec3d4 = vec3d4.add(vec3d5.x * d6 / d2, d6, vec3d5.z * d6 / d2);
                    }

                    if (f < 0.0F && d2 > 0.0D) {
                        d6 = d3 * (double)(-Mth.sin(f)) * 0.04D;
                        vec3d4 = vec3d4.add(-vec3d5.x * d6 / d2, d6 * 3.2D, -vec3d5.z * d6 / d2);
                    }

                    if (d2 > 0.0D) {
                        vec3d4 = vec3d4.add((vec3d5.x / d2 * d3 - vec3d4.x) * 0.1D, 0.0D, (vec3d5.z / d2 * d3 - vec3d4.z) * 0.1D);
                    }

                    this.setDeltaMovement(vec3d4.multiply(0.9900000095367432D, 0.9800000190734863D, 0.9900000095367432D));
                    this.move(MoverType.SELF, this.getDeltaMovement());
                    if (this.horizontalCollision && !this.level.isClientSide) {
                        d6 = this.getDeltaMovement().horizontalDistance();
                        double d7 = d3 - d6;
                        float f3 = (float)(d7 * 10.0D - 3.0D);
                        if (f3 > 0.0F) {
                            this.playSound(this.getFallDamageSound((int)f3), 1.0F, 1.0F);
                            this.hurt(DamageSource.FLY_INTO_WALL, f3);
                        }
                    }

                    if (this.onGround && !this.level.isClientSide && this.getSharedFlag(7) && !CraftEventFactory.callToggleGlideEvent(this, false).isCancelled()) {
                        this.setSharedFlag(7, false);
                    }
                } else {
                    BlockPos blockposition = this.getBlockPosBelowThatAffectsMyMovement();
                    f2 = this.level.getBlockState(blockposition).getBlock().getFriction();
                    f = this.onGround ? f2 * 0.91F : 0.91F;
                    vec3d1 = this.handleRelativeFrictionAndCalculateMovement(vec3d, f2);
                    double d8 = vec3d1.y;
                    if (this.hasEffect(MobEffects.LEVITATION)) {
                        d8 += (0.05D * (double)(this.getEffect(MobEffects.LEVITATION).getAmplifier() + 1) - vec3d1.y) * 0.2D;
                        this.resetFallDistance();
                    } else if (this.level.isClientSide && !this.level.hasChunkAt(blockposition)) {
                        if (this.getY() > (double)this.level.getMinBuildHeight()) {
                            d8 = -0.1D;
                        } else {
                            d8 = 0.0D;
                        }
                    } else if (!this.isNoGravity()) {
                        d8 -= d0;
                    }

                    if (this.shouldDiscardFriction()) {
                        this.setDeltaMovement(vec3d1.x, d8, vec3d1.z);
                    } else {
                        this.setDeltaMovement(vec3d1.x * (double)f, d8 * 0.9800000190734863D, vec3d1.z * (double)f);
                    }
                }
            }
        }

        this.calculateEntityAnimation(this, this instanceof FlyingAnimal);
    }

    public void calculateEntityAnimation(LivingEntity entityliving, boolean flag) {
        entityliving.animationSpeedOld = entityliving.animationSpeed;
        double d0 = entityliving.getX() - entityliving.xo;
        double d1 = flag ? entityliving.getY() - entityliving.yo : 0.0D;
        double d2 = entityliving.getZ() - entityliving.zo;
        float f = (float)Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2) * 4.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        entityliving.animationSpeed += (f - entityliving.animationSpeed) * 0.4F;
        entityliving.animationPosition += entityliving.animationSpeed;
    }

    public Vec3 handleRelativeFrictionAndCalculateMovement(Vec3 vec3d, float f) {
        this.moveRelative(this.getFrictionInfluencedSpeed(f), vec3d);
        this.setDeltaMovement(this.handleOnClimbable(this.getDeltaMovement()));
        this.move(MoverType.SELF, this.getDeltaMovement());
        Vec3 vec3d1 = this.getDeltaMovement();
        if ((this.horizontalCollision || this.jumping) && (this.onClimbable() || this.getFeetBlockState().is(Blocks.POWDER_SNOW) && PowderSnowBlock.canEntityWalkOnPowderSnow(this))) {
            vec3d1 = new Vec3(vec3d1.x, 0.2D, vec3d1.z);
        }

        return vec3d1;
    }

    public Vec3 getFluidFallingAdjustedMovement(double d0, boolean flag, Vec3 vec3d) {
        if (!this.isNoGravity() && !this.isSprinting()) {
            double d1;
            if (flag && Math.abs(vec3d.y - 0.005D) >= 0.003D && Math.abs(vec3d.y - d0 / 16.0D) < 0.003D) {
                d1 = -0.003D;
            } else {
                d1 = vec3d.y - d0 / 16.0D;
            }

            return new Vec3(vec3d.x, d1, vec3d.z);
        } else {
            return vec3d;
        }
    }

    private Vec3 handleOnClimbable(Vec3 vec3d) {
        if (this.onClimbable()) {
            this.resetFallDistance();
            float f = 0.15F;
            double d0 = Mth.clamp(vec3d.x, -0.15000000596046448D, 0.15000000596046448D);
            double d1 = Mth.clamp(vec3d.z, -0.15000000596046448D, 0.15000000596046448D);
            double d2 = Math.max(vec3d.y, -0.15000000596046448D);
            if (d2 < 0.0D && !this.getFeetBlockState().is(Blocks.SCAFFOLDING) && this.isSuppressingSlidingDownLadder() && this instanceof Player) {
                d2 = 0.0D;
            }

            vec3d = new Vec3(d0, d2, d1);
        }

        return vec3d;
    }

    private float getFrictionInfluencedSpeed(float f) {
        return this.onGround ? this.getSpeed() * (0.21600002F / (f * f * f)) : this.flyingSpeed;
    }

    public float getSpeed() {
        return this.speed;
    }

    public void setSpeed(float f) {
        this.speed = f;
    }

    public boolean doHurtTarget(Entity entity) {
        this.setLastHurtMob(entity);
        return false;
    }

    public void tick() {
        SpigotTimings.timerEntityBaseTick.startTiming();
        super.tick();
        this.updatingUsingItem();
        this.updateSwimAmount();
        if (!this.level.isClientSide) {
            int i = this.getArrowCount();
            if (i > 0) {
                if (this.removeArrowTime <= 0) {
                    this.removeArrowTime = 20 * (30 - i);
                }

                --this.removeArrowTime;
                if (this.removeArrowTime <= 0) {
                    this.setArrowCount(i - 1);
                }
            }

            int j = this.getStingerCount();
            if (j > 0) {
                if (this.removeStingerTime <= 0) {
                    this.removeStingerTime = 20 * (30 - j);
                }

                --this.removeStingerTime;
                if (this.removeStingerTime <= 0) {
                    this.setStingerCount(j - 1);
                }
            }

            this.detectEquipmentUpdates();
            if (this.tickCount % 20 == 0) {
                this.getCombatTracker().recheckStatus();
            }

            if (this.isSleeping() && !this.checkBedExists()) {
                this.stopSleeping();
            }
        }

        SpigotTimings.timerEntityBaseTick.stopTiming();
        this.aiStep();
        SpigotTimings.timerEntityTickRest.startTiming();
        double d0 = this.getX() - this.xo;
        double d1 = this.getZ() - this.zo;
        float f = (float)(d0 * d0 + d1 * d1);
        float f1 = this.yBodyRot;
        float f2 = 0.0F;
        this.oRun = this.run;
        float f3 = 0.0F;
        if (f > 0.0025000002F) {
            f3 = 1.0F;
            f2 = (float)Math.sqrt((double)f) * 3.0F;
            float f4 = (float)Mth.atan2(d1, d0) * 57.295776F - 90.0F;
            float f5 = Mth.abs(Mth.wrapDegrees(this.getYRot()) - f4);
            if (95.0F < f5 && f5 < 265.0F) {
                f1 = f4 - 180.0F;
            } else {
                f1 = f4;
            }
        }

        if (this.attackAnim > 0.0F) {
            f1 = this.getYRot();
        }

        if (!this.onGround) {
            f3 = 0.0F;
        }

        this.run += (f3 - this.run) * 0.3F;
        this.level.getProfiler().push("headTurn");
        f2 = this.tickHeadTurn(f1, f2);
        this.level.getProfiler().pop();
        this.level.getProfiler().push("rangeChecks");

        while(this.getYRot() - this.yRotO < -180.0F) {
            this.yRotO -= 360.0F;
        }

        while(this.getYRot() - this.yRotO >= 180.0F) {
            this.yRotO += 360.0F;
        }

        while(this.yBodyRot - this.yBodyRotO < -180.0F) {
            this.yBodyRotO -= 360.0F;
        }

        while(this.yBodyRot - this.yBodyRotO >= 180.0F) {
            this.yBodyRotO += 360.0F;
        }

        while(this.getXRot() - this.xRotO < -180.0F) {
            this.xRotO -= 360.0F;
        }

        while(this.getXRot() - this.xRotO >= 180.0F) {
            this.xRotO += 360.0F;
        }

        while(this.yHeadRot - this.yHeadRotO < -180.0F) {
            this.yHeadRotO -= 360.0F;
        }

        while(this.yHeadRot - this.yHeadRotO >= 180.0F) {
            this.yHeadRotO += 360.0F;
        }

        this.level.getProfiler().pop();
        this.animStep += f2;
        if (this.isFallFlying()) {
            ++this.fallFlyTicks;
        } else {
            this.fallFlyTicks = 0;
        }

        if (this.isSleeping()) {
            this.setXRot(0.0F);
        }

        SpigotTimings.timerEntityTickRest.stopTiming();
    }

    public void detectEquipmentUpdates() {
        Map<EquipmentSlot, ItemStack> map = this.collectEquipmentChanges();
        if (map != null) {
            this.handleHandSwap(map);
            if (!map.isEmpty()) {
                this.handleEquipmentChanges(map);
            }
        }

    }

    @Nullable
    private Map<EquipmentSlot, ItemStack> collectEquipmentChanges() {
        Map<EquipmentSlot, ItemStack> map = null;
        EquipmentSlot[] aenumitemslot = EquipmentSlot.values();
        int i = aenumitemslot.length;

        for(int j = 0; j < i; ++j) {
            EquipmentSlot enumitemslot = aenumitemslot[j];
            ItemStack itemstack;
            switch($SWITCH_TABLE$net$minecraft$world$entity$EnumItemSlot$Function()[enumitemslot.getType().ordinal()]) {
            case 1:
                itemstack = this.getLastHandItem(enumitemslot);
                break;
            case 2:
                itemstack = this.getLastArmorItem(enumitemslot);
                break;
            default:
                continue;
            }

            ItemStack itemstack1 = this.getItemBySlot(enumitemslot);
            if (!ItemStack.matches(itemstack1, itemstack)) {
                if (map == null) {
                    map = Maps.newEnumMap(EquipmentSlot.class);
                }

                map.put(enumitemslot, itemstack1);
                if (!itemstack.isEmpty()) {
                    this.getAttributes().removeAttributeModifiers(itemstack.getAttributeModifiers(enumitemslot));
                }

                if (!itemstack1.isEmpty()) {
                    this.getAttributes().addTransientAttributeModifiers(itemstack1.getAttributeModifiers(enumitemslot));
                }
            }
        }

        return map;
    }

    private void handleHandSwap(Map<EquipmentSlot, ItemStack> map) {
        ItemStack itemstack = (ItemStack)map.get(EquipmentSlot.MAINHAND);
        ItemStack itemstack1 = (ItemStack)map.get(EquipmentSlot.OFFHAND);
        if (itemstack != null && itemstack1 != null && ItemStack.matches(itemstack, this.getLastHandItem(EquipmentSlot.OFFHAND)) && ItemStack.matches(itemstack1, this.getLastHandItem(EquipmentSlot.MAINHAND))) {
            ((ServerLevel)this.level).getChunkSource().broadcast(this, new ClientboundEntityEventPacket(this, (byte)55));
            map.remove(EquipmentSlot.MAINHAND);
            map.remove(EquipmentSlot.OFFHAND);
            this.setLastHandItem(EquipmentSlot.MAINHAND, itemstack.copy());
            this.setLastHandItem(EquipmentSlot.OFFHAND, itemstack1.copy());
        }

    }

    private void handleEquipmentChanges(Map<EquipmentSlot, ItemStack> map) {
        List<Pair<EquipmentSlot, ItemStack>> list = Lists.newArrayListWithCapacity(map.size());
        map.forEach((enumitemslot, itemstack) -> {
            ItemStack itemstack1 = itemstack.copy();
            list.add(Pair.of(enumitemslot, itemstack1));
            switch($SWITCH_TABLE$net$minecraft$world$entity$EnumItemSlot$Function()[enumitemslot.getType().ordinal()]) {
            case 1:
                this.setLastHandItem(enumitemslot, itemstack1);
                break;
            case 2:
                this.setLastArmorItem(enumitemslot, itemstack1);
            }

        });
        ((ServerLevel)this.level).getChunkSource().broadcast(this, new ClientboundSetEquipmentPacket(this.getId(), list));
    }

    private ItemStack getLastArmorItem(EquipmentSlot enumitemslot) {
        return (ItemStack)this.lastArmorItemStacks.get(enumitemslot.getIndex());
    }

    private void setLastArmorItem(EquipmentSlot enumitemslot, ItemStack itemstack) {
        this.lastArmorItemStacks.set(enumitemslot.getIndex(), itemstack);
    }

    private ItemStack getLastHandItem(EquipmentSlot enumitemslot) {
        return (ItemStack)this.lastHandItemStacks.get(enumitemslot.getIndex());
    }

    private void setLastHandItem(EquipmentSlot enumitemslot, ItemStack itemstack) {
        this.lastHandItemStacks.set(enumitemslot.getIndex(), itemstack);
    }

    protected float tickHeadTurn(float f, float f1) {
        float f2 = Mth.wrapDegrees(f - this.yBodyRot);
        this.yBodyRot += f2 * 0.3F;
        float f3 = Mth.wrapDegrees(this.getYRot() - this.yBodyRot);
        boolean flag = f3 < -90.0F || f3 >= 90.0F;
        if (f3 < -75.0F) {
            f3 = -75.0F;
        }

        if (f3 >= 75.0F) {
            f3 = 75.0F;
        }

        this.yBodyRot = this.getYRot() - f3;
        if (f3 * f3 > 2500.0F) {
            this.yBodyRot += f3 * 0.2F;
        }

        if (flag) {
            f1 *= -1.0F;
        }

        return f1;
    }

    public void aiStep() {
        if (this.noJumpDelay > 0) {
            --this.noJumpDelay;
        }

        if (this.isControlledByLocalInstance()) {
            this.lerpSteps = 0;
            this.setPacketCoordinates(this.getX(), this.getY(), this.getZ());
        }

        if (this.lerpSteps > 0) {
            double d0 = this.getX() + (this.lerpX - this.getX()) / (double)this.lerpSteps;
            double d1 = this.getY() + (this.lerpY - this.getY()) / (double)this.lerpSteps;
            double d2 = this.getZ() + (this.lerpZ - this.getZ()) / (double)this.lerpSteps;
            double d3 = Mth.wrapDegrees(this.lerpYRot - (double)this.getYRot());
            this.setYRot(this.getYRot() + (float)d3 / (float)this.lerpSteps);
            this.setXRot(this.getXRot() + (float)(this.lerpXRot - (double)this.getXRot()) / (float)this.lerpSteps);
            --this.lerpSteps;
            this.setPos(d0, d1, d2);
            this.setRot(this.getYRot(), this.getXRot());
        } else if (!this.isEffectiveAi()) {
            this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
        }

        if (this.lerpHeadSteps > 0) {
            this.yHeadRot += (float)Mth.wrapDegrees(this.lyHeadRot - (double)this.yHeadRot) / (float)this.lerpHeadSteps;
            --this.lerpHeadSteps;
        }

        Vec3 vec3d = this.getDeltaMovement();
        double d4 = vec3d.x;
        double d5 = vec3d.y;
        double d6 = vec3d.z;
        if (Math.abs(vec3d.x) < 0.003D) {
            d4 = 0.0D;
        }

        if (Math.abs(vec3d.y) < 0.003D) {
            d5 = 0.0D;
        }

        if (Math.abs(vec3d.z) < 0.003D) {
            d6 = 0.0D;
        }

        this.setDeltaMovement(d4, d5, d6);
        this.level.getProfiler().push("ai");
        SpigotTimings.timerEntityAI.startTiming();
        if (this.isImmobile()) {
            this.jumping = false;
            this.xxa = 0.0F;
            this.zza = 0.0F;
        } else if (this.isEffectiveAi()) {
            this.level.getProfiler().push("newAi");
            this.serverAiStep();
            this.level.getProfiler().pop();
        }

        SpigotTimings.timerEntityAI.stopTiming();
        this.level.getProfiler().pop();
        this.level.getProfiler().push("jump");
        if (this.jumping && this.isAffectedByFluids()) {
            double d7;
            if (this.isInLava()) {
                d7 = this.getFluidHeight(FluidTags.LAVA);
            } else {
                d7 = this.getFluidHeight(FluidTags.WATER);
            }

            boolean flag = this.isInWater() && d7 > 0.0D;
            double d8 = this.getFluidJumpThreshold();
            if (!flag || this.onGround && !(d7 > d8)) {
                if (!this.isInLava() || this.onGround && !(d7 > d8)) {
                    if ((this.onGround || flag && d7 <= d8) && this.noJumpDelay == 0) {
                        this.jumpFromGround();
                        this.noJumpDelay = 10;
                    }
                } else {
                    this.jumpInLiquid(FluidTags.LAVA);
                }
            } else {
                this.jumpInLiquid(FluidTags.WATER);
            }
        } else {
            this.noJumpDelay = 0;
        }

        this.level.getProfiler().pop();
        this.level.getProfiler().push("travel");
        this.xxa *= 0.98F;
        this.zza *= 0.98F;
        this.updateFallFlying();
        AABB axisalignedbb = this.getBoundingBox();
        SpigotTimings.timerEntityAIMove.startTiming();
        this.travel(new Vec3((double)this.xxa, (double)this.yya, (double)this.zza));
        SpigotTimings.timerEntityAIMove.stopTiming();
        this.level.getProfiler().pop();
        this.level.getProfiler().push("freezing");
        boolean flag1 = this.getType().is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES);
        int i;
        if (!this.level.isClientSide && !this.isDeadOrDying()) {
            i = this.getTicksFrozen();
            if (this.isInPowderSnow && this.canFreeze()) {
                this.setTicksFrozen(Math.min(this.getTicksRequiredToFreeze(), i + 1));
            } else {
                this.setTicksFrozen(Math.max(0, i - 2));
            }
        }

        this.removeFrost();
        this.tryAddFrost();
        if (!this.level.isClientSide && this.tickCount % 40 == 0 && this.isFullyFrozen() && this.canFreeze()) {
            i = flag1 ? 5 : 1;
            this.hurt(DamageSource.FREEZE, (float)i);
        }

        this.level.getProfiler().pop();
        this.level.getProfiler().push("push");
        if (this.autoSpinAttackTicks > 0) {
            --this.autoSpinAttackTicks;
            this.checkAutoSpinAttack(axisalignedbb, this.getBoundingBox());
        }

        SpigotTimings.timerEntityAICollision.startTiming();
        this.pushEntities();
        SpigotTimings.timerEntityAICollision.stopTiming();
        this.level.getProfiler().pop();
        if (!this.level.isClientSide && this.isSensitiveToWater() && this.isInWaterRainOrBubble()) {
            this.hurt(DamageSource.DROWN, 1.0F);
        }

    }

    public boolean isSensitiveToWater() {
        return false;
    }

    private void updateFallFlying() {
        boolean flag = this.getSharedFlag(7);
        if (flag && !this.onGround && !this.isPassenger() && !this.hasEffect(MobEffects.LEVITATION)) {
            ItemStack itemstack = this.getItemBySlot(EquipmentSlot.CHEST);
            if (itemstack.is(Items.ELYTRA) && ElytraItem.isFlyEnabled(itemstack)) {
                flag = true;
                int i = this.fallFlyTicks + 1;
                if (!this.level.isClientSide && i % 10 == 0) {
                    int j = i / 10;
                    if (j % 2 == 0) {
                        itemstack.hurtAndBreak(1, this, (entityliving) -> {
                            entityliving.broadcastBreakEvent(EquipmentSlot.CHEST);
                        });
                    }

                    this.gameEvent(GameEvent.ELYTRA_FREE_FALL);
                }
            } else {
                flag = false;
            }
        } else {
            flag = false;
        }

        if (!this.level.isClientSide && flag != this.getSharedFlag(7) && !CraftEventFactory.callToggleGlideEvent(this, flag).isCancelled()) {
            this.setSharedFlag(7, flag);
        }

    }

    protected void serverAiStep() {
    }

    protected void pushEntities() {
        List<Entity> list = this.level.getEntities(this, this.getBoundingBox(), EntitySelector.pushableBy(this));
        if (!list.isEmpty()) {
            int i = this.level.getGameRules().getInt(GameRules.RULE_MAX_ENTITY_CRAMMING);
            int j;
            if (i > 0 && list.size() > i - 1 && this.random.nextInt(4) == 0) {
                j = 0;

                for(int k = 0; k < list.size(); ++k) {
                    if (!((Entity)list.get(k)).isPassenger()) {
                        ++j;
                    }
                }

                if (j > i - 1) {
                    this.hurt(DamageSource.CRAMMING, 6.0F);
                }
            }

            for(j = 0; j < list.size(); ++j) {
                Entity entity = (Entity)list.get(j);
                this.doPush(entity);
            }
        }

    }

    protected void checkAutoSpinAttack(AABB axisalignedbb, AABB axisalignedbb1) {
        AABB axisalignedbb2 = axisalignedbb.minmax(axisalignedbb1);
        List<Entity> list = this.level.getEntities(this, axisalignedbb2);
        if (!list.isEmpty()) {
            for(int i = 0; i < list.size(); ++i) {
                Entity entity = (Entity)list.get(i);
                if (entity instanceof LivingEntity) {
                    this.doAutoAttackOnTouch((LivingEntity)entity);
                    this.autoSpinAttackTicks = 0;
                    this.setDeltaMovement(this.getDeltaMovement().scale(-0.2D));
                    break;
                }
            }
        } else if (this.horizontalCollision) {
            this.autoSpinAttackTicks = 0;
        }

        if (!this.level.isClientSide && this.autoSpinAttackTicks <= 0) {
            this.setLivingEntityFlag(4, false);
        }

    }

    protected void doPush(Entity entity) {
        entity.push(this);
    }

    protected void doAutoAttackOnTouch(LivingEntity entityliving) {
    }

    public boolean isAutoSpinAttack() {
        return ((Byte)this.entityData.get(DATA_LIVING_ENTITY_FLAGS) & 4) != 0;
    }

    public void stopRiding() {
        Entity entity = this.getVehicle();
        super.stopRiding();
        if (entity != null && entity != this.getVehicle() && !this.level.isClientSide) {
            this.dismountVehicle(entity);
        }

    }

    public void rideTick() {
        super.rideTick();
        this.oRun = this.run;
        this.run = 0.0F;
        this.resetFallDistance();
    }

    public void lerpTo(double d0, double d1, double d2, float f, float f1, int i, boolean flag) {
        this.lerpX = d0;
        this.lerpY = d1;
        this.lerpZ = d2;
        this.lerpYRot = (double)f;
        this.lerpXRot = (double)f1;
        this.lerpSteps = i;
    }

    public void lerpHeadTo(float f, int i) {
        this.lyHeadRot = (double)f;
        this.lerpHeadSteps = i;
    }

    public void setJumping(boolean flag) {
        this.jumping = flag;
    }

    public void onItemPickup(ItemEntity entityitem) {
        Player entityhuman = entityitem.getThrower() != null ? this.level.getPlayerByUUID(entityitem.getThrower()) : null;
        if (entityhuman instanceof ServerPlayer) {
            CriteriaTriggers.ITEM_PICKED_UP_BY_ENTITY.trigger((ServerPlayer)entityhuman, entityitem.getItem(), this);
        }

    }

    public void take(Entity entity, int i) {
        if (!entity.isRemoved() && !this.level.isClientSide && (entity instanceof ItemEntity || entity instanceof AbstractArrow || entity instanceof ExperienceOrb)) {
            ((ServerLevel)this.level).getChunkSource().broadcast(entity, new ClientboundTakeItemEntityPacket(entity.getId(), this.getId(), i));
        }

    }

    public boolean hasLineOfSight(Entity entity) {
        if (entity.level != this.level) {
            return false;
        } else {
            Vec3 vec3d = new Vec3(this.getX(), this.getEyeY(), this.getZ());
            Vec3 vec3d1 = new Vec3(entity.getX(), entity.getEyeY(), entity.getZ());
            return vec3d1.distanceTo(vec3d) > 128.0D ? false : this.level.clip(new ClipContext(vec3d, vec3d1, Block.COLLIDER, net.minecraft.world.level.ClipContext.Fluid.NONE, this)).getType() == Type.MISS;
        }
    }

    public float getViewYRot(float f) {
        return f == 1.0F ? this.yHeadRot : Mth.lerp(f, this.yHeadRotO, this.yHeadRot);
    }

    public float getAttackAnim(float f) {
        float f1 = this.attackAnim - this.oAttackAnim;
        if (f1 < 0.0F) {
            ++f1;
        }

        return this.oAttackAnim + f1 * f;
    }

    public boolean isEffectiveAi() {
        return !this.level.isClientSide;
    }

    public boolean isPickable() {
        return !this.isRemoved() && this.collides;
    }

    public boolean isPushable() {
        return this.isAlive() && !this.isSpectator() && !this.onClimbable() && this.collides;
    }

    public boolean canCollideWithBukkit(Entity entity) {
        return this.isPushable() && this.collides != this.collidableExemptions.contains(entity.getUUID());
    }

    public float getYHeadRot() {
        return this.yHeadRot;
    }

    public void setYHeadRot(float f) {
        this.yHeadRot = f;
    }

    public void setYBodyRot(float f) {
        this.yBodyRot = f;
    }

    protected Vec3 getRelativePortalPosition(Axis enumdirection_enumaxis, FoundRectangle blockutil_rectangle) {
        return resetForwardDirectionOfRelativePortalPosition(super.getRelativePortalPosition(enumdirection_enumaxis, blockutil_rectangle));
    }

    public static Vec3 resetForwardDirectionOfRelativePortalPosition(Vec3 vec3d) {
        return new Vec3(vec3d.x, vec3d.y, 0.0D);
    }

    public float getAbsorptionAmount() {
        return this.absorptionAmount;
    }

    public void setAbsorptionAmount(float f) {
        if (f < 0.0F) {
            f = 0.0F;
        }

        this.absorptionAmount = f;
    }

    public void onEnterCombat() {
    }

    public void onLeaveCombat() {
    }

    protected void updateEffectVisibility() {
        this.effectsDirty = true;
    }

    public abstract HumanoidArm getMainArm();

    public boolean isUsingItem() {
        return ((Byte)this.entityData.get(DATA_LIVING_ENTITY_FLAGS) & 1) > 0;
    }

    public InteractionHand getUsedItemHand() {
        return ((Byte)this.entityData.get(DATA_LIVING_ENTITY_FLAGS) & 2) > 0 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
    }

    private void updatingUsingItem() {
        if (this.isUsingItem()) {
            if (ItemStack.isSameIgnoreDurability(this.getItemInHand(this.getUsedItemHand()), this.useItem)) {
                this.useItem = this.getItemInHand(this.getUsedItemHand());
                this.updateUsingItem(this.useItem);
            } else {
                this.stopUsingItem();
            }
        }

    }

    protected void updateUsingItem(ItemStack itemstack) {
        itemstack.onUseTick(this.level, this, this.getUseItemRemainingTicks());
        if (this.shouldTriggerItemUseEffects()) {
            this.triggerItemUseEffects(itemstack, 5);
        }

        if (--this.useItemRemaining == 0 && !this.level.isClientSide && !itemstack.useOnRelease()) {
            this.completeUsingItem();
        }

    }

    private boolean shouldTriggerItemUseEffects() {
        int i = this.getUseItemRemainingTicks();
        FoodProperties foodinfo = this.useItem.getItem().getFoodProperties();
        boolean flag = foodinfo != null && foodinfo.isFastFood();
        flag |= i <= this.useItem.getUseDuration() - 7;
        return flag && i % 4 == 0;
    }

    private void updateSwimAmount() {
        this.swimAmountO = this.swimAmount;
        if (this.isVisuallySwimming()) {
            this.swimAmount = Math.min(1.0F, this.swimAmount + 0.09F);
        } else {
            this.swimAmount = Math.max(0.0F, this.swimAmount - 0.09F);
        }

    }

    protected void setLivingEntityFlag(int i, boolean flag) {
        byte b0 = (Byte)this.entityData.get(DATA_LIVING_ENTITY_FLAGS);
        int j;
        if (flag) {
            j = b0 | i;
        } else {
            j = b0 & ~i;
        }

        this.entityData.set(DATA_LIVING_ENTITY_FLAGS, (byte)j);
    }

    public void startUsingItem(InteractionHand enumhand) {
        ItemStack itemstack = this.getItemInHand(enumhand);
        if (!itemstack.isEmpty() && !this.isUsingItem()) {
            this.useItem = itemstack;
            this.useItemRemaining = itemstack.getUseDuration();
            if (!this.level.isClientSide) {
                this.setLivingEntityFlag(1, true);
                this.setLivingEntityFlag(2, enumhand == InteractionHand.OFF_HAND);
            }
        }

    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> datawatcherobject) {
        super.onSyncedDataUpdated(datawatcherobject);
        if (SLEEPING_POS_ID.equals(datawatcherobject)) {
            if (this.level.isClientSide) {
                this.getSleepingPos().ifPresent(this::setPosToBed);
            }
        } else if (DATA_LIVING_ENTITY_FLAGS.equals(datawatcherobject) && this.level.isClientSide) {
            if (this.isUsingItem() && this.useItem.isEmpty()) {
                this.useItem = this.getItemInHand(this.getUsedItemHand());
                if (!this.useItem.isEmpty()) {
                    this.useItemRemaining = this.useItem.getUseDuration();
                }
            } else if (!this.isUsingItem() && !this.useItem.isEmpty()) {
                this.useItem = ItemStack.EMPTY;
                this.useItemRemaining = 0;
            }
        }

    }

    public void lookAt(Anchor argumentanchor_anchor, Vec3 vec3d) {
        super.lookAt(argumentanchor_anchor, vec3d);
        this.yHeadRotO = this.yHeadRot;
        this.yBodyRot = this.yHeadRot;
        this.yBodyRotO = this.yBodyRot;
    }

    protected void triggerItemUseEffects(ItemStack itemstack, int i) {
        if (!itemstack.isEmpty() && this.isUsingItem()) {
            if (itemstack.getUseAnimation() == UseAnim.DRINK) {
                this.playSound(this.getDrinkingSound(itemstack), 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
            }

            if (itemstack.getUseAnimation() == UseAnim.EAT) {
                this.spawnItemParticles(itemstack, i);
                this.playSound(this.getEatingSound(itemstack), 0.5F + 0.5F * (float)this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            }
        }

    }

    private void spawnItemParticles(ItemStack itemstack, int i) {
        for(int j = 0; j < i; ++j) {
            Vec3 vec3d = new Vec3(((double)this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
            vec3d = vec3d.xRot(-this.getXRot() * 0.017453292F);
            vec3d = vec3d.yRot(-this.getYRot() * 0.017453292F);
            double d0 = (double)(-this.random.nextFloat()) * 0.6D - 0.3D;
            Vec3 vec3d1 = new Vec3(((double)this.random.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
            vec3d1 = vec3d1.xRot(-this.getXRot() * 0.017453292F);
            vec3d1 = vec3d1.yRot(-this.getYRot() * 0.017453292F);
            vec3d1 = vec3d1.add(this.getX(), this.getEyeY(), this.getZ());
            this.level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, itemstack), vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y + 0.05D, vec3d.z);
        }

    }

    protected void completeUsingItem() {
        if (!this.level.isClientSide || this.isUsingItem()) {
            InteractionHand enumhand = this.getUsedItemHand();
            if (!this.useItem.equals(this.getItemInHand(enumhand))) {
                this.releaseUsingItem();
            } else if (!this.useItem.isEmpty() && this.isUsingItem()) {
                this.triggerItemUseEffects(this.useItem, 16);
                ItemStack itemstack;
                if (this instanceof ServerPlayer) {
                    org.bukkit.inventory.ItemStack craftItem = CraftItemStack.asBukkitCopy(this.useItem);
                    PlayerItemConsumeEvent event = new PlayerItemConsumeEvent((org.bukkit.entity.Player)this.getBukkitEntity(), craftItem);
                    this.level.getCraftServer().getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                        ((ServerPlayer)this).getBukkitEntity().updateInventory();
                        ((ServerPlayer)this).getBukkitEntity().updateScaledHealth();
                        return;
                    }

                    itemstack = craftItem.equals(event.getItem()) ? this.useItem.finishUsingItem(this.level, this) : CraftItemStack.asNMSCopy(event.getItem()).finishUsingItem(this.level, this);
                } else {
                    itemstack = this.useItem.finishUsingItem(this.level, this);
                }

                if (itemstack != this.useItem) {
                    this.setItemInHand(enumhand, itemstack);
                }

                this.stopUsingItem();
            }
        }

    }

    public ItemStack getUseItem() {
        return this.useItem;
    }

    public int getUseItemRemainingTicks() {
        return this.useItemRemaining;
    }

    public int getTicksUsingItem() {
        return this.isUsingItem() ? this.useItem.getUseDuration() - this.getUseItemRemainingTicks() : 0;
    }

    public void releaseUsingItem() {
        if (!this.useItem.isEmpty()) {
            this.useItem.releaseUsing(this.level, this, this.getUseItemRemainingTicks());
            if (this.useItem.useOnRelease()) {
                this.updatingUsingItem();
            }
        }

        this.stopUsingItem();
    }

    public void stopUsingItem() {
        if (!this.level.isClientSide) {
            this.setLivingEntityFlag(1, false);
        }

        this.useItem = ItemStack.EMPTY;
        this.useItemRemaining = 0;
    }

    public boolean isBlocking() {
        if (this.isUsingItem() && !this.useItem.isEmpty()) {
            net.minecraft.world.item.Item item = this.useItem.getItem();
            return item.getUseAnimation(this.useItem) != UseAnim.BLOCK ? false : item.getUseDuration(this.useItem) - this.useItemRemaining >= 5;
        } else {
            return false;
        }
    }

    public boolean isSuppressingSlidingDownLadder() {
        return this.isShiftKeyDown();
    }

    public boolean isFallFlying() {
        return this.getSharedFlag(7);
    }

    public boolean isVisuallySwimming() {
        return super.isVisuallySwimming() || !this.isFallFlying() && this.getPose() == Pose.FALL_FLYING;
    }

    public int getFallFlyingTicks() {
        return this.fallFlyTicks;
    }

    public boolean randomTeleport(double d0, double d1, double d2, boolean flag) {
        return (Boolean)this.randomTeleport(d0, d1, d2, flag, TeleportCause.UNKNOWN).orElse(false);
    }

    public Optional<Boolean> randomTeleport(double d0, double d1, double d2, boolean flag, TeleportCause cause) {
        double d3 = this.getX();
        double d4 = this.getY();
        double d5 = this.getZ();
        double d6 = d1;
        boolean flag1 = false;
        BlockPos blockposition = new BlockPos(d0, d1, d2);
        Level world = this.level;
        if (world.hasChunkAt(blockposition)) {
            boolean flag2 = false;

            while(!flag2 && blockposition.getY() > world.getMinBuildHeight()) {
                BlockPos blockposition1 = blockposition.below();
                BlockState iblockdata = world.getBlockState(blockposition1);
                if (iblockdata.getMaterial().blocksMotion()) {
                    flag2 = true;
                } else {
                    --d6;
                    blockposition = blockposition1;
                }
            }

            if (flag2) {
                this.setPos(d0, d6, d2);
                if (world.noCollision(this) && !world.containsAnyLiquid(this.getBoundingBox())) {
                    flag1 = true;
                }

                this.setPos(d3, d4, d5);
                if (flag1) {
                    if (!(this instanceof ServerPlayer)) {
                        EntityTeleportEvent teleport = new EntityTeleportEvent(this.getBukkitEntity(), new Location(this.level.getWorld(), d3, d4, d5), new Location(this.level.getWorld(), d0, d6, d2));
                        this.level.getCraftServer().getPluginManager().callEvent(teleport);
                        if (teleport.isCancelled()) {
                            return Optional.empty();
                        }

                        Location to = teleport.getTo();
                        this.teleportTo(to.getX(), to.getY(), to.getZ());
                    } else if (((ServerPlayer)this).connection.teleport(d0, d6, d2, this.getYRot(), this.getXRot(), Collections.emptySet(), false, cause)) {
                        return Optional.empty();
                    }
                }
            }
        }

        if (!flag1) {
            return Optional.of(false);
        } else {
            if (flag) {
                world.broadcastEntityEvent(this, (byte)46);
            }

            if (this instanceof PathfinderMob) {
                ((PathfinderMob)this).getNavigation().stop();
            }

            return Optional.of(true);
        }
    }

    public boolean isAffectedByPotions() {
        return true;
    }

    public boolean attackable() {
        return true;
    }

    public void setRecordPlayingNearby(BlockPos blockposition, boolean flag) {
    }

    public boolean canTakeItem(ItemStack itemstack) {
        return false;
    }

    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddMobPacket(this);
    }

    public EntityDimensions getDimensions(Pose entitypose) {
        return entitypose == Pose.SLEEPING ? SLEEPING_DIMENSIONS : super.getDimensions(entitypose).scale(this.getScale());
    }

    public ImmutableList<Pose> getDismountPoses() {
        return ImmutableList.of(Pose.STANDING);
    }

    public AABB getLocalBoundsForPose(Pose entitypose) {
        EntityDimensions entitysize = this.getDimensions(entitypose);
        return new AABB((double)(-entitysize.width / 2.0F), 0.0D, (double)(-entitysize.width / 2.0F), (double)(entitysize.width / 2.0F), (double)entitysize.height, (double)(entitysize.width / 2.0F));
    }

    public Optional<BlockPos> getSleepingPos() {
        return (Optional)this.entityData.get(SLEEPING_POS_ID);
    }

    public void setSleepingPos(BlockPos blockposition) {
        this.entityData.set(SLEEPING_POS_ID, Optional.of(blockposition));
    }

    public void clearSleepingPos() {
        this.entityData.set(SLEEPING_POS_ID, Optional.empty());
    }

    public boolean isSleeping() {
        return this.getSleepingPos().isPresent();
    }

    public void startSleeping(BlockPos blockposition) {
        if (this.isPassenger()) {
            this.stopRiding();
        }

        BlockState iblockdata = this.level.getBlockState(blockposition);
        if (iblockdata.getBlock() instanceof BedBlock) {
            this.level.setBlock(blockposition, (BlockState)iblockdata.setValue(BedBlock.OCCUPIED, true), 3);
        }

        this.setPose(Pose.SLEEPING);
        this.setPosToBed(blockposition);
        this.setSleepingPos(blockposition);
        this.setDeltaMovement(Vec3.ZERO);
        this.hasImpulse = true;
    }

    private void setPosToBed(BlockPos blockposition) {
        this.setPos((double)blockposition.getX() + 0.5D, (double)blockposition.getY() + 0.6875D, (double)blockposition.getZ() + 0.5D);
    }

    private boolean checkBedExists() {
        return (Boolean)this.getSleepingPos().map((blockposition) -> {
            return this.level.getBlockState(blockposition).getBlock() instanceof BedBlock;
        }).orElse(false);
    }

    public void stopSleeping() {
        Optional<BlockPos> optional = this.getSleepingPos();
        Level world = this.level;
        java.util.Objects.requireNonNull(this.level);
        world.getClass();
        optional.filter(world::hasChunkAt).ifPresent((blockposition) -> {
            BlockState iblockdata = this.level.getBlockState(blockposition);
            if (iblockdata.getBlock() instanceof BedBlock) {
                this.level.setBlock(blockposition, (BlockState)iblockdata.setValue(BedBlock.OCCUPIED, false), 3);
                Vec3 vec3d = (Vec3)BedBlock.findStandUpPosition(this.getType(), this.level, blockposition, this.getYRot()).orElseGet(() -> {
                    BlockPos blockposition1 = blockposition.above();
                    return new Vec3((double)blockposition1.getX() + 0.5D, (double)blockposition1.getY() + 0.1D, (double)blockposition1.getZ() + 0.5D);
                });
                Vec3 vec3d1 = Vec3.atBottomCenterOf(blockposition).subtract(vec3d).normalize();
                float f = (float)Mth.wrapDegrees(Mth.atan2(vec3d1.z, vec3d1.x) * 57.2957763671875D - 90.0D);
                this.setPos(vec3d.x, vec3d.y, vec3d.z);
                this.setYRot(f);
                this.setXRot(0.0F);
            }

        });
        Vec3 vec3d = this.position();
        this.setPose(Pose.STANDING);
        this.setPos(vec3d.x, vec3d.y, vec3d.z);
        this.clearSleepingPos();
    }

    @Nullable
    public Direction getBedOrientation() {
        BlockPos blockposition = (BlockPos)this.getSleepingPos().orElse((Object)null);
        return blockposition != null ? BedBlock.getBedOrientation(this.level, blockposition) : null;
    }

    public boolean isInWall() {
        return !this.isSleeping() && super.isInWall();
    }

    protected final float getEyeHeight(Pose entitypose, EntityDimensions entitysize) {
        return entitypose == Pose.SLEEPING ? 0.2F : this.getStandingEyeHeight(entitypose, entitysize);
    }

    protected float getStandingEyeHeight(Pose entitypose, EntityDimensions entitysize) {
        return super.getEyeHeight(entitypose, entitysize);
    }

    public ItemStack getProjectile(ItemStack itemstack) {
        return ItemStack.EMPTY;
    }

    public ItemStack eat(Level world, ItemStack itemstack) {
        if (itemstack.isEdible()) {
            world.gameEvent(this, GameEvent.EAT, this.eyeBlockPosition());
            world.playSound((Player)null, this.getX(), this.getY(), this.getZ(), this.getEatingSound(itemstack), SoundSource.NEUTRAL, 1.0F, 1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.4F);
            this.addEatEffect(itemstack, world, this);
            if (!(this instanceof Player) || !((Player)this).getAbilities().instabuild) {
                itemstack.shrink(1);
            }

            this.gameEvent(GameEvent.EAT);
        }

        return itemstack;
    }

    private void addEatEffect(ItemStack itemstack, Level world, LivingEntity entityliving) {
        net.minecraft.world.item.Item item = itemstack.getItem();
        if (item.isEdible()) {
            List<Pair<MobEffectInstance, Float>> list = item.getFoodProperties().getEffects();
            Iterator iterator = list.iterator();

            while(iterator.hasNext()) {
                Pair<MobEffectInstance, Float> pair = (Pair)iterator.next();
                if (!world.isClientSide && pair.getFirst() != null && world.random.nextFloat() < (Float)pair.getSecond()) {
                    entityliving.addEffect(new MobEffectInstance((MobEffectInstance)pair.getFirst()), Cause.FOOD);
                }
            }
        }

    }

    private static byte entityEventForEquipmentBreak(EquipmentSlot enumitemslot) {
        switch($SWITCH_TABLE$net$minecraft$world$entity$EnumItemSlot()[enumitemslot.ordinal()]) {
        case 1:
            return 47;
        case 2:
            return 48;
        case 3:
            return 52;
        case 4:
            return 51;
        case 5:
            return 50;
        case 6:
            return 49;
        default:
            return 47;
        }
    }

    public void broadcastBreakEvent(EquipmentSlot enumitemslot) {
        this.level.broadcastEntityEvent(this, entityEventForEquipmentBreak(enumitemslot));
    }

    public void broadcastBreakEvent(InteractionHand enumhand) {
        this.broadcastBreakEvent(enumhand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
    }

    public AABB getBoundingBoxForCulling() {
        if (this.getItemBySlot(EquipmentSlot.HEAD).is(Items.DRAGON_HEAD)) {
            float f = 0.5F;
            return this.getBoundingBox().inflate(0.5D, 0.5D, 0.5D);
        } else {
            return super.getBoundingBoxForCulling();
        }
    }

    public static EquipmentSlot getEquipmentSlotForItem(ItemStack itemstack) {
        net.minecraft.world.item.Item item = itemstack.getItem();
        return itemstack.is(Items.CARVED_PUMPKIN) || item instanceof BlockItem && ((BlockItem)item).getBlock() instanceof AbstractSkullBlock ? EquipmentSlot.HEAD : (item instanceof ArmorItem ? ((ArmorItem)item).getSlot() : (itemstack.is(Items.ELYTRA) ? EquipmentSlot.CHEST : (itemstack.is(Items.SHIELD) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND)));
    }

    private static SlotAccess createEquipmentSlotAccess(LivingEntity entityliving, EquipmentSlot enumitemslot) {
        return enumitemslot != EquipmentSlot.HEAD && enumitemslot != EquipmentSlot.MAINHAND && enumitemslot != EquipmentSlot.OFFHAND ? SlotAccess.forEquipmentSlot(entityliving, enumitemslot, (itemstack) -> {
            return itemstack.isEmpty() || Mob.getEquipmentSlotForItem(itemstack) == enumitemslot;
        }) : SlotAccess.forEquipmentSlot(entityliving, enumitemslot);
    }

    @Nullable
    private static EquipmentSlot getEquipmentSlot(int i) {
        return i == 100 + EquipmentSlot.HEAD.getIndex() ? EquipmentSlot.HEAD : (i == 100 + EquipmentSlot.CHEST.getIndex() ? EquipmentSlot.CHEST : (i == 100 + EquipmentSlot.LEGS.getIndex() ? EquipmentSlot.LEGS : (i == 100 + EquipmentSlot.FEET.getIndex() ? EquipmentSlot.FEET : (i == 98 ? EquipmentSlot.MAINHAND : (i == 99 ? EquipmentSlot.OFFHAND : null)))));
    }

    public SlotAccess getSlot(int i) {
        EquipmentSlot enumitemslot = getEquipmentSlot(i);
        return enumitemslot != null ? createEquipmentSlotAccess(this, enumitemslot) : super.getSlot(i);
    }

    public boolean canFreeze() {
        if (this.isSpectator()) {
            return false;
        } else {
            boolean flag = !this.getItemBySlot(EquipmentSlot.HEAD).is(ItemTags.FREEZE_IMMUNE_WEARABLES) && !this.getItemBySlot(EquipmentSlot.CHEST).is(ItemTags.FREEZE_IMMUNE_WEARABLES) && !this.getItemBySlot(EquipmentSlot.LEGS).is(ItemTags.FREEZE_IMMUNE_WEARABLES) && !this.getItemBySlot(EquipmentSlot.FEET).is(ItemTags.FREEZE_IMMUNE_WEARABLES);
            return flag && super.canFreeze();
        }
    }

    public boolean isCurrentlyGlowing() {
        return !this.level.isClientSide() && this.hasEffect(MobEffects.GLOWING) || super.isCurrentlyGlowing();
    }

    public void recreateFromPacket(ClientboundAddMobPacket packetplayoutspawnentityliving) {
        double d0 = packetplayoutspawnentityliving.getX();
        double d1 = packetplayoutspawnentityliving.getY();
        double d2 = packetplayoutspawnentityliving.getZ();
        float f = (float)(packetplayoutspawnentityliving.getyRot() * 360) / 256.0F;
        float f1 = (float)(packetplayoutspawnentityliving.getxRot() * 360) / 256.0F;
        this.setPacketCoordinates(d0, d1, d2);
        this.yBodyRot = (float)(packetplayoutspawnentityliving.getyHeadRot() * 360) / 256.0F;
        this.yHeadRot = (float)(packetplayoutspawnentityliving.getyHeadRot() * 360) / 256.0F;
        this.yBodyRotO = this.yBodyRot;
        this.yHeadRotO = this.yHeadRot;
        this.setId(packetplayoutspawnentityliving.getId());
        this.setUUID(packetplayoutspawnentityliving.getUUID());
        this.absMoveTo(d0, d1, d2, f, f1);
        this.setDeltaMovement((double)((float)packetplayoutspawnentityliving.getXd() / 8000.0F), (double)((float)packetplayoutspawnentityliving.getYd() / 8000.0F), (double)((float)packetplayoutspawnentityliving.getZd() / 8000.0F));
    }

    private static class ProcessableEffect {
        private MobEffect type;
        private MobEffectInstance effect;
        private final Cause cause;

        private ProcessableEffect(MobEffectInstance effect, Cause cause) {
            this.effect = effect;
            this.cause = cause;
        }

        private ProcessableEffect(MobEffect type, Cause cause) {
            this.type = type;
            this.cause = cause;
        }
    }

    public static record Fallsounds(SoundEvent small, SoundEvent big) {
        public Fallsounds(SoundEvent small, SoundEvent big) {
            this.small = small;
            this.big = big;
        }

        public SoundEvent small() {
            return this.small;
        }

        public SoundEvent big() {
            return this.big;
        }
    }
}
