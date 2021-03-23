package com.legobmw99.stormlight.modules.world.entity;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class SprenModel extends EntityModel<SprenEntity> {

    public ModelRenderer renderer;

    public SprenModel() {
        this.texWidth = 32;
        this.texHeight = 16;
        this.renderer = new ModelRenderer(this, 0, 0);
        this.renderer.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.renderer.setPos(0.0F, 0.0F, 0.0F);
    }


    @Override
    public void renderToBuffer(MatrixStack matrixStack,
                               IVertexBuilder iVertexBuilder,
                               int p_225598_3_,
                               int p_225598_4_,
                               float p_225598_5_,
                               float p_225598_6_,
                               float p_225598_7_,
                               float p_225598_8_) {
        this.renderer.render(matrixStack, iVertexBuilder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
    }

    @Override
    public void setupAnim(SprenEntity p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {

    }
}
