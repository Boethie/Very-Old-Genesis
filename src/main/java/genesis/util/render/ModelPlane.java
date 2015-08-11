package genesis.util.render;

import genesis.util.EnumAxis;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
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
		Vec3 normal = null;
		Vec3 localW = null;
		Vec3 localH = null;
		
		switch (facingAxis)
		{
		case X:
			normal = new Vec3(1, 0, 0);
			localW = new Vec3(0, 0, w);
			localH = new Vec3(0, h, 0);
			break;
		case Y:
			normal = new Vec3(0, 1, 0);
			localW = new Vec3(h, 0, 0);
			localH = new Vec3(0, 0, w);
			break;
		case Z:
			normal = new Vec3(0, 0, -1);
			localW = new Vec3(w, 0, 0);
			localH = new Vec3(0, h, 0);
			break;
		case NONE:
			throw new IllegalArgumentException("NONE axis is not a valid axis for a ModelPlane.");
		}
		
		float texW = part.textureWidth;
		float texH = part.textureHeight;
		float uStart = u / texW;
		float uEnd = (u + w) / texW;
		float vStart = v / texH;
		float vEnd = (v + h) / texH;
		
		PositionTextureVertex vertBL = new PositionTextureVertex(start, uStart, vStart);
		PositionTextureVertex vertTL = new PositionTextureVertex(start.add(localH), uStart, vEnd);
		PositionTextureVertex vertTR = new PositionTextureVertex(vertTL.vector3D.add(localW), uEnd, vEnd);
		PositionTextureVertex vertBR = new PositionTextureVertex(vertTR.vector3D.subtract(localH), uEnd, v);
		
		quadUp = new TexturedQuad(new PositionTextureVertex[]{vertBL, vertTL, vertTR, vertBR});
		
		double sepAmt = -0.01;
		Vec3 sep = new Vec3(normal.xCoord * sepAmt, normal.yCoord * sepAmt, normal.zCoord * sepAmt);
		vertBL = new PositionTextureVertex(vertBL.vector3D.add(sep), vertBL.texturePositionX, vertBL.texturePositionY);
		vertTL = new PositionTextureVertex(vertTL.vector3D.add(sep), vertBL.texturePositionX, vertBL.texturePositionY);
		vertTR = new PositionTextureVertex(vertTR.vector3D.add(sep), vertTR.texturePositionX, vertTR.texturePositionY);
		vertBR = new PositionTextureVertex(vertBR.vector3D.add(sep), vertBR.texturePositionX, vertBR.texturePositionY);
		
		quadDown = new TexturedQuad(new PositionTextureVertex[]{vertBL, vertBR, vertTR, vertTL});
	}
	
	public ModelPlane(EntityPart part, EnumAxis facingAxis, float x, float y, float z, float w, float h)
	{
		this(part, part.getTextureOffsetX(), part.getTextureOffsetY(), facingAxis, x, y, z, w, h);
	}
	
	@Override
	public void render(WorldRenderer renderer, float scale)
	{
		quadUp.draw(renderer, scale);
		quadDown.draw(renderer, scale);
	}
}
