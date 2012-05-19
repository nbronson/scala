/* scala-stm - (c) 2009-2011, Stanford University, PPL */


import scala.concurrent.stm._
import scala.concurrent.stm.skel._
import scala.concurrent.stm.japi._
import scala.concurrent.stm.impl._
import skel.SimpleRandom
import java.util.concurrent.atomic.AtomicInteger

/** Verifies that blocking STM operations can be interrupted. */
object Test {

  def intercept[X](block: => Unit)(implicit xm: ClassManifest[X]) {
    try {
      block
      assert(false, "expected " + xm.erasure)
    } catch {
      case x if (xm.erasure.isAssignableFrom(x.getClass)) => // okay
    }
  }

  def main(args: Array[String]) {


    test("txn retry arriving interrupt") {
      delayedInterrupt(100)
      val x = Ref(0)
      intercept[InterruptedException] {
        atomic { implicit txn =>
          if (x() == 0) retry
        }
      }
    }

    test("txn retry pending interrupt") {
      Thread.currentThread.interrupt()
      val x = Ref(0)
      intercept[InterruptedException] {
        atomic { implicit txn =>
          if (x() == 0) retry
        }
      }
    }

    test("single await arriving interrupt") {
      delayedInterrupt(100)
      val x = Ref(0)
      intercept[InterruptedException] {
        x.single.await( _ != 0 )
      }
    }

    test("single await pending interrupt") {
      Thread.currentThread.interrupt()
      val x = Ref(0)
      intercept[InterruptedException] {
        x.single.await( _ != 0 )
      }
    }

    test("random interrupts during contention") {
      val refs = Array.tabulate(100)( _ => Ref(0) )
      val txnInterrupts = new AtomicInteger
      val nonTxnInterrupts = new AtomicInteger
      var failure = null : Throwable
      lazy val threads: Array[Thread] = Array.tabulate(10)( _ => new Thread {
        override def run() {
          try {
            for (i <- 0 until 10000) {
              try {
                atomic { implicit txn =>
                  for (r <- refs) r() = r() + 1
                }
              } catch {
                case x: InterruptedException => txnInterrupts.incrementAndGet
              }
              for (r <- refs) {
                try {
                  r.single += 1
                } catch {
                  case x: InterruptedException => nonTxnInterrupts.incrementAndGet
                }
              }
              threads(SimpleRandom.nextInt(threads.length)).interrupt()
            }
          } catch {
            case x => failure = x
          }
        }
      })
      for (t <- threads) t.start
      for (t <- threads) t.join
      if (failure != null)
        throw failure
      if (false) println(txnInterrupts.get + " txn rollbacks, " + nonTxnInterrupts.get + " non-txn interrupts")
    }
  }

  //////// machinery for InterruptSuite

  val pendingInterrupts = new ThreadLocal[List[Thread]] { override def initialValue = Nil }

  def test(name: String)(block: => Unit) {
    println("running retry " + name)
    var failure = null : Throwable
    val t = new Thread {
      override def run {
        try {
          block
        } catch {
          case x => failure = x
        } finally {
          while (!pendingInterrupts.get.isEmpty) {
            try {
              pendingInterrupts.get.head.join
              pendingInterrupts.set(pendingInterrupts.get.tail)
            } catch {
              case _ =>
            }
          }
          Thread.interrupted
        }
      }
    }
    t.start
    t.join
    if (failure != null)
      throw failure
  }

  def delayedInterrupt(delay: Long) { delayedInterrupt(Thread.currentThread, delay) }

  def delayedInterrupt(target: Thread, delay: Long) {
    val t = new Thread {
      override def run {
        Thread.sleep(delay)
        target.interrupt()
      }
    }
    pendingInterrupts.set(t :: pendingInterrupts.get)
    t.start
  }
}
