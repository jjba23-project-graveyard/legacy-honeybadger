// Copyright © 2023 Josep Bigorra
//
// This file is part of HoneyBadger.
// HoneyBadger is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation,
//   either version 3 of the License, or (at your option) any later version.
//
// HoneyBadger is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
//   without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
//
// See the GNU General Public License for more details.
// You should have received a copy of the GNU General Public License along with HoneyBadger.
// If not, see <https://www.gnu.org/licenses/>.

object Time {
  def now: Long = System.nanoTime()

  def timeElapsedFromThenTillNow(thenNanoTime: Long): Double = {
    val now = System.nanoTime()
    val elapsed = (now - thenNanoTime) / 1000000000.0
    elapsed
  }
}
