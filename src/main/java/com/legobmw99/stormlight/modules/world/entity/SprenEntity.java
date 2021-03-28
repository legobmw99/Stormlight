package com.legobmw99.stormlight.modules.world.entity;

import com.legobmw99.stormlight.modules.powers.StormlightCapability;
import com.legobmw99.stormlight.modules.world.WorldSetup;
import com.legobmw99.stormlight.util.Order;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public class SprenEntity extends TameableEntity implements IFlyingAnimal {

    public static final IDataSerializer<Enum<Order>> ORDER = new IDataSerializer<Enum<Order>>() {

        public void write(PacketBuffer buf, Enum<Order> order) {
            buf.writeEnum(order);
        }

        public Enum<Order> read(PacketBuffer buf) {
            return buf.readEnum(Order.class);
        }

        public Enum<Order> copy(Enum<Order> order) {
            return order;
        }

    };

    private static final DataParameter<Enum<Order>> SPREN_TYPE = EntityDataManager.defineId(SprenEntity.class, ORDER);
    private static final float[][] colors = {{0.737F, 0.960F, 0.945F}, {0.356F, 0.333F, 0.407F}, {0.819F, 0.819F, 0.819F}, {0.349F, 0.670F, 0.466F}, {0.913F, 0.945F, 0.945F},
                                             {0.337F, 0.286F, 0.396F}, {0.188F, 0.145F, 0.129F}, {0.815F, 0.588F, 0.207F}, {0.513F, 0.027F, 0.098F}, {0.380F, 0.352F, 0.886F}};
    private static final BasicParticleType[] particles = {ParticleTypes.CLOUD, ParticleTypes.PORTAL, ParticleTypes.ASH, ParticleTypes.HAPPY_VILLAGER, ParticleTypes.END_ROD,
                                                          ParticleTypes.ENCHANT, ParticleTypes.SQUID_INK, ParticleTypes.EFFECT, ParticleTypes.LAVA, ParticleTypes.SOUL};

    static {
        DataSerializers.registerSerializer(ORDER);
    }

    private int delay = 0;

    public SprenEntity(World world, Entity other) {
        this(null, world);
        if (other instanceof SprenEntity) {
            this.setSprenType(((SprenEntity) other).getSprenType());
        }
    }


    public SprenEntity(EntityType<SprenEntity> entityEntityType, World world) {
        super(WorldSetup.SPREN_ENTITY, world);

        this.setTame(false);
        this.moveControl = new FlyingMovementController(this, 20, true);
    }

    public static AttributeModifierMap createAttributes() {

        return MonsterEntity.createMonsterAttributes().add(Attributes.FLYING_SPEED, 0.4D).add(Attributes.MOVEMENT_SPEED, 0.2D).add(Attributes.MAX_HEALTH, 18.0D).build();

    }

    public static <T extends MobEntity> boolean checkSprenSpawnRules(EntityType<T> tEntityType,
                                                                     IServerWorld iServerWorld,
                                                                     SpawnReason spawnReason,
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
    public ILivingEntityData finalizeSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData data, @Nullable CompoundNBT nbt) {
        //todo biomes
        this.setSprenType(Order.getOrNull(this.random.nextInt(10)));
        return super.finalizeSpawn(world, difficulty, reason, data, nbt);
    }

    @Override
    public void die(DamageSource p_70645_1_) {
        // TODO break oaths
        super.die(p_70645_1_);
    }

    @Override
    public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
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
            AxisAlignedBB aabb = getBoundingBox();
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
        if ((isTame() && !in.equals(DamageSource.playerAttack((PlayerEntity) getOwner()))) || in.equals(DamageSource.OUT_OF_WORLD)) {
            return false;
        }
        return true;
    }

    @Override
    protected PathNavigator createNavigation(World world) {
        FlyingPathNavigator flying = new FlyingPathNavigator(this, world);
        flying.setCanOpenDoors(true);
        flying.setCanFloat(true);
        flying.setCanPassDoors(true);
        flying.canFloat();
        return flying;
    }

    @Override
    protected void tickEffects() {
        super.tickEffects();
    }

    @Override
    public float getEyeHeight(Pose p) {
        return 0.8f;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.0D, 5.0F, 1.0F, true));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new FollowMobGoal(this, 1.0D, 3.0F, 7.0F));
        this.goalSelector.addGoal(4, new SitGoal(this));

    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SPREN_TYPE, Order.WINDRUNNERS);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putByte("type", (byte) getSprenType().getIndex());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        setSprenType(Order.getOrNull(compoundNBT.getByte("type")));
    }

    @Override
    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
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
        if (e instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) e;
            Order order = StormlightCapability.forPlayer(e).getOrder();
            return order != null && order == this.entityData.get(SPREN_TYPE);
        }

        return false;
    }

    @Override
    public boolean canBeLeashed(PlayerEntity p) {
        return false;
    }

    @Override
    public boolean canMate(AnimalEntity p_70878_1_) {
        return false;
    }

    @Nullable
    @Override
    public AgeableEntity getBreedOffspring(ServerWorld p_241840_1_, AgeableEntity p_241840_2_) {
        return null;
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
}
