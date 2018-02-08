/*
 * DefineJPEGImage.java
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
package com.flagstone.transform.image;

import java.io.IOException;
import java.util.Arrays;

import com.flagstone.transform.Constants;
import com.flagstone.transform.MovieTypes;
import com.flagstone.transform.coder.Coder;
import com.flagstone.transform.coder.Context;
import com.flagstone.transform.coder.SWFDecoder;
import com.flagstone.transform.coder.SWFEncoder;
import com.flagstone.transform.exception.IllegalArgumentRangeException;

/**
 * DefineJPEGImage is used to define a JPEG encoded image.
 *
 * <p>
 * DefineJPEGImage objects only contain the image data, the encoding table for
 * the image is defined in a JPEGEncodingTable object. All images using a shared
 * JPEGEncodingTable object to represent the encoding table have the same
 * compression ratio.
 * </p>
 *
 * <p>
 * Although the DefineJPEGImage class is supposed to be used with the
 * JPEGEncodingTable class which defines the encoding table for the images it is
 * not essential. If an JPEGEncodingTable object is created with an empty
 * encoding table then the Flash Player will still display the JPEG image
 * correctly if the encoding table is included in the image data.
 * </p>
 *
 * @see JPEGEncodingTable
 * @see DefineJPEGImage2
 * @see DefineJPEGImage3
 */
public final class DefineJPEGImage implements ImageTag {

    /** Format string used in toString() method. */
    private static final String FORMAT = "DefineJPEGImage: { identifier=%d;"
            + " image=byte<%d> ...}";

    /** The unique identifier for this object. */
    private int identifier;
    /** The JPEG encoded image. */
    private byte[] image;

    /** The length of the object, minus the header, when it is encoded. */
    private transient int length;
    /** The width of the image in pixels. */
    private transient int width;
    /** The height of the image in pixels. */
    private transient int height;

    /**
     * Creates and initialises a DefineJPEGImage object using values encoded
     * in the Flash binary format.
     *
     * @param coder
     *            an SWFDecoder object that contains the encoded Flash data.
     *
     * @throws IOException
     *             if an error occurs while decoding the data.
     */
    public DefineJPEGImage(final SWFDecoder coder) throws IOException {
        length = coder.readUnsignedShort() & Coder.LENGTH_FIELD;
        if (length == Coder.IS_EXTENDED) {
            length = coder.readInt();
        }
        coder.mark();
        identifier = coder.readUnsignedShort();
        image = coder.readBytes(new byte[length - 2]);
        decodeInfo();
        coder.check(length);
        coder.unmark();
    }

    /**
     * Creates a DefineJPEGImage object with the identifier and JPEG data.
     *
     * @param uid
     *            the unique identifier for this object. Must be in the range
     *            1..65535.
     * @param bytes
     *            the JPEG encoded image data. Must not be null.
     */
    public DefineJPEGImage(final int uid, final byte[] bytes) {
        setIdentifier(uid);
        setImage(bytes);
    }

    /**
     * Creates and initialises a DefineJPEGImage object using the values copied
     * from another DefineJPEGImage object.
     *
     * @param object
     *            a DefineJPEGImage object from which the values will be
     *            copied.
     */
    public DefineJPEGImage(final DefineJPEGImage object) {
        identifier = object.identifier;
        width = object.width;
        height = object.height;
        image = object.image;
    }

    /** {@inheritDoc} */
    public int getIdentifier() {
        return identifier;
    }

    /** {@inheritDoc} */
    public void setIdentifier(final int uid) {
        if ((uid < 1) || (uid > Coder.USHORT_MAX)) {
            throw new IllegalArgumentRangeException(
                    1, Coder.USHORT_MAX, uid);
        }
        identifier = uid;
    }

    /**
     * Get the width of the image in pixels.
     *
     * @return the image width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the height of the image in pixels (not twips).
     *
     * @return the height of the image.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get a copy of the image.
     *
     * @return  a copy of the data.
     */
    public byte[] getImage() {
        return Arrays.copyOf(image, image.length);
    }

    /**
     * Sets the image data. The image data may be taken directly from a file
     * containing a JPEG encoded image. if the image contains an encoding table
     * the Flash Player will display it correctly and there is no need to
     * specify a separate table using a JPEGEncodingTable object.
     *
     * @param bytes
     *            a list of bytes containing the image data. Must not be null
     *            or empty.
     */
    public void setImage(final byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException();
        }
        image = Arrays.copyOf(bytes, bytes.length);
        decodeInfo();
    }

    /** {@inheritDoc} */
    public DefineJPEGImage copy() {
        return new DefineJPEGImage(this);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return String.format(FORMAT, identifier, image.length);
    }

    /** {@inheritDoc} */
    public int prepareToEncode(final Context context) {
        length = 2 + image.length;

        return (length > Coder.HEADER_LIMIT ? Coder.LONG_HEADER
                : Coder.SHORT_HEADER) + length;
    }

    /** {@inheritDoc} */
    public void encode(final SWFEncoder coder, final Context context)
            throws IOException {

        if (length > Coder.HEADER_LIMIT) {
            coder.writeShort((MovieTypes.DEFINE_JPEG_IMAGE
                    << Coder.LENGTH_FIELD_SIZE) | Coder.IS_EXTENDED);
            coder.writeInt(length);
        } else {
            coder.writeShort((MovieTypes.DEFINE_JPEG_IMAGE
                    << Coder.LENGTH_FIELD_SIZE) | length);
        }
        if (Constants.DEBUG) {
            coder.mark();
        }
        coder.writeShort(identifier);
        coder.writeBytes(image);
        if (Constants.DEBUG) {
            coder.check(length);
            coder.unmark();
        }
    }

    /**
     * Decode the JPEG image to get the width and height.
     */
    private void decodeInfo() {
        final JPEGInfo info = new JPEGInfo();
        info.decode(image);
        width = info.getWidth();
        height = info.getHeight();
    }
}
