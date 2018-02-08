/*
 * Protect.java
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

/**
 * Protect marks a file as not-readable, preventing the file from being loaded
 * into an editor.
 *
 * <p>
 * From Flash 4, a password field was added. In order to load a file in
 * Macromedia's flash editor tool a password must be entered and the MD5 hash
 * must match the value stored in the password field.
 * </p>
 *
 * <p>
 * IMPORTANT: this form of protection only works with Macromedia's Flash
 * Authoring tool. Any application that parses Flash files can choose to ignore
 * or delete this data structure therefore it is not safe to use this to protect
 * the contents of a Flash file.
 * </p>
 *
 * <p>
 * Transform will parse all Flash files containing the Protect data structure.
 * Since the encoded data is can be removed by trivial scripts the level of
 * copy-protection offered is minimal. Indeed the use of the Protect mechanism
 * in Flash movies may lead to a false sense of security, putting proprietary
 * information at risk. Sensitive information should not be included in Flash
 * movies.
 * </p>
 */
public final class Protect implements MovieTag {

    /** Format string used in toString() method. */
    private static final String FORMAT = "Protect: { password=%s}";

    /** The MD5 encoded password. */
    private String password;

    /** The length of the object, minus the header, when it is encoded. */
    private transient int length;

    /**
     * Creates and initialises a Protect object using values encoded
     * in the Flash binary format.
     *
     * @param coder
     *            an SWFDecoder object that contains the encoded Flash data.
     *
     * @throws IOException
     *             if an error occurs while decoding the data.
     */
    public Protect(final SWFDecoder coder) throws IOException {
        length = coder.readUnsignedShort() & Coder.LENGTH_FIELD;
        if (length == Coder.IS_EXTENDED) {
            length = coder.readInt();
        }
        coder.mark();
        /*
         * Force a read of the entire password field, including any zero bytes
         * that are encountered.
         */
        if (length > 0) {
            coder.readUnsignedShort();
            password = coder.readString(length - 2);

            while (password.charAt(password.length() - 1) == 0) {
                password = password.substring(0, password.length() - 1);
            }
        }
        coder.check(length);
        coder.unmark();
    }

    /**
     * Creates a Protect object with no password - Flash versions 1 to 3 only.
      */
    public Protect() {
        // password remains null
    }

    /**
     * Creates a Protect object with the specified password - used for file with
     * Flash version 4 and above.
     *
     * @param pass
     *            the string defining the password. Must not be null.
     */
    public Protect(final String pass) {
        setPassword(pass);
    }

    /**
     * Creates and initialises a Protect object using the password copied
     * from another Protect object.
     *
     * @param object
     *            a Protect object from which the password will be
     *            copied.
     */
    public Protect(final Protect object) {
        password = object.password;
    }

    /**
     * Get the MD5 password hash. This may be null if the object was
     * decoded from a file containing Flash version 2 or 3.
     *
     * @return the MD5 hash of the password used to protect the file.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the MD5 encrypted password.
     *
     * @param aString
     *            the string defining the password. Must not be null.
     */
    public void setPassword(final String aString) {
        if (aString == null) {
            throw new IllegalArgumentException();
        }
        password = aString;
    }

    /** {@inheritDoc} */
    public Protect copy() {
        return new Protect(this);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return String.format(FORMAT, password);
    }

    /** {@inheritDoc} */
    public int prepareToEncode(final Context context) {
        length = 0;

        if (password != null) {
            length += 2 + context.strlen(password);
        }
        return (length > Coder.HEADER_LIMIT ? Coder.LONG_HEADER
                : Coder.SHORT_HEADER) + length;
    }

    /** {@inheritDoc} */
    public void encode(final SWFEncoder coder, final Context context)
            throws IOException {
        if (length > Coder.HEADER_LIMIT) {
            coder.writeShort((MovieTypes.PROTECT
                    << Coder.LENGTH_FIELD_SIZE) | Coder.IS_EXTENDED);
            coder.writeInt(length);
        } else {
            coder.writeShort((MovieTypes.PROTECT
                    << Coder.LENGTH_FIELD_SIZE) | length);
        }
        if (password != null) {
            coder.writeShort(0);
            coder.writeString(password);
        }
    }
}
