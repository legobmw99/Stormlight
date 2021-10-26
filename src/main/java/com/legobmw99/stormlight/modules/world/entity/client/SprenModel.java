package com.legobmw99.stormlight.modules.world.entity.client;


import com.legobmw99.stormlight.Stormlight;
import com.legobmw99.stormlight.modules.world.entity.SprenEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class SprenModel extends HierarchicalModel<SprenEntity> {

    public static final ModelLayerLocation MODEL_LOC = new ModelLayerLocation(new ResourceLocation(Stormlight.MODID, "spren_model"), "main");

    private final ModelPart root;
    private SprenEntity spren;

    public SprenModel(ModelPart p_170955_) {
        super(RenderType::entityTranslucent);

        this.root = p_170955_;
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        var builder = CubeListBuilder.create().texOffs(0, 0).addBox(-4F, 16F, -4.0F, 8, 8, 8);
        partdefinition.addOrReplaceChild("cube", builder, PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 32, 16);
    }

    @Override
    public void renderToBuffer(PoseStack matrix, VertexConsumer iVertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        matrix.pushPose();
        this.root.render(matrix, iVertexBuilder, packedLightIn, packedOverlayIn, spren.getRed(), spren.getGreen(), spren.getBlue(), 0.3F);
        matrix.popPose();
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(SprenEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.root.xRot = headPitch * ((float) Math.PI / 180F);
        this.spren = entity;

    }
}
