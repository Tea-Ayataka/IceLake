/*
 * TabOrder.java
 * Transform
 *
 * Copyright (c) 2001-2010 Flagstone Software Ltd. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *  * Neither the name of Flagstone Software Ltd. nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.flagstone.transform;

import java.io.IOException;

import com.flagstone.transform.coder.Coder;
import com.flagstone.transform.coder.Context;
import com.flagstone.transform.coder.SWFDecoder;
import com.flagstone.transform.coder.SWFEncoder;
import com.flagstone.transform.exception.IllegalArgumentRangeException;

/**
 * The TabOrder class is used to set the tabbing order of text fields, movie
 * clips and buttons visible on the display list.
 *
 * <p>
 * The objects are referenced by the number of the layer on which they displayed
 * rather than the unique identifier. This differs from the other classes in the
 * framework but it does allow objects creating at run-time by ActionScript
 * statements to be referenced.
 * </p>
 */
public final class TabOrder implements MovieTag {

    /** The highest index when defining an objects tab order. */
    public static final int MAX_TAB = 65535;

    /** Format string used in toString() method. */
    private static final String FORMAT = "TabOrder: { layer=%d; index=%d}";
    /** The layer on which the object is displayed. */
    private int layer;
    /** The order in which the object will received keyboard focus. */
    private int index;

    /**
     * Creates and initialises a TabOrder object using values encoded
     * in the Flash binary format.
     *
     * @param coder
     *            an SWFDecoder object that contains the encoded Flash data.
     *
     * @throws IOException
     *             if an error occurs while decoding the data.
     */
    public TabOrder(final SWFDecoder coder) throws IOException {
        if ((coder.readUnsignedShort() & Coder.LENGTH_FIELD)
        		== Coder.IS_EXTENDED) {
            coder.readInt();
        }
        layer = coder.readUnsignedShort();
        index = coder.readUnsignedShort();
    }

    /**
     * Construct a TabOrder object that set the tab order for the object on the
     * display list at the specified layer.
     *
     * @param level
     *            the layer number which contains the object assigned to the
     *            tabbing order. Must be in the range 1..65535.
     * @param idx
     *            the index of the object in the tabbing order. Must be in the
     *            range 0..65535.
     */
    public TabOrder(final int level, final int idx) {
        setLayer(level);
        setIndex(idx);
    }

    /**
     * Creates and initialises a TabOrder object using the values copied
     * from another TabOrder object.
     *
     * @param object
     *            a TabOrder object from which the values will be
     *            copied.
     */
    public TabOrder(final TabOrder object) {
        layer = object.layer;
        index = object.index;
    }

    /**
     * Get the layer number which contains the object assigned to the
     * tabbing order.
     * @return the layer number.
     */
    public int getLayer() {
        return layer;
    }

    /**
     * Sets the layer number which contains the object assigned to the tabbing
     * order.
     *
     * @param level
     *            the layer number. Must be in the range 1..65535.
     */
    public void setLayer(final int level) {
        if ((level < 1) || (level > Coder.USHORT_MAX)) {
            throw new IllegalArgumentRangeException(
                    1, Coder.USHORT_MAX, level);
        }
        layer = level;
    }

    /**
     * Get the index of the object in the tabbing order.
     * @return the order in which the object will get keyboard focus.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the index of the object in the tabbing order.
     *
     * @param idx
     *            the index in the tabbing order. Must be in the range 0..65535.
     */
    public void setIndex(final int idx) {
        if ((idx < 0) || (idx > MAX_TAB)) {
            throw new IllegalArgumentRangeException(0, MAX_TAB, idx);
        }
        index = idx;
    }

    /** {@inheritDoc} */
    @Override
	public TabOrder copy() {
        return new TabOrder(this);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return String.format(FORMAT, layer, index);
    }

    /** {@inheritDoc} */
    @Override
	public int prepareToEncode(final Context context) {
        // CHECKSTYLE IGNORE MagicNumberCheck FOR NEXT 1 LINES
        return 6;
    }

    /** {@inheritDoc} */
    @Override
	public void encode(final SWFEncoder coder, final Context context)
            throws IOException {
        // CHECKSTYLE IGNORE MagicNumberCheck FOR NEXT 1 LINES
        coder.writeShort((MovieTypes.TAB_ORDER << Coder.LENGTH_FIELD_SIZE) | 4);
        coder.writeShort(layer);
        coder.writeShort(index);
    }
}
