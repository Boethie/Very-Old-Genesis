package genesis.util.render;

import java.util.*;

import com.google.common.collect.*;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;

@SuppressWarnings("deprecation")
public class NormalizedCubeProjectedBakedModel implements IBakedModel
{
	public static class UVRange
	{
		public float minU = Float.MAX_VALUE;
		public float maxU = -Float.MAX_VALUE;
		public float minV = Float.MAX_VALUE;
		public float maxV = -Float.MAX_VALUE;
		
		public void apply(float u, float v)
		{
			minU = Math.min(minU, u);
			maxU = Math.max(maxU, u);
			minV = Math.min(minV, v);
			maxV = Math.max(maxV, v);
		}
	}
	
	private static final int VERT_LENGTH = 7;
	
	public static class RemappedQuad extends BakedQuad
	{
		public RemappedQuad(BakedQuad base, TextureAtlasSprite texture)
		{
			super(Arrays.copyOf(base.getVertexData(), base.getVertexData().length), base.getTintIndex(), base.getFace());
			
			for (int vert = 0; vert < 4 * VERT_LENGTH; vert += VERT_LENGTH)
			{
				float x = Float.intBitsToFloat(vertexData[vert]);
				float y = Float.intBitsToFloat(vertexData[vert + 1]);
				float z = Float.intBitsToFloat(vertexData[vert + 2]);
				float u = 0;
				float v = 0;
				
				switch (face)
				{
					case DOWN:
						u = x * 16;
						v = (1 - z) * 16;
						break;
					case UP:
						u = x * 16;
						v = z * 16;
						break;
					case NORTH:
						u = (1 - x) * 16;
						v = (1 - y) * 16;
						break;
					case SOUTH:
						u = x * 16;
						v = (1 - y) * 16;
						break;
					case WEST:
						u = z * 16;
						v = (1 - y) * 16;
						break;
					case EAST:
						u = (1 - z) * 16;
						v = (1 - y) * 16;
				}
				
				vertexData[vert + 4] = Float.floatToRawIntBits(texture.getInterpolatedU(u));
				vertexData[vert + 4 + 1] = Float.floatToRawIntBits(texture.getInterpolatedV(v));
			}
		}
		
		public void normalize(UVRange range, TextureAtlasSprite texture)
		{
			for (int vert = 0; vert < 4 * VERT_LENGTH; vert += VERT_LENGTH)
			{
				int iU = vert + 4;
				int iV = vert + 4 + 1;
				float u = Float.intBitsToFloat(vertexData[iU]);
				float v = Float.intBitsToFloat(vertexData[iV]);
				u = (u - range.minU) / (range.maxU - range.minU);
				v = (v - range.minV) / (range.maxV - range.minV);
				vertexData[iU] = Float.floatToRawIntBits(u);
				vertexData[iV] = Float.floatToRawIntBits(v);
			}
		}
	}
	
	private static void applyRanges(Map<EnumFacing, UVRange> map, List<BakedQuad> quads)
	{
		for (BakedQuad quad : quads)
		{
			UVRange range = map.get(quad.getFace());
			
			if (range == null)
			{
				range = new UVRange();
				map.put(quad.getFace(), range);
			}

			for (int vert = 0; vert < 4 * VERT_LENGTH; vert += VERT_LENGTH)
			{
				range.apply(Float.intBitsToFloat(quad.getVertexData()[vert + 4]),
							Float.intBitsToFloat(quad.getVertexData()[vert + 4 + 1]));
			}
		}
	}
	
	private final ImmutableMap<EnumFacing, ImmutableList<BakedQuad>> faceQuads;
	private final ImmutableList<BakedQuad> generalQuads;
	private final TextureAtlasSprite texture;
	private final boolean ao;
	private final boolean gui3D;
	private final ItemCameraTransforms transforms;
	
	public NormalizedCubeProjectedBakedModel(IBakedModel base, TextureAtlasSprite texture)
	{
		ao = base.isAmbientOcclusion();
		gui3D = base.isGui3d();
		transforms = base.getItemCameraTransforms();
		
		this.texture = texture;
		
		// Create RemappedQuads for face quads.
		Map<EnumFacing, List<BakedQuad>> faceQuadsMap = Maps.newHashMap();
		
		for (EnumFacing facing : EnumFacing.values())
		{
			List<BakedQuad> quads = Lists.newArrayList();
			List<BakedQuad> baseQuads = base.getFaceQuads(facing);
			
			for (BakedQuad quad : baseQuads)
			{
				quads.add(new RemappedQuad(quad, texture));
			}
			
			faceQuadsMap.put(facing, quads);
		}
		
		// Create RemappedQuads for general quads.
		List<BakedQuad> generalQuadsList = Lists.newArrayList();
		List<BakedQuad> baseQuads = base.getGeneralQuads();
		
		for (BakedQuad quad : baseQuads)
			generalQuadsList.add(new RemappedQuad(quad, texture));
		
		Map<EnumFacing, UVRange> ranges = new EnumMap<EnumFacing, UVRange>(EnumFacing.class);
		
		// Get the UV ranges to use in normalization.
		for (EnumFacing face : EnumFacing.values())
			applyRanges(ranges, faceQuadsMap.get(face));
		applyRanges(ranges, generalQuadsList);
		
		// Normalize the faces.
		for (EnumFacing face : EnumFacing.values())
			for (BakedQuad quad : faceQuadsMap.get(face))
				((RemappedQuad) quad).normalize(ranges.get(quad.getFace()), texture);
		
		for (BakedQuad quad : generalQuadsList)
			((RemappedQuad) quad).normalize(ranges.get(quad.getFace()), texture);
		
		// Copy the ArrayLists into ImmutableLists.
		ImmutableMap.Builder<EnumFacing, ImmutableList<BakedQuad>> faceQuadsBuilder = ImmutableMap.builder();
		
		for (EnumFacing face : EnumFacing.values())
			faceQuadsBuilder.put(face, ImmutableList.copyOf(faceQuadsMap.get(face)));
		
		faceQuads = faceQuadsBuilder.build();
		generalQuads = ImmutableList.copyOf(generalQuadsList);
	}
	
	@Override
	public List<BakedQuad> getFaceQuads(EnumFacing face)
	{
		return faceQuads.get(face);
	}
	
	@Override
	public List<BakedQuad> getGeneralQuads()
	{
		return generalQuads;
	}
	
	@Override
	public TextureAtlasSprite getTexture()
	{
		return texture;
	}
	
	@Override
	public boolean isAmbientOcclusion()
	{
		return ao;
	}
	
	@Override
	public boolean isGui3d()
	{
		return gui3D;
	}
	
	@Override
	public ItemCameraTransforms getItemCameraTransforms()
	{
		return transforms;
	}
	
	@Override
	public boolean isBuiltInRenderer()
	{
		return false;
	}
}
