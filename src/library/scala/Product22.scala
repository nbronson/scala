
/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2002-2006, LAMP/EPFL             **
**  __\ \/ /__/ __ |/ /__/ __ |                                         **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */

// generated on Mon Nov 27 15:01:28 CET 2006
package scala

import Predef._

/** Product22 is a cartesian product of 22 components
 */
trait Product22 [+T1, +T2, +T3, +T4, +T5, +T6, +T7, +T8, +T9, +T10, +T11, +T12, +T13, +T14, +T15, +T16, +T17, +T18, +T19, +T20, +T21, +T22] extends Product {

  /**
   *  The arity of this product.
   *  @return 22
   */
  override def arity = 22

  /**
   *  Returns the n-th projection of this product if 0<n<=arity, otherwise null
   *  @param n number of the projection to be returned
   *  @throws IndexOutOfBoundsException
   */
  override def element(n: Int) = n match {
    case 1 => _1
    case 2 => _2
    case 3 => _3
    case 4 => _4
    case 5 => _5
    case 6 => _6
    case 7 => _7
    case 8 => _8
    case 9 => _9
    case 10 => _10
    case 11 => _11
    case 12 => _12
    case 13 => _13
    case 14 => _14
    case 15 => _15
    case 16 => _16
    case 17 => _17
    case 18 => _18
    case 19 => _19
    case 20 => _20
    case 21 => _21
    case 22 => _22
    case _ => throw new IndexOutOfBoundsException(n.toString())
  }

  /** projection of this product */
  def _1:T1

  /** projection of this product */
  def _2:T2

  /** projection of this product */
  def _3:T3

  /** projection of this product */
  def _4:T4

  /** projection of this product */
  def _5:T5

  /** projection of this product */
  def _6:T6

  /** projection of this product */
  def _7:T7

  /** projection of this product */
  def _8:T8

  /** projection of this product */
  def _9:T9

  /** projection of this product */
  def _10:T10

  /** projection of this product */
  def _11:T11

  /** projection of this product */
  def _12:T12

  /** projection of this product */
  def _13:T13

  /** projection of this product */
  def _14:T14

  /** projection of this product */
  def _15:T15

  /** projection of this product */
  def _16:T16

  /** projection of this product */
  def _17:T17

  /** projection of this product */
  def _18:T18

  /** projection of this product */
  def _19:T19

  /** projection of this product */
  def _20:T20

  /** projection of this product */
  def _21:T21

  /** projection of this product */
  def _22:T22


}
