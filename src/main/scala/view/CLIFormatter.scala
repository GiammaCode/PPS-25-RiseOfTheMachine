package view

import model.map.WorldMapModule.WorldMap

trait Formatter:
  def printBoxedContent(title: String, body: List[String]): Unit
  def printBoxedMenu(title: String, options: List[String]): Unit
  def printAsciiTitle(text: String): Unit
  def printMap(map: WorldMap, conquered: Set[String]): Unit


/**
 * CLIFormatter is a utility trait that provides reusable functions
 * for rendering stylized CLI elements such as boxes, menus, and ASCII titles.
 * It helps enforce visual consistency and reduces duplication in CLI views.
 */
trait CLIFormatter extends Formatter:

  /**
   * Prints a boxed section to the console with a title and multiline body.
   *
   * @param title the title to be displayed at the top of the box
   * @param body  the list of lines to be printed inside the box
   */
  def printBoxedContent(title: String, body: List[String]): Unit  =
    val contentWidth = (title.length :: body.map(_.length)).max + 4
    println("╭" + "─" * contentWidth + "╮")
    println(s"│  $title" + " " * (contentWidth - title.length - 2) + "│")
    println("├" + "─" * contentWidth + "┤")
    body.foreach { line =>
      println(s"│  $line" + " " * (contentWidth - line.length - 2) + "│")
    }
    println("╰" + "─" * contentWidth + "╯")

  /**
   * Prints a menu list with indexed options inside a styled box,
   * and prompts the user to select an option.
   *
   * @param title the title displayed at the top of the menu
   * @param body  the list of menu options to display
   */
  def printBoxedMenu(title: String, body: List[String]): Unit  =
    val width = (title.length :: body.map(_.length + 6)).max + 4
    println("╭" + "─" * width + "╮")
    println(s"│  $title" + " " * (width - title.length - 2) + "│")
    println("├" + "─" * width + "┤")
    body.zipWithIndex.foreach { case (option, i) =>
      val line = f"$i. $option"
      println(s"│  $line" + " " * (width - line.length - 2) + "│")
    }
    println("╰" + "─" * width + "╯")
    print("Insert your choice > ")

  /**
   * Prints a large ASCII art title to the console using the jfiglet font engine.
   *
   * @param text the text to be rendered in ASCII art style
   */
  def printAsciiTitle(text: String): Unit =
    val ascii = com.github.lalyos.jfiglet.FigletFont.convertOneLine(text)
    println(ascii)

  /**
   * Prints a grid of String from a map (WorldMap)
   *
   * @param map worldMap of a cities
   * @param conquered string also contained in map, to change color
   */
  def printMap(map: WorldMap, conquered: Set[String]): Unit =
    val mapString = (0 until map.getSizeOfTheMap).map { y =>
      (0 until map.getSizeOfTheMap).map { x =>
        map.findInMap { case (_, coords) => coords.contains((x, y)) } match
          case Some(city) =>
            val name = city
            if conquered.contains(city) then s"❗"
            else name
          case None => "/"
      }.mkString(" ")
    }.mkString("\n    ")
    println("    " + mapString)

