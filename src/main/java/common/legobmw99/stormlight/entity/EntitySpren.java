package common.legobmw99.stormlight.entity;

import javax.annotation.Nullable;

import com.elytradev.mirage.lighting.IColoredLight;
import com.elytradev.mirage.lighting.Light;

import common.legobmw99.stormlight.Stormlight;
import common.legobmw99.stormlight.network.packets.StormlightCapabilityPacket;
import common.legobmw99.stormlight.util.Registry;
import common.legobmw99.stormlight.util.StormlightCapability;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollow;
import net.minecraft.entity.ai.EntityAIFollowOwnerFlying;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWaterFlying;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityFlyHelper;
import net.minecraft.entity.passive.EntityFlying;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Optional.Interface(iface="com.elytradev.mirage.lighting.IColoredLight", modid="mirage")
public class EntitySpren extends EntityTameable implements EntityFlying, IColoredLight {

	private static final DataParameter<Integer> SPREN_TYPE = EntityDataManager.createKey(EntitySpren.class,
			DataSerializers.VARINT);

	private static final float[][] colors = { 
			{0.55F, 0.70F, 0.38F}, 
			{0.02F, 0.40F, 0.13F},
			{0.98F, 0.58F, 0.09F}, 
			{0.33F, 0.48F, 0.04F},
			{0.04F, 0.40F, 0.35F},
			{1.00F, 1.00F, 1.00F},
			{0.85F, 0.27F, 0.08F},
			{0.03F, 0.98F, 0.70F},
			{0.38F, 0.38F, 0.38F },
			{0.74F, 0.70F, 0.37F}};
	
	private static final String[] SPREN_TYPES = {
			"Windrunner",
			"Skybreaker",
			"Dustbringer",
			"Edgedancer",
			"Truthwatcher",
			"Lightweaver",
			"Elsecaller",
			"Willshaper",
			"Stoneward",
			"Bondsmith"
	};

