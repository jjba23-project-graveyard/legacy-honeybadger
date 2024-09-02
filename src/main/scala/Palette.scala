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

import java.awt.Color
import scala.util.matching.Regex

class HoneyBadgerColor {
  def toColor: Color = new Color(rgb._1, rgb._2, rgb._3)

  def rgb: (Int, Int, Int) = (0, 0, 0)
}

object HoneyBadgerColor {
  def changeBrightnessModifier(rgb: (Int, Int, Int), percentage: Int): HoneyBadgerColor = {
    val toChange = 255 * percentage / 100
    val newRgb = (rgb._1 + toChange, rgb._2 + toChange, rgb._3 + toChange)

    if (!isValidRgb(newRgb._1) || !isValidRgb(newRgb._2) || !isValidRgb(newRgb._3)) {
      scribe.error(s"Error! Cannot accept RGB values that are above 255 or below 0! Computed value would be: $newRgb! Defaulting to original color.")
      new HoneyBadgerColor {
        override def toColor: Color = new Color(rgb._1, rgb._2, rgb._3)
      }
    } else {
      new HoneyBadgerColor {
        override def toColor: Color = new Color(newRgb._1, newRgb._2, newRgb._3)
      }
    }
  }

  private def isValidRgb(value: Int): Boolean = value >= 0 && value <= 255
}

object HoneyBadgerPaletteUtils {
  private val slugToColorMap: Map[String, HoneyBadgerColor] = Map(
    // Catppuccin Mocha
    RosewaterCatppuccinMochaColor.slug -> RosewaterCatppuccinMochaColor,
    FlamingoCatppuccinMochaColor.slug -> FlamingoCatppuccinMochaColor,
    PinkCatppuccinMochaColor.slug -> PinkCatppuccinMochaColor,
    MauveCatppuccinMochaColor.slug -> MauveCatppuccinMochaColor,
    RedCatppuccinMochaColor.slug -> RedCatppuccinMochaColor,
    MaroonCatppuccinMochaColor.slug -> MaroonCatppuccinMochaColor,
    PeachCatppuccinMochaColor.slug -> PeachCatppuccinMochaColor,
    YellowCatppuccinMochaColor.slug -> YellowCatppuccinMochaColor,
    GreenCatppuccinMochaColor.slug -> GreenCatppuccinMochaColor,
    TealCatppuccinMochaColor.slug -> TealCatppuccinMochaColor,
    SkyCatppuccinMochaColor.slug -> SkyCatppuccinMochaColor,
    SapphireCatppuccinMochaColor.slug -> SapphireCatppuccinMochaColor,
    BlueCatppuccinMochaColor.slug -> BlueCatppuccinMochaColor,
    LavenderCatppuccinMochaColor.slug -> LavenderCatppuccinMochaColor,
    TextCatppuccinMochaColor.slug -> TextCatppuccinMochaColor,
    Subtext0CatppuccinMochaColor.slug -> Subtext0CatppuccinMochaColor,
    Subtext1CatppuccinMochaColor.slug -> Subtext1CatppuccinMochaColor,
    Overlay0CatppuccinMochaColor.slug -> Overlay0CatppuccinMochaColor,
    Overlay1CatppuccinMochaColor.slug -> Overlay1CatppuccinMochaColor,
    Overlay2CatppuccinMochaColor.slug -> Overlay2CatppuccinMochaColor,
    Surface0CatppuccinMochaColor.slug -> Surface0CatppuccinMochaColor,
    Surface1CatppuccinMochaColor.slug -> Surface1CatppuccinMochaColor,
    Surface2CatppuccinMochaColor.slug -> Surface2CatppuccinMochaColor,
    BaseCatppuccinMochaColor.slug -> BaseCatppuccinMochaColor,
    MantleCatppuccinMochaColor.slug -> MantleCatppuccinMochaColor,
    CrustCatppuccinMochaColor.slug -> CrustCatppuccinMochaColor,
    // Catppuccin Latte
    MauveCatppuccinLatteColor.slug -> MauveCatppuccinLatteColor,
    BlueCatppuccinLatteColor.slug -> BlueCatppuccinLatteColor,
    RedCatppuccinLatteColor.slug -> RedCatppuccinLatteColor,
    PeachCatppuccinLatteColor.slug -> PeachCatppuccinLatteColor
  )

  def parseColor(colorChoice: String): HoneyBadgerColor = {
    if (isHexCodeColor(colorChoice)) {
      val newColor = new HoneyBadgerColor {
        override def toColor: Color = Color.decode(colorChoice)
      }

      newColor
    } else {
      val optionNewColorBySlug = slugToColorMap.get(colorChoice)
      if (optionNewColorBySlug.isEmpty) {
        scribe.error(s"Error! The given color was not a valid hex code: $colorChoice ! Please give a correct hex e.g. #00000")
        new HoneyBadgerColor {}
      }else {
        optionNewColorBySlug.get
      }
    }
  }

  private def isHexCodeColor(hexInput: String): Boolean = {
    val hexPattern: Regex = "#[0-9a-fA-F]".r

    hexPattern.findFirstMatchIn(hexInput) match {
      case Some(_) => true
      case None =>
        false
    }
  }
}

object RosewaterCatppuccinMochaColor extends HoneyBadgerColor {
  override val rgb: (Int, Int, Int) = (245, 224, 220)
  val slug: String = "rosewater-catppuccin-mocha"
}

object FlamingoCatppuccinMochaColor extends HoneyBadgerColor {
  override val rgb: (Int, Int, Int) = (242, 205, 205)
  val slug: String = "flamingo-catppuccin-mocha"
}

