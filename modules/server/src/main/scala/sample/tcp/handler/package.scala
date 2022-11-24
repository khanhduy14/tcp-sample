package sample.tcp

import monix.execution.ExecutionModel.AlwaysAsyncExecution
import monix.execution.Scheduler
import monix.execution.schedulers.TracingScheduler

package object handler {
  implicit val IOScheduler: Scheduler =
    TracingScheduler(Scheduler.io(name = "IO-Bounded", daemonic = false, executionModel = AlwaysAsyncExecution))
}
