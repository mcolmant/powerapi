/*
 * This software is licensed under the GNU Affero General Public License, quoted below.
 *
 * This file is a part of PowerAPI.
 *
 * Copyright (C) 2011-2016 Inria, University of Lille 1.
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
package org.powerapi.module.cpu.dvfs

import scala.concurrent.duration.DurationInt

import akka.util.Timeout

import org.powerapi.UnitTest
import org.powerapi.core.{LinuxHelper, OSHelper}
import org.scalamock.scalatest.MockFactory

class CpuDvfsModulesSuite extends UnitTest with MockFactory {
  val timeout = Timeout(1.seconds)

  override def afterAll() = {
    system.terminate()
  }

  "The CpuDvfsModule class" should "create the underlying classes (sensor/formul)" in {
    val osHelper = mock[OSHelper]

    val module = new CpuDvfsModule(osHelper, 10, 0.5, Map(1 -> 10))
    module.sensor.get._1 should equal(classOf[CpuDvfsSensor])
    module.sensor.get._2.size should equal(1)
    module.sensor.get._2(0) should equal(osHelper)

    module.formula.get._1 should equal(classOf[CpuDvfsFormula])
    module.formula.get._2.size should equal(3)
    module.formula.get._2(0) should equal(10)
    module.formula.get._2(1) should equal(0.5)
    module.formula.get._2(2) should equal(Map(1 -> 10))
  }

  "The CpuDvfsModule object" should "build correctly the companion class" in {
    val module = CpuDvfsModule()

    module.sensor.get._1 should equal(classOf[CpuDvfsSensor])
    module.sensor.get._2.size should equal(1)
    module.sensor.get._2(0).getClass should equal(classOf[LinuxHelper])
    module.sensor.get._2(0).asInstanceOf[LinuxHelper].frequenciesPath should equal("p1/%?core")
    module.sensor.get._2(0).asInstanceOf[LinuxHelper].taskPath should equal("p2/%?pid")
    module.sensor.get._2(0).asInstanceOf[LinuxHelper].globalStatPath should equal("p3")
    module.sensor.get._2(0).asInstanceOf[LinuxHelper].processStatPath should equal("p4/%?pid")
    module.sensor.get._2(0).asInstanceOf[LinuxHelper].timeInStatePath should equal("p5")
    module.sensor.get._2(0).asInstanceOf[LinuxHelper].topology should equal(Map(0 -> Set(0, 4), 1 -> Set(1, 5), 2 -> Set(2, 6), 3 -> Set(3, 7)))

    module.formula.get._1 should equal(classOf[CpuDvfsFormula])
    module.formula.get._2.size should equal(3)
    module.formula.get._2(0) should equal(120)
    module.formula.get._2(1) should equal(0.80)
    module.formula.get._2(2) should equal(Map(1800002 -> 1.31, 2100002 -> 1.41, 2400003 -> 1.5))
  }
}
