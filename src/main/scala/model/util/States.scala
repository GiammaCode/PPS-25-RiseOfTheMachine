package model.util

object States:

  trait Monad[M[_]]:
    def unit[A](a: A): M[A]
    extension[A] (m: M[A])
      def flatMap[B](f: A => M[B]): M[B]
      def map[B](f: A => B): M[B] = m.flatMap(a => unit(f(a)))

  object Monad:
    private def map2[M[_] : Monad, A, B, C](m: M[A], m2:  M[B])(f: (A, B) => C): M[C]=
      m.flatMap(a => m2.map(b => f(a, b)))

    private def seq[M[_] : Monad, A, B](m: M[A], m2: => M[B]): M[B] =
     map2 (m, m2)((a, b) => b)

  object State:
    case class State[S, A](run: S => (S, A))
    extension[S, A] (m: State[S, A])
      def apply(s: S): (S, A) = m match
        case State(run)=> run (s)
      def withFilter(p: A => Boolean): State[S, A] =
        State { s =>
          val (s2, a) = m.run(s)
          if p(a) then (s2, a)
          else throw new MatchError(s"State.withFilter failed: value $a did not satisfy predicate.")
        }

    given stateMonad[S]: Monad[[A] =>> State[S, A]] with
      def unit[A](a: A): State[S, A] = State(s =>(s, a))
      extension[A] (m: State[S, A])
        override def flatMap[B](f: A => State[S, B] ): State[S, B] = State (s => m.apply (s) match
              case (s2, a) => f (a).apply (s2) )



