package genesis.metadata;

public interface IMetadata<V> extends IVariant, Comparable<V>
{
	public String getUnlocalizedName();
}
