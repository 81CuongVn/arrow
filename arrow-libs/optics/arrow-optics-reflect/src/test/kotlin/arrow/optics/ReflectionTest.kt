package arrow.optics

import arrow.core.test.UnitSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.string

data class Person(val name: String, val friends: List<String>)

sealed interface Cutlery
object Fork: Cutlery
object Spoon: Cutlery

object ReflectionTest: UnitSpec() {
  init {
    "optional for function" {
      checkAll(Arb.list(Arb.int())) { ints ->
        val firsty = { it: List<Int> -> it.firstOrNull() }
        firsty.ogetter.get(ints) shouldBe ints.firstOrNull()
      }
    }

    "lenses for field, get" {
      checkAll(Arb.string(), Arb.list(Arb.string())) { nm, fs ->
        val p = Person(nm, fs.toMutableList())
        Person::name.lens.get(p) shouldBe nm
      }
    }

    "lenses for field, set" {
      checkAll(Arb.string(), Arb.list(Arb.string())) { nm, fs ->
        val p = Person(nm, fs.toMutableList())
        val m = Person::name.lens.modify(p) { it.capitalize() }
        m shouldBe Person(nm.capitalize(), fs)
      }
    }

    "traversal for list, set" {
      checkAll(Arb.string(), Arb.list(Arb.string())) { nm, fs ->
        val p = Person(nm, fs)
        val m = Person::friends.every.modify(p) { it.capitalize() }
        m shouldBe Person(nm, fs.map { it.capitalize() })
      }
    }

    "instances" {
      val things = listOf(Fork, Spoon, Fork)
      val forks = Every.list<Cutlery>() compose instance<Cutlery, Fork>()
      val spoons = Every.list<Cutlery>() compose instance<Cutlery, Spoon>()
      forks.size(things) shouldBe 2
      spoons.size(things) shouldBe 1
    }
  }
}
