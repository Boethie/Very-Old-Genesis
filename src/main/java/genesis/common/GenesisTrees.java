package genesis.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.base.Function;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import genesis.block.BlockGenesisLogs;
import genesis.item.ItemBlockMetadata;
import genesis.metadata.EnumTree;
import genesis.util.BlockStateToMetadata;
import genesis.util.Constants;
import genesis.util.Metadata;

public class GenesisTrees
{
	/*public static ArrayList<BlockGenesisLogs> logBlocks = new ArrayList<BlockGenesisLogs>(){};
	
	public static <T> void populate(ArrayList<T> list, Class<? extends T> clazz)
	{
		try
		{
			List<EnumTree> typesList = Arrays.asList(EnumTree.values());
			
			Method propsFunc = clazz.getMethod("getProperties");
			Object propsListObj = propsFunc.invoke(null);
			
			int maxVariants = Short.MAX_VALUE - 1;
			
			if (propsListObj != null)
			{
				maxVariants = BlockStateToMetadata.getMetadataLeftAfter((IProperty[]) propsListObj);
			}
			
			for (int i = 0; i < Math.ceil(typesList.size() / (float) maxVariants); i++)
			{
				List<EnumTree> variants = typesList.subList(i * maxVariants, Math.min((i + 1) * maxVariants, typesList.size()));
				
				Constructor<? extends T> constructor = clazz.getConstructor(List.class);
				T obj = constructor.newInstance(variants);
				
				list.add(obj);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static <T> void populate(ArrayList<T> list)
	{
		populate(list, (Class<T>) ((ParameterizedType) list.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
	}
	
	static
	{
		populate(logBlocks);
	}

	public static void registerAll()
	{
		int i = 0;
		
		for (final BlockGenesisLogs logs : logBlocks)
		{
			ItemMultiTexture item = new ItemMultiTexture(logs, logs, new Function()
			{
				@Override
				public Object apply(Object input)
				{
					return logs.TYPES.get(((ItemStack) input).getMetadata()).getUnlocalizedName();
				}
			});
			Genesis.proxy.registerBlockWithItem(logs, "log" + i, item);
			logs.setUnlocalizedName("log");
			
			i++;
		}
	}*/
}
