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

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.{ClassTagExtensions, DefaultScalaModule}
import org.apache.batik.transcoder.image.PNGTranscoder
import org.apache.batik.transcoder.{TranscoderInput, TranscoderOutput}
import org.apache.commons.io.output.ByteArrayOutputStream

import java.awt.font.TextAttribute
import java.awt.geom.RoundRectangle2D
import java.awt.image.BufferedImage
import java.awt.{AlphaComposite, Font, FontMetrics, GraphicsEnvironment, Image, RenderingHints}
import java.io.{ByteArrayInputStream, File, FileInputStream, StringWriter}
import java.text.AttributedCharacterIterator.Attribute
import javax.imageio.ImageIO
import scala.collection.parallel.CollectionConverters._

/** *
 * public void paint(Graphics g) {
 * Graphics2D g2 = (Graphics2D) g;
 *
 * g2.setPaint(Color.RED);
 * g2.setStroke(new BasicStroke(2.0f));
 *
 * double x = 50;
 * double y = 50;
 * double w = x + 250;
 * double h = y + 100;
 * g2.draw(new RoundRectangle2D.Double(x, y, w, h, 50, 50));
 * }
 */


abstract class HoneyBadger {

  def processDesiredColor(color: String, maybeBrightnessModifier: Option[Int]): HoneyBadgerColor = {
    if (maybeBrightnessModifier.isEmpty) {
      HoneyBadgerPaletteUtils.parseColor(color)
    } else {
      HoneyBadgerColor.changeBrightnessModifier(
        HoneyBadgerPaletteUtils.parseColor(color).rgb,
        maybeBrightnessModifier.get
      )
    }
  }

  def writeBufferedImage(bufferedImage: BufferedImage, destinationFileExtension: String, destinationFilePath: String): Unit = {
    scribe.info(s"Writing image to file: $destinationFilePath")
    ImageIO.write(bufferedImage, destinationFileExtension, new File(destinationFilePath))
  }

  def combineLeftAndRightBadges(left: BufferedImage, right: BufferedImage, separatorColor: HoneyBadgerColor, separatorWidth: Double, separatorHeight: Double): BufferedImage = {
    val bufferedImage = new BufferedImage(left.getWidth + right.getWidth, left.getHeight(), BufferedImage.TYPE_INT_ARGB)
    val gfx = bufferedImage.createGraphics()

    // draw left side
    gfx.drawImage(left, 0, 0, null)

    // draw right side
    gfx.drawImage(right, left.getWidth, 0, null)

    // draw a separator, to make the rounded badges look as joined
    gfx.setPaint(separatorColor.toColor)
    gfx.setColor(separatorColor.toColor)
    gfx.fillRect(left.getWidth() - 4, 0, separatorWidth.toInt, separatorHeight.toInt)

    gfx.dispose()
    bufferedImage
  }

  def generateImage(parameters: HoneyBadgerBadgeParameters): BufferedImage = {
    val fallbackFont = new Font(Font.SANS_SERIF, Font.BOLD, parameters.fontSize)
    val badgeFont = if (parameters.customFontFilePath.isEmpty) {
      fallbackFont
    } else {
      loadCustomFont(parameters.customFontFilePath.get, parameters.fontSize).getOrElse(fallbackFont)
    }

    val optionBadgeIcon = if (parameters.iconParameters.isEmpty) {
      None
    } else {
      loadCustomIcon(parameters.iconParameters.get)
    }


    val bufferedImage = new BufferedImage(parameters.width.toInt, parameters.height.toInt, BufferedImage.TYPE_INT_ARGB)
    val gfx = bufferedImage.createGraphics()


    val badgeBackgroundRectX: Double = 0
    val badgeBackgroundRectY: Double = 0

    val badgeBackgroundRect = new RoundRectangle2D.Double(
      badgeBackgroundRectX,
      badgeBackgroundRectY,
      parameters.width,
      parameters.height,
      parameters.arcWidth,
      parameters.arcHeight
    )

    val backgroundColor = processDesiredColor(parameters.backgroundColor, parameters.backgroundColorBrightnessModifier)


    // This is what we want, but it only does hard-clipping, i.e. aliasing
    // g2.setClip(new RoundRectangle2D ...)

    // so instead fake soft-clipping by first drawing the desired clip shape
    // in fully opaque white with antialiasing enabled...
    gfx.setComposite(AlphaComposite.Src)
    gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    gfx.setColor(backgroundColor.toColor)
    gfx.setPaint(backgroundColor.toColor)
    // consider using on-toggle gfx.draw(badgeBackgroundRect)
    gfx.fill(badgeBackgroundRect)

    // atop
    // ... then compositing the image on top,
    // using the white shape from above as alpha source
    gfx.setComposite(AlphaComposite.SrcAtop);

    val fontMetrics = gfx.getFontMetrics(badgeFont)
    val centeredTextCoordinates = calculateCenteredTextCoordinates(parameters.textContents, fontMetrics, badgeBackgroundRect)

    val textColor = HoneyBadgerPaletteUtils.parseColor(parameters.textColor).toColor
    gfx.setColor(textColor)

    gfx.setFont(badgeFont)


    // turn anti-alias ON so as to have smooth font rendering
    //
    // other values may include RenderingHints.VALUE_TEXT_ANTIALIAS_GASP, etc.
    gfx.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR)
    if (optionBadgeIcon.isEmpty) {
      gfx.drawString(parameters.textContents, centeredTextCoordinates._1.toFloat, centeredTextCoordinates._2.toFloat)
    } else {
      val icon = optionBadgeIcon.get
      val iconDrawPositionX = parameters.width / 10
      val iconDrawPositionY = (parameters.height - icon.getHeight(null)) / 2

      val textDrawPositionX: Double = centeredTextCoordinates._1 + (icon.getWidth(null) / 2)

      gfx.drawImage(icon, iconDrawPositionX.toInt, iconDrawPositionY.toInt, null)
      gfx.drawString(parameters.textContents, textDrawPositionX.toFloat, centeredTextCoordinates._2.toFloat)
    }

