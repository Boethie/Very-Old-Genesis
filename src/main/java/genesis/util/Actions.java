package genesis.util;

import net.minecraft.util.ActionResult;

import static net.minecraft.util.EnumActionResult.*;

public class Actions
{
	public static <T> ActionResult<T> success(T result)
	{
		return ActionResult.newResult(SUCCESS, result);
	}
	
	public static <T> ActionResult<T> fail(T result)
	{
		return ActionResult.newResult(FAIL, result);
	}
	
	public static <T> ActionResult<T> pass(T result)
	{
		return ActionResult.newResult(PASS, result);
	}
}
