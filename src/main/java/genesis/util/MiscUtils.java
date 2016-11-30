package genesis.util;

import com.google.common.collect.Iterators;

import genesis.common.Genesis;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public final class MiscUtils
{
	@SafeVarargs
	public static <T> Iterable<T> iterable(final T... array)
	{
		return () -> Iterators.forArray(array);
	}

	@SafeVarargs
	public static <T extends Enum<T>> Set<T> unmodSet(T... values)
	{
		Set<T> out = EnumSet.noneOf(ReflectionUtils.getClass(values));
		Collections.addAll(out, values);
		return Collections.unmodifiableSet(out);
	}

    /** reads a text file as a single string of text **/
    public static String readFileAsString(ResourceLocation loc)
    {
    	try
    	{
        	InputStream in = Minecraft.getMinecraft().getResourceManager().getResource(loc).getInputStream();
            StringBuilder source = new StringBuilder();
            Exception exception = null;
            BufferedReader reader;
            try
            {
                reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                Exception innerExc= null;
                try
                {
                    String line;
                    while((line = reader.readLine()) != null) source.append(line).append('\n');
                }
                catch(Exception exc)
                {
                    exception = exc;
                }
                finally
                {
                    try
                    {
                        reader.close();
                    }
                    catch(Exception exc)
                    {
                        if (innerExc == null) innerExc = exc;
                        else exc.printStackTrace();
                    }
                }
                if (innerExc != null) throw innerExc;
            }
            catch(Exception exc)
            {
                exception = exc;
            }
            finally
            {
                try
                {
                    in.close();
                }
                catch(Exception exc)
                {
                    if (exception == null) exception = exc;
                    else exc.printStackTrace();
                }
                if (exception != null) throw exception;
            }
            return source.toString();
		}
    	catch (Exception e)
    	{
    		Genesis.logger.error("Could not load file " + loc.toString(), e);
			return "";
		}
    }
}