    gfx.dispose()
    bufferedImage
  }

  private def calculateCenteredTextCoordinates(textContents: String, fontMetrics: FontMetrics, backgroundRect: RoundRectangle2D.Double): (Double, Double) = {
    val x = backgroundRect.getX + (backgroundRect.getWidth - fontMetrics.stringWidth(textContents)) / 2
    val y = backgroundRect.getX + ((backgroundRect.getHeight - fontMetrics.getHeight) / 2) + fontMetrics.getAscent
    (x, y)
  }

  private def loadCustomIcon(parameters: HoneyBadgerIconParameters): Option[Image] = {
    val iconFile = new File(parameters.filePath)
    if (!iconFile.exists()) {
      scribe.error(s"Error! The icon file does not exist or is not accessible: ${parameters.filePath}")
      None
    } else {
      val result = transcodeSVGToBufferedImage(iconFile, parameters.width, parameters.height)
      Some(result)
    }
  }

  private def transcodeSVGToBufferedImage(file: File, width: Int, height: Int): Image = {
    val t = new PNGTranscoder()
    val inputStream = new FileInputStream(file)

    val transcoderInput = new TranscoderInput(inputStream)
    val outputStream = new ByteArrayOutputStream()
    val transcoderOutput = new TranscoderOutput(outputStream)
    t.transcode(transcoderInput, transcoderOutput)
    outputStream.flush()
    outputStream.close()
    inputStream.close()

    val imgData = outputStream.toByteArray
    ImageIO.read(new ByteArrayInputStream(imgData)).getScaledInstance(width, height, Image.SCALE_SMOOTH)
  }

  private def loadCustomFont(customFontFilePath: String, fontSize: Int): Option[Font] = {
    val fontFile = new File(customFontFilePath)
    if (!fontFile.exists()) {
      scribe.error(s"Error! The font file does not exist or is not accessible: $customFontFilePath")
      None
    } else {
      val attributes = new java.util.HashMap[Attribute, Any]
      attributes.put(TextAttribute.TRACKING, 0f)
      attributes.put(TextAttribute.SIZE, fontSize)

      val customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(attributes)
      val ge = GraphicsEnvironment.getLocalGraphicsEnvironment
      ge.registerFont(customFont)

      Some(customFont)
    }
  }
}


object HoneyBadgerTaskProcessor extends HoneyBadger {
  private val mapper = new ObjectMapper()
    .registerModule(DefaultScalaModule)
    .enable(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES) :: ClassTagExtensions

  def singleBadgeToDiskProcessor(tasks: List[HoneyBadgerSingleTask]): Unit = {
    val processingFunction = (task: HoneyBadgerSingleTask) => {
      val out = new StringWriter
      mapper.writeValue(out, task)
      scribe.debug(out.toString)

      val img = generateImage(task.badgeParameters)
      writeBufferedImage(img, task.destinationFileExtension, task.destinationFilePath)
    }

    genericParallelTaskProcessor[HoneyBadgerSingleTask](tasks, processingFunction)
  }

  // genericParallelTaskProcessor takes a generic list of tasks,
  // and the IO function you might want to apply to it
  // and will handle the task processing
  // returning the number of tasks successfully completed
  private def genericParallelTaskProcessor[T](tasks: List[T], processingFunction: T => Unit): Int = {
    val thenNanoTime = Time.now

    tasks.par.foreach(processingFunction)

    val successfulTasks = tasks.length

    val elapsed = Time.timeElapsedFromThenTillNow(thenNanoTime)

    scribe.info(s"HoneyBadger processed $successfulTasks tasks in $elapsed seconds")
    successfulTasks
  }

  def leftRightBadgeToDiskProcessor(tasks: List[HoneyBadgerLeftRightTask]): Unit = {
    val processingFunction = (task: HoneyBadgerLeftRightTask) => {
      val out = new StringWriter
      mapper.writeValue(out, task)
      scribe.debug(out.toString)

      val imgLeft = generateImage(task.leftBadgeParameters)
      val imgRight = generateImage(task.rightBadgeParameters)
      val separatorWidth = 10
      val combined = combineLeftAndRightBadges(
        imgLeft,
        imgRight,
        processDesiredColor(
          task.leftBadgeParameters.backgroundColor,
          task.leftBadgeParameters.backgroundColorBrightnessModifier
        ),
        separatorWidth,
        task.leftBadgeParameters.height
      )

      writeBufferedImage(combined, task.destinationFileExtension, task.destinationFilePath)
    }

    genericParallelTaskProcessor[HoneyBadgerLeftRightTask](tasks, processingFunction)
  }
}
