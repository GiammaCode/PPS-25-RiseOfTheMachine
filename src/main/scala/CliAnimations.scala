object RiseOfTheMachine extends App {
  val message = "Benvenuto, seleziona la difficoltà:"
  val delay = 150 // millisecondi tra ogni lettera

  val asciiArt =
    """
      |
      |______ _                   __   _   _           ___  ___           _     _
      || ___ (_)                 / _| | | | |          |  \/  |          | |   (_)
      || |_/ /_ ___  ___    ___ | |_  | |_| |__   ___  | .  . | __ _  ___| |__  _ _ __   ___
      ||    /| / __|/ _ \  / _ \|  _| | __| '_ \ / _ \ | |\/| |/ _` |/ __| '_ \| | '_ \ / _ \
      || |\ \| \__ \  __/ | (_) | |   | |_| | | |  __/ | |  | | (_| | (__| | | | | | | |  __/
      |\_| \_|_|___/\___|  \___/|_|    \__|_| |_|\___| \_|  |_/\__,_|\___|_| |_|_|_| |_|\___|
      |
      |
      |""".stripMargin

  def typewriterEffectMultiline(text: String, delay: Int): Unit = {
    for (char <- text) {
      print(char)
      if (char != '\n') Thread.sleep(delay)
    }
  }

  // Call this instead of the original one
  typewriterEffectMultiline(asciiArt, delay = 2)


  // Pulizia iniziale dello schermo
  print("\u001b[2J")
  print("\u001b[H")

  // Avvia animazione
  typewriterEffectMultiline(message, delay)
}
