package genesis.common;

import genesis.util.Constants;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;

import org.apache.commons.compress.utils.IOUtils;

import com.google.common.io.ByteStreams;
import com.google.gson.Gson;

public final class GenesisVersion extends ForgeVersion
{
	public static Status status = Status.PENDING;
	public static String target = null;
	public static String homepage = null;

	/**
	 * Check latest mod version for the current Minecraft version
	 *
	 * @see ForgeVersion#startVersionCheck()
	 */
	public static void startVersionCheck()
	{
		new Thread(Constants.MOD_NAME + " Version Check")
		{
			@Override
			public void run()
			{
				InputStream con = null;

				try
				{
					URL url = new URL(Constants.VERSIONS_URL);
					con = url.openStream();
					String data = new String(ByteStreams.toByteArray(con));

					Map<String, Object> json = new Gson().fromJson(data, Map.class);
					homepage = (String) json.get("homepage");
					Map<String, String> promos = (Map<String, String>) json.get("versions");

					String lat = promos.get(MinecraftForge.MC_VERSION);
					ArtifactVersion currentVersion = new DefaultArtifactVersion(Constants.MOD_VERSION);

					if (lat != null)
					{
						if (currentVersion.compareTo(new DefaultArtifactVersion(lat)) < 0)
						{
							status = Status.OUTDATED;
							target = lat;
						}
						else
						{
							status = Status.UP_TO_DATE;
						}
					}
					else
					{
						status = Status.BETA;
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
					status = Status.FAILED;
				}
				finally
				{
					IOUtils.closeQuietly(con);

					Genesis.logger.info("Version status: " + status.toString());
				}
			}
		}.start();
	}
}
