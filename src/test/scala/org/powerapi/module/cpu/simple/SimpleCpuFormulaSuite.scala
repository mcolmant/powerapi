/*
 * This software is licensed under the GNU Affero General Public License, quoted below.
 *
 * This file is a part of PowerAPI.
 *
 * Copyright (C) 2011-2014 Inria, University of Lille 1.
 *
 * PowerAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * PowerAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with PowerAPI.
 *
 * If not, please consult http://www.gnu.org/licenses/agpl-3.0.html.
 */
package org.powerapi.module.cpu.simple

import java.util.UUID
import akka.actor.{Props, ActorSystem}
import akka.testkit.{TestActorRef, TestKit}
import akka.util.Timeout
import org.powerapi.UnitTest
import org.powerapi.core.MessageBus
import scala.concurrent.duration.DurationInt

class SimpleCpuFormulaMock(messageBus: MessageBus) extends CpuFormula(messageBus) {
  override lazy val tdp = 220
  override lazy val tdpFactor = 0.7
}

class SimpleCpuFormulaSuite(system: ActorSystem) extends UnitTest(system) {

  implicit val timeout = Timeout(1.seconds)

  def this() = this(ActorSystem("SimpleCpuFormulaSuite"))

  override def afterAll() = {
    TestKit.shutdownActorSystem(system)
  }

  val eventBus = new MessageBus
  val formulaMock = TestActorRef(Props(classOf[SimpleCpuFormulaMock], eventBus), "simple-cpuFormula")(system)

  "A simple cpu formula" should "process a SensorReport and then publish a PowerReport" in {
    import org.powerapi.core.ClockChannel.ClockTick
    import org.powerapi.core.target.{intToProcess, Target, TargetUsageRatio}
    import org.powerapi.module.PowerChannel.{PowerReport, subscribePowerReport}
    import org.powerapi.module.cpu.UsageMetricsChannel.publishUsageReport
    import org.powerapi.module.PowerUnit

    val muid = UUID.randomUUID()
    val target: Target = 1
    val targetRatio = TargetUsageRatio(0.4)
    val tickMock = ClockTick("test", 25.milliseconds)
    val power = 220 * 0.7 * targetRatio.ratio

    subscribePowerReport(muid)(eventBus)(testActor)
    publishUsageReport(muid, target, targetRatio, tickMock)(eventBus)

    expectMsgClass(classOf[PowerReport]) match {
      case PowerReport(_, id, targ, pow, PowerUnit.W, "cpu", tic) => id should equal(muid); targ should equal(target); pow should equal(power); tic should equal(tickMock)
    }
  }
}
