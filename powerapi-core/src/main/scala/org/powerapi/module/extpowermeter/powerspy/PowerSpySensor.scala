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
package org.powerapi.module.extpowermeter.powerspy

import java.util.UUID

import org.powerapi.core.power.Power
import org.powerapi.core.target.Target
import org.powerapi.core.{ExternalPMeter, MessageBus, OSHelper}
import org.powerapi.module.extpowermeter.ExtPowerMeterChannel.{publishPowerSpyPowerReport, subscribePowerSpyRawPowerReport, unsubscribePowerSpyRawPowerReport}
import org.powerapi.module.extpowermeter.ExtPowerMeterSensor

/**
  * PowerSpy's sensor.
  *
  * @author <a href="mailto:maxime.colmant@gmail.com">Maxime Colmant</a>
  */
class PowerSpySensor(eventBus: MessageBus, muid: UUID, target: Target, osHelper: OSHelper, pMeter: ExternalPMeter, idlePower: Power)
  extends ExtPowerMeterSensor(eventBus, muid, target,
    subscribePowerSpyRawPowerReport, unsubscribePowerSpyRawPowerReport, publishPowerSpyPowerReport,
    osHelper, pMeter, idlePower)
