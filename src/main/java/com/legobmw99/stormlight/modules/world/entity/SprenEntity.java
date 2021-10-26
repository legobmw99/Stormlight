package com.legobmw99.stormlight.modules.world.entity;

import com.legobmw99.stormlight.api.ISurgebindingData;
import com.legobmw99.stormlight.modules.powers.data.SurgebindingCapability;
import com.legobmw99.stormlight.modules.world.WorldSetup;
import com.legobmw99.stormlight.util.Order;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public class SprenEntity extends TamableAnimal implements FlyingAnimal {

    public static final EntityDataSerializer<Enum<Order>> ORDER = new EntityDataSerializer<>() {

        public void write(FriendlyByteBuf buf, Enum<Order> order) {
            buf.writeEnum(order);
        }

        public Enum<Order> read(FriendlyByteBuf buf) {
            return buf.readEnum(Order.class);
        }

        public Enum<Order> copy(Enum<Order> order) {
            return order;
        }

    };

    private static final EntityDataAccessor<Enum<Order>> SPREN_TYPE = SynchedEntityData.defineId(SprenEntity.class, ORDER);
    private static final float[][] colors = {{0.737F, 0.960F, 0.945F}, {0.356F, 0.333F, 0.407F}, {0.819F, 0.819F, 0.819F}, {0.349F, 0.670F, 0.466F}, {0.913F, 0.945F, 0.945F},
                                             {0.337F, 0.286F, 0.396F}, {0.188F, 0.145F, 0.129F}, {0.815F, 0.588F, 0.207F}, {0.513F, 0.027F, 0.098F}, {0.380F, 0.352F, 0.886F}};
    private static final SimpleParticleType[] particles = {ParticleTypes.CLOUD, ParticleTypes.PORTAL, ParticleTypes.ASH, ParticleTypes.HAPPY_VILLAGER, ParticleTypes.END_ROD,
                                                           ParticleTypes.ENCHANT, ParticleTypes.SQUID_INK, ParticleTypes.EFFECT, ParticleTypes.LAVA, ParticleTypes.SOUL};

    static {
        EntityDataSerializers.registerSerializer(ORDER);
    }

    private int delay = 0;

    public SprenEntity(Level world, Entity other) {
        this(null, world);
        if (other instanceof SprenEntity) {
            this.setSprenType(((SprenEntity) other).getSprenType());
        }
    }


    public SprenEntity(EntityType<SprenEntity> entityEntityType, Level world) {
        super(WorldSetup.SPREN_ENTITY, world);

        this.setTame(false);
        this.moveControl = new FlyingMoveControl(this, 20, true);
    }

    public static AttributeSupplier createAttributes() {

        return Monster.createMonsterAttributes().add(Attributes.FLYING_SPEED, 0.4D).add(Attributes.MOVEMENT_SPEED, 0.2D).add(Attributes.MAX_HEALTH, 18.0D).build();

    }

    public static <T extends Mob> boolean checkSprenSpawnRules(EntityType<T> tEntityType,
                                                               ServerLevelAccessor iServerWorld,
                                                               MobSpawnType spawnReason,
                                                               BlockPos blockPos,
                                                               Random random) {
        //todo biomes
        return checkMobSpawnRules(tEntityType, iServerWorld, spawnReason, blockPos, random);
    }

    public Order getSprenType() {
        return (Order) this.entityData.get(SPREN_TYPE);
    }

    public void setSprenType(Order order) {
        this.entityData.set(SPREN_TYPE, order);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag nbt) {
        //todo biomes
        this.setSprenType(Order.getOrNull(this.random.nextInt(10)));
        return super.finalizeSpawn(world, difficulty, reason, data, nbt);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
        return null;
    }

    @Override
    public void die(DamageSource p_70645_1_) {
        // TODO break oaths
        super.die(p_70645_1_);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        return super.mobInteract(player, hand);
    }

    @Override
    public void setTame(boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(180.0D);
        }
    }

    @Override
    public void aiStep() {
        if (this.level.isClientSide && delay == 0) {
            AABB aabb = getBoundingBox();
            double x = aabb.minX + Math.random() * (aabb.maxX - aabb.minX);
            double y = aabb.minY + Math.random() * (aabb.maxY - aabb.minY);
            double z = aabb.minZ + Math.random() * (aabb.maxZ - aabb.minZ);
            this.level.addParticle(particles[getSprenType().getIndex()], x, y, z, 0, 0, 0);
        }
        delay++;
        delay = delay % 10;
        super.aiStep();

    }

    @Override
    public void tick() {
        super.tick();
        if (this.random.nextInt(100) == 0) {
            this.heal(2.0F);
        }
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
    }

    @Override
    protected int calculateFallDamage(float p_225508_1_, float p_225508_2_) {
        return 0;
    }


    @Override
    public boolean isInvulnerableTo(DamageSource in) {
        return (!isTame() || in.equals(DamageSource.playerAttack((Player) getOwner()))) && !in.equals(DamageSource.OUT_OF_WORLD);
    }

    @Override
    protected PathNavigation createNavigation(Level world) {
        var flying = new FlyingPathNavigation(this, world);
        flying.setCanOpenDoors(true);
        flying.setCanFloat(true);
        flying.setCanPassDoors(true);
        flying.canFloat();
        return flying;
    }

    @Override
    public float getEyeHeight(Pose p) {
        return 0.8f;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.0D, 5.0F, 1.0F, true));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new FollowMobGoal(this, 1.0D, 3.0F, 7.0F));
        this.goalSelector.addGoal(4, new SitWhenOrderedToGoal(this));

    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SPREN_TYPE, Order.WINDRUNNERS);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putByte("type", (byte) getSprenType().getIndex());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        setSprenType(Order.getOrNull(compoundNBT.getByte("type")));
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }

    @Override
    protected void checkFallDamage(double p_184231_1_, boolean p_184231_3_, BlockState p_184231_4_, BlockPos p_184231_5_) {

    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity e) {
        if (e.equals(this.getOwner())) {
            super.doPush(e);
        }
    }

    @Override
    public boolean isAlliedTo(Entity e) {
        if (e instanceof Player) {
            Order order = e.getCapability(SurgebindingCapability.PLAYER_CAP).map(ISurgebindingData::getOrder).orElse(null);
            return order != null && order == this.entityData.get(SPREN_TYPE);
        }

        return false;
    }

    @Override
    public boolean canBeLeashed(Player p) {
        return false;
    }

    @Override
    public boolean canMate(Animal p_70878_1_) {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public float getRed() {
        return colors[getSprenType().getIndex()][0];
    }

    @OnlyIn(Dist.CLIENT)
    public float getGreen() {
        return colors[getSprenType().getIndex()][1];
    }

    @OnlyIn(Dist.CLIENT)
    public float getBlue() {
        return colors[getSprenType().getIndex()][2];
    }

    @Override
    public boolean isFlying() {
        return false;
    }
}
