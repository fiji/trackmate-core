package fiji.plugin.trackmate.util;

import java.util.Iterator;

import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.Positionable;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.outofbounds.OutOfBoundsPeriodicFactory;

/**
 * A {@link Positionable} {@link IterableInterval} that serves as a local
 * neighborhood, e.g. in filtering operation.
 * <p>
 * This particular class implements a movable nD rectangle, defined by a
 * <code>span long[]</code> array. The <code>span</code> array is such that the
 * size of the rectangle in dimension <code>d</code> will be
 * <code>2 x span[d] + 1</code>. {@link Cursor}s can be instantiated from this
 * neighborhood, that will iterate through the rectangle in raster order.
 */
public class RectangleNeighborhood< T > extends AbstractNeighborhood< T >
{

	/*
	 * CONSTRUCTOR
	 */

	/**
	 * Instantiate a new rectangular neighborhood, on the given image, with the
	 * given factory to return out of bounds values.
	 * <p>
	 * The rectangle is initiated centered on the first pixel of the source, and
	 * span a single pixel.
	 */
	public RectangleNeighborhood( final int numDims,
			final OutOfBoundsFactory< T, RandomAccessibleInterval< T > > outOfBounds )
	{
		super( numDims, outOfBounds );
	}

	/**
	 * Instantiate a new rectangular neighborhood, on the given image, with the
	 * given factory to return out of bounds values.
	 * <p>
	 * The rectangle is initiated centered on the first pixel of the source, and
	 * span a single pixel.
	 */
	public RectangleNeighborhood( final RandomAccessibleInterval< T > source,
			final OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds) {
		super(source.numDimensions(), outOfBounds);
		updateSource(source);
	}

	/**
	 * Instantiate a rectangular neighborhood, with a
	 * {@link OutOfBoundsPeriodicFactory}
	 *
	 * @param source
	 */
	public RectangleNeighborhood( final RandomAccessibleInterval< T > source )
	{
		this(source.numDimensions(), new OutOfBoundsPeriodicFactory<T, RandomAccessibleInterval<T>>());
		updateSource(source);
	}

	/*
	 * SPECIFIC METHODS
	 */

	/**
	 * @return <b>the</b> cursor over this neighborhood.
	 */
	@Override
	public Cursor< T > cursor()
	{
		final RectangleCursor< T > cursor = new RectangleCursor< T >( this );
		cursor.reset();
		return cursor;
	}

	/**
	 * @return <b>the</b> cursor over this neighborhood.
	 */
	@Override
	public Cursor< T > localizingCursor()
	{
		return cursor();
	}

	/**
	 * @return <b>the</b> cursor over this neighborhood.
	 */
	@Override
	public Iterator< T > iterator()
	{
		return cursor();
	}

	@Override
	public long size()
	{
		long size = 1;
		for ( int d = 0; d < span.length; d++ )
		{
			size *= ( 2 * span[ d ] + 1 );
		}
		return size;
	}

	@Override
	public AbstractNeighborhood< T > copy()
	{
		if ( source != null )
			return new RectangleNeighborhood< T >( source, outOfBounds );
		return new RectangleNeighborhood< T >( n, outOfBounds );
	}

}
