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
// You should have received a copy of the GNU General Public License along with Foobar.
// If not, see <https://www.gnu.org/licenses/>.

// You might use HoneyBadger programatically like this, for example
object Main extends App {
  private val singleBadgeTasks = List[HoneyBadgerSingleTask](
    HoneyBadgerSingleTask(
      destinationFilePath = "./dist/haskell.png",
      badgeParameters = HoneyBadgerBadgeParameters(
        textContents = "HASKELL",
        customFontFilePath = Some("./resources/fonts/SofiaSans-Black.ttf"),
        iconParameters = Some(HoneyBadgerIconParameters(filePath = "./resources/icons/haskell.svg")),
        backgroundColor = MauveCatppuccinLatteColor.slug,
        textColor = TextCatppuccinMochaColor.slug,
        backgroundColorBrightnessModifier = Some(-20)
      )
    ),
    HoneyBadgerSingleTask(
      destinationFilePath = "./dist/python.png",
      badgeParameters = HoneyBadgerBadgeParameters(
        textContents = "PYTHON",
        customFontFilePath = Some("./resources/fonts/SofiaSans-Black.ttf"),
        iconParameters = Some(HoneyBadgerIconParameters(filePath = "./resources/icons/python.svg")),
        backgroundColor = BlueCatppuccinLatteColor.slug,
        textColor = TextCatppuccinMochaColor.slug,
        backgroundColorBrightnessModifier = Some(-10)
      )
    )
  )

  private val leftRightBadgeTasks = List[HoneyBadgerLeftRightTask](
    HoneyBadgerLeftRightTask(
      destinationFilePath = "./dist/scala.png",
      leftBadgeParameters = HoneyBadgerBadgeParameters(
        textContents = "SCALA",
        customFontFilePath = Some("./resources/fonts/SofiaSans-Black.ttf"),
        iconParameters = Some(HoneyBadgerIconParameters(filePath = "./resources/icons/scala.svg", size = 16)),
        backgroundColor = MantleCatppuccinMochaColor.slug,
        textColor = TextCatppuccinMochaColor.slug,
        width = 76
      ),
      rightBadgeParameters = HoneyBadgerBadgeParameters(
        textContents = "v2.13.8",
        customFontFilePath = Some("./resources/fonts/SofiaSans-Black.ttf"),
        backgroundColor = RedCatppuccinLatteColor.slug,
        textColor = TextCatppuccinMochaColor.slug,
        width = 50,
        fontSize = 10
      )
    )
  )

  println("HoneyBadger is the one and only Scala CLI badge generator!")
  println(s"Received ${singleBadgeTasks.length} single-part badge tasks! These will be processed concurrently!")
  HoneyBadgerTaskProcessor.singleBadgeToDiskProcessor(singleBadgeTasks)

  println(s"Received ${leftRightBadgeTasks.length} left-right badge tasks! These will be processed concurrently!")
  HoneyBadgerTaskProcessor.leftRightBadgeToDiskProcessor(leftRightBadgeTasks)
}
