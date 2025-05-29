package view

/**
 * CLIFormatter is a utility object that provides reusable functions
 * for rendering stylized CLI elements such as boxes, menus, and ASCII titles.
 * It helps enforce visual consistency and reduces duplication in CLI views.
 */
object CLIFormatter:

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

  def printAsciiTitle(text: String): Unit =
    val ascii = com.github.lalyos.jfiglet.FigletFont.convertOneLine(text)
    println(ascii)


