package genesis.util;

import static genesis.util.SimpleIterator.State.DONE;
import static genesis.util.SimpleIterator.State.NOT_READY;
import static genesis.util.SimpleIterator.State.READY;

import java.util.NoSuchElementException;

import com.google.common.collect.PeekingIterator;

public abstract class SimpleIterator<T> implements PeekingIterator<T>
{
	protected enum State
	{
		NOT_READY, READY, DONE;
	}
	
	private T current = null;
	private State state = NOT_READY;
	
	protected abstract T computeNext();
	
	protected final T prepareNext()
	{
		if (state == NOT_READY)
			current = computeNext();
		if (state != DONE)
			state = READY;
		return current;
	}
	
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
