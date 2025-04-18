import model.basicCity
import model.city.Owner
import org.junit.*
import org.junit.Assert.*


class ProvaTest:
  @Test def test(): Unit = assertTrue(true)

  @Test
  def CreatebasicCityTest: Unit =
    val city = basicCity("A", Owner.AI, 3)
    assertTrue(city.getSize == 3)
    assertTrue(city.getName=="A")


end ProvaTest