	public EntitySpren(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 1F);
		this.setTamed(false);
		this.moveHelper = new EntityFlyHelper(this);
	}
	


	@Override
	protected void entityInit() {
		super.entityInit();
		this.getDataManager().register(SPREN_TYPE, Integer.valueOf(0));
	}

	@Override
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		int type;
		switch (Biome.getIdForBiome(this.getEntityWorld().getBiome(getPosition()))) {
		case 1: // Plains
			type = 0; // Windrunners
			break;
		case 4: // Forest
			type = 1; // Skybreakers
			break;
		case 2: // Desert
			type = 2; // Dustbringers
			break;
		case 21: // Jungle
			type = 3; // Edgedancers
			break;
		case 5: // Taiga
			type = 4; // Truthwatchers
			break;
		case 12: // Ice plains
			type = 5; // Lightweavers
			break;
		case 37: // Mesa
			type = 6; // Elsecallers
			break;
		case 6: // Swamp
			type = 7; // Willshapers
			break;
		case 3: // Extreme hills
			type = 8; // Stonewards
			break;
		case 35: // Savanna
			type = 9; // Bondsmiths
			break;
		default: //Mushroom biome, and any other if using spawn egg
			type = this.rand.nextInt(10);
		}
		this.setType(type);
		String name = SPREN_TYPES[getType()] + "s";
		this.setCustomNameTag(name);
		return super.onInitialSpawn(difficulty, livingdata);
	}

	@Override
	protected void initEntityAI() {
		this.aiSit = new EntityAISit(this);
		this.tasks.addTask(0, new EntityAIPanic(this, 1.25D));
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(2, this.aiSit);
		this.tasks.addTask(2, new EntityAIFollowOwnerFlying(this, 1.0D, 5.0F, 1.0F));
		this.tasks.addTask(2, new EntityAIWanderAvoidWaterFlying(this, 1.0D));
		this.tasks.addTask(3, new EntityAIFollow(this, 1.0D, 3.0F, 7.0F));
	}

	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		return null;
	}

	public void setType(int type) {
		this.getDataManager().set(SPREN_TYPE, Integer.valueOf(type));
	}

	public int getType() {
		return this.getDataManager().get(SPREN_TYPE).intValue();
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();

		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
		this.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.4000000059604645D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);

		if (isTamed()) {
			this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(180.0D);
		}
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(18.0D);

	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 1;
	}

	@Override
	protected PathNavigate createNavigator(World worldIn) {
		PathNavigateFlying pathnavigateflying = new PathNavigateFlying(this, worldIn);
		pathnavigateflying.setCanOpenDoors(true);
		pathnavigateflying.setCanFloat(true);
		pathnavigateflying.setCanEnterDoors(true);
		return pathnavigateflying;
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public void fall(float distance, float damageMultiplier) {
	}

	@Override
	protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
	}

	@Override
	public void setTamed(boolean tamed) {
		super.setTamed(tamed);
		if (tamed) {
			this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(180.0D);
		}
	}

	@Override
	public float getEyeHeight() {
		return 0.8F;
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		ItemStack itemstack = player.getHeldItem(hand);
		if (isTamed()) {
			if (this.isOwner(player) && !this.world.isRemote && !this.isBreedingItem(itemstack)) {
				this.aiSit.setSitting(!this.isSitting());
				this.isJumping = false;
				this.navigator.clearPathEntity();
				this.setAttackTarget((EntityLivingBase) null);
			}
		} else if (itemstack.getItem() == Items.NETHER_STAR
				&& player.hasCapability(Stormlight.PLAYER_CAP, null)
				&& player.getCapability(Stormlight.PLAYER_CAP, null).getType() < 0) {
			if (!player.capabilities.isCreativeMode) {
				itemstack.shrink(1);
			}
			if (!this.world.isRemote) {
				if (!net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {
					this.setTamedBy(player);
					
					//Add capability stuff
					StormlightCapability cap = StormlightCapability.forPlayer(player);
					cap.setType(this.getType());
					cap.setProgression(0);
					cap.setSprenID(this.getPersistentID());
					
					Registry.network.sendTo(new StormlightCapabilityPacket(player.getCapability(Stormlight.PLAYER_CAP, null)), (EntityPlayerMP) player);
					
					//Advancement stuff
					AdvancementManager manager = player.getServer().getAdvancementManager();
					((EntityPlayerMP)player).getAdvancements().grantCriterion(manager.getAdvancement(new ResourceLocation(Stormlight.MODID,"surgebinding/bondSpren")), "impossible");
					((EntityPlayerMP)player).getAdvancements().grantCriterion(manager.getAdvancement(new ResourceLocation(Stormlight.MODID,"surgebinding/become" + SPREN_TYPES[getType()])), "impossible");

					
					//Normal taming stuff
					this.navigator.clearPathEntity();
					this.setAttackTarget((EntityLivingBase) null);
					this.aiSit.setSitting(true);
					this.setHealth(20.0F);
					this.setCustomNameTag("");
					this.playTameEffect(true);
					this.world.setEntityState(this, (byte) 7);
				} else {
					this.playTameEffect(false);
					this.world.setEntityState(this, (byte) 6);
				}
				return true;
			}
		}
		return super.processInteract(player, hand);
	}

	@Override
	public void onDeath(DamageSource cause) {
		// No longer Stormbound
		if (!this.world.isRemote && this.getOwner() instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) this.getOwner();
			StormlightCapability cap = StormlightCapability.forPlayer(player);
			cap.setType(-1);
			cap.setProgression(-1);
			cap.setBladeStored(true);
			Registry.network.sendTo(new StormlightCapabilityPacket(this.getOwner().getCapability(Stormlight.PLAYER_CAP, null)), (EntityPlayerMP) this.getOwner());
			
			AdvancementManager manager = player.getServer().getAdvancementManager();
			player.getAdvancements().revokeCriterion(manager.getAdvancement(new ResourceLocation(Stormlight.MODID,"surgebinding/bondSpren")), "impossible");
			player.getAdvancements().revokeCriterion(manager.getAdvancement(new ResourceLocation(Stormlight.MODID,"surgebinding/become" + SPREN_TYPES[getType()])), "impossible");

		}
		super.onDeath(cause);
	}
	

	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return false;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.isEntityInvulnerable(source)) {
			return false;
		} else {
			if (this.aiSit != null) {
				this.aiSit.setSitting(false);
			}

			return super.attackEntityFrom(source, amount);
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("TYPE", this.getType());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.setType(compound.getInteger("TYPE"));
	}

	@Override
	public SoundCategory getSoundCategory() {
		return SoundCategory.NEUTRAL;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (this.rand.nextInt(15) == 0) {
			double d0 = this.rand.nextGaussian() * 0.02D;
			double d1 = this.rand.nextGaussian() * 0.02D;
			double d2 = this.rand.nextGaussian() * 0.02D;
			this.world.spawnParticle(EnumParticleTypes.END_ROD,
					this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width,
					this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height),
					this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
		}
		if (this.rand.nextInt(30) == 0) {
			this.heal(2.0F);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public float getRed(){
		return colors[getType()][0];
	}
	@SideOnly(Side.CLIENT)
	public float getGreen(){
		return colors[getType()][1];
	}	
	@SideOnly(Side.CLIENT)
	public float getBlue(){
		return colors[getType()][2];
	}

	@Override
	public boolean canBePushed() {
		return true;
	}

	@Override
	@Nullable
	protected ResourceLocation getLootTable() {
		return null;
	}

	@SideOnly(Side.CLIENT)
	@Optional.Method(modid = "albedo")
	@Override
	public Light getColoredLight() {
		return Light.builder()
				.color(getRed(), getGreen(), getBlue()).radius(4).pos(posX, posY + this.height / 2, posZ).build();
	}
}
