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

case class HoneyBadgerBadgeParameters(
                                       textContents: String,
                                       textColor: String = TextCatppuccinMochaColor.slug,
                                       backgroundColor: String = MantleCatppuccinMochaColor.slug,
                                       width: Double = 120,
                                       height: Double = 28,
                                       fontSize: Int = 13,
                                       backgroundColorBrightnessModifier: Option[Int] = None,
                                       customFontFilePath: Option[String] = None,
                                       iconParameters: Option[HoneyBadgerIconParameters] = None,
                                       arcWidth: Double = 10,
                                       arcHeight: Double = 10
                                     )


case class HoneyBadgerIconParameters(
                                      width: Int = 20,
                                      height: Int = 20,
                                      filePath: String
                                    )


case class HoneyBadgerSingleTask(
                                  badgeParameters: HoneyBadgerBadgeParameters,
                                  destinationFilePath: String = "./result.png",
                                  destinationFileExtension: String = "png",
                                )


case class HoneyBadgerLeftRightTask(
                                     leftBadgeParameters: HoneyBadgerBadgeParameters,
                                     rightBadgeParameters: HoneyBadgerBadgeParameters,
                                     destinationFilePath: String = "./result.png",
                                     destinationFileExtension: String = "png",
                                   )


case class HoneyBadgerUserInputTasks(
                                      leftRightBadgeTasks: Option[List[HoneyBadgerLeftRightTask]],
                                      singleBadgeTasks: Option[List[HoneyBadgerSingleTask]]
                                    )