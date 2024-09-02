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
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.scala.{ClassTagExtensions, DefaultScalaModule}

import java.io.File

object HoneyBadgerCLIRunner {
  def run(args: List[String]): Unit = {

    val validFiles = args.map[Option[File]](fileName => {
      val file = new File(fileName)

      if (!file.exists()) {
        scribe.error(s"Error! The specified file does not exist or is not accessible: $fileName")
        None
      } else {
        Some(file)
      }
    }).filter(_.isDefined)
      .map(_.get)

    if (validFiles.isEmpty) {
      scribe.error("Error! No suitable tasks were found in the specified files!")
      System.exit(1)
    }

    val mapper = new ObjectMapper(new YAMLFactory())
      .registerModule(DefaultScalaModule)
      .enable(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES) :: ClassTagExtensions

    var userInput: HoneyBadgerUserInputTasks = HoneyBadgerUserInputTasks(None, None)

    validFiles.foreach(file => {
      val tasks = mapper.readValue[HoneyBadgerUserInputTasks](file)

      userInput = HoneyBadgerUserInputTasks(
        leftRightBadgeTasks = Some(userInput.leftRightBadgeTasks.getOrElse(List()).appendedAll(tasks.leftRightBadgeTasks.getOrElse(List()))),
        singleBadgeTasks = Some(userInput.singleBadgeTasks.getOrElse(List()).appendedAll(tasks.singleBadgeTasks.getOrElse(List())))
      )
    })

    scribe.info("HoneyBadger is the one and only Scala CLI badge generator!")

    if (userInput.singleBadgeTasks.isEmpty && userInput.leftRightBadgeTasks.isEmpty) {
      scribe.error("No tasks found in specified files! Please read the file at README.md or README.org to learn how to use this utility!")
      System.exit(1)
    }

    if (userInput.singleBadgeTasks.isDefined) {
      scribe.info(s"Received ${userInput.singleBadgeTasks.get.length} single-part badge tasks! These will be processed concurrently!")
      HoneyBadgerTaskProcessor.singleBadgeToDiskProcessor(userInput.singleBadgeTasks.get)
    }

    if (userInput.leftRightBadgeTasks.isDefined) {
      scribe.info(s"Received ${userInput.leftRightBadgeTasks.get.length} left-right badge tasks! These will be processed concurrently!")
      HoneyBadgerTaskProcessor.leftRightBadgeToDiskProcessor(userInput.leftRightBadgeTasks.get)
    }
  }
}

object Main extends App {
  if (args.length == 0) {
    scribe.error("Error! At least one file with tasks is required to start processing with HoneyBadger!")
    System.exit(1)
  }

  HoneyBadgerCLIRunner.run(args.toList)
}
