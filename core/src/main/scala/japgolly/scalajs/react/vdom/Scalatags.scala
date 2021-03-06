package japgolly.scalajs.react.vdom

import japgolly.scalajs.react._

private[vdom] object Scalatags {

  def camelCase(dashedString: String) = {
    val first :: rest = dashedString.split("-").toList
    (first :: rest.map(s => s(0).toUpper.toString + s.drop(1))).mkString
  }

  @inline def makeAbstractReactTag[N <: TopNode](tag: String, namespaceConfig: Namespace): ReactTagOf[N] = {
    Escaping.assertValidTag(tag)
    new ReactTagOf[N](tag, Nil, namespaceConfig)
  }

  @inline implicit def stringStyleType =
    ReactStyle.ValueType.string

  implicit class STStringExt(private val s: String) extends AnyVal {
    /**
     * Converts the string to a [[ReactTagOf]]
     */
    @inline def tag[N <: TopNode](implicit namespaceConfig: Namespace): ReactTagOf[N] =
      makeAbstractReactTag(s, namespaceConfig)

    /**
     * Converts the string to a void [[ReactTagOf]]; that means that they cannot
     * contain any content, and can be rendered as self-closing tags.
     */
    @inline def voidTag[N <: TopNode](implicit namespaceConfig: Namespace): ReactTagOf[N] =
      makeAbstractReactTag(s, namespaceConfig)

    /**
     * Converts the string to a [[ReactAttr]]
     */
    @inline def attr = new ReactAttr.Generic(s)

    /**
     * Converts the string to a [[ReactStyle]]. The string is used as the cssName of the
     * style, and the jsName of the style is generated by converted the dashes
     * to camelcase.
     */
    @inline def style = new ReactStyle.Generic(s)
  }

  /**
   * Allows you to modify a [[ReactTagOf]] by adding a Seq containing other nest-able
   * objects to its list of children.
   */
  implicit class SeqNode[A <% TagMod](xs: Seq[A]) extends TagMod {
    def applyTo(t: Builder) = xs.foreach(_.applyTo(t))
  }
}

