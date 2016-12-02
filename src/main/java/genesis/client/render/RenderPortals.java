package genesis.client.render;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.glu.GLU;

import genesis.common.Genesis;
import genesis.util.Constants;
import genesis.util.MiscUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class RenderPortals
{
    /** The current GL viewport */
    private static final IntBuffer VIEWPORT = GLAllocation.createDirectIntBuffer(16);
    /** The current GL modelview matrix */
    private static final FloatBuffer MODELVIEW = GLAllocation.createDirectFloatBuffer(16);
    /** The current GL projection matrix */
    private static final FloatBuffer PROJECTION = GLAllocation.createDirectFloatBuffer(16);
    /** The current GL projection matrix */
    private static final FloatBuffer WINDOW = GLAllocation.createDirectFloatBuffer(3);
	/** list of {x, y, z, radius}, where x, y, and z are RENDER coordinates **/
	public static final ArrayList<Float[]> portals = new ArrayList<Float[]>();
	/** just a texture **/
	private static final int tex = GL11.glGenTextures();
	/** shader program **/
	public static int prog = 0;
	/** passthrough shader **/
	public static int passthrough = 0;
	/** passthrough shader uniform "dx" **/
	public static int pdx;
	/** passthrough shader uniform "dy" **/
	public static int pdy;
	/** shader uniform "dx" **/
	public static int dx;
	/** shader uniform "dy" **/
	public static int dy;
	/** shader uniform "cx" **/
	public static int cx;
	/** shader uniform "cy" **/
	public static int cy;
	/** shader uniform "rad" **/
	public static int rad;
	/** screen size **/
	public static int w = 0, h = 0;
	/** previous shader program **/
	private static int prevProg = 0;
	public static final Minecraft mc = Minecraft.getMinecraft();
	public static void load()
	{
		if (prog != 0) GL20.glDeleteProgram(prog);
		prog = createShader("portal");
		dx = GL20.glGetUniformLocation(prog, "dx");
		dy = GL20.glGetUniformLocation(prog, "dy");
		cx = GL20.glGetUniformLocation(prog, "cx");
		cy = GL20.glGetUniformLocation(prog, "cy");
		rad = GL20.glGetUniformLocation(prog, "rad");
		int prevProg = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);
		GL20.glUseProgram(prog);
		GL20.glUniform1i(GL20.glGetUniformLocation(prog, "tex"), 0);
		GL20.glUniform1i(GL20.glGetUniformLocation(prog, "overlay"), 1);
		GL20.glUniform1f(GL20.glGetUniformLocation(prog, "pi"), (float) Math.PI);
		if (passthrough != 0) GL20.glDeleteProgram(passthrough);
		passthrough = createShader("passthrough");
		pdx = GL20.glGetUniformLocation(passthrough, "dx");
		pdy = GL20.glGetUniformLocation(passthrough, "dy");
		GL20.glUseProgram(passthrough);
		GL20.glUniform1i(GL20.glGetUniformLocation(passthrough, "tex"), 0);
		GL20.glUseProgram(prevProg);
	}
	
	public static int createShader(String name)
	{
		String vshsrc = MiscUtils.readFileAsString(new ResourceLocation(Constants.ASSETS_PREFIX + "shaders/" + name + ".vsh"));
		String fshsrc = MiscUtils.readFileAsString(new ResourceLocation(Constants.ASSETS_PREFIX + "shaders/" + name + ".fsh"));
		int prog = GL20.glCreateProgram();
		int vsh = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		int fsh = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		GL20.glShaderSource(vsh, vshsrc);
		GL20.glShaderSource(fsh, fshsrc);
		GL20.glCompileShader(vsh);
		GL20.glCompileShader(fsh);
		String verr = GL20.glGetShaderInfoLog(vsh, 512);
		if (verr.length() > 0) Genesis.logger.error("VSH ERROR\n" + verr);
		String ferr = GL20.glGetShaderInfoLog(fsh, 512);
		if (ferr.length() > 0) Genesis.logger.error("FSH ERROR\n" + ferr);
		GL20.glAttachShader(prog, vsh);
		GL20.glAttachShader(prog, fsh);
		GL20.glLinkProgram(prog);
		String perr = GL20.glGetProgramInfoLog(prog, 512);
		if (perr.length() > 0) Genesis.logger.error("PROGRAM ERROR\n" + perr);
		return prog;
	}
	
	public static void drawPortals()
	{
		if (portals.size() <= 0) return;
		int prevProg = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);
		Framebuffer f = mc.getFramebuffer();
		int width = f.framebufferTextureWidth;
		int height = f.framebufferTextureHeight;
		Framebuffer f2 = new Framebuffer(width, height, false);
		f2.bindFramebuffer(false);
		f.bindFramebufferTexture();
		GL20.glUseProgram(passthrough);
		GL20.glUniform1f(pdx, 1f / width);
		GL20.glUniform1f(pdy, 1f / height);
        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0, width, height, 0, 1000, 3000);
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.translate(0, 0, -2000);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3f(0    , height, 0);
        GL11.glVertex3f(width, height, 0);
        GL11.glVertex3f(width, 0     , 0);
        GL11.glVertex3f(0    , 0     , 0);
        GL11.glEnd();
        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.popMatrix();
		f.bindFramebuffer(false);
		f2.bindFramebufferTexture();
		GL20.glUseProgram(prog);
		GL20.glUniform1f(dx, 1f / width);
		GL20.glUniform1f(dy, 1f / height);
		GL11.glEnable(GL11.GL_BLEND);
		GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		RenderManager manager = mc.getRenderManager();
		for (Float[] portal : portals)
		{
			float x = portal[0];
			float y = portal[1];
			float z = portal[2];
			float r = portal[3];
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            GlStateManager.rotate(180.0F - manager.playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(manager.options.thirdPersonView == 2 ? manager.playerViewX : -manager.playerViewX, 1.0F, 0.0F, 0.0F);
            GlStateManager.getFloat(GL11.GL_MODELVIEW_MATRIX, MODELVIEW);
            GlStateManager.getFloat(GL11.GL_PROJECTION_MATRIX, PROJECTION);
            GlStateManager.glGetInteger(GL11.GL_VIEWPORT, VIEWPORT);
			GLU.gluProject(0, 0, 0, MODELVIEW, PROJECTION, VIEWPORT, WINDOW);
			float cX = WINDOW.get(0);
			float cY = WINDOW.get(1);
			GLU.gluProject(r, 0, 0, MODELVIEW, PROJECTION, VIEWPORT, WINDOW);
			float oX = WINDOW.get(0) - cX;
			float oY = WINDOW.get(1) - cY;
			float ra = MathHelper.sqrt_float(oX * oX + oY * oY);
			GL20.glUniform1f(cx, cX);
			GL20.glUniform1f(cy, cY);
			GL20.glUniform1f(rad, ra);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex3d(-r, -r, 0);
			GL11.glVertex3d(r, -r, 0);
			GL11.glVertex3d(r, r, 0);
			GL11.glVertex3d(-r, r, 0);
			GL11.glEnd();
			GL11.glPopMatrix();
		}
		GL11.glDisable(GL11.GL_BLEND);
		//Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		f2.unbindFramebufferTexture();
		f2.deleteFramebuffer();
		GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR);
		GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		portals.clear();
		GL20.glUseProgram(prevProg);
	}
}