object PinkCatppuccinMochaColor extends HoneyBadgerColor {
  override val rgb: (Int, Int, Int) = (245, 194, 231)
  val slug: String = "pink-catppuccin-mocha"
}

object MauveCatppuccinMochaColor extends HoneyBadgerColor {
  override val rgb: (Int, Int, Int) = (203, 166, 247)
  val slug: String = "mauve-catppuccin-mocha"
}

object RedCatppuccinMochaColor extends HoneyBadgerColor {
  override val rgb: (Int, Int, Int) = (243, 139, 168)
  val slug: String = "red-catppuccin-mocha"
}

object MaroonCatppuccinMochaColor extends HoneyBadgerColor {
  override val rgb: (Int, Int, Int) = (235, 160, 172)
  val slug: String = "maroon-catppuccin-mocha"
}

object PeachCatppuccinMochaColor extends HoneyBadgerColor {
  override val rgb: (Int, Int, Int) = (250, 179, 135)
  val slug: String = "peach-catppuccin-mocha"
}

object YellowCatppuccinMochaColor extends HoneyBadgerColor {
  override val rgb: (Int, Int, Int) = (249, 226, 175)
  val slug: String = "yellow-catppuccin-mocha"
}

object GreenCatppuccinMochaColor extends HoneyBadgerColor {
  override val rgb: (Int, Int, Int) = (166, 227, 161)
  val slug: String = "green-catppuccin-mocha"
}

object TealCatppuccinMochaColor extends HoneyBadgerColor {
  override val rgb: (Int, Int, Int) = (148, 226, 213)
  val slug: String = "teal-catppuccin-mocha"
}

object SkyCatppuccinMochaColor extends HoneyBadgerColor {
  override val rgb: (Int, Int, Int) = (137, 220, 235)
  val slug: String = "sky-catppuccin-mocha"
}

object SapphireCatppuccinMochaColor extends HoneyBadgerColor {
  override val rgb: (Int, Int, Int) = (116, 199, 236)
  val slug: String = "sapphire-catppuccin-mocha"
}

object BlueCatppuccinMochaColor extends HoneyBadgerColor {
  override val rgb: (Int, Int, Int) = (137, 180, 250)
  val slug: String = "blue-catppuccin-mocha"
}

object LavenderCatppuccinMochaColor extends HoneyBadgerColor {
  override val rgb: (Int, Int, Int) = (180, 190, 254)
  val slug: String = "lavender-catppuccin-mocha"
}

object TextCatppuccinMochaColor extends HoneyBadgerColor {
  override val rgb: (Int, Int, Int) = (205, 214, 244)
  val slug: String = "text-catppuccin-mocha"
}

object Subtext1CatppuccinMochaColor extends HoneyBadgerColor {
  override val rgb: (Int, Int, Int) = (186, 194, 222)
  val slug: String = "subtext1-catppuccin-mocha"
}

object Subtext0CatppuccinMochaColor extends HoneyBadgerColor {
  override val rgb: (Int, Int, Int) = (166, 173, 200)
  val slug: String = "subtext0-catppuccin-mocha"
}

object Overlay2CatppuccinMochaColor extends HoneyBadgerColor {
  override val rgb: (Int, Int, Int) = (147, 153, 178)
  val slug: String = "overlay2-catppuccin-mocha"
}

object Overlay1CatppuccinMochaColor extends HoneyBadgerColor {
  override val rgb: (Int, Int, Int) = (127, 132, 156)
  val slug: String = "overlay1-catppuccin-mocha"
}

object Overlay0CatppuccinMochaColor extends HoneyBadgerColor {
  override val rgb: (Int, Int, Int) = (108, 112, 134)
  val slug: String = "overlay0-catppuccin-mocha"
}

object Surface2CatppuccinMochaColor extends HoneyBadgerColor {
  override val rgb: (Int, Int, Int) = (88, 91, 112)
  val slug: String = "surface2-catppuccin-mocha"
}

object Surface1CatppuccinMochaColor extends HoneyBadgerColor {
  override val rgb: (Int, Int, Int) = (69, 71, 90)
  val slug: String = "surface1-catppuccin-mocha"
}

object Surface0CatppuccinMochaColor extends HoneyBadgerColor {
  override val rgb: (Int, Int, Int) = (49, 50, 68)
  val slug: String = "surface0-catppuccin-mocha"
}

object BaseCatppuccinMochaColor extends HoneyBadgerColor {
  override val rgb: (Int, Int, Int) = (30, 30, 46)
  val slug: String = "base-catppuccin-mocha"
}

object MantleCatppuccinMochaColor extends HoneyBadgerColor {
  override val rgb: (Int, Int, Int) = (24, 24, 37)
  val slug: String = "mantle-catppuccin-mocha"
}

object CrustCatppuccinMochaColor extends HoneyBadgerColor {
  override val rgb: (Int, Int, Int) = (17, 17, 27)
  val slug: String = "crust-catppuccin-mocha"
}

object RedCatppuccinLatteColor extends HoneyBadgerColor {
  override val rgb: (Int, Int, Int) = (210, 15, 57)
  val slug: String = "red-catppuccin-latte"
}

object PeachCatppuccinLatteColor extends HoneyBadgerColor {
  override val rgb: (Int, Int, Int) = (254, 100, 11)
  val slug: String = "peach-catppuccin-latte"
}


object MauveCatppuccinLatteColor extends HoneyBadgerColor {
  override val rgb: (Int, Int, Int) = (136, 57, 239)
  val slug: String = "mauve-catppuccin-latte"
}

object BlueCatppuccinLatteColor extends HoneyBadgerColor {
  override val rgb: (Int, Int, Int) = (30, 102, 245)
  val slug: String = "blue-catppuccin-latte"
}