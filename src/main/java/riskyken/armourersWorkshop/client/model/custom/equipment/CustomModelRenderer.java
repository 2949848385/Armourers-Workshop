package riskyken.armourersWorkshop.client.model.custom.equipment;

import java.util.BitSet;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.Tessellator;
import riskyken.armourersWorkshop.client.abstraction.IRenderBuffer;
import riskyken.armourersWorkshop.client.abstraction.RenderBridge;

public class CustomModelRenderer extends ModelRenderer {

    public CustomModelRenderer(ModelBase p_i1174_1_, int p_i1174_2_, int p_i1174_3_) {
        super(p_i1174_1_, p_i1174_2_, p_i1174_3_);
        // TODO Auto-generated constructor stub
    }
    
    public void render(float scale, BitSet faceFlags) {
        Tessellator tessellator = Tessellator.instance;
        IRenderBuffer renderBuffer = RenderBridge.INSTANCE;
        int size = this.cubeList.size();
        renderBuffer.startDrawingQuads();
        for (int i = 0; i < size; ++i) {
            ((ModelEquipmentBox)this.cubeList.get(i)).render(renderBuffer, scale, faceFlags);
        }
        renderBuffer.draw();
    }
    
    @Override
    public ModelRenderer addBox(float x, float y, float z, int xSize, int ySize, int zSize)
    {
        this.cubeList.add(new ModelEquipmentBox(this, 0, 0, x, y, z, xSize, ySize, zSize, 0.0F));
        return this;
    }
}
