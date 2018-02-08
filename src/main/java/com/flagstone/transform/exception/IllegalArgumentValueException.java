/*
 * IllegalArgumentValueException.java
 * Transform
 *
 * Copyright (c) 2009-2010 Flagstone Software Ltd. All rights reserved.
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

package com.flagstone.transform.exception;

import java.util.Arrays;

/**
 * IllegalArgumentValueException is thrown when a value is used that is not
 * a member of the expected set.
 */
public final class IllegalArgumentValueException
            extends IllegalArgumentException {

    /** Serial number identifying the version of the object. */
    private static final long serialVersionUID = 3748031731035981638L;
    /** The set of expected values. */
    private final transient int[] expected;
    /** The actual value used. */
    private final transient int actual;

    /**
     * Creates an IllegalArgumentValueException with the set of expected values
     * and the actual value used.
     *
     * @param set the set of expected values.
     * @param value the actual value used.
     */
    public IllegalArgumentValueException(final int[] set, final int value) {
        super("Valid values: " + Arrays.toString(set) + " Value: " + value);
        expected = Arrays.copyOf(set, set.length);
        actual = value;
    }
    /**
     * Get the set of expected values.
     *
     * @return a copy of the expected values.
     */
    public int[] getExpected() {
        return Arrays.copyOf(expected, expected.length);
    }
    /**
     * Get the actual value that triggered the exception.
     * @return the actual value used.
     */
    public int getActual() {
        return actual;
    }
}
