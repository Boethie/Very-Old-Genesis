package genesis.util.render;

import genesis.util.EnumAxis;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.Vec3;

public class ModelPlane extends CustomModelElement
{
	protected TexturedQuad quadUp;
	protected TexturedQuad quadDown;
	
	public ModelPlane(ModelRenderer part, float u, float v, EnumAxis facingAxis, float x, float y, float z, float w, float h)
	{
		super(part);
		
		Vec3 start = new Vec3(x, y, z);
		Vec3 localW = null;
		Vec3 localH = null;
		
		float texW = part.textureWidth;
		float texH = part.textureHeight;

		float uW = w / texW;
		float uH = h / texH;
		
		switch (facingAxis)
		{
		case X:
			localW = new Vec3(0, 0, w);
			localH = new Vec3(0, h, 0);
			break;
		case Y:
			localW = new Vec3(h, 0, 0);
			localH = new Vec3(0, 0, w);
			
			uW = h / texW;
			uH = w / texH;
			break;
		case Z:
			localW = new Vec3(w, 0, 0);
			localH = new Vec3(0, h, 0);
			break;
		default:
			throw new IllegalArgumentException(facingAxis + " is not a valid axis for a ModelPlane.");
		}
		
		float uStart = u / texW;
		float uEnd = u / texW + uW;
		float vStart = v / texH;
		float vEnd = v / texH + uH;
		
		PositionTextureVertex vertBL;
		PositionTextureVertex vertTL;
		PositionTextureVertex vertTR;
		PositionTextureVertex vertBR;
		
		vertBL = new PositionTextureVertex(start, 							uStart, vStart);
		vertTL = new PositionTextureVertex(start.add(localH),				uStart, vEnd);
		vertTR = new PositionTextureVertex(start.add(localW).add(localH),	uEnd, vEnd);
		vertBR = new PositionTextureVertex(start.add(localW),				uEnd, vStart);
		
		quadUp = new TexturedQuad(new PositionTextureVertex[]{vertBR, vertTR, vertTL, vertBL});
		
		uStart += uW;
		uEnd += uW;
		vertBL = new PositionTextureVertex(start.add(localW), 				uStart, vStart);
		vertTL = new PositionTextureVertex(start.add(localW).add(localH),	uStart, vEnd);
		vertTR = new PositionTextureVertex(start.add(localH),				uEnd, vEnd);
		vertBR = new PositionTextureVertex(start,							uEnd, vStart);
		
		quadDown = new TexturedQuad(new PositionTextureVertex[]{vertBR, vertTR, vertTL, vertBL});
	}
	
	public ModelPlane(EntityPart part, EnumAxis facingAxis, float x, float y, float z, float w, float h)
	{
		this(part, part.getTextureOffsetX(), part.getTextureOffsetY(), facingAxis, x, y, z, w, h);
	}
	
	@Override
	public void render(WorldRenderer renderer, float scale)
	{
		GlStateManager.enableCull();
		quadUp.draw(renderer, scale);
		quadDown.draw(renderer, scale);
		GlStateManager.disableCull();
	}
}
