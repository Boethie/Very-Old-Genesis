package genesis.util;

import static genesis.util.SimpleIterator.State.*;

import java.util.NoSuchElementException;

import com.google.common.base.Optional;
import com.google.common.collect.PeekingIterator;

public abstract class SimpleIterator<T> implements PeekingIterator<T>
{
	protected enum State
	{
		NOT_READY, READY, DONE;
	}
	
	private boolean iterNull = true;
	private T current = null;
	private State state = NOT_READY;
	
	public SimpleIterator()
	{
	}
	
	public SimpleIterator(boolean iterNull)
	{
		this.iterNull = iterNull;
	}
	
	protected abstract T computeNext();
	
	protected final T prepareNext()
	{
		if (state == NOT_READY)
			current = computeNext();
		if (!iterNull && current == null)
			setDone();
		if (state != DONE)
			state = READY;
		return current;
	}
	
	protected final T getCurrent() { return current; }
	
	protected final void setDone() { state = DONE; }
	
	@Override public boolean hasNext()
	{
		prepareNext();
		return state != DONE;
	}
	@Override public T next()
	{
		if (!hasNext())
			throw new NoSuchElementException();
		state = NOT_READY;
		return current;
	}
	@Override public T peek() { return prepareNext(); }
}
