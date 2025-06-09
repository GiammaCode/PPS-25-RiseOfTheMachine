/**
 * This module defines a simple Monad typeclass and a custom implementation
 * of the State monad for managing stateful computations in a functional style.
 */
package model.util

object States:

  /**
   * A typeclass representing Monad behavior for any type constructor `M[_]`.
   * A monad must define `unit` and `flatMap`, and optionally `map`.
   */
  trait Monad[M[_]]:
    /**
     * Lifts a value into the monadic context.
     *
     * @param a the value to lift
     * @return a monadic value containing `a`
     */
    def unit[A](a: A): M[A]

    /**
     * Extension methods available on any monadic value `M[A]`.
     */
    extension [A](m: M[A])
      /**
       * Chains a computation that returns a monadic value.
       *
       * @param f a function from A to M[B]
       * @return the result of applying `f` and flattening
       */
      def flatMap[B](f: A => M[B]): M[B]

      /**
       * Transforms the value inside the monad.
       *
       * @param f a function from A to B
       * @return a monad containing the transformed value
       */
      def map[B](f: A => B): M[B] = m.flatMap(a => unit(f(a)))

  object Monad:
    /**
     * Combines two monadic values with a binary function.
     *
     * @param m first monadic value
     * @param m2 second monadic value
     * @param f function to combine values from `m` and `m2`
     * @return combined monadic result
     */
    private def map2[M[_]: Monad, A, B, C](m: M[A], m2: M[B])(f: (A, B) => C): M[C] =
      m.flatMap(a => m2.map(b => f(a, b)))

    /**
     * Sequences two monadic computations, discarding the result of the first.
     *
     * @param m first monadic value
     * @param m2 second monadic computation (lazy)
     * @return the result of the second computation
     */
    private def seq[M[_]: Monad, A, B](m: M[A], m2: => M[B]): M[B] =
      map2(m, m2)((_, b) => b)

  /**
   * Companion object for the custom `State` monad.
   */
  object State:

    /**
     * Represents a stateful computation. It takes an input state `S`
     * and returns a new state along with a result of type `A`.
     *
     * @param run the state transformation function
     */
    case class State[S, A](run: S => (S, A))

    /**
     * Extension methods for the `State` monad.
     */
    extension [S, A](m: State[S, A])
      /**
       * Runs the stateful computation with an initial state.
       *
       * @param s the initial state
       * @return a tuple containing the new state and result
       */
      def apply(s: S): (S, A) = m match
        case State(run) => run(s)

      /**
       * Filters the result of the computation based on a predicate.
       * Throws a `MatchError` if the predicate fails.
       *
       * @param p the predicate to test on the result
       * @return the same computation if it satisfies the predicate
       */
      def withFilter(p: A => Boolean): State[S, A] =
        State { s =>
          val (s2, a) = m.run(s)
          if p(a) then (s2, a)
          else throw MatchError("State withFilter failed")
        }

    /**
     * Given instance of `Monad` for the `State` type constructor.
     */
    given stateMonad[S]: Monad[[A] =>> State[S, A]] with
      /**
       * Lifts a value into a `State` that does not modify the input state.
       */
      def unit[A](a: A): State[S, A] = State(s => (s, a))

      /**
       * Chains stateful computations, threading state through each step.
       */
      extension [A](m: State[S, A])
        override def flatMap[B](f: A => State[S, B]): State[S, B] =
          State(s => m.apply(s) match
            case (s2, a) => f(a).apply(s2)
          )
