package io.udash.bootstrap
package alert

import io.udash._
import io.udash.bootstrap.button.UdashButton
import org.scalajs.dom.Element

import scalacss.ScalatagsCss._
import scalatags.JsDom.all._

sealed class UdashAlert private[alert](alertStyle: AlertStyle)(mds: Modifier*) extends UdashBootstrapComponent {

  protected def partial() = div(alertStyle, role := "alert")(mds: _*)

  override lazy val render: Element = partial().render

}

class DismissibleUdashAlert private[alert](alertStyle: AlertStyle)(mds: Modifier*) extends UdashAlert(alertStyle)() {

  def dismiss(): Unit = buttonRendered.click()

  private val _dismissed = Property[Boolean](false)

  val dismissed: ReadableProperty[Boolean] = _dismissed.transform(identity)

  private val button = UdashButton()(
    `type` := "button", BootstrapStyles.close, BootstrapTags.dataDismiss := "alert", aria.label := "close",
    span(aria.hidden := "true")("×")
  )

  button.listen { case ev => _dismissed.set(true) }

  private lazy val buttonRendered = button.render

  override lazy val render: Element = partial()(BootstrapStyles.Alert.alertDismissible)(buttonRendered, mds).render

}


object UdashAlert {

  import AlertStyle._

  private def create(alertStyle: AlertStyle, mds: Modifier*): UdashAlert = new UdashAlert(alertStyle)(mds: _*)

  def success(mds: Modifier*): UdashAlert = create(Success, mds: _*)

  def info(mds: Modifier*): UdashAlert = create(Info, mds: _*)

  def warning(mds: Modifier*): UdashAlert = create(Warning, mds: _*)

  def danger(mds: Modifier*): UdashAlert = create(Danger, mds: _*)

  def dismissible(alertStyle: AlertStyle)(mds: Modifier*): DismissibleUdashAlert = new DismissibleUdashAlert(alertStyle)(mds: _*)
}
