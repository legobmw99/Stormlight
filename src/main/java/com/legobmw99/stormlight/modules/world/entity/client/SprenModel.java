package com.legobmw99.stormlight.modules.world.entity.client;


import com.legobmw99.stormlight.modules.world.entity.SprenEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class SprenModel extends EntityModel<SprenEntity> {

    private final ModelRenderer renderer;
    private SprenEntity spren;

    public SprenModel() {
        super(RenderType::entityTranslucent);
        this.texWidth = 32;
        this.texHeight = 16;
        this.renderer = new ModelRenderer(this, 0, 0);
        this.renderer.addBox(-4F, 16F, -4.0F, 8, 8, 8, 1.0F);
        this.renderer.setPos(0.0F, 0.0F, 0.0F);
    }


    @Override
    public void renderToBuffer(MatrixStack matrix, IVertexBuilder iVertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        matrix.pushPose();
        this.renderer.render(matrix, iVertexBuilder, packedLightIn, packedOverlayIn, spren.getRed(), spren.getGreen(), spren.getBlue(), 0.3F);
        matrix.popPose();
    }

    @Override
    public void setupAnim(SprenEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.renderer.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.renderer.xRot = headPitch * ((float) Math.PI / 180F);
        this.spren = entity;

    }
}
