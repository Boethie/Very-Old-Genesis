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
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

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
	/** shader program **/
	public static int prog = 0;
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
		String vshsrc = MiscUtils.readFileAsString(new ResourceLocation(Constants.ASSETS_PREFIX + "shaders/portal.vsh"));
		String fshsrc = MiscUtils.readFileAsString(new ResourceLocation(Constants.ASSETS_PREFIX + "shaders/portal.fsh"));
		prog = GL20.glCreateProgram();
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
		dx = GL20.glGetUniformLocation(prog, "dx");
		dy = GL20.glGetUniformLocation(prog, "dy");
		cx = GL20.glGetUniformLocation(prog, "cx");
		cy = GL20.glGetUniformLocation(prog, "cy");
		rad = GL20.glGetUniformLocation(prog, "rad");
		GL20.glUniform1i(GL20.glGetUniformLocation(prog, "tex"), 0);
		GL20.glUniform1i(GL20.glGetUniformLocation(prog, "overlay"), 1);
	}
	
	public static void drawPortals()
	{
		if (portals.size() <= 0) return;
		int prevProg = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);
		GL11.glPointSize(1);
		GL20.glUseProgram(prog);
		GL20.glUniform1f(dx, 1f / mc.displayWidth);
		GL20.glUniform1f(dy, 1f / mc.displayHeight);
		GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
		GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		RenderManager manager = mc.getRenderManager();
		GL11.glDisable(GL11.GL_CULL_FACE);
		for (Float[] portal : portals)
		{
			mc.getFramebuffer().bindFramebufferTexture();
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
			mc.getFramebuffer().unbindFramebufferTexture();
		}
		GL11.glEnable(GL11.GL_CULL_FACE);
		GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		portals.clear();
		GL20.glUseProgram(prevProg);
	}
}