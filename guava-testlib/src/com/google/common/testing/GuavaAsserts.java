/*
 * Copyright (C) 2010 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.testing;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;

/**
 * Contains additional assertion methods not found in JUnit.
 *
 * @author Kevin Bourillion
 * @since Guava release 10
 */
@Beta
@GwtCompatible
public final class GuavaAsserts {
  private GuavaAsserts() { }

  /**
   * Utility for testing equals() and hashCode() results at once.
   * Tests that lhs.equals(rhs) matches expectedResult, as well as
   * rhs.equals(lhs).  Also tests that hashCode() return values are
   * equal if expectedResult is true.  (hashCode() is not tested if
   * expectedResult is false, as unequal objects can have equal hashCodes.)
   *
   * @param lhs An Object for which equals() and hashCode() are to be tested.
   * @param rhs As lhs.
   * @param expectedResult True if the objects should compare equal,
   *   false if not.
   */
  // TODO(kevinb): sort out why both this and EqualsTester exist
  public static void checkEqualsAndHashCodeMethods(
      String message, Object lhs, Object rhs, boolean expectedResult) {

    if ((lhs == null) && (rhs == null)) {
      // TODO(cpovirk): just fail()?
      assertTrue(
          "Your check is dubious...why would you expect null != null?",
          expectedResult);
      return;
    }

    if ((lhs == null) || (rhs == null)) {
      assertTrue(
          "Your check is dubious...why would you expect an object "
          + "to be equal to null?", !expectedResult);
    }

    if (lhs != null) {
      assertEqualsImpl(message, expectedResult, lhs.equals(rhs));
    }
    if (rhs != null) {
      assertEqualsImpl(message, expectedResult, rhs.equals(lhs));
    }

    if (expectedResult) {
      String hashMessage =
          "hashCode() values for equal objects should be the same";
      if (message != null) {
        hashMessage += ": " + message;
      }
      assertTrue(hashMessage, lhs.hashCode() == rhs.hashCode());
    }
  }

  /**
   * Replacement of {@link GuavaAsserts#assertEquals} which provides the same error
   * message in GWT and java.
   */
  private static void assertEqualsImpl(
      String message, Object expected, Object actual) {
    if (!Objects.equal(expected, actual)) {
      failWithMessage(
          message, "expected:<" + expected + "> but was:<" + actual + ">");
    }
  }

  private static void failWithMessage(String userMessage, String ourMessage) {
    fail((userMessage == null)
        ? ourMessage
        : userMessage + ' ' + ourMessage);
  }

  /**
   * Fail with an RuntimeException.
   *
   * @throws RuntimeException always
   */
  public static void fail() {
    throw new TestAssertionFailure();
  }

  /**
   * Fail with an RuntimeException and a message.
   *
   * @throws RuntimeException always
   */
  public static void fail(String message) {
    throw new TestAssertionFailure(message);
  }

  /**
   * Test the condition and throw a failure exception if false with
   * a stock message.
   */
  public static void assertTrue(boolean condition) {
    if (!condition) {
      fail("Condition expected to be true but was false.");
    }
  }

  /**
   * Test the condition and throw a failure exception if false with
   * a stock message.
   */
  public static void assertTrue(String message, boolean condition) {
    if (!condition) {
      fail(message);
    }
  }

  /**
   * Assert the equality of two objects
   */
  public static void assertEquals(Object expected, Object actual) {
    // String.format is not GwtCompatible.
    assertEquals("Expected '"+ expected +"' but got '" + actual + "'",
        expected, actual);
  }

  /**
   * Assert the equality of two objects
   */
  public static void assertEquals(String message, Object o1, Object o2) {
    if (o1 == null) {
      assertTrue(message, o2 == null);
      return;
    }
    assertTrue(message, o1.equals(o2));
  }
  
  /**
   * An error thrown when test assertions are shown to be invalid.
   * 
   * @author cgruber@google.com (Christian Gruber)
   */
  public static class TestAssertionFailure extends AssertionError {

    public TestAssertionFailure() {
      super();
    }

    public TestAssertionFailure(String errorMessage) {
      super(errorMessage);
    }
    
  }
}